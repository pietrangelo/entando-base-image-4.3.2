package org.entando.entando.plugins.jpsugarcrm.aps.services.client;

import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrmLeadInfo {

	private static final Logger _logger =  LoggerFactory.getLogger(CrmLeadInfo.class);
	
	public String getDetailLink() throws Exception {
		String url = "";
		try {
			url = "index.php?action=DetailView&module=Leads&record=" + this.getProperty("id");
			url =  URLEncoder.encode(url, "UTF-8");
		} catch (Exception e) {
			_logger.error("error creating getDetailLink", e);
		}
		return url;
	}

	public String getProperty(String key) {
		String value = null;
		if (null != _properties) {
			value = _properties.get(key);
		}
		return value;
	}

	public Map<String, String> getProperties() {
		return _properties;
	}

	public void setProperties(Map<String, String> properties) {
		this._properties = properties;
	}



	private Map<String, String> _properties;

}
