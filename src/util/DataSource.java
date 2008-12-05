package util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DataSource implements Externalizable {
	public DataSource(String login, String mdp, String url, String type) {
		super();
		this.login = login;
		this.mdp = mdp;
		this.url = url;
		this.type = type;
	}

	private String login;
	private String mdp;
	private String url;
	private String type;
	
	
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.login = (String)in.readObject();
		this.mdp = (String)in.readObject();
		this.url = (String)in.readObject();
		this.type = (String)in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(login);
		out.writeObject(mdp);
		out.writeObject(url);
		out.writeObject(type);
	}

}
