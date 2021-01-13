package edu.tarleton.drdup2;

import com.github.javaparser.Range;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
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
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * The visitor that counts lines of code and AST nodes.
 *
 * @author Zdenek Tronicek, tronicek@tarleton.edu
 */
public class CountingVisitor extends VoidVisitorAdapter<Void> {

    private int lines;
    private int nodes;

    public int getLines() {
        return lines;
    }

    public int getNodes() {
        return nodes;
    }

    @Override
    public void visit(ArrayAccessExpr n, Void arg) {
        nodes++;
        n.getName().accept(this, arg);
        n.getIndex().accept(this, arg);
    }

    @Override
    public void visit(ArrayCreationExpr n, Void arg) {
        nodes++;
        n.getElementType().accept(this, arg);
        n.getLevels().forEach(p -> p.accept(this, arg));
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ArrayCreationLevel n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getDimension().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ArrayInitializerExpr n, Void arg) {
        nodes++;
        n.getValues().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ArrayType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComponentType().accept(this, arg);
    }

    @Override
    public void visit(AssertStmt n, Void arg) {
        nodes++;
        n.getCheck().accept(this, arg);
        n.getMessage().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        nodes++;
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
    }

    @Override
    public void visit(BinaryExpr n, Void arg) {
        nodes++;
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
    }

    @Override
    public void visit(BlockStmt n, Void arg) {
        nodes++;
        n.getStatements().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(BooleanLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(BreakStmt n, Void arg) {
        nodes++;
        n.getLabel().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(CastExpr n, Void arg) {
        nodes++;
        n.getType().accept(this, arg);
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(CatchClause n, Void arg) {
        nodes++;
        n.getParameter().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(CharLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(ClassExpr n, Void arg) {
        nodes++;
        n.getType().accept(this, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ClassOrInterfaceType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
    }

    @Override
    public void visit(CompilationUnit n, Void arg) {
        nodes++;
        n.getTypes().forEach(p -> p.accept(this, arg));
        Range r = n.getRange().orElse(null);
        lines += r.end.line - r.begin.line + 1;
    }

    @Override
    public void visit(ConditionalExpr n, Void arg) {
        nodes++;
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(ContinueStmt n, Void arg) {
        nodes++;
        n.getLabel().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        nodes++;
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
    }

    @Override
    public void visit(DoubleLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(EmptyStmt n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(EnclosedExpr n, Void arg) {
        nodes++;
        n.getInner().accept(this, arg);
    }

    @Override
    public void visit(EnumConstantDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getClassBody().accept(this, arg);
    }

    @Override
    public void visit(EnumDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getEntries().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void arg) {
        nodes++;
        n.getExpression().ifPresent(p -> p.accept(this, arg));
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        n.getArguments().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ExpressionStmt n, Void arg) {
        nodes++;
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        nodes++;
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        n.getScope().accept(this, arg);
        n.getName().accept(this, arg);
    }

    @Override
    public void visit(final FieldDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        nodes++;
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        nodes++;
        n.getInitialization().forEach(p -> p.accept(this, arg));
        n.getCompare().ifPresent(p -> p.accept(this, arg));
        n.getUpdate().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(IfStmt n, Void arg) {
        nodes++;
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        n.getElseStmt().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(InitializerDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(InstanceOfExpr n, Void arg) {
        nodes++;
        n.getExpression().accept(this, arg);
        n.getType().accept(this, arg);
    }

    @Override
    public void visit(IntegerLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(IntersectionType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getElements().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(LabeledStmt n, Void arg) {
        nodes++;
        n.getLabel().accept(this, arg);
        n.getStatement().accept(this, arg);
    }

    @Override
    public void visit(LambdaExpr n, Void arg) {
        nodes++;
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Void arg) {
        nodes++;
        n.getClassDeclaration().accept(this, arg);
    }

    @Override
    public void visit(LongLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(MarkerAnnotationExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(MemberValuePair n, Void arg) {
        nodes++;
        n.getName().accept(this, arg);
        n.getValue().accept(this, arg);
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        nodes++;
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        n.getName().accept(this, arg);
        n.getArguments().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getBody().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(MethodReferenceExpr n, Void arg) {
        nodes++;
        n.getScope().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
    }

    @Override
    public void visit(Modifier n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(Name n, Void arg) {
        nodes++;
        n.getQualifier().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(NameExpr n, Void arg) {
        nodes++;
        n.getName().accept(this, arg);
    }

    @Override
    public void visit(NormalAnnotationExpr n, Void arg) {
        nodes++;
        n.getName().accept(this, arg);
        n.getPairs().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(NullLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(ObjectCreationExpr n, Void arg) {
        nodes++;
        n.getScope().ifPresent(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        n.getTypeArguments().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getAnonymousClassBody().ifPresent(p -> p.forEach(v -> v.accept(this, arg)));
    }

    @Override
    public void visit(Parameter n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
    }

    @Override
    public void visit(PrimitiveType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ReturnStmt n, Void arg) {
        nodes++;
        n.getExpression().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(SimpleName n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Void arg) {
        nodes++;
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
    }

    @Override
    public void visit(StringLiteralExpr n, Void arg) {
        nodes++;
    }

    @Override
    public void visit(SuperExpr n, Void arg) {
        nodes++;
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(SwitchEntry n, Void arg) {
        nodes++;
        n.getLabels().forEach(p -> p.accept(this, arg));
        n.getStatements().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(SwitchExpr n, Void arg) {
        nodes++;
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(SwitchStmt n, Void arg) {
        nodes++;
        n.getSelector().accept(this, arg);
        n.getEntries().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(SynchronizedStmt n, Void arg) {
        nodes++;
        n.getExpression().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(ThisExpr n, Void arg) {
        nodes++;
        n.getTypeName().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(ThrowStmt n, Void arg) {
        nodes++;
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(TryStmt n, Void arg) {
        nodes++;
        n.getResources().forEach(p -> p.accept(this, arg));
        n.getTryBlock().accept(this, arg);
        n.getCatchClauses().forEach(p -> p.accept(this, arg));
        n.getFinallyBlock().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(TypeExpr n, Void arg) {
        nodes++;
        n.getType().accept(this, arg);
    }

    @Override
    public void visit(TypeParameter n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getTypeBound().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(UnaryExpr n, Void arg) {
        nodes++;
        n.getExpression().accept(this, arg);
    }

    @Override
    public void visit(UnionType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getElements().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(VariableDeclarationExpr n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(VariableDeclarator n, Void arg) {
        nodes++;
        n.getType().accept(this, arg);
        n.getName().accept(this, arg);
        n.getInitializer().ifPresent(p -> p.accept(this, arg));
    }

    @Override
    public void visit(VarType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(VoidType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        nodes++;
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(WildcardType n, Void arg) {
        nodes++;
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getExtendedType().ifPresent(p -> p.accept(this, arg));
        n.getSuperType().ifPresent(p -> p.accept(this, arg));
    }
}
