package edu.tarleton.drdup2.index.compressed.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Labels of edges in the compressed persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CPLinearizations {

    private final List<String> labels;
    private final List<Integer> buffer;

    private CPLinearizations(List<String> labels, List<Integer> buffer) {
        this.labels = labels;
        this.buffer = buffer;
    }

    public static void initialize(Storage storage) throws IOException {
        File labelFile = storage.getLabelFile();
        try (FileChannel ch = FileChannel.open(labelFile.toPath(),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ch.truncate(0);
        }
        File linFile = storage.getLinearizationFile();
        try (FileChannel ch = FileChannel.open(linFile.toPath(),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ch.truncate(0);
        }
    }

    public static CPLinearizations load(Storage storage) throws IOException {
        List<String> labels = loadLabels(storage);
        List<Integer> linearization = new ArrayList<>();
        File linFile = storage.getLinearizationFile();
        if (!linFile.exists()) {
            return new CPLinearizations(labels, linearization);
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(linFile))) {
            try {
                while (true) {
                    int lab = in.readUnsignedShort();
                    linearization.add(lab);
                }
            } catch (EOFException e) {
                // okay
            }
        }
        return new CPLinearizations(labels, linearization);
    }

    private static List<String> loadLabels(Storage storage) throws IOException {
        List<String> labels = new ArrayList<>();
        File labelFile = storage.getLabelFile();
        if (!labelFile.exists()) {
            return labels;
        }
        try (DataInputStream in = new DataInputStream(new FileInputStream(labelFile))) {
            try {
                while (true) {
                    String label = in.readUTF();
                    labels.add(label);
                }
            } catch (EOFException e) {
                // okay
            }
        }
        return labels;
    }

    private static String toString(List<String> linearization) {
        StringBuilder sb = new StringBuilder();
        for (String lab : linearization) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(lab);
        }
        return sb.toString();
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getLabel(Integer i) {
        return labels.get(i);
    }

    private int toLabelId(String label) {
        int index = labels.indexOf(label);
        if (index >= 0) {
            return index;
        }
        labels.add(label);
        assert labels.size() <= Short.MAX_VALUE;
        return labels.size() - 1;
    }

    public int findLabel(String label) {
        return labels.indexOf(label);
    }

    public List<Integer> getBuffer() {
        return buffer;
    }
    
    public int getBufferSize() {
        return buffer.size();
    }

    public List<Integer> getBuffer(int start, int end) {
        return buffer.subList(start, end);
    }

    public Integer getBufferAt(int index) {
        return buffer.get(index);
    }

    public void extendBuffer(List<String> labs) {
        for (int i = 0; i < labs.size(); i++) {
            String lab = labs.get(i);
            int labId = toLabelId(lab);
            buffer.add(labId);
        }
    }

    public void store(Storage storage) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(storage.getLabelFile()))) {
            for (String lab : labels) {
                out.writeUTF(lab);
            }
        }
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(storage.getLinearizationFile()))) {
            for (Integer i : buffer) {
                out.writeShort(i);
            }
        }
    }

    public void print(Storage storage) throws IOException {
        long linId = 0L;
        try (DataInputStream in = new DataInputStream(
                new FileInputStream(storage.getPathFile()))) {
            try {
                while (true) {
                    int len = in.readUnsignedShort();
                    List<String> linear = new ArrayList<>();
                    for (int i = 0; i < len; i++) {
                        int lab = in.readUnsignedShort();
                        String slab = labels.get(lab);
                        linear.add(slab);
                    }
                    String str = toString(linear);
                    System.out.printf("%d %s%n", linId, str);
                    linId++;
                }
            } catch (EOFException e) {
                // okay
            }
        }
    }
}
