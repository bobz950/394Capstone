package App.widgets;

import spark.Request;
import spark.Response;

import App.User;
import App.SQLcon;
import java.sql.*;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StudentProfile implements Widget {
	
	private User usr;
	
	public StudentProfile(User u) {
		this.usr = u;
	}

	public String display(Request req, Response res) {
		String query = "SELECT * FROM Student_Profile WHERE UserID=" + usr.id + ";";
		HashMap<String, String> results = runStatement(query);
		String html = getHTML();
		html = html.replaceAll("<~~!!@@StudentID@@!!~~>", results.get("StudentID"));
		html = html.replaceAll("<~~!!@@CareerID@@!!~~>", results.get("CareerID"));
		html = html.replaceAll("<~~!!@@ProgramID@@!!~~>", results.get("ProgramID"));
		html = html.replaceAll("<~~!!@@MajorID@@!!~~>", results.get("MajorID"));
		html = html.replaceAll("<~~!!@@ConcentrationID@@!!~~>", results.get("ConcentrationID"));
		html = html.replaceAll("<~~!!@@DateOfEnrollment@@!!~~>", results.get("DateOfEnrollment"));
		html = html.replaceAll("<~~!!@@CumulativeGPA@@!!~~>", results.get("CumulativeGPA"));
		return html;
	}

	public String display() {
		return null;
	}
	
	public HashMap<String, String> runStatement(String q) {
		Statement st = null;
		ResultSet r = null;
		Connection con = SQLcon.connect();
		HashMap<String, String> h = new HashMap<String, String>(7);
		try {
			st = con.createStatement();
			try {
				r = st.executeQuery(q);
				try {
					if (r.next()) {
						h.put("StudentID", r.getString("StudentID"));
						h.put("CareerID", r.getString("CareerID"));
						h.put("ProgramID", r.getString("ProgramID"));
						h.put("MajorID", r.getString("MajorID"));
						h.put("ConcentrationID", r.getString("ConcentrationID"));
						h.put("DateOfEnrollment", r.getString("DateOfEnrollment"));
						h.put("CumulativeGPA", r.getString("CumulativeGPA"));
					}
				}
				finally {
					r.close();
				}
			}
			finally {
				st.close();
			}
		} catch (SQLException s) {
			System.out.println("error statement");
		}
		finally {
			try {
				con.close();
			} catch (SQLException s) {
				System.out.println("error closing connection");
			}
		}

		return h;
	}
	
	public String getHTML() {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/studentprofile.html")));
		} catch (IOException s) {
			System.out.println("Could not get html");
			html = " ";
		}
		return html;
	}
	
	

}
