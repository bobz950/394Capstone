package App.UserView;
import java.sql.SQLException;

import App.UserControl;
import spark.Request;
import spark.Response;

public class UserProfile extends App.view.Page {

	public UserProfile(String name) {
		super(name, false);
	}
	
	@Override
	public String display(Request req, Response res) {
		App.User u = UserControl.getUser(req);
		if (u != null) {
			u1 = u; //refresh u1
			String type;
			int thetype;
			try {
				thetype = App.UserControl.getType(u1.username);
			} catch (SQLException s) {
				thetype = -1;
			}
			if (thetype == 0) type = "Student";
			else if (thetype == 1) type = "Faculty";
			else type = "Admin";
			
			String email;
			try {
				email = App.UserControl.getEmail(u1.username);
			} catch (SQLException s) {
				email = "not found";
			}
			
			String name[];
			try {
				name = App.UserControl.getName(u1.username);
			} catch (SQLException s) {
				name = new String[2];
			}
			
			String active;
			boolean isactive;
			try {
				isactive = App.UserControl.isActive(u1.username);
			} catch (SQLException s) {
				isactive = false;
			}
			if (isactive) active = "Active";
			else active = "Inactive)";
			
			String dateRegistered;
			try {
				dateRegistered = App.UserControl.getRegistered(u1.username);
			} catch (SQLException s) {
				dateRegistered = "not available";
			}
			return layout(type, email, name, active, dateRegistered);
		}
		else return "not logged in";
	}
	
	public String layout(String type, String email, String name[], String active, String date) {
		
		return "<table class='tg'>"
				+ "<tr>"
				+ "<th>First Name: </th>"
				+ "<th>" + name[0]  +"</th>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Last Name:</td>"
				+ "<td>" + name[1]  +"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Type:</td>"
				+ "<td>" + type  +"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Email:</td>"
				+ "<td>" + email  +"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Activity:</td>"
				+ "<td>" + active  +"</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td>Date Registered:</td>"
				+ "<td>" + date  +"</td>"
				+ "</tr>"
				+ "</table>";
	}
	
}
