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
package com.agiletec.plugins.jpforum.aps.tags.util;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.tags.util.AdminPagerTagHelper;
import com.agiletec.apsadmin.util.ApsRequestParamsUtil;
import com.agiletec.plugins.jpforum.aps.system.JpforumSystemConstants;

public class JpforumPagerTagHelper extends AdminPagerTagHelper {

	private static final Logger _logger =  LoggerFactory.getLogger(JpforumPagerTagHelper.class);

	protected int getItemNumber(ServletRequest request) {
		String stringItem = null;
		String[] params = ApsRequestParamsUtil.getApsParams("pagerItem", "_", request);
		if (params != null && params.length == 2) {
			stringItem = params[1];
		} else {
			stringItem = request.getParameter("pagerItem");
			if (null == stringItem) {
				Integer item =  (Integer) request.getAttribute(JpforumSystemConstants.ATTR_PAGER_ITEM_NUMBER);
				if (null != item) {
					stringItem = item.toString();
				}
			}
		}
		int item = 0;
		if (stringItem != null) {
			try {
				item = Integer.parseInt(stringItem);
			} catch (NumberFormatException e) {
				_logger.error("Errore in parsing stringItem {}", stringItem);
			}
		}
		return item;
	}
}
