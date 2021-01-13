package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import edu.tarleton.drdup2.index.plain.persistent.PBlockType;
import java.io.IOException;

/**
 * The second-level indirect block of positions in the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPPosBlockIndirect2 extends CPPosBlock {

    public static final int BLOCK_SIZE = (CPPosBlockDirect.LENGTH - 8 - 2 - 2) / 8;
    private int posBlockCount;
    private final CPPosBlock[] posBlock = new CPPosBlock[BLOCK_SIZE];

    public CPPosBlockIndirect2() {
        super(PBlockType.INDIRECT2);
    }

    public CPPosBlockIndirect2(long id) {
        super(id, PBlockType.INDIRECT2);
    }

    public CPPosBlockIndirect2(CPPosBlock pb) {
        super(PBlockType.INDIRECT2);
        posBlock[0] = pb;
        posBlockCount++;
    }

    @Override
    public boolean addPos(CPPos pos) {
        if (posBlockCount == 0) {
            posBlock[0] = new CPPosBlockIndirect();
            posBlock[0].addPos(pos);
            posBlockCount++;
            return true;
        }
        if (posBlockCount == posBlock.length) {
            return posBlock[posBlockCount - 1].addPos(pos);
        }
        if (posBlock[posBlockCount - 1].addPos(pos)) {
            return true;
        }
        posBlock[posBlockCount] = new CPPosBlockIndirect();
        posBlock[posBlockCount].addPos(pos);
        posBlockCount++;
        return true;
    }

    @Override
    public int countPositions() {
        int posCount = 0;
        for (int i = 0; i < posBlockCount; i++) {
            posCount += posBlock[i].countPositions();
        }
        return posCount;
    }

    @Override
    public CPPos[] getPositions() {
        CPPos[] pp = new CPPos[0];
        for (int i = 0; i < posBlockCount; i++) {
            CPPos[] pp2 = posBlock[i].getPositions();
            pp = merge(pp, pp2);
        }
        return pp;
    }

    @Override
    public CPPos[] getPositions(int minSize, int maxSize) {
        CPPos[] pp = new CPPos[0];
        for (int i = 0; i < posBlockCount; i++) {
            CPPos[] pp2 = posBlock[i].getPositions(minSize, maxSize);
            pp = merge(pp, pp2);
        }
        return pp;
    }

    private CPPos[] merge(CPPos[] pp1, CPPos[] pp2) {
        CPPos[] pp = new CPPos[pp1.length + pp2.length];
        System.arraycopy(pp1, 0, pp, 0, pp1.length);
        System.arraycopy(pp2, 0, pp, pp1.length, pp2.length);
        return pp;
    }

    @Override
    public void readFrom(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        readHeaderFrom(posFile);
        posBlockCount = posFile.readShort();
        for (int i = 0; i < posBlockCount; i++) {
            long pbId = posFile.readLong();
            posBlock[i] = new CPPosBlockIndirect(pbId);
        }
        for (int i = 0; i < posBlockCount; i++) {
            posBlock[i].readFrom(storage);
        }
    }

    @Override
    public void writeTo(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        super.writeHeaderTo(posFile);
        posFile.writeShort((short) posBlockCount);
        for (int i = 0; i < posBlockCount; i++) {
            long pbId = posBlock[i].getId();
            posFile.writeLong(pbId);
        }
        for (int i = 0; i < posBlockCount; i++) {
            posBlock[i].writeTo(storage);
        }
    }

    @Override
    public CPPosBlock toUpperLevelPosBlock(CPPosBlock pblock) {
        return new CPPosBlockIndirect3(pblock);
    }

    @Override
    public CPPosBlockIndirect2 makeClone() {
        CPPosBlockIndirect2 p = new CPPosBlockIndirect2();
        p.posBlockCount = posBlockCount;
        for (int i = 0; i < posBlockCount; i++) {
            p.posBlock[i] = posBlock[i].makeClone();
        }
        return p;
    }

    @Override
    public void print() {
        System.out.printf("    posBlockId: %d (indirect2)%n", id);
        for (int i = 0; i < posBlockCount; i++) {
            posBlock[i].print();
        }
    }
}
