package App;
import static spark.Spark.*;
import java.sql.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class Home extends CControl {
	

	public String content(Request req, Response res) {
		String str;
		if (isLoggedIn()) str = "Welcome " + u1.username;
		else str = "Welcome Guest!";
		return str;
	}

}
