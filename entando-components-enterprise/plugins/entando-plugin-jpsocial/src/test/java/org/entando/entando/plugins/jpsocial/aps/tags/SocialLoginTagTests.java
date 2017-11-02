/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * Entando is a free software;
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 *
 * See the file License for the specific language governing permissions
 * and limitations under the License
 *
 *
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */

package org.entando.entando.plugins.jpsocial.aps.tags;

import java.net.URLEncoder;
import javax.servlet.jsp.tagext.TagSupport;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author S.Loru
 */

public class SocialLoginTagTests extends ApsTagBaseTestCase {
	
	private SocialLoginTag socialLoginTag;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		socialLoginTag = new SocialLoginTag();
		socialLoginTag.setPageContext(this.getMockPageContext());
		assertEquals(((WebApplicationContext) this.getApplicationContext()).getServletContext(), this.getServletContext());
	}
	
	@Test
	public void testSocialLoginFacebook() throws Exception{
		socialLoginTag.setProvider("facebook");
		String redirTest = "http://www.google.it";
		socialLoginTag.setCurrentPage(redirTest);
		redirTest = redirTest.concat("?login=false");
		redirTest = URLEncoder.encode(redirTest, "UTF-8");
		String loginUrl="https://www.facebook.com/dialog/oauth?client_id=533910603314020&redirect_uri="+redirTest+"&scope=email,publish_stream";
		int tagReturnValue = socialLoginTag.doStartTag();
		assertEquals("Tag should return 'EVAL_PAGE'", tagReturnValue, TagSupport.EVAL_PAGE);
		assertEquals(loginUrl, this.getMockPageContext().getAttribute("loginUrl"));
	}
	
	@Test
	public void testSocialLoginTwitter() throws Exception{
		socialLoginTag.setProvider("twitter");
		String redirTest = "http://www.google.it";
		socialLoginTag.setCurrentPage(redirTest);
		redirTest = redirTest.concat("?login=false");
		redirTest = URLEncoder.encode(redirTest, "UTF-8");
		String loginUrl="/Entando/Twitter?tw_redirectUrl="+redirTest;
		int tagReturnValue = socialLoginTag.doStartTag();
		assertEquals("Tag should return 'EVAL_PAGE'", TagSupport.EVAL_PAGE, tagReturnValue);
		assertEquals(loginUrl, this.getMockPageContext().getAttribute("loginUrl"));
	}
	
}
