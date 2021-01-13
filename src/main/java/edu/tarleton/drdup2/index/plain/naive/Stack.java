package edu.tarleton.drdup2.index.plain.naive;

import edu.tarleton.drdup2.rename.RenameStrategy;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * The implementation of stack.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Stack implements Iterable<StackNode> {

    private final String rename;
    private final Deque<StackNode> nodes = new ArrayDeque<>();

    public Stack(String rename) {
        this.rename = rename;
    }

    public void push() {
        RenameStrategy rs = RenameStrategy.instance(rename);
        StackNode p = new StackNode(rs);
        nodes.addLast(p);
    }

    public StackNode peek() {
        return nodes.peekLast();
    }

    public StackNode pop() {
        return nodes.removeLast();
    }

    @Override
    public Iterator<StackNode> iterator() {
        return nodes.iterator();
    }
}
