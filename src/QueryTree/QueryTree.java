package QueryTree;

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
	
	private int nodeID;
	private int treeType;
	private String sql;
	
	public QueryTree(){
		this.nodeID = 0;
		this.treeType = 0;
		this.sql = null;
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

}
