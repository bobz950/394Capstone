package App.UserView;

import java.util.HashMap;

import App.SQLcon;
import spark.Request;
import spark.Response;

public class DegreeChange extends App.view.Page {
	public DegreeChange(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		String html = "";
		if (req.queryParams("concentration") != null) {
			if (process(Integer.valueOf(req.queryParams("concentration")), req)) html = html.concat("<div class='alert alert-success' role='alert'><strong>Success!</strong> Successfully changed degree/major.</div>");
			else html =  html.concat("<div class='alert alert-danger' role='alert'>Could not change degree/major.</div>");
		}
		html = html.concat((App.DisplayControl.getHTML("layouts/degreechange.html")));
		StringBuilder options = new StringBuilder();
		String q = "SELECT Name, ID FROM Area WHERE MajorID = 1";
		String[] params = {};
		String[] columns = {"Name", "ID"};
		HashMap<String, String> r = SQLcon.multiResultQuery(q, params, columns, 0, 2);
		int count = Integer.valueOf(r.get("Count"));
		for (int i = 0; i < count; i++) {
			if (!r.get(i + "Name").equals("NA")) options.append("<option value='" + r.get(i + "ID") + "'>" + r.get(i + "Name") + "</option>");
		}
		html = html.replaceAll("<~~!!@@options@@!!~~>", options.toString());
		return html;
	}
	
	public boolean process(int areaid, Request req) {
		int uid = App.UserControl.getUserID(App.UserControl.getUser(req).username);
		String q = "UPDATE Student_Profile SET ConcentrationID=" + areaid + " WHERE UserID=" + uid;
		if (App.SQLcon.insertQuery(q) > 0) return true;
		else return false;
	}
}
