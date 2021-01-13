package edu.tarleton.drdup2.rename;

import edu.tarleton.drdup2.symtab.Entry;

/**
 * The renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public abstract class RenameStrategy {

    public static RenameStrategy instance(String name) {
        switch (name) {
            case "blind":
                return new BlindRename();
            case "consistent":
                return new ConsistentRename();
        }
        System.out.println("missing renaming strategy, using default...");
        return new BlindRename();
    }

    public abstract void enterBlock();

    public abstract void exitBlock();

    public abstract String declare(String name);

    public abstract String declareVar(String name, String type);

    public abstract String declareGlobal(String name);

    public abstract String rename(String name);
    
    public abstract Entry lookup(String name);
}
