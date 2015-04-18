import java.util.ArrayList;
import java.util.List;

import GlobalDefinition.SimpleExpression;


public class ConditionCheck {
	private List<SimpleExpression> condList;

	public List<SimpleExpression> getCondList() {
		return this.condList;
	}

	public void setCondList(List<SimpleExpression> condList) {
		this.condList = condList;
	}
	
	public void addCondition(SimpleExpression e){
		if(this.condList == null){
			this.condList = new ArrayList<SimpleExpression>();
		}
		this.condList.add(e);
	}
	
	public void addCondList(List<SimpleExpression> conditionList){
		for (int i = 0; i < conditionList.size(); i++) {
			addCondition(conditionList.get(i));
		}
	}
	
	public void displayConditionList(){
		if(condList == null) return;
		SimpleExpression se;
		System.out.println("***** Condition List *****");
		for (int i = 0; i < condList.size(); i++) {
			se = condList.get(i);
			System.out.println(se.tableName + " " + se.columnName + " "
								+ se.op + " " + se.value);
		}
	}
	
	public boolean isRange(String attrName, int start, int end) throws NullPointerException {
		if(condList == null) return true;
		boolean flag = false;
		SimpleExpression se;
		for (int i = 0; i < condList.size(); i++) {
			se = condList.get(i);
			if(se.columnName.equals(attrName)){
				flag = true;
				//Only integers are considers to be fragmented
				int value = Integer.parseInt(se.value);
				if(se.op.equals("=")){
					return (start <= value) && (value <= end);
				}
				else if(se.op.equals(">")){
					return value < end;
				}
				else if(se.op.equals("<")){
					return value > start;
				}
				else if(se.op.equals(">=")){
					return value <= end;
				}
				else if(se.op.equals("<=")){
					return value >= start;
				}
				else if(se.op.equals("<>")){
					return !(value == start && value == end);
				}
				
			}
		}
		if(!flag) return true;
		return false;
	}
}
