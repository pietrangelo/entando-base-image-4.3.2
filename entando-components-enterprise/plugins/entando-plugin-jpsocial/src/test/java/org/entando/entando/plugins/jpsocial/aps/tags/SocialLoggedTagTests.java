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

package org.entando.entando.plugins.jpsocial.aps.tags;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.entando.entando.plugins.jpsocial.aps.system.JpSocialSystemConstants;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPageContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author S.Loru
 */
public class SocialLoggedTagTests extends ApsTagBaseTestCase {
	
	private SocialLoggedTag loggedTag;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ConfigInterface configInterface = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		configInterface.updateConfigItem(JpSocialSystemConstants.FACEBOOK_CONSUMER_KEY_PARAM_NAME, "533910603314020");
		configInterface.updateConfigItem(JpSocialSystemConstants.FACEBOOK_CONSUMER_SECRET_PARAM_NAME, "b1da5012ec3cd51927a813449f83cf3e");
		configInterface.updateConfigItem(JpSocialSystemConstants.TWITTER_CONSUMER_KEY_PARAM_NAME, "AORobBdRhfR6WrwUff0vg");
		configInterface.updateConfigItem(JpSocialSystemConstants.TWITTER_CONSUMER_SECRET_PARAM_NAME, "eRSPfTuZJGyFZvpslztFqs6TTuFGAr2makstC0XGhg");
		loggedTag = new SocialLoggedTag();
		loggedTag.setPageContext(this.getMockPageContext());
		this.setUserOnSession("admin");
		assertEquals(((WebApplicationContext) this.getApplicationContext()).getServletContext(), this.getServletContext());
		
	}
	
	@Test
	public void testLoggedFacebook() throws Exception{
		loggedTag.setProvider("facebook");
		boolean parseBoolean = Boolean.parseBoolean((String) this.getMockPageContext().getAttribute("isLogged"));
		assertFalse(parseBoolean);
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
		MockPageContext mockPageContext = new MockPageContext(this.getServletContext(), request);
		loggedTag = new SocialLoggedTag();
		loggedTag.setPageContext(mockPageContext);
		loggedTag.setProvider("facebook");
		loggedTag.doStartTag();
		boolean logged = (Boolean) mockPageContext.getAttribute("isLogged");
		assertTrue(logged);
		mockPageContext = new MockPageContext(this.getServletContext(), request);
		loggedTag = new SocialLoggedTag();
		loggedTag.setPageContext(mockPageContext);
		loggedTag.setProvider("facebook");
		loggedTag.setVar("var");
		loggedTag.doStartTag();
		boolean loggedVar = (Boolean) mockPageContext.getAttribute("var");
		assertTrue(loggedVar);
	}
	
	@Test
	public void testLoggedTwitter() throws Exception{
		loggedTag.setProvider("twitter");
		boolean notLogged = Boolean.parseBoolean((String) this.getMockPageContext().getAttribute("isLogged"));
		assertFalse(notLogged);
		HttpServletRequest request = new MockHttpServletRequest()
        {
            public Cookie[] getCookies()
            {
                return new Cookie[]
                { new Cookie("adminAccessTokenTwitter","test") };
            }
        };
		String currentUser = "admin";
		UserDetails user = this.getUser(currentUser, currentUser);//nel database di test, username e password sono uguali
		HttpSession session = request.getSession();
		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
		MockPageContext mockPageContext = new MockPageContext(this.getServletContext(), request);
		loggedTag = new SocialLoggedTag();
		loggedTag.setPageContext(mockPageContext);
		loggedTag.setProvider("twitter");
		loggedTag.doStartTag();
		boolean logged = (Boolean) mockPageContext.getAttribute("isLogged");
		assertTrue(logged);
		mockPageContext = new MockPageContext(this.getServletContext(), request);
		loggedTag = new SocialLoggedTag();
		loggedTag.setPageContext(mockPageContext);
		loggedTag.setProvider("twitter");
		loggedTag.setVar("var");
		loggedTag.doStartTag();
		boolean loggedVar = (Boolean) mockPageContext.getAttribute("var");
		assertTrue(loggedVar);
	}
	
}
