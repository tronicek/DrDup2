package edu.tarleton.drdup2.index.compressed.persistent;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import edu.tarleton.drdup2.Engine;
import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.NormalizingVisitor;
import edu.tarleton.drdup2.index.CompressedIndexBuilder;
import edu.tarleton.drdup2.index.compressed.CTrie;
import edu.tarleton.drdup2.index.compressed.FullCompressedIndexBuilder;
import edu.tarleton.drdup2.index.compressed.SimplifiedCompressedIndexBuilder;
import edu.tarleton.drdup2.index.compressed.SimplifiedCompressedIndexStmtBuilder;
import edu.tarleton.drdup2.index.compressed.naive.FullCompressedIndexBuilderNaive;
import edu.tarleton.drdup2.index.compressed.naive.SimplifiedCompressedIndexBuilderNaive;
import edu.tarleton.drdup2.index.compressed.naive.SimplifiedCompressedIndexStmtBuilderNaive;
import edu.tarleton.drdup2.index.plain.persistent.PEdgeBlock;
import edu.tarleton.drdup2.index.plain.persistent.PNode;
import edu.tarleton.drdup2.index.plain.persistent.PPos;
import edu.tarleton.drdup2.nicad.NiCadConvertor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * The class that builds the index and finds the clones.
 *
 * @author Zdenek Tronicek
 */
public class CPEngine extends Engine {

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
    private final String linearizationFileName;
    private final String nextStmtMapFileName;
    private final int batchFileSize;

    public CPEngine(Properties conf) {
        super(conf);
        nodeFileName = conf.getProperty("nodeFile");
        nodeFilePageSize = getIntProperty(conf, "nodeFilePageSize", DEFAULT_NODE_FILE_PAGE_SIZE);
        edgeFileName = conf.getProperty("edgeFile");
        edgeFilePageSize = getIntProperty(conf, "edgeFilePageSize", DEFAULT_EDGE_FILE_PAGE_SIZE);
        posFileName = conf.getProperty("posFile");
        posFilePageSize = getIntProperty(conf, "posFilePageSize", DEFAULT_POS_FILE_PAGE_SIZE);
        pathFileName = conf.getProperty("pathFile");
        labelFileName = conf.getProperty("labelFile");
        linearizationFileName = conf.getProperty("linearizationFile");
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
        CompressedIndexBuilder builder = rename.equals("blind")
                ? createBlindBuilder()
                : createConsistentBuilder();
        try (CPTrie trie = CPTrie.initialize(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                linearizationFileName, nextStmtMapFileName)) {
            processDir(builder, sourceDir, trie);
            if (fileCount > 0) {
                CTrie t = builder.getTrie();
                trie.addTrie(t);
                if (printStatistics) {
                    statistics.store(countingVisitor.getLines(), countingVisitor.getNodes(), CPNode.getCount(), CPEdge.getCount(), CPPos.getCount());
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

    private CompressedIndexBuilder createBlindBuilder() {
        boolean methodLevel = level.equals("method");
        Path dir = Paths.get(sourceDir).toAbsolutePath();
        switch (index) {
            case "simplified":
                return methodLevel
                        ? new SimplifiedCompressedIndexBuilder(conf, dir)
                        : new SimplifiedCompressedIndexStmtBuilder(conf, dir);
            case "full":
                return new FullCompressedIndexBuilder(conf, dir);
            default:
                throw new AssertionError("invalid index: " + index);
        }
    }

    private CompressedIndexBuilder createConsistentBuilder() {
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
                        ? new SimplifiedCompressedIndexBuilderNaive(conf, dir)
                        : new SimplifiedCompressedIndexStmtBuilderNaive(conf, dir);
            case "full":
                return new FullCompressedIndexBuilderNaive(conf, dir);
            default:
                throw new AssertionError("invalid index: " + index);
        }
    }

    private void processDir(CompressedIndexBuilder builder, String srcDir, CPTrie trie) throws IOException {
        Path path = Paths.get(srcDir);
        try (Stream<Path> paths = Files.walk(path)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        processFile(builder, srcDir, p, trie);
                    });
        }
    }

    private void processFile(CompressedIndexBuilder builder, String srcDir, Path path, CPTrie trie) {
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
                CTrie t = builder.getTrie();
                trie.addTrie(t);
                if (printStatistics) {
                    statistics.store(countingVisitor.getLines(),
                            countingVisitor.getNodes(), CPNode.getCount(),
                            CPEdge.getCount(), CPPos.getCount());
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
        try (CPTrie trie = CPTrie.fromFiles(nodeFileName, nodeFilePageSize,
                edgeFileName, edgeFilePageSize,
                posFileName, posFilePageSize,
                pathFileName, labelFileName,
                linearizationFileName, nextStmtMapFileName)) {
            trie.print();
        }
    }
}
