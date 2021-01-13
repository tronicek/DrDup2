package edu.tarleton.drdup2;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ParserConfiguration.LanguageLevel;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.index.compressed.MemoryCompressedEngine;
import edu.tarleton.drdup2.index.compressed.persistent.CPEngine;
import edu.tarleton.drdup2.index.plain.MemoryPlainEngine;
import edu.tarleton.drdup2.index.plain.persistent.PlainPersistentEngine;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * The class that builds the index and finds the clones.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class Engine {

    protected final Properties conf;
    protected final String index;
    protected final String level;
    protected final String rename;
    protected final String type;
    protected final String sourceDir;
    protected final String outputFileName;
    protected final int minSize;
    protected final int maxSize;
    protected final boolean printHistogram;
    protected final boolean printStatistics;
    protected final boolean printTrie;
    protected final boolean verbose;
    protected final boolean preprocessUnicodeEscapes;
    protected final String languageLevel;
    protected final String sourceEncoding;
    protected final ParserConfiguration parserConfiguration = new ParserConfiguration();
    protected final CountingVisitor countingVisitor;
    protected final Statistics statistics;
    protected int fileCount;
    protected CloneSet cloneSet;

    protected Engine(Properties conf) {
        this.conf = conf;
        index = conf.getProperty("index", "full");
        level = conf.getProperty("level", "method");
        rename = conf.getProperty("rename", "blind");
        type = conf.getProperty("type", "2");
        sourceDir = conf.getProperty("sourceDir");
        outputFileName = conf.getProperty("outputFile");
        minSize = Integer.parseInt(conf.getProperty("minSize", "0"));
        maxSize = Integer.parseInt(conf.getProperty("maxSize", "1000000"));
        printHistogram = Boolean.parseBoolean(conf.getProperty("printHistogram"));
        printStatistics = Boolean.parseBoolean(conf.getProperty("printStatistics"));
        printTrie = Boolean.parseBoolean(conf.getProperty("printTrie"));
        verbose = Boolean.parseBoolean(conf.getProperty("verbose"));
        preprocessUnicodeEscapes = Boolean.parseBoolean(conf.getProperty("preprocessUnicodeEscapes"));
        languageLevel = conf.getProperty("languageLevel", "JAVA_8");
        sourceEncoding = conf.getProperty("sourceEncoding", "UTF-8");
        prepareParserConfiguration();
        countingVisitor = printStatistics ? new CountingVisitor() : null;
        statistics = printStatistics ? new Statistics() : null;
    }

    private void prepareParserConfiguration() {
        LanguageLevel lang = LanguageLevel.valueOf(languageLevel);
        Charset cs = Charset.forName(sourceEncoding);
        parserConfiguration.setPreprocessUnicodeEscapes(preprocessUnicodeEscapes);
        parserConfiguration.setLanguageLevel(lang);
        parserConfiguration.setCharacterEncoding(cs);
    }

    public static Engine instance(Properties conf) {
        boolean compressed = Boolean.parseBoolean(conf.getProperty("compressed"));
        boolean persistent = Boolean.parseBoolean(conf.getProperty("persistent"));
        if (persistent) {
            return compressed ? new CPEngine(conf) : new PlainPersistentEngine(conf);
        }
        return compressed ? new MemoryCompressedEngine(conf) : new MemoryPlainEngine(conf);
    }

    public CloneSet getCloneSet() {
        return cloneSet;
    }

    public abstract void findClones() throws Exception;

    public abstract void printTrie() throws Exception;
}
