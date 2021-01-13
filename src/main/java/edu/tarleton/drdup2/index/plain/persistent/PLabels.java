package edu.tarleton.drdup2.index.plain.persistent;

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
 * The class that represents the persistent labels of TRIE edges.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PLabels {

    private final List<String> labels;

    private PLabels(List<String> labels) {
        this.labels = labels;
    }

    public static void initialize(Storage storage) throws IOException {
        File labelFile = storage.getLabelFile();
        try (FileChannel ch = FileChannel.open(labelFile.toPath(), 
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            ch.truncate(0);
        }
    }

    public static PLabels load(Storage storage) throws IOException {
        List<String> labels = new ArrayList<>();
        File labelFile = storage.getLabelFile();
        if (!labelFile.exists()) {
            return new PLabels(labels);
        }
        try (DataInputStream in = new DataInputStream(
                new FileInputStream(storage.getLabelFile()))) {
            try {
                while (true) {
                    String label = in.readUTF();
                    labels.add(label);
                }
            } catch (EOFException e) {
                // okay
            }
        }
        return new PLabels(labels);
    }

    public void store(Storage storage) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(storage.getLabelFile()))) {
            for (String label : labels) {
                out.writeUTF(label);
            }
        }
    }

    public int toLabelId(String label) {
        int index = labels.indexOf(label);
        if (index >= 0) {
            return index;
        }
        labels.add(label);
        assert labels.size() <= Short.MAX_VALUE;
        return labels.size() - 1;
    }
    
    public String fromLabelId(int labelId) {
        return labels.get(labelId);
    }

    public void print(Storage storage) throws IOException {
        int index = 0;
        try (DataInputStream in = new DataInputStream(
                new FileInputStream(storage.getLabelFile()))) {
            try {
                while (true) {
                    String label = in.readUTF();
                    System.out.printf("%d %s%n", index, label);
                    index++;
                }
            } catch (EOFException e) {
                // okay
            }
        }
    }
}
