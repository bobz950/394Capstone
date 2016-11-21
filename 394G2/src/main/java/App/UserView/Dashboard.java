package App.UserView;

import java.sql.SQLException;

import spark.Request;
import spark.Response;
import App.DisplayControl;
import App.widgets.StudentProfile;

public class Dashboard extends App.view.Page {
	
	public Dashboard(String name) {
		super(name, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		App.User u = App.UserControl.getUser(req);
		if (u == null) return "Restricted Access. You must log in!";
		String result = "";
		result += profile(req, res, u);
		int type;
		try {
			type = App.UserControl.getType(u.username);
		} catch (SQLException s) {
			type = -1;
		}
		String navs = "";
		//if (type == 2) navs += "<li><a href='/admin'>Admin Center</a></li>";
		//if (type == 1) navs += "<li><a href='#'>Faculty Center</a></li>";
		if (type == 0) navs += "<li><a href='/studentcenter'>Student Center</a></li>";
		navs += "<li><a href='/editprofile'>Edit Profile Info</a></li>";
		String navhtml = DisplayControl.getHTML("layouts/dashnav.html");
		navhtml = navhtml.replaceAll("<~~!!@@links@@!!~~>", navs);
		navhtml += "<legend>Your Dashboard</legend>";
		result = navhtml + result;
		if (type == 0) result += new StudentProfile(u).display(req, res); 
		return result;
	}
	
	public String profile(Request req, Response res, App.User u) {
		if (u != null) {
			return new UserProfile("profie").display(req, res);
		}
		else return "no user";
	}
}
