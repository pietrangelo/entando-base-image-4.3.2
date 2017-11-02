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

import com.agiletec.aps.util.ApsWebApplicationUtils;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.entando.entando.plugins.jpmultisite.aps.system.services.multisite.IMultisiteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteListTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteListTag.class);

	@Override
	public int doEndTag() throws JspException {
		IMultisiteManager multisiteManager = (IMultisiteManager) ApsWebApplicationUtils.getBean(JpmultisiteSystemConstants.MULTISITE_MANAGER, this.pageContext);
		try {
			List<String> loadMultisites = multisiteManager.loadMultisites();
			if (null != this.getVar()) {
				this.pageContext.setAttribute(this.getVar(), loadMultisites);
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

	private String _var;
}
