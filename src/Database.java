import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;

public class Database {

	//Credentials string used to establish connection to the MongoDB database
	static ConnectionString connString = new ConnectionString(
			"mongodb+srv://anonymooseadmin:D6cTw6PxjSDGnkMf@cluster0.q4jee.mongodb.net/UniquePass?retryWrites=true&w=majority"
			);
	static CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	//MongoClient object that connects the Java application to MongoDB
	static MongoClient mongoClient = MongoClients.create(connString);
	//The database object being referenced
	static MongoDatabase uniquePassDB = mongoClient.getDatabase("uniquepass").withCodecRegistry(pojoCodecRegistry);;
	//The collection within the database being referenced
	static MongoCollection<Document> collection = uniquePassDB.getCollection("users");
	
	
	public Database() {
	}
	
	//Returns a HashMap of all users currently registered in the database and their login passwords as key-value pairs.
	public HashMap<String,String> getAllUsersAndPasswords(){
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
	public void addNewPassword(String loginArg, String siteName, String sitePassword, SecretKey key, IvParameterSpec spec) {
		
		BasicDBObject listItem = (new BasicDBObject("siteName",siteName).append("sitePassword",sitePassword).append("secretkey", key).append("spec", spec));
		//System.out.println("___________");
		//System.out.println(key);
		uniquePassDB.getCollection("users").updateMany(
				new BasicDBObject("email", loginArg),
				new BasicDBObject("$push", new BasicDBObject("storedPasswords", listItem))
				);
	}
	
	
	//Returns a HashMap of all the stored website names and passwords as key-value pairs for a specified user.
	//Returns an empty HashMap if the specified user was not found.
	public HashMap<String,ArrayList<Object>> getAllPasswordsForUser(String loginArg){
		
		HashMap<String,ArrayList<Object>> websitesAndPasswordsMap = new HashMap<String,ArrayList<Object>>();
		
		Document myDoc = collection.find(Filters.eq("email", loginArg)).first();
		BasicDBObject d = new BasicDBObject(myDoc);
		Gson gson = new Gson();
		DatabaseUserEntry user = gson.fromJson(d.toString(), DatabaseUserEntry.class);
		ArrayList<Object> passes = new ArrayList<Object>();
		for(DatabasePasswordEntry p : user.getStoredPasswords()) {
			SecretKey h = p.getKeys();
			//System.out.println(h);
			passes.add(p.getPassword());
			passes.add(h);
			passes.add(p.getSpec());
			//System.out.println(passes);
			websitesAndPasswordsMap.put(p.getSiteName(), passes);
		}
		
		return websitesAndPasswordsMap;
	}

	//Creates and saves a new user within the database collection, with the given emailAddress and userPassword.
	public void createNewUser(String loginArg, String userPasswordArg) {
		Document newUser  = new Document("_id", new ObjectId());
		newUser.append("email", loginArg)
		.append("password", userPasswordArg)
		.append("storedPasswords", new ArrayList<>());
		collection.insertOne(newUser);
	}

	//Removes the user with the given email address from the database and all of their stored passwords.
	public void deleteUser(String loginArg) {
		collection.deleteOne(Filters.eq("email", loginArg));
	}
	
	
	//Deletes one passwordEntry from the database for the provided user at the provided siteName.
	public void deletePassword(String loginArg, String siteNameArg) {
		
		HashMap<String, ArrayList<Object>> temp = getAllPasswordsForUser(loginArg);
		for (Map.Entry<String, ArrayList<Object>> set : temp.entrySet()) {
		    if(set.getKey().equals(siteNameArg)) {
				BasicDBObject listItem = (new BasicDBObject("siteName",siteNameArg));
				uniquePassDB.getCollection("users").updateOne(
						new BasicDBObject("email", loginArg),
						new BasicDBObject("$pull", new BasicDBObject("storedPasswords", listItem))
						);
				return;
		    }
		}
		
		System.out.println("The requested site name was not found.");

	}

	
	//Updates a password given the user's email address, website of password to be updated, and the new password.
	//If the given website is not already registered, it will register the website.
	public void updateUserPassword(String loginArg, String websiteArg, String newPasswordArg, SecretKey key, IvParameterSpec spec) {
		deletePassword(loginArg, websiteArg);
		addNewPassword(loginArg, websiteArg, newPasswordArg, key, spec);
	}
	
	//Returns true if the given username does not already exist in the database.
	public boolean usernameFree(String loginArg) {
		HashMap<String,String> emailAndPasswordMap = getAllUsersAndPasswords();
		for (Map.Entry<String, String> set : emailAndPasswordMap.entrySet()) {
			if(set.getKey().equals(loginArg)){
				return false;
			}
		}
		return true;	
	}
	
	//Returns true if the given login and password arguments exist for a user in the database.
	public boolean validLogin(String loginArg, String passwordArg) {
		HashMap<String,String> emailAndPasswordMap = getAllUsersAndPasswords();
		for (Map.Entry<String, String> set : emailAndPasswordMap.entrySet()) {
			if(set.getKey().equals(loginArg) && set.getValue().equals(passwordArg)) {
				return true;
			}    
		}
		return false;
	}
	
	
	//Prints all values for all users currently stored in the database in JSON format.
	public void printAllValues() {
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
		
		Database d = new Database();
			
		//d.createNewUser("New Test", "New Test 2");
		
		d.deletePassword("Testing 2", "test website 4");
		
		//d.addNewPassword("New Test", "NewSite2", "NewPass2");
		
		HashMap<String, ArrayList<Object>> g = d.getAllPasswordsForUser("New Test");
		for (Entry<String, ArrayList<Object>> set : g.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		System.out.println("-------");
		HashMap<String,String> i = d.getAllUsersAndPasswords();
		for (Map.Entry<String, String> set : i.entrySet()) {
		    System.out.println(set.getKey() + " = " + set.getValue());
		}
		
		System.out.println(d.validLogin("Testing 2","Password 2"));
		

		//Create and insert a new user to the database
		//d.createNewUser("sample email", "sample password");

		//Delete user from the database
		//d.deleteUser("sample email");

		//printAllValues();  

	}
	
}


