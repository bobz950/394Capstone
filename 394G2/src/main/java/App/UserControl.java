package App;
import spark.Request;
import spark.Response;

import java.sql.*;

public class UserControl {
	
	//returns User object of current logged in user.
	public static User getUser(Request req) {
		return (User) req.session().attribute("user");
	}
	
	public static String logout(Request req, Response res) {
		req.session().removeAttribute("user");
		String lasturl = req.queryParams("lasturl");
		res.redirect(lasturl);
		return "logged out";
	}
	
	
	//type will return 0 for student, 1 for faculty, 2 for admin, -1 on error
	public static int getType(String n) throws SQLException {
		int result = 0;
		Connection con = SQLcon.connect();
		try {
			Statement st = SQLcon.getStatement(con);
			try {
				String query = "SELECT Type FROM User WHERE Login='" + n + "'";
				ResultSet r = st.executeQuery(query);
				try {
					if (!r.next()) result = -1;
					result = r.getInt("Type");
				}
				finally {
					r.close();
				}
			}
			finally {
				st.close();
			}
		}
		finally {
			con.close();
		}
		return result;

	}
	

	
	public static String getEmail(String n) throws SQLException {
		String email = null;
		Connection con = SQLcon.connect();
		try {
			Statement st = SQLcon.getStatement(con);
			try {
				String query = "SELECT Email FROM User WHERE Login='" + n + "'";
				ResultSet r = st.executeQuery(query);
				try {
					if (!r.next()) email = (String) null;
					email = r.getString("Email");
				}
				finally {
					r.close();
				}
			}
			finally {
				st.close();
			}
		}
		finally {
			con.close();
		}
		return email;
	}
	

	//returns array with firstname in index 0 and lastame in index 1
	public static String[] getName(String n) throws SQLException {
		String[] names = new String[2];
		Connection con = SQLcon.connect();
		try {
			Statement st = SQLcon.getStatement(con);
			try {
				String query = "SELECT FName, LName FROM User WHERE Login='" + n + "'";
				ResultSet r = st.executeQuery(query);
				try {
					if (!r.next()) return (String[]) null;
					names[0] = r.getString("FName");
					names[1] = r.getString("LName");
				}
				finally {
					r.close();
				}
			}
			finally{
				st.close();
			}
		}
		finally {
			con.close();
		}
		return names;
		
	}
	
	
	//get if user is active
	public static boolean isActive(String n) throws SQLException {
		boolean isactive = true;
		Connection con = SQLcon.connect();
		try {
			Statement st = SQLcon.getStatement(con);
			try {
				String query = "SELECT Active FROM User WHERE Login='" + n + "'";
				ResultSet r = st.executeQuery(query);
				try {
					if (!r.next()) isactive = false;
					else {
						int active = r.getInt("Active");
						if (active <= 0) isactive = false;
						else isactive = true;
					}
					
				}
				finally {
					r.close();
				}
			}
			finally {
				st.close();
			}
		}
		finally {
			con.close();
		}
		
		return isactive;
		
	}
	
	
	//get data user registered
		public static String getRegistered(String n) throws SQLException {
			String date = "now";
			Connection con = SQLcon.connect();
			try {
				Statement st = SQLcon.getStatement(con);
				try {
					String query = "SELECT Registered FROM User WHERE Login='" + n + "'";
					ResultSet r = st.executeQuery(query);
					try {
						if (!r.next()) date = "User never registered";
						else date = r.getString("Registered");
					}
					finally {
						r.close();
					}
				}
				finally {
					st.close();
				}
			}
			finally {
				con.close();
			}
			return date;
			
		}
	
	
	//(unfinished) Get all users from database. Return array of User objects.
	public static User[] getUsers(Connection con) {
		return null;
	}
	
	//(unfinished) Create new user and add to database
	public static boolean makeUser(Connection con) {
		return false;
	}

}
