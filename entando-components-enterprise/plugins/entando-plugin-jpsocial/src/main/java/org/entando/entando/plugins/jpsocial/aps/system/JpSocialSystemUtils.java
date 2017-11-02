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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import java.net.URI;

/**
 *
 * @author entando
 */
public class JpSocialSystemUtils {

	private static final Logger _logger =  LoggerFactory.getLogger(JpSocialSystemUtils.class);
	
	public static String normalizeTweet(String tweet) {
		String normalizedTweet = null;

		if (null != tweet && !"".equals(tweet.trim())) {
			boolean isNormalized = Normalizer.isNormalized(tweet, Normalizer.Form.NFC);

			if (!isNormalized) {
				normalizedTweet = Normalizer.normalize(tweet.trim(), Normalizer.Form.NFC);
			} else {
				// already normalized
				normalizedTweet = tweet;
			}
		}
		return normalizedTweet;
	}

	public static FacebookCredentials getFacebookCredentialsFromCookie(HttpServletRequest request) throws ApsSystemException {
		FacebookCredentials facebookCredentials = null;
		String currentUser = getCurrentUserName(request);
		Cookie[] cookies = request.getCookies();
		String accessTokenName = currentUser.concat("AccessTokenFacebook");
		String accessToken = getTokenFromCookie(cookies, accessTokenName);
		if (!StringUtils.isBlank(accessToken)) {
			facebookCredentials = new FacebookCredentials(accessToken);
		}
		return facebookCredentials;
	}

	public static TwitterCredentials getTwitterCredentialsFromCookie(HttpServletRequest request) throws ApsSystemException {
		TwitterCredentials twitterCredentials = null;
		String currentUser = getCurrentUserName(request);
		Cookie[] cookies = request.getCookies();
		String accessTokenName = currentUser.concat("AccessTokenTwitter");
		String accessToken = getTokenFromCookie(cookies, accessTokenName);
		String secretTokenName = currentUser.concat("SecretTokenTwitter");
		String secretToken = getTokenFromCookie(cookies, secretTokenName);
		if (!StringUtils.isBlank(accessToken)) {
			twitterCredentials = new TwitterCredentials(accessToken, secretToken);
		}
		return twitterCredentials;
	}

	public static boolean removeFacebookCredentialsFromCookie(HttpServletRequest request, HttpServletResponse response) throws ApsSystemException, MalformedURLException {
		String currentUser = getCurrentUserName(request);
		Cookie[] cookies = request.getCookies();
		String accessTokenName = currentUser.concat("AccessTokenFacebook");
		expireCookie(accessTokenName, response, request);
		return true;
	}

	public static void removeTwitterCredentialsFromCookie(HttpServletRequest request, HttpServletResponse response) throws ApsSystemException, MalformedURLException {
		String currentUser = getCurrentUserName(request);
		String accessTokenName = currentUser.concat("AccessTokenTwitter");
		expireCookie(accessTokenName, response, request);
		String secretTokenName = currentUser.concat("SecretTokenTwitter");
		expireCookie(secretTokenName, response, request);
	}

	public static void saveTwitterCredentialsCookie(TwitterCredentials twitterCredentials, HttpServletResponse response, HttpServletRequest request) throws ApsSystemException {
		String currentUserName = getCurrentUserName(request);
		if (StringUtils.isNotBlank(currentUserName) && !"guest".equals(currentUserName)) {
			String cookieName = currentUserName.concat("AccessTokenTwitter");
			String cookieValue = twitterCredentials.getAccessToken();
			saveCookie(cookieName, cookieValue, response, request);
			cookieName = currentUserName.concat("SecretTokenTwitter");
			cookieValue = twitterCredentials.getTokenSecret();
			saveCookie(cookieName, cookieValue, response, request);
		}
	}

