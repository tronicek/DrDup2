package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The class that represents the persistent TRIE edge.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PEdge {

    public static final int LENGTH = 18;
    private static long count;
    private final int labelId;
    private final Long destId;
    private PPosBlock posBlock;

    public static long getCount() {
        return count;
    }

    public PEdge(int labelId, Long destId) {
        this.labelId = labelId;
        this.destId = destId;
        count++;
    }

    private PEdge(int labelId, Long destId, PPosBlock posBlock) {
        this(labelId, destId);
        this.posBlock = posBlock;
    }

    public int getLabelId() {
        return labelId;
    }

    public Long getDestId() {
        return destId;
    }

    public PPosBlock getPosBlock() {
        return posBlock;
    }

    public void addPos(PPos pos) {
        if (posBlock == null) {
            posBlock = new PPosBlockDirect();
        }
        if (posBlock.addPos(pos)) {
            return;
        }
        posBlock = posBlock.toUpperLevelPosBlock(posBlock);
        posBlock.addPos(pos);
    }

    public static PEdge readFrom(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        int labelId = edgeFile.readShort();
        long destId = edgeFile.readLong();
        long posBlockId = edgeFile.readLong();
        PPosBlock posBlock = (posBlockId < 0) ? null : PPosBlock.read(posBlockId, storage);
        return new PEdge(labelId, destId, posBlock);
    }

    public void writeTo(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        edgeFile.writeShort((short) labelId);
        edgeFile.writeLong(destId);
        if (posBlock == null) {
            edgeFile.writeLong(-1);
        } else {
            edgeFile.writeLong(posBlock.getId());
            posBlock.writeTo(storage);
        }
    }

    public void print(PLabels labels) throws IOException {
        String lab = labels.fromLabelId(labelId);
        System.out.printf("  %s -> %d%n", lab, destId);
        if (posBlock != null) {
            posBlock.print();
        }
    }
}
