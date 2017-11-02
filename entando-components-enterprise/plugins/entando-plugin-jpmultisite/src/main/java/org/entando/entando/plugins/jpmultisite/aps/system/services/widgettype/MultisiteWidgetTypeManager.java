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
package org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeManager;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteWidgetTypeManager extends WidgetTypeManager implements IMultisiteWidgetTypeManager {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteWidgetTypeManager.class);

	@Override
	public void deleteWidgetTypeLocked(String widgetTypeCode) throws ApsSystemException {
		try {
			WidgetType type = this.getWidgetType(widgetTypeCode);
			if (null == type) {
				_logger.error("Type not exists : type code {}", widgetTypeCode);
				return;
			}
			if (type.isLocked() && !type.getCode().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR)) {
				_logger.error("A locked widget can't be deleted - type {}", widgetTypeCode);
				return;
			}
			this.getWidgetTypeDAO().deleteWidgetTypeLocked(widgetTypeCode);
			init();
		} catch (Throwable t) {
			_logger.error("Error deleting widget type", t);
			throw new ApsSystemException("Error deleting widget type", t);
		}
	}

	/**
	 * to get an improved method we need to have a _widgetTypes field protected
	 * and not private
	 *
	 * @param multisiteCode
	 * @return
	 */
	@Override
	public List<WidgetType> getWidgetTypesByMultisiteCode(final String multisiteCode) {
		List<WidgetType> widgetTypes = this.getWidgetTypes();
		CollectionUtils.filter(widgetTypes, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				WidgetType widgetType;
				_logger.info("evaluate:"+multisiteCode);
				boolean result = true;
				if (object instanceof WidgetType) {
					widgetType = (WidgetType) object;
					_logger.info("***EVALUATE*** widgetType: "+ widgetType.getCode());
					if ((StringUtils.isBlank(multisiteCode) && widgetType.getCode().contains(JpmultisiteSystemConstants.MULTISITE_SUFFIX_SEPARATOR))) {
						result = false;
					} else if((StringUtils.isNotBlank(multisiteCode) && !widgetType.getCode().endsWith(multisiteCode))) {
						result = false;
					}
				}
				_logger.info("result: " + result);
				return result;
			}
		});
		return widgetTypes;
	}

}
