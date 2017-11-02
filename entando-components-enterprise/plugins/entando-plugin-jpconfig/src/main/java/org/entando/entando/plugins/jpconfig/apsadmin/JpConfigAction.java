package org.entando.entando.plugins.jpconfig.apsadmin;

import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;

public class JpConfigAction extends BaseAction {

	@Override
	public void validate() {
		super.validate();
		this.checkItem();
	}

	private void checkItem() {
		if (null == this.getItem() || this.getItem().trim().length() == 0) {
			this.addActionError(this.getText("jpconfig.message.noItemSelected"));
		}
	}

	public String viewItem() {
		try {
			String config = this.getBaseConfigMnager().getConfigItem(this.getItem());
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "viewItem");
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	public String saveItem() {
		try {
			this.getBaseConfigMnager().updateConfigItem(this.getItem(), this.getConfig());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveItem");
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	

	public void setItem(String item) {
		this._item = item;
	}
	public String getItem() {
		return _item;
	}

	public void setConfig(String config) {
		this._config = config;
	}
	public String getConfig() {
		return _config;
	}

	public void setItems(Map<String, String> items) {
		this._items = items;
	}
	public Map<String, String> getItems() {
		return _items;
	}

	public void setBaseConfigMnager(ConfigInterface baseConfigMnager) {
		this._baseConfigMnager = baseConfigMnager;
	}
	public ConfigInterface getBaseConfigMnager() {
		return _baseConfigMnager;
	}

	private ConfigInterface _baseConfigMnager;;
	
	private String _item;
	private String _config;
	private Map<String, String> _items;
}
