package QueryTree;

public class VJoinNode extends TreeNode {

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
		return "JOIN";
	}

}
