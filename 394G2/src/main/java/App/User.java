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
	public int id;
	public boolean isValid = true;
	
	public User(String name, String pass) {
		this.username = name;
		this.password = pass;
		//get values from db and check if user exists
		try {
			this.isValid = this.getValues(this.username);
		} catch (SQLException s) {
			System.out.print(s.getSQLState());
		} 
		if (this.isValid) this.checkPass(this.password); //if user exists check password
	}
	
	
	//returns false if user not in table
	public boolean getValues(String user) throws SQLException {
		boolean completed = false;
		Connection con = SQLcon.connect(); //get sql connection
		try {
			Statement st = con.createStatement();
			try {
				String query = "SELECT * FROM User WHERE Login='" + user + "'";
				ResultSet r = st.executeQuery(query);
				try {
					if (!r.next()) completed = false; //user name not in database
					else {
						this.uPassword = r.getString("Password"); //get real pass from db
						this.id = r.getInt("ID"); //store user id;
						completed = true;
					}
				}
				finally {
					r.close();
				}

			}
			finally {
				st.close();
			}
		}
		finally {
			con.close();
		}

		return completed;
	
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
