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
package com.agiletec.plugins.jpforum.aps.tags;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.IStatisticManager;
import com.agiletec.plugins.jpforum.aps.system.services.statistics.MostOnlineUsersRecord;

public class MostOnlineUsersTag extends OutSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(MostOnlineUsersTag.class);

	@Override
	public int doStartTag() throws JspException {
		try {
			IStatisticManager statisticManager = (IStatisticManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.STATISTIC_MANAGER, this.pageContext);
			MostOnlineUsersRecord value  = statisticManager.getMostOnlineUsersRecord();
			if (null == value) return super.doStartTag();
			if (this.getVar() != null) {
				this.pageContext.setAttribute(this.getVar(), value);
			} 
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setVar(null);
	}
	
	/**
	 * Imposta l'attributo che definisce il nome della variabile di output.
	 * @param var Il nome della variabile.
	 */
	public void setVar(String var) {
		this._var = var;
	}

	/**
	 * Restituisce l'attributo che definisce il nome della variabile di output.
	 * @return Il nome della variabile.
	 */
	public String getVar() {
		return _var;
	}

	private String _var;

}