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

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static junit.framework.Assert.assertEquals;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.FacebookCredentials;
import org.entando.entando.plugins.jpsocial.aps.system.services.users.credentials.TwitterCredentials;
import org.entando.entando.plugins.jpsocial.aps.tags.ApsTagBaseTestCase;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author S.Loru
 */
public class JpSocialSystemUtilsTest extends ApsTagBaseTestCase{
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		assertEquals(((WebApplicationContext) this.getApplicationContext()).getServletContext(), this.getServletContext());
	}
	
	@Test
	public void testFacebookCookie() throws Exception{
		FacebookCredentials facebookCredentials = new FacebookCredentials("test");
		HttpServletResponse response = (HttpServletResponse) this.getMockPageContext().getResponse();
		assertNotNull(response);
		HttpServletRequest request = new MockHttpServletRequest()
        {
            public Cookie[] getCookies()
            {
                return new Cookie[]
                { new Cookie("adminAccessTokenFacebook","test") };
            }
        };
		String currentUser = "admin";
		UserDetails user = this.getUser(currentUser, currentUser);//nel database di test, username e password sono uguali
		HttpSession session = request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
//		JpSocialSystemUtils.saveFacebookCredentialsCookie(facebookCredentials, (HttpServletResponse) response, currentUser);
		FacebookCredentials facebookCredentialsFromCookie = JpSocialSystemUtils.getFacebookCredentialsFromCookie(request);
		assertNotNull(facebookCredentialsFromCookie);
		assertEquals("test", facebookCredentialsFromCookie.getAccessToken());
	}
	
	@Test
	public void testTwitterCookie() throws Exception{
		TwitterCredentials twitterCredentials = new TwitterCredentials("test");
		String currentUser = "admin";
		HttpServletResponse response = (HttpServletResponse) this.getMockPageContext().getResponse();
		assertNotNull(response);
		HttpServletRequest request = new MockHttpServletRequest()
        {
            public Cookie[] getCookies()
            {
                return new Cookie[]
                { new Cookie("adminAccessTokenTwitter","test") };
            }
        };
		UserDetails user = this.getUser(currentUser, currentUser);//nel database di test, username e password sono uguali
		HttpSession session = request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
//		JpSocialSystemUtils.saveTwitterCredentialsCookie(twitterCredentials, (HttpServletResponse) response, currentUser);
		TwitterCredentials twitterCredentialsFromCookie = JpSocialSystemUtils.getTwitterCredentialsFromCookie(request);
		assertNotNull(twitterCredentialsFromCookie);
		assertEquals("test", twitterCredentialsFromCookie.getAccessToken());
	}
	
	public void testRemoveTwitterCookie() throws Exception{
		TwitterCredentials twitterCredentials = new TwitterCredentials("test");
		String currentUser = "admin";
		HttpServletResponse response = (HttpServletResponse) this.getMockPageContext().getResponse();
		assertNotNull(response);
		HttpServletRequest request = new MockHttpServletRequest()
        {
            public Cookie[] getCookies()
            {
                return new Cookie[]
                { new Cookie("adminAccessTokenTwitter","test") };
            }
        };
		UserDetails user = this.getUser(currentUser, currentUser);//nel database di test, username e password sono uguali
		HttpSession session = request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
//		JpSocialSystemUtils.saveTwitterCredentialsCookie(twitterCredentials, (HttpServletResponse) response, currentUser);
		TwitterCredentials twitterCredentialsFromCookie = JpSocialSystemUtils.getTwitterCredentialsFromCookie(request);
		assertNotNull(twitterCredentialsFromCookie);
		assertEquals("test", twitterCredentialsFromCookie.getAccessToken());
	}
	
	public void testAddToUrl() {
		String url = "http://127.0.0.1:8080/social/en/test.page";
		String url2 = JpSocialSystemUtils.addParameterToUrl(url, "status", "ok");
		assertEquals("http://127.0.0.1:8080/social/en/test.page?status=ok", url2);
		
		String url3 = "http://127.0.0.1:8080/social/en/test.page?status=ok";
		url2 = JpSocialSystemUtils.addParameterToUrl(url3, "status", "ok");
		assertEquals("http://127.0.0.1:8080/social/en/test.page?status=ok", url2);
	}

}
