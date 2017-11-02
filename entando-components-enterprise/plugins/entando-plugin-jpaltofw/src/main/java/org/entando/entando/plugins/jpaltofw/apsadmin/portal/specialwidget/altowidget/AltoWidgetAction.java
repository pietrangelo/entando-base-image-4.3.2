/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpaltofw.apsadmin.portal.specialwidget.altowidget;

import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.plugins.jpaltofw.aps.system.AltoSystemConstants;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.GokibiClient;
import org.entando.entando.plugins.jpaltofw.aps.system.service.client.GokibiWidget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class AltoWidgetAction extends SimpleWidgetConfigAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(AltoWidgetAction.class);
	
	@Override
	public void validate() {
		super.validate();
		try {
			this.createValuedShowlet();
			ApsProperties config = (null != this.getWidget()) ? this.getWidget().getConfig() : new ApsProperties();
			String baseUrl = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL);
			String pid = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_PID);
			String widgetCode = config.getProperty(AltoSystemConstants.WIDGET_PARAM_WIDGET_CODE);
			GokibiWidget gokibiWidget = new GokibiClient().getWidget(baseUrl, pid, widgetCode);
			if (null == gokibiWidget) {
				this.addFieldError(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL, 
						this.getText("jpaltofw.error.widget.invalidServer"));
				this.setValidServer(false);
			}
		} catch (Throwable t) {
			_logger.error("Error creating new widget", t);
			this.addFieldError(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL, 
					this.getText("jpaltofw.error.widget.invalidServer"));
			this.setValidServer(false);
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		ApsProperties config = this.getWidget().getConfig();
		if (null != config && !config.isEmpty()) {
			this.checkGokibiServer();
		}
		return result;
	}
	
	public String configServer() {
		try {
			this.createValuedShowlet();
			this.checkGokibiServer();
		} catch (Throwable t) {
			_logger.error("Error Configurating Alto Server", t);
			throw new RuntimeException("Error Configurating Alto Server", t);
		}
		return SUCCESS;
	}
	
	public String changeServer() {
		try {
			this.createValuedShowlet();
			this.setValidServer(false);
		} catch (Throwable t) {
			_logger.error("Error Changing Alto Server", t);
			throw new RuntimeException("Error Changing Alto Server", t);
		}
		return SUCCESS;
	}
	
	public List<SelectItem> getGokibiWidgets() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		try {
			Widget widget = this.getWidget();
			ApsProperties config = (null != widget) ? widget.getConfig() : new ApsProperties();
			String baseUrl = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL);
			String pid = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_PID);
			Map<String, GokibiWidget> gokibiWidgets = new GokibiClient().getWidgets(baseUrl, pid);
			Iterator<GokibiWidget> iter = gokibiWidgets.values().iterator();
			while (iter.hasNext()) {
				GokibiWidget gokibiWidget = iter.next();
				SelectItem item = new SelectItem(gokibiWidget.getCode(), gokibiWidget.getDescription());
				items.add(item);
			}
			BeanComparator comparator = new BeanComparator("value");
			Collections.sort(items, comparator);
		} catch (Throwable t) {
			_logger.error("Error Changing Alto Server", t);
			throw new RuntimeException("Error Changing Alto Server", t);
		}
		return items;
	}
	
	protected boolean checkGokibiServer() {
		Widget widget = this.getWidget();
		ApsProperties config = (null != widget) ? widget.getConfig() : new ApsProperties();
		String baseUrl = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL);
		String pid = config.getProperty(AltoSystemConstants.WIDGET_PARAM_SERVER_PID);
		return this.checkGokibiServer(baseUrl, pid);
	}
	
	protected boolean checkGokibiServer(String baseUrl, String pid) {
		this.setValidServer(false);
		try {
			Map<String, GokibiWidget> gokibiWidgets = new GokibiClient().getWidgets(baseUrl, pid);
			if (null != gokibiWidgets && !gokibiWidgets.isEmpty()) {
				this.setValidServer(true);
			}
		} catch (Throwable t) {
			_logger.error("Error checking Gokibi Server", t);
		}
		if (!this.getValidServer()) {
			this.addFieldError(AltoSystemConstants.WIDGET_PARAM_SERVER_BASE_URL, 
					this.getText("jpaltofw.error.widget.invalidServer"));
		}
		return false;
	}
	
	public Boolean getValidServer() {
		return _validServer;
	}
	public void setValidServer(Boolean validServer) {
		this._validServer = validServer;
	}
	
	private Boolean _validServer;
	
}