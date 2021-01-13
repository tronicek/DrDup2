package edu.tarleton.drdup2.index.plain.persistent;

import edu.tarleton.drdup2.index.MappedFile;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * The class that represents the persistent storage for the plain (not
 * compressed) persistent TRIE.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Storage {

    private final MappedFile nodeFile;
    private final MappedFile edgeFile;
    private final MappedFile posFile;
    private final File pathFile;
    private final File labelFile;
    private final File nextStmtMapFile;

    private Storage(MappedFile nodeFile, MappedFile edgeFile, MappedFile posFile,
            File pathFile, File labelFile, File nextStmtMapFile) throws IOException {
        this.nodeFile = nodeFile;
        this.edgeFile = edgeFile;
        this.posFile = posFile;
        this.pathFile = pathFile;
        this.labelFile = labelFile;
        this.nextStmtMapFile = nextStmtMapFile;
    }

    public static Storage initialize(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String nextStmtMapFileName) throws IOException {
        MappedFile nodeFile = MappedFile.initialize(nodeFileName, FileChannel.MapMode.READ_WRITE, nodeFilePageSize);
        MappedFile edgeFile = MappedFile.initialize(edgeFileName, FileChannel.MapMode.READ_WRITE, edgeFilePageSize);
        MappedFile posFile = MappedFile.initialize(posFileName, FileChannel.MapMode.READ_WRITE, posFilePageSize);
        File pathFile = new File(pathFileName);
        File labelFile = new File(labelFileName);
        File nextStmtMapFile = (nextStmtMapFileName == null) ? null : new File(nextStmtMapFileName);
        return new Storage(nodeFile, edgeFile, posFile, pathFile, labelFile, nextStmtMapFile);
    }

    public static Storage open(String nodeFileName, int nodeFilePageSize,
            String edgeFileName, int edgeFilePageSize,
            String posFileName, int posFilePageSize,
            String pathFileName, String labelFileName,
            String nextStmtMapFileName) throws IOException {
        MappedFile nodeFile = MappedFile.open(nodeFileName, FileChannel.MapMode.READ_WRITE, nodeFilePageSize);
        MappedFile edgeFile = MappedFile.open(edgeFileName, FileChannel.MapMode.READ_WRITE, edgeFilePageSize);
        MappedFile posFile = MappedFile.open(posFileName, FileChannel.MapMode.READ_WRITE, posFilePageSize);
        File pathFile = new File(pathFileName);
        File labelFile = new File(labelFileName);
        File nextStmtMapFile = (nextStmtMapFileName == null) ? null : new File(nextStmtMapFileName);
        return new Storage(nodeFile, edgeFile, posFile, pathFile, labelFile, nextStmtMapFile);
    }

    public void close() throws IOException {
        nodeFile.close();
        edgeFile.close();
        posFile.close();
    }

    public MappedFile getNodeFile() {
        return nodeFile;
    }

    public MappedFile getEdgeFile() {
        return edgeFile;
    }

    public MappedFile getPosFile() {
        return posFile;
    }

    public File getPathFile() {
        return pathFile;
    }

    public File getLabelFile() {
        return labelFile;
    }

    public File getNextStmtMapFile() {
        return nextStmtMapFile;
    }
}
