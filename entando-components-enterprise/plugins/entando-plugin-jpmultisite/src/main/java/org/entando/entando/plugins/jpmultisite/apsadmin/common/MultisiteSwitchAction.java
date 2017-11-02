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
package org.entando.entando.plugins.jpmultisite.apsadmin.common;

import com.agiletec.apsadmin.system.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpmultisite.aps.system.JpmultisiteSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteSwitchAction extends BaseAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteSwitchAction.class);

	public String switchMultisite() {
		String result = SUCCESS;
		_logger.info("multisite_code: "+ this.getMultisiteCode());
		if(StringUtils.isNotEmpty(this.getMultisiteCode())){
			result = "multisite";
		}
		this.getRequest().getSession().setAttribute(JpmultisiteSystemConstants.SESSION_PAR_CURRENT_MULTISITE, this.getMultisiteCode());
		return result;
	}

	public String getMultisiteCode() {
		return multisiteCode;
	}

	public void setMultisiteCode(String multisiteCode) {
		this.multisiteCode = multisiteCode;
	}

	private String multisiteCode;

}
