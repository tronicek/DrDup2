package edu.tarleton.drdup2.index;

import edu.tarleton.drdup2.Histogram;
import edu.tarleton.drdup2.clones.CloneSet;
import edu.tarleton.drdup2.clones.Pos;
import java.util.List;

/**
 * The AST index.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public interface Index {

    final String[] STATEMENTS = {
        "AssertStmt",
        "BreakStmt",
        "ContinueStmt",
        "DoStmt",
        "EmptyStmt",
        "ExplicitConstructorInvocationStmt",
        "ExpressionStmt",
        "ForEachStmt",
        "ForStmt",
        "IfStmt",
        "LabeledStmt",
        "ReturnStmt",
        "SwitchStmt",
        "SynchronizedStmt",
        "ThrowStmt",
        "TryStmt",
        "WhileStmt"
    };

    void add(List<String> labels, Pos pos);

    void nextStmt(Pos prev, Pos curr);

    void print() throws Exception;

    CloneSet detectClonesType2(String level, int minSize, int maxSize) throws Exception;

    Histogram createHistogram() throws Exception;
}
