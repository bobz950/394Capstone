package App.widgets;

import App.User;
import App.UserControl;
import spark.Request;
import spark.Response;

public class UserWidget implements Widget {

	@Override
	public String display(Request req, Response res) {
		User u = UserControl.getUser(req);
		if (u != null) return "<h3 class='widgethead'>Welcome back to the site, " + u.getName() + "</h3>"
				+ "<br><form method='post' action='logout'><input type='hidden' name='lasturl' value='" + req.url() + "'><br>"
				+ "<input type='submit' value='Logout'></form>";
		else return "<h3 class='widgethead'>Log in here: </h3><br><form method='post' action='signin'>"
				+ "<input id='name' name='name' type='text'><br>"
				+ "<input id='password' name='password' type='text' text='password'>"
				+ "<input type='hidden' name='lasturl' value='" + req.url() + "'>"
				+ "<input type='submit'></form>";
	}
	
	@Override
	public String display() {
		return null;
	}
	

}
