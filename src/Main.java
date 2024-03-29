import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
	
	public static void main(String[]args) {
		Database db = new Database();
		Scanner scnr = new Scanner(System.in);
		PasswordGenerator p = new PasswordGenerator();
		char selection = ' ';
		String site = "";
		String userInput = "";
		String username = "";
		String passwordInput = "";
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
				System.out.print("Enter your username: ");
				scnr.next();
				username = scnr.nextLine();
				System.out.print("Enter your password: ");
				passwordInput = scnr.nextLine();
			
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
				System.out.print("Enter your email address: ");
				username = scnr.next();

				if(!db.usernameFree(username)){
					while(!db.usernameFree(username)){
				       		System.out.println("Email is already in use. Enter your email address: ");
			       			username = scnr.nextLine();
					}
				}

				System.out.print("Enter your password: ");
				userInput = scnr.next();
				System.out.print("Confirm your password: ");
				passwordInput = scnr.next();
				
				if(userInput.equals(passwordInput)){
					db.createNewUser(username, passwordInput);
					System.out.println("Account created.");
				}
				else{
					System.out.println("Passwords do not match");
				}
				break;
		}
		do{
			System.out.println("Select an option.");
			System.out.println("Enter 1 to view all usernames and passwords.");
			System.out.println("Enter 2 to add a username and password.");
			System.out.println("Enter 3 to delete a username and password.");
			System.out.println("Enter 4 to update a username and password.");
			System.out.println("Enter 5 to delete your account.");
			System.out.println("Enter any other key to exit the program.");
		
			selection = scnr.next().charAt(0);

			switch(selection){
				case '1': 
					HashMap<String,String> userEntries = db.getAllPasswordsForUser(username);

					for (Map.Entry<String, String> set : userEntries.entrySet()) {
						System.out.println("Site Name: " + set.getKey());
						System.out.println("Site Password: " + set.getValue());
						System.out.println("");
					}
					break;
				
				case '2':
                                        System.out.print("Enter the site name: ");
                                        site = scnr.next();
                                        System.out.println("Would you like to generate a new random password? Enter y for yes, enter any other key for no: ");
                                        selection = scnr.next().charAt(0);
                                        if(selection == 'y'){
                                                passwordInput = String.valueOf(p.passwordGen());
                                                if(db.passwordUsed(username, passwordInput)){
                                                        passwordInput = String.valueOf(p.passwordGen());
                                                }
                                                System.out.print("Your password for " + site + " is: " + passwordInput);
                                                try{
                                                        Thread.sleep(20000);
                                                }
                                                catch(Exception e){}

                                                for(int i = 0; i  < 20000; i++) {
                                                        System.out.println();
                                                }

                                                System.out.println();
                                                db.addNewPassword(username, site, passwordInput);
                                        }
                                        else{
                                                System.out.println("Enter the password: ");
                                                passwordInput = scnr.nextLine();
                                                if(db.passwordUsed(username, passwordInput)){
                                                        System.out.println("The password is already being used on another site.");
                                                        while(db.passwordUsed(username, passwordInput)){
                                                                System.out.print("Would you like the program to generate a new password? Enter y for yes, enter any other key for no: ");
                                                                selection = scnr.next().charAt(0);
                                                                if(selection == 'y'){
                                                                        passwordInput = String.valueOf(p.passwordGen());
                                                                }
                                                                else{
                                                                        System.out.print("Enter the new password: ");
                                                                        passwordInput = scnr.next();
                                                                }
                                                        }
                                                }
                                                db.addNewPassword(username, site, passwordInput);
                                        }
                                        break;
				
				case '3':
					System.out.print("Enter the site that you would like to delete the password for: ");
					site = scnr.next();
					db.deletePassword(username, site);
					System.out.println("Password deleted.");
					break;
				
				case '4':
					System.out.print("Enter the site that you would like to update the password for: ");
					site = scnr.next();
					System.out.println("Enter g if you would like to randomly generate a password, enter any other key to set your own password: ");
					selection = scnr.next().charAt(0);
					if(selection == 'g'){
						passwordInput = String.valueOf(p.passwordGen());
						System.out.print("Your new password for " + site + " is: " + passwordInput);
						try{
                                                        Thread.sleep(20000);
                                                }
                                                catch(Exception e){}
												for(int i = 0; i  < 20000; i++) {
													System.out.println();
												}
                                                System.out.println();
						db.updateUserPassword(username, site, passwordInput);
						System.out.println("Your password has been updated successfully.");
					}
					break;
				
				case '5':
					System.out.println("All stored usernames and passwords will be deleted and you will be immediately logged out. Are you sure you want to delete your account? Enter y for yes, enter any other key for no: ");
					selection = scnr.next().charAt(0);
					if(selection == 'y'){
						db.deleteUser(username);
						System.out.println("Your account has been deleted");
						System.out.println("Goodbye.");
					}
					break;
				
				default:
					selection = 'x';
					System.out.println("Goodbye.");
					System.exit(0);
		
			}
		}while(selection != 'x');
		System.exit(0);
	}	
}
