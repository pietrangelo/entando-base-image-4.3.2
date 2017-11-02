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
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.ParameterStyle;

import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthClient;
import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthResponseMessage;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpMessage;
import org.entando.entando.plugins.jpsocial.aps.oauth.http.HttpResponseMessage;
import org.entando.entando.plugins.jpsocial.aps.system.httpclient.TwitterHttpClient;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterCookieConsumer;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterManager;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * This adds in the session the credentials, without affecting the current
 * logged user; this is true only if the currently logged user has the social
 * permissions
 *
 * @author entando
 */
public class TwitterEnforcer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(TwitterEnforcer.class);
  
  
  /**
   * Create the credential object and put in in the session
   * @param request
   * @param accessToken
   * @param tokenSecret
   * @return 
   */
  private TwitterCredentials createTwitterCredentials(HttpServletRequest request, HttpServletResponse response, String accessToken, String tokenSecret) throws ApsSystemException {
    TwitterCredentials credentials = null;
    
    if (null != request && null != accessToken && null != tokenSecret) {
		ITwitterManager twitterManager = (ITwitterManager) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CLIENT_MANAGER, request);
		
      credentials = (TwitterCredentials) twitterManager.createSocialCredentials(accessToken);
      
      // remove the Twitter credentials from the session in any case
      request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER);
      // yeah
//      request.getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_TWITTER, credentials);
	  JpSocialSystemUtils.saveTwitterCredentialsCookie(credentials, response, request);
    }
    return credentials;
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    OAuthConsumer consumer = null;
    String redirectUrl = request.getParameter(JpSocialSystemConstants.TW_REDIRECT_URL);
    ITwitterCookieConsumer twitterConsumerManager =
            (ITwitterCookieConsumer) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CONSUMER_MANAGER, request);
    UserDetails currentUser = (UserDetails) getCurrentUser(request);
    try {
      consumer = twitterConsumerManager.getTwitterConsumer(request);
      OAuthAccessor accessor = twitterConsumerManager.getAccessor(request, response, consumer);
      OAuthClient client = new OAuthClient(new TwitterHttpClient());
      OAuthResponseMessage result = client.access(accessor.newRequestMessage(OAuthMessage.GET,
              "https://api.twitter.com/1.1/account/verify_credentials.json", null), ParameterStyle.AUTHORIZATION_HEADER);
      int status = result.getHttpResponse().getStatusCode();

      if (status != HttpResponseMessage.STATUS_OK) {
        OAuthProblemException problem = result.toOAuthProblemException();
        if (problem.getProblem() != null) {
          throw problem;
        }
        Map<String, Object> dump = problem.getParameters();
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(dump.get(HttpMessage.REQUEST));
        out.println("----------------------");
        out.println(dump.get(HttpMessage.RESPONSE));
      } else {
        // Simply pass the data through to the browser:
        String json = twitterConsumerManager.copyResponse(result);
        JSONObject jsonObject = new JSONObject(json);
        String id = jsonObject.getString("id");
        //String id_str = jsonObject.getString("id_str");				
        String name = jsonObject.getString("name");

        // set twitter credentials for the current user
        if (isSocialUser(request)) {
          createTwitterCredentials(request, response, accessor.accessToken, accessor.tokenSecret);

          _logger.debug("user {} can now tweet contents", currentUser.getUsername());
        } else if (isPortalUrl(redirectUrl)) {
          // if we are redirecting to a page of the portal then we are allowed to put on the session the twitter credential only
          createTwitterCredentials(request, response, accessor.accessToken, accessor.tokenSecret);
          _logger.debug(" ANONYMOUS user can now tweet FREE TEXT");
        } else {
          // we are very sorry! Neither the user nor the redirect url allow any action to be taken!

          if (null != currentUser) {
        	  _logger.debug("user {} cannot access Twitter functionality", currentUser.getUsername());
          } else {
        	  _logger.debug("Unknown users cannot access Twitter functionality");
          }
        }
        // TODO log the twitterUser here
      }
      //log.info(" MineTwitterConsumer REDIRECT URL " + redirectUrl);
      if (null != redirectUrl && redirectUrl.length() > 0) {
        //log.info(" MineTwitterConsumer REDIRECT");
        response.sendRedirect(redirectUrl);
        return;
      }
    } catch (Exception e) {
      twitterConsumerManager.handleException(e, request, response, consumer);
    }
  }

  private static final long serialVersionUID = 2L;
}
