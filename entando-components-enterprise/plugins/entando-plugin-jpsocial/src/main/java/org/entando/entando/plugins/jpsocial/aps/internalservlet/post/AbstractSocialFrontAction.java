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
package org.entando.entando.plugins.jpsocial.aps.internalservlet.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.IFacebookManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.ISocialPostManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.socialpost.SocialPost;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.apsadmin.system.BaseAction;

/**
 *
 * @author entando
 */
public class AbstractSocialFrontAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractSocialFrontAction.class);

	protected void purgeSessionCredentials() {
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER);
		this.getRequest().getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_FACEBOOK);
	}

	/**
	 * Load all the credentials in session
	 */
	protected void loadSocialSessionCredentials() throws ApsSystemException {
		try {
			this._credentialsTwitter = JpSocialSystemUtils.getTwitterCredentialsFromCookie(this.getRequest());
			this._credentialsFacebook = JpSocialSystemUtils.getFacebookCredentialsFromCookie(this.getRequest());
		} catch (ApsSystemException ex) {
			_logger.error("error in  loadSocialSessionCredentials", ex);
			throw new ApsSystemException("failed to load credentials");
		}
	}

	/**
	 * Generate the link to the given page
	 *
	 * @param pageCode
	 * @return
	 * @throws Throwable
	 */
	private String getPageUrl(IPage page) throws ApsSystemException {
		String url = "";

		try {
			if (null != page) {
				url = this.getUrlManager().createURL(page, this.getCurrentLang(), null);
			}
		} catch (Throwable t) {
			_logger.error("failed to obtain the link to a page", t);
			throw new ApsSystemException("failed to obtain the link to a page");
		}
		return url;
	}

	private IPage getLoginPage(String showletTypeCode) throws ApsSystemException {
		IPage page = null;

		try {
			List<IPage> pages = this.getPageManager().getWidgetUtilizers(showletTypeCode);
			// we are only interested in ONE login page so we pick the first
			if (null != pages && !pages.isEmpty()) {
				return pages.get(0);
			}
		} catch (Throwable t) {
			_logger.error("failed to obtain the link to a page", t);
			throw new ApsSystemException("failed to obtain the link to a page");
		}
		return page;
	}

	/**
	 * Get the page where this internal servlet is deployed. Needed to form the
	 * redirect URL for the login action
	 *
	 * @return
	 */
	public String getCurrentPageURL() {
		String url = "";
		try {
			HttpServletRequest request = this.getRequest();
			RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			url = getPageUrl(page);
		} catch (ApsSystemException ex) {
			_logger.error("error in getCurrentPageURL", ex);
		}
		return url;
	}

	public boolean isLogged(String socialNetwork) throws ApsSystemException {
		// load the credential in session
		loadSocialSessionCredentials();
		// evaluate the requested social network
		if (null != socialNetwork && !"".equals(socialNetwork.trim())) {
			if (socialNetwork.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_TWITTER)) {
				return (null != this.getCredentialsTwitter());
			} else if (socialNetwork.equalsIgnoreCase(JpSocialSystemConstants.PROVIDER_FACEBOOK)) {
				return (null != this.getCredentialsFacebook());
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Return the URL of the page containing the twitter login showlet
	 *
	 * @return
	 */
//  public String getTwitterLoginPageURL() throws ApsSystemException {
//    String url = "";
//    try {
//      IPage page = getLoginPage(JpSocialSystemConstants.SHOWLET_LOGIN_TWITTER);
//      url = getPageUrl(page);
//    } catch (Throwable t) {
//      ApsSystemUtils.logThrowable(t, this, "getTwitterLoginPage");
//      throw new ApsSystemException("error getting the Twitter login page");
//    }
//    return url;
//  }
	/**
	 * Return the URL of the page containing the twitter login showlet
	 *
	 * @return
	 */
//  public String getFacebookLoginPageURL() throws ApsSystemException {
//    String url = "";
//    try {
//      IPage page = getLoginPage(JpSocialSystemConstants.SHOWLET_LOGIN_FACEBOOK);
//      url = getPageUrl(page);
//    } catch (Throwable t) {
//      ApsSystemUtils.logThrowable(t, this, "getFacebookLoginPage");
//      throw new ApsSystemException("error getting the Facebook login page");
//    }
//    return url;
//  }
	public String getTwitterLoginURL() {
		String url = "";
		try {
			String currentPageURL = this.getCurrentPageURL();
			url = this.getTwitterManager().getAuthenticationURL(currentPageURL);
		} catch (Throwable t) {
			_logger.error("error in getTwitterLoginURL", t);
		}
		return url;
	}

	public String getFacebookLoginURL() {
		String url = "";
		try {
			String currentPageURL = this.getCurrentPageURL();
			url = this.getFacebookManager().getAuthenticationURL(currentPageURL);
		} catch (Throwable t) {
			_logger.error("error in getFacebookLoginURL", t);
		}
		return url;
	}

	protected SocialPost createAndAddSocialPost(String socialId, String provider, String text, String user) throws ApsSystemException {
		return createAndAddSocialPost(socialId, null, null, null, provider, text, user);
	}

	protected SocialPost createAndAddSocialPost(String socialId, String objectId, String service, String permalink, String provider, String text, String user) throws ApsSystemException {
		SocialPost post;
		post = new SocialPost(text, permalink, service, objectId, provider, socialId, user);
		this.getSocialPostManager().addSocialPost(post);
		return post;
	}

	protected boolean isSocialUser() {
		return hasCurrentUserPermission(JpSocialSystemConstants.PERMISSION_POST);
	}

	public IFacebookManager getFacebookManager() {
		return _facebookManager;
	}

	public void setFacebookManager(IFacebookManager facebookManager) {
		this._facebookManager = facebookManager;
	}

	public ITwitterManager getTwitterManager() {
		return _twitterManager;
	}

	public void setTwitterManager(ITwitterManager twitterManager) {
		this._twitterManager = twitterManager;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public FacebookCredentials getCredentialsFacebook() {
		return _credentialsFacebook;
	}

	protected void setCredentialsFacebook(FacebookCredentials credentialsFacebook) {
		this._credentialsFacebook = credentialsFacebook;
	}

	public TwitterCredentials getCredentialsTwitter() {
		return _credentialsTwitter;
	}

	protected void setCredentialsTwitter(TwitterCredentials credentialsTwitter) {
		this._credentialsTwitter = credentialsTwitter;
	}

	public IURLManager getUrlManager() {
		return _urlManager;
	}

	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}

	public ISocialPostManager getSocialPostManager() {
		return _socialPostManager;
	}

	public void setSocialPostManager(ISocialPostManager socialPostManager) {
		this._socialPostManager = socialPostManager;
	}
	private TwitterCredentials _credentialsTwitter;
	private FacebookCredentials _credentialsFacebook;
	private ITwitterManager _twitterManager;
	private IFacebookManager _facebookManager;
	private IPageManager _pageManager;
	private IURLManager _urlManager;
	private ISocialPostManager _socialPostManager;
}
