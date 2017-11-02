package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model.PentahoRepositoryFileDto;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model.PentahoRepositoryFileDtoes;
import org.entando.entando.plugins.jppentaho5.aps.system.services.api.service.model.PentahoTransparentAuthTicket;
import org.entando.entando.plugins.jprestapi.aps.core.IRestInvocation;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;

/**
 * 
 * @author entando
 * 
 */


public class PentahoFileResourceService {

	public PentahoFileResourceService(PentahoClient client) {
		this._client = client;
	}


	/**
	 * 
	 * @return
	 * @throws Throwable
	 */
	public List<PentahoRepositoryFileDto> getFilesChildren(String pathId) throws Throwable {
	  	List<String> path = Arrays.asList(pathId, "children");	
		
	  	PentahoRepositoryFileDtoes filesResult = (PentahoRepositoryFileDtoes) new PentahoRequestBuilder(_client)
		.setEndpoint(PentahoEndpointsDictionary.endpoints[PentahoEndpointsDictionary.API_GET_FILES])
		.setDebug(_debug)
		.setUnmarshalOptions(false, true)
		.addPath(path)
		.setTestMode(_testMode)
		.doRequest(PentahoRepositoryFileDtoes.class); 
		return filesResult.getRepositoryFileDto();
	}

	public String getAuthTicket() throws Throwable {
	
		System.out.println("************************");
		System.out.println("****  getAuthTicket ****");
		System.out.println("****                ****");
	  	
	  	HashMap<String, String> requestParams = new HashMap<String, String>();

	  	requestParams.put("generate-ticket","1");
	  	requestParams.put("app","entando");
	 	requestParams.put("username","entandoUser");
	  	
	  	System.out.println("**** VERB " +PentahoEndpointsDictionary.endpoints[PentahoEndpointsDictionary.API_GET_AUTH_TICKET].getVerb().toString());		
	  	System.out.println("**** URL  " +PentahoEndpointsDictionary.endpoints[PentahoEndpointsDictionary.API_GET_AUTH_TICKET].getUrl().toString());
	 	System.out.println("**** PORT " +_client.getPort() );

	 	String username = getClient().getCredentials().getUsername();
	 	String password = getClient().getCredentials().getPassword();
	 	
	 	String plainCreds = username+":"+password;
	 	
	 	byte[] plainCredsBytes = plainCreds.getBytes();
	 	byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
	 	String base64Creds = new String(base64CredsBytes);

	 	Map<String, String> headersMap = new HashMap<String, String>();
	 	headersMap.put("Authorization", "Basic " + base64Creds);
	 		 
	 	RequestTracker intercept = new RequestTracker();
			 	
	 	PentahoTransparentAuthTicket resp  = (PentahoTransparentAuthTicket) new PentahoRequestBuilder(_client)
		.setEndpoint(PentahoEndpointsDictionary.endpoints[PentahoEndpointsDictionary.API_GET_AUTH_TICKET])
		.setDebug(_debug)		
		.setTestMode(_testMode)
		.setHeaders(headersMap)
		.trackRestInvocation(intercept)
		.setRequestParams(requestParams)
	//	.doRequest(); 
		.doRequest(PentahoTransparentAuthTicket.class); 

		System.out.println("TICKET ID : " + resp.getTicketId());
	
	
		
		Header[] headerList = intercept.getRequest().getAllHeaders();
		System.out.println("--- PAGE HEADERS ---");
		
		for (Header h : headerList) {
			System.out.println("name --> : " + h.getName() + " value -->:" + h.getValue());
		}

		
		System.out.println("************************");
		//return "";
		return resp.getTicketId();	
	}
	
	public PentahoClient getClient() {
		return _client;
	}

	public void setClient(PentahoClient client) {
		this._client = client;
	}

	public boolean isDebug() {
		return _debug;
	}

	public void setDebug(boolean debug) {
		this._debug = debug;
	}

	public boolean isTestMode() {
		return _testMode;
	}

	public void setTestMode(boolean testMode) {
		this._testMode = testMode;
	}

	private PentahoClient _client;
	private boolean _debug = false;
	private boolean _testMode = false;
	private IRestInvocation intercept;

}
