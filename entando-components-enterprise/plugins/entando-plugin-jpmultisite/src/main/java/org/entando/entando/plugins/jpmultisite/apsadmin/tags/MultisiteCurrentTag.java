/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.apsadmin.tags;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.ResourceBundle;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.Multisite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteCurrentTag extends TagSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteCurrentTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		IMultisiteManager multisiteManager = (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.pageContext);
		String currentMultisiteCode = (String) session.getAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE);
		try {
		Multisite multisite = multisiteManager.loadMultisite(currentMultisiteCode);
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), multisite);
			} 
		} catch (Throwable t) {
			_logger.error("error in doEndTag", t);
			throw new JspException("Errore tag", t);
		}
		this.release();
		return super.doEndTag();
	}

	@Override
	public void release() {
		this.setVar(null);
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public String getKey() {
		return _key;
	}

	public void setKey(String key) {
		this._key = key;
	}
	
	private String _var;
	private String _key;
	

}
