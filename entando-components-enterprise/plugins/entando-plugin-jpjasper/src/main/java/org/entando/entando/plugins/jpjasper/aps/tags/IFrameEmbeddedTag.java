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
package org.entando.entando.plugins.jpjasper.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.embedded.AbstractEmbeddedJasperConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class IFrameEmbeddedTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IFrameEmbeddedTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		HttpSession session = this.pageContext.getSession();
		try {
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			Widget showlet = this.extractShowlet(reqCtx);
			ApsProperties showletConfig = showlet.getConfig();
			String height = this.extractShowletParameter(AbstractEmbeddedJasperConfigAction.SHOWLET_PARAM_FRAME_HEIGHT, showletConfig, reqCtx);
			String baseUrl = this.buildIframeEndpoint(reqCtx, currentUser, showlet);
			if (this.getVar() != null) {
				this.pageContext.setAttribute(this.getVar(), baseUrl);
			} else {
				this.pageContext.getOut().print(baseUrl);
			}
			this.pageContext.setAttribute(this.getHeightVarName(), height);
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Error detected into endTag", t);
		}
		return super.doEndTag();
	}


	public String buildIframeEndpoint(RequestContext reqCtx, UserDetails currentUser, Widget showlet) {
		String baseUrl = null;
		try {
			IJasperServerManager client = (IJasperServerManager) ApsWebApplicationUtils.getBean(JpJasperSystemConstants.JASPER_CLIENT_MANAGER, this.pageContext);
			ApsProperties showletConfig = showlet.getConfig();
			baseUrl = client.getJasperBaseUrl();
			String endpoint = this.extractShowletParameter(AbstractEmbeddedJasperConfigAction.SHOWLET_PARAM_ENDPOINT, showletConfig, reqCtx);
			String theme = this.extractShowletParameter(AbstractEmbeddedJasperConfigAction.SHOWLET_PARAM_THEME, showletConfig, reqCtx);
			baseUrl = baseUrl + endpoint + "&" + JpJasperSystemConstants.JASPER_PARAM_USERNAME + "=" + this.getJasperUsername(currentUser) + "&" + JpJasperSystemConstants.JASPER_PARAM_PASSWORD + "=" + currentUser.getPassword();
			if (null != theme) {
				baseUrl = baseUrl + "&theme=" + theme;
			}
		} catch (Throwable t) {
			_logger.error("Error in buildIframeEndpoint", t);
			throw new RuntimeException("Error in buildIframeEndpoint", t);
		}
		return baseUrl;
	}

	/**
	 * when jasper has organizations, the username is username|organizationid
	 * @param currentUser
	 * @return
	 */
	private String getJasperUsername(UserDetails currentUser) {
		String username = currentUser.getUsername();
		try {
			String organization = this.getOrganizationValue(currentUser);
			if (StringUtils.isNotBlank(organization)) {
				username = username + "|" + organization;
			}
		} catch (Throwable t) {
			_logger.error("Error in getJasperUsername", t);
			throw new RuntimeException("Error in getJasperUsername", t);
		}
		return username;
	}

	/**
	 * Extract the organization value given a {@link UserDetails}<br />
	 * @param userDetails the user to extract the organization from
	 * @return the organization if exists or null
	 * @throws ApsSystemException
	 */
	protected String getOrganizationValue(UserDetails userDetails) throws ApsSystemException {
		String organization = null;
		IUserProfileManager userProfileManager = (IUserProfileManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_PROFILE_MANAGER, this.pageContext);
		IUserProfile userProfile = userProfileManager.getProfile(userDetails.getUsername());
		if (null != userProfile) {
			AttributeInterface attr = userProfile.getAttributeByRole(JpJasperSystemConstants.ROLE_ATTRIBUTE_ORGANIZATION);
			if (null != attr && attr instanceof AbstractTextAttribute)
			organization = ((AbstractTextAttribute)attr).getText();
		}
		return organization;
	}

	protected Widget extractShowlet(RequestContext reqCtx) {
		return (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
	}

	protected String extractShowletParameter(String parameterName, ApsProperties showletConfig, RequestContext reqCtx) {
		String retval = null;
		if (null != showletConfig && !showletConfig.isEmpty()) {
			retval = (String) showletConfig.get(parameterName);
		}
		return retval;
	}


	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setHeightVarName(null);
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public String getHeightVarName() {
		return _heightVarName;
	}
	public void setHeightVarName(String heightVarName) {
		this._heightVarName = heightVarName;
	}

	private String _var;
	private String _heightVarName;
}
