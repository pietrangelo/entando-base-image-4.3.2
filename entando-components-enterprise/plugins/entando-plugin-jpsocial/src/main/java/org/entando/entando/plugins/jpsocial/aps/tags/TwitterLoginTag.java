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
package org.entando.entando.plugins.jpsocial.aps.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * 
https://twitter.com/#!/mrwilsonyaksha
 
http://lordedgar.zapto.org:8080/myartifact/

Access level 	Read-only
About the application permission model
Consumer key 	a0Eu6YzdMfvwQa7tbwzK7A
Consumer secret 	zivhha18Sbo4de5Fbjky5d4f8SBU25id9BdB5hhg5I
Request token URL 	https://api.twitter.com/oauth/request_token
Authorize URL 	https://api.twitter.com/oauth/authorize
Access token URL 	https://api.twitter.com/oauth/access_token
Callback URL 	http://lordedgar.zapto.org:8080/myartifact/
  
callback?
		oauth_token=myc4aNkj7I9jlNhgHWnPkzUieZw2Zhli9mjM4Y0Cqww
		&
		oauth_verifier=3olvEslYQSn8zkbo1YYVmPn3CZhLhGRWyvEEdbQOM
  
 * */
public class TwitterLoginTag extends AbstractSocialLoginTag {

	private static final Logger _logger =  LoggerFactory.getLogger(TwitterLoginTag.class);

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		try {
			ConfigInterface configInterface = (ConfigInterface) ApsWebApplicationUtils.getBean("BaseConfigManager", pageContext);
			String devMode = configInterface.getParam(JpSocialSystemConstants.DEV_MODE_PARAM_NAME);
			String loginrequest = request.getParameter("loginrequest");
			if ("true".equalsIgnoreCase(devMode)) {
				_logger.debug(" DEV MODE :: {} dev authn url {}",  devMode, this.getDevLoginUrl());
				this.pageContext.setAttribute("twitter_autentication_url", this.getDevLoginUrl());
				if (this.getDomain().equals(loginrequest)) {
					UserDetails userDetails = this.getFakeUser(); 
					request.getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, userDetails);
				}
			} else {
				_logger.debug(" MineTwitterConsumer Tag - dev authn url {}", this.getDevLoginUrl());
				String baseUrl = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
				this.pageContext.setAttribute("twitter_autentication_url", this.getLoginUrl(baseUrl));
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected", t);
		}
		return EVAL_PAGE;
	}
	
	protected String getLoginUrl(String appBaseUrl) {
		StringBuilder logUrl = new StringBuilder(appBaseUrl);
		logUrl.append("Twitter?");
		logUrl.append(JpSocialSystemConstants.TW_REDIRECT_URL);
		logUrl.append("=");
		logUrl.append(this.getCurrentUrl());
		return logUrl.toString();
	}
	
	@Override
	protected UserDetails getFakeUser() {
		return new TwitterUser(322+this.getDomain(), "mr nice");
	}
	
	@Override
	protected String getDomain() {
		return "@twitter.com";
	}
	
}
