import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Hasher {

	//These need to be saved with the encoded word so it can be decoded later
	private static SecretKey secretKey;
	private static IvParameterSpec spec;
	
	public Hasher() {
	
	}
	
	public SecretKey getSecretKey() {
		return secretKey;
	}
	
	public IvParameterSpec getIV() {
		return spec;
	}
	
	//Generates a secret key
	public static SecretKey createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		SecretKey secret = keyGenerator.generateKey();
		return secret;
	}
	
	//Generates the IvParameterSpec
	public static IvParameterSpec generateIV() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		spec = new IvParameterSpec(iv);
		return spec;
	}
	
	//Function to encrypt
	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}
	
	//Function to decrypt
	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}
	
	//Runs encrypt function
	public String runEncrypt(String encodeMe) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String cipherText;
		secretKey = createSecretKey();
		spec = generateIV();
		String algorithm = "AES/CBC/PKCS5Padding";
		cipherText = encrypt(algorithm, encodeMe, secretKey, spec);
		return cipherText;
	}
	
	//Runs decrypt function
	public String runDecrypt(String decodeMe) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		String plainText;
		SecretKey key = getSecretKey();
		IvParameterSpec iv = getIV();
		String algorithm = "AES/CBC/PKCS5Padding";
		plainText = decrypt(algorithm, decodeMe, key, iv);
		return plainText;
	}
	
	/*
	//Main function to test, comment out when done
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		String cipherText = runEncrypt("hello123!");
		String plainText = runDecrypt(cipherText);
		System.out.println(cipherText);
		System.out.println(plainText);

	}*/


}
