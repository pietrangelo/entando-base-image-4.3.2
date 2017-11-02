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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.config;

import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.Template;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model.UserRegConfig;

import java.util.Map;
import java.util.Set;

public class SubscriptionConfig extends UserRegConfig {
	
	@Override
	public Map<String, Template> getReactivationTemplates() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	@Override
	public Set<String> getDefaultCsvAuthorizations() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	@Override
	public String getReactivationPageCode() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	@Override
	public String getActivationPageCode() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	@Override
	public String getEMailSenderCode() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	@Override
	public long getTokenValidityMinutes() {
		throw new RuntimeException("Invalid Method for custom subscription - entando.com");
	}
	
	public String getUserprofileTypeCode() {
		return _userprofileTypeCode;
	}
	public void setUserprofileTypeCode(String userprofileTypeCode) {
		this._userprofileTypeCode = userprofileTypeCode;
	}
	
	public String getRedirectPageCode() {
		return _redirectPageCode;
	}
	public void setRedirectPageCode(String redirectPageCode) {
		this._redirectPageCode = redirectPageCode;
	}
	
	private String _userprofileTypeCode;
	private String _redirectPageCode;
	
}
