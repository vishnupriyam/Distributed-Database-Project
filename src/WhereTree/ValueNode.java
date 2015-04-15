package WhereTree;

public class ValueNode implements WhereNode {

	private WhereNode parent;
	private int nodeType;
	private String value;
	private String valueType;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public ValueNode(){
		nodeType = 4;
	}
	
	public ValueNode(String value, String valueType){
		this.value = value;
		this.nodeType = 4;
		this.valueType = valueType;
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
		System.out.println(valueType+": "+value);
	}

}
