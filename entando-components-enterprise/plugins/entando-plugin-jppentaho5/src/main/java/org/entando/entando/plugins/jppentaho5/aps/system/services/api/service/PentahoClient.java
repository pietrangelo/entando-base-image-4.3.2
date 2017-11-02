package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.IClient;



/**
 *
 * @author entando
 */
public class PentahoClient extends IClient {
	
	public PentahoClient(PentahoAuthenticationCredentials credentials) {
		this._credentials = credentials;
	}

	public HttpHost toHost() {
		return new HttpHost(_hostname, _port, _schema);
	}

	@Override
	protected void init() {
		// do nothing
	}

	public PentahoAuthenticationCredentials getCredentials() {
		return _credentials;
	}

	public void setCredentials(PentahoAuthenticationCredentials credentials) {
		this._credentials = credentials;
	}

	public String getBaseUrl() {
		URIBuilder uri = new URIBuilder();

		uri.setScheme(_schema);
		uri.setHost(_hostname);
		uri.setPort(_port);

		uri.setPath(_basePath);
		return uri.toString();
	}

	public String getHostname() {
		return _hostname;
	}

	public void setHostname(String hostname) {
		this._hostname = hostname;
	}

	public String getSchema() {
		return _schema;
	}

	public void setSchema(String schema) {
		this._schema = schema;
	}

	public int getPort() {
		return _port;
	}

	public void setPort(int port) {
		this._port = port;
	}

	public String getBasePath() {
		return _basePath;
	}

	public void setBasePath(String _basePath) {
		this._basePath = _basePath;
	}
	private PentahoAuthenticationCredentials _credentials; 
	private String _hostname;
	private String _schema = "http";
	private int _port = 80;

	private String _basePath;	

}
