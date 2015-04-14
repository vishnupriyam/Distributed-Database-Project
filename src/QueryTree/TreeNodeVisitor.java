package QueryTree;

public interface TreeNodeVisitor {
	public void visit(LeafNode leafNode);
	public void visit(JoinNode joinNode);
	public void visit(ProjectionNode projectionNode);
	public void visit(SelectionNode selectionNode);
	public void visit(UnionNode unionNode);
	public void visit(VJoinNode vjoinNode);
}
