package App.UserView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.Request;
import spark.Response;

public class EnrollSearch extends App.view.Page {
	
	public EnrollSearch(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	@Override
	public String display(Request req, Response res) {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/classsearch.html")));
			html = html.replaceAll("<~~!!@@action@@!!~~>", "/csearch");
			html = html.replaceAll("<~~!!@@enroll@@!!~~>", "yes");
		} 
		catch (IOException s) {
			html = System.getProperty("user.dir");
		}
		
		return html;
	
	}
}
