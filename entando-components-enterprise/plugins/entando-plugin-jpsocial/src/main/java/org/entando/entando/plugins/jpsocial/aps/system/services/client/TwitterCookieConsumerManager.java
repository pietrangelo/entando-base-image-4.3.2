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
package org.entando.entando.plugins.jpsocial.aps.system.services.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.server.OAuthServlet;

import org.apache.geronimo.mail.util.StringBufferOutputStream;
import org.entando.entando.plugins.jpsocial.aps.oauth.client.OAuthClient;
import org.entando.entando.plugins.jpsocial.aps.system.CookieMap;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.entando.entando.plugins.jpsocial.aps.system.MineCallback;
import org.entando.entando.plugins.jpsocial.aps.system.RedirectException;
import org.entando.entando.plugins.jpsocial.aps.system.httpclient.TwitterHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Utility methods for consumers that store tokens and secrets in cookies. Each
 * consumer has a name, and its accessors' credentials are stored in cookies
 * named [name].requestToken, [name].accessToken and [name].tokenSecret.
 *
 * @author John Kristian
 */
public class TwitterCookieConsumerManager
        extends AbstractService implements ITwitterCookieConsumer, ServletContextAware {

	private static final Logger _logger =  LoggerFactory.getLogger(TwitterCookieConsumerManager.class);

    @Override
    public void init() throws Exception {
        _logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    protected void release() {
        this._twitterConsumer = null;
        super.release();
    }

    @Override
    public OAuthConsumer getTwitterConsumer(HttpServletRequest request) throws IOException {
        if (null != this._twitterConsumer) {
            return this._twitterConsumer;
        }
        String name = TWITTER_CONSUMER_NAME;
        OAuthConsumer consumer = null;
        InputStream is = null;
        try {
            ConfigInterface configManager = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, request);
            is = this.getServletContext().getResourceAsStream("/WEB-INF/plugins/jpsocial/twitterconsumer.properties");
            Properties properties = new Properties();
            properties.load(is);

            //String base = configManager.getParam(ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_BASE_URL_PARAM_NAME);
            String base = properties.getProperty(name + ".serviceProvider.baseURL");

            URL baseURL = (base == null) ? null : new URL(base);
            if (null == baseURL) {
                return null;
            }
            //String requestTokenURL = getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_REQ_TOKEN_URL_PARAM_NAME);
            //String userAuthorizationURL = getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_AUTH_URL_PARAM_NAME);
            //String accessTokenURL = getURL(baseURL, properties, ConsumerSystemConstants.ENTANDO_SERVICE_PROVIDER_ACCESS_TOKEN_URL_PARAM_NAME);
            String requestTokenURL = this.getURL(baseURL, properties, (name + ".serviceProvider.requestTokenURL"));
            String userAuthorizationURL = this.getURL(baseURL, properties, (name + ".serviceProvider.userAuthorizationURL"));
            String accessTokenURL = this.getURL(baseURL, properties, (name + ".serviceProvider.accessTokenURL"));

            //OAuthServiceProvider serviceProvider = new OAuthServiceProvider(
            //		getURL(baseURL, name + ".serviceProvider.requestTokenURL"), 
            //		getURL(baseURL, name + ".serviceProvider.userAuthorizationURL"),
            //		getURL(baseURL, name + ".serviceProvider.accessTokenURL"));

            if (null == requestTokenURL || null == userAuthorizationURL || null == accessTokenURL) {
                return null;
            }
            OAuthServiceProvider serviceProvider = new OAuthServiceProvider(requestTokenURL, userAuthorizationURL, accessTokenURL);
            consumer = new OAuthConsumer(null,
                    configManager.getParam(JpSocialSystemConstants.TWITTER_CONSUMER_KEY_PARAM_NAME),
                    configManager.getParam(JpSocialSystemConstants.TWITTER_CONSUMER_SECRET_PARAM_NAME), serviceProvider);
            consumer.setProperty("name", name);
            if (baseURL != null) {
                consumer.setProperty("serviceProvider.baseURL", baseURL);
            }
            _twitterConsumer = consumer;
            //this._consumers.put(ConsumerSystemConstants.DEFAULT_CONSUMER_NAME, consumer);
        } catch (Throwable t) {
        	_logger.error("Error extracting twitter consumer", t);
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return consumer;
    }

    private String getURL(URL baseUrl, Properties properties, String pathParamName) throws MalformedURLException {
        String path = properties.getProperty(pathParamName);
        if (null == baseUrl || null == path) {
            return null;
        }
        return (new URL(baseUrl, path)).toExternalForm();
    }

    /**
     * Get the access token and token secret for the given consumer. Get them
     * from cookies if possible; otherwise obtain them from the service
     * provider. In the latter case, throw RedirectException.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Override
    public OAuthAccessor getAccessor(HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer) throws OAuthException, IOException, URISyntaxException {
        CookieMap cookies = new CookieMap(request, response);
        OAuthAccessor accessor = newAccessor(consumer, cookies);
        if (accessor.accessToken == null) {
            getAccessToken(request, cookies, accessor);
        }
        return accessor;
    }

    /**
     * Construct an accessor from cookies. The resulting accessor won't
     * necessarily have any tokens.
     */
    @Override
    public OAuthAccessor newAccessor(OAuthConsumer consumer, CookieMap cookies) throws OAuthException {
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        String consumerName = (String) consumer.getProperty("name");
        accessor.requestToken = cookies.get(consumerName + ".requestToken");
        accessor.accessToken = cookies.get(consumerName + ".accessToken");
        accessor.tokenSecret = cookies.get(consumerName + ".tokenSecret");
        return accessor;
    }

    /**
     * Remove all the cookies that contain accessors' data.
     */
    public static void removeAccessors(CookieMap cookies) {
        List<String> names = new ArrayList<String>(cookies.keySet());
        for (String name : names) {
            if (name.endsWith(".requestToken") || name.endsWith(".accessToken")
                    || name.endsWith(".tokenSecret")) {
                cookies.remove(name);
            }
        }
    }

    /**
     * Get a fresh access token from the service provider.
     *
     * @throws IOException
     * @throws URISyntaxException
     *
     * @throws RedirectException to obtain authorization
     */
    private static void getAccessToken(HttpServletRequest request,
            CookieMap cookies, OAuthAccessor accessor)
            throws OAuthException, IOException, URISyntaxException {
        String consumerName = TWITTER_CONSUMER_NAME;//(String) accessor.consumer.getProperty("name");
        final String callbackURL = getCallbackURL(request /*
                 * , consumerName
                 */);
        List<OAuth.Parameter> parameters = OAuth.newList(OAuth.OAUTH_CALLBACK, callbackURL);
        // Google needs to know what you intend to do with the access token:
        Object scope = accessor.consumer.getProperty("request.scope");
        if (scope != null) {
            parameters.add(new OAuth.Parameter("scope", scope.toString()));
        }
        OAuthClient client = new OAuthClient(new TwitterHttpClient());
        OAuthMessage response = client.getRequestTokenResponse(accessor, null, parameters);
        cookies.put(consumerName + ".requestToken", accessor.requestToken);
        cookies.put(consumerName + ".tokenSecret", accessor.tokenSecret);
        String authorizationURL = accessor.consumer.serviceProvider.userAuthorizationURL;
        if (authorizationURL.startsWith("/")) {
            authorizationURL = (new URL(new URL(request.getRequestURL().toString()), request.getContextPath() + authorizationURL)).toString();
        }
        authorizationURL = OAuth.addParameters(authorizationURL //
                , OAuth.OAUTH_TOKEN, accessor.requestToken);
        if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
            authorizationURL = OAuth.addParameters(authorizationURL //
                    , OAuth.OAUTH_CALLBACK, callbackURL);
        }
        throw new RedirectException(authorizationURL);
    }

    private static String getCallbackURL(HttpServletRequest request) throws IOException {
        URL base = new URL(new URL(request.getRequestURL().toString()), //
                request.getContextPath() + MineCallback.PATH);
        return OAuth.addParameters(base.toExternalForm() //
                , "consumer", TWITTER_CONSUMER_NAME //
                , "returnTo", getRequestPath(request) //
                );
    }

    /**
     * Reconstruct the requested URL path, complete with query string (if any).
     */
    private static String getRequestPath(HttpServletRequest request)
            throws MalformedURLException {
        URL url = new URL(OAuthServlet.getRequestURL(request));
        StringBuilder path = new StringBuilder(url.getPath());
        String queryString = url.getQuery();
        if (queryString != null) {
            path.append("?").append(queryString);
        }
        return path.toString();
    }

    @Override
    public String copyResponse(OAuthMessage from) throws IOException {
        InputStream in = from.getBodyAsStream();
        StringBuffer out = new StringBuffer();
        StringBufferOutputStream bufferOutputStream = new StringBufferOutputStream(out);
        byte[] buffer = new byte[1024];
        for (int len; 0 < (len = in.read(buffer, 0, buffer.length));) {
            bufferOutputStream.write(buffer, 0, len);
        }
        return out.toString();
    }

    @Override
    public void copyResponse(OAuthMessage from, HttpServletResponse into) throws IOException {
        InputStream in = from.getBodyAsStream();
        OutputStream out = into.getOutputStream();
        into.setContentType(from.getHeader("Content-Type"));
        try {
            TwitterCookieConsumerManager.copyAll(in, out);
        } finally {
            in.close();
        }
    }

    private static void copyAll(InputStream from, OutputStream into) throws IOException {
        byte[] buffer = new byte[1024];
        for (int len; 0 < (len = from.read(buffer, 0, buffer.length));) {
            into.write(buffer, 0, len);
        }
    }

    /**
     * Handle an exception that occurred while processing an HTTP request.
     * Depending on the exception, either send a response, redirect the client
     * or propagate an exception.
     */
    @Override
    public void handleException(Exception e, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer)
            throws IOException, ServletException {
        if (e instanceof RedirectException) {
            RedirectException redirect = (RedirectException) e;
            String targetURL = redirect.getTargetURL();
            if (targetURL != null) {
                response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                response.setHeader("Location", targetURL);
            }
        } else if (e instanceof OAuthProblemException) {
            OAuthProblemException p = (OAuthProblemException) e;
            String problem = p.getProblem();
            List<String> recoverableProblems = Arrays.asList(RECOVERABLE_PROBLEMS);
            if (consumer != null && recoverableProblems.contains(problem)) {
                try {
                    CookieMap cookies = new CookieMap(request, response);
                    OAuthAccessor accessor = newAccessor(consumer, cookies);
                    getAccessToken(request, cookies, accessor);
                    // getAccessToken(request, consumer,
                    // new CookieMap(request, response));
                } catch (Exception e2) {
                    handleException(e2, request, response, null);
                }
            } else {
                //System.out.println(" ERRORE ");
                
            	_logger.warn("Redirecting due a unhandled error....");
            	_logger.error("error in handleException", e);
//                e.printStackTrace();
                /*
                 * try { StringWriter s = new StringWriter(); PrintWriter pw =
                 * new PrintWriter(s); e.printStackTrace(pw); pw.flush();
                 * p.setParameter("stack trace", s.toString()); } catch
                 * (Exception rats) {}
                 * response.setStatus(p.getHttpStatusCode());
                 * response.resetBuffer();
                 * request.setAttribute("OAuthProblemException", p);
                 * request.getRequestDispatcher("/WEB-INF/plugins/jpsocial/OAuthProblemException.jsp").forward(request,
                 * response);
                 */
            }
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else if (e instanceof ServletException) {
            throw (ServletException) e;
        } else if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else {
            throw new ServletException(e);
        }
    }

    protected ServletContext getServletContext() {
        return _servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this._servletContext = servletContext;
    }

    protected ConfigInterface getConfigManager() {
        return _configManager;
    }

    public void setConfigManager(ConfigInterface configManager) {
        this._configManager = configManager;
    }
    private OAuthConsumer _twitterConsumer;
    private ServletContext _servletContext;
    private ConfigInterface _configManager;
    private static final String[] RECOVERABLE_PROBLEMS =
            new String[]{"token_revoked", "token_expired", "permission_unknown"};
}
