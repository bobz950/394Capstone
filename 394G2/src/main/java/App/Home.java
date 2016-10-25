package App;
import static spark.Spark.*;
import java.sql.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class Home extends App.view.Page {
	

	public Home(String name) {
		super(name, false);
	}

	public String display(Request req, Response res) {
		String str = "";
		str += "<center><div style='max-width:750px;'><img src='http://www.acha.org/app_themes/HC2020/images/HC_Partner_Logos/depaul.jpeg' style='max-width: 100%; height: auto;' /></div><br>";
		if (isLoggedIn()) str += "Welcome " + u1.username;
		else str += "Welcome Guest!";
		str += "</center>";
		return str;
	}

}
