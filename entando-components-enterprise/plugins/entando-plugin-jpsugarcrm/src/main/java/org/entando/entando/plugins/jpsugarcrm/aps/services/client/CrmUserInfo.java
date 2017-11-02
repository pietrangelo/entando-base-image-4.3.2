package org.entando.entando.plugins.jpsugarcrm.aps.services.client;

import java.util.HashMap;
import java.util.Map;

import com.sugarcrm.www.sugarcrm.Entry_value;
import com.sugarcrm.www.sugarcrm.Name_value;

public class CrmUserInfo {

	public String getFullName() {
		String fullname = null;
		if (null != _properties) {
			fullname = _properties.get("first_name") + " " + _properties.get("last_name");
		}
		return fullname;
	}

	public Map<String, String> getProperties() {
		return _properties;
	}

	public void setProperties(Map<String, String> properties) {
		this._properties = properties;
	}

	public void setProperties(Entry_value[] list) {
		Map<String, String> crmUserMap = new HashMap<String, String>();
		for (int i = 0; i < list.length; i++) {
			Entry_value entry = list[i];
			Name_value[] nv = entry.getName_value_list();
			for (int k = 0; k < nv.length; k++) {
				Name_value n = nv[k];
				crmUserMap.put(n.getName(), n.getValue());
			}
		}
		_properties = crmUserMap;
	}

	private Map<String, String> _properties;
}
