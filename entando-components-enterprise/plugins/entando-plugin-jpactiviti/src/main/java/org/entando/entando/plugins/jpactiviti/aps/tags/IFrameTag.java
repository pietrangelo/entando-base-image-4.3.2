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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class IFrameTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IFrameTag.class);
	
	 @Override
    public int doEndTag() throws JspException {
        HttpSession session = this.pageContext.getSession();
        ServletRequest request = pageContext.getRequest();
//        ISugarCRMClientManager client = (ISugarCRMClientManager) ApsWebApplicationUtils.getBean(JpSugarCRMSystemConstants.SUGAR_CLIENT_MANAGER, this.pageContext);
        try {
			HttpClient client = new DefaultHttpClient();
            String url = "http://localhost:8081/activiti-explorer";
			String appUrl = "/ui/1/loginHandler";
			HttpPost post = new HttpPost(url+appUrl);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("username", "kermit"));
			nameValuePairs.add(new BasicNameValuePair("password", "kermit"));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("status code: "+statusCode);
            if (statusCode != 200) {
                return super.doEndTag();
            }
            if (this.getVar() != null) {
                this.pageContext.setAttribute(this.getVar(), url + "/ui/#process");
            } else {
                try {
                    this.pageContext.getOut().print(url);
                } catch (Throwable t) {
                	_logger.error("error in doEndTag", t);
                    throw new JspException("Error closing tag", t);
                }
            }
        } catch (Throwable t) {
        	_logger.error("error in doEndTag", t);
            throw new JspException("Error detected into endTag", t);
        }
        return super.doEndTag();
    }
	
    @Override
    public void release() {
        super.release();
        this.setVar(null);
    }
	
    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }
	
    private String var;
	
	
	/*
	  HttpClient client = new DefaultHttpClient();
    HttpPost post = new HttpPost("http://vogellac2dm.appspot.com/register");
    try {
      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
      nameValuePairs.add(new BasicNameValuePair("registrationid",
          "123456789"));
      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
      HttpResponse response = client.execute(post);
      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String line = "";
      while ((line = rd.readLine()) != null) {
        System.out.println(line);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

   
	 */
	
}