package Parser;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;

public class TableNamesFinder implements SelectVisitor, FromItemVisitor,
		ExpressionVisitor, ItemsListVisitor {

	private ArrayList<String> tablesList;
	
	@Override
	public void visit(ExpressionList arg0) {
		// TODO Auto-generated method stub

	}

	public ArrayList<String> getTablesList(Select select) {
		tablesList = new ArrayList<String>();
		select.getSelectBody().accept(this);
		return tablesList;
	}

	@Override
	public void visit(NullValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Function arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(JdbcParameter arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DoubleValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LongValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DateValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TimeValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TimestampValue arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Parenthesis arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(StringValue arg0) {
		// TODO Auto-generated method stub

	}

	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}
	
	@Override
	public void visit(Addition arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(Division arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(Multiplication arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(Subtraction arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(AndExpression arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(OrExpression arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(Between arg0) {
		// TODO Auto-generated method stub
		arg0.getLeftExpression().accept(this);
		arg0.getBetweenExpressionStart().accept(this);
		arg0.getBetweenExpressionEnd().accept(this);
	}

	@Override
	public void visit(EqualsTo arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(GreaterThan arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(GreaterThanEquals arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(InExpression arg0) {
		// TODO Auto-generated method stub
		arg0.getLeftExpression().accept(this);
		arg0.getRightItemsList().accept(this);
		arg0.getLeftItemsList().accept(this);
	}

	@Override
	public void visit(IsNullExpression arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LikeExpression arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(MinorThan arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(MinorThanEquals arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(NotEqualsTo arg0) {
		// TODO Auto-generated method stub
		visitBinaryExpression(arg0);
	}

	@Override
	public void visit(Column arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CaseExpression arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WhenClause arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExistsExpression arg0) {
		// TODO Auto-generated method stub
		arg0.getRightExpression().accept(this);
	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		// TODO Auto-generated method stub
		arg0.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		// TODO Auto-generated method stub
		arg0.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(Concat arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Matches arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BitwiseAnd arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BitwiseOr arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BitwiseXor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Table arg0) {
		// TODO Auto-generated method stub
		String tableName =  arg0.getFullyQualifiedName();
		tablesList.add(tableName);
	}

	@Override
	public void visit(SubSelect arg0) {
		// TODO Auto-generated method stub
		arg0.getSelectBody().accept(this);
	}

	@Override
	public void visit(SubJoin arg0) {
		// TODO Auto-generated method stub
		arg0.getLeft().accept(this);
		arg0.getJoin().getRightItem().accept(this);
	}

	@Override
	public void visit(PlainSelect arg0) {
		// TODO Auto-generated method stub
		arg0.getFromItem().accept(this);
		if(arg0.getJoins() != null){
			for (Iterator joinsIterator = arg0.getJoins().iterator(); joinsIterator.hasNext(); ) {
				Join join = (Join) joinsIterator.next();
				join.getRightItem().accept(this);
			}
		}
		if(arg0.getWhere() != null){
			arg0.getWhere().accept(this);
		}
	}

	@Override
	public void visit(MultiExpressionList multiExprList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SignedExpression signedExpression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(JdbcNamedParameter jdbcNamedParameter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(CastExpression cast) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Modulo modulo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AnalyticExpression aexpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ExtractExpression eexpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntervalExpression iexpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(OracleHierarchicalExpression oexpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RegExpMatchOperator rexpr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ValuesList valuesList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SetOperationList setOpList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WithItem withItem) {
		// TODO Auto-generated method stub
		
	}

}
