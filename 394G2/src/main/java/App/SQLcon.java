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
		String dbhost = "jdbc:mysql://phpmyadmin.c9dcmkhb1jiz.us-west-2.rds.amazonaws.com:3306/javasite";
		String dbuser = "phpmyadmin";
		String dbpass = "phpmyadmin";
		
		//create database connection
		try {
			c = DriverManager.getConnection(dbhost, dbuser, dbpass);
		}
		catch(SQLException s) {
			String msg = s.getSQLState();
			System.out.print(msg);
			return null;
		}
		return c;
	}

}
