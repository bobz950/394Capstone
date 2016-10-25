package App.UserView;

import java.sql.SQLException;

import spark.Request;
import spark.Response;

public class Dashboard extends App.view.Page {
	
	public Dashboard(String name) {
		super(name, false);
		
	}
	
	public String display(Request req, Response res) {
		App.User u = App.UserControl.getUser(req);
		if (u == null) return "Restricted Access. You must log in!";
		String result = "<h3>Your Dashboard</h3>";
		result += profile(req, res, u);
		result += "<br><ul class='list-inline'>";
		int type;
		try {
			type = App.UserControl.getType(u.username);
		} catch (SQLException s) {
			type = -1;
		}
		if (type == 2) result += "<li><a href='/admin'>Admin Center</a></li>";
		if (type == 1) result += "<li><a href='#'>Faculty Center</a></li>";
		if (type == 0) result += "<li><a href='#'>Student Center</a></li>";
		result += "<li><a href='#'>Edit Profile Info</a></li>";
		
		result += "</ul>";
		return result;
	}
	
	public String profile(Request req, Response res, App.User u) {
		if (u != null) {
			return new UserProfile("profie").display(req, res);
		}
		else return "no user";
	}
}
