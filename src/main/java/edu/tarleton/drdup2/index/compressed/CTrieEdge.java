package edu.tarleton.drdup2.index.compressed;

import edu.tarleton.drdup2.clones.Pos;
import java.io.Serializable;
import java.util.List;

/**
 * The edge of the compressed TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CTrieEdge implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int count;
    private final List<String> linearization;
    private int start;
    private int end;
    private CTrieNode destination;
    private Pos[] positions = new Pos[1];
    private int positionsCount;

    public CTrieEdge(List<String> linearization, int start, int end, CTrieNode destination) {
        this.linearization = linearization;
        this.start = start;
        this.end = end;
        this.destination = destination;
        count++;
        assert start >= 0;
        assert start < end;
    }

    public static int getCount() {
        return count;
    }

    public List<String> getLinearization() {
        return linearization;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void incEnd() {
        end++;
    }

    public List<String> getLabel() {
        return linearization.subList(start, end);
    }

    public CTrieNode getDestination() {
        return destination;
    }

    public void setDestination(CTrieNode destination) {
        this.destination = destination;
    }

    public Pos[] getPositions() {
        if (positionsCount == positions.length) {
            return positions;
        }
        Pos[] pp = new Pos[positionsCount];
        System.arraycopy(positions, 0, pp, 0, positionsCount);
        return pp;
    }
    
    public Pos[] removePositions() {
        Pos[] pp = getPositions();
        positions = new Pos[1];
        positionsCount = 0;
        return pp;
    }

    public void addPosition(Pos position) {
        if (positionsCount > 0 && positions[positionsCount - 1].equals(position)) {
            return;
        }
        if (positionsCount == positions.length) {
            Pos[] pp = new Pos[positions.length * 2];
            System.arraycopy(positions, 0, pp, 0, positions.length);
            positions = pp;
        }
        positions[positionsCount] = position;
        positionsCount++;
    }

    public CTrieEdge makeClone() {
        CTrieEdge p = new CTrieEdge(linearization, start, end, destination);
        for (int i = 0; i < positionsCount; i++) {
            p.addPosition(positions[i]);
        }
        return p;
    }

    public void print() {
        System.out.printf("  edge: %s, start: %d, end: %d, destination: %d%n", getLabel(), start, end, destination.getNum());
        for (int i = 0; i < positionsCount; i++) {
            System.out.printf("    position: %s%n", positions[i]);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %d,%d -> %s", getLabel(), start, end, destination.getNum());
    }
}
