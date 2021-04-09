import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Username {
	
	private String hashedUser;
	private SecretKey secretKey;
	private IvParameterSpec spec;
	
	public Username(String hashedUser, SecretKey secretKey, IvParameterSpec spec) {
		this.hashedUser = hashedUser;
		this.secretKey = secretKey;
		this.spec = spec;
	}

	public String getHashedUser() {
		return hashedUser;
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}

	public IvParameterSpec getSpec() {
		return spec;
	}

}
