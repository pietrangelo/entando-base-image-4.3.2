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
package org.entando.entando.plugins.jpjasper.aps.services.report.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.JasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptorDOM;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.aps.services.report.api.model.JAXBBaseResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.report.api.model.JAXBResourceDescriptor;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

public class ApiReportInterface {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiReportInterface.class);
	
    public List<JAXBBaseResourceDescriptor> getReports(Properties properties) throws ApiException, Throwable {
    	List<JAXBBaseResourceDescriptor> reports = null;
        String uriString = properties.getProperty("uriString");
        String filter = properties.getProperty("filter");
       
        //FIXME
//        System.out.println("FIXME");
//        properties.put(SystemConstants.API_USER_PARAMETER, this.getUserManager().getUser("jasperadmin"));
        
        UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
        try {
        	if (null == user) {
        		user = this.getUserManager().getGuestUser();
        	}
        	String cookie = this.getJasperServerManager().getCookieHeader(user);
        	if (StringUtils.isBlank(cookie)) {
        		throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User " + user.getUsername() + " is not allowed to report '" + uriString + "'", Response.Status.FORBIDDEN);
        	}
        	
        	List<JasperResourceDescriptor> list = this.getJasperServerManager().searchRestResources(cookie, filter, WsTypeParams.REPORT_UNIT, uriString, true);
        	if (null != list) {
        		reports = new ArrayList<JAXBBaseResourceDescriptor>();
        		Iterator<JasperResourceDescriptor> it = list.iterator();
        		while (it.hasNext()) {
        			JasperResourceDescriptor resourceDescriptor = it.next();
        			reports.add(new JAXBBaseResourceDescriptor(resourceDescriptor));
        		}
        	}
        } catch (ApiException ae) {
        	_logger.error("Error into getReports API method", ae);
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error into getReports API method", t);
            throw new ApsSystemException("Error into getReports API method", t);
        }
        return reports;
    }
	
    public JAXBResourceDescriptor getReport(Properties properties) throws ApiException, Throwable {
        JAXBResourceDescriptor jaxbResourceDescriptor = null;
        String uriString = properties.getProperty("uriString");
        String params = properties.getProperty("inputControls");

//        properties.put(SystemConstants.API_USER_PARAMETER, this.getUserManager().getUser("jasperadmin"));
        
        UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
        try {
        	if (null == user) {
        		user = this.getUserManager().getGuestUser();
        	}
        	String cookie = this.getJasperServerManager().getCookieHeader(user);
        	if (StringUtils.isBlank(cookie)) {
        		throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User " + user.getUsername() + " is not allowed to report '" + uriString + "'", Response.Status.FORBIDDEN);
        	}
        	//FIXME
        	String resourceXMLResponse = ((JasperServerManager)this.getJasperServerManager()).getRestResourceXML(cookie, uriString, params);
        	JasperResourceDescriptorDOM parser = new JasperResourceDescriptorDOM();
			JasperResourceDescriptor resourceDescriptor = parser.parseResource(resourceXMLResponse);
			jaxbResourceDescriptor = new JAXBResourceDescriptor(resourceDescriptor);

        } catch (ApiException ae) {
        	_logger.error("Error into getReport API method", ae);
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error into getReport API method", t);
            throw new ApsSystemException("Error into getReport API method", t);
        }
        return jaxbResourceDescriptor;
    }
 
    public String getReportToHtml(Properties properties) throws ApiException, Throwable {
        String html = null;
        String uriString = properties.getProperty("uriString");
        String params = properties.getProperty("inputControls");
        UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
        String format = "html";
        try {
        	if (null == user) {
        		user = this.getUserManager().getGuestUser();
        	}
        	String cookie = this.getJasperServerManager().getCookieHeader(user);
        	if (StringUtils.isBlank(cookie)) {
        		throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User " + user.getUsername() + " is not allowed to report '" + uriString + "'", Response.Status.FORBIDDEN);
        	}
			String imageUri = JpJasperApsadminUtils.getReportImageUriV2(this.getConfigManager());
			//get report html and parse image src to apply entando image uri
			InputStream is = this.getJasperServerManager().runReportRestV2(cookie, uriString, format, params);
			html = IOUtils.toString(is, "UTF-8");
			
			String baseUrl = this.getJasperServerManager().getJasperBaseUrl();
			html = JpJasperApsadminUtils.updateJSUri(html, baseUrl);
			
			html = JpJasperApsadminUtils.updateImageUri(html, imageUri);
        } catch (ApiException ae) {
        	_logger.error("Error into getReportToHtml API method", ae);
            throw ae;
        } catch (Throwable t) {
        	_logger.error("Error into getReportToHtml API method", t);
            throw new ApsSystemException("Error into getReportToHtml API method", t);
        }
        return html;
    }
	
	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}

	private IJasperServerManager _jasperServerManager;
	private ConfigInterface _configManager;
	private IUserManager _userManager;
}
