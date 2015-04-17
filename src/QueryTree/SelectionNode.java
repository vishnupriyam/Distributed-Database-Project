package QueryTree;

import java.util.ArrayList;
import java.util.List;

import GlobalDefinition.SimpleExpression;

public class SelectionNode extends TreeNode {
	private String tableName;
	private List<SimpleExpression> condList;
	
	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "SELECTION";
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void accept(TreeNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		String content =  new String();
		content += "SELECTION: ";
		for (int i = 0; i < condList.size(); i++) {
			content += condList.get(i).tableName + "."
						+ condList.get(i).columnName 
						+ condList.get(i).op
						+ condList.get(i).value;
			if(i < condList.size() - 1){
				content += " and ";
			}
		}
		return content;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<SimpleExpression> getCondList() {
		return condList;
	}

	public void setCondList(List<SimpleExpression> condList) {
		this.condList = condList;
	}

	public void addCondition(SimpleExpression e){
		if(condList == null){
			condList = new ArrayList<SimpleExpression>();
		}
		condList.add(e);
	}
	
}
