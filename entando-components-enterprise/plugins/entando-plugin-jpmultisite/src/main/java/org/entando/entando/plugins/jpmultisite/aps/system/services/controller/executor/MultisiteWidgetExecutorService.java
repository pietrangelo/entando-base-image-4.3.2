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
package org.entando.entando.plugins.jpmultisite.aps.system.services.controller.executor;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.util.IFrameDecoratorContainer;
import com.agiletec.aps.util.ApsWebApplicationUtils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.controller.executor.WidgetExecutorService;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.IGuiFragmentManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.plugins.jpmultisite.apsadmin.multisite.util.MultisiteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class MultisiteWidgetExecutorService extends WidgetExecutorService {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteWidgetExecutorService.class);
	
	@Override
	protected String buildWidgetOutput(RequestContext reqCtx, 
			Widget widget, List<IFrameDecoratorContainer> decorators) throws ApsSystemException {
		StringBuilder buffer = new StringBuilder();
		try {
			if (null != widget && this.isUserAllowed(reqCtx, widget)) {
				reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
			} else {
				reqCtx.removeExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			}
			buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, false, true));
			if (null != widget && this.isUserAllowed(reqCtx, widget)) {
				String widgetOutput = this.extractMutisiteWidgetOutput(reqCtx, widget.getType());
				buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, true, true));
				buffer.append(widgetOutput);
				buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, true, false));
			}
			buffer.append(this.extractDecoratorsOutput(reqCtx, widget, decorators, false, false));
		} catch (Throwable t) {
			String msg = "Error creating widget output";
			_logger.error(msg, t);
			throw new RuntimeException(msg, t);
		}
		return buffer.toString();
	}
	
	protected String extractMutisiteWidgetOutput(RequestContext reqCtx, WidgetType type) throws ApsSystemException {
		try {
			String widgetTypeCode = (type.isLogic()) ? type.getParentType().getCode() : type.getCode();
			
			widgetTypeCode = MultisiteUtils.cutMultisiteCodeWithSuffix(widgetTypeCode, MultisiteUtils.getMultisiteCode(reqCtx.getRequest()));
			
			IGuiFragmentManager guiFragmentManager = 
					(IGuiFragmentManager) ApsWebApplicationUtils.getBean(SystemConstants.GUI_FRAGMENT_MANAGER, reqCtx.getRequest());
			GuiFragment guiFragment = guiFragmentManager.getUniqueGuiFragmentByWidgetType(widgetTypeCode);
			if (null != guiFragment) {
				String fragmentOutput = extractFragmentOutput(guiFragment, reqCtx);
				if (StringUtils.isBlank(fragmentOutput)) {
					_logger.info("The fragment '{}' of widget '{}' is not available", guiFragment.getCode(), widgetTypeCode);
					return "The fragment '" + guiFragment.getCode() + "' of widget '" + widgetTypeCode + "' is not available";
				}
				return fragmentOutput;
			} else {
				String widgetJspPath = type.getJspPath();
				return extractJspWidgetOutput(widgetTypeCode, reqCtx, widgetJspPath);
			}
		} catch (Throwable t) {
			String msg = "Error creating widget output";
			_logger.error(msg, t);
			throw new ApsSystemException(msg, t);
		}
	}
	
	
}
