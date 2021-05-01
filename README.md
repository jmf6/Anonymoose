# Anonymoose
The Anonymoose Password Manager is an application that allows users to store passwords, update passwords, generate a complex password, and check if they have reused a password on multiple sites. 

# Repository Contents

src/Database.java: Class File - This class interacts with the web hosted database and uses functions in the DatabasePasswordEntry and DatabaseUserEntry classes to retrieve information 

src/DatabasePasswordEntry.java: Class File - This class performs all interactions that involve user passwords

src/DatabaseUserEntry: Class File - This class performs all interactions that involve user information in the database
src/Hasher.java: Class File - This class performs all hashing and de-hashing of user passwords that are stored
src/Password.java: Class File - This class stores passwords with their respective secret key to be able to de-hash them in the future
src/PasswordGenerator.java: Class File - This class randomly generators secure passwords for users
src/User.java: Class File - This class stores user information for users that have already created an account in the password manager
src/Username.java: Class File - This class stores username information and a secret key for de-hashing
src/Main.java: Main Application File - This file is the main program that is run
