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
package com.agiletec.plugins.jppentaho.aps.system.services.config;

public class PentahoConfig implements Cloneable {
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		PentahoConfig config = new PentahoConfig();
		config.setDebug(this.isDebug());
		config.setDataIntegrationPassword(this.getDataIntegrationPassword());
		config.setDataIntegrationRepositoryName(this.getDataIntegrationRepositoryName());
		config.setDataIntegrationUsername(this.getDataIntegrationUsername());
		config.setReportDefBasePath(this.getReportDefBasePath());
		config.setReportDetailPage(this.getReportDetailPage());
		config.setServerLocalPort(this.getServerLocalPort());
		config.setServerLocalProtocol(this.getServerLocalProtocol());
		config.setServerLocalUrl(this.getServerLocalUrl());
		config.setServerPort(this.getServerPort());
		config.setServerProtocol(this.getServerProtocol());
		config.setServerUrl(this.getServerUrl());
                
                config.setServerContextPath(this.getServerContextPath());
                
		config.setServerPassword(this.getServerPassword());
		config.setServerUsername(this.getServerUsername());
		config.setServerVisibleFromClient(this.isServerVisibleFromClient());
		return config;
	}
	
	public String getServerUsername() {
		return _serverUsername;
	}
	public void setServerUsername(String serverUsername) {
		this._serverUsername = serverUsername;
	}
	
	public String getServerPassword() {
		return _serverPassword;
	}
	public void setServerPassword(String serverPassword) {
		this._serverPassword = serverPassword;
	}
	
	public String getServerLocalUrl() {
		return _serverLocalUrl;
	}
	public void setServerLocalUrl(String serverLocalUrl) {
		this._serverLocalUrl = serverLocalUrl;
	}
	
	public Integer getServerLocalPort() {
		return _serverLocalPort;
	}
	public void setServerLocalPort(Integer serverLocalPort) {
		this._serverLocalPort = serverLocalPort;
	}
	
	public String getServerLocalProtocol() {
		return _serverLocalProtocol;
	}
	public void setServerLocalProtocol(String serverLocalProtocol) {
		this._serverLocalProtocol = serverLocalProtocol;
	}
	
	public String getServerUrl() {
		return _serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this._serverUrl = serverUrl;
	}
	
	public Integer getServerPort() {
		return _serverPort;
	}
	public void setServerPort(Integer serverPort) {
		this._serverPort = serverPort;
	}
	
	public String getServerProtocol() {
		return _serverProtocol;
	}
	public void setServerProtocol(String serverProtocol) {
		this._serverProtocol = serverProtocol;
	}

    public String getServerContextPath() {
        return _serverContextPath;
    }
    public void setServerContextPath(String serverContextPath) {
        this._serverContextPath = serverContextPath;
    }
	
	public String getReportDefBasePath() {
		return _reportDefBasePath;
	}
	public void setReportDefBasePath(String reportDefBasePath) {
		this._reportDefBasePath = reportDefBasePath;
	}
	
	public boolean isServerVisibleFromClient() {
		return _serverVisibleFromClient;
	}
	public void setServerVisibleFromClient(boolean serverVisibleFromClient) {
		this._serverVisibleFromClient = serverVisibleFromClient;
	}
	
	public String getReportDetailPage() {
		return _reportDetailPage;
	}
	public void setReportDetailPage(String reportDetailPage) {
		this._reportDetailPage = reportDetailPage;
	}
	
	public String getDataIntegrationRepositoryName() {
		return _dataIntegrationRepositoryName;
	}
	public void setDataIntegrationRepositoryName(
			String dataIntegrationRepositoryName) {
		this._dataIntegrationRepositoryName = dataIntegrationRepositoryName;
	}
	
	public String getDataIntegrationUsername() {
		return _dataIntegrationUsername;
	}
	public void setDataIntegrationUsername(String dataIntegrationUsername) {
		this._dataIntegrationUsername = dataIntegrationUsername;
	}
	
	public String getDataIntegrationPassword() {
		return _dataIntegrationPassword;
	}
	public void setDataIntegrationPassword(String dataIntegrationPassword) {
		this._dataIntegrationPassword = dataIntegrationPassword;
	}

	public boolean isDebug() {
		return _debug;
	}
	public void setDebug(boolean debug) {
		this._debug = debug;
	}

	private String _serverUsername;
	private String _serverPassword;
	private String _serverLocalUrl;
	private Integer _serverLocalPort;
	private String _serverLocalProtocol;
	private String _serverUrl;
	private Integer _serverPort;
	private String _serverProtocol;
        
	private String _serverContextPath;
        
	private String _reportDefBasePath;
	private boolean _serverVisibleFromClient;
	private String _reportDetailPage;
	
	private String _dataIntegrationRepositoryName;
	private String _dataIntegrationUsername;
	private String _dataIntegrationPassword;
	
	private boolean _debug;
	
}