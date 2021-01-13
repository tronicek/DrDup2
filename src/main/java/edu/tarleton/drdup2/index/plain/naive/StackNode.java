package edu.tarleton.drdup2.index.plain.naive;

import edu.tarleton.drdup2.rename.RenameStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * The representation of the stack node.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class StackNode {

    private final RenameStrategy renameStrategy;
    private final List<String> labels = new ArrayList<>();

    public StackNode(RenameStrategy renameStrategy) {
        this.renameStrategy = renameStrategy;
    }

    public RenameStrategy getRenameStrategy() {
        return renameStrategy;
    }

    public List<String> getLabels() {
        return labels;
    }
}
