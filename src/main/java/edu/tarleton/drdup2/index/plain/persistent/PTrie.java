package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.clones.Clone;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.clones.Pos;
import edu.tarleton.drdup2.index.PIndex;
import edu.tarleton.drdup2.index.plain.Trie;
import edu.tarleton.drdup2.index.plain.TrieEdge;
import edu.tarleton.drdup2.index.plain.TrieNode;
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
 * The class that represents the plain (not compressed) persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PTrie implements AutoCloseable, PIndex {

    private final Storage storage;
    private final PFilePaths filePaths;
    private final PLabels labels;
    private final PNextStmtMap nextStmtMap;

    private PTrie(Storage storage) throws IOException {
        this.storage = storage;
        filePaths = PFilePaths.load(storage);
        labels = PLabels.load(storage);
        nextStmtMap = PNextStmtMap.load(storage);
    }

    public static PTrie initialize(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String nextStmtMapFileName) throws IOException {
        Storage st = Storage.initialize(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                nextStmtMapFileName);
        PNode.reset();
        PEdgeBlock.reset();
        PPosBlock.reset();
        PNode root = new PNode();
        root.writeTo(st);
        PFilePaths.initialize(st);
        PLabels.initialize(st);
        PNextStmtMap.initialize(st);
        return new PTrie(st);
    }

    public static PTrie fromFiles(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String nextStmtMapFileName) throws IOException {
        Storage st = Storage.open(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                nextStmtMapFileName);
        return new PTrie(st);
    }

    @Override
    public void close() throws Exception {
        labels.store(storage);
        if (nextStmtMap != null) {
            nextStmtMap.store(storage);
        }
        storage.close();
    }

    public void addTrie(Trie trie) throws IOException {
        TrieNode root = trie.getRoot();
        root.setPersistentId(0L);
        Deque<TrieNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TrieNode n = queue.remove();
            PNode p = new PNode(n.getPersistentId());
            p.readFrom(storage);
            PEdgeBlock eb = p.getEdgeBlock();
            for (TrieEdge e : n.getEdges()) {
                short labelId = (short) labels.toLabelId(e.getLabel());
                PEdge pe = eb.findEdge(labelId);
                if (pe == null) {
                    PNode dest = new PNode();
                    dest.writeTo(storage);
                    pe = new PEdge(labelId, dest.getId());
                    eb.addEdge(pe);
                }
                for (Pos pos : e.getPositions()) {
                    long fileId = filePaths.toFileId(storage, pos.getFile());
                    PPos pp = new PPos(fileId, pos.getStart(), pos.getEnd());
                    pe.addPos(pp);
                }
                TrieNode nn = e.getDestination();
                nn.setPersistentId(pe.getDestId());
                queue.add(nn);
            }
            p.writeTo(storage);
        }
        if (nextStmtMap != null) {
            nextStmtMap.addNextStmtMap(storage, trie.getNextStmtMap());
        }
    }

    @Override
    public void print() throws IOException {
        PNode n = new PNode(0L);
        n.readFrom(storage);
        Deque<PNode> queue = new ArrayDeque<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            PNode p = queue.remove();
            p.print(labels);
            PEdgeBlock eb = p.getEdgeBlock();
            while (eb != null) {
                PEdge[] ee = eb.getEdges();
                for (int i = 0; i < eb.getEdgeCount(); i++) {
                    PEdge e = ee[i];
                    PNode q = new PNode(e.getDestId());
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
    public CloneSet detectClonesType2(String level, int minSize, int maxSize) throws IOException {
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

    public CloneSet detectMethodClonesType2(int minSize, int maxSize) throws IOException {
        CloneSet cloneSet = new CloneSet();
        PNode n = new PNode(0L);
        n.readFrom(storage);
        Deque<PNode> queue = new ArrayDeque<>();
        Deque<PEdge> edges = new ArrayDeque<>();
        PEdgeBlock eb = n.getEdgeBlock();
        short methodDeclId = (short) labels.toLabelId("MethodDeclaration");
        PEdge e = eb.findEdge(methodDeclId);
        if (e != null) {
            PNode dest = new PNode(e.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e);
        }
        short constDeclId = (short) labels.toLabelId("ConstructorDeclaration");
        PEdge e2 = eb.findEdge(constDeclId);
        if (e2 != null) {
            PNode dest = new PNode(e2.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e2);
        }
        short initDeclId = (short) labels.toLabelId("InitializerDeclaration");
        PEdge e3 = eb.findEdge(initDeclId);
        if (e3 != null) {
            PNode dest = new PNode(e3.getDestId());
            dest.readFrom(storage);
            queue.add(dest);
            edges.add(e3);
        }
        while (!queue.isEmpty()) {
            PNode p = queue.remove();
            PEdge iedge = edges.remove();
            PEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    PPos[] pp = iedge.getPosBlock().getPositions(minSize, maxSize);
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cloneSet.addClone(cl);
                    }
                } else {
                    PEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        PEdge edge = ee[i];
                        PNode q = new PNode(edge.getDestId());
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

    private Pos[] toFilePos(PPos[] pp) {
        Map<Long, String> map = filePaths.getInverseMap();
        Pos[] fpp = new Pos[pp.length];
        for (int i = 0; i < pp.length; i++) {
            PPos p = pp[i];
            String fn = map.get(p.getFileId());
            fpp[i] = new Pos(fn, p.getBegin(), p.getEnd());
        }
        return fpp;
    }

    private CloneSet detectStmtClonesType2(int minSize, int maxSize) throws IOException {
        CloneSet cloneSet = new CloneSet();
        PNode n = new PNode(0L);
        n.readFrom(storage);
        Deque<PNode> queue = new ArrayDeque<>();
        Deque<PEdge> edges = new ArrayDeque<>();
        PEdgeBlock eb = n.getEdgeBlock();
        for (String stmt : STATEMENTS) {
            short stmtId = (short) labels.toLabelId(stmt);
            PEdge edge = eb.findEdge(stmtId);
            if (edge != null) {
                PNode dest = new PNode(edge.getDestId());
                dest.readFrom(storage);
                queue.add(dest);
                edges.add(edge);
            }
        }
        while (!queue.isEmpty()) {
            PNode p = queue.remove();
            PEdge edge = edges.remove();
            PEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    PPos[] pp = edge.getPosBlock().getPositions(minSize, maxSize);
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cloneSet.addClone(cl);
                    }
                } else {
                    PEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        PEdge e = ee[i];
                        PNode q = new PNode(e.getDestId());
                        q.readFrom(storage);
                        queue.add(q);
                        edges.add(e);
                    }
                }
                block = block.getNext();
            }
        }
        return cloneSet;
    }

    private CloneSet detectMergedStmtClonesType2(int minSize, int maxSize) throws IOException {
        List<Clone> cls = new ArrayList<>();
        PNode n = new PNode(0L);
        n.readFrom(storage);
        Deque<PNode> queue = new ArrayDeque<>();
        Deque<PEdge> edges = new ArrayDeque<>();
        PEdgeBlock eb = n.getEdgeBlock();
        for (String stmt : STATEMENTS) {
            short stmtId = (short) labels.toLabelId(stmt);
            PEdge edge = eb.findEdge(stmtId);
            if (edge != null) {
                PNode dest = new PNode(edge.getDestId());
                dest.readFrom(storage);
                queue.add(dest);
                edges.add(edge);
            }
        }
        while (!queue.isEmpty()) {
            PNode p = queue.remove();
            PEdge edge = edges.remove();
            PEdgeBlock block = p.getEdgeBlock();
            while (block != null) {
                if (block.getEdgeCount() == 0) {
                    PPos[] pp = edge.getPosBlock().getPositions();
                    if (pp.length > 1) {
                        Pos[] fpp = toFilePos(pp);
                        Clone cl = new Clone(100, fpp);
                        cls.add(cl);
                    }
                } else {
                    PEdge[] ee = block.getEdges();
                    for (int i = 0; i < block.getEdgeCount(); i++) {
                        PEdge e = ee[i];
                        PNode q = new PNode(e.getDestId());
                        q.readFrom(storage);
                        queue.add(q);
                        edges.add(e);
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
        PNode n = new PNode(0L);
        n.readFrom(storage);
        Deque<PNode> queue = new ArrayDeque<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            PNode p = queue.remove();
            int edges = 0;
            PEdgeBlock eb = p.getEdgeBlock();
            while (eb != null) {
                PEdge[] ee = eb.getEdges();
                for (int i = 0; i < eb.getEdgeCount(); i++) {
                    PEdge e = ee[i];
                    PPosBlock pb = e.getPosBlock();
                    if (pb != null) {
                        int c = pb.countPositions();
                        hist.storeEdge(c);
                    }
                    PNode q = new PNode(e.getDestId());
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
