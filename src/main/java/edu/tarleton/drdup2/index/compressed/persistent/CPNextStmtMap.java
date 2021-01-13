package edu.tarleton.drdup2.index.compressed.persistent;

import com.github.javaparser.Position;
import edu.tarleton.drdup2.clones.Pos;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * The class that represents the persistent next-statement map.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPNextStmtMap {

    private final Map<Pos, Pos> nextStmtMap;

    private CPNextStmtMap(Map<Pos, Pos> nextStmtMap) {
        this.nextStmtMap = nextStmtMap;
    }

    public Pos getNext(Pos stmt) {
        return nextStmtMap.get(stmt);
    }

    public void addNextStmtMap(Storage storage, Map<Pos, Pos> map) throws IOException {
        append(storage, map);
        for (Pos stmt : map.keySet()) {
            Pos next = map.get(stmt);
            nextStmtMap.put(stmt, next);
        }
    }

    private void append(Storage storage, Map<Pos, Pos> map) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(storage.getNextStmtMapFile(), true))) {
            for (Pos stmt : map.keySet()) {
                Pos next = map.get(stmt);
                out.writeUTF(stmt.getFile());
                out.writeInt(stmt.getStartLine());
                out.writeInt(stmt.getStartColumn());
                out.writeInt(stmt.getEndLine());
                out.writeInt(stmt.getEndColumn());
                out.writeInt(next.getStartLine());
                out.writeInt(next.getStartColumn());
                out.writeInt(next.getEndLine());
                out.writeInt(next.getEndColumn());
            }
        }
    }

    public static void initialize(Storage storage) throws IOException {
        File mapFile = storage.getNextStmtMapFile();
        if (mapFile != null) {
            try (FileChannel ch = FileChannel.open(mapFile.toPath(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                ch.truncate(0);
            }
        }
    }

    public static CPNextStmtMap load(Storage storage) throws IOException {
        Map<Pos, Pos> map = new HashMap<>();
        File mapFile = storage.getNextStmtMapFile();
        if (mapFile == null) {
            return null;
        }
        if (!mapFile.exists()) {
            return new CPNextStmtMap(map);
        }
        try (DataInputStream in = new DataInputStream(
                new FileInputStream(storage.getNextStmtMapFile()))) {
            try {
                while (true) {
                    Pos[] pos = readTwoPos(in);
                    map.put(pos[0], pos[1]);
                }
            } catch (EOFException e) {
                // okay
            }
        }
        return new CPNextStmtMap(map);
    }

    private static Pos[] readTwoPos(DataInputStream in) throws IOException {
        String file = in.readUTF();
        Position stmtStart = new Position(in.readInt(), in.readInt());
        Position stmtEnd = new Position(in.readInt(), in.readInt());
        Position nextStart = new Position(in.readInt(), in.readInt());
        Position nextEnd = new Position(in.readInt(), in.readInt());
        return new Pos[]{new Pos(file, stmtStart, stmtEnd), new Pos(file, nextStart, nextEnd)};
    }

    public void store(Storage storage) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(storage.getLabelFile()))) {
            for (Pos stmt : nextStmtMap.keySet()) {
                Pos next = nextStmtMap.get(stmt);
                out.writeUTF(stmt.getFile());
                out.writeInt(stmt.getStartLine());
                out.writeInt(stmt.getStartColumn());
                out.writeInt(stmt.getEndLine());
                out.writeInt(stmt.getEndColumn());
                out.writeInt(next.getStartLine());
                out.writeInt(next.getStartColumn());
                out.writeInt(next.getEndLine());
                out.writeInt(next.getEndColumn());
            }
        }
    }

    public void print(Storage storage) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(storage.getNextStmtMapFile()))) {
            try {
                while (true) {
                    Pos[] pos = readTwoPos(in);
                    System.out.printf("%s -> %s%n", pos[0], pos[1]);
                }
            } catch (EOFException e) {
                // okay
            }
        }
    }
}
