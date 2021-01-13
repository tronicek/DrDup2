package edu.tarleton.drdup2.index.compressed;

import java.io.Serializable;
import java.util.List;

/**
 * The node of the compressed TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CTrieNode implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int count;
    private final int num;
    private CTrieEdge[] edges = new CTrieEdge[1];
    private int edgesCount;

    public CTrieNode() {
        num = count++;
    }

    public static int getCount() {
        return count;
    }

    public boolean isLeaf() {
        return edgesCount == 0;
    }

    public CTrieEdge[] getEdges() {
        if (edgesCount == edges.length) {
            return edges;
        }
        CTrieEdge[] ee = new CTrieEdge[edgesCount];
        System.arraycopy(edges, 0, ee, 0, edgesCount);
        return ee;
    }

    public CTrieEdge findEdge(String label) {
        for (int i = 0; i < edgesCount; i++) {
            List<String> elab = edges[i].getLabel();
            String s = elab.get(0);
            if (s.equals(label)) {
                return edges[i];
            }
        }
        return null;
    }

    public CTrieEdge addEdge(List<String> linearization, int ind) {
        CTrieNode dst = new CTrieNode();
        CTrieEdge e = new CTrieEdge(linearization, ind, ind + 1, dst);
        addEdge(e);
        return e;
    }

    public CTrieEdge addEdge(List<String> linearization, int start, int end) {
        CTrieNode dst = new CTrieNode();
        CTrieEdge e = new CTrieEdge(linearization, start, end, dst);
        addEdge(e);
        return e;
    }

    public void addEdge(CTrieEdge e) {
        if (edgesCount == edges.length) {
            CTrieEdge[] ee = new CTrieEdge[edges.length * 2];
            System.arraycopy(edges, 0, ee, 0, edges.length);
            edges = ee;
        }
        edges[edgesCount] = e;
        edgesCount++;
    }

    public int getNum() {
        return num;
    }

    public void print(String label) {
        System.out.printf("node %d%n", num);
        for (int i = 0; i < edgesCount; i++) {
            List<String> labs = edges[i].getLabel();
            String lab = labs.get(0);
            if (!lab.equals(label)) {
                continue;
            }
            edges[i].print();
        }
        for (int i = 0; i < edgesCount; i++) {
            List<String> labs = edges[i].getLabel();
            String lab = labs.get(0);
            if (!lab.equals(label)) {
                continue;
            }
            CTrieNode dst = edges[i].getDestination();
            dst.print();
        }
    }

    public void print() {
        System.out.printf("node %d%n", num);
        for (int i = 0; i < edgesCount; i++) {
            edges[i].print();
        }
        for (int i = 0; i < edgesCount; i++) {
            CTrieNode dst = edges[i].getDestination();
            dst.print();
        }
    }

    public void printNode(int num) {
        if (this.num == num) {
            System.out.println("---------------------");
            System.out.printf("node %d%n", num);
            for (int i = 0; i < edgesCount; i++) {
                edges[i].print();
            }
            System.out.println("---------------------");
        }
        for (int i = 0; i < edgesCount; i++) {
            CTrieNode dst = edges[i].getDestination();
            dst.printNode(num);
        }
    }

    @Override
    public String toString() {
        return String.format("node %d", num);
    }
}
