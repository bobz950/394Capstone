package App.UserView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import spark.Request;
import spark.Response;

public class Whatif extends App.view.Page {

	public Whatif(String name) {
		super(name, false);
	}
	
	@Override
	public String display(Request req, Response res) {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/whatif.html")));
		} 
		catch (IOException s) {
			html = System.getProperty("user.dir");
		}
		
		return html;
	}

}
