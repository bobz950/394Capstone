package App;
import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;
import java.sql.*;

public class CControl implements Route {
	public Connection con;
	public User u1;
	public String content = "";
	
	
	public String content(Request req, Response res) {
		return null;
	}
	
	
	@Override
	public Object handle(Request req, Response res) throws Exception {
		//check if user exists in session. If so, set member variable to u1.
		if (req.session().attribute("user") != null) {
			this.u1 = req.session().attribute("user");
		}
		//if not, ensure u1 is null
		else this.u1 = null;
		con = SQLcon.connect();
		content = content(req, res);
		String template = App.view.Template.getContent();
		
		template = template.replaceAll("<~~!!@@content@@!!~~>", this.content);
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
