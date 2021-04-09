import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Password {
	
	private String hashedPass;
	private SecretKey secretKey;
	private IvParameterSpec spec;

	public Password(String hashedPass, SecretKey secretKey, IvParameterSpec spec) {
		this.hashedPass = hashedPass;
		this.secretKey = secretKey;
		this.spec = spec;
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	public String getHashedPass() {
		return hashedPass;
	}

	public IvParameterSpec getSpec() {
		return spec;
	}
}
