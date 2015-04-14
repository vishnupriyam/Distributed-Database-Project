package QueryTree;

import java.util.ArrayList;

public class ProjectionNode extends TreeNode {

	private ArrayList<String> attributeList = new ArrayList<String>();
	private ArrayList<String> tableNameList = new ArrayList<String>();
	public ArrayList<String> getAttributeList() {
		return attributeList;
	}

	public void addAttribute(String attr){
		attributeList.add(attr);
	}
	
	public void addTableName(String name){
		tableNameList.add(name);
	}
	
	public int getAttributeCount() {
		return attributeList.size();
	}
	
	public ArrayList<String> getTableNameList() {
		return tableNameList;
	}

	public void setTableNameList(ArrayList<String> tableNameList) {
		this.tableNameList = tableNameList;
	}

	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "PROJECTION";
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
		String content = new String();
		content += "PROJECTION: ";
		for (int i = 0; i < attributeList.size(); i++) {
			content += tableNameList.get(i) + "."
						+ attributeList.get(i);
			if(i != attributeList.size() - 1){
				content += ", ";
			}
		}
		return content;
	}

}
