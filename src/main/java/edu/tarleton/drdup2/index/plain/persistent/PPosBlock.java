package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.IOException;

/**
 * The block of positions in the plain (not compressed) persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class PPosBlock {

    private static long posBlockCount;
    protected final long id;
    protected final PBlockType type;

    public static void reset() {
        posBlockCount = 0;
    }

    public PPosBlock(PBlockType type) {
        this(posBlockCount, type);
        posBlockCount++;
    }

    public PPosBlock(long id, PBlockType type) {
        this.id = id;
        this.type = type;
    }

    public static PPosBlock read(long id, Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        long offset = id * PPosBlockDirect.LENGTH;
        posFile.seek(offset);
        long pid = posFile.readLong();
        assert pid == id;
        int t = posFile.readShort();
        PBlockType type = PBlockType.fromOrdinal(t);
        PPosBlock p = instantiate(type, id);
        p.readFrom(storage);
        return p;
    }

    private static PPosBlock instantiate(PBlockType type, long id) {
        switch (type) {
            case DIRECT:
                return new PPosBlockDirect(id);
            case INDIRECT:
                return new PPosBlockIndirect(id);
            case INDIRECT2:
                return new PPosBlockIndirect2(id);
            case INDIRECT3:
                return new PPosBlockIndirect3(id);
            case INDIRECT4:
                return new PPosBlockIndirect4(id);
            default:
                throw new AssertionError("invalid type");
        }
    }

    public long getId() {
        return id;
    }

    public abstract boolean addPos(PPos pos);

    public abstract int countPositions();

    public abstract PPos[] getPositions();

    public abstract PPos[] getPositions(int minSize, int maxSize);

    public abstract void readFrom(Storage storage) throws IOException;

    public abstract void writeTo(Storage storage) throws IOException;

    public abstract PPosBlock toUpperLevelPosBlock(PPosBlock pblock);

    public abstract void print();

    protected void readHeaderFrom(MappedFile posFile) throws IOException {
        long offset = id * PPosBlockDirect.LENGTH;
        posFile.seek(offset);
        long pid = posFile.readLong();
        assert pid == id;
        int t = posFile.readShort();
        PBlockType ptype = PBlockType.fromOrdinal(t);
        assert ptype == type;
    }

    protected void writeHeaderTo(MappedFile posFile) throws IOException {
        long offset = id * PPosBlockDirect.LENGTH;
        posFile.seek(offset);
        posFile.writeLong(id);
        posFile.writeShort((short) type.ordinal());
    }
}
