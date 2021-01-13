package edu.tarleton.drdup2;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.Properties;

/**
 * The visitor that implements normalization, such as adding curly braces.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class NormalizingVisitor extends VoidVisitorAdapter<Void> {

    private static final String THIS = "_this_";
    private static final String SUPER = "_super_";
    private final boolean addBlocks;
    private final boolean concatenateStrings;
    private final boolean ignoreParentheses;
    private final boolean ignoreUnaryAtLiterals;
    private final boolean insertSuperCall;
    private final boolean treatSuperThisAsIdentifier;

    public NormalizingVisitor(Properties conf) {
        addBlocks = Boolean.parseBoolean(conf.getProperty("addBlocks", "false"));
        concatenateStrings = Boolean.parseBoolean(conf.getProperty("concatenateStrings", "false"));
        ignoreParentheses = Boolean.parseBoolean(conf.getProperty("ignoreParentheses", "false"));
        ignoreUnaryAtLiterals = Boolean.parseBoolean(conf.getProperty("ignoreUnaryAtLiterals", "false"));
        insertSuperCall = Boolean.parseBoolean(conf.getProperty("insertSuperCall", "false"));
        treatSuperThisAsIdentifier = Boolean.parseBoolean(conf.getProperty("treatSuperThisAsIdentifier", "false"));
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);
        if (concatenateStrings) {
            concatenateStrings(n);
        }
    }

    private void concatenateStrings(BinaryExpr n) {
        if (n.getOperator() == BinaryExpr.Operator.PLUS) {
            Expression left = n.getLeft();
            Expression right = n.getRight();
            if (left.isStringLiteralExpr() && right.isStringLiteralExpr()) {
                StringLiteralExpr concat = new StringLiteralExpr(left.toString() + right.toString());
                Node parent = n.getParentNode().get();
                parent.replace(n, concat);
                return;
            }
            if (left.isBinaryExpr() && right.isStringLiteralExpr()) {
                BinaryExpr n2 = left.asBinaryExpr();
                Expression right2 = n2.getRight();
                if (n2.getOperator() == BinaryExpr.Operator.PLUS
                        && right2.isStringLiteralExpr()) {
                    StringLiteralExpr concat = new StringLiteralExpr(right2.toString() + right.toString());
                    n2.replace(right2, concat);
                    Node parent = n.getParentNode().get();
                    parent.replace(n, n2);
                }
            }
        }
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        if (insertSuperCall) {
            insertSuperCall(n);
        }
        super.visit(n, arg);
    }

    private void insertSuperCall(ConstructorDeclaration n) {
        BlockStmt body = n.getBody();
        NodeList<Statement> stats = body.getStatements();
        if (stats.size() > 0) {
            Statement first = stats.get(0);
            if (!first.isExplicitConstructorInvocationStmt()) {
                ExplicitConstructorInvocationStmt superCall
                        = new ExplicitConstructorInvocationStmt(false, null, new NodeList<>());
                stats.addFirst(superCall);
            }
        }
    }

    @Override
    public void visit(EnclosedExpr n, Void arg) {
        if (ignoreParentheses) {
            removeParentheses(n);
        }
        super.visit(n, arg);
    }

    private void removeParentheses(EnclosedExpr n) {
        Node parent = n.getParentNode().get();
        parent.replace(n, n.getInner());
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        if (treatSuperThisAsIdentifier) {
            String name = n.isThis() ? THIS : SUPER;
            MethodCallExpr call = new MethodCallExpr(null, name, n.getArguments());
            ExpressionStmt stmt = new ExpressionStmt(call);
            replace(n, stmt);
        }
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        Statement body = n.getBody();
        if (addBlocks && !body.isBlockStmt()) {
            BlockStmt block = new BlockStmt();
            block.addStatement(body);
            n.setBody(block);
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        Statement body = n.getBody();
        if (addBlocks && !body.isBlockStmt()) {
            BlockStmt block = new BlockStmt();
            block.addStatement(body);
            n.setBody(block);
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        Statement thenStmt = n.getThenStmt();
        if (addBlocks && !thenStmt.isBlockStmt()) {
            BlockStmt block = new BlockStmt();
            block.addStatement(thenStmt);
            n.setThenStmt(block);
        }
        Statement elseStmt = n.getElseStmt().orElse(null);
        if (addBlocks && elseStmt != null && !elseStmt.isBlockStmt()) {
            BlockStmt block = new BlockStmt();
            block.addStatement(elseStmt);
            n.setElseStmt(block);
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(SuperExpr n, Void arg) {
        if (treatSuperThisAsIdentifier) {
            if (n.getTypeName().isPresent()) {
                Name tn = n.getTypeName().get();
                NameExpr scope = new NameExpr(tn.toString());
                FieldAccessExpr expr = new FieldAccessExpr(scope, SUPER);
                replace(n, expr);
            } else {
                NameExpr expr = new NameExpr(SUPER);
                replace(n, expr);
            }
        }
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        if (treatSuperThisAsIdentifier) {
            if (n.getTypeName().isPresent()) {
                Name tn = n.getTypeName().get();
                NameExpr scope = new NameExpr(tn.toString());
                FieldAccessExpr expr = new FieldAccessExpr(scope, THIS);
                replace(n, expr);
            } else {
                NameExpr expr = new NameExpr(THIS);
                replace(n, expr);
            }
        }
    }

    private void replace(Node orig, Node repl) {
        repl.setRange(orig.getRange().get());
        Node parent = orig.getParentNode().get();
        parent.replace(orig, repl);
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        if (ignoreUnaryAtLiterals) {
            removeUnary(n);
        }
        super.visit(n, arg);
    }

    private void removeUnary(UnaryExpr n) {
        switch (n.getOperator()) {
            case PLUS:
            case MINUS:
                Expression expr = n.getExpression();
                if (expr.isDoubleLiteralExpr()
                        || expr.isIntegerLiteralExpr()
                        || expr.isLongLiteralExpr()) {
                    Node parent = n.getParentNode().get();
                    parent.replace(n, expr);
                }
        }
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        Statement body = n.getBody();
        if (addBlocks && !body.isBlockStmt()) {
            BlockStmt block = new BlockStmt();
            block.addStatement(body);
            n.setBody(block);
        }
        super.visit(n, arg);
    }
}
