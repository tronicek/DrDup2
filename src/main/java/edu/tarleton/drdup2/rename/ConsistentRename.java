package edu.tarleton.drdup2.rename;

import edu.tarleton.drdup2.symtab.Entry;
import edu.tarleton.drdup2.symtab.SymbolTable;

/**
 * The consistent renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class ConsistentRename extends RenameStrategy {

    private SymbolTable symbolTable = new SymbolTable(true, null);

    @Override
    public void enterBlock() {
        symbolTable = new SymbolTable(false, symbolTable);
    }

    @Override
    public void exitBlock() {
        symbolTable = symbolTable.getNext();
    }

    @Override
    public String declare(String name) {
        return symbolTable.declare(name);
    }

    @Override
    public String declareVar(String name, String type) {
        return symbolTable.declareVar(name, type);
    }

    @Override
    public String declareGlobal(String name) {
        return symbolTable.declareGlobal(name);
    }

    @Override
    public String rename(String name) {
        return symbolTable.lookup(name);
    }

    @Override
    public Entry lookup(String name) {
        return symbolTable.lookupEntry(name);
    }
}
