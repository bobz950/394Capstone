package App.UserView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import spark.Request;
import spark.Response;

public class WhenIfResult extends App.view.Page {

	public WhenIfResult(String name) {
		super(name, false);
	}
	
	public String display(Request req, Response res) {
		int uid;
		if (req.queryParams("asuser") != null) uid = Integer.valueOf(req.queryParams("asuser"));
		else uid = App.UserControl.getUser(req).id;
		int major = Integer.valueOf(req.queryParams("program"));
		int conc = Integer.valueOf(req.queryParams("concentration"));
		int fall = Integer.valueOf(req.queryParams("fall"));
		int winter = Integer.valueOf(req.queryParams("winter"));
		int spring = Integer.valueOf(req.queryParams("spring"));
		int summer = Integer.valueOf(req.queryParams("summer"));
		String start = req.queryParams("start");
		
		int year = Integer.valueOf(req.queryParams("year"));
		App.logic.ClassPathSearch thePath = new App.logic.ClassPathSearch(uid, major, conc, fall, winter, spring, summer, start);
		if (thePath.flag == 1) return "Sorry, there is no path to graduation";
		HashMap<String, String> orderMap = new HashMap<String, String>();
		String[] html = {App.DisplayControl.getHTML("layouts/pathresult.html")};
		int c = 1;
		for (Map<String, Integer> e : thePath.order) {
			Iterator itt = e.entrySet().iterator();
			Map.Entry d = ((Map.Entry<String, Integer>)itt.next());
			orderMap.put((String)d.getKey(), "T" + c);
			this.show((String)d.getKey(), html, thePath, orderMap, year);
			c++;
		}
		return html[0];
	}
	
	public void show(String term, String[] html, App.logic.ClassPathSearch path, HashMap<String, String> o, int year) {
		String classes = "";
		ArrayList<String> t = path.resultTerms.get(term);
		int c = 1;
		for (String cls : t) {
			if (cls == "---") {
				html[0] = html[0].replaceFirst("<~~!!@@" + o.get(term) + "@@!!~~>", classes);
				if (classes.length() > 0) html[0] = html[0].replaceFirst("<~~!!@@" + o.get(term) + "TITLE@@!!~~>", "<tr><td style='background-color:#337ab7' class='list-group-item active'>" + term + " " + (year + c++) +  "</td></tr>");
				classes = "";
			}
			else {
				String qu = "SELECT Long_Name, Description FROM Class WHERE Name='" + cls + "'";
				HashMap<String, String> r = App.SQLcon.multiResultQuery(qu, new String[]{}, new String[]{"Long_Name", "Description"}, 0, 2);
				int count = Integer.valueOf(r.get("Count"));
				if (count > 0) classes += "<tr><td><a href='#' data-toggle='popover' title='" + r.get("0Long_Name") + "' data-content='" + r.get("0Description") + "'>" + cls + "</a></td></tr>";
				else classes += "<tr><td>" + cls + "</td></tr>";
			}
		}
		html[0] = html[0].replaceAll("<~~!!@@" + o.get(term) + "@@!!~~>", "");
		html[0] = html[0].replaceAll("<~~!!@@" + o.get(term) + "TITLE@@!!~~>", "");
	}
	
	

}
