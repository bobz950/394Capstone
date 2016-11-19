package App.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import App.SQLcon;
import spark.Request;
import spark.Response;

public class CurrentClasses extends App.view.Page {
	
	public CurrentClasses(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		String uname = App.UserControl.getUser(req).username;
		int utype = Integer.valueOf(SQLcon.singleResultQuery("SELECT Type FROM User WHERE Login='" + uname + "'", "Type"));
		if (utype != 1) return "You must be faculty to access this area";
		String q = "SELECT ID, ClassID, StudentUserID, Term FROM Course_Taken WHERE Status=0 AND TeacherUserID=" + App.UserControl.getUserID(uname);
		HashMap<String, ArrayList<Integer>> classes = new HashMap<String, ArrayList<Integer>>();
		HashMap<String, String> r = SQLcon.multiResultQuery(q, new String[]{}, new String[]{"ID", "ClassID", "StudentUserID", "Term"}, 0, 4);
		int c = Integer.valueOf(r.get("Count"));
		//populate hashmap of classes for teacher
		for (int i = 0; i<c; i++) {
			String theclass = r.get(i + "Term") + " " + r.get(i + "ClassID");
			if (!classes.containsKey(theclass)) classes.put(theclass, new ArrayList<Integer>());
			classes.get(theclass).add(Integer.valueOf(r.get(i + "ID")));
		}
		String html = App.DisplayControl.getHTML("layouts/openclasses.html");
		String content = show(classes);
		html = html.replaceAll("<~~!!@@entry@@!!~~>", content);
		return html;
	}
	
	public String show(HashMap<String, ArrayList<Integer>> classmap) {
		
		String entry = "";
		Iterator it = classmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = ((Map.Entry<String, ArrayList<Integer>>)it.next());
			entry += "<tr><td style='background-color:#337ab7' class='list-group-item active'>" + e.getKey() + "</td></tr>";
			for (Integer id : (ArrayList<Integer>)e.getValue()) {
				String query = "SELECT User.ID, User.FName, User.LName FROM Course_Taken INNER JOIN User ON Course_Taken.StudentUserID = User.ID WHERE Course_Taken.ID=?";
				HashMap<String, String> r = SQLcon.multiResultQuery(query, new String[]{id.toString()}, new String[]{"ID", "FName", "LName",}, 1, 3);
				int c = Integer.valueOf(r.get("Count"));
				if (c > 0) {
					entry += App.DisplayControl.getHTML("layouts/openclassentry.html");
					entry = entry.replaceAll("<~~!!@@name@@!!~~>", r.get("0FName") + " " + r.get("0LName"));
					entry = entry.replaceAll("<~~!!@@id@@!!~~>", id.toString());
				}
			}
		}
		return entry;
		
	}
}
