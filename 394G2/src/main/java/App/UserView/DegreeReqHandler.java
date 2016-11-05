package App.UserView;

import java.util.HashMap;

import App.SQLcon;
import spark.Request;
import spark.Response;

public class DegreeReqHandler extends App.view.Page {
	
	public DegreeReqHandler(String s) {
		super(s, false);
	}
	
	public String display(Request req, Response res) {
		int majorID = Integer.valueOf(req.queryParams("degree"));
		String query = "SELECT Name, Long_Name, Description, CreditHours, TypicallyOffered FROM Class WHERE MajorID=? AND Course_TypeID=2 OR Course_TypeID=3";
		String[] params = {req.queryParams("degree")};
		String[] columns = {"Name", "Long_Name", "Description", "CreditHours", "TypicallyOffered"};
		HashMap<String, String> resultMap = SQLcon.multiResultQuery(query, params, columns, 1, 5);
		String result = "";
		int num = Integer.valueOf(resultMap.get("Count"));
		for (int i = 0; i<num; i++) {
			String html = App.DisplayControl.getHTML("layouts/reqsresult.html");
			html = html.replaceAll("<~~!!@@number@@!!~~>", ((Integer)i).toString());
			html = html.replaceAll("<~~!!@@name@@!!~~>", resultMap.get(i + "Name"));
			html = html.replaceAll("<~~!!@@classname@@!!~~>", resultMap.get(i + "Long_Name"));
			html = html.replaceAll("<~~!!@@classdes@@!!~~>", resultMap.get(i + "Description"));
			result += html;
		}
		return result;
	}
}
