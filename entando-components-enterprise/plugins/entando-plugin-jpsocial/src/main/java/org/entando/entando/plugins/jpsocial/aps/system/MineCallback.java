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

import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.server.OAuthServlet;

import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthClient;

import org.entando.entando.plugins.jpsocial.aps.system.httpclient.TwitterHttpClient;
import org.entando.entando.plugins.jpsocial.aps.system.services.client.ITwitterCookieConsumer;

/**
 * An OAuth callback handler.
 *
 * @author John Kristian
 */
public class MineCallback extends HttpServlet {

    public static final String PATH = "/jpsocial_OAuth/Callback";

    protected final Logger log = LoggerFactory.getLogger(getClass().getName());

    /**
     * Exchange an OAuth request token for an access token, and store the latter
     * in cookies.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        OAuthConsumer consumer = null;
        log.info(" MineCallback Start");
        ITwitterCookieConsumer twitterConsumerManager =
				(ITwitterCookieConsumer) ApsWebApplicationUtils.getBean(JpSocialSystemConstants.TWITTER_CONSUMER_MANAGER, request);
		try {
			final OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            requestMessage.requireParameters("consumer");
            //final String consumerName = requestMessage.getParameter("consumer");
            consumer = twitterConsumerManager.getTwitterConsumer(request);
            final CookieMap cookies = new CookieMap(request, response);
            final OAuthAccessor accessor = twitterConsumerManager.newAccessor(consumer, cookies);
            final String expectedToken = accessor.requestToken;
            String requestToken = requestMessage.getParameter(OAuth.OAUTH_TOKEN);
            if (requestToken == null || requestToken.length() <= 0) {
                log.warn(request.getMethod() + " "
                        + OAuthServlet.getRequestURL(request));
                requestToken = expectedToken;
                if (requestToken == null) {
                    OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
                    problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
                    throw problem;
                }
            } else if (!requestToken.equals(expectedToken)) {
                OAuthProblemException problem = new OAuthProblemException("token_rejected");
                problem.setParameter("oauth_rejected_token", requestToken);
                problem.setParameter("oauth_expected_token", expectedToken);
                throw problem;
            }
            List<OAuth.Parameter> parameters = null;
            String verifier = requestMessage.getParameter(OAuth.OAUTH_VERIFIER);
            if (verifier != null) {
                parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, verifier);
            }
			OAuthClient client = new OAuthClient(new TwitterHttpClient());
            OAuthMessage result = client.getAccessToken(accessor, null, parameters);

//            log.info(" RESULT :: START ");
//
//            log.info(result.toString());
//
//            log.info(" RESULT :: STOP ");

            if (accessor.accessToken != null) {
                String returnTo = requestMessage.getParameter("returnTo");
                if (returnTo == null) {
                    returnTo = request.getContextPath(); // home page
                }

//        System.out.println("UTENTE CORRENTE");
//       	UserDetails currentUser = (UserDetails)((HttpServletRequest)request).getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
//        System.out.println(">>>>>"+currentUser.getUsername());

				String consumerName = ITwitterCookieConsumer.TWITTER_CONSUMER_NAME;
                cookies.remove(consumerName + ".requestToken");
                cookies.put(consumerName + ".accessToken", accessor.accessToken);
                cookies.put(consumerName + ".tokenSecret", accessor.tokenSecret);
                log.info(" MineCallBack REDIRECT TO " + returnTo);
                response.sendRedirect(returnTo);
            }
            OAuthProblemException problem = new OAuthProblemException(OAuth.Problems.PARAMETER_ABSENT);
            problem.setParameter(OAuth.Problems.OAUTH_PARAMETERS_ABSENT, OAuth.OAUTH_TOKEN);
            problem.getParameters().putAll(result.getDump());
            throw problem;
        } catch (Exception e) {
            twitterConsumerManager.handleException(e, request, response, consumer);
        }
    }

    private static final long serialVersionUID = 1L;

}
