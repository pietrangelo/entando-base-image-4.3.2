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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.GoogleCredentials;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.google.gdata.client.http.AuthSubUtil;

/**
 *
 * @author entando
 */
public class GoogleEnforcer extends AbstractEnforcerServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(GoogleEnforcer.class);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    ConfigInterface configInterface =
            (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request);
    String clientId = configInterface.getParam(JpSocialSystemConstants.GOOGLE_CONSUMER_KEY_PARAM_NAME);
    String clientSecret = configInterface.getParam(JpSocialSystemConstants.GOOGLE_CONSUMER_SECRET_PARAM_NAME);
    String baseURL = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
    String developerKey = configInterface.getParam(JpSocialSystemConstants.YOUTUBE_DEVELOPER_KEY);

    try {
      // retrieve the code parameter OR the error code!
      String code = ((HttpServletRequest) request).getParameter("code");
      String error = ((HttpServletRequest) request).getParameter("code");
      // retrieve the optional parameter that helps to identify the desired service (Google+ or Youtube)
      String state = ((HttpServletRequest) request).getParameter("state");
      if (null != code && !"".equals(code.trim())) {
        // we have a response, get access token
        AccessTokenInfo tokenInfo = negotiateAccessToken(code, clientId, clientSecret, baseURL, request);
        if (null != tokenInfo) {
          UserDetails currentUser = getCurrentUser(request);

          // put credentials in the session
          request.getSession().removeAttribute(JpSocialSystemConstants.SESSION_PARAM_GOOGLE);
          // check if the current user has social capabilities
          if (isSocialUser(request)) {
            GoogleCredentials googleCredentials = new GoogleCredentials(
                    tokenInfo.getAccessToken(),
                    tokenInfo.getRefreshToken(),
                    developerKey);
            request.getSession().setAttribute(JpSocialSystemConstants.SESSION_PARAM_GOOGLE, googleCredentials);

            _logger.info("user {} can now post contents on Google", currentUser.getUsername());

            response.sendRedirect(JpSocialSystemConstants.BACKEND_ACTION);

          } else {
            _logger.info("user {} cannot post contents on Google due to insufficient permissions", currentUser.getUsername());
          }
        } else {
          _logger.info("Could not acquire the access token");
        }
      }
    } catch (Throwable t) {
      _logger.error("Google Enforcer servlet failere detected");
      _logger.error("error in doGet", t);
    }
  }

  /**
   *
   * @param code
   * @param client_id
   * @param client_secret
   * @param app_baseURL
   * @return
   */
  private AccessTokenInfo negotiateAccessToken(String code, String client_id, String client_secret, String redirect_uri, HttpServletRequest request) {
    AccessTokenInfo tokenInfo = null;
    HttpClient client = new HttpClient();
    PostMethod method = new PostMethod("https://accounts.google.com/o/oauth2/token");

    try {
      redirect_uri = redirect_uri.concat("GoogleEnforcer");
      NameValuePair[] data = {
        new NameValuePair("code", code),
        new NameValuePair("client_id", client_id),
        new NameValuePair("client_secret", client_secret),
        new NameValuePair("redirect_uri", redirect_uri),
        new NameValuePair("grant_type", "authorization_code")
      };
      method.setRequestBody(data);
      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

      int status = client.executeMethod(method);
      if (status == HttpStatus.SC_OK) {
        byte[] in = method.getResponseBody();

        String onetimeUseToken = AuthSubUtil.getTokenFromReply(request.getQueryString());

        String body = new String(in);

        // process body
        if (null != body && body.length() > 0) {
          JSONObject jsonObject = new JSONObject(body);
          tokenInfo = new AccessTokenInfo();
          String accessToken = "";
          String refreshToken = "";
          Integer expiration = jsonObject.getInt("expires_in");
          tokenInfo.setStart(new Date().getTime());


          if (jsonObject.has("access_token")) {
            accessToken = jsonObject.getString("access_token");
            System.out.println("Access token " + accessToken);
          }
          if (jsonObject.has("refresh_token")) {
            refreshToken = jsonObject.getString("refresh_token");
            System.out.println("Refresh token " + refreshToken);
          }
          if (jsonObject.has("expires_in")) {
            expiration = jsonObject.getInt("expires_in");
            System.out.println("expires_in " + expiration);
          }

          tokenInfo.setAccessToken(accessToken);
          tokenInfo.setRefreshToken(refreshToken);
          tokenInfo.setExpires(expiration);
        }
      } else {
        _logger.error("Could not establish connection with Google to obtain access token");
        _logger.error("Reason: {0}", method.getStatusText());
        return tokenInfo;
      }
    } catch (HttpException e) {
    	_logger.error("error in negotiateAccessToken", e);
      e.printStackTrace();
    } catch (IOException e) {
    	_logger.error("error in negotiateAccessToken", e);
      e.printStackTrace();
    } catch (Throwable t) {
    	_logger.error("error in negotiateAccessToken", t);
    } finally {
      method.releaseConnection();
    }
    return tokenInfo;
  }

  private void exchangeSessionToken(String refreshToken) {
  }
}
