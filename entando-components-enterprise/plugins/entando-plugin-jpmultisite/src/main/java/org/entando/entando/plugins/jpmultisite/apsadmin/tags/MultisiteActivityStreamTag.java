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
package org.entando.entando.plugins.jpmultisite.apsadmin.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.struts2.views.jsp.StrutsBodyTagSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.util.ValueStack;
import java.util.ArrayList;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.MultisiteActivityStreamManager;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;

/**
 * Return the list (of integer) for the activity stream of the current user.
 * @author E.Santoboni S.Loru
 */
public class MultisiteActivityStreamTag extends StrutsBodyTagSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(MultisiteActivityStreamTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		try {
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			MultisiteActivityStreamManager activityStreamManager = (MultisiteActivityStreamManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_ACTIVITY_STREAM_MANAGER, this.pageContext);
			List<Integer> ids = new ArrayList<Integer>();
			if(MultisiteUtils.getMultisiteCodeSuffix(request).isEmpty()){
				ids = activityStreamManager.getActivityStream(currentUser);
			} else {
				FieldSearchFilter filter = new FieldSearchFilter("username", MultisiteUtils.getMultisiteCodeSuffix(request), true, FieldSearchFilter.LikeOptionType.LEFT);
				FieldSearchFilter[] filters = {filter};
				ids = activityStreamManager.getActionRecords(filters);
			}
			if (null != this.getVar()) {
				ValueStack stack = this.getStack();
				stack.getContext().put(this.getVar(), ids);
	            stack.setValue("#attr['" + this.getVar() + "']", ids, false);
			}
		} catch (Throwable t) {
			_logger.error("Error on doEndTag", t);
			throw new JspException("Error on doEndTag", t);
		}
		return super.doEndTag();
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	private String _var;
	
}