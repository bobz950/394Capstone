package App;

import spark.Request;
import spark.Response;

public class Info extends App.view.Page {

	public Info(String name) {
		super(name, false);	
	}
	
	public String display(Request req, Response res) {
		String str = "";
		str += "<center><h3>CapstoneG2 version 1.0</h3><br>";
		str += "<p>CapstoneG2 is an application that allows students of DePaul University to plan their academics with ease. ";
		str += "It was created by Jessica Craft, Bobby Alianello, Segundo Auqui, Patrick Drucker, Mahek Gheewala, and Stefan Hiebl</p></center>";
		return str;
	}
}
