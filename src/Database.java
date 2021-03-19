import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import java.util.HashMap;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
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

	//Creates a new user within the database collection, also creates an empty HashMap to store future passwords
	public static void createNewUser(String emailAddressArg) {
		Document newUser  = new Document("_id", new ObjectId());
		newUser.append("email", emailAddressArg)
		.append("passwords", new HashMap<String, String>());
		collection.insertOne(newUser);
	}

	//Removes the user with the given email address and all of their stored passwords
	public static void deleteUser(String userEmailArg) {
		collection.deleteOne(Filters.eq("email", userEmailArg));
	}

	//Updates a password given the user's email address, website of password to be updated, and the new password
	//If the given website is not already registered, it will register the website.
	public static void updatePasswords(String emailArg, String websiteArg, String newPasswordArg) {
		uniquePassDB.getCollection("users").updateOne(
				new BasicDBObject("email", emailArg),
				new BasicDBObject("$set", new BasicDBObject("passwords."+websiteArg, newPasswordArg))
				);
	}

	//Prints all stored user email addresses and passwords
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

		System.out.println("First Entry: " + collection.find().first().toJson());

		//updatePasswords("sample@website.com", "website5", "test5");

		//Create and insert a new user to the database
		//createNewUser("sample email");

		//Delete user from the database
		//deleteUser("sample email");

		printAllValues();  

	}
}


