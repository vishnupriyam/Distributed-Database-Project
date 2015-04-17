import java.io.StringReader;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import GlobalDefinition.JoinExpression;
import Parser.WhereItemsFinder;
import QueryTree.JoinNode;
import QueryTree.QueryTree;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;


public class DBConnection {

	private final static String driver = "com.mysql.jdbc.Driver";
	
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
	public static void main(String[] args) throws JSQLParserException {
		// TODO Auto-generated method stub
		Statement stmt;
		ResultSet rs;
		Connection conn1 = DBConnection.connectDB("jdbc:mysql://127.0.0.1:3306/","DDBProject","root","");
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
		  String sql = "SELECT Book.name,Publisher.name,Customer.name,Orders.quantity FROM Publisher,Book,Customer,Orders WHERE Publisher.publisherId = Book.Publisher_publisherId AND Publisher.publisherId = 10 AND Customer.customerId = Orders.Customer_customerId AND Orders.Book_bookId = Book.bookId";
		  net.sf.jsqlparser.statement.Statement statement = pm.parse(new StringReader(sql));
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
