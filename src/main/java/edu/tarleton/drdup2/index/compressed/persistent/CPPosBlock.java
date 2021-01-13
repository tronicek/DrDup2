package edu.tarleton.drdup2.index.compressed.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import edu.tarleton.drdup2.index.plain.persistent.PBlockType;
import java.io.IOException;

/**
 * The block of positions in the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class CPPosBlock {

    private static long posBlockCount;
    protected final long id;
    protected final PBlockType type;

    public static void reset() {
        posBlockCount = 0;
    }

    public CPPosBlock(PBlockType type) {
        this(posBlockCount, type);
        posBlockCount++;
    }

    public CPPosBlock(long id, PBlockType type) {
        this.id = id;
        this.type = type;
    }

    public static CPPosBlock read(long id, Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        long offset = id * CPPosBlockDirect.LENGTH;
        posFile.seek(offset);
        long pid = posFile.readLong();
        assert pid == id;
        int t = posFile.readShort();
        PBlockType type = PBlockType.fromOrdinal(t);
        CPPosBlock p = instantiate(type, id);
        p.readFrom(storage);
        return p;
    }

    private static CPPosBlock instantiate(PBlockType type, long id) {
        switch (type) {
            case DIRECT:
                return new CPPosBlockDirect(id);
            case INDIRECT:
                return new CPPosBlockIndirect(id);
            case INDIRECT2:
                return new CPPosBlockIndirect2(id);
            case INDIRECT3:
                return new CPPosBlockIndirect3(id);
            case INDIRECT4:
                return new CPPosBlockIndirect4(id);
            default:
                throw new AssertionError("invalid type");
        }
    }

    public long getId() {
        return id;
    }

    public abstract boolean addPos(CPPos pos);

    public abstract int countPositions();

    public abstract CPPos[] getPositions();

    public abstract CPPos[] getPositions(int minSize, int maxSize);

    public abstract void readFrom(Storage storage) throws IOException;

    public abstract void writeTo(Storage storage) throws IOException;

    public abstract CPPosBlock toUpperLevelPosBlock(CPPosBlock pblock);
    
    public abstract CPPosBlock makeClone();
    
    public abstract void print();

    protected void readHeaderFrom(MappedFile posFile) throws IOException {
        long offset = id * CPPosBlockDirect.LENGTH;
        posFile.seek(offset);
        long pid = posFile.readLong();
        assert pid == id;
        int t = posFile.readShort();
        PBlockType ptype = PBlockType.fromOrdinal(t);
        assert ptype == type;
    }

    protected void writeHeaderTo(MappedFile posFile) throws IOException {
        long offset = id * CPPosBlockDirect.LENGTH;
        posFile.seek(offset);
        posFile.writeLong(id);
        posFile.writeShort((short) type.ordinal());
    }
}
