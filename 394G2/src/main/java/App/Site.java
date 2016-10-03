package App;
import static spark.Spark.*;
import java.sql.*;

import spark.Request;
import spark.Response;
import spark.Route;

public class Site {

	

	public static void main(String[] args) {

		port(81);
		staticFiles.location("/views");
			
		
		get("/", new Home());
		
		//not necessary anymore after creating user widget
		get("/login", new Login());	
		
		//sign in post route. Redirects to previous page after visited.
		post("/signin", new Signin());
		
		
		post("logout", (req, res) -> { return UserControl.logout(req,  res); });
		
		//route for pages retrieved dynamically from database or other areas
		get("/page/:name", (req, res) -> { return new App.view.Page(req.params(":name")).handle(req, res); });
		
		get("/admin", new Admin("Admin Center", false));
		
		get("/t", new Test2());	
		
		
		
	}
	
	

}
