package App.UserView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import App.SQLcon;
import spark.Request;
import spark.Response;

public class ClassSearchHandler extends App.view.Page {
	
	public ClassSearchHandler(String s) {
		super(s, false);
	}
	
	@Override
	public String display(Request req, Response res) {
		String content = "";
		String term = req.queryParams("term");
		String subject = req.queryParams("subject");
		String num = " " + req.queryParams("num");
		boolean addEnroll = false;
		if (req.queryParams("enroll").contains("yes")) addEnroll = true;
		//String query = "SELECT MajorID, ProgramID, Course_TypeID, AreaID, CreditHours, Long_Name, Description FROM Class WHERE Name =?";
		String query = "SELECT ProgramID, CreditHours, Long_Name, Description, Name FROM Class WHERE Name LIKE ?";
		if (num.length() < 2) num = "";
		String name = "%" + subject + num + "%";
		String[] params = {name};
		String[] columns = {"ProgramID","CreditHours", "Long_Name", "Description", "Name"};
		HashMap<String, String> resultMap = SQLcon.multiResultQuery(query, params, columns, 1, 5);
		if (resultMap.size() < 4) return "No classes found";
		int numResults = Integer.valueOf(resultMap.get("Count"));
		int i = 0;
		while (i < numResults) {
			String result = getHTML();
			result = result.replaceAll("<~~!!@@classnum@@!!~~>",  resultMap.get(i + "Name"));
			result = result.replaceAll("<~~!!@@name@@!!~~>", resultMap.get(i + "Long_Name"));
			result = result.replaceAll("<~~!!@@description@@!!~~>", resultMap.get(i + "Description"));
			result = result.replaceAll("<~~!!@@credits@@!!~~>", resultMap.get(i + "CreditHours"));
			if (addEnroll) result = result.replaceAll("<~~!!@@enroll@@!!~~>", "<tr><td><form action='/enroll' method='post'><fieldset><input type='hidden' name='classname' value='" + resultMap.get(i + "Name")
					+ "'><button id='submit' class='btn btn-primary'>Enroll</button></fieldset></form></td></tr>");
			else result = result.replaceAll("<~~!!@@enroll@@!!~~>", "");
			content += result;
			i++;
		}

		return content;
	}
	
	public String getHTML() {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/classsearchresult.html")));
		} catch (IOException s) {
			System.out.println("Could not get html");
			html = " ";
		}
		return html;
	}
}
