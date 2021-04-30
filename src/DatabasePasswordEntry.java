import java.io.Serializable;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.mongodb.BasicDBObject;

public class DatabasePasswordEntry implements Serializable{

	private static final long serialVersionUID = -85403779212127775L;
	private String id = UUID.randomUUID().toString();
	private String siteName;
	private String sitePassword;
	public SecretKey key;
	public IvParameterSpec spec;
	
	public DatabasePasswordEntry(String siteNameArg, String sitePasswordArg, SecretKey key, IvParameterSpec spec) {
		siteName = siteNameArg;
		sitePassword = sitePasswordArg;
		this.key = key;
		this.spec = spec;
	}

	public String getId() {
		return id;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPassword() {
		return sitePassword;
	}

	public void setPassword(String password) {
		this.sitePassword = password;
	}
	
	public SecretKey getKeys() {
		return key;
	}
	
	public IvParameterSpec getSpec() {
		return spec;
	}
	
}
