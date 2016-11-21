package App;
import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.*;

public class CControl implements Route {

	public User u1;
	public StringBuilder content;
	
	
	public String content(Request req, Response res) {
		return null;
	}
	
	
	@Override
	public Object handle(Request req, Response res) throws Exception {
		this.content = new StringBuilder();
		//check if user exists in session. If so, set member variable to u1.
		if (req.session().attribute("user") != null) this.u1 = req.session().attribute("user");
		//check if guest is logged in using the session
		else if (req.session().attribute("guest") != null) {
			this.u1 = new User();
			content.append("<div class='alert alert-info' role='alert'>You are logged into a <strong>guest account</strong>, and have limited access to functionality!</div>");
		}
		//if not, ensure u1 is null
		else this.u1 = null;
		this.content.append(content(req, res));
		//content = content(req, res);
		String template = App.view.Template.getContent();
		
		template = template.replaceAll("<~~!!@@style@@!!~~>", DisplayControl.getHTML("layouts/style.css"));
		template = template.replaceAll("<~~!!@@content@@!!~~>", this.content.toString());
		template = template.replaceAll("<~~!!@@nav@@!!~~>", new App.widgets.Navigation().display());
		template = template.replaceAll("<~~!!@@user@@!!~~>", new App.widgets.UserWidget().display(req, res));
		return template;
		//return this.content;
	}
	
	public boolean isLoggedIn() {
		if (this.u1 != null) return true;
		return false;
	}
}
