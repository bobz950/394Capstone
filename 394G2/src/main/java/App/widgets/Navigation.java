package App.widgets;

import spark.Request;
import spark.Response;
import java.sql.*;

public class Navigation implements Widget {

	public String display(Request req, Response res) {

		return null;
	}

	public String display() {
		String str = "";
		try {
			Connection con = App.SQLcon.connect();
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery("SELECT * FROM nav");
			while (r.next()) str += "<li><a href='" +  r.getString("url") + "'>" + r.getString("name") + "</a></li>";
		}
		catch (SQLException s) {
			
		}
		return str;
		
	}

}
