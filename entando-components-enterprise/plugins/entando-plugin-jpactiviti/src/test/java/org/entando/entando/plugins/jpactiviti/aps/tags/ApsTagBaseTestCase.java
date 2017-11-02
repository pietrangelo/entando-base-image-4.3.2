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
package org.entando.entando.plugins.jpactiviti.aps.tags;

import com.agiletec.ConfigTestUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.struts2.spring.StrutsSpringObjectFactory;
import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import java.util.Arrays;
import java.util.Collection;
import junit.framework.TestCase;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.dispatcher.Parameter;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Zarar Siddiqi - E.Santoboni - S.Loru
 */

public class ApsTagBaseTestCase extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		boolean refresh = false;
		if (null == _applicationContext) {
			// Link the servlet context and the Spring context
			_servletContext = new MockServletContext("", new FileSystemResourceLoader());
			_applicationContext = this.getConfigUtils().createApplicationContext(_servletContext);
			_servletContext.setAttribute(
					WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, _applicationContext);
		} else {
			refresh = true;
		}
		this._request = new MockHttpServletRequest();
		this._response = new MockHttpServletResponse();
		this._request.setSession(new MockHttpSession(this._servletContext));
		if (refresh) {
			try {
				ApsWebApplicationUtils.executeSystemRefresh(this._request);
				this.waitNotifyingThread();
			} catch (Throwable e) {}
		}
		// Use spring as the object factory for Struts
		StrutsSpringObjectFactory ssf = new StrutsSpringObjectFactory(null, null, null, null, _servletContext, null, this.createContainer());
		ssf.setApplicationContext(_applicationContext);
		// Dispatcher is the guy that actually handles all requests.  Pass in
		// an empty Map as the parameters but if you want to change stuff like
		// what config files to read, you need to specify them here
		// (see Dispatcher's source code)
		java.net.URL url = ClassLoader.getSystemResource("struts.properties");
	    Properties props = new Properties();
		props.load(url.openStream());
		this.setInitParameters(props);
		Map params = new HashMap(props);
		this._dispatcher = new Dispatcher(_servletContext, params);
		this._dispatcher.init();
		Dispatcher.setInstance(this._dispatcher);
	}
	
	protected Container createContainer() {
        ContainerBuilder builder = new ContainerBuilder();
        builder.constant("devMode", "false");
        return builder.create(true);
    }
	
	@Override
	protected void tearDown() throws Exception {
		this.waitNotifyingThread();
		super.tearDown();
		this.getConfigUtils().closeDataSources(this.getApplicationContext());
		this.getConfigUtils().destroyContext(this.getApplicationContext());
	}
    
	protected void waitNotifyingThread() throws InterruptedException {
		Thread[] threads = new Thread[20];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
	    	if (currentThread != null && 
	    			currentThread.getName().startsWith(NotifyManager.NOTIFYING_THREAD_NAME)) {
	    		currentThread.join();
	    	}
	    }
	}
	
    protected void setInitParameters(Properties params) {
    	params.setProperty("config", 
    			"struts-default.xml,struts-plugin.xml,struts.xml,entando-struts-plugin.xml,japs-struts-plugin.xml");
    }
    
    /**
     * Return a user (with his autority) by username.
     * @param username The username
     * @param password The password
     * @return The required user.
     * @throws Exception In case of error.
     */
    protected UserDetails getUser(String username, String password) throws Exception {
		IAuthenticationProviderManager provider = (IAuthenticationProviderManager) this.getService(SystemConstants.AUTHENTICATION_PROVIDER_MANAGER);
		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		UserDetails user = null;
		if (username.equals(SystemConstants.GUEST_USER_NAME)) {
			user = userManager.getGuestUser();
		} else {
			user = provider.getUser(username, password);
		}
		return user;
    }
    
    /**
     * Return a user (with his autority) by username, with the password equals than username.
     * @param username The username
     * @return The required user.
     * @throws Exception In case of error.
     */
    protected UserDetails getUser(String username) throws Exception {
		return this.getUser(username, username);
    }
    
	protected void setUserOnSession(String username) throws Exception {
		if (null == username) {
			this.removeUserOnSession();
			return;
		}
		UserDetails currentUser = this.getUser(username, username);//nel database di test, username e password sono uguali
		HttpSession session = this._request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, currentUser);
    }
	
	protected void removeUserOnSession() throws Exception {
    	HttpSession session = this._request.getSession();
		session.removeAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
    }
	
	protected void addParameters(Map params) {
		Iterator iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			this.addParameter(key, params.get(key).toString());
		}
	}
	
	protected void addParameter(String name, String[] values) {
		if (null != values) {
			this.addParameter(name, Arrays.asList(values));
		}
	}
	
	protected void addParameter(String name, Collection<String> value) {
		this._request.removeParameter(name);
		if (null == value) {
			return;
		}
		String[] array = new String[value.size()];
		Iterator<String> iter = value.iterator();
		int i = 0;
		while (iter.hasNext()) {
			String stringValue = iter.next();
			this._request.addParameter(name, stringValue);
			array[i++] = stringValue;
		}
		Parameter.Request parameter = new Parameter.Request(name, array);
		this._parameters.put(name, parameter);
	}
	
	protected void addParameter(String name, Object value) {
		this._request.removeParameter(name);
		if (null == value) return;
		this._request.addParameter(name, value.toString());
		Parameter.Request parameter = new Parameter.Request(name, value.toString());
		this._parameters.put(name, parameter);
	}
	
	protected void addAttribute(String name, Object value) {
		this._request.removeAttribute(name);
		if (null == value) return;
		this._request.setAttribute(name, value);
	}
	/*
	private void removeParameter(String name) {
		this._request.removeParameter(name);
		this._request.removeAttribute(name);
		this._parameters.remove(name);
	}
	*/
	protected String executeAction() throws Throwable {
		ActionContext ac = this._proxy.getInvocation().getInvocationContext();
		ac.setParameters(HttpParameters.create(this._request.getParameterMap()).build());
		ac.getParameters().appendAll(this._parameters);
		String result = this._proxy.execute();
		return result;
	}
	
	protected IManager getService(String name) {
		return (IManager) this.getApplicationContext().getBean(name);
	}
	
    protected void setApplicationContext(ApplicationContext applicationContext) {
    	this._applicationContext = applicationContext;
    }
    
    protected ApplicationContext getApplicationContext() {
    	return this._applicationContext;
    }
	
    
    protected ConfigTestUtils getConfigUtils() {
		return new ConfigTestUtils();
	}
    
    protected ActionSupport getAction() {
    	return this._action;
    }
    
    protected HttpServletRequest getRequest() {
    	return this._request;
    }

	public MockPageContext getMockPageContext() {
		return _mockPageContext;
	}

	public void setMockPageContext(MockPageContext mockPageContext) {
		this._mockPageContext = mockPageContext;
	}
	
	public MockServletContext getServletContext() {
		return _servletContext;
	}

	public void setServletContext(MockServletContext _servletContext) {
		this._servletContext = _servletContext;
	}
	
    private ApplicationContext _applicationContext;
    private Dispatcher _dispatcher;
	private ActionProxy _proxy;
	private MockServletContext _servletContext;
	private MockHttpServletRequest _request;
	private MockHttpServletResponse _response;
	private ActionSupport _action;
	private MockPageContext _mockPageContext;
	
	private Map<String, Parameter> _parameters = new HashMap<String, Parameter>();
	
}
