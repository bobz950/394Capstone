package App.UserView;

import spark.Request;
import spark.Response;

public class UserProfileEdit extends UserProfile {
	
	public UserProfileEdit(String n) {
		super(n);
		this.requireLogin = true;
	}
	@Override
	public String layout(String type, String email, String name[], String active, String date, String address, String phone, String html) {
		String result = "<form class='form-horizontal' method='post' action ='/updateprofile'><fieldset>";
		result += html;
		result += "</fieldset>";
		result = result.replaceAll("<~~!!@@fname@@!!~~>", makeInput("fname", name[0]));
		result = result.replaceAll("<~~!!@@lname@@!!~~>", makeInput("lname", name[1]));
		
		result = result.replaceAll("Status", "Language");
		result = result.replaceAll("<~~!!@@type@@!!~~>", makeInput("language", "English"));
		
		result = result.replaceAll("<~~!!@@email@@!!~~>", makeInput("email", email));
		
		result = result.replaceAll("Activity", "Username");
		result = result.replaceAll("<~~!!@@active@@!!~~>", makeInput("username", u1.username));
		
		result = result.replaceAll("Date Registered", "Password");
		result = result.replaceAll("<~~!!@@date@@!!~~>", makeInput("password", "Password"));
		
		result = result.replaceAll("<~~!!@@phone@@!!~~>", makeInput("phone", phone));
		result = result.replaceAll("<~~!!@@address@@!!~~>", makeInput("address", address));
		
		result += "<button id='submit' class='btn btn-primary'>Submit</button>";
		return result;
	}
	
	
	public String makeInput(String id, String label) {
		return "<input id='" + id + "' name='" + id + "' type='text' placeholder='" + label + "' class='form-control input-md'>";
	}
}
