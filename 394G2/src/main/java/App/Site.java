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
		
		get ("/studentcenter", new App.UserView.StudentCenter("Student Center"));
		post ("/studentcenter", new App.UserView.StudentCenter("Student Center"));
		
		get ("/whatif", new App.UserView.Whatif("What If"));
		
		get ("/classsearch", new App.UserView.ClassSearch("Class Search"));
		
		get ("/enroll", new App.UserView.EnrollSearch("Class Search"));
		
		post ("/enroll", new App.UserView.EnrollHandle("Enrollment"));
		get ("/enroll", new App.UserView.StudentCenter("Enrollment"));
		
		post ("/csearch", new App.UserView.ClassSearchHandler("Class Search"));
		get ("/csearch", new App.UserView.StudentCenter("Class Search"));
		
		get ("/degree", new App.UserView.DegreeReq("Degree Requistrements"));
		
		post ("/degreesearch", new App.UserView.DegreeReqHandler("Degree Requirements"));
		get ("/degreesearch", new App.UserView.DegreeReq("Degree Requirements"));
		
		get ("/editprofile", new App.UserView.UserProfileEdit("Edit User Profile"));
		
		post ("/pathresult", new App.UserView.WhenIfResult("When If Result"));
		
		//get("/path", (req, res) -> { return new App.logic.ClassPathSearch(2, 1 , 2, 3, 3, 3, 3, "Spring").display(); });
		
		get("/studentlookup", new App.UserView.StudentLookup("Student Lookup"));
		post ("/studentresult", new App.UserView.StudentLookupHandler("Student Result"));
		get ("/studentresult", new App.UserView.StudentLookup("Student Lookup"));
		post ("/modify", new App.UserView.StudentLookupHandler("Student Result"));
		get ("/modify", new App.UserView.StudentLookup("Student Modify"));
		
		get ("/myclasses", new App.UserView.CurrentClasses("Your Classes"));
	}
	
	

}
