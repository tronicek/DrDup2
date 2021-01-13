package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The direct block of positions in the plain (not compressed) persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PPosBlockDirect extends PPosBlock {

    public static final int BLOCK_SIZE = 4;
    public static final int LENGTH = 8 + 2 + 2 + BLOCK_SIZE * PPos.LENGTH;
    private int posCount;
    private final PPos[] positions = new PPos[BLOCK_SIZE];

    public PPosBlockDirect() {
        super(PBlockType.DIRECT);
    }

    public PPosBlockDirect(long id) {
        super(id, PBlockType.DIRECT);
    }

    @Override
    public boolean addPos(PPos pos) {
        if (posCount == positions.length) {
            return false;
        }
        positions[posCount] = pos;
        posCount++;
        return true;
    }

    @Override
    public int countPositions() {
        return posCount;
    }

    @Override
    public PPos[] getPositions() {
        PPos[] pp = new PPos[posCount];
        System.arraycopy(positions, 0, pp, 0, pp.length);
        return pp;
    }

    @Override
    public PPos[] getPositions(int minSize, int maxSize) {
        int c = 0;
        for (int i = 0; i < posCount; i++) {
            int size = positions[i].getLines();
            if (minSize <= size && size <= maxSize) {
                c++;
            }
        }
        PPos[] pp = new PPos[c];
        for (int i = 0, j = 0; i < posCount; i++) {
            int size = positions[i].getLines();
            if (minSize <= size && size <= maxSize) {
                pp[j] = positions[i];
                j++;
            }
        }
        return pp;
    }

    @Override
    public void readFrom(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        readHeaderFrom(posFile);
        posCount = posFile.readShort();
        for (int i = 0; i < posCount; i++) {
            positions[i] = PPos.readFrom(storage);
        }
    }

    @Override
    public void writeTo(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        super.writeHeaderTo(posFile);
        posFile.writeShort((short) posCount);
        for (int i = 0; i < posCount; i++) {
            PPos p = positions[i];
            p.writeTo(storage);
        }
    }

    @Override
    public PPosBlock toUpperLevelPosBlock(PPosBlock pblock) {
        return new PPosBlockIndirect(pblock);
    }

    @Override
    public void print() {
        System.out.printf("    posBlockId: %d (direct)%n", id);
        for (int i = 0; i < posCount; i++) {
            positions[i].print();
        }
    }
}