	public static void saveFacebookCredentialsCookie(FacebookCredentials facebookCredentials, HttpServletResponse response, HttpServletRequest request) throws ApsSystemException {
		String currentUserName = getCurrentUserName(request);
		if (StringUtils.isNotBlank(currentUserName) && !"guest".equals(currentUserName)) {
			String cookieName = currentUserName.concat("AccessTokenFacebook");
			String cookieValue = facebookCredentials.getAccessToken();
			saveCookie(cookieName, cookieValue, response, request);
		}
	}

	/**
	 * Check whether a tweet exceedes the allowed length. We have to measure the
	 * length of the text using the Normalization Form C
	 *
	 * @param tweet
	 * @return true if the Tweet i svalid, false otherwise
	 */
	public static boolean isTweetValid(String tweet) {

		if (null != tweet && !"".equals(tweet.trim())) {
			String text = normalizeTweet(tweet);

			return (text.length() <= TWEET_LENGTH);
		}
		return false;
	}
	public final static int TWEET_LENGTH = 140;

	private static void saveCookie(String cookieName, String cookieValue, HttpServletResponse response, HttpServletRequest request) throws ApsSystemException {
		Cookie cookie;
		try {
			cookie = new Cookie(URLEncoder.encode(cookieName, "UTF-8"), URLEncoder.encode(cookieValue, "UTF-8"));
			cookie.setMaxAge(365 * 24 * 60 * 60);//one year
			cookie.setPath(request.getContextPath());
			cookie.setDomain(new URL(request.getRequestURL().toString()).getHost());
		} catch (Throwable ex) {
			_logger.error("error in saveCookie", ex);
			throw new ApsSystemException("error while encoding cookie");
		}
		response.addCookie(cookie);
	}

	private static void expireCookie(String cookieName, HttpServletResponse response, HttpServletRequest request) throws ApsSystemException, MalformedURLException {
		Cookie cookie = (Cookie) getCookie(request.getCookies(), cookieName);
		if (cookie != null) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath());
			cookie.setDomain(new URL(request.getRequestURL().toString()).getHost());
			response.addCookie(cookie);
		}
	}

	private static String getTokenFromCookie(Cookie[] cookies, String accessTokenName) throws ApsSystemException {
		String accessToken = null;
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				try {
					String cookieName = URLDecoder.decode(cookie.getName(), "UTF-8");
					if (cookieName.equals(accessTokenName)) {
						accessToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
						break;
					}
				} catch (UnsupportedEncodingException ex) {
					_logger.error("error while decoding cookie", ex);
					throw new ApsSystemException("error while decoding cookie");
				}
			}
		}
		return accessToken;
	}

	private static Cookie getCookie(Cookie[] cookies, String accessTokenName) throws ApsSystemException {
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(accessTokenName)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static String getCurrentUserName(HttpServletRequest request) {
		String currentUserName = "";
		UserDetails currentUser =
				(UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		if (null != currentUser) {
			currentUserName = currentUser.getUsername();
		}
		return currentUserName;
	}

	public static String getUserIdBySocialId(String id){
		String[] split = id.split("_");
		if(split.length == 2){
			return split[0];
		}
		return "";
	}

	public static String getPostIdBySocialId(String id){
		String[] split = id.split("_");
		if(split.length == 2){
			return split[1];
		}
		return "";
	}

	public static String addParameterToUrl(String url, String name, String value) {
		String uri = url;
		try {
			if(url.contains("?")){
				uri = url.split("\\?")[0];
			}
			URI uriObject = new URI(url);
			List<NameValuePair> parseValue = URLEncodedUtils.parse(uriObject, "US-ASCII");
			uri = uri.concat("?");
			for (int i = 1; i < parseValue.size(); i++) {
				NameValuePair nameValuePair = parseValue.get(i);
				if(name.equals(nameValuePair.getName())){
					parseValue.remove(nameValuePair);
				} else {
					uri = uri.concat(nameValuePair.getName() + "=" + nameValuePair.getValue() + "&");
				}
			}
			uri = uri.concat(name + "=" + value);
		} catch (Throwable t) {
			throw new RuntimeException("Error creating url", t);
		}
		return uri;
	}
}
