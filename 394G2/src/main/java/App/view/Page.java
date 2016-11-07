package App.view;

import spark.Request;
import spark.Response;
import java.sql.*;

public class Page extends App.CControl {
	public String theName;
	public boolean fromDb = true;
	public String content;
	public boolean requireLogin = false;
	
	//constructor to use if page is from database
	public Page(String name) {
		this.theName = name;
	}
	
	//constructor to use if page is not from database
	public Page(String name, boolean usedb) {
		this.theName = name;
		this.fromDb = usedb;
	}
	
	public String content(Request req, Response res) {
		boolean dbcheck;
		try {
			dbcheck = checkDb();
		} catch (SQLException s) {
			dbcheck = false;
		}
		if (fromDb && dbcheck) {
			//this.content = getDbContent();
			return this.content;
		}
		if (this.requireLogin && req.session().attribute("user") == null) return "You must be logged in to view this content";
		return display(req, res);
	}
	
	public boolean checkDb() throws SQLException {
		boolean result = false;
		Connection con = App.SQLcon.connect();
		try {
			Statement st = con.createStatement();
			try {
				ResultSet r = st.executeQuery("SELECT id, pagecontent FROM pages WHERE pagename='" + theName + "'");
				try {
					if (r.next()) {
						result = true;
						this.content = r.getString("pagecontent");
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
		return result;

		
	}
	
//	public String getDbContent() {
//		try {
//			Connection con = App.SQLcon.connect();
//			Statement st = con.createStatement();
//			ResultSet r = st.executeQuery("SELECT pagecontent FROM pages WHERE pagename='" + theName + "'");
//			con.close();
//			st.close();
//			if (r.next()) return r.getString("pagecontent");
//		}
//		catch (SQLException s) {
//			return s.getSQLState();
//		}
//		return "Page not found";
//		
//	}
	
	//override this if creating page not from database
	public String display(Request req, Response res) {
		return "";
	}

}
