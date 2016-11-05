package App;
import App.SQLcon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DisplayControl {
	public static String careerDisplay(String s) {
		Integer result = Integer.valueOf(s);
		if (result == 0) return "Undergraduate";
		else if (result == 1) return "Graduate";
		else return "NA";
	}
	
	public static String programDisplay(String s) {
		Integer progId = Integer.valueOf(s);
		String q = "SELECT Name FROM Program WHERE ID=" + progId + ";";
		return SQLcon.singleResultQuery(q, "Name");
	}
	
	public static String majorDisplay(String s) {
		Integer majorID = Integer.valueOf(s);
		String q = "SELECT Name FROM Major WHERE ID=" + majorID + ";";
		return SQLcon.singleResultQuery(q, "Name");
	}
	
	public static String getHTML(String loc) {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get(loc)));
		} catch (IOException s) {
			System.out.println("Could not get html");
			html = " ";
		}
		return html;
	}
	

}
