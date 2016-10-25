package App.widgets;

import java.sql.SQLException;

import App.User;
import App.UserControl;
import spark.Request;
import spark.Response;

public class UserWidget implements Widget {

	@Override
	public String display(Request req, Response res) {
		String type;
		User u = UserControl.getUser(req);
		if (u != null) {
			int thetype;
			try {
				thetype = App.UserControl.getType(u.getName());
			} catch (SQLException s) {
				thetype = -1;
			}
			if (thetype == 0) type = "Student";
			else if (thetype == 1) type = "Faculty";
			else type = "Administrator";
			String result = "<h3 class='widgethead'>Welcome back to the site, " + u.getName() + "</h3>"
							+ "<br><ul>"
							+ "<li>Your Status: " + type + "</li>"
							+ "<li><a href='/profile'>Your Profile</a></li>"
							+ "<li><a href='/dashboard'>Your Dashboard</a></li>"
							+ "<li><a href='/classsearch'>Course Search</a></li>"
							+ "<li><a href='/degree'>Degree Requirements</a></li>";


					if (type == "Student") result += "<li><a href='/whatif'>What-If Report</a></li>";
					
					result += "</ul>"
							+ "<br><form method='post' action='/logout'><input type='hidden' name='lasturl' value='" + req.url() + "'><br>"
							+ "<input type='submit' value='Logout'></form>";
					
					return result;
		}
		else return "<h3 class='widgethead'>Log in here: </h3><br><form method='post' action='/signin'>"
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
