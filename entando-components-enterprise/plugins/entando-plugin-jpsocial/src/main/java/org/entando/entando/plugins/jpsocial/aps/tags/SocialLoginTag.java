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
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.FacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.TwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.FacebookUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author entando
 */
public class SocialLoginTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialLoginTag.class);

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		FacebookManager facebookManager =
				(FacebookManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.FACEBOOK_CLIENT_MANAGER, pageContext);
		TwitterManager twitterManager =
				(TwitterManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CLIENT_MANAGER, pageContext);
		boolean isAuth = false;
		String authUrl = null;
		long limit = DEFAULT_MAX_UPLOAD_SIZE;

		// evaluate tag params
		try {
			if (null != getMaxSize()) {
				limit = Long.valueOf(getMaxSize());
			}
		} catch (NumberFormatException e) {
			_logger.error("invalid max upload size limit, defaulting to {}", limit, e);
		}
		if (!this.isLogInSystem()) {
			if(!getCurrentPage().contains("?")){
				setCurrentPage(getCurrentPage().concat("?"));
			} else {
				setCurrentPage(getCurrentPage().concat("&"));
			}
			setCurrentPage(getCurrentPage().concat("login=false"));
		}
		try {
			if (getProvider().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				authUrl = facebookManager.getAuthenticationURL(getCurrentPage());
				isAuth = (request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) instanceof FacebookUser);
			} else if (getProvider().equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				authUrl = twitterManager.getAuthenticationURL(getCurrentPage());
				isAuth = (request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) instanceof TwitterUser);
			}
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error detected", t);
		}
		if (authUrl != null) {
			authUrl = authUrl.replaceAll(";jsessionid=[a-zA-Z0-9]{0,33}", "");
		}
		if (null == getLoginUrlVar()) {
			this.pageContext.setAttribute(VAR_LOGIN, authUrl);
		} else {
			this.pageContext.setAttribute(getLoginUrlVar(), authUrl);
		}
		if (null == getAuthVar()) {
			this.pageContext.setAttribute(JpSocialSystemConstants.VAR_AUTH, isAuth);
		} else {
			this.pageContext.setAttribute(getAuthVar(), isAuth);
		}

		return EVAL_PAGE;
	}

	public String getProvider() {
		return _provider;
	}

	public void setProvider(String provider) {
		this._provider = provider;
	}

	public String getCurrentPage() {
		return _currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this._currentPage = currentPage;
	}

	public String getAuthVar() {
		return _authVar;
	}

	public void setAuthVar(String authVar) {
		this._authVar = authVar;
	}

	public String getLoginUrlVar() {
		return _loginUrlVar;
	}

	public void setLoginUrlVar(String loginUrlVar) {
		this._loginUrlVar = loginUrlVar;
	}

	public String getSharerd() {
		return _sharerd;
	}

	public void setSharerd(String sharerd) {
		this._sharerd = sharerd;
	}

	public String getMaxSize() {
		return _maxSize;
	}

	public void setMaxSize(String maxSize) {
		this._maxSize = maxSize;
	}

	public String getErrMsgVar() {
		return _errMsgVar;
	}

	public void setErrMsgVar(String errMsgVar) {
		this._errMsgVar = errMsgVar;
	}

	public boolean isLogInSystem() {
		return _logInSystem;
	}

	public void setLogInSystem(boolean logInSystem) {
		this._logInSystem = logInSystem;
	}

	protected String getDomain() {
		return "@facebook.com";
	}
	private String _provider;
	private String _currentPage;
	private String _loginUrlVar;
	private String _authVar;
	private String _sharerd;
	private String _maxSize;
	private String _errMsgVar;
	private boolean _logInSystem;
	public final static String VAR_LOGIN = "loginUrl";
	public final static String VAR_SENT = "shared";
	public final static String FIELD_POST_TEXT = "postText";
	public final static String FIELD_POST_IMAGE = "postImage";
	public final static long DEFAULT_MAX_UPLOAD_SIZE = 1048576;
}
