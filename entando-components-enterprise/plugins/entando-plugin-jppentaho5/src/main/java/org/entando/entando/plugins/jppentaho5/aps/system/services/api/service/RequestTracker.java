package org.entando.entando.plugins.jppentaho5.aps.system.services.api.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.entando.entando.plugins.jprestapi.aps.core.IRestInvocation;

public class RequestTracker implements IRestInvocation {

	@Override
	public HttpResponse getResponse() {
		// TODO Auto-generated method stub
		return _response;
	}

	@Override
	public HttpRequestBase getRequest() {
		// TODO Auto-generated method stub
		return _request;
	}

	@Override
	public void setResponse(HttpResponse response) {
		// TODO Auto-generated method stub
		_response = response;
	}

	@Override
	public void setRequest(HttpRequestBase request) {
		// TODO Auto-generated method stub
		_request = request;
	}
	
	private HttpResponse _response;
	private HttpRequestBase _request;

}
