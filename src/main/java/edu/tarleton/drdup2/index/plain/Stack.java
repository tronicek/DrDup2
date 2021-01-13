package edu.tarleton.drdup2.index.plain;

import edu.tarleton.drdup2.clones.Pos;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * The implementation of stack.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Stack implements Iterable<StackNode> {

    private final Deque<StackNode> nodes = new ArrayDeque<>();

    public void push(TrieNode node, Pos pos) {
        StackNode p = new StackNode(node, pos);
        nodes.addLast(p);
    }

    public StackNode pop() {
        return nodes.removeLast();
    }

    public boolean isOnTop(StackNode node) {
        return node == nodes.peekLast();
    }

    @Override
    public Iterator<StackNode> iterator() {
        return nodes.iterator();
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}
