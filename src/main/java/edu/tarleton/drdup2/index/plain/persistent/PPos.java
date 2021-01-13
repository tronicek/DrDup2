package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import com.github.javaparser.Position;
import java.io.IOException;

/**
 * The class that represents the persistent position in the source code.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PPos {

    public static final int LENGTH = 24;
    private static final Position NO_POSITION = new Position(-1, -1);
    private static long count;
    private final long fileId;
    private final Position begin;
    private final Position end;

    public static long getCount() {
        return count;
    }

    public PPos(long fileId, Position begin, Position end) {
        this.fileId = fileId;
        this.begin = (begin == null) ? NO_POSITION : begin;
        this.end = (end == null) ? NO_POSITION : end;
        count++;
    }

    public long getFileId() {
        return fileId;
    }

    public Position getBegin() {
        return begin;
    }

    public Position getEnd() {
        return end;
    }

    public int getLines() {
        if (begin == NO_POSITION || end == NO_POSITION) {
            return 0;
        }
        return end.line - begin.line + 1;
    }

    public static PPos readFrom(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        long fileId = posFile.readLong();
        int bline = posFile.readInt();
        int bcol = posFile.readInt();
        int eline = posFile.readInt();
        int ecol = posFile.readInt();
        return new PPos(fileId, position(bline, bcol), position(eline, ecol));
    }

    private static Position position(int begin, int end) {
        if (begin < 0 && end < 0) {
            return NO_POSITION;
        }
        return new Position(begin, end);
    }

    public void writeTo(Storage storage) throws IOException {
        MappedFile posFile = storage.getPosFile();
        posFile.writeLong(fileId);
        posFile.writeInt(begin.line);
        posFile.writeInt(begin.column);
        posFile.writeInt(end.line);
        posFile.writeInt(end.column);
    }

    public void print() {
        System.out.printf("      %d:%s,%s%n", fileId, begin, end);
    }
}
