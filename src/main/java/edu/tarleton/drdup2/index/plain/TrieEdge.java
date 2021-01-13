package edu.tarleton.drdup2.index.plain;

import edu.tarleton.drdup2.clones.Pos;
import java.io.Serializable;

/**
 * The class that represents the TRIE edge.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class TrieEdge implements Comparable<TrieEdge>, Serializable {

    private static final long serialVersionUID = 1L;
    private static int count;
    private final String label;
    private final TrieNode destination;
    private Pos[] positions = new Pos[1];
    private int positionsCount;

    public TrieEdge(String label, TrieNode destination) {
        this.label = label;
        this.destination = destination;
        count++;
    }

    public static int getCount() {
        return count;
    }

    public String getLabel() {
        return label;
    }

    public TrieNode getDestination() {
        return destination;
    }

    public Pos[] getPositions() {
        if (positionsCount == positions.length) {
            return positions;
        }
        Pos[] pp = new Pos[positionsCount];
        System.arraycopy(positions, 0, pp, 0, positionsCount);
        return pp;
    }

    public void addPosition(Pos position) {
        if (positionsCount == positions.length) {
            Pos[] pp = new Pos[positions.length * 2];
            System.arraycopy(positions, 0, pp, 0, positions.length);
            positions = pp;
        }
        positions[positionsCount] = position;
        positionsCount++;
    }

    @Override
    public int compareTo(TrieEdge that) {
        return label.compareTo(that.label);
    }

    public void print() {
        System.out.printf("  edge: %s, destination: %d%n", label, destination.getNum());
        for (int i = 0; i < positionsCount; i++) {
            System.out.printf("    position: %s%n", positions[i]);
        }
    }
}
