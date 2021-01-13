package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The class that represents the block of persistent TRIE edges.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PEdgeBlock {

    public static final int BLOCK_SIZE = 4;
    public static final int LENGTH = 8 + 4 + BLOCK_SIZE * PEdge.LENGTH + 8;
    private static long edgeBlockCount;
    private final long id;
    private int edgeCount;
    private final PEdge[] edges = new PEdge[BLOCK_SIZE];
    private PEdgeBlock next;

    public static void reset() {
        edgeBlockCount = 0;
    }

    public PEdgeBlock() {
        id = edgeBlockCount;
        edgeBlockCount++;
    }

    public PEdgeBlock(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public PEdge[] getEdges() {
        return edges;
    }

    public void addEdge(PEdge edge) {
        if (edgeCount < edges.length) {
            edges[edgeCount] = edge;
            edgeCount++;
        } else {
            if (next == null) {
                next = new PEdgeBlock();
            }
            next.addEdge(edge);
        }
    }

    public PEdge findEdge(short labelId) {
        for (int i = 0; i < edgeCount; i++) {
            PEdge e = edges[i];
            if (e.getLabelId() == labelId) {
                return e;
            }
        }
        if (next != null) {
            return next.findEdge(labelId);
        }
        return null;
    }

    public PEdgeBlock getNext() {
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
            edges[i] = PEdge.readFrom(storage);
        }
        if (nextId >= 0) {
            next = new PEdgeBlock(nextId);
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
            PEdge e = edges[i];
            e.writeTo(storage);
        }
        if (next != null) {
            next.writeTo(storage);
        }
    }

    public void print(PLabels labels) throws IOException {
        for (int i = 0; i < edgeCount; i++) {
            edges[i].print(labels);
        }
        if (next != null) {
            next.print(labels);
        }
    }
}
