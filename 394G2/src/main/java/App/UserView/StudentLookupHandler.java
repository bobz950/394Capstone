package App.UserView;

import java.util.HashMap;

import App.SQLcon;
import App.UserControl;
import spark.Request;
import spark.Response;

public class StudentLookupHandler extends App.view.Page {

	public StudentLookupHandler(String name) {
		super(name, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		if (App.UserControl.getUserType(App.UserControl.getUser(req).username) < 1) return "You are not authorized to view this area";
		String modify = req.queryParams("modify");
		if (modify != null) return modify(Integer.valueOf(modify));
		if (req.queryParams("grade") != null) {
			String grade = req.queryParams("grade");
			int id = Integer.valueOf(req.queryParams("id"));
			return submit(grade, id, req);
		}
		int studentid = Integer.valueOf(req.queryParams("num"));
		String q = "SELECT Student_Profile.UserID, User.FName, User.LName FROM Student_Profile INNER JOIN User ON Student_Profile.UserID = User.ID WHERE Student_Profile.StudentID=?";
		HashMap<String, String> r = SQLcon.multiResultQuery(q, new String[]{((Integer)studentid).toString()}, new String[]{"UserID", "FName", "LName"}, 1, 3);
		int rcount = Integer.valueOf(r.get("Count"));
		int userid = 0;
		String userName = "";
		if (rcount < 1) return "No results found";
		else {
			userid = Integer.valueOf(r.get("0UserID"));
			userName = r.get("0FName") + " " + r.get("0LName");
		}
		String query = "SELECT ID, ClassID FROM Course_Taken WHERE Status=0 AND StudentUserID=?";
		String[] params = {((Integer)userid).toString()};
		String[] columns = {"ID", "ClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(query, params, columns, 1, 2);
		int count = Integer.valueOf(result.get("Count"));
		String add = "";
		for (int i = 0; i<count; i++) {
			int entryid = Integer.valueOf(result.get(i + "ID"));
			add += "<tr><td>" + result.get(i + "ClassID") + "<div style='float:right'><form method='post' action='/modify'><input type='hidden' name='modify' value='" + entryid + "'><button>Submit Final Grade</button></div></form>" + "</td></tr>";
		}
		String html = App.DisplayControl.getHTML("layouts/studentlookupresult.html");
		html = html.replaceAll("<~~!!@@entry@@!!~~>", add);
		html = html.replaceAll("<~~!!@@name@@!!~~>", userName);
		return html;
	}
	
	public String modify(int id) {
		String q = "SELECT Course_Taken.ClassID, Course_Taken.StudentUserID, User.FName, User.LName FROM Course_Taken INNER JOIN User ON Course_Taken.StudentUserID = User.ID WHERE Course_Taken.ID=?";
		String[] params = {((Integer)id).toString()};
		String[] columns = {"ClassID", "StudentUserID", "FName", "LName"};
		HashMap<String, String> result = SQLcon.multiResultQuery(q, params, columns, 1, 4);
		if (Integer.valueOf(result.get("Count")) < 1) return "error";
		String studentname = result.get("0FName") + " " + result.get("0LName");
		String title = "Modifying class " + result.get("0ClassID") + " For student " + studentname + ".";
		String html = App.DisplayControl.getHTML("layouts/studentclassmodify.html");
		html = html.replaceAll("<~~!!@@title@@!!~~>", title);
		html = html.replaceAll("<~~!!@@hide@@!!~~>", "<input type='hidden' name='id' value='" + id + "'>");
		return html;
	}
	
	public String submit(String grade, int id, Request req) {
		String q = "UPDATE Course_Taken SET Status=1, Grade='" + grade + "' WHERE ID=" + id;
		int r = SQLcon.insertQuery(q);
		if (r > 0) return "successfully modified class!";
		else return "error modifying class";
	}

}
