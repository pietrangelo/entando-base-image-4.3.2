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
package org.entando.entando.plugins.jpsocial.aps.system.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.entando.entando.plugins.jpsocial.aps.oauth.client.ExcerptInputStream;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpMessage;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpResponseMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * Utility methods for an OAuth client based on the Jakarta Commons HTTP client.
 * 
 * @author John Kristian
 */
public class TwitterHttpClient implements org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpClient {
	
    public TwitterHttpClient() {}
	
    public TwitterHttpClient(HttpClientPool clientPool) {
        this._clientPool = clientPool;
    }
	
    private HttpClientPool _clientPool = new TwitterHttpClient.SingleClient();
	
	@Override
    public HttpResponseMessage execute(HttpMessage request, Map<String, Object> parameters) throws IOException {
        final String method = request.method;
        final String url = request.url.toExternalForm();
        final InputStream body = request.getBody();
        final boolean isDelete = DELETE.equalsIgnoreCase(method);
        final boolean isPost = POST.equalsIgnoreCase(method);
        final boolean isPut = PUT.equalsIgnoreCase(method);
        byte[] excerpt = null;
        HttpMethod httpMethod;
        if (isPost || isPut) {
            EntityEnclosingMethod entityEnclosingMethod =
                isPost ? new PostMethod(url) : new PutMethod(url);
            if (body != null) {
                ExcerptInputStream e = new ExcerptInputStream(body);
				String length = request.removeHeaders(HttpMessage.CONTENT_LENGTH);
				entityEnclosingMethod.setRequestEntity((length == null)
                        ? new InputStreamRequestEntity(e)
                        : new InputStreamRequestEntity(e, Long.parseLong(length)));
				excerpt = e.getExcerpt();
            }
            httpMethod = entityEnclosingMethod;
        } else if (isDelete) {
            httpMethod = new DeleteMethod(url);
        } else {
			httpMethod = new GetMethod(url);
        }
		for (Map.Entry<String, Object> p : parameters.entrySet()) {
            String name = p.getKey();
            String value = p.getValue().toString();
            if (FOLLOW_REDIRECTS.equals(name)) {
                httpMethod.setFollowRedirects(Boolean.parseBoolean(value));
            } else if (READ_TIMEOUT.equals(name)) {
                httpMethod.getParams().setIntParameter(HttpMethodParams.SO_TIMEOUT, Integer.parseInt(value));
            }
        }
		for (Map.Entry<String, String> header : request.headers) {
            httpMethod.addRequestHeader(header.getKey(), header.getValue());
        }
		HttpClient client = this._clientPool.getHttpClient(new URL(httpMethod
                .getURI().toString()));
		client.executeMethod(httpMethod);
        return new HttpMethodResponse(httpMethod, excerpt, request.getContentCharset());
    }

    //private final HttpClientPool SHARED_CLIENT = new SingleClient();
    
    /**
     * A pool that simply shares a single HttpClient, as recommended <a
     * href="http://hc.apache.org/httpclient-3.x/performance.html">here</a>. An
     * HttpClient owns a pool of TCP connections. So, callers that share an
     * HttpClient will share connections. Sharing improves performance (by
     * avoiding the overhead of creating connections) and uses fewer resources
     * in the client and its servers.
     */
    private class SingleClient implements HttpClientPool {
        
		SingleClient(){
            this.client = new HttpClient();
            this.client.setHttpConnectionManager(new MultiThreadedHttpConnectionManager());
		}
		
        public HttpClient getHttpClient(URL server) {
            return client;
        }
		
		private final HttpClient client;
		
    }

}
