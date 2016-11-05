package App;
import static spark.Spark.*;

import java.io.IOException;
import java.sql.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class Site {

	

	public static void main(String[] args) {

		port(81);
		staticFiles.location("/views");
			
		
		get("/", new Home("Home"));
		
		get("/info", new Info("Info"));	
		
		//not necessary anymore after creating user widget
		get("/login", new Login());	
		
		//sign in post route. Redirects to previous page after visited.
		post("/signin", new Signin());
		
		
		post("logout", (req, res) -> { return UserControl.logout(req,  res); });
		
		//route for pages retrieved dynamically from database or other areas
		get("/page/:name", (req, res) -> { return new App.view.Page(req.params(":name")).handle(req, res); });
		
		get("/admin", new Admin("Admin Center", false));

		
		get("/profile", new App.UserView.UserProfile("User Profile"));	
		
		get ("/dashboard", new App.UserView.Dashboard("Dashboard"));
		
		get ("/whatif", new App.UserView.Whatif("What If"));
		
		get ("/classsearch", new App.UserView.ClassSearch("Class Search"));
		
		post ("/csearch", new App.UserView.ClassSearchHandler("Class Search"));
		
		get ("/degree", new App.UserView.DegreeReq("Degree Requirements"));
		
		
	}
	
	

}
