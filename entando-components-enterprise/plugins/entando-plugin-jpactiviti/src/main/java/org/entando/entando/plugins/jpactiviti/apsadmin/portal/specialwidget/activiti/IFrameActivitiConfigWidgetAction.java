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

package org.entando.entando.plugins.jpactiviti.apsadmin.portal.specialwidget.activiti;

import org.entando.entando.plugins.jpactiviti.aps.system.services.JpactivitiSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

/**
 * @author S.Loru
 */
public class IFrameActivitiConfigWidgetAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IFrameActivitiConfigWidgetAction.class);
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				String paramName = JpactivitiSystemConstants.WIDGET_PARAM_HEIGHT;
				String height = this.getWidget().getConfig().getProperty(paramName);
				this.setHeight(height);
			}
		} catch (Throwable t) {
			_logger.error("error in init", t);
			return FAILURE;
		}
		return result;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	private String height;
	private String section;
}
