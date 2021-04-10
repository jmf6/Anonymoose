import java.util.Scanner;

public class Main {
	
	public static void main(String[]args) {
		//Database db;
		Scanner scnr = new Scanner(System.in);
		String userInput;
		String username;
		String passwordInput;

		System.out.println("Welcome to the Anonymoose Password Manager");
		System.out.println("Enter 1 or L to login, enter 2 or C to create an account: ");

		userInput = scnr.next().charAt(0);
		
		while(userInput != "1" || userInput != "l" || userInput != "L" || userInput != "2" || userInput != "c" || userInput != "C"){
			System.out.println("Invalid option. Enter 1 or L to login, enter 2 or C to create a new account: ");
			userInput = scnr.nextLine();
		}

		switch(userInput){
			case "1":
			case "l":
			case "L":
				System.out.println("Enter your username: ");
				username = scnr.nextLine();
				System.out.println("Enter your password: "); //need a way to not show password while user is typing
				passwordInput = scnr.nextLine();

				//check for valid login

				break;
			case "2":
			case "c":
			case "C":
				System.out.println("Enter your email address: ");
				username = scnr.nextLine();
				//check if email is in use
				System.out.println("Enter your password: ");
				userInput = scnr.nextLine();
				System.out.println("Confirm your password: ");
				passwordInput = scnr.nextLine();
				
				if(userInput == passwordInput){
					//db.createNewUser(username, passwordInput);
				}
				else{
					System.out.println("Passwords do not match");
				}
				break; 
		}
	}
}
