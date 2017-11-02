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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

public class JasperUrlTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(JasperUrlTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		try {
			IJasperServerManager jasperServerManager = (IJasperServerManager) ApsWebApplicationUtils.getBean(JpJasperSystemConstants.JASPER_CLIENT_MANAGER, this.pageContext);
			String value = jasperServerManager.getJasperBaseUrl();
			if (!value.endsWith("/")) {
				value = value + "/";
			}

			if (null != this.getOrgIdVar()) {
				UserDetails currentUser = (UserDetails) pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
				this.pageContext.getRequest().setAttribute(this.getOrgIdVar(), this.getOrganizationValue(currentUser));
			}

			if (this.getVar() != null) {
				this.pageContext.setAttribute(this.getVar(), value);
			} else {
				this.pageContext.getOut().print(value);
			}
		} catch (Throwable t) {
			_logger.error("Error detected into endTag", t);
			throw new JspException("Error detected into endTag", t);
		}
		return super.doEndTag();
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
			if (null != attr && attr instanceof AbstractTextAttribute) {
				organization = ((AbstractTextAttribute) attr).getText();
			}
		}
		return organization;
	}

	@Override
	public void release() {
		super.release();
		this.setVar(null);
		this.setOrgIdVar(null);
	}

	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}

	public String getOrgIdVar() {
		return _orgIdVar;
	}
	public void setOrgIdVar(String orgIdVar) {
		this._orgIdVar = orgIdVar;
	}

	private String _var;
	private String _orgIdVar;
}
