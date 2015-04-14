package QueryTree;

import java.util.HashMap;
import java.util.Iterator;

public class JoinNode extends TreeNode {

	private HashMap<String, String> attributeList = new HashMap<String,String>();
	private String leftTableName = null;
	private String rightTableName = null;
	
	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "JOIN";
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
		content += "JOIN: ";
		for (Iterator<String> i = attributeList.keySet().iterator();i.hasNext();) {
			String key = i.next();
			String value = attributeList.get(key);
			content += leftTableName+"."+key
						+ "=" +
						rightTableName+"."+value;
			if(i.hasNext()){
				content += ", ";
			}
		}
		return content;
	}

	public String getLeftTableName() {
		return leftTableName;
	}

	public void setLeftTableName(String leftTableName) {
		this.leftTableName = leftTableName;
	}

	public String getRightTableName() {
		return rightTableName;
	}

	public void setRightTableName(String rightTableName) {
		this.rightTableName = rightTableName;
	}

	public HashMap<String, String> getAttributeList() {
		return attributeList;
	}

	public void addAttribute(String leftAttr, String rightAttr) {
		attributeList.put(leftAttr, rightAttr);
	}
	
	public void removeAttribute(String leftAttr) {
		if(attributeList.containsKey(leftAttr)){
			attributeList.remove(leftAttr);
		}
	}
}
