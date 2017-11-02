/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.multisite;

public class Site {
	
	public String getCode() {
		return _code;
	}
	protected void setCode(String code) {
		this._code = code;
	}
	public String getDescr() {
		return _descr;
	}
	protected void setDescr(String descr) {
		this._descr = descr;
	}
	public String getFullApplBaseURL() {
		return _fullApplBaseURL;
	}
	protected void setFullApplBaseURL(String fullApplBaseURL) {
		this._fullApplBaseURL = fullApplBaseURL;
	}
	public String getBaseUrl() {
		return _baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this._baseUrl = baseUrl;
	}
	public String getIp() {
		return _ip;
	}
	public void setIp(String ip) {
		this._ip = ip;
	}

	private String _code;
	private String _descr;
	private String _fullApplBaseURL;
	private String _baseUrl;
	private String _ip;
	
}
