package App;
import static spark.Spark.*;
import java.sql.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class Test2 extends CControl {
	


	public String content(Request req, Response res) {
		
		String str = "names: ";
		try {
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery("SELECT * FROM users");
			while (r.next()) str += r.getString("name");
		}
		catch (SQLException s) {
			System.out.print("statement not made");
		}
		return str;
	}

}
