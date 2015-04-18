import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mysql.jdbc.PreparedStatement;

import GlobalDefinition.JoinExpression;
import Parser.WhereItemsFinder;
import QueryTree.JoinNode;
import QueryTree.LeafNode;
import QueryTree.QueryTree;
import QueryTree.SelectionNode;
import QueryTree.TreeNode;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.util.SelectUtils;


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
		connection[0] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/","DDBProject","root","");
		connection[1] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/","DDBProject1","root","");
		connection[2] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/", "DDBProject2", "root", "");
		connection[3] = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/", "DDBProject3", "root", ""); 
		
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
		  String sql = "SELECT Book.name,Publisher.name,Customer.name,Orders.quantity FROM Publisher,Book,Customer,Orders WHERE Publisher.publisherId = Book.Publisher_publisherId AND Publisher.publisherId = 10 AND Customer.customerId = Orders.Customer_customerId AND Orders.Book_bookId = Book.bookId AND Book.bookId = 12 AND Customer.rank >= 2";
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
		  	queryTree.displayTree();
		  	
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
				System.out.println(query);
				
				check.displayConditionList();
				
				stmt = connection[0].createStatement();
				resultSet = stmt.executeQuery(
						select + space + siteID + comma + attributeName + comma + startValue + comma + endValue + space +
						from + space + horizontalFragmentTable + space +
						where + space + tableName + space + equals + space + "\"" + leaves.get(i).getTableName() + "\"" + ";"
						);
				
				ArrayList<Integer> sites = new ArrayList<Integer>();
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
									        for (int j = 1; j < meta.getColumnCount(); j++)
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
					else{
						
					}
				}
				
				
			}
		  	
		  	
		  	
		  	/*------where clause----*/
		  	
			WhereItemsFinder finder3 = new WhereItemsFinder(selectStatement);
			
			/*------join clause----*/
			ArrayList<JoinNode> joins = new ArrayList<JoinNode>();
			ArrayList<JoinExpression> joinList = finder3.getJoinList();
			for(int i=0;i<joinList.size();++i){
				JoinNode node2 = new JoinNode();
				//System.out.println(joinList.get(i).leftTableName);
				node2.setRightTableName(joinList.get(i).rightTableName);
				//System.out.println(joinList.get(i).rightTableName);
				
				node2.addAttribute(joinList.get(i).leftColumn, joinList.get(i).rightColumn);
				;
				
			}
			
		  }
		
		
	} 
}
