package edu.tarleton.drdup2.index;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import edu.tarleton.drdup2.index.compressed.CTrie;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The logger class.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class Logger {

    private static final Logger INSTANCE = new Logger();
    private final Deque<String> classes = new ArrayDeque<>();
    private final Deque<String> methods = new ArrayDeque<>();
    private boolean output;

    private Logger() {
    }

    public static Logger getInstance() {
        return INSTANCE;
    }

    public void turnOn() {
        output = true;
    }

    public void turnOff() {
        output = false;
    }

    public void enterClass(ClassOrInterfaceDeclaration clazz) {
        String cls = clazz.getNameAsString();
//        if (cls.equals("Command")) {
//            output = true;
//        }
        classes.addLast(cls);
    }

    public void exitClass(ClassOrInterfaceDeclaration cls) {
        String name = classes.removeLast();
//        if (name.equals("Command")) {
//            output = false;
//        }
    }

    public void enterMethod(MethodDeclaration method) {
        String cls = classes.peekLast();
        String m = formatMethod(method);
        String fqn = String.format("%s.%s", cls, m);
        methods.addLast(fqn);
//        if (fqn.equals("Command.isCacheable()boolean")) {
//            System.out.printf("--- entering method: %s ---%n", fqn);
//            output = true;
//        }
    }

    private String formatMethod(MethodDeclaration method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getNameAsString()).append("(");
        boolean comma = false;
        for (Parameter param : method.getParameters()) {
            if (comma) {
                sb.append(",");
            }
            sb.append(param.getType().asString());
            comma = true;
        }
        sb.append(")");
        if (method.getType() != null) {
            sb.append(method.getType().asString());
        }
        return sb.toString();
    }

    public void exitMethod(MethodDeclaration method) {
        String fqn = methods.removeLast();
        if (output) {
            System.out.printf("--- exiting method: %s ---%n", fqn);
            output = false;
        }
    }

    public void printTrie(CTrie trie) {
        if (output) {
            trie.print();
        }
    }

    public void printf(String format, Object... args) {
        if (output) {
            System.out.printf(format, args);
        }
    }

    public void println(Object o) {
        println(o.toString());
    }

    public void println(String s) {
        if (output) {
            System.out.println(s);
        }
    }
}
