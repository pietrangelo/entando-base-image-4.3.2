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
package org.entando.entando.plugins.jpsalesforce.apsadmin.message.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config.NotifierConfigAction;

public class FormNotifierConfigAction extends NotifierConfigAction {
	
	@Override
	public String save() {
		String result = null;
		
		try {
			result = super.save();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return result;
	}
	
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}
	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}
	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}
	public Widget getShowlet() {
		return _showlet;
	}
	public void setShowlet(Widget showlet) {
		this._showlet = showlet;
	}
	public String getCampaignId() {
		return _campaignId;
	}
	public void setCampaignId(String campaignId) {
		this._campaignId = campaignId;
	}
	public String getProfileId() {
		return _profileId;
	}
	public void setProfileId(String profileId) {
		this._profileId = profileId;
	}

	private String _pageCode;
	private int _frame = -1;
	private String _widgetTypeCode;
	private Widget _showlet;
	private String _campaignId;
	private String _profileId;
	
}
