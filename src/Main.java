import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Main {
	
	public static void main(String[]args) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
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
				Hasher hash1 = new Hasher();
				userInput = hash1.runEncrypt(userInput);
				SecretKey key1 = hash1.getSecretKey();
				IvParameterSpec spec1 = hash1.getIV();
				//System.out.println(userInput);
				System.out.println("Confirm your password: ");
				passwordInput = scnr.nextLine();
				//hash password
				String algorithm = "AES/CBC/PKCS5Padding";
				passwordInput = hash1.encrypt(algorithm, passwordInput, key1, spec1);
				//System.out.println(passwordInput);
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
					//These passwords will need to be dehashed (everything in the hashmap Value set)
					HashMap<String, ArrayList<Object>> userEntries = db.getAllPasswordsForUser(username);

					//Display everything in console
					for (Entry<String, ArrayList<Object>> set : userEntries.entrySet()) {
						System.out.println("Site Name: " + set.getKey());
						ArrayList<Object> ob = set.getValue();
						System.out.println(ob);
						SecretKey key1 = (SecretKey) ob.get(1);
						IvParameterSpec spec1 = (IvParameterSpec) ob.get(2);
						String cipher = (String) ob.get(0);
						Hasher hash7 = new Hasher();
						String algorithm = "AES/CBC/PKCS5Padding";
						String un = hash7.decrypt(algorithm, cipher, key1, spec1);
						System.out.println("Site Password: " + un); //dehash as passwords print
						System.out.println("");
					}
					break;
				
				case '2':
					System.out.println("Enter the site name: ");
					site = scnr.nextLine();
					System.out.println("Would you like to generate a new random password? Enter y for yes, enter n for no: ");
					selection = scnr.next().charAt(0);
					if(selection != 'y' && selection != 'n'){
						passwordInput = String.valueOf(p.passwordGen());
						System.out.println("Your password for " + site + " is: " + passwordInput);
						//hash passwordInput here
						//passwordInput = passwordInput hashed
						Hasher hash2 = new Hasher();
						passwordInput = hash2.runEncrypt(passwordInput);
						SecretKey key2 = hash2.getSecretKey();
						IvParameterSpec spec2 = hash2.getIV();
						//Password passwordIn = new Password(passwordInput, key2, spec2);
						//System.out.println(key2);
						db.addNewPassword(username, site, passwordInput, key2, spec2);
					}
					else{
						System.out.println("Enter the password: ");
						passwordInput = scnr.nextLine(); 
						//passwordInput = passwordInput hashed
						Hasher hash2 = new Hasher();
						passwordInput = hash2.runEncrypt(passwordInput);
						SecretKey key2 = hash2.getSecretKey();
						IvParameterSpec spec2 = hash2.getIV();
						db.addNewPassword(username, site, passwordInput, key2, spec2);
					}
					break;
				
				case '3':
					System.out.println("Enter the site that you would like to delete the password for: ");
					site = scnr.nextLine();
					db.deletePassword(username, site);
					System.out.println("Password deleted.");
					break;
				
				case '4':
					System.out.println("Enter the site that you would like to update the password for: ");
					site = scnr.nextLine();
					System.out.println("Enter g if you would like to randomly generate a password, enter any other key to set your own password: ");
					selection = scnr.next().charAt(0);
					if(selection == 'g'){
						passwordInput = String.valueOf(p.passwordGen());
						System.out.println("Your new password for " + site + " is: " + passwordInput);
						//hash passwordInput here
						//passwordInput = passwordInput hashed
						Hasher hash4 = new Hasher();
						passwordInput = hash4.runEncrypt(passwordInput);
						SecretKey key4 = hash4.getSecretKey();
						IvParameterSpec spec4 = hash4.getIV();
						db.updateUserPassword(username, site, passwordInput, key4, spec4);
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
