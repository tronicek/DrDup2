package edu.tarleton.drdup2.index.compressed.naive;

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
import edu.tarleton.drdup2.index.CompressedIndexBuilder;
import edu.tarleton.drdup2.index.compressed.CTrie;
import edu.tarleton.drdup2.rename.RenameStrategy;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * The class that builds the simplified compressed statement-level index.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class SimplifiedCompressedIndexStmtBuilderNaive extends CompressedIndexBuilder {

    private final EStack stack;
    private CTrie trie = new CTrie();
    private String srcFile;
    private int inMethod;
    private Pos prevStmt;

    public SimplifiedCompressedIndexStmtBuilderNaive(Properties conf, Path srcDir) {
        super(conf, srcDir);
        stack = new EStack(rename);
    }

    @Override
    public CTrie getTrie() {
        return trie;
    }

    @Override
    public void reset() {
        trie = new CTrie();
    }

    private void declareVar(String name, String type) {
        for (EStackNode node : stack) {
            RenameStrategy rs = node.getRenameStrategy();
            rs.declareVar(name, type);
        }
    }

    private void enterBlock() {
        for (EStackNode node : stack) {
            RenameStrategy rs = node.getRenameStrategy();
            rs.enterBlock();
        }
    }

    private void exitBlock() {
        for (EStackNode node : stack) {
            RenameStrategy rs = node.getRenameStrategy();
            rs.exitBlock();
        }
    }

    private void addChild(Node n) {
        String lab = n.getClass().getSimpleName();
        addChild(lab);
    }

    private void addChild(String lab) {
        for (EStackNode node : stack) {
            List<String> labs = node.getLabels();
            labs.add(lab);
        }
    }

    private void addChildEnd(Node n) {
        String lab = n.getClass().getSimpleName() + "_end";
        addChild(lab);
    }

    private void addChildEnd(String lab) {
        addChild(lab + "_end");
    }

    private void addChildEndWithPos(Node n) {
        String lab = n.getClass().getSimpleName() + "_end";
        addChild(lab);
        EStackNode node = stack.peek();
        List<String> labs = node.getLabels();
        trie.add(labs, pos(n));
    }

    private void addId(String id) {
        for (EStackNode node : stack) {
            RenameStrategy rs = node.getRenameStrategy();
            String rid = rs.rename(id);
            if (rid == null) {
                rid = rs.declareGlobal(id);
            }
            List<String> labs = node.getLabels();
            labs.add(rid);
        }
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
        addChild(n);
        n.getName().accept(this, arg);
        n.getIndex().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(ArrayCreationExpr n, Void arg) {
        if (inMethod == 0) {
            n.getInitializer().ifPresent(p -> p.accept(this, arg));
            return;
        }
        addChild(n);
        n.getElementType().accept(this, arg);
        n.getLevels().forEach(p -> p.accept(this, arg));
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(ArrayCreationLevel n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getDimension().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(ArrayInitializerExpr n, Void arg) {
        if (inMethod == 0) {
            n.getValues().forEach(p -> p.accept(this, arg));
            return;
        }
        addChild(n);
        n.getValues().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(ArrayType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!treatArrayDeclEqual) {
            Origin orig = n.getOrigin();
            switch (orig.toString()) {
                case "NAME":
                    addChild("ATYPE_NAME");
                    break;
                case "TYPE":
                    addChild("ATYPE_TYPE");
                    break;
                default:
                    throw new RuntimeException("unknown array origin: " + orig);
            }
        }
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getComponentType().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(AssertStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getCheck().accept(this, arg);
        n.getMessage().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        String lab = "ASSIGN_" + n.getOperator();
        addChild(lab);
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
        addChildEnd(lab);
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        String lab = "BIN_" + n.getOperator();
        addChild(lab);
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
        addChildEnd(lab);
    }

    @Override
    public void visit(BlockStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        enterBlock();
        addChild(n);
        if (mergeClones) {
            n.getStatements().forEach(p -> nextStmt(p));
            endStmts();
        }
        n.getStatements().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
        exitBlock();
    }

    @Override
    public void visit(BooleanLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(BreakStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getLabel().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(CastExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getType().accept(this, arg);
        n.getExpression().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        enterBlock();
        addChild(n);
        n.getParameter().accept(this, arg);
        n.getBody().accept(this, arg);
        addChildEnd(n);
        exitBlock();
    }

    @Override
    public void visit(CharLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(ClassExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getType().accept(this, arg);
        addChildEnd(n);
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
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        enterBlock();
        if (n.getTypeParameters().isNonEmpty()) {
            addChild("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            addChild("TYPE_PARAMS_END");
        }
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        if (n.getImplementedTypes().isNonEmpty()) {
            addChild("IMPLEMENTS");
            n.getImplementedTypes().forEach(p -> p.accept(this, arg));
            addChild("IMPLEMENTS_END");
        }
        n.getMembers().forEach(p -> p.accept(this, arg));
        exitBlock();
        addChildEnd(n);
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        if (!ignoreClassScope) {
            n.getScope().ifPresent(p -> p.accept(this, arg));
        }
        n.getName().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            addChild("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            addChild("TYPE_ARGS_END");
        }
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
        addChild(n);
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        if (inMethod == 0) {
            inMethod++;
            if (!ignoreAnnotations) {
                n.getAnnotations().forEach(p -> p.accept(this, arg));
            }
            n.getModifiers().forEach(p -> p.accept(this, arg));
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            n.getName().accept(this, arg);
            n.getParameters().forEach(p -> p.accept(this, arg));
            n.getThrownExceptions().forEach(p -> p.accept(this, arg));
            n.getBody().accept(this, arg);
            inMethod--;
            return;
        }
        inMethod++;
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        if (n.getTypeParameters().isNonEmpty()) {
            addChild("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            addChild("TYPE_PARAMS_END");
        }
        n.getName().accept(this, arg);
        addChild("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        addChild("PARAMS_END");
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
        addChildEnd(n);
        inMethod--;
    }

    @Override
    public void visit(ContinueStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getLabel().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(DoubleLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(EmptyStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(EnclosedExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getInner().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        stack.push();
        addChild(n);
        String ref = n.isThis() ? "this" : "super";
        addChild(ref);
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        if (n.getTypeArguments().isPresent()) {
            addChild("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            addChild("TYPE_ARGS_END");
        }
        addChild("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        addChild("ARGS_END");
        addChildEnd(ref);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getExpression().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getScope().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            addChild("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            addChild("TYPE_ARGS_END");
        }
        addId(n.getNameAsString());
        addChildEnd(n);
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        enterBlock();
        stack.push();
        addChild(n);
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        addChildEndWithPos(n);
        exitBlock();
        stack.pop();
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        enterBlock();
        stack.push();
        addChild(n);
        addChild("INIT");
        n.getInitialization().forEach(p -> p.accept(this, arg));
        addChild("COND");
        n.getCompare().ifPresent(p -> p.accept(this, arg));
        addChild("UPDATE");
        n.getUpdate().forEach(p -> p.accept(this, arg));
        addChild("UPDATE_END");
        n.getBody().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
        exitBlock();
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        n.getElseStmt().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(InitializerDeclaration n, Void arg) {
        if (inMethod == 0) {
            inMethod++;
            if (!ignoreAnnotations) {
                n.getAnnotations().forEach(p -> p.accept(this, arg));
            }
            n.getBody().accept(this, arg);
            inMethod--;
            return;
        }
        inMethod++;
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        String type = n.isStatic() ? "STATIC_INIT_DECL" : "INIT_DECL";
        addChild(type);
        n.getBody().accept(this, arg);
        addChildEnd(n);
        inMethod--;
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getExpression().accept(this, arg);
        n.getType().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(IntersectionType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getElements().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(LabeledStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getLabel().accept(this, arg);
        n.getStatement().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(LambdaExpr n, Void arg) {
        if (inMethod == 0) {
            inMethod++;
            n.getBody().accept(this, arg);
            inMethod--;
            return;
        }
        enterBlock();
        addChild(n);
        addChild("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        addChild("PARAMS_END");
        n.getBody().accept(this, arg);
        addChildEnd(n);
        exitBlock();
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getClassDeclaration().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(LongLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getName().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(MemberValuePair n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getName().accept(this, arg);
        n.getValue().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        if (inMethod == 0) {
            n.getArguments().forEach(p -> p.accept(this, arg));
            return;
        }
        addChild(n);
        n.getScope().ifPresent(p -> p.accept(this, arg));
        if (n.getTypeArguments().isPresent()) {
            addChild("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            addChild("TYPE_ARGS_END");
        }
        addId(n.getNameAsString());
        addChild("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        addChild("ARGS_END");
        addChildEnd(n);
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        if (!n.getBody().isPresent()) {
            return;
        }
        if (inMethod == 0) {
            if (!ignoreAnnotations) {
                n.getAnnotations().forEach(p -> p.accept(this, arg));
            }
            n.getModifiers().forEach(p -> p.accept(this, arg));
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            n.getType().accept(this, arg);
            n.getName().accept(this, arg);
            n.getParameters().forEach(p -> p.accept(this, arg));
            n.getThrownExceptions().forEach(p -> p.accept(this, arg));
            inMethod++;
            n.getBody().ifPresent(p -> p.accept(this, arg));
            inMethod--;
            return;
        }
        enterBlock();
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        if (n.getTypeParameters().isNonEmpty()) {
            addChild("TYPE_PARAMS");
            n.getTypeParameters().forEach(p -> p.accept(this, arg));
            addChild("TYPE_PARAMS_END");
        }
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        addChild("PARAMS");
        n.getParameters().forEach(p -> p.accept(this, arg));
        addChild("PARAMS_END");
        if (n.getThrownExceptions().isNonEmpty()) {
            addChild("THROWS");
            n.getThrownExceptions().forEach(p -> p.accept(this, arg));
            addChild("THROWS_END");
        }
        n.getBody().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
        exitBlock();
    }

    @Override
    public void visit(MethodReferenceExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getScope().accept(this, arg);
        addId(n.getIdentifier());
        addChild("TYPE_ARGS");
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        addChild("TYPE_ARGS_END");
        addChildEnd(n);
    }

    @Override
    public void visit(Modifier n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        String mod = n.getKeyword().asString().toUpperCase();
        addChild(mod);
    }

    @Override
    public void visit(Name n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getQualifier().ifPresent(p -> p.accept(this, arg));
        addId(n.getIdentifier());
        addChildEnd(n);
    }

    @Override
    public void visit(NameExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getName().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(NormalAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getName().accept(this, arg);
        n.getPairs().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(NullLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        if (treatNullAsLiteral) {
            addChild("Literal");
        } else {
            addChild(n);
        }
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        if (inMethod == 0) {
            n.getArguments().forEach(p -> p.accept(this, arg));
            n.getAnonymousClassBody().ifPresent(p -> p.forEach((v -> v.accept(this, arg))));
            return;
        }
        addChild(n);
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        if (n.getTypeArguments().isPresent()) {
            addChild("TYPE_ARGS");
            n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
            addChild("TYPE_ARGS_END");
        }
        addChild("ARGS");
        n.getArguments().forEach(p -> p.accept(this, arg));
        addChild("ARGS_END");
        n.getAnonymousClassBody().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        addChildEnd(n);
    }

    @Override
    public void visit(Parameter n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        if (n.isVarArgs()) {
            addChild("VAR_ARGS");
        }
        declareVar(n.getNameAsString(), n.getTypeAsString());
        addId(n.getNameAsString());
        addChildEnd(n);
    }

    @Override
    public void visit(PrimitiveType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        addId(n.asString());
    }

    @Override
    public void visit(ReturnStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(SimpleName n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addId(n.getIdentifier());
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(StringLiteralExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild("Literal");
    }

    @Override
    public void visit(SuperExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getLabels().forEach(p -> p.accept(this, arg));
        if (mergeClones) {
            n.getStatements().forEach(p -> nextStmt(p));
            endStmts();
        }
        n.getStatements().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(SwitchExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(SynchronizedStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getExpression().accept(this, arg);
        n.getBody().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(ThrowStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getExpression().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(TryStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        enterBlock();
        stack.push();
        addChild(n);
        n.getResources().forEach(p -> p.accept(this, arg));
        n.getTryBlock().accept(this, arg);
        n.getCatchClauses().forEach(p -> p.accept(this, arg));
        n.getFinallyBlock().ifPresent(p -> p.accept(this, arg));
        addChildEndWithPos(n);
        stack.pop();
        exitBlock();
    }

    @Override
    public void visit(TypeExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        n.getType().accept(this, arg);
        addChildEnd(n);
    }

    @Override
    public void visit(TypeParameter n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getName().accept(this, arg);
        n.getTypeBound().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        String lab = "UNARY_" + n.getOperator();
        addChild(lab);
        n.getExpression().accept(this, arg);
        addChildEnd(lab);
    }

    @Override
    public void visit(UnionType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getElements().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        if (inMethod == 0) {
            n.getInitializer().ifPresent(p -> p.accept(this, arg));
            return;
        }
        addChild(n);
        n.getType().accept(this, arg);
        declareVar(n.getNameAsString(), n.getTypeAsString());
        addId(n.getNameAsString());
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
        addChildEnd(n);
    }

    @Override
    public void visit(VarType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        addChildEnd(n);
    }

    @Override
    public void visit(VoidType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        if (treatVoidAsType) {
            addId(n.asString());
        } else {
            addChild(n);
        }
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        stack.push();
        addChild(n);
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
        addChildEndWithPos(n);
        stack.pop();
    }

    @Override
    public void visit(WildcardType n, Void arg) {
        if (inMethod == 0) {
            return;
        }
        addChild(n);
        if (!ignoreAnnotations) {
            n.getAnnotations().forEach(p -> p.accept(this, arg));
        }
        if (n.getExtendedType().isPresent()) {
            addChild("EXTENDS");
            n.getExtendedType().ifPresent(p -> p.accept(this, arg));
            addChild("EXTENDS_END");
        }
        if (n.getSuperType().isPresent()) {
            addChild("SUPER");
            n.getSuperType().ifPresent(p -> p.accept(this, arg));
            addChild("SUPER_END");
        }
        addChildEnd(n);
    }
}
