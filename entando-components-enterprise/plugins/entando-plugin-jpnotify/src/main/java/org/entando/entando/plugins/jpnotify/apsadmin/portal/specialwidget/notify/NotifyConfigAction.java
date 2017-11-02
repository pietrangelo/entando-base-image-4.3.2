/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.apsadmin.portal.specialwidget.notify;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.INotifyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifyConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getNotifysId() {
		try {
			List<Integer> notifys = this.getNotifyManager().searchNotifys(null);
			return notifys;
		} catch (Throwable t) {
			_logger.error("error in getNotifysId", t);
			throw new RuntimeException("Error getting notifys list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected INotifyManager getNotifyManager() {
		return _notifyManager;
	}
	public void setNotifyManager(INotifyManager notifyManager) {
		this._notifyManager = notifyManager;
	}

	private int _id;
	private INotifyManager _notifyManager;
}

