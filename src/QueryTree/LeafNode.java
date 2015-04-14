package QueryTree;

public class LeafNode extends TreeNode {

	private String tableName;
	private String columnName;
	private boolean segment = false;
	@Override
	public String getNodeType() {
		// TODO Auto-generated method stub
		return "LEAF";
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean hasSegment() {
		return segment;
	}

	public void setSegment(boolean segment) {
		this.segment = segment;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void accept(TreeNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return tableName;
	}

}
