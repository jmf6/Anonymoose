import java.util.ArrayList;

public class DatabaseUserEntry {

	private String email;
	private String password;
	private ArrayList<DatabasePasswordEntry> storedPasswords;
	
	public DatabaseUserEntry(String emailArg, String passwordArg) {
		email = emailArg;
		password = passwordArg;
		storedPasswords = new ArrayList<DatabasePasswordEntry>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<DatabasePasswordEntry> getStoredPasswords() {
		return storedPasswords;
	}

	public void setStoredPasswords(ArrayList<DatabasePasswordEntry> storedPasswords) {
		this.storedPasswords = storedPasswords;
	}
	
	
}
