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
			String result = "<div class='well widgethead'>Welcome back to the site, " + u.getName() + "</div>";
			result += "<div class='list-group' id='usernav'>";
			result += listitem("Your Status: " + type);
			result += listitemurl("/profile", "Your Profile");				
			result += listitemurl("/dashboard", "Your Dashboard");	
			result += listitemurl("/classsearch", "Course Search");	
			result += listitemurl("/degree", "Degree Requirements");
			result += listitemurl("/whatif", "When-If Report");	
			if (type == "Student") {
				result += listitemurl("/studentcenter", "Student Center");	
			}
			if (type == "Faculty") {
				result += listitemurl("/myclasses", "Your Open Classes");	
			}
			if (type == "Administrator") result += listitemurl("/studentlookup", "Student Search");	
					
			result += "</div>";
			String logout = App.DisplayControl.getHTML("layouts/logout.html");
			logout = logout.replaceAll("<~~!!@@lasturl@@!!~~>", req.url());
			
			result += logout;
			return result;
		}
		else {
			String html = App.DisplayControl.getHTML("layouts/loginform.html");
			html = html.replaceAll("<~~!!@@lasturl@@!!~~>", req.url());
			return html;
		}

	}
	
	@Override
	public String display() {
		return null;
	}
	
	public String listitemurl(String url, String name) {
		return "<a href='" + url + "'class='list-group-item'>" + name + "</a>";
	}
	
	public String listitem(String msg) {
		return "<li class='list-group-item active'>" + msg + "</li>";
	}


}
