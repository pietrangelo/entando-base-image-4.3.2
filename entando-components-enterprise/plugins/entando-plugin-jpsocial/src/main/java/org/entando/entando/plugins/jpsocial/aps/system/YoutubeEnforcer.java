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
package org.entando.entando.plugins.jpsocial.aps.system;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.YoutubeCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.google.gdata.client.http.AuthSubUtil;

/**
 * @author entando
 */
public class YoutubeEnforcer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(YoutubeEnforcer.class);
	  
  private YoutubeCredentials createYoutubeCedentials(HttpServletRequest request, String accessToken) {
    YoutubeCredentials credentials = null;

    // remove the Twitter credentials from the session in any case
    request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_YOUTUBE);
    if (null != request && null != accessToken) {
      credentials = new YoutubeCredentials(accessToken);

      try {
        // get session token
        String sessionToken = AuthSubUtil.exchangeForSessionToken(accessToken, null);
        // update session token in credentials
        credentials.setSessionToken(sessionToken);
      } catch (Throwable t) {
    	  _logger.error("error obtaining session token", t);
      }
      // yeah
      request.getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_YOUTUBE, credentials);
    }
    return credentials;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	  getAndRemoveNoLoginAction(request);
    ConfigInterface configInterface =
            (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request);
    String baseURL = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
    String onetimeUseToken = null;
    YoutubeCredentials youtubeCredentials = null;

    // ??? BUG of the google API?
    if (null != request && null != request.getQueryString()) {
      onetimeUseToken = AuthSubUtil.getTokenFromReply(request.getQueryString());
    } else {
      onetimeUseToken = request.getParameter("token");
    }
    String pathInfo = request.getPathInfo();
    UserDetails currentUser = getCurrentUser(request);

    if (isSocialUser(request)) {
      youtubeCredentials = createYoutubeCedentials(request, onetimeUseToken);
      _logger.debug("user {} can now upload video on Youtube", currentUser.getUsername());
    } else if (isPortalUrl(pathInfo)) {
      // only portal access 
      youtubeCredentials = createYoutubeCedentials(request, onetimeUseToken);
      if (null != currentUser) {
        _logger.debug(" ANONYMOUS user can now upload Video");
      }
    } else {
      // sorry not allowed
      _logger.debug("user {} cannot access Youtube functionality", currentUser.getUsername());
    }
    // redirect
    if (pathInfo.startsWith("/")) {
      response.sendRedirect(baseURL.concat(pathInfo.substring(1)));
    } else {
      response.sendRedirect(baseURL.concat(pathInfo));
    }
  }
}
