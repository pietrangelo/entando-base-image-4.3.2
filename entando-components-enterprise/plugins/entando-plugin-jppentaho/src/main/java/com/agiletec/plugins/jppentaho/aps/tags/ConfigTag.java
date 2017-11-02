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
package com.agiletec.plugins.jppentaho.aps.tags;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jppentaho.aps.system.JpPentahoSystemConstants;
import com.agiletec.plugins.jppentaho.aps.system.services.config.IPentahoConfigManager;
import com.agiletec.plugins.jppentaho.aps.system.services.config.PentahoConfig;

/**
 * Return a attribute value of the pentaho config.
 * @author E.Santoboni
 */
public class ConfigTag extends OutSupport {

	private static final Logger _logger =  LoggerFactory.getLogger(ConfigTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			IPentahoConfigManager configManager = (IPentahoConfigManager) ApsWebApplicationUtils.getBean(JpPentahoSystemConstants.PENTAHO_CONFIG_MANAGER, pageContext);
                        PentahoConfig config = configManager.getConfig();
                        Object value = this.getPropertyConfigValue(config);
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
        
        
	protected Object getPropertyConfigValue(PentahoConfig config) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(config.getClass());
			PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = descriptors[i];
				if (!descriptor.getName().equals(this.getKey())) {
					continue;
				}
				Method method = descriptor.getReadMethod();
				Object[] args = null;
				return method.invoke(config, args);
                        }
		} catch (Throwable t) {
			_logger.error("Error extracting property value : Master Object '{}' property '{}'",config.getClass().getName(), this.getKey(), t);
		}
		return null;
	}
    
	
	@Override
	public void release() {
		super.release();
		this.setVar(null);
		super.escapeXml = true;
		this.setKey(null);
	}
	
	public void setVar(String var) {
		this._var = var;
	}
        
	public String getVar() {
		return _var;
	}
	
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _var;
	private String _key;
	
}