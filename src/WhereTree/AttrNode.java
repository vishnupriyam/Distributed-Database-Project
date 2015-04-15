package WhereTree;

public class AttrNode implements WhereNode {

	private WhereNode parent;
	private int nodeType;
	private String attrName;
	private String tableName;
	public AttrNode(){
		nodeType = 3;
	}
	@Override
	public void accept(WhereNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public WhereNode getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void setParent(WhereNode parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}

	@Override
	public WhereNode getLeftChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLeftChild(WhereNode leftChild) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WhereNode getRightChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRightChild(WhereNode rightChild) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsLeaf() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeName(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getNodeType() {
		// TODO Auto-generated method stub
		return nodeType;
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		System.out.println(tableName+"."+attrName);
	}

}
