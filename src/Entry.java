import java.util.Scanner;

public class Entry {
	private String username;
	private String hash;
	private char[] password;
	
	public Entry() {
		this.setUsername("None");
		this.setHash("None");
	}
	
	public Entry(String username) {
		PasswordGenerator p = new PasswordGenerator();
		this.setUsername(username);
		createPassword(p);
	}
	
	public Entry(String username, String hash) {
		this.setUsername(username);
		this.setHash(hash);
	}
	
	//still working on getting this set up
	/*public String createHash() {
		Scanner scnr = new Scanner(System.in);
		
	}
	*/
	
	private void setUsername(String username) {
		this.username = username;
	}
	
	private void setHash(String hash) {
		this.hash = hash;
	}
	
	//using the password as a hash until I get the hashing figured out
	private void createPassword(PasswordGenerator p) {
		setHash(String.valueOf(p.passwordGen()));
	}
}
