package QueryTree;

import java.util.ArrayList;

public abstract class TreeNode {
	private TreeNode parentNode;
	private ArrayList<TreeNode> childList ;
	private int siteID;
	private int nodeID;
	private String nodeName;
	private boolean isRoot;
	public TreeNode(){
		parentNode = null;
		childList = new ArrayList<TreeNode>();
		siteID = -1;
		nodeID = -1;
		nodeName = null;
		isRoot = false;
	}
	
	public void setParentNode(TreeNode parentNode){
		this.parentNode = parentNode;
		if(parentNode != null)
			parentNode.addchild(this);
	}
	
	public TreeNode getParentNode(){
		return this.parentNode;
	}
	
	public ArrayList<TreeNode> getChildList(){
		return this.childList;
	}
	
	public void setSiteID(int siteID){
		this.siteID = siteID;
	}
	
	public int getSiteID(){
		return siteID;
	}
	
	public void setNodeID(int nodeID){
		this.nodeID = nodeID;
	}

	public int getNodeID() {
		return nodeID;
	}


	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public boolean isRoot(){
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	public void addchild(TreeNode child){
		this.childList.add(child);
	}
	
	public int getChildCount(){
		return this.childList.size();
	}
	
	public void removeChildAt(int index){
		TreeNode node = this.childList.get(index);
		this.childList.remove(index);
		node.setParentNode(null);
	}
	
	public int getChildIndex(TreeNode node){
		return this.childList.indexOf(node);
	}
	
	public void removeChildNode(TreeNode child){
		removeChildAt(getChildIndex(child));
	}
	
	public void displayNode(){
		System.out.println(nodeName);
	}
	
	public TreeNode getChild(int index){
		return this.childList.get(index);
	}
	
	public abstract String getNodeType();
	
	public abstract boolean isLeaf();
	
	public abstract void accept(TreeNodeVisitor visitor);
	
	public abstract String getContent();
	
}
