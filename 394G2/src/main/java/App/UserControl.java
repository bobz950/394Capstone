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
	
	
	//(unfinished) Get all users from database. Return array of User objects.
	public static User[] getUsers(Connection con) {
		return null;
	}
	
	//(unfinished) Create new user and add to database
	public static boolean makeUser(Connection con) {
		return false;
	}

}
