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
package com.agiletec.plugins.jppentaho.aps.system.services.cache;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedEvent;
import org.entando.entando.aps.system.services.widgettype.events.WidgetTypeChangedObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.cache.ICacheManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jppentaho.aps.system.JpPentahoSystemConstants;
import com.agiletec.plugins.jppentaho.aps.system.services.report.IPentahoDynamicReportManager;

/**
 * @author E.Santoboni
 */
public class CacheWrapperManager extends AbstractService implements PageChangedObserver, WidgetTypeChangedObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CacheWrapperManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		try {
			IPage page = event.getPage();
			if (null != page && null != page.getWidgets()) {
				for (int i = 0; i < page.getWidgets().length; i++) {
					Widget widget = page.getWidgets()[i];
					if (null != widget && null != widget.getType()) {
						WidgetType type = widget.getType();
						ApsProperties properties = (type.isLogic()) ? type.getConfig() : widget.getConfig();
						if (null != properties) {
							String param1 = properties.getProperty(IPentahoDynamicReportManager.SHOWLET_REFRESH_PERIOD);
							String param2 = properties.getProperty(IPentahoDynamicReportManager.SHOWLET_REFRESH_PERIOD_UNITS_OF_MEASUREMENT);
							if (null != param1 && null != param2) {
								String showletTypeCode = type.getCode();
								String groupName = JpPentahoSystemConstants.SHOWLET_TYPE_CACHE_GROUP_PREFIX + showletTypeCode;
								this.getCacheManager().flushGroup(groupName);
								System.out.println("RILASCIO - PAGINA " + page.getCode()
										+ " - frame " + i + " - Widget TYPE " + showletTypeCode);
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in updateFromPageChanged", t);
		}
	}

	@Override
	public void updateFromShowletTypeChanged(WidgetTypeChangedEvent event) {
		try {
			String showletTypeCode = event.getShowletTypeCode();
			String groupName = JpPentahoSystemConstants.SHOWLET_TYPE_CACHE_GROUP_PREFIX + showletTypeCode;
			this.getCacheManager().flushGroup(groupName);
			System.out.println("RILASCIO - Widget TYPE " + showletTypeCode);
		} catch (Throwable t) {
			_logger.error("error in updateFromShowletTypeChanged", t);
		}
	}

	protected ICacheManager getCacheManager() {
		return _cacheManager;
	}
	public void setCacheManager(ICacheManager cacheManager) {
		this._cacheManager = cacheManager;
	}

	private ICacheManager _cacheManager;

}