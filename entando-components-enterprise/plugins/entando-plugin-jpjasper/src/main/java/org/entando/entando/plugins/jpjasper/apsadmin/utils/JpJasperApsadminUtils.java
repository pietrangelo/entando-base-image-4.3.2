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
package org.entando.entando.plugins.jpjasper.apsadmin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.JpJasperSystemConstants;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;

public class JpJasperApsadminUtils {

	private static final Logger _logger =  LoggerFactory.getLogger(JpJasperApsadminUtils.class);
	
	/**
	 * Return the cookie header for rest api calls. It's stored in session
	 * When there is no cookie header sotred on session, a new one is created, stored on session and returned
	 * @param request
	 * @param jasperServerManager
	 * @return
	 */
	public static String getCookieHeader(HttpServletRequest request, IJasperServerManager jasperServerManager) {
		String cookieHeader = null;
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			cookieHeader = (String) request.getSession().getAttribute(currentUser.getUsername() + "_"  + JpJasperSystemConstants.SESSION_PARAM_COOKIE_HEADER);
			if (null == cookieHeader) {
				cookieHeader = jasperServerManager.getCookieHeader(currentUser);
				if (!StringUtils.isBlank(cookieHeader)) {
					_logger.debug("The cookie for {} is {}", currentUser.getUsername(), cookieHeader);
					request.getSession().setAttribute(currentUser.getUsername() + "_"  + JpJasperSystemConstants.SESSION_PARAM_COOKIE_HEADER, cookieHeader);
				}
			} else {
				boolean sessionAlive = jasperServerManager.ping(cookieHeader);
				if (!sessionAlive) {
					cookieHeader = jasperServerManager.getCookieHeader(currentUser);
					if (!StringUtils.isBlank(cookieHeader)) {
						request.getSession().setAttribute(currentUser.getUsername() + "_"  + JpJasperSystemConstants.SESSION_PARAM_COOKIE_HEADER, cookieHeader);
						_logger.debug("The cookie for {} is {}", currentUser.getUsername(), cookieHeader);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error trying to get a jasperserver cookie for user {}",currentUser.getUsername(), t);
			throw new RuntimeException(t);
		}
		return cookieHeader;
	}
	

	public static String getReportImageUriV2(ConfigInterface configInterface) {
		String applicationURL = configInterface.getParam(SystemConstants.PAR_APPL_BASE_URL);
		if (!applicationURL.endsWith("/"))applicationURL = applicationURL + "/";
		String imageUri = applicationURL + "do/jpjasper/FrontEnd/Report/Download/getReportImageV2?path=";
		return imageUri;
	}
	
	/*
	public static String updateImageUri(String html, String imageUri) {
		Pattern pattern = Pattern.compile("src=\"(.*?)\"");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			html =  matcher.replaceAll("src=\""+imageUri+"$1" + "\"");
		}
		return html;
	}
	*/
	
	/**
	 * Update the javascript src 
	 *<li>I JS non possono essere presi tramite API rest
	 *<li>Non è possibile configurare jasperServer: <strong>Bug 27253 -REST Services: Internal links to related resources in Flash Reports use hardcoded localhost values</strong>
	 *<pre>
	 *todo: aggiungere un sistema di caching per i file js.
	 *Copia su una dir locale dei file js e sostituzione del path relativo
	 *il file esiste in locale?
	 *	NO: copia il file in locale
	 *	SI: sostituzione del path relativo
	 *</pre>
	 * @param html
	 * @return
	 */
	public static String updateJSUri(String html, String baseUrl) {
		try {
			//String baseUrl = this.getJasperServerManager().getJasperBaseUrl();
			if (!baseUrl.endsWith("/")) baseUrl = baseUrl + "/";
			Pattern pattern = Pattern.compile("(<script.*)(src=\"|')(.*?\\.js)(\"|')");
			Matcher matcher = pattern.matcher(html);
			if (matcher.find()) {
				html =  matcher.replaceAll("$1$2"+baseUrl+"$3" + "$4");
			}
		} catch (Throwable t) {
			_logger.error("Error updating js uri");
			throw new RuntimeException(t);
		}
		return html;
	}


	public static String updateImageUri(String html, String imageUri) {
		Pattern pattern = Pattern.compile("(<img.*)(src=\"|')(.*?)(\"|')");
		Matcher matcher = pattern.matcher(html);
		if (matcher.find()) {
			html =  matcher.replaceAll("$1$2"+imageUri+"$3" + "$4");
		}
		return html;
	}
	
}
