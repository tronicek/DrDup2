package edu.tarleton.drdup2.index.plain.persistent;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import edu.tarleton.drdup2.Engine;
import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.NormalizingVisitor;
import edu.tarleton.drdup2.index.IndexBuilder;
import edu.tarleton.drdup2.index.plain.naive.FullIndexBuilderNaive;
import edu.tarleton.drdup2.index.plain.naive.SimplifiedIndexBuilderNaive;
import edu.tarleton.drdup2.index.plain.naive.SimplifiedIndexStmtBuilderNaive;
import edu.tarleton.drdup2.index.plain.FullIndexBuilder;
import edu.tarleton.drdup2.index.plain.SimplifiedIndexBuilder;
import edu.tarleton.drdup2.index.plain.SimplifiedIndexStmtBuilder;
import edu.tarleton.drdup2.index.plain.Trie;
import edu.tarleton.drdup2.nicad.NiCadConvertor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * The class that builds the plain (not compressed) index and finds the clones.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class PlainPersistentEngine extends Engine {

    public static final int DEFAULT_NODE_FILE_PAGE_SIZE = PNode.LENGTH * 1024 * 1024 * 60;
    public static final int DEFAULT_EDGE_FILE_PAGE_SIZE = PEdgeBlock.LENGTH * 1024 * 1024 * 4;
    public static final int DEFAULT_POS_FILE_PAGE_SIZE = PPos.LENGTH * 1024 * 1024 * 40;
    private final String nodeFileName;
    private final int nodeFilePageSize;
    private final String edgeFileName;
    private final int edgeFilePageSize;
    private final String posFileName;
    private final int posFilePageSize;
    private final String pathFileName;
    private final String labelFileName;
    private final String nextStmtMapFileName;
    private final int batchFileSize;

    public PlainPersistentEngine(Properties conf) {
        super(conf);
        nodeFileName = conf.getProperty("nodeFile");
        nodeFilePageSize = getIntProperty(conf, "nodeFilePageSize", DEFAULT_NODE_FILE_PAGE_SIZE);
        edgeFileName = conf.getProperty("edgeFile");
        edgeFilePageSize = getIntProperty(conf, "edgeFilePageSize", DEFAULT_EDGE_FILE_PAGE_SIZE);
        posFileName = conf.getProperty("posFile");
        posFilePageSize = getIntProperty(conf, "posFilePageSize", DEFAULT_POS_FILE_PAGE_SIZE);
        pathFileName = conf.getProperty("pathFile");
        labelFileName = conf.getProperty("labelFile");
        nextStmtMapFileName = conf.getProperty("nextStmtMapFile");
        batchFileSize = Integer.parseInt(conf.getProperty("batchFileSize", "1000"));
    }

    private int getIntProperty(Properties conf, String name, int defaultValue) {
        String s = conf.getProperty(name);
        if (s == null) {
            return defaultValue;
        }
        return Integer.parseInt(s);
    }

    @Override
    public void findClones() throws Exception {
        IndexBuilder builder = rename.equals("blind")
                ? createBlindBuilder()
                : createConsistentBuilder();
        try (PTrie trie = PTrie.initialize(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                nextStmtMapFileName)) {
            processDir(builder, sourceDir, trie);
            if (fileCount > 0) {
                Trie t = (Trie) builder.getIndex();
                trie.addTrie(t);
                if (printStatistics) {
                    statistics.store(countingVisitor.getLines(), 
                            countingVisitor.getNodes(), PNode.getCount(), 
                            PEdge.getCount(), PPos.getCount());
                }
            }
            if (printStatistics) {
                statistics.print(true);
            }
            if (printTrie) {
                trie.print();
            }
            if (printHistogram) {
                Histogram hist = trie.createHistogram();
                hist.print();
            }
            if (verbose) {
                System.out.println("searching for clones...");
            }
            cloneSet = trie.detectClonesType2(level, minSize, maxSize);
            if (outputFileName == null) {
                cloneSet.print();
            } else {
                NiCadConvertor conv = new NiCadConvertor(conf);
                conv.convert(cloneSet, outputFileName);
            }
        }
    }

    private IndexBuilder createBlindBuilder() {
        boolean methodLevel = level.equals("method");
        Path dir = Paths.get(sourceDir).toAbsolutePath();
        switch (index) {
            case "simplified":
                return methodLevel
                        ? new SimplifiedIndexBuilder(conf, dir)
                        : new SimplifiedIndexStmtBuilder(conf, dir);
            case "full":
                return new FullIndexBuilder(conf, dir);
            default:
                throw new AssertionError("invalid index: " + index);
        }
    }

    private IndexBuilder createConsistentBuilder() {
        String alg = conf.getProperty("algorithm", "online");
        if (alg.equals("online")) {
            throw new AssertionError("rename=consistent, algorithm=online: not supported");
        }
        if (level.equals("statements")) {
            throw new AssertionError("rename=consistent, level=statements: not supported");
        }
        boolean methodLevel = level.equals("method");
        Path dir = Paths.get(sourceDir).toAbsolutePath();
        switch (index) {
            case "simplified":
                return methodLevel
                        ? new SimplifiedIndexBuilderNaive(conf, dir)
                        : new SimplifiedIndexStmtBuilderNaive(conf, dir);
            case "full":
                return new FullIndexBuilderNaive(conf, dir);
            default:
                throw new AssertionError("invalid index: " + index);
        }
    }

    private void processDir(IndexBuilder builder, String srcDir, PTrie trie) throws IOException {
        Path path = Paths.get(srcDir);
        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        processFile(builder, srcDir, p, trie);
                    });
        }
    }

    private void processFile(IndexBuilder builder, String srcDir, Path path, PTrie trie) {
        String fn = path.toString().substring(srcDir.length());
        if (fn.startsWith("/") || fn.startsWith("\\")) {
            fn = fn.substring(1);
        }
        if (verbose) {
            System.out.printf("processing %s...%n", fn);
        }
        NormalizingVisitor normVisitor = new NormalizingVisitor(conf);
        Path root = Paths.get(srcDir);
        SourceRoot sourceRoot = new SourceRoot(root, parserConfiguration);
        try {
            CompilationUnit cu = sourceRoot.parse("", fn);
            if (printStatistics) {
                cu.accept(countingVisitor, null);
            }
            cu.accept(normVisitor, null);
            cu.accept(builder, null);
            fileCount++;
            if (fileCount == batchFileSize) {
                Trie t = (Trie) builder.getIndex();
                trie.addTrie(t);
                if (printStatistics) {
                    statistics.store(countingVisitor.getLines(), 
                            countingVisitor.getNodes(), PNode.getCount(), 
                            PEdge.getCount(), PPos.getCount());
                }
                builder.reset();
                fileCount = 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printTrie() throws Exception {
        try (PTrie trie = PTrie.fromFiles(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                nextStmtMapFileName)) {
            trie.print();
        }
    }
}
