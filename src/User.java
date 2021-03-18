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

	public boolean registering(String login, String password) {
		//check the presence of a login in the map
		if (users.get(login) != null) {
			return false;
		} else {
			//add a new combination of login and password
			users.put(login, password);
			return true;
		}
	}

	public void run() {
		Scanner in = new Scanner(System.in);
		String login;
		String password;
		while (true) {
			System.out.println("1. Register an account.");
			System.out.println("2. Login.");
			System.out.println("0. Exit");
			login = in.nextLine();
			switch (login) {
				case "1":
					System.out.println("Enter login.");
					login = in.nextLine();
					System.out.println("Enter password");
					password = in.nextLine();
					if (registering(login, password)) {
						System.out.println("You have successfully registered.");
					} else {
						System.out.println("The login is already in use by another user.");
					}
					break;
				case "2":
					System.out.println("Enter login.");
					login = in.nextLine();
					System.out.println("Enter password");
					password = in.nextLine();
					if (login(login, password)) {
						System.out.println("You have successfully logged in.");
					} else {
						System.out.println("Invalid login and password combination.");
					}
					break;
				case "0":
					return;
			}
		}
	}

	public static void main(String[] args) {
		new User();
	}
}
