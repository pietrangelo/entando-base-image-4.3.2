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

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants.PROVIDER;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.AbstractSocialUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.ISocialUser;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.AbstractCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author S.Loru
 */
public class SocialLoggedTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialLoggedTag.class);

	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		AbstractCredentials credentials = null;
		boolean socialLogged = false;
		try {
			if (null != this.getProvider()) {
				if (currentUser instanceof AbstractSocialUser && ((ISocialUser) currentUser).getProvider().equals(this.getProvider())) {
					socialLogged = true;
				} else {
					switch (PROVIDER.valueOf(this.getProvider())) {
						case facebook:
							credentials = JpSocialSystemUtils.getFacebookCredentialsFromCookie(request);
							break;
						case twitter:
							credentials = JpSocialSystemUtils.getTwitterCredentialsFromCookie(request);
							break;
					}
					if (null != credentials) {
						socialLogged = true;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error while getting credentials", t);
		}

		if (null != getVar() && !"".equals(getVar().trim())) {
			this.pageContext.setAttribute(getVar(), socialLogged);
		} else {
			this.pageContext.setAttribute(VAR_LOGGED, socialLogged);
		}

		return EVAL_PAGE;
	}

	public String getProvider() {
		return _provider;
	}

	public void setProvider(String provider) {
		this._provider = provider;
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}
	private String _var;
	private String _provider;
	private static final String VAR_LOGGED = "isLogged";
}
