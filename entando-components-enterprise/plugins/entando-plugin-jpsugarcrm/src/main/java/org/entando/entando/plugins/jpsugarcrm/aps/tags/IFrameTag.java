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
package org.entando.entando.plugins.jpsugarcrm.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.entando.entando.plugins.jpsugarcrm.aps.JpSugarCRMSystemConstants;
import org.entando.entando.plugins.jpsugarcrm.aps.services.client.ISugarCRMClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * @author E.Santoboni
 * @author Salvatore Isaja, Taktyka s.r.l. (added support for the "redir" parameter)
 */
public class IFrameTag extends TagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(IFrameTag.class);
	
    @Override
    public int doEndTag() throws JspException {
        HttpSession session = this.pageContext.getSession();
        ServletRequest request = pageContext.getRequest();
        ISugarCRMClientManager client = (ISugarCRMClientManager) ApsWebApplicationUtils.getBean(JpSugarCRMSystemConstants.SUGAR_CLIENT_MANAGER, this.pageContext);
        try {
            UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            String url = client.executeLogin(currentUser, request.getParameter("redir"));
            if (url == null) {
                return super.doEndTag();
            }
            if (this.getVar() != null) {
                this.pageContext.setAttribute(this.getVar(), url);
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
	
}
