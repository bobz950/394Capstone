package App;
import static spark.Spark.*;


import spark.Request;
import spark.Response;
import spark.Route;

public class Signin implements Route {

	public Object handle(Request req, Response res) throws Exception {
		String name = req.queryParams("name");
		String pass = req.queryParams("password");
		String lasturl = req.queryParams("lasturl");
		
		
		User u2 = new User(name, pass);
		if (u2.isValid) {
			req.session().attribute("user", u2);
			res.redirect(lasturl);
			return "login success!!!";
		}
		else {
			return "nope you suck";
		}

	}

}
