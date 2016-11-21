package App;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
		//String dbhost = "jdbc:mysql://phpmyadmin.c9dcmkhb1jiz.us-west-2.rds.amazonaws.com:3306/dev";
		//String dbhost = "jdbc:mysql://localhost/dev?autoReconnect=true&useSSL=false";
		//String dbuser = "root";
		//String dbpass = "";
		String dbhost = "jdbc:mysql://phpmyadmin.c9dcmkhb1jiz.us-west-2.rds.amazonaws.com:3306/dev";
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
	
	public static Statement getStatement(Connection con) {
		Statement st;
		try {
			st = con.createStatement();
		} catch (SQLException s) {
			System.out.print("database connection failed");
			st = (Statement) null;
		}
		return st;
	}
	
	public static int insertQuery(String q) {
		int rows = 0;
		Connection con = SQLcon.connect();
		try {
			Statement st = con.createStatement();
			try {
				rows += st.executeUpdate(q);
			}
			catch (SQLException s) {
				System.out.println(s.getSQLState());
				System.out.println(s.getMessage());
			}
			finally {
				try {
					st.close();
				} catch (SQLException e) {
					System.out.print("did not close statement");
				}
			}
		}
		catch (SQLException s) {
			System.out.print("database connection failed");
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
	}
	
	//param s is the query, param name is the column name to retrieve
	public static String singleResultQuery(String s, String name) {
		String result = "Not Available";
		Connection con = SQLcon.connect();
		try {
			Statement st = con.createStatement();
			try {
				ResultSet r = st.executeQuery(s);
				try {
					if (!r.next()) return result;
					result = r.getString(name);
				}
				catch (SQLException sq) {
					System.out.println("could not get result");
				}
				finally {
					r.close();
				}
			}
			catch (SQLException sq) {
				System.out.println("could not execute query11");
			}
			finally {
				try {
					st.close();
				} catch (SQLException sq) {
					System.out.println("Could not close statement");
				}
			}
		}
		catch (SQLException sq) {
			System.out.println("did not connect to db");
		}
		finally {
			try {
				con.close();
			} catch (SQLException sq) {
				System.out.println("Could not close connection");
			}
		}
		
		return result;
	}
	
	//multi result query using string params in prepared statement
	//Param s for query, v for statement params, c for column names, p - # of query params. r = # of fields to get
	public static HashMap<String, String> multiResultQuery(String s, String[] v, String[] c, int p, int f) {
		HashMap<String, String> result = new HashMap<String, String>();
		Connection con = SQLcon.connect();
		try {
			PreparedStatement st = con.prepareStatement(s);
			for (int i=0; i<p; i++) st.setString(i+1, v[i]);
			try {
				ResultSet r = st.executeQuery();
				try {
					int counter = 0;
					while (r.next()) {
						for (int i=0; i<f; i++) {
							String value = new String(r.getString(c[i]));
							StringBuilder sb = new StringBuilder();
							sb.append(counter);
							sb.append(c[i]);
							result.put(sb.toString(), value);
							sb = null;
						}
						counter++;
					}
					result.put("Count", ((Integer)(counter)).toString());
				}
				catch (SQLException sq) {
					System.out.println("could not get result");
				}
				finally {
					r.close();
				}
			}
			catch (SQLException sq) {
				System.out.println("could not execute query: " + s);
			}
			finally {
				try {
					st.close();
				} catch (SQLException sq) {
					System.out.println("Could not close statement");
				}
			}
		}
		catch (SQLException sq) {
			System.out.println("did not connect to db");
		}
		finally {
			try {
				con.close();
			} catch (SQLException sq) {
				System.out.println("Could not close connection");
			}
		}
		
		return result;
	}

}
