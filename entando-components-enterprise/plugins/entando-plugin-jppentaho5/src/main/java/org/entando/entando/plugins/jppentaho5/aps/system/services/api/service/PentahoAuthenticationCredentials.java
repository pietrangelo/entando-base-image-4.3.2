package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import org.entando.entando.plugins.jprestapi.aps.core.IAuthenticationCredentials;


/**
 *
 * @author entando
 */
public class PentahoAuthenticationCredentials implements IAuthenticationCredentials {

	@Override
	public boolean valid() {
		return (null != _username && null != _password);
	}

	public String getUsername() {
		return _username;
	}

	public void setUsername(String username) {
		this._username = username;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		this._password = password;
	}

	private String _username;
	private String _password;

}
