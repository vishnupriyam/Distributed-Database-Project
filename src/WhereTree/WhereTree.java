package WhereTree;

import java.util.ArrayList;
import java.util.Iterator;

import GlobalDefinition.JoinExpression;
import GlobalDefinition.SimpleExpression;
import SystemCatalogue.Tbl;

public class WhereTree {
	private WhereNode root =null;
	ArrayList<Tbl> tbl = null;
	String cstr = new String();
	ArrayList<JoinExpression> jeList = new ArrayList<JoinExpression>();
	ArrayList<SimpleExpression> seList = new ArrayList<SimpleExpression>();
	
	public WhereTree(){
	}
	
	public WhereNode getRoot(){
		return root;
	}
	
	public void setRoot(WhereNode root){
		this.root = root;
	}

	public ArrayList<JoinExpression> getJeList() {
		return jeList;
	}

	public void setJeList(ArrayList<JoinExpression> jeList) {
		this.jeList = jeList;
	}

	public ArrayList<SimpleExpression> getSeList() {
		return seList;
	}

	public void setSeList(ArrayList<SimpleExpression> seList) {
		this.seList = seList;
	}
	
	public void displayTree(){
		if(root == null)
			return;
		display(root);
	}
	
	private void display(WhereNode node){
		if(node.IsLeaf())	
			node.display();
		else{
			display(node.getLeftChild());
			node.display();
			display(node.getRightChild());
		}
	}
	
	public void collectJoins(WhereNode node){
		if(node instanceof OpNode){
			OpNode onode = (OpNode)node;
			AttrNode leftAttr = null;
			AttrNode rightAttr = null;
			
			if((onode.getLeftChild() instanceof AttrNode) && (onode.getRightChild() instanceof AttrNode)){
				
				JoinExpression je = new JoinExpression();
				leftAttr = (AttrNode) onode.getLeftChild();
				rightAttr = (AttrNode) onode.getRightChild();
				
				je.leftColumn = leftAttr.getAttrName();
				je.leftTableName = leftAttr.getTableName();
				
				je.op = onode.getOp();
				
				je.rightColumn = rightAttr.getAttrName();
				je.rightTableName = rightAttr.getTableName();
				
				jeList.add(je);
			}
			
			else{
				SimpleExpression se = new SimpleExpression();
				if(onode.getLeftChild() instanceof AttrNode){
					leftAttr = (AttrNode) onode.getLeftChild();
					se.columnName = leftAttr.getAttrName();
					se.tableName = leftAttr.getTableName();
					se.op = onode.getOp();
					
					ValueNode vnode = (ValueNode) onode.getRightChild();
					
					se.setValType(vnode.getValueType());
					se.value = vnode.getValue();
					
				}
				
				else{
					rightAttr = (AttrNode) onode.getRightChild();
					se.columnName = rightAttr.getAttrName();
					se.tableName = rightAttr.getTableName();
					se.op = onode.getOp();
					
					ValueNode vnode = (ValueNode) onode.getLeftChild();
					
					se.setValType(vnode.getValueType());
					se.value = vnode.getValue();
				}
				
			}
		}
		else{
			collectJoins(node.getLeftChild());
			collectJoins(node.getRightChild());
		}
	}
	
	public void validateWhere(ArrayList<Tbl> tbl, String cstr) throws Exception{
		this.tbl = tbl;
		this.cstr = cstr;
		if(root == null)
			return;
		validate(root);
	}

