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

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;

public class UserInfoTag extends OutSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(UserInfoTag.class);

	@Override
	public int doStartTag() throws JspException {
		try {
			IThreadManager threadManager = (IThreadManager) ApsWebApplicationUtils.getBean(JpforumSystemConstants.THREAD_MANAGER, this.pageContext);
			Object value = null;
			if (this.getKey().equals("totalThreads")) {
				value = 0;
				List<Integer> list  = threadManager.getUserThreads(this.getUsername());
				if (null != list) value = list.size();
			} else if (this.getKey().equalsIgnoreCase("totalPosts")) {
				value = 0;
				List<Integer> list = threadManager.getUserPosts(this.getUsername());
				if (null != list) value = list.size();
			}

			if (null == value) return super.doStartTag();
			if (this.getVar() != null) {
				this.pageContext.setAttribute(this.getVar(), value);
			} else {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), value);
				} else {
					this.pageContext.getOut().print(value);
				}
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
		super.escapeXml = true;
		this.setKey(null);
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
	
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	/**
	 * Determina se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @return True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Setta se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @param escapeXml True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	

	private String _var;
	private String _key;
	private String _username;
	
}