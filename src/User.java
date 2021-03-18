import java.util.HashMap;
import java.util.Scanner;

public class User {
	private HashMap<String, String> users;

	public User() {
		users = new HashMap<>();
		run();
	}

	public boolean login(String login, String password) {
		//check that there is such a login in the map
		if (users.get(login) != null) {
			//compare the password for this login with the password passed
			//in the parameters and return the verification result
			return users.get(login).equals(password);
		} else {
			return false;
		}
	}
}
