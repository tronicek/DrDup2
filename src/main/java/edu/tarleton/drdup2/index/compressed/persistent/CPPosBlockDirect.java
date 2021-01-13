package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import edu.tarleton.drdup2.index.plain.persistent.PBlockType;
import java.io.IOException;

/**
 * The direct block of positions in the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPPosBlockDirect extends CPPosBlock {

    public static final int BLOCK_SIZE = 4;
    public static final int LENGTH = 8 + 2 + 2 + BLOCK_SIZE * CPPos.LENGTH;
    private int posCount;
    private final CPPos[] positions = new CPPos[BLOCK_SIZE];

    public CPPosBlockDirect() {
        super(PBlockType.DIRECT);
    }

    public CPPosBlockDirect(long id) {
        super(id, PBlockType.DIRECT);
    }

    @Override
    public boolean addPos(CPPos pos) {
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
    public CPPos[] getPositions() {
        CPPos[] pp = new CPPos[posCount];
        System.arraycopy(positions, 0, pp, 0, pp.length);
        return pp;
    }

    @Override
    public CPPos[] getPositions(int minSize, int maxSize) {
        int c = 0;
        for (int i = 0; i < posCount; i++) {
            int size = positions[i].getLines();
            if (minSize <= size && size <= maxSize) {
                c++;
            }
        }
        CPPos[] pp = new CPPos[c];
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
            positions[i] = CPPos.readFrom(storage);
        }
    }

    @Override
    public void writeTo(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        super.writeHeaderTo(posFile);
        posFile.writeShort((short) posCount);
        for (int i = 0; i < posCount; i++) {
            CPPos p = positions[i];
            p.writeTo(storage);
        }
    }

    @Override
    public CPPosBlock toUpperLevelPosBlock(CPPosBlock pblock) {
        return new CPPosBlockIndirect(pblock);
    }

    @Override
    public CPPosBlockDirect makeClone() {
        CPPosBlockDirect p = new CPPosBlockDirect();
        p.posCount = posCount;
        for (int i = 0; i < posCount; i++) {
            p.positions[i] = positions[i];
        }
        return p;
    }

    @Override
    public void print() {
        System.out.printf("    posBlockId: %d (direct)%n", id);
        for (int i = 0; i < posCount; i++) {
            positions[i].print();
        }
    }
}
