import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class Entry {
	private String username;
	private String hash;
	private String password;
	
	public Entry() {
		this.setUsername("None");
		this.setHash("None");
	}
	
	public Entry(String username) {
		PasswordGenerator p = new PasswordGenerator();
		this.setUsername(username);
		createPassword(p);
	}
	
	public Entry(String username, String hash) {
		this.setUsername(username);
		this.setHash(hash);
	}
	
	//returns an encrypted password
	public Password getPasswordHash() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String pass = getPassword();
		Hasher hash = new Hasher();
		String encryptedPassw = hash.runEncrypt(pass);
		IvParameterSpec spec = hash.getIV();
		SecretKey secret = hash.getSecretKey();
		Password encryptedPass = new Password(encryptedPassw, secret, spec);
		return encryptedPass;	
	}
	
	public Username getUsernameHash() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String user = getUsername();
		Hasher hash = new Hasher();
		String encryptedUsern = hash.runEncrypt(user);
		IvParameterSpec spec = hash.getIV();
		SecretKey secret = hash.getSecretKey();
		Username encryptedUser = new Username(encryptedUsern, secret, spec);
		return encryptedUser;	
	}
	
	private String getUsername() {
		return username;
	}
	
	private String getPassword() {
		return password;
	}

	
	private void setUsername(String username) {
		this.username = username;
	}
	
	private void setHash(String hash) {
		this.hash = hash;
	}
	
	private void createPassword(PasswordGenerator p) {
		setHash(String.valueOf(p.passwordGen()));
	}
}
