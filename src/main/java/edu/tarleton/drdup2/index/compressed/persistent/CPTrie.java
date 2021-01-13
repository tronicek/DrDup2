package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.clones.Clone;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.clones.Pos;
import edu.tarleton.drdup2.index.PIndex;
import edu.tarleton.drdup2.index.compressed.CTrie;
import edu.tarleton.drdup2.index.compressed.CTrieEdge;
import edu.tarleton.drdup2.index.compressed.CTrieNode;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * The representation of the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPTrie implements AutoCloseable, PIndex {

    private final Storage storage;
    private final CPFilePaths filePaths;
    private final CPLinearizations linearizations;
    private final CPNextStmtMap nextStmtMap;

    private CPTrie(Storage storage) throws IOException {
        this.storage = storage;
        filePaths = CPFilePaths.load(storage);
        linearizations = CPLinearizations.load(storage);
        nextStmtMap = CPNextStmtMap.load(storage);
    }

    public static CPTrie initialize(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String linearizationFileName, String nextStmtMapFileName) throws IOException {
        Storage st = Storage.initialize(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                linearizationFileName, nextStmtMapFileName);
        CPNode.reset();
        CPEdgeBlock.reset();
        CPPosBlock.reset();
        CPNode root = new CPNode();
        root.writeTo(st);
        CPFilePaths.initialize(st);
        CPLinearizations.initialize(st);
        return new CPTrie(st);
    }

    public static CPTrie fromFiles(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String linearizationFileName, String nextStmtMapFileName) throws IOException {
        Storage st = Storage.open(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                linearizationFileName, nextStmtMapFileName);
        return new CPTrie(st);
    }

    @Override
    public void close() throws Exception {
        linearizations.store(storage);
        if (nextStmtMap != null) {
            nextStmtMap.store(storage);
        }
        storage.close();
    }

    public void addTrie(CTrie trie) throws IOException {
        int shift = linearizations.getBufferSize();
        linearizations.extendBuffer(trie.getBuffer());
        CTrieNode root = trie.getRoot();
        Deque<CTrieNode> queue = new ArrayDeque<>();
        queue.add(root);
        Deque<Long> queue2 = new ArrayDeque<>();
        queue2.add(0L);
        while (!queue.isEmpty()) {
            CTrieNode n = queue.remove();
            Long nodeId = queue2.remove();
            CPNode p = new CPNode(nodeId);
            p.readFrom(storage);
            CPEdgeBlock eb = p.getEdgeBlock();
            for (CTrieEdge e : n.getEdges()) {
                List<Integer> plin = linearizations.getBuffer();
                Integer first = plin.get(e.getStart() + shift);
                CPEdge pe = eb.findEdge(first, linearizations.getBuffer());
                if (pe == null) {
                    CPNode dest = new CPNode();
                    dest.writeTo(storage);
                    pe = new CPEdge(e.getStart() + shift, e.getEnd() + shift, dest.getId());
                    eb.addEdge(pe);
                    queue.add(e.getDestination());
                    queue2.add(dest.getId());
                } else {
                    List<Integer> curLab = linearizations.getBuffer(pe.getStart(), pe.getEnd());
                    List<Integer> newLab = plin.subList(e.getStart() + shift, e.getEnd() + shift);
                    int pref = commonPrefix(curLab, newLab);
                    CPNode dest;
                    if (pref == curLab.size()) {
                        dest = new CPNode(pe.getDestId());
                        dest.readFrom(storage);
                    } else {
                        CPEdge pe2 = pe.makeClone();
                        pe2.setStart(pe.getStart() + pref);
                        dest = new CPNode();
                        CPEdgeBlock eb2 = dest.getEdgeBlock();
                        eb2.addEdge(pe2);
                        pe.setEnd(pe.getStart() + pref);
                        pe.setDestId(dest.getId());
                        dest.writeTo(storage);
                    }
                    CTrieNode temp;
                    if (pref == newLab.size()) {
                        temp = e.getDestination();
                    } else {
                        temp = new CTrieNode();
                        CTrieEdge tempEdge = e.makeClone();
                        tempEdge.setStart(e.getStart() + pref);
                        temp.addEdge(tempEdge);
                    }
                    queue.add(temp);
                    queue2.add(dest.getId());
                }
                for (Pos pos : e.getPositions()) {
                    long fileId = filePaths.toFileId(storage, pos.getFile());
                    CPPos pp = new CPPos(fileId, pos.getStart(), pos.getEnd());
                    pe.addPos(pp);
                }
                p.writeTo(storage);
            }
        }
        if (nextStmtMap != null) {
            nextStmtMap.addNextStmtMap(storage, trie.getNextStmtMap());
        }
    }

    private int commonPrefix(List<Integer> lab1, List<Integer> lab2) {
        int n = Math.min(lab1.size(), lab2.size());
        int i = 0;
        while (i < n) {
            Integer a = lab1.get(i);
            Integer b = lab2.get(i);
            if (!a.equals(b)) {
                return i;
            }
            i++;
        }
        return i;
    }

    @Override
    public void print() throws IOException {
        CPNode n = new CPNode(0L);
        n.readFrom(storage);
        Deque<CPNode> queue = new ArrayDeque<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            CPNode p = queue.remove();
            p.print(linearizations);
            CPEdgeBlock eb = p.getEdgeBlock();
            while (eb != null) {
                CPEdge[] ee = eb.getEdges();
                for (int i = 0; i < eb.getEdgeCount(); i++) {
                    CPEdge e = ee[i];
                    CPNode q = new CPNode(e.getDestId());
                    q.readFrom(storage);
                    queue.add(q);
                }
                eb = eb.getNext();
            }
        }
        System.out.println("---------------------");
        filePaths.print(storage);
        System.out.println("---------------------");
        if (nextStmtMap != null) {
            nextStmtMap.print(storage);
            System.out.println("---------------------");
        }
    }

    @Override
    public CloneSet detectClonesType2(String level, int minSize, int maxSize) throws Exception {
        switch (level) {
            case "method":
                return detectMethodClonesType2(minSize, maxSize);
            case "statement":
                return detectStmtClonesType2(minSize, maxSize);
            case "statements":
                return detectMergedStmtClonesType2(minSize, maxSize);
            default:
                throw new AssertionError("invalid level: " + level);
        }
    }

    private CloneSet detectMethodClonesType2(int minSize, int maxSize) throws IOException {
        CloneSet cloneSet = new CloneSet();
        CPNode n = new CPNode(0L);
        n.readFrom(storage);
        Deque<CPNode> queue = new ArrayDeque<>();
        Deque<CPEdge> edges = new ArrayDeque<>();
        CPEdgeBlock eb = n.getEdgeBlock();
        int methodDeclId = linearizations.findLabel("MethodDeclaration");
        CPEdge e = eb.findEdge(methodDeclId, linearizations.getBuffer());
        if (e != null) {
            CPNode dest = new CPNode(e.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e);
        }
        int constDeclId = linearizations.findLabel("ConstructorDeclaration");
        CPEdge e2 = eb.findEdge(constDeclId, linearizations.getBuffer());
        if (e2 != null) {
            CPNode dest = new CPNode(e2.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e2);
        }
        int initDeclId = linearizations.findLabel("InitializerDeclaration");
        CPEdge e3 = eb.findEdge(initDeclId, linearizations.getBuffer());
        if (e3 != null) {
            CPNode dest = new CPNode(e3.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e3);
        }
        while (!queue.isEmpty()) {
            CPNode p = queue.remove();
            CPEdge iedge = edges.remove();
            CPEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    CPPos[] pp = iedge.getPosBlock().getPositions(minSize, maxSize);
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cloneSet.addClone(cl);
                    }
                } else {
                    CPEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        CPEdge edge = ee[i];
                        CPNode q = new CPNode(edge.getDestId());
                        q.readFrom(storage);
                        queue.add(q);
                        edges.add(edge);
                    }
                }
                block = block.getNext();
            }
        }
        return cloneSet;
    }

    private Pos[] toFilePos(CPPos[] pp) {
        Map<Long, String> map = filePaths.getInverseMap();
        Pos[] fpp = new Pos[pp.length];
        for (int i = 0; i < pp.length; i++) {
            CPPos p = pp[i];
            String fn = map.get(p.getFileId());
            fpp[i] = new Pos(fn, p.getBegin(), p.getEnd());
        }
        return fpp;
    }

    private CloneSet detectStmtClonesType2(int minSize, int maxSize) throws IOException {
        CloneSet cloneSet = new CloneSet();
        CPNode n = new CPNode(0L);
        n.readFrom(storage);
        Deque<CPNode> queue = new ArrayDeque<>();
        Deque<CPEdge> edges = new ArrayDeque<>();
        CPEdgeBlock eb = n.getEdgeBlock();
        for (String stmt : STATEMENTS) {
            int stmtId = linearizations.findLabel(stmt);
            CPEdge e = eb.findEdge(stmtId, linearizations.getBuffer());
            if (e != null) {
                CPNode dest = new CPNode(e.getDestId());
                dest.readFrom(storage);
                queue.add(dest);
                edges.add(e);
            }
        }
        while (!queue.isEmpty()) {
            CPNode p = queue.remove();
            CPEdge iedge = edges.remove();
            CPEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    CPPos[] pp = iedge.getPosBlock().getPositions(minSize, maxSize);
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cloneSet.addClone(cl);
                    }
                } else {
                    CPEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        CPEdge edge = ee[i];
                        CPNode q = new CPNode(edge.getDestId());
                        q.readFrom(storage);
                        queue.add(q);
                        edges.add(edge);
                    }
                }
                block = block.getNext();
            }
        }
        return cloneSet;
    }

    private CloneSet detectMergedStmtClonesType2(int minSize, int maxSize) throws IOException {
        List<Clone> cls = new ArrayList<>();
        CPNode n = new CPNode(0L);
        n.readFrom(storage);
        Deque<CPNode> queue = new ArrayDeque<>();
        Deque<CPEdge> edges = new ArrayDeque<>();
        CPEdgeBlock eb = n.getEdgeBlock();
        for (String stmt : STATEMENTS) {
            int stmtId = linearizations.findLabel(stmt);
            CPEdge e = eb.findEdge(stmtId, linearizations.getBuffer());
            if (e != null) {
                CPNode dest = new CPNode(e.getDestId());
                dest.readFrom(storage);
                queue.add(dest);
                edges.add(e);
            }
        }
        while (!queue.isEmpty()) {
            CPNode p = queue.remove();
            CPEdge iedge = edges.remove();
            CPEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    CPPos[] pp = iedge.getPosBlock().getPositions();
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cls.add(cl);
                    }
                } else {
                    CPEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        CPEdge edge = ee[i];
                        CPNode q = new CPNode(edge.getDestId());
                        q.readFrom(storage);
                        queue.add(q);
                        edges.add(edge);
                    }
                }
                block = block.getNext();
            }
        }
        return merge(cls, minSize, maxSize);
    }

    private CloneSet merge(List<Clone> cls, int minSize, int maxSize) {
        List<Clone> cls2 = new ArrayList<>();
        Map<Pos, Set<Pos>> posMap = createPosMap(cls);
        for (Clone cl : cls) {
            Pos[] pp = cl.getPositions();
            Set<Integer> merged = new TreeSet<>();
            for (int i = 0; i < pp.length; i++) {
                if (merged.contains(i)) {
                    continue;
                }
                Pos next = nextStmtMap.getNext(pp[i]);
                Set<Pos> set = posMap.get(next);
                if (set == null) {
                    continue;
                }
                Set<Integer> inds = new TreeSet<>();
                for (int j = i + 1; j < pp.length; j++) {
                    Pos next2 = nextStmtMap.getNext(pp[j]);
                    if (set.contains(next2)) {
                        inds.add(j);
                    }
                }
                if (inds.isEmpty()) {
                    continue;
                }
                Pos[] np = new Pos[inds.size() + 1];
                np[0] = new Pos(pp[i].getFile(), pp[i].getStart(), next.getEnd());
                int k = 1;
                for (Integer j : inds) {
                    Pos next2 = nextStmtMap.getNext(pp[j]);
                    np[k] = new Pos(pp[j].getFile(), pp[j].getStart(), next2.getEnd());
                    k++;
                }
                Clone clone = new Clone(100, np);
                cls2.add(clone);
                merged.add(i);
                merged.addAll(inds);
            }
            if (merged.size() < pp.length) {
                cls2.add(cl);
            }
        }
        List<Clone> cls3 = new ArrayList<>();
        for (Clone cl : cls2) {
            Pos[] pp = cl.getPositions();
            Pos[] rr = positions(pp, minSize, maxSize);
            if (rr.length > 1) {
                Clone clone = new Clone(100, rr);
                cls3.add(clone);
            }
        }
        List<Clone> cls4 = removeDuplicates(cls3);
        CloneSet clones = new CloneSet();
        clones.addClones(cls4);
        return clones;
    }

    private Map<Pos, Set<Pos>> createPosMap(List<Clone> clones) {
        Map<Pos, Set<Pos>> map = new HashMap<>();
        for (Clone cl : clones) {
            for (Pos p : cl.getPositions()) {
                Set<Pos> pset = map.get(p);
                if (pset == null) {
                    pset = new HashSet<>();
                    map.put(p, pset);
                }
                for (Pos p2 : cl.getPositions()) {
                    if (p2 == p) {
                        continue;
                    }
                    pset.add(p2);
                }
            }
        }
        return map;
    }

    private Pos[] positions(Pos[] positions, int minSize, int maxSize) {
        List<Pos> pp = new ArrayList<>();
        for (Pos p : positions) {
            long size = p.getLines();
            if (minSize <= size && size <= maxSize) {
                pp.add(p);
            }
        }
        Pos[] rr = new Pos[pp.size()];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = pp.get(i);
        }
        return rr;
    }

    private List<Clone> removeDuplicates(List<Clone> clones) {
        Map<Clone, Integer> numMap = new HashMap<>();
        Map<Integer, Clone> revNumMap = new HashMap<>();
        for (int i = 0; i < clones.size(); i++) {
            Clone cl = clones.get(i);
            numMap.put(cl, i);
            revNumMap.put(i, cl);
        }
        Map<String, Set<Integer>> fileMap = new HashMap<>();
        for (Clone cl : clones) {
            Integer i = numMap.get(cl);
            for (Pos pos : cl.getPositions()) {
                String file = pos.getFile();
                Set<Integer> cls = fileMap.get(file);
                if (cls == null) {
                    cls = new HashSet<>();
                    fileMap.put(file, cls);
                }
                cls.add(i);
            }
        }
        List<Clone> subs = new ArrayList<>();
        for (Clone cl : clones) {
            Pos[] pp = cl.getPositions();
            String file = pp[0].getFile();
            Set<Integer> cls = fileMap.get(file);
            for (int i = 1; i < pp.length; i++) {
                String file2 = pp[i].getFile();
                Set<Integer> cls2 = fileMap.get(file2);
                cls.retainAll(cls2);
            }
            Integer i = numMap.get(cl);
            for (Integer j : cls) {
                if (i.equals(j)) {
                    continue;
                }
                Clone cl2 = revNumMap.get(j);
                if (cl.subsetOf(cl2)) {
                    subs.add(cl);
                    break;
                }
            }
        }
        List<Clone> clones2 = new ArrayList<>();
        for (Clone cl : clones) {
            if (!subs.contains(cl)) {
                clones2.add(cl);
            }
        }
        return clones2;
    }
    
    @Override
    public Histogram createHistogram() throws IOException {
        Histogram hist = new Histogram();
        CPNode n = new CPNode(0L);
        n.readFrom(storage);
        Deque<CPNode> queue = new ArrayDeque<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            CPNode p = queue.remove();
            int edges = 0;
            CPEdgeBlock eb = p.getEdgeBlock();
            while (eb != null) {
                CPEdge[] ee = eb.getEdges();
                for (int i = 0; i < eb.getEdgeCount(); i++) {
                    CPEdge e = ee[i];
                    CPPosBlock pb = e.getPosBlock();
                    if (pb != null) {
                        int c = pb.countPositions();
                        hist.storeEdge(c);
                    }
                    CPNode q = new CPNode(e.getDestId());
                    q.readFrom(storage);
                    queue.add(q);
                }
                edges += eb.getEdgeCount();
                eb = eb.getNext();
            }
            hist.storeNode(edges);
        }
        return hist;
    }
}
