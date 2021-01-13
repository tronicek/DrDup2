package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The edge of the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPEdge {

    public static final int LENGTH = 24;
    private static long count;
    private int start;
    private int end;
    private Long destId;
    private CPPosBlock posBlock;

    public static long getCount() {
        return count;
    }

    public CPEdge(int start, int end, Long destId) {
        this.start = start;
        this.end = end;
        this.destId = destId;
        count++;
    }

    private CPEdge(int start, int end, Long destId, CPPosBlock posBlock) {
        this(start, end, destId);
        this.posBlock = posBlock;
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

    public Long getDestId() {
        return destId;
    }

    public void setDestId(Long destId) {
        this.destId = destId;
    }

    public CPPosBlock getPosBlock() {
        return posBlock;
    }

    public void addPos(CPPos pos) {
        if (posBlock == null) {
            posBlock = new CPPosBlockDirect();
        }
        if (posBlock.addPos(pos)) {
            return;
        }
        posBlock = posBlock.toUpperLevelPosBlock(posBlock);
        posBlock.addPos(pos);
    }

    public static CPEdge readFrom(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        int start = edgeFile.readInt();
        int end = edgeFile.readInt();
        long destId = edgeFile.readLong();
        long posBlockId = edgeFile.readLong();
        CPPosBlock posBlock = (posBlockId < 0) ? null : CPPosBlock.read(posBlockId, storage);
        return new CPEdge(start, end, destId, posBlock);
    }

    public void writeTo(Storage storage) throws IOException {
        MappedFile edgeFile = storage.getEdgeFile();
        edgeFile.writeInt(start);
        edgeFile.writeInt(end);
        edgeFile.writeLong(destId);
        if (posBlock == null) {
            edgeFile.writeLong(-1);
        } else {
            edgeFile.writeLong(posBlock.getId());
            posBlock.writeTo(storage);
        }
    }

    public CPEdge makeClone() {
        CPEdge p = new CPEdge(start, end, destId);
        if (posBlock != null) {
            p.posBlock = posBlock.makeClone();
        }
        return p;
    }

    public void print(CPLinearizations linearizations) throws IOException {
        List<String> str = new ArrayList<>();
        for (int i = start; i < end; i++) {
            Integer j = linearizations.getBufferAt(i);
            String lab = linearizations.getLabel(j);
            str.add(lab);
        }
        System.out.printf("  %s -> %d%n", str, destId);
        if (posBlock != null) {
            posBlock.print();
        }
    }
}