	private void validate(WhereNode node) throws Exception {
		// TODO Auto-generated method stub
		if((node instanceof ValueNode) || (node instanceof AttrNode)){
			return;
		}
		if((node instanceof AndNode) || (node instanceof OrNode)){
			validate(node.getLeftChild());
			validate(node.getRightChild());
			return;
		}
		if(node instanceof OpNode){
			OpNode onode = (OpNode) node;
			String op = onode.getOp();
			String[] S = null;
			String type = new String();
			String type1 = new String();
			if((node.getLeftChild() instanceof AttrNode) && (node.getRightChild() instanceof ValueNode)){
				AttrNode anode = (AttrNode) node.getLeftChild();
				String colName = anode.getAttrName();
				String tableName = anode.getTableName();
				
				ValueNode bNode = (ValueNode) node.getRightChild();
				String value =  bNode.getValue();
				type1 = bNode.getValueType();
				if(type1.matches("string"))
					type1="varchar";
				else if (type1.matches("long")) {
					type1 = "int";
				}
				boolean flag = false;
				if(tableName== null){
					for (Iterator iterator = tbl.iterator(); iterator
							.hasNext();) {
						Tbl t1 = (Tbl) iterator.next();
						S = t1.getColumnNames().split(":");
						int i;
						for (i = 0; i < S.length; i++) {
							if(S[i].matches(colName)){
								flag = true;
								break;
							}
						}
						if(flag == true){
							type = t1.getColumnTypes().split(":")[i];
							break;
						}
					}
				}
				else {
					for (Iterator iterator = tbl.iterator(); iterator
							.hasNext();) {
						Tbl t1 = (Tbl) iterator.next();
						if(tableName.equals(t1.getTblName()) && t1.getColumnNames().contains(colName)){
							int i;
							S = t1.getColumnNames().split(":");
							for (i = 0; i < S.length; i++) {
								if(colName.matches(S[i]))
									break;
							}
							type = t1.getColumnTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag = false){
					throw new Exception("Invalid where Clause");
				}
			}
			else if (node.getRightChild() instanceof AttrNode && node.getLeftChild() instanceof ValueNode) {
				AttrNode aNode = (AttrNode) node.getRightChild();
				String colName = aNode.getAttrName();
				String tableName = aNode.getTableName();
				ValueNode bnode = (ValueNode) node.getLeftChild();
				String value = bnode.getValue();
				type1 = bnode.getValueType();
				if(type1.matches("string")){
					type1 = "varchar";
				}
				else if(type1.matches("long")){
					type1 = "int";
				}
				boolean flag = false;
				if(tableName == null){
					for (Iterator iterator = tbl.iterator(); iterator
							.hasNext();) {
						Tbl t1 = (Tbl) iterator.next();
						S = t1.getColumnNames().split(":");
						int i;
						for (i = 0; i < S.length; i++) {
							if(S[i].matches(colName)){
								flag = true;
								break;
							}
						}
						if(flag == true){
							type = t1.getColumnTypes().split(":")[i];
							break;
						}
					}
				}
				else{
					for (Iterator iterator = tbl.iterator(); iterator
							.hasNext();) {
						Tbl t1 = (Tbl) iterator.next();
						if(tableName.equals(t1.getTblName()) && t1.getColumnNames().contains(colName)){
							int i;
							S = t1.getColumnNames().split(":");
							for (i = 0; i < S.length; i++) {
								if(colName.matches(S[i])){
									break;
								}
							}
							type = t1.getColumnTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag == false){
					throw new Exception("Invalid where Clause");
				}
			}
			else if(node.getLeftChild() instanceof AttrNode && node.getRightChild() instanceof AttrNode){
				AttrNode anode = (AttrNode) node.getLeftChild();
				String colName = anode.getAttrName();
				String tableName = anode.getTableName();
				AttrNode bnode =  (AttrNode)node.getRightChild();
				String colName1 = bnode.getAttrName();
				String tableName1 = bnode.getTableName();
				boolean flag = false;
				if(tableName == null){
					for (Iterator j = tbl.iterator(); j
							.hasNext();) {
						Tbl t1 = (Tbl) j.next();
						S = t1.getColumnNames().split(":");
						int i;
						for (i = 0; i < S.length; i++) {
							if(S[i].matches(colName)){
								flag = true;
								break;
							}
						}
						if(flag == true){
							type = t1.getColumnTypes().split(":")[i];
							break;
						}
					}
				}
				else{
					for (Iterator j = tbl.iterator(); j
							.hasNext();) {
						Tbl t1 = (Tbl) j.next();
						if(tableName.equals(t1.getTblName()) && t1.getColumnNames().contains(colName)){
							int i;
							S = t1.getColumnNames().split(":");
							for (i = 0; i < S.length; i++) {
								if(colName.matches(S[i]))
									break;
							}
							type = t1.getColumnTypes().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if(flag == false){
					throw new Exception("Invalid where Clause");
				}
				flag = false;
				if(tableName == null){
					////Line 279
					for (Iterator j = tbl.iterator(); j
							.hasNext();) {
						Tbl t1 = (Tbl) j.next();
						S = t1.getColumnNames().split(":");
						int i;
						for (i = 0; i < S.length; i++) {
							if (S[i].matches(colName)) {
								flag = true;
								break;
							}
						}
						if(flag){
							type = t1.getColumnTypes().split(":")[i];
							break;
						}
					}
					
				}
				
				else{
					for (Iterator j = tbl.iterator(); j
							.hasNext();) {
						Tbl t1 = (Tbl) j.next();
						if(tableName1.equals(t1.getTblName()) && t1.getColumnNames().contains(colName1)){
							S = t1.getColumnNames().split(":");
							int i;
							for (i = 0;  i < S.length; i++) {
								if (colName1.matches(S[i])) {
									break;
								}
							}
							type1 = t1.getColumnNames().split(":")[i];
							flag = true;
							break;
						}
					}
				}
				if (flag == false) {
					throw new Exception("invalid where clause");
				}
			}
			if(!(type.matches(type1))){
				throw new Exception("invalid where clause type miss match");
			}
		}
		else{
			throw new Exception("invalid where clause");
		}
		
		validate(node.getLeftChild());
		validate(node.getRightChild());
	}
	
	public WhereNode toCNF(WhereNode node){
		if(node.IsLeaf()){
			return node;
		}
		
		WhereNode cnfLeft = null,cnfRight = null;
		if(node.getLeftChild() != null)	
			cnfLeft = node.getLeftChild();
		if(node.getRightChild() != null)
			cnfRight = node.getRightChild();
		
		if(node instanceof OpNode){
			node.setLeftChild(cnfLeft);
			node.setRightChild(cnfRight);
			return node;
		}
		
		if(node instanceof AndNode){
			node.setLeftChild(cnfLeft);
			node.setRightChild(cnfRight);
			return node;
		}
		
		if(node instanceof OrNode){
			if( (cnfLeft == null || cnfLeft.IsLeaf() || cnfLeft instanceof OpNode || cnfLeft instanceof AndNode) 
				&& (cnfRight ==null || cnfRight.IsLeaf() || cnfRight instanceof OpNode || cnfRight instanceof OrNode)){
				node.setLeftChild(cnfLeft);
				node.setRightChild(cnfRight);
				return node;

			}
			else if ((cnfLeft != null && cnfLeft instanceof AndNode) && (cnfRight == null || cnfRight.IsLeaf() || cnfRight instanceof OpNode || cnfRight instanceof OrNode)) {
				 OrNode newLeft = new OrNode();
				 newLeft.setLeftChild(cnfLeft.getLeftChild());
				 newLeft.setRightChild(cnfRight);
				 WhereNode newLeft1 = toCNF(newLeft);
				 
				 OrNode newRight = new OrNode();
				 newRight.setLeftChild(cnfLeft.getRightChild());
				 newRight.setRightChild(cnfRight);
				 WhereNode newRight1 = toCNF(newRight);
				 
				 AndNode node1 = new AndNode();
				 node1.setLeftChild(newLeft1);
				 node1.setRightChild(newRight1);
				 return node1;
				 
			}
			
			else if((cnfRight != null && cnfRight instanceof AndNode) && (cnfLeft == null || cnfLeft.IsLeaf() || cnfLeft instanceof OpNode || cnfLeft instanceof OrNode)){
				OrNode newleft = new OrNode();
            	newleft.setLeftChild(cnfLeft);
            	newleft.setRightChild(cnfRight.getRightChild());
            	WhereNode newleft1 = toCNF(newleft);
            	
               OrNode newRight = new OrNode();
               newRight.setLeftChild(cnfLeft);
               newRight.setRightChild(cnfRight.getLeftChild());
               WhereNode newright1 = toCNF(newRight);

               AndNode node1 = new AndNode();
               node1.setLeftChild(newleft1);
               node1.setRightChild(newright1);
               return node1;
			}
			else if ((cnfLeft != null && cnfLeft instanceof AndNode)
                    && (cnfRight != null && cnfRight instanceof AndNode))
            {
            	AndNode newLeft = new AndNode();
            	
            	OrNode or1 = new OrNode();
            	or1.setLeftChild(cnfLeft.getLeftChild());
            	or1.setRightChild(cnfRight.getLeftChild());
            	
            	OrNode or2 = new OrNode();
            	or2.setLeftChild(cnfLeft.getRightChild());
            	or2.setRightChild(cnfRight.getLeftChild());
            	
                newLeft.setLeftChild(or1);
                newLeft.setRightChild(or2);

                WhereNode newLeft1 = toCNF(newLeft);
                
               AndNode newRight = new AndNode();
            	
            	OrNode or3 = new OrNode();
            	or3.setLeftChild(cnfLeft.getLeftChild());
            	or3.setRightChild(cnfRight.getRightChild());
            	
            	OrNode or4 = new OrNode();
            	or4.setLeftChild(cnfLeft.getRightChild());
            	or4.setRightChild(cnfRight.getRightChild());
            	
            	newRight.setLeftChild(or1);
            	newRight.setRightChild(or2);

                WhereNode newRight1 = toCNF(newRight);
                
                AndNode node1 = new AndNode();
                node1.setLeftChild(newLeft1);
                node1.setRightChild(newRight1);

                return node1;
            }
			
		}
		// error status, should NOT reach here
        System.out.println("Error Status");
        return null;
		
		
	}
	
}
