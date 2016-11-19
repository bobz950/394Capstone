package App.UserView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import App.SQLcon;
import App.UserControl;
import spark.Request;
import spark.Response;

public class Whatif extends App.view.Page {

	public Whatif(String name) {
		super(name, false);
		this.requireLogin = true;
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
		String asuser = "<div class='form-group'><label class='col-md-4 control-label'>Run report as student:</label>";
		asuser += "<div class='col-md-4'><select name='asuser' class='form-control'><~~!!@@useroption@@!!~~></select></div></div>";
		int type = UserControl.getUserType(UserControl.getUser(req).username);
		if (type > 0) {
			String options = "";
			html = html.replaceAll("<~~!!@@asuser@@!!~~>", asuser);
			String q = "SELECT ID, FName, LName FROM User WHERE Type=0";
			HashMap<String, String> r = SQLcon.multiResultQuery(q, new String[]{}, new String[]{"ID", "FName", "LName"}, 0, 3);
			int c = Integer.valueOf(r.get("Count"));
			options += "<option value=0>No Student(Fresh Report)</option>";
			for (int i = 0; i<c; i++) {
				options += "<option value='" + r.get(i + "ID") + "'>" + r.get(i + "FName") + " " + r.get(i + "LName") + "</option>";
			}
			html = html.replaceAll("<~~!!@@useroption@@!!~~>", options);
		}
		else html = html.replaceAll("<~~!!@@asuser@@!!~~>", "");
		return html;
	}

}
