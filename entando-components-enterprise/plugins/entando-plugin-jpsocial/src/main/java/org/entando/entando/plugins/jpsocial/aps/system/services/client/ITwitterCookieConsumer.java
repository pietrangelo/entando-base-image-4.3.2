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
import java.net.URISyntaxException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import org.entando.entando.plugins.jpsocial.aps.system.CookieMap;

/**
 * @author E.Santoboni
 */
public interface ITwitterCookieConsumer {
	
	public OAuthConsumer getTwitterConsumer(HttpServletRequest request) throws IOException;
	
	public OAuthAccessor getAccessor(HttpServletRequest request,
			HttpServletResponse response, OAuthConsumer consumer) throws OAuthException, IOException, URISyntaxException;
	
	public OAuthAccessor newAccessor(OAuthConsumer consumer, CookieMap cookies) throws OAuthException;
	
	public String copyResponse(OAuthMessage from) throws IOException;
	
	public void copyResponse(OAuthMessage from, HttpServletResponse into) throws IOException;
	
	public void handleException(Exception e, HttpServletRequest request,
            HttpServletResponse response, OAuthConsumer consumer)
            throws IOException, ServletException;
	
	public static final String TWITTER_CONSUMER_NAME = "twitter";
	
}
