package GlobalDefinition;

public class SimpleExpression {
	public String tableName;
	public String columnName;
	public String op;
	public String value;
	public int valueType;
	public String valType;
	
	public SimpleExpression(){
		
	}
	
	public SimpleExpression(String tableName, String columnName, String op, String value, int valueType){
		this.tableName = tableName;
		this.columnName = columnName;
		this.op = op;
		this.value = value;
		this.valueType = valueType;
	}
	
	public SimpleExpression(SimpleExpression simpleExpression){
		this.tableName = simpleExpression.tableName;
		this.columnName = simpleExpression.columnName;
		this.op = simpleExpression.op;
		this.value = simpleExpression.value;
		this.valueType = simpleExpression.valueType;
	}

	public String getValType() {
		return Constant.DATATYPE[valueType];
	}

	public void setValType(String valType) {
		this.valType = valType;
	}
}
