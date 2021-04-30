import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	
	public static void main(String[]args) {
		Database db = new Database();
		Scanner scnr = new Scanner(System.in);
		char selection;
		String userInput;
		String username = "";
		String passwordInput;
		HashMap<String,String> emailAndPasswordMap = db.getAllUsersAndPasswords();

		System.out.println("Welcome to the Anonymoose Password Manager");
		System.out.println("Enter 1 or L to login, enter 2 or C to create an account: ");

		selection = scnr.next().charAt(0);
		
		while(selection != '1' && selection != 'l' && selection != 'L' && selection != '2' && selection != 'c' && selection != 'C'){
			System.out.println("Invalid option. Enter 1 or L to login, enter 2 or C to create a new account: ");
			userInput = scnr.nextLine();
		}

		switch(selection){
			case '1':
			case 'l':
			case 'L':
				System.out.println("Enter your username: ");
				username = scnr.nextLine();
				System.out.println("Enter your password: "); //need a way to not show password while user is typing
				passwordInput = scnr.nextLine();
				//hash password
				if(db.validLogin(username,passwordInput)){
					System.out.println("Login successful");
				}
				else{
					while(!db.validLogin(username,passwordInput)){
						System.out.println("The username and password do not match any accounts.");
						System.out.println("Enter your username: ");
						username = scnr.nextLine();
						System.out.println("Enter your password: ");
						passwordInput = scnr.nextLine();
						db.validLogin(username,passwordInput);
					}
				}
				break;
			case '2':
			case 'c':
			case 'C':
				System.out.println("Enter your email address: ");
				username = scnr.nextLine();

				if(!db.usernameFree(username)){
					while(!db.usernameFree(username)){
				       		System.out.println("Email is already in use. Enter your email address: ");
			       			username = scnr.nextLine();
					}
				}

				System.out.println("Enter your password: ");
				userInput = scnr.nextLine();
				//hash password
				System.out.println("Confirm your password: ");
				passwordInput = scnr.nextLine();
				//hash password
				if(userInput == passwordInput){
					db.createNewUser(username, passwordInput);
					System.out.println("Account created.");
				}
				else{
					System.out.println("Passwords do not match");
				}
				break;
		}

		System.out.println("Select an option.");
		System.out.println("Enter 1 to view all usernames and passwords.");
		System.out.println("Enter 2 to add a username and password.");
		System.out.println("Enter 3 to delete a username and password.");
		System.out.println("Enter 4 to update a username and password.");
		System.out.println("Enter 5 to delete your account.");
		
		selection = scnr.next().charAt(0);

		switch(selection){
			case '1': 
				HashMap<String,String> userEntries = db.getAllPasswordsForUser(username);

				//figure out how to iterate through hashmap and print values
				for (Map.Entry<String, String> set : userEntries.entrySet()) {
					System.out.println("Site Name: " + set.getKey());
					System.out.println("Site Password: " + set.getValue());
					System.out.println("");
				}
				
				break;
			case '2':
				System.out.println("Would you like to generate a new random password? Enter y for yes, enter n for no: ");
				selection = scnr.next().charAt(0);
				if(selection != 'y' || selection != 'n'){}
				//this still needs finished
				break;

			case '3':

				break;
			case '4':

				break;
			case '5':
				System.out.println("All stored usernames and passwords will be deleted and you will be immediately logged out. Are you sure you want to delete your account? Enter y for yes, enter any other key for no: ");
				selection = scnr.next().charAt(0);
				if(selection == 'y'){
					db.deleteUser(username); //does this logout after it finishes?
					System.out.println("Your account has been deleted");
				}
				break;
		}
	}
}
