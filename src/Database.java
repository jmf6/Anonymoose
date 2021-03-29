import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
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
	
	
	//Returns a HashMap of all users currently registered in the database and their login passwords as key-value pairs.
	public static HashMap<String,String> getAllUsersAndPasswords(){
		HashMap<String,String> emailAndPasswordMap = new HashMap<String,String>();
		Gson gson = new Gson();

		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				DatabaseUserEntry user = gson.fromJson(cursor.next().toJson(), DatabaseUserEntry.class);
				emailAndPasswordMap.put(user.getEmail(), user.getPassword());
			}
		} finally {
			cursor.close();
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

	//Creates and saves a new user within the database collection, with the given emailAddress and userPassword.
	public static void createNewUser(String emailAddressArg, String userPasswordArg) {
		Document newUser  = new Document("_id", new ObjectId());
		newUser.append("email", emailAddressArg)
		.append("password", userPasswordArg)
		.append("storedPasswords", new ArrayList<>());
		collection.insertOne(newUser);
	}

	//Removes the user with the given email address from the database and all of their stored passwords.
	public static void deleteUser(String userEmailArg) {
		collection.deleteOne(Filters.eq("email", userEmailArg));
	}
	
	
	//Deletes one passwordEntry from the database for the provided user at the provided siteName.
	public static void deletePassword(String emailArg, String siteNameArg) {
		BasicDBObject listItem = (new BasicDBObject("siteName",siteNameArg));
		uniquePassDB.getCollection("users").updateOne(
				new BasicDBObject("email", emailArg),
				new BasicDBObject("$pull", new BasicDBObject("storedPasswords", listItem))
				);
	}

	
	//Updates a password given the user's email address, website of password to be updated, and the new password.
	//If the given website is not already registered, it will register the website.
	public static void updateUserPassword(String emailArg, String websiteArg, String newPasswordArg) {
		deletePassword(emailArg, websiteArg);
		addNewPassword(emailArg, websiteArg, newPasswordArg);
	}
	
	
	//Prints all values for all users currently stored in the database in JSON format.
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
	/*
	public static void main(String[] args) {
		
		//createNewUser("Test Email New", "TestPasswordNew");
		
		HashMap<String,String> g = getAllPasswordsForUser("Testing 2");
		for (Map.Entry<String, String> set : g.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		System.out.println("-------");
		HashMap<String,String> i = getAllUsersAndPasswords();
		for (Map.Entry<String, String> set : i.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		//updateUserPassword("Testing 2", "test website 2", "new test password 2");
		
		//deletePassword("Testing 2", "test website 3");
		

		//Create and insert a new user to the database
		//createNewUser("sample email3", "sample password3");

		//Delete user from the database
		//deleteUser("sample@website.com");

		//printAllValues();  

	}
	*/
}


