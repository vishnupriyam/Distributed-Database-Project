import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statement stmt;
		ResultSet rs;
		Connection conn1 = DBConnection.connectDB("jdbc:mysql://192.168.40.140:3306/","QUIZ","root","");
		try {
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
		}
		
		
	} 
}
