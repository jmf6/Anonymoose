import java.io.Serializable;
import java.util.UUID;

import com.mongodb.BasicDBObject;

public class DatabasePasswordEntry implements Serializable{

	private static final long serialVersionUID = -85403779212127775L;
	private String id = UUID.randomUUID().toString();
	private String siteName;
	private String sitePassword;
	
	public DatabasePasswordEntry(String siteNameArg, String sitePasswordArg) {
		siteName = siteNameArg;
		sitePassword = sitePasswordArg;
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
	
	
	
}
