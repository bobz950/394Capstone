package App;
import static spark.Spark.*;
import java.sql.*;

import App.widgets.Widget;
import spark.Request;
import spark.Response;
import spark.Route;

public class Login extends CControl {
	

	public String content(Request req, Response res) {
		// TODO Auto-generated method stub
		return new App.widgets.UserWidget().display(req, res);
	}

}
