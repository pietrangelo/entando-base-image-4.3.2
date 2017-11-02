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
package org.entando.entando.plugins.jpsugarcrm.apsadmin.portal.specialwidget.leads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public class LeadsListConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(LeadsListConfigAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			this.createValuedShowlet();
		} catch (Exception e) {
			_logger.error("error on LeadsListConfigAction validate", e);
		}
	}


	@Override
	public String save() {
		try {
			this.checkBaseParams();
			//this.createValuedWidget();
			this.createValuedShowlet();
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code ={}, pageCode ={}, frame ={}", this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}

	public Integer getMaxResults() {
		return _maxResults;
	}
	public void setMaxResults(Integer maxResults) {
		this._maxResults = maxResults;
	}

	private Integer _maxResults;
}

