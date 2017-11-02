package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.entando.entando.plugins.jprestapi.aps.core.RequestBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.helper.RequestHelper;


/**
 *
 * @author entando
 * 
 */
public class PentahoRequestBuilder extends RequestBuilder {

	public PentahoRequestBuilder(PentahoClient client) {
		this._client = client;
		this._credentials = client.getCredentials();
	}

	@Override
	protected void setupRequest(HttpRequestBase verb) throws Throwable {
		// process evaluation URL
		RequestHelper.addBaseUrl(verb, _client.getBaseUrl());
		// fixed headers
		verb.setHeader(HTTP.CONTENT_TYPE, "application/json");
	}
  
  @Override
  protected DefaultHttpClient setupClient() {
    DefaultHttpClient client = new DefaultHttpClient();
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setContentCharset(params, "UTF-8");
    
    final String username = _client.getCredentials().getUsername();
    final String password = _client.getCredentials().getPassword();

    Credentials credentials = new UsernamePasswordCredentials(username, password);
    client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);

    return client;
  }

	public PentahoClient _client;

}
