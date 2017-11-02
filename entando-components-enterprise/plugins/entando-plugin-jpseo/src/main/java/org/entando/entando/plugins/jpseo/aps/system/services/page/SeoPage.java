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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.util.Map;

import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.util.ApsProperties;

/**
 * This is the representation of a portal page
 * @author E.Santoboni
 */
public class SeoPage extends Page implements ISeoPage {
	
	@Override
	protected void setPosition(int position) {
		super.setPosition(position);
	}
	
	@Override
	public String getDescription(String langCode) {
		return this.getDescriptions().getProperty(langCode);
	}
	
	@Override
	public ApsProperties getDescriptions() {
		return _descriptions;
	}
	public void setDescriptions(ApsProperties descriptions) {
		this._descriptions = descriptions;
	}
	
	@Override
	public boolean isUseExtraDescriptions() {
		return _useExtraDescriptions;
	}
	public void setUseExtraDescriptions(boolean useExtraDescriptions) {
		this._useExtraDescriptions = useExtraDescriptions;
	}
	
	@Override
	public String getFriendlyCode() {
		return _friendlyCode;
	}
	public void setFriendlyCode(String friendlyCode) {
		this._friendlyCode = friendlyCode;
	}
	
	@Override
	public String getXmlConfig() {
		return _xmlConfig;
	}
	public void setXmlConfig(String xmlConfig) {
		this._xmlConfig = xmlConfig;
	}
	
	@Override
	public Map<String, Object> getComplexParameters() {
		return _complexParameters;
	}
	public void setComplexParameters(Map<String, Object> complexParameters) {
		this._complexParameters = complexParameters;
	}
	
	private ApsProperties _descriptions = new ApsProperties();
	private boolean _useExtraDescriptions = false;
	
	private String _friendlyCode;
	
	private String _xmlConfig;
	private Map<String, Object> _complexParameters;
	
}