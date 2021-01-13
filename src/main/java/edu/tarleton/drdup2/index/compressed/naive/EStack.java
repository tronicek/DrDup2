package edu.tarleton.drdup2.index.compressed.naive;

import edu.tarleton.drdup2.rename.RenameStrategy;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * The implementation of stack.
 * 
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class EStack implements Iterable<EStackNode> {

    private final String rename;
    private final Deque<EStackNode> nodes = new ArrayDeque<>();

    public EStack(String rename) {
        this.rename = rename;
    }

    public void push() {
        RenameStrategy rs = RenameStrategy.instance(rename);
        EStackNode p = new EStackNode(rs);
        nodes.addLast(p);
    }

    public EStackNode peek() {
        return nodes.peekLast();
    }

    public EStackNode pop() {
        return nodes.removeLast();
    }

    @Override
    public Iterator<EStackNode> iterator() {
        return nodes.iterator();
    }
}
