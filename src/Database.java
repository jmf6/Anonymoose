import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Database {

	//Credentials string used to establish connection to the MongoDB database
	static ConnectionString connString = new ConnectionString(
			"mongodb+srv://anonymooseadmin:D6cTw6PxjSDGnkMf@cluster0.q4jee.mongodb.net/UniquePass?retryWrites=true&w=majority"
			);
	//MongoClient object that connects the Java application to MongoDB
	static MongoClient mongoClient = MongoClients.create(connString);
	//The database object being referenced
	static MongoDatabase uniquePassDB = mongoClient.getDatabase("uniquepass");
	//The collection within the database being referenced
	static MongoCollection<Document> collection = uniquePassDB.getCollection("users");
	
	
	//Returns a HashMap of all users currently registered in the database and their login passwords.
	//BUG: Passwords are not always saved in the right order -- need to fix this
	public static HashMap<String,String> getAllUsersAndPasswords(){
		
		//Create an ArrayList of all currently registered users...
		ArrayList<String> emails = new ArrayList<String>();
		MongoCursor<String> emailCursor = collection.distinct("email", String.class).iterator();
		try {
			while (emailCursor.hasNext()) {
				emails.add(emailCursor.next());
			}
		} finally {
			emailCursor.close();
		}
		
		//Create an ArrayList of all currently registered users' passwords...
		ArrayList<String> passwords = new ArrayList<String>();
		MongoCursor<String> passwordCursor = collection.distinct("password", String.class).iterator();
		try {
			while (passwordCursor.hasNext()) {
				passwords.add(passwordCursor.next());
			}
		} finally {
			passwordCursor.close();
		}
		
		//Use the ArrayLists to create a HashMap of all user emails and passwords
		HashMap<String,String> emailAndPasswordMap = new HashMap<String,String>();
		for(int i = 0; i < emails.size(); i++) {
			emailAndPasswordMap.put(emails.get(i), passwords.get(i));
		}
		return emailAndPasswordMap;
	}
	
	//Adds a new PasswordEntry object within the database for the given user, with the given siteName and sitePassword.
	public static void addNewPassword(String emailArg, String siteName, String sitePassword) {
		
		BasicDBObject listItem = (new BasicDBObject("siteName",siteName).append("sitePassword",sitePassword));
		
		uniquePassDB.getCollection("users").updateOne(
				new BasicDBObject("email", emailArg),
				new BasicDBObject("$push", new BasicDBObject("storedPasswords", listItem))
				);
	}
	
	
	//Returns a HashMap of all the stored website names and passwords as key-value pairs for a specified user.
	//Returns an empty HashMap if the specified user was not found.
	public static HashMap<String,String> getAllPasswordsForUser(String emailArg){
		
		HashMap<String,String> websitesAndPasswordsMap = new HashMap<String,String>();
		
		Document myDoc = collection.find(Filters.eq("email", emailArg)).first();
		BasicDBObject d = new BasicDBObject(myDoc);
		Gson gson = new Gson();
		DatabaseUserEntry user = gson.fromJson(d.toString(), DatabaseUserEntry.class);
		
		for(DatabasePasswordEntry p : user.getStoredPasswords()) {
			websitesAndPasswordsMap.put(p.getSiteName(), p.getPassword());
		}
		
		return websitesAndPasswordsMap;
	}

	//Creates and saves a new user within the database collection.
	//Need to check for valid email and password
	public static void createNewUser(String emailAddressArg, String userPasswordArg) {
		Document newUser  = new Document("_id", new ObjectId());
		newUser.append("email", emailAddressArg)
		.append("password", userPasswordArg)
		.append("storedPasswords", new ArrayList<>());
		collection.insertOne(newUser);
	}

	//Removes the user with the given email address and all of their stored passwords.
	public static void deleteUser(String userEmailArg) {
		collection.deleteOne(Filters.eq("email", userEmailArg));
	}

	/*
	//Updates a password given the user's email address, website of password to be updated, and the new password
	//If the given website is not already registered, it will register the website.
	//This method broke when I changed how password entries are stored -- need to fix this.
	
	public static void updateUserPassword(String emailArg, String websiteArg, String newPasswordArg) {
		uniquePassDB.getCollection("users").updateOne(
				new BasicDBObject("email", emailArg),
				new BasicDBObject("$set", new BasicDBObject("storedPasswords."+websiteArg, newPasswordArg))
				);
	}
	*/
	
	//Prints all values for all users currently stored in the database
	public static void printAllValues() {
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}

	//Main method for testing
	public static void main(String[] args) {
		
		HashMap<String,String> h = getAllUsersAndPasswords();
		for (Map.Entry<String, String> set : h.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		HashMap<String,String> g = getAllPasswordsForUser("Testing 2");
		for (Map.Entry<String, String> set : g.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		//createNewUser("Testing 3","Password 3");
		
		//addNewPassword("Testing 2", "test website 4", "test password 4");
		
		//Gson gson = new Gson();

		//createNewUser("Testing", "Testing");
		//addNewPassword("Testing", "email2", "password2");
		
		//updatePasswords("sample email", "website5", "test5");

		//Create and insert a new user to the database
		//createNewUser("sample email3", "sample password3");

		//Delete user from the database
		//deleteUser("sample@website.com");

		//printAllValues();  

	}
}


