package edu.tarleton.drdup2.rename;

import edu.tarleton.drdup2.symtab.Entry;

/**
 * The blind renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class BlindRename extends RenameStrategy {

    @Override
    public void enterBlock() {
    }

    @Override
    public void exitBlock() {
    }

    @Override
    public String declare(String name) {
        return "id";
    }

    @Override
    public String declareVar(String name, String type) {
        return "id";
    }

    @Override
    public String declareGlobal(String name) {
        return "id";
    }

    @Override
    public String rename(String name) {
        return "id";
    }

    @Override
    public Entry lookup(String name) {
        return null;
    }
}
