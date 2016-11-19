package App.UserView;

import spark.Request;
import spark.Response;

public class StudentLookup extends App.view.Page {
	
	public StudentLookup(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		if (App.UserControl.getUserType(App.UserControl.getUser(req).username) < 1) return "You are not authorized to view this area";
		String result = App.DisplayControl.getHTML("layouts/studentlookup.html");
		return result;
	}
}
