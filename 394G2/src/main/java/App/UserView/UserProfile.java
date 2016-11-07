package App.UserView;
import java.sql.SQLException;

import App.User;
import App.UserControl;
import spark.Request;
import spark.Response;

public class UserProfile extends App.view.Page {

	public UserProfile(String name) {
		super(name, false);
		this.requireLogin = true;
	}
	
	@Override
	public String display(Request req, Response res) {
		User u = UserControl.getUser(req);
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
			
			String address = App.UserControl.getAddress(u1.username);
			String phone = App.UserControl.getPhone(u1.username);
			
			String html = App.DisplayControl.getHTML("layouts/userprofile.html");
			
			return layout(type, email, name, active, dateRegistered, address, phone, html);
		}
		else return "not logged in";
	}
	
	public String layout(String type, String email, String name[], String active, String date, String address, String phone, String html) {
		
		html = html.replaceAll("<~~!!@@fname@@!!~~>", name[0]);
		html = html.replaceAll("<~~!!@@lname@@!!~~>", name[1]);
		html = html.replaceAll("<~~!!@@type@@!!~~>", type);
		html = html.replaceAll("<~~!!@@email@@!!~~>", email);
		html = html.replaceAll("<~~!!@@active@@!!~~>", active);
		html = html.replaceAll("<~~!!@@date@@!!~~>", date);
		html = html.replaceAll("<~~!!@@phone@@!!~~>", phone);
		html = html.replaceAll("<~~!!@@address@@!!~~>", address);
		return html;
	}
	
}
