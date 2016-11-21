package App.UserView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.Request;
import spark.Response;

public class DegreeReq extends App.view.Page {
	
	public DegreeReq(String name) {
		super(name, false);
		this.requireLogin = true;
		this.restrictGuest = false;
	}
	
	@Override
	public String display(Request req, Response res) {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/reqs.html")));
		} 
		catch (IOException s) {
			html = System.getProperty("user.dir");
		}
		
		return html;
	}
}
