package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The node of the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPNode {

    public static final int LENGTH = 16;
    private static long count;
    private final long id;
    private CPEdgeBlock edgeBlock;

    public static long getCount() {
        return count;
    }

    public static void reset() {
        count = 0;
    }

    public CPNode() {
        id = count;
        count++;
        edgeBlock = new CPEdgeBlock();
    }

    public CPNode(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public CPEdgeBlock getEdgeBlock() {
        return edgeBlock;
    }

    public void readFrom(Storage storage) throws IOException {
        MappedFile nodeFile = storage.getNodeFile();
        long offset = id * LENGTH;
        nodeFile.seek(offset);
        nodeFile.readLong();
        long ebId = nodeFile.readLong();
        edgeBlock = new CPEdgeBlock(ebId);
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

    public void print(CPLinearizations linearizations) throws IOException {
        System.out.printf("node: %d%n", id);
        edgeBlock.print(linearizations);
    }
}
