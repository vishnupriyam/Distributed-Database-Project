package QueryTree;

import java.util.ArrayList;
import java.util.List;

import GlobalDefinition.Constant;
import GlobalDefinition.JoinExpression;
import GlobalDefinition.SimpleExpression;
import Parser.SelectItemsFinder;
import Parser.TableNamesFinder;
import Parser.WhereClauseDecomposition;
import Parser.WhereItemsFinder;
import WhereTree.WhereNode;
import WhereTree.WhereTree;

import net.sf.jsqlparser.statement.select.Select;

public class QueryTree {
	
	public class FormattedTreeNode{
		public String content;
		public int nodeID;
		public int parentID;
		public int siteID;
		public FormattedTreeNode(){
			this.content = null;
			this.nodeID = -1;
			this.parentID = -1;
			this.siteID = -1;
		}
	}

	private TreeNode root = null;
	private List<FormattedTreeNode> nodeList = null;
	private List<LeafNode> leafNodeList = null;
	
	private int nodeID;
	private int treeType;
	private String sql;
	
	public QueryTree(){
		this.nodeID = 0;
		this.treeType = 0;
		this.sql = null;
	}
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public boolean isValidTree(){
		return !(root == null);
	}
	
	public void displayTree(){
		recDisplayTree(root,0,0);
	}
	
	
	private void recDisplayTree(TreeNode thisNode, int level, int childNumber) {
		// TODO Auto-generated method stub
		System.out.print("level = " + level +" child = " + childNumber + ": " );
		System.out.print(thisNode.getContent());
		System.out.println(" siteID = " + thisNode.getSiteID() + " nodeID = " + thisNode.getNodeID());
		//if(thisNode.getParentNode() != null)
			//System.out.println("parent node Id : " + thisNode.getParentNode().getNodeID());
		int childCount = thisNode.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TreeNode nextNode = thisNode.getChildList().get(i);
			if(nextNode != null){
				recDisplayTree(nextNode,level+1,i);
			}
			else
				return;
		}
		
	}

	public void generateTreeList(){
		if(root == null) return;
		nodeList = new ArrayList<FormattedTreeNode>();
		generateTreeListByNode(root);
	}
	
	
	private void generateTreeListByNode(TreeNode node) {
		// TODO Auto-generated method stub
		FormattedTreeNode n = new FormattedTreeNode();
		n.content = node.getContent();
		n.nodeID = node.getNodeID();
		n.parentID = (node.getParentNode() == null)?-1:node.getParentNode().getNodeID();
		n.siteID = node.getSiteID();
		nodeList.add(n);
		if(node.isLeaf())
			return;
		for (int i = 0; i < node.getChildCount(); i++) {
			generateTreeListByNode(node.getChild(i));
		}
	}
	
	private void setSiteIdOnNodes() {
		if(this.root == null) return;
		setSiteIdOnNodesByChild(root);
	}
	

	private int setSiteIdOnNodesByChild(TreeNode node) {
		// TODO Auto-generated method stub
		if(node.isLeaf()) return node.getSiteID();
		List<TreeNode> childList = node.getChildList();
		for (int i = 0; i < childList.size(); i++) {
			setSiteIdOnNodesByChild(childList.get(i));
		}
		node.setNodeID(setSiteIdOnNodesByChild(node.getChild(0)));
		return node.getSiteID();
	}
	
	

	public List<FormattedTreeNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<FormattedTreeNode> nodeList) {
		this.nodeList = nodeList;
	}

	public List<LeafNode> getLeafNodeList() {
		if(this.root ==null)
			return null;
		leafNodeList = new ArrayList<LeafNode>();
		getLeafNodeList(root);
		return leafNodeList;
	}

	private void getLeafNodeList(TreeNode node) {
		if(node.isLeaf()){
			leafNodeList.add((LeafNode)node);
			return;
		}
		for (int i = 0; i < node.getChildCount(); i++) {
			getLeafNodeList(node.getChild(i));
		}
	}
	
	private LeafNode findLeafNode(String tableName,ArrayList<LeafNode> leaves) {
		if(leaves.size() == 0) return null;
		for (int i = 0; i < leaves.size(); i++) {
			if (leaves.get(i).getTableName().equalsIgnoreCase(tableName)) {
				return leaves.get(i);
			}
		}
		return null;
	}
	
	private void localisation(LeafNode leafNode) {
		if(leafNode.hasSegment()) return;
		UnionNode unionNode = new UnionNode();
		
		unionNode.setParentNode(leafNode.getParentNode());
		leafNode.getParentNode().removeChildNode(leafNode);
		leafNode.setParentNode(null);
		unionNode.setNodeID(leafNode.getNodeID());
		String tableName = leafNode.getTableName();
		/*GDD gdd = GDD.getInstance();
		  List<String> subTableList = (List<String>)gdd.getTableFragList(tableName);
		  
		  TODO Look at fragmenting conditions and attach the appropriate leaf nodes from the site to the union node
		 * */
	}

	public void setLeafNodeList(List<LeafNode> leafNodeList) {
		this.leafNodeList = leafNodeList;
	}
	
	

	public void setNodeID(int nodeID){
		this.nodeID = nodeID;
	}
	
	public int getNodeID(){
		return this.nodeID;
	}
	
	public void setTreeType(int treeType){
		this.treeType = treeType;
	}
	
	public int getTreeType(){
		return this.treeType;
	}
	
	public void setSQL(String sql){
		this.sql = sql;
	}
	
	public String getSQL(){
		return this.sql;
	}
	
	public void genSelectTree(Select select){
		this.treeType = Constant.TREE_SELECT;
		
		/***** Select Clause *****/
		SelectItemsFinder selectItemsFinder = new SelectItemsFinder();
		ArrayList<String> selectItemsList = selectItemsFinder.getSelectItemsList(select);
		String attributes = new String();
		ProjectionNode node = new ProjectionNode();
		node.setNodeName("PROJECTION");
		node.setNodeID(-1);
		for (int i = 0; i < selectItemsList.size(); i++) {
			attributes += selectItemsList.get(i) + 
							(i < selectItemsList.size() - 1?",":"");
			int pos = selectItemsList.get(i).indexOf(".");
			if(pos == -1){
				node.addTableName(null);
				node.addAttribute(selectItemsList.get(i));
			}
			else{
				String tableName = selectItemsList.get(i).substring(0, pos);
				String attrName = selectItemsList.get(i).substring(pos+1);
				node.addTableName(tableName);
				node.addAttribute(attrName);
			}
		}
		node.setRoot(true);
		node.setParentNode(null);
		node.setNodeID(this.nodeID);
		this.nodeID++;	
		root = node;
		
		//System.out.println(attributes);
		
		/***** from clause *****/
		ArrayList<LeafNode> leaves = new ArrayList<LeafNode>();
		TableNamesFinder tableNamesFinder = new TableNamesFinder();
		ArrayList<String> tableList = (ArrayList<String>) tableNamesFinder.getTablesList(select);
		for (int i = 0; i < tableList.size(); i++) {
			LeafNode leafNode = new LeafNode();
			leafNode.setNodeName(tableList.get(i));
			leafNode.setTableName(tableList.get(i));
			leafNode.setSegment(false);
			leafNode.setNodeID(this.nodeID);
			this.nodeID++;
			leaves.add(leafNode);
		}
		
		/*
		for (int i = 0; i < leaves.size(); i++) {
			System.out.print(leaves.get(i).getTableName()+" ");
		}
		System.out.println();
		*/
		
		/***** where tree *****/
		 WhereClauseDecomposition wc = new WhereClauseDecomposition(select);
	     WhereNode wn = wc.getWhereTree().toCNF(wc.getWhereTree().getRoot());
	     WhereTree wt = new WhereTree();
	     wt.setRoot(wn);
	     wt.collectJoins(wt.getRoot());
	     ArrayList<JoinExpression> joinList = wt.getJeList();
	     ArrayList<SimpleExpression> selectionList = wt.getSeList();
	    
	     /*
	     System.out.println("\n***** Join List *****\n");
	    for (int i = 0; i < joinList.size(); i++) {
			System.out.println(joinList.get(i).leftTableName
								+"."
								+joinList.get(i).leftColumn
								+" = "
								+joinList.get(i).rightTableName
								+"."
								+joinList.get(i).rightColumn);
		}
	    
	    System.out.println("\n***** Selection List *****\n");
	    
	    for (int i = 0; i < selectionList.size(); i++) {
			System.out.println(selectionList.get(i).tableName
								+"."
								+selectionList.get(i).columnName
								+selectionList.get(i).op
								+selectionList.get(i).value 
								+"\t" 
								+ selectionList.get(i).valType 
								+ " "
								+ selectionList.get(i).valueType);
		}
		*/
	     /*------where clause----*/
			
		WhereItemsFinder finder3 = new WhereItemsFinder(select); //COMPLETE WHERE ITWEMS FINDER
		/*------join clause----*/
		ArrayList<JoinNode> joins = new ArrayList<JoinNode>();
		for(int i=0;i<joinList.size();++i){
			JoinNode node2 = new JoinNode();
			node2.setLeftTableName(joinList.get(i).leftTableName);
			node2.setRightTableName(joinList.get(i).rightTableName);
			node2.addAttribute(joinList.get(i).leftColumn, joinList.get(i).rightColumn);
			node2.setNodeName("JOIN");
			node2.setNodeID(this.nodeID);
			this.nodeID++;
			joins.add(node2);
				
		}
		
		/*-------selection clause-------*/	

		ArrayList<SelectionNode> selections = new ArrayList<SelectionNode>();
		for(int i=0;i<selectionList.size();++i){
			SelectionNode node3 = new SelectionNode();
			node3.setNodeName("SELECTION");
			node3.addCondition(selectionList.get(i));
			node3.setNodeID(this.nodeID);
			node3.setTableName(selectionList.get(i).tableName);
			this.nodeID++;
			selections.add(node3);
		}
		
		for(int i=0;i<selections.size();++i){
			SelectionNode snode = selections.get(i);
			TreeNode child = findLeafNode(snode.getTableName(), leaves);
			while (child.getParentNode()!=null) child = child.getParentNode();
			child.setParentNode(snode);
		}
		
		for(int i=0;i<joins.size();++i){
			JoinNode jnode = joins.get(i);
			TreeNode leftChild = findLeafNode(jnode.getLeftTableName(),leaves);
			TreeNode rightChild = findLeafNode(jnode.getRightTableName(),leaves);
			while(leftChild.getParentNode()!= null) leftChild = leftChild.getParentNode();
			while(rightChild.getParentNode()!=null) rightChild = rightChild.getParentNode();
			leftChild.setParentNode(jnode);
			rightChild.setParentNode(jnode);
		}
		
		
		
		TreeNode leaf1 = leaves.get(0);
		while(leaf1.getParentNode()!= null) {
			leaf1 = leaf1.getParentNode();
		}
		leaf1.setParentNode(root);
		
		
	     
	}
}
