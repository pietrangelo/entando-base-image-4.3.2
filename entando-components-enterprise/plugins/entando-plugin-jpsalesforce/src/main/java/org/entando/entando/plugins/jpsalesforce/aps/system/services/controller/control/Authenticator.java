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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.controller.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.IPortalSubscriptionManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.config.SubscriptionConfig;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class Authenticator extends com.agiletec.aps.system.services.controller.control.Authenticator {

	@Override
	public int service(RequestContext reqCtx, int status) {
		HttpServletRequest req = reqCtx.getRequest();
		HttpSession session = req.getSession();
		UserDetails userBefore = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String usernameBefore = (null != userBefore) ? userBefore.getUsername() : SystemConstants.GUEST_USER_NAME;
		int retStatus = super.service(reqCtx, status);
		
		if (retStatus != ControllerManager.CONTINUE) {
			return retStatus;
		}
		UserDetails userAfter = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		String usernameAfter = (null != userAfter) ? userAfter.getUsername() : SystemConstants.GUEST_USER_NAME;
		try {
			if (!usernameAfter.equals(usernameBefore) && !usernameAfter.equals(SystemConstants.GUEST_USER_NAME)) {
				IUserProfile profile = (IUserProfile) userAfter.getProfile();
				String profileTypeCode = (null != profile) ? profile.getTypeCode() : "marker";
				SubscriptionConfig config = this.getPortalSubscriptionManager().getSubscriptionConfig();
				String expectedType = (null != config.getUserprofileTypeCode()) ? config.getUserprofileTypeCode() : "expected";
				if (!expectedType.equals(profileTypeCode)) {
					return retStatus;
				}
				String redirectPageCode = config.getRedirectPageCode();
				if (null != redirectPageCode && null != this.getPageManager().getPage(redirectPageCode)) {
					return super.redirect(redirectPageCode, reqCtx);
				}
			}
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "service", "Error, could not fulfill the request");
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return retStatus;
	}

	protected IPortalSubscriptionManager getPortalSubscriptionManager() {
		return _portalSubscriptionManager;
	}
	public void setPortalSubscriptionManager(IPortalSubscriptionManager portalSubscriptionManager) {
		this._portalSubscriptionManager = portalSubscriptionManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private IPortalSubscriptionManager _portalSubscriptionManager;
	private IPageManager _pageManager;

}
