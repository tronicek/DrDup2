package edu.tarleton.drdup2.symtab;

/**
 * The symbol table entry.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Entry {

    private final String name;
    private final String type;
    private final int count;

    public Entry(String name, String type, int count) {
        this.name = name;
        this.type = type;
        this.count = count;
    }

    public String norm() {
        return String.format("i%d", count);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("entry: %s, %s -> %s", name, type, norm());
    }
}
