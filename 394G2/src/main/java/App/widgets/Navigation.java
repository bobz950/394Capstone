package App.widgets;

import spark.Request;
import spark.Response;
import java.sql.*;

public class Navigation implements Widget {

	public String display(Request req, Response res) {

		return null;
	}

	public String display() {
		String str = "<li><a href='/'>Home</a></li>";
		Connection con = App.SQLcon.connect();
		try {
			Statement st = con.createStatement();
			try {
				ResultSet r = st.executeQuery("SELECT * FROM nav");
				try {
					while (r.next()) str += "<li><a href='" +  r.getString("url") + "'>" + r.getString("name") + "</a></li>";
				}
				finally {
					r.close();
				}
			}
			finally {
				st.close();
			}
			
		}
		catch (SQLException s) {
			
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return str;
		
	}

}
