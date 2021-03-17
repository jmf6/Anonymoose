/**
 * @author Jesstina Farrell, Elizabeth Porter, Zack Starrett, Jaclyn Bramlette
 * @Team Anonymoose
 * 
 * This class generates a random password. Passwords genertated will be ten characters
 * and must contain at least one number, one uppercase letter, one lowercase letter
 * and one special character.
 * 
 * 
 **/

import java.util.Random;

public class PasswordGenerator {

	//variables
	char password[] = {'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'};
	int charNeeded = 16;
	boolean positionArray[] = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

	
	public PasswordGenerator() {
		//initialize password array

	}
	
	//this function decides how many numbers
	public int numOfNumbers() {
		int numsNeeded = 0;
		Random rand = new Random();
		int randN = rand.nextInt(5) + 1;
		numsNeeded = randN;
		charNeeded = charNeeded - numsNeeded;
		return numsNeeded;
	}
	
	//this function decides how many special characters
	public int numOfSpecChar() {
		int specCharNeeded = 0;
		Random rand = new Random();
		int randN = rand.nextInt(5) + 1;
		specCharNeeded = randN;
		charNeeded = charNeeded - specCharNeeded;
		return specCharNeeded;
	}
	
	//this function decides how many uppercase letters
	public int numOfUpper() {
		int upperNeeded = 0;
		Random rand = new Random();
		int randN = rand.nextInt(5) + 1;
		upperNeeded = randN;
		charNeeded = charNeeded - upperNeeded;
		return upperNeeded;
	}
	
	//this function decides how many lowercase letters
	public int numOfLower() {
		int lowerNeeded = charNeeded;
		charNeeded = charNeeded - lowerNeeded;
		return lowerNeeded;
	}
	
	//this function generates a random password
	public char[] passwordGen() {
		char passwordR[] = password;
		//arrays of possible characters
		char numberArray[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		char specialArray[] = {'!', '@', '#', '$', '%', '^', '&', '*', '?', '+', '=', '(', ')'};
		char upperArray[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		char lowerArray[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
		//num of each character
		int numNumber = numOfNumbers();
		int numSpecial = numOfSpecChar();
		int numUpper = numOfUpper();
		int numLower = numOfLower();
		
		//decides which character
		Random rand = new Random();
		
		//finds positions of numbers
		for(int i = 0; i < numNumber; i++) {
			int numberR = rand.nextInt(10);
			int index = rand.nextInt(16);
			int ind = checkValid(index);
			positionArray[ind] = false;			
			//System.out.println("Number indexes " + ind);
			passwordR[ind] = numberArray[numberR];
		}
		
		//finds position of special character
		for(int i = 0; i < numSpecial; i++) {
			int specialR = rand.nextInt(13);
			int index = rand.nextInt(16);
			int ind = checkValid(index);
			positionArray[ind] = false;			
			//System.out.println("Special indexes " + ind);
			passwordR[ind] = specialArray[specialR];
		}
		
		//finds position of upper case letter
		for(int i = 0; i < numUpper; i++) {
			int upperR = rand.nextInt(26);
			int index = rand.nextInt(16);
			int ind = checkValid(index);
			positionArray[ind] = false;			
			//System.out.println("Upper indexes " + ind);
			passwordR[ind] = upperArray[upperR];
		}
		
		//finds position of lower case letters
		for(int i = 0; i < numLower; i++) {
			int lowerR = rand.nextInt(26);
			int index = rand.nextInt(16);
			int ind = checkValid(index);
			positionArray[ind] = false;			
			//System.out.println("Lower indexes " + ind);
			passwordR[ind] = lowerArray[lowerR];
		}

		
		return passwordR;
	}
	
	//checks if the position in the password is already taken
	private int checkValid(int ind) {
		int ind1;
		if(positionArray[ind] == true) {
			return ind;
		}
		if((ind == 15) && (positionArray[ind] == true)) {
			return ind;
		}
		else {
			if((ind < 15) && (positionArray[ind] == false)) {
				ind1 = ind + 1;
				return checkValid(ind1);
			
			}
			else  {
	
					ind1 = 0;
					return checkValid(ind1);
				
			}
		}
		
	}
	
	//Main to test the class. comment out when done
	public static void main(String[] args) {
		PasswordGenerator pass = new PasswordGenerator();
		char passy[] = pass.passwordGen();
		System.out.println(passy);
	}
	
	
	
	
}
