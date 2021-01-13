package edu.tarleton.drdup2.index.plain;

import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.clones.Clone;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.clones.Pos;
import edu.tarleton.drdup2.index.Index;
import java.io.Serializable;
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
 * The class that represents the TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Trie implements Index, Serializable {

    private static final long serialVersionUID = 1L;
    private final TrieNode root = new TrieNode();
    private final Map<Pos, Pos> nextStmtMap = new HashMap<>();

    public TrieNode getRoot() {
        return root;
    }

    public Map<Pos, Pos> getNextStmtMap() {
        return nextStmtMap;
    }

    @Override
    public void nextStmt(Pos prev, Pos curr) {
        nextStmtMap.put(prev, curr);
    }

    @Override
    public void add(List<String> labels, Pos pos) {
        TrieNode p = root;
        int lastIndex = labels.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            String lab = labels.get(i);
            p = p.addChild(lab, null);
        }
        p.addChild(labels.get(lastIndex), pos);
    }

    @Override
    public void print() {
        root.print();
    }

    @Override
    public CloneSet detectClonesType2(String level, int minSize, int maxSize) {
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

    private CloneSet detectMethodClonesType2(int minSize, int maxSize) {
        CloneSet clones = new CloneSet();
        List<TrieNode> nodes = new ArrayList<>();
        List<Pos[]> positions = new ArrayList<>();
        TrieEdge edge = root.findEdge("MethodDeclaration");
        if (edge != null) {
            nodes.add(edge.getDestination());
            positions.add(edge.getPositions());
        }
        TrieEdge edge2 = root.findEdge("ConstructorDeclaration");
        if (edge2 != null) {
            nodes.add(edge2.getDestination());
            positions.add(edge2.getPositions());
        }
        TrieEdge edge3 = root.findEdge("InitializerDeclaration");
        if (edge3 != null) {
            nodes.add(edge3.getDestination());
            positions.add(edge3.getPositions());
        }
        while (!nodes.isEmpty()) {
            TrieNode node = nodes.remove(0);
            Pos[] pp = positions.remove(0);
            if (node.isLeaf()) {
                Pos[] rr = positions(pp, minSize, maxSize);
                if (rr.length > 1) {
                    Clone clone = new Clone(100, rr);
                    clones.addClone(clone);
                }
                continue;
            }
            for (TrieEdge e : node.getEdges()) {
                nodes.add(e.getDestination());
                positions.add(e.getPositions());
            }
        }
        return clones;
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

    private CloneSet detectStmtClonesType2(int minSize, int maxSize) {
        CloneSet clones = new CloneSet();
        List<TrieNode> nodes = new ArrayList<>();
        List<Pos[]> positions = new ArrayList<>();
        for (String stmt : STATEMENTS) {
            TrieEdge edge = root.findEdge(stmt);
            if (edge != null) {
                nodes.add(edge.getDestination());
                positions.add(edge.getPositions());
            }
        }
        while (!nodes.isEmpty()) {
            TrieNode node = nodes.remove(0);
            Pos[] pp = positions.remove(0);
            if (node.isLeaf()) {
                Pos[] rr = positions(pp, minSize, maxSize);
                if (rr.length > 1) {
                    Clone clone = new Clone(100, rr);
                    clones.addClone(clone);
                }
                continue;
            }
            for (TrieEdge e : node.getEdges()) {
                nodes.add(e.getDestination());
                positions.add(e.getPositions());
            }
        }
        return clones;
    }

    private CloneSet detectMergedStmtClonesType2(int minSize, int maxSize) {
        List<Clone> cls = new ArrayList<>();
        List<TrieNode> nodes = new ArrayList<>();
        List<Pos[]> positions = new ArrayList<>();
        for (String stmt : STATEMENTS) {
            TrieEdge edge = root.findEdge(stmt);
            if (edge != null) {
                nodes.add(edge.getDestination());
                positions.add(edge.getPositions());
            }
        }
        while (!nodes.isEmpty()) {
            TrieNode node = nodes.remove(0);
            Pos[] pp = positions.remove(0);
            if (node.isLeaf()) {
                if (pp.length > 1) {
                    Clone clone = new Clone(100, pp);
                    cls.add(clone);
                }
                continue;
            }
            for (TrieEdge e : node.getEdges()) {
                nodes.add(e.getDestination());
                positions.add(e.getPositions());
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
                Pos next = nextStmtMap.get(pp[i]);
                Set<Pos> set = posMap.get(next);
                if (set == null) {
                    continue;
                }
                Set<Integer> inds = new TreeSet<>();
                for (int j = i + 1; j < pp.length; j++) {
                    Pos next2 = nextStmtMap.get(pp[j]);
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
                    Pos next2 = nextStmtMap.get(pp[j]);
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
    public Histogram createHistogram() {
        Histogram hist = new Histogram();
        Deque<TrieNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TrieNode p = queue.remove();
            TrieEdge[] edges = p.getEdges();
            for (TrieEdge e : edges) {
                Pos[] pp = e.getPositions();
                hist.storeEdge(pp.length);
            }
            hist.storeNode(edges.length);
        }
        return hist;
    }
}
