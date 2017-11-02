package org.entando.entando.plugins.jpactiviti.aps.system.config;

public class ActivitiConfig {

	public String getServerURL() {
		String url = this._serverURL;
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public void setServerURL(String serverURL) {
		this._serverURL = serverURL;
	}

	private String _serverURL;
}
