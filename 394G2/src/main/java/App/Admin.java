package App;
import spark.Request;
import spark.Response;

public class Admin extends App.view.Page {
	
	public Admin(String n, boolean f) {
		super(n, false);
	}
	
	@Override
	public String display(Request req, Response res) {
		if (!isLoggedIn()) return "You are not logged in and cannot come in here!";
		else {
			if (req.queryParams().isEmpty()) {
				return "<ul><li><a href='/admin?clicked=addpage'>Add Page</a></li></ul>";
			}
			else if (req.queryParams("clicked").equals("addpage")) {
				return new App.adminWidgets.newPageWidget().display();
			}
			else return "hi";
		}
	}

}
