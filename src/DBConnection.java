import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;
import GlobalDefinition.JoinExpression;
import Parser.WhereItemsFinder;
import QueryTree.JoinNode;
import QueryTree.LeafNode;
import QueryTree.QueryTree;
import QueryTree.SelectionNode;
import QueryTree.TreeNode;


public class DBConnection {

	private final static String driver = "com.mysql.jdbc.Driver";
	
	private final static String select= "SELECT";
	private final static String space = " ";
	private final static String from = "FROM";
	private final static String where = "WHERE";
	private final static String comma = ",";
	private final static String and = "AND";
	private final static String or = "OR";
	private final static String allAttributes = "*";
	private final static String horizontalFragmentTable = "HorizontalFragmentation";
	private final static String tableName = "tableName";
	private final static String attributeName = "attributeName";
	private final static String siteID = "siteID";
	private final static String startValue = "startValue";
	private final static String endValue = "endValue";
	private final static String equals = "=";
	
	/**
	 *@param url of the server, database name, user name to access database, password for authentication
	 *@return object Connection if successful  
	 */
	public static Connection connectDB(String url,String dbName,String userName,String password){
		Connection conn = null;
		try {
		  Class.forName(driver).newInstance();
		  conn = DriverManager.getConnection(url+dbName,userName,password);
		  System.out.printf("Connected to the database %s at %s\n",dbName,url);
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		return conn;
	}
	
	/**
	 * @param args
	 * @throws JSQLParserException 
	 */
	public static void main(String[] args) throws JSQLParserException, SQLException {
		// TODO Auto-generated method stub
		Statement stmt,stmt1;
		ResultSet resultSet,resultSet2;
		Connection connection[] = new Connection[4];
		System.out.println("********** CONNECTION LOG **********");
		System.out.println();
		connection[0] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/","DDBProject","root","");
		connection[1] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/","DDBProject1","root","");
		connection[2] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/", "DDBProject2", "root", "");
		connection[3] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/", "DDBProject3", "root", ""); 
		System.out.println();
		/*try {
			stmt = conn1.createStatement();
			rs = stmt.executeQuery("SELECT * FROM faculty_master");
			while(rs.next()){
				String id = rs.getString("Faculty_Id");
				String name = rs.getString("Faculty_Name");
				String nation = rs.getString("Faculty_Email");
				System.out.println("ID : " + id + " name : " + name + " nation : " + nation);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		CCJSqlParserManager pm = new CCJSqlParserManager();
		  String sql = "SELECT Book.title,Publisher.name,Customer.name,Orders.quantity FROM Publisher,Book,Customer,Orders WHERE Publisher.publisherId = Book.Publisher_publisherId AND Publisher.publisherId = 10 AND Customer.customerId = Orders.Customer_customerId AND Orders.Book_bookId = Book.bookId AND Book.bookId = 12 AND Customer.rank >= 2";
		  Select statement = (Select) pm.parse(new StringReader(sql));
		  /* 
		  now you should use a class that implements StatementVisitor to decide what to do
		  based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
		  interested in SELECTS
		  */
		  if (statement instanceof Select) {
		  	Select selectStatement = (Select) statement;
		  	
		  	QueryTree queryTree = new QueryTree();
		  	queryTree.genSelectTree(selectStatement);
		  	queryTree.generateTreeList();
		  	System.out.println("********** QUERY TREE **********");
		  	System.out.println();
		  	queryTree.displayTree();
		  	System.out.println();
		  	
		  	System.out.println("********** LOCAL QUERIES **********");
		  	System.out.println();
		  	List<LeafNode> leaves = queryTree.getLeafNodeList();
		  	for (int i = 0; i < leaves.size(); i++) {
		  		String query = new String();
		  		String conditions = new String();
		  		ConditionCheck check = new ConditionCheck();
				TreeNode node = leaves.get(i);
				query += select + space + allAttributes + space + from + space + ((LeafNode)node).getTableName();
				node = node.getParentNode();
				if(!(node.getNodeType().equalsIgnoreCase("join") || node.getNodeType().equalsIgnoreCase("projection"))){
					query += space + where + space;
				}
				while(!(node.getNodeType().equalsIgnoreCase("JOIN") || node.getNodeType().equalsIgnoreCase("PROJECTION"))){
					if(node.getNodeType().equalsIgnoreCase("selection")){
						conditions += ((SelectionNode)node).getContent().substring(((SelectionNode)node).getContent().indexOf(':') + 2);
						query += ((SelectionNode)node).getContent().substring(((SelectionNode)node).getContent().indexOf(':') + 2);
						check.addCondList(((SelectionNode)node).getCondList());
					}
					if(node.getParentNode() != null && !(node.getParentNode().getNodeType().equalsIgnoreCase("join") || node.getParentNode().getNodeType().equalsIgnoreCase("projection"))){
						query += space + and + space;
						conditions += space + and + space;
					}
					node = node.getParentNode();
				}
				query += ";";
				System.out.println("***** QUERY "+ (i+1) +" *****");
				System.out.println(query);
				System.out.println();
				check.displayConditionList();
				System.out.println();
				
				stmt = connection[0].createStatement();
				resultSet = stmt.executeQuery(
						select + space + siteID + comma + attributeName + comma + startValue + comma + endValue + space +
						from + space + horizontalFragmentTable + space +
						where + space + tableName + space + equals + space + "\"" + leaves.get(i).getTableName() + "\"" + ";"
						);
				
				if (!resultSet.isBeforeFirst() ) {    
					try (java.sql.PreparedStatement s1 = connection[1].prepareStatement(query);
						     ResultSet rs = s1.executeQuery()) {
						    ResultSetMetaData meta = rs.getMetaData();
						    
						    
						    StringBuilder columnNames = new StringBuilder();
						    StringBuilder bindVariables = new StringBuilder();
						    
						    List<String> columns = new ArrayList<>();
						    for (int j = 1; j <= meta.getColumnCount(); j++)
						        columns.add(meta.getColumnName(j));

						    for (String column : columns) {
						        if (columnNames.length() > 0) {
						            columnNames.append(", ");
						            bindVariables.append(", ");
						        }

						        columnNames.append(column);
						        bindVariables.append('?');
						    }

						    String query2 = "INSERT INTO " + leaves.get(i).getTableName() + " ("
						               + columnNames
						               + ") VALUES ("
						               + bindVariables
						               + ")";
						    
						    /*
						    PreparedStatement s2 = connection[0].prepareStatement(
						        "INSERT INTO " + leaves.get(i).getTableName() + " ("
						      + columns.stream().collect(Collectors.joining(", "))
						      + ") VALUES ("
						      + columns.stream().map(c -> "?").collect(Collectors.joining(", "))
						      + ")");
							*/
						    
						    java.sql.PreparedStatement s2 = connection[0].prepareStatement(query2);
						    
						    while (rs.next()) {
						        for (int j = 1; j <= meta.getColumnCount(); j++)
						            s2.setObject(j, rs.getObject(j));

						        s2.addBatch();
						    }

						    s2.executeBatch();
						}
 
					 continue;
				} 
				
				boolean flag = false;
				System.out.println("***** FRAGMENTATION CONDITIONS *****");
				while(resultSet.next()){
					String id = resultSet.getString(siteID);
					String attrName = resultSet.getString(attributeName);
					int start = resultSet.getInt(startValue);
					int end = resultSet.getInt(endValue);
					System.out.println("ID : " + id + comma + space + 
										attributeName + ": " + attrName + comma + space + 
										startValue + ": " + start + comma + space +
										endValue + ": " + end);
					if(conditions.contains(attrName)){
						flag = true;
						if(check.isRange(resultSet.getString(attributeName), resultSet.getInt(startValue), resultSet.getInt(endValue))){
								//System.out.println(rs.getString(siteID));
								//stmt1 = connection[resultSet.getInt(siteID)].createStatement();
								//resultSet2 = stmt1.executeQuery(query);
								try (java.sql.PreparedStatement s1 = connection[resultSet.getInt(siteID)].prepareStatement(query);
									     ResultSet rs = s1.executeQuery()) {
									    ResultSetMetaData meta = rs.getMetaData();
									    
									    
									    StringBuilder columnNames = new StringBuilder();
									    StringBuilder bindVariables = new StringBuilder();
									    
									    List<String> columns = new ArrayList<>();
									    for (int j = 1; j <= meta.getColumnCount(); j++)
									        columns.add(meta.getColumnName(j));

									    for (String column : columns) {
									        if (columnNames.length() > 0) {
									            columnNames.append(", ");
									            bindVariables.append(", ");
									        }

									        columnNames.append(column);
									        bindVariables.append('?');
									    }

									    String query2 = "INSERT INTO " + leaves.get(i).getTableName() + " ("
									               + columnNames
									               + ") VALUES ("
									               + bindVariables
									               + ")";
									    
									    /*
									    PreparedStatement s2 = connection[0].prepareStatement(
									        "INSERT INTO " + leaves.get(i).getTableName() + " ("
									      + columns.stream().collect(Collectors.joining(", "))
									      + ") VALUES ("
									      + columns.stream().map(c -> "?").collect(Collectors.joining(", "))
									      + ")");
										*/
									    
									    java.sql.PreparedStatement s2 = connection[0].prepareStatement(query2);
									    
									    while (rs.next()) {
									        for (int j = 1; j <= meta.getColumnCount(); j++)
									            s2.setObject(j, rs.getObject(j));

									        s2.addBatch();
									    }

									    s2.executeBatch();
									}
						}
						else{
								//just ignore case - as no site contains required information
						}
					}
				}
				System.out.println();
				if(!flag){
					for(int site = 1; site <= 3; site++){
						try (java.sql.PreparedStatement s1 = connection[site].prepareStatement(query);
							     ResultSet rs = s1.executeQuery()) {
							    ResultSetMetaData meta = rs.getMetaData();
							    
							    
							    StringBuilder columnNames = new StringBuilder();
							    StringBuilder bindVariables = new StringBuilder();
							    
							    List<String> columns = new ArrayList<>();
							    for (int j = 1; j <= meta.getColumnCount(); j++)
							        columns.add(meta.getColumnName(j));
	
							    for (String column : columns) {
							        if (columnNames.length() > 0) {
							            columnNames.append(", ");
							            bindVariables.append(", ");
							        }
	
							        columnNames.append(column);
							        bindVariables.append('?');
							    }
	
							    String query2 = "INSERT INTO " + leaves.get(i).getTableName() + " ("
							               + columnNames
							               + ") VALUES ("
							               + bindVariables
							               + ")";
							    
							    /*
							    PreparedStatement s2 = connection[0].prepareStatement(
							        "INSERT INTO " + leaves.get(i).getTableName() + " ("
							      + columns.stream().collect(Collectors.joining(", "))
							      + ") VALUES ("
							      + columns.stream().map(c -> "?").collect(Collectors.joining(", "))
							      + ")");
								*/
							    
							    java.sql.PreparedStatement s2 = connection[0].prepareStatement(query2);
							    
							    while (rs.next()) {
							        for (int j = 1; j <= meta.getColumnCount(); j++)
							            s2.setObject(j, rs.getObject(j));
	
							        s2.addBatch();
							    }
	
							    s2.executeBatch();
							}
					}
				}
			}		
		  }
		  resultSet = connection[0].createStatement().executeQuery(sql);
		  System.out.println();
		  System.out.println("********** RESULT **********");
		  System.out.println();
		  int columnCount = resultSet.getMetaData().getColumnCount();
		  for (int j = 1; j <= columnCount; j++) {
			System.out.print(resultSet.getMetaData().getColumnName(j) + "\t");
		}
		  System.out.println();
		  while(resultSet.next()){
			  for (int j = 1; j <= columnCount; j++) {
				System.out.print(resultSet.getString(j) + "\t");
			}
			  System.out.println();
		  }
		  
		  connection[0].createStatement().executeUpdate("DELETE FROM Book");
		  connection[0].createStatement().executeUpdate("DELETE FROM Publisher");
		  connection[0].createStatement().executeUpdate("DELETE FROM Customer");
		  connection[0].createStatement().executeUpdate("DELETE FROM Orders");
		  for (int j = 0; j < 4; j++) {
			  connection[j].close();
		}
	} 
}
