package App;
import java.sql.*;

public class SQLcon {
	
	public static Connection connect() {
		Connection c;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch(ClassNotFoundException e) {
			System.out.print("driver reg");
			return null;
		}
		
		//database connection info
		String dbhost = "jdbc:mysql://localhost/javasite";
		String dbuser = "root";
		String dbpass = "";
		
		//create database connection
		try {
			c = DriverManager.getConnection(dbhost, dbuser, dbpass);
		}
		catch(SQLException s) {
			System.out.print("db didnt connect");
			return null;
		}
		return c;
	}

}
