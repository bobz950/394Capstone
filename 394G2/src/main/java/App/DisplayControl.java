package App;
import App.SQLcon;
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
	

}
