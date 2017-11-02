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
package org.entando.entando.plugins.jpactiviti.aps.tags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author S.Loru
 */
public class TestIFrameTag extends ApsTagBaseTestCase {

	private IFrameTag iFrameTag;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		iFrameTag = new IFrameTag();
		iFrameTag.setPageContext(this.getMockPageContext());
		assertEquals(((WebApplicationContext) this.getApplicationContext()).getServletContext(), this.getServletContext());
	}

	@Test
	public void test() throws Exception {
		iFrameTag.doEndTag();
		assertTrue(true);
	}
	/*
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
	
	 */
}
