package App.view;

import spark.Request;
import spark.Response;
import java.sql.*;

public class Page extends App.CControl {
	public String theName;
	public boolean fromDb = true;
	public String content;
	
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
		if (fromDb && checkDb()) {
			this.content = getDbContent();
			return this.content;
		}
		return display(req, res);
	}
	
	public boolean checkDb() {
		try {
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery("SELECT id FROM pages WHERE pagename='" + theName + "'");
			return true;
		}
		catch (SQLException s) {
			return false;
		}
		
	}
	
	public String getDbContent() {
		try {
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery("SELECT pagecontent FROM pages WHERE pagename='" + theName + "'");
			if (r.next()) return r.getString("pagecontent");
		}
		catch (SQLException s) {
			return s.getSQLState();
		}
		return "Page not found";
		
	}
	
	//override this if creating page not from database
	public String display(Request req, Response res) {
		return "";
	}

}
