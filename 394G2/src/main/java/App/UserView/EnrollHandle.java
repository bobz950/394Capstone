package App.UserView;

import java.util.HashMap;

import App.SQLcon;
import App.User;
import App.UserControl;
import spark.Request;
import spark.Response;

public class EnrollHandle extends App.view.Page {
	public EnrollHandle(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	public String display (Request req, Response res) {
		String content = "";
		if (req.queryParams("classname") != null) {
			String classname = req.queryParams("classname");
			User u = UserControl.getUser(req);
			int userid = u.id;
			String terms = getAvailable(classname);
			if (!checkPreqs(userid, classname)) return "Prerequisites have not been satisfied for this course";
			String checkTaken = "SELECT ID FROM Course_Taken WHERE ClassID='" + classname + "'" + " AND StudentUserID=" + userid;
			String n;
			if  ((n = SQLcon.singleResultQuery(checkTaken, "ID")) != "Not Available") return "You have already taken that course";
			return select(classname, terms, userid);
		}
		String theclassname = req.queryParams("theclass");
		int userid = Integer.valueOf(req.queryParams("theuserid"));
		String teacher = req.queryParams("teachers");
		int teacherid = Integer.valueOf(teacher);
		String term = req.queryParams("terms");
		
		String query = "INSERT INTO Course_Taken (ClassID, StudentUserID, TeacherUserID, Term, Status) VALUES ('" + theclassname + "', " + userid + ", " + teacherid + ", '" + term + "', 0)";
		if (SQLcon.insertQuery(query) > 0) content += "Successfully enrolled in " + theclassname;
		else content += "Enrollment failed";
		return content;
	}
	
	public boolean checkPreqs(int uid, String classname) {
		boolean completed = true;
		String userid = ((Integer)uid).toString();
		String preqQuery = "SELECT PreqClassID FROM Class_Preqs WHERE ClassID=?";
		String[] params = {classname};
		String[] columns = {"PreqClassID"};
		HashMap<String, String> result = SQLcon.multiResultQuery(preqQuery, params,  columns,  1,  1);
		
		String takenQuery = "SELECT ClassID FROM Course_Taken WHERE StudentUserID=? AND Status=?";
		String[] takenparams = {userid, "1"};
		String[] takencolumns = {"ClassID"};
		HashMap<String, String> takenresult = SQLcon.multiResultQuery(takenQuery, takenparams, takencolumns, 2, 1);
		int resultnum = Integer.valueOf(result.get("Count"));
		for (int i = 0; i < resultnum; i++) if (!takenresult.containsValue(result.get(i + "PreqClassID"))) return false;
		return completed;
	}
	
	public String getAvailable(String name) {
		String q = "SELECT TypicallyOffered FROM Class WHERE Name='" + name + "'";
		String r = SQLcon.singleResultQuery(q, "TypicallyOffered");
		return r;
	}
	
	public String select(String classname, String terms, int userid) {
		String q = "SELECT ID, FName, LName FROM User WHERE Type=1";
		
		HashMap<String, String> teachers = SQLcon.multiResultQuery(q, new String[] {}, new String[] {"ID", "FName", "LName"}, 0, 3);
		int count = Integer.valueOf(teachers.get("Count"));
		String teach = "";
		for (int i = 0; i< count; i++) {
			teach += "<option value = '" + teachers.get(i + "ID") + "'>" + teachers.get(i + "FName") + " " +  teachers.get(i + "LName") + "</option>";
		}
		String termsavail = "";
		if (terms.contains("Fall")) termsavail += "<option value='Fall'>Fall</option>";
		if (terms.contains("Winter")) termsavail += "<option value='Fall'>Winter</option>";
		if (terms.contains("Spring")) termsavail += "<option value='Spring'>Spring</option>";
		if (terms.contains("Summer")) termsavail += "<option value='Fall'>Summer</option>";
		String html = App.DisplayControl.getHTML("layouts/enrollselect.html");
		html = html.replaceAll("<~~!!@@teachers@@!!~~>", teach);
		html = html.replaceAll("<~~!!@@title@@!!~~>", classname);
		html = html.replaceAll("<~~!!@@terms@@!!~~>", termsavail);
		String hidden = "<input type='hidden' name='theclass' value='" + classname + "'>";
		hidden += "<input type='hidden' name='theuserid' value='" + userid + "'>";
		html = html.replaceAll("<~~!!@@hide@@!!~~>", hidden);
		return html;
	}
}
