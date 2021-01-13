package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;
import java.util.List;

/**
 * The block of edges of the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPEdgeBlock {

    public static final int BLOCK_SIZE = 4;
    public static final int LENGTH = 8 + 4 + BLOCK_SIZE * CPEdge.LENGTH + 8;
    private static long edgeBlockCount;
    private final long id;
    private int edgeCount;
    private final CPEdge[] edges = new CPEdge[BLOCK_SIZE];
    private CPEdgeBlock next;

    public static void reset() {
        edgeBlockCount = 0;
    }

    public CPEdgeBlock() {
        id = edgeBlockCount;
        edgeBlockCount++;
    }

    public CPEdgeBlock(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public CPEdge[] getEdges() {
        return edges;
    }

    public void addEdge(CPEdge edge) {
        if (edgeCount < edges.length) {
            edges[edgeCount] = edge;
            edgeCount++;
        } else {
            if (next == null) {
                next = new CPEdgeBlock();
            }
            next.addEdge(edge);
        }
    }

    public CPEdge findEdge(Integer label, List<Integer> buffer) {
        for (int i = 0; i < edgeCount; i++) {
            CPEdge e = edges[i];
            Integer first = buffer.get(e.getStart());
            if (label.equals(first)) {
                return e;
            }
        }
        if (next != null) {
            return next.findEdge(label, buffer);
        }
        return null;
    }

    public CPEdgeBlock getNext() {
        return next;
    }

    public void readFrom(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        long offset = id * LENGTH;
        edgeFile.seek(offset);
        long eid = edgeFile.readLong();
        assert eid == id;
        long nextId = edgeFile.readLong();
        edgeCount = edgeFile.readInt();
        for (int i = 0; i < edgeCount; i++) {
            edges[i] = CPEdge.readFrom(storage);
        }
        if (nextId >= 0) {
            next = new CPEdgeBlock(nextId);
            next.readFrom(storage);
        }
    }

    public void writeTo(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        long offset = id * LENGTH;
        edgeFile.seek(offset);
        edgeFile.writeLong(id);
        long nextId = (next == null) ? -1L : next.id;
        edgeFile.writeLong(nextId);
        edgeFile.writeInt(edgeCount);
        for (int i = 0; i < edgeCount; i++) {
            CPEdge e = edges[i];
            e.writeTo(storage);
        }
        if (next != null) {
            next.writeTo(storage);
        }
    }

    public void print(CPLinearizations linearizations) throws IOException {
        for (int i = 0; i < edgeCount; i++) {
            edges[i].print(linearizations);
        }
        if (next != null) {
            next.print(linearizations);
        }
    }
}
