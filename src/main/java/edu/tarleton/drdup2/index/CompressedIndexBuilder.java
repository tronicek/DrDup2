package edu.tarleton.drdup2.index;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import edu.tarleton.drdup2.index.compressed.CTrie;
import java.nio.file.Path;
import java.util.Properties;

/**
 * The visitor that builds compressed index.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class CompressedIndexBuilder extends VoidVisitorAdapter<Void> {

    protected final String rename;
    protected final boolean ignoreAnnotations;
    protected final boolean ignoreClassScope;
    protected final boolean treatArrayDeclEqual;
    protected final boolean treatNullAsLiteral;
    protected final boolean treatVoidAsType;
    protected final boolean mergeClones;
    protected final Path srcDir;

    public CompressedIndexBuilder(Properties conf, Path srcDir) {
        rename = conf.getProperty("rename", "blind");
        ignoreAnnotations = Boolean.parseBoolean(conf.getProperty("ignoreAnnotations", "false"));
        ignoreClassScope = Boolean.parseBoolean(conf.getProperty("ignoreClassScope", "false"));
        treatArrayDeclEqual = Boolean.parseBoolean(conf.getProperty("treatArrayDeclEqual", "false"));
        treatNullAsLiteral = Boolean.parseBoolean(conf.getProperty("treatNullAsLiteral", "false"));
        treatVoidAsType = Boolean.parseBoolean(conf.getProperty("treatVoidAsType", "false"));
        mergeClones = "statements".equals(conf.getProperty("level"));
        this.srcDir = srcDir;
    }

    public abstract CTrie getTrie();

    public abstract void reset();
}
