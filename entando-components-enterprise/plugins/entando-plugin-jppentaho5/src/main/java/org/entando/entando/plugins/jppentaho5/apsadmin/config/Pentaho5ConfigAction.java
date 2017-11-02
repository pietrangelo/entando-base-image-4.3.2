package org.entando.entando.plugins.jppentaho5.apsadmin.config;

import org.entando.entando.plugins.jppentaho5.aps.system.services.IPentahoManager;
import org.entando.entando.plugins.jppentaho5.aps.system.services.model.PentahoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

public class Pentaho5ConfigAction extends BaseAction implements IPentaho5ConfigAction {

	Logger _logger = LoggerFactory.getLogger(Pentaho5ConfigAction.class);

	@Override
	public String edit() {
		try {
			_config = this.getPentahoManager().getConfiguration();
		} catch (Throwable t) {
			_logger.error("Error editing configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String save() {
		PentahoConfiguration config = this.getConfig();

		try {
			getPentahoManager().updateConfiguration(config);
			_logger.info("Enpoints resetted as requested");
		} catch (Throwable t) {
			_logger.error("Error saving configuration", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String test() {
		PentahoConfiguration config = this.getConfig();
		String version = null;
		
		try {
			getPentahoManager().updateConfiguration(config);
			try {
				version = getPentahoManager().getPentahoVersion();
				addActionMessage(getText("jppentaho5.test.ok"));
			} catch (Throwable t) {
				_logger.error("Connection test failed");
				addActionError(getText("jppentaho5.test.fail"));
			}
		} catch (Throwable t) {
			addActionError(getText("jpbasecamp.test.invalid.configuration"));
			_logger.error("Invalid configuration detected", t);
		}
		return SUCCESS;
	}

	public PentahoConfiguration getConfig() {
		return _config;
	}

	public void setConfig(PentahoConfiguration config) {
		this._config = config;
	}

	public IPentahoManager getPentahoManager() {
		return _pentahoManager;
	}

	public void setPentahoManager(IPentahoManager pentahoManager) {
		this._pentahoManager = pentahoManager;
	}

	private PentahoConfiguration _config;
	private IPentahoManager _pentahoManager;

}
