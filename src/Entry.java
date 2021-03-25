import java.util.Scanner;

public class Entry {
	private String username;
	private String hash;
	
	public Entry() {
		this.setUsername("None");
		this.setHash("None");
	}
	
	public Entry(String username) {
		this.setUsername(username);
	}
	
	public Entry(String username, String hash) {
		this.setUsername(username);
		this.setHash(hash);
	}
	
	/*public String createHash() {
		Scanner scnr = new Scanner(System.in);
		
	}
	*/
	
	
}
