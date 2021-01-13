package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The class that represents the persistent node.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PNode {

    public static final int LENGTH = 16;
    private static long count;
    private final long id;
    private PEdgeBlock edgeBlock;

    public static long getCount() {
        return count;
    }

    public static void reset() {
        count = 0;
    }

    public PNode() {
        id = count;
        count++;
        edgeBlock = new PEdgeBlock();
    }

    public PNode(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public PEdgeBlock getEdgeBlock() {
        return edgeBlock;
    }

    public void readFrom(Storage storage) throws IOException {
        MappedFile nodeFile = storage.getNodeFile();
        long offset = id * LENGTH;
        nodeFile.seek(offset);
        nodeFile.readLong();
        long ebId = nodeFile.readLong();
        edgeBlock = new PEdgeBlock(ebId);
        edgeBlock.readFrom(storage);
    }

    public void writeTo(Storage storage) throws IOException {
        MappedFile nodeFile = storage.getNodeFile();
        long offset = id * LENGTH;
        nodeFile.seek(offset);
        nodeFile.writeLong(id);
        nodeFile.writeLong(edgeBlock.getId());
        edgeBlock.writeTo(storage);
    }

    public void print(PLabels labels) throws IOException {
        System.out.printf("node: %d%n", id);
        edgeBlock.print(labels);
    }
}
