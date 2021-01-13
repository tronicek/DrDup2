package edu.tarleton.drdup2.index.plain;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ArrayType.Origin;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import edu.tarleton.drdup2.clones.Pos;
import edu.tarleton.drdup2.index.IndexBuilder;
import java.nio.file.Path;
import java.util.Properties;

/**
 * The class that builds the full plain (not compressed) method-level index. It
 * uses the blind renaming strategy.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class FullIndexBuilder extends IndexBuilder {

    private final boolean methodLevel;
    private Trie trie = new Trie();
    private Stack stack = new Stack();
    private String srcFile;
    private int inMethod;
    private Pos prevStmt;

    public FullIndexBuilder(Properties conf, Path srcDir) {
        super(conf, srcDir);
        methodLevel = conf.getProperty("level", "method").equals("method");
    }

    @Override
    public Trie getIndex() {
        return trie;
    }

    @Override
    public void reset() {
        trie = new Trie();
    }

    private void append(Node n) {
        String type = n.getClass().getSimpleName();
        append(type);
    }

    private void append(String type) {
        Stack s = new Stack();
        for (StackNode node : stack) {
            TrieNode tnode = node.getNode();
            TrieNode p = tnode.addChild(type, null);
            s.push(p, node.getPos());
        }
        stack = s;
    }

    private void appendEnd(Node n) {
        String type = n.getClass().getSimpleName();
        append(type + "_end");
    }

    private void appendEnd(String type) {
        append(type + "_end");
    }

    private void appendEndMethod(Node n) {
        String type = n.getClass().getSimpleName() + "_end";
        if (methodLevel) {
            appendWithPos(type);
        } else {
            append(type);
        }
    }

    private void appendEndStmt(Node n) {
        String type = n.getClass().getSimpleName() + "_end";
        if (!methodLevel) {
            appendWithPos(type);
        } else {
            append(type);
        }
    }

    private void appendWithPos(String type) {
        Stack s = new Stack();
        for (StackNode node : stack) {
            Pos pos = node.getPos();
            TrieNode tnode = node.getNode();
            Pos pos2 = stack.isOnTop(node) ? pos : null;
            TrieNode p = tnode.addChild(type, pos2);
            s.push(p, pos);
        }
        stack = s;
    }

    private void appendId() {
        Stack s = new Stack();
        for (StackNode node : stack) {
            TrieNode tnode = node.getNode();
            TrieNode p = tnode.addChild("id", null);
            s.push(p, node.getPos());
        }
        stack = s;
    }

    private Pos pos(Node n) {
        Range r = n.getRange().orElse(null);
        Position begin = (r == null) ? null : r.begin;
        Position end = (r == null) ? null : r.end;
        return new Pos(srcFile, begin, end);
    }

    private void nextStmt(Statement stmt) {
        Pos pos = pos(stmt);
        if (prevStmt != null) {
            trie.nextStmt(prevStmt, pos);
        }
        prevStmt = pos;
    }

    private void endStmts() {
        prevStmt = null;
    }

    @Override
    public void visit(ArrayAccessExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getName().accept(this, arg);
        n.getIndex().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ArrayCreationExpr n, Void arg) {
        if (inMethod == 0) {
            n.getInitializer().ifPresent(p -> p.accept(this, arg));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getElementType().accept(this, arg);
        n.getLevels().forEach(p -> p.accept(this, arg));
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ArrayCreationLevel n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getDimension().ifPresent(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ArrayInitializerExpr n, Void arg) {
        if (inMethod == 0) {
            n.getValues().forEach(p -> p.accept(this, arg));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getValues().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ArrayType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!treatArrayDeclEqual) {
            Origin orig = n.getOrigin();
            switch (orig.toString()) {
                case "NAME":
                    append("ATYPE_NAME");
                    break;
                case "TYPE":
                    append("ATYPE_TYPE");
                    break;
                default:
                    throw new RuntimeException("unknown array origin: " + orig);
            }
        }
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getComponentType().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(AssertStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getCheck().accept(this, arg);
        n.getMessage().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        String type = "ASSIGN_" + n.getOperator();
        append(type);
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
        appendEnd(type);
        stack.pop();
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        String type = "BIN_" + n.getOperator();
        append(type);
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
        appendEnd(type);
        stack.pop();
    }

    @Override
    public void visit(BlockStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        if (mergeClones) {
            n.getStatements().forEach(p -> nextStmt(p));
            endStmts();
        }
        n.getStatements().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(BooleanLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(BreakStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getLabel().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(CastExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getType().accept(this, arg);
        n.getExpression().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getParameter().accept(this, arg);
        n.getBody().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(CharLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(ClassExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getType().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        if (inMethod == 0) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
            n.getModifiers().forEach(p -> p.accept(this, arg));
            n.getName().accept(this, arg);
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            n.getExtendedTypes().forEach(p -> p.accept(this, arg));
            n.getImplementedTypes().forEach(p -> p.accept(this, arg));
            n.getMembers().forEach(p -> p.accept(this, arg));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        if (n.getTypeParameters().isNonEmpty()) {
            append("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            append("TYPE_PARAMS_END");
        }
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        if (n.getImplementedTypes().isNonEmpty()) {
            append("IMPLEMENTS");
            n.getImplementedTypes().forEach(p -> p.accept(this, arg));
            append("IMPLEMENTS_END");
        }
        n.getMembers().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        if (!ignoreClassScope) {
            n.getScope().ifPresent(p -> p.accept(this, arg));
        }
        n.getName().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            append("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            append("TYPE_ARGS_END");
        }
        stack.pop();
    }

    @Override
    public void visit(CompilationUnit n, Void arg) {
        Path path = n.getStorage().get().getPath();
        Path rel = srcDir.relativize(path);
        srcFile = rel.toString();
        n.getTypes().forEach(p -> p.accept(this, arg));
        srcFile = null;
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        inMethod++;
        Pos pos = methodLevel ? pos(n) : null;
        stack.push(trie.getRoot(), pos);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        if (n.getTypeParameters().isNonEmpty()) {
            append("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            append("TYPE_PARAMS_END");
        }
        n.getName().accept(this, arg);
        append("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        append("PARAMS_END");
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
        appendEndMethod(n);
        stack.pop();
        inMethod--;
    }

    @Override
    public void visit(ContinueStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getLabel().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(DoubleLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(EmptyStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(EnclosedExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getInner().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        String ref = n.isThis() ? "this" : "super";
        append(ref);
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        if (n.getTypeArguments().isPresent()) {
            append("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            append("TYPE_ARGS_END");
        }
        append("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        append("ARGS_END");
        appendEnd(ref);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getExpression().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getScope().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            append("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            append("TYPE_ARGS_END");
        }
        n.getName().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        append("INIT");
        n.getInitialization().forEach(p -> p.accept(this, arg));
        append("COND");
        n.getCompare().ifPresent(p -> p.accept(this, arg));
        append("UPDATE");
        n.getUpdate().forEach(p -> p.accept(this, arg));
        append("UPDATE_END");
        n.getBody().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        n.getElseStmt().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(InitializerDeclaration n, Void arg) {
        inMethod++;
        Pos pos = methodLevel ? pos(n) : null;
        stack.push(trie.getRoot(), pos);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        String type = n.isStatic() ? "STATIC_INIT_DECL" : "INIT_DECL";
        append(type);
        n.getBody().accept(this, arg);
        appendEndMethod(n);
        stack.pop();
        inMethod--;
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getExpression().accept(this, arg);
        n.getType().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(IntegerLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(IntersectionType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getElements().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(LabeledStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getLabel().accept(this, arg);
        n.getStatement().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(LambdaExpr n, Void arg) {
        if (inMethod == 0) {
            inMethod++;
            stack.push(trie.getRoot(), null);
            n.getBody().accept(this, arg);
            stack.pop();
            inMethod--;
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        append("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        append("PARAMS_END");
        n.getBody().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getClassDeclaration().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(LongLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getName().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(MemberValuePair n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getName().accept(this, arg);
        n.getValue().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        if (inMethod == 0) {
            n.getArguments().forEach(p -> p.accept(this, arg));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getScope().ifPresent(p -> p.accept(this, arg));
        if (n.getTypeArguments().isPresent()) {
            append("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            append("TYPE_ARGS_END");
        }
        n.getName().accept(this, arg);
        append("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        append("ARGS_END");
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        if (!n.getBody().isPresent()) {
            return;
        }
        inMethod++;
        Pos pos = methodLevel ? pos(n) : null;
        stack.push(trie.getRoot(), pos);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        if (n.getTypeParameters().isNonEmpty()) {
            append("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            append("TYPE_PARAMS_END");
        }
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        append("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        append("PARAMS_END");
        if (n.getThrownExceptions().isNonEmpty()) {
            append("THROWS");
            n.getThrownExceptions().forEach(p -> p.accept(this, arg));
            append("THROWS_END");
        }
        n.getBody().ifPresent(p -> p.accept(this, arg));
        appendEndMethod(n);
        stack.pop();
        inMethod--;
    }

    @Override
    public void visit(MethodReferenceExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getScope().accept(this, arg);
        appendId();
        append("ARGS");
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        append("ARGS_END");
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(Modifier n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        String mod = n.getKeyword().asString().toUpperCase();
        append(mod);
    }

    @Override
    public void visit(Name n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getQualifier().ifPresent(p -> p.accept(this, arg));
        appendId();
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(NameExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getName().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(NormalAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append(n);
        n.getName().accept(this, arg);
        n.getPairs().forEach(p -> p.accept(this, arg));
        appendEnd(n);
    }

    @Override
    public void visit(NullLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        if (treatNullAsLiteral) {
            append("Literal");
        } else {
            append(n);
        }
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        if (inMethod == 0) {
            n.getArguments().forEach(p -> p.accept(this, arg));
            n.getAnonymousClassBody().ifPresent(p -> p.forEach((v -> v.accept(this, arg))));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            append("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            append("TYPE_ARGS_END");
        }
        append("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        append("ARGS_END");
        n.getAnonymousClassBody().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(Parameter n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        if (n.isVarArgs()) {
            append("VAR_ARGS");
        }
        n.getName().accept(this, arg);
        appendEnd(n);
    }

    @Override
    public void visit(PrimitiveType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        appendId();
        stack.pop();
    }

    @Override
    public void visit(ReturnStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(SimpleName n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        appendId();
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(StringLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        append("Literal");
    }

    @Override
    public void visit(SuperExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (mergeClones) {
            n.getStatements().forEach(p -> nextStmt(p));
            endStmts();
        }
        n.getLabels().forEach(p -> p.accept(this, arg));
        n.getStatements().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(SwitchExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(SynchronizedStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getExpression().accept(this, arg);
        n.getBody().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(ThrowStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getExpression().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(TryStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getResources().forEach(p -> p.accept(this, arg));
        n.getTryBlock().accept(this, arg);
        n.getCatchClauses().forEach(p -> p.accept(this, arg));
        n.getFinallyBlock().ifPresent(p -> p.accept(this, arg));
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(TypeExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getType().accept(this, arg);
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(TypeParameter n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getName().accept(this, arg);
        n.getTypeBound().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        String type = "UNARY_" + n.getOperator();
        append(type);
        n.getExpression().accept(this, arg);
        appendEnd(type);
        stack.pop();
    }

    @Override
    public void visit(UnionType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getElements().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        if (inMethod == 0) {
            n.getInitializer().ifPresent(p -> p.accept(this, arg));
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(VarType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        appendEnd(n);
        stack.pop();
    }

    @Override
    public void visit(VoidType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        if (treatVoidAsType) {
            appendId();
        } else {
            append(n);
        }
        stack.pop();
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        Pos pos = methodLevel ? null : pos(n);
        stack.push(trie.getRoot(), pos);
        append(n);
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
        appendEndStmt(n);
        stack.pop();
    }

    @Override
    public void visit(WildcardType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push(trie.getRoot(), null);
        append(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        if (n.getExtendedType().isPresent()) {
            append("EXTENDS");
            n.getExtendedType().ifPresent(p -> p.accept(this, arg));
            append("EXTENDS_END");
        }
        if (n.getSuperType().isPresent()) {
            append("SUPER");
            n.getSuperType().ifPresent(p -> p.accept(this, arg));
            append("SUPER_END");
        }
        appendEnd(n);
        stack.pop();
    }
}
