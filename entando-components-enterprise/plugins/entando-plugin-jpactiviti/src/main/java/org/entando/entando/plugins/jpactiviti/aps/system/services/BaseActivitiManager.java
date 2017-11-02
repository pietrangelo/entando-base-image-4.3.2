package org.entando.entando.plugins.jpactiviti.aps.system.services;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
//import org.restlet.data.ChallengeScheme;
//import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseActivitiManager extends AbstractService {
	private static final Logger _logger = LoggerFactory
			.getLogger(BaseActivitiManager.class);
	protected ObjectMapper objectMapper = new ObjectMapper();
	
	
	public CloseableHttpClient getClient(String username, String password) {
        System.out.println("getClient called");
		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(AuthScope.ANY, credentials);
		CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
		return client;
	}
	
	
	
	
	/**
	 * IMPORTANT: calling method is responsible for calling close() on returned
	 * {@link HttpResponse} to free the connection.
	 */
	public CloseableHttpResponse executeRequest(CloseableHttpClient client, HttpUriRequest request) {
		return internalExecuteRequest(client,request, true);
	}

	/**
	 * IMPORTANT: calling method is responsible for calling close() on returned
	 * {@link HttpResponse} to free the connection.
	 */
	public CloseableHttpResponse executeBinaryRequest(CloseableHttpClient client, HttpUriRequest request) {
		return internalExecuteRequest(client, request, false);
	}

	protected CloseableHttpResponse internalExecuteRequest(CloseableHttpClient client, HttpUriRequest request, boolean addJsonContentType) {
		CloseableHttpResponse response = null;
		try {
			if (addJsonContentType
					&& request.getFirstHeader(HttpHeaders.CONTENT_TYPE) == null) {
				// Revert to default content-type
				request.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE,
						"application/json"));
			}
			response = client.execute(request);
			//Assert.assertNotNull(response.getStatusLine());
			//Assert.assertEquals(expectedStatusCode, response.getStatusLine().getStatusCode());
			
			return response;

		} catch (ClientProtocolException e) {
			//Assert.fail(e.getMessage());
		} catch (IOException e) {
			//Assert.fail(e.getMessage());
		}
		return null;
	}

	public void closeResponse(CloseableHttpResponse response) {
		if (response != null) {
			try {
				response.close();
			} catch (IOException e) {
				//fail("Could not close http connection");
			}
		}
	}
	
	@Override
	public void init() throws Exception {
		_logger.info("{} ready", this.getClass().getName());
		this.setObjectMapper(new ObjectMapper());
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

}
