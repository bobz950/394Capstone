package App;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import org.apache.commons.codec.binary.Hex;
public class User {
	public String username;
	private String password;
	private String uPassword;
	public Connection con;
	public boolean isValid = true;
	
	public User(String name, String pass) {
		this.con = SQLcon.connect(); //get sql connection
		this.username = name;
		this.password = pass;
		this.isValid = this.getValues(this.username); //get values from db and check if user exists
		if (this.isValid) this.checkPass(this.password); //if user exists check password
	}
	
	
	//returns false if user not in table
	public boolean getValues(String user) {
		try {
			Statement st = con.createStatement();
			String query = "SELECT * FROM users WHERE name='" + user + "'";
			ResultSet r = st.executeQuery(query);
			if (!r.next()) return false;
			else this.uPassword = r.getString("password"); // get real pass from db
			
		}
		catch (SQLException s) {
			System.out.print("getValues Failed");
			System.out.print(s.getSQLState());
			return false;
		}
		return true;
	
	}
	
	
	//checks if password is a match
	public void checkPass(String inputtedPass) {

		try {
			if (!hashPass(inputtedPass).equals(this.uPassword)) {
				System.out.print(inputtedPass);
				System.out.print("wrong pass ");
				System.out.print(this.uPassword);
				this.isValid = false;
			}
		}
		catch (UnsupportedEncodingException s) {
			System.out.print("hashing failed");
		}

		
	}
	
	public String getName() {
		return this.username;
	}
	
	//do this once you figure out how to hash passwords properly
	private String hashPass(String p) throws UnsupportedEncodingException {
		String hashed;
		byte[] by;
		try {
			MessageDigest m = MessageDigest.getInstance("sha-256");
			m.update(p.getBytes("UTF-8"));
			by = m.digest();
		}
		catch (NoSuchAlgorithmException s) {
			return s.getMessage();
		}
		hashed = Hex.encodeHexString(by);
		System.out.print(hashed);
		return hashed;
	}
	
}
