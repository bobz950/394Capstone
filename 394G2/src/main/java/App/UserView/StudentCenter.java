package App.UserView;

import java.util.HashMap;

import App.SQLcon;
import App.UserControl;
import spark.Request;
import spark.Response;

public class StudentCenter extends App.view.Page {
	
	public StudentCenter(String n) {
		super(n, false);
		this.requireLogin = true;
	}
	
	public String display(Request req, Response res) {
		String enrolledClasses = "";
		String takenClasses = "";
		String drop;
		String msg = "";
		if ((drop = req.queryParams("drop")) != null) msg = dropclass(drop);
		String enrolledQuery = "SELECT TeacherUserID, ID, ClassID, Term, CreditHours FROM Course_Taken WHERE StudentUserID=? AND Status=?";
		String[] params = {((Integer)(UserControl.getUser(req).id)).toString(), "0"};
		String[] columns = {"TeacherUserID", "ID", "ClassID", "Term", "CreditHours"};
		HashMap<String, String> enrolledClassMap = SQLcon.multiResultQuery(enrolledQuery, params, columns, 2, 5);
		
		String takenQuery = "SELECT Course_Taken.ID, Course_Taken.ClassID, User.FName, User.LName, Course_Taken.Term, Course_Taken.CreditHours, Course_Taken.Grade FROM Course_Taken INNER JOIN User ON User.ID = Course_Taken.TeacherUserID WHERE Course_Taken.StudentUserID=? AND Course_Taken.Status=?";
		String[] takenparams = {((Integer)(UserControl.getUser(req).id)).toString(), "1"};
		String[] takencolumns = {"ID", "ClassID", "FName", "LName", "Term", "CreditHours", "Grade"};
		HashMap<String, String> takenClassMap = SQLcon.multiResultQuery(takenQuery, takenparams, takencolumns, 2, 7);
		
		String html = msg + App.DisplayControl.getHTML("layouts/studentcenter.html");
		String entry = App.DisplayControl.getHTML("layouts/enrolledentry.html");
		int enrolled = Integer.valueOf(enrolledClassMap.get("Count"));
		if (enrolled < 1) enrolledClasses = "<tr><td>Not currently enrolled in any classes</td></tr>";
		for (int i = 0; i<enrolled; i++) {
			String e = entry;
			e = e.replaceAll("<~~!!@@classname@@!!~~>", enrolledClassMap.get(i + "ClassID"));
			e = e.replaceAll("<~~!!@@term@@!!~~>", enrolledClassMap.get(i + "Term"));
			String[] teachernamearr = UserControl.getName(Integer.valueOf(enrolledClassMap.get(i + "TeacherUserID")));
			String teachername = teachernamearr[0] + " " + teachernamearr[1];
			e = e.replaceAll("<~~!!@@teacher@@!!~~>", teachername);
			e = e.replaceAll("<~~!!@@hours@@!!~~>", enrolledClassMap.get(i + "CreditHours"));
			e = e.replaceAll("<~~!!@@status@@!!~~>", "In Progress");
			e = e.replaceAll("<~~!!@@options@@!!~~>", "<form method='post' action='studentcenter'><input type='hidden' name='drop' value='" + enrolledClassMap.get(i + "ID") + "'><button>Drop</button></form>");
			enrolledClasses += e;
		}
		html = html.replaceAll("<~~!!@@enrolled@@!!~~>", enrolledClasses);
		String takenentry = App.DisplayControl.getHTML("layouts/takenentry.html");
		int taken = Integer.valueOf(takenClassMap.get("Count"));
		if (taken < 1) takenClasses = "<tr><td>You have not taken any classes yet</td></tr>";
		for (int i = 0; i < taken; i++) {
			String e = takenentry;
			String teachname = takenClassMap.get(i + "FName") + " " + takenClassMap.get(i + "LName");
			e = e.replaceAll("<~~!!@@classname@@!!~~>", takenClassMap.get(i + "ClassID"));
			e = e.replaceAll("<~~!!@@term@@!!~~>", takenClassMap.get(i + "Term"));
			e = e.replaceAll("<~~!!@@hours@@!!~~>", takenClassMap.get(i + "CreditHours"));
			e = e.replaceAll("<~~!!@@instructor@@!!~~>", teachname);
			e = e.replaceAll("<~~!!@@grade@@!!~~>", takenClassMap.get(i + "Grade"));
			e = e.replaceAll("<~~!!@@status@@!!~~>", "Completed");
			takenClasses += e;
		}
		html = html.replaceAll("<~~!!@@completed@@!!~~>", takenClasses);
		return html;
	}
	
	public String dropclass(String id) {
		int cid = Integer.valueOf(id);
		String q = "DELETE FROM Course_Taken WHERE ID=" + cid;
		int r = SQLcon.insertQuery(q);
		if (r < 1) return "Could not drop class";
		return "Successfully dropped class";
	}
}
