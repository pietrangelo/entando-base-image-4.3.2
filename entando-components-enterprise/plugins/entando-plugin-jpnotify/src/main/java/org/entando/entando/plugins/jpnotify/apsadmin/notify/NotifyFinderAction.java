/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.apsadmin.notify;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.Notify;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.INotifyManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifyFinderAction.class);

	public List<Integer> getNotifysId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getPayload())) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("payload"), this.getPayload(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getSenddateStart() || null != this.getSenddateEnd()) {
				Date startDate = this.getSenddateStart();
				Date endDate = this.getSenddateEnd();
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("senddate"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getAttempts()) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("attempts"), this.getAttempts(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getSent()) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("sent"), this.getSent(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getSender())) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("sender"), this.getSender(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getRecipient())) {
				//TODO add a constant into your INotifyManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("recipient"), this.getRecipient(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			List<Integer> notifys = this.getNotifyManager().searchNotifys(filters);
			return notifys;
		} catch (Throwable t) {
			_logger.error("Error getting notifys list", t);
			throw new RuntimeException("Error getting notifys list", t);
		}
	}

	protected FieldSearchFilter[] addFilter(FieldSearchFilter[] filters, FieldSearchFilter filterToAdd) {
		int len = filters.length;
		FieldSearchFilter[] newFilters = new FieldSearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	public Notify getNotify(int id) {
		Notify notify = null;
		try {
			notify = this.getNotifyManager().getNotify(id);
		} catch (Throwable t) {
			_logger.error("Error getting notify with id {}", id, t);
			throw new RuntimeException("Error getting notify with id " + id, t);
		}
		return notify;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public String getPayload() {
		return _payload;
	}
	public void setPayload(String payload) {
		this._payload = payload;
	}


	public Date getSenddateStart() {
		return _senddateStart;
	}
	public void setSenddateStart(Date senddateStart) {
		this._senddateStart = senddateStart;
	}


	public Date getSenddateEnd() {
		return _senddateEnd;
	}
	public void setSenddateEnd(Date senddateEnd) {
		this._senddateEnd = senddateEnd;
	}


	public Integer getAttempts() {
		return _attempts;
	}
	public void setAttempts(Integer attempts) {
		this._attempts = attempts;
	}


	public Integer getSent() {
		return _sent;
	}
	public void setSent(Integer sent) {
		this._sent = sent;
	}


	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}


	public String getRecipient() {
		return _recipient;
	}
	public void setRecipient(String recipient) {
		this._recipient = recipient;
	}


	protected INotifyManager getNotifyManager() {
		return _notifyManager;
	}
	public void setNotifyManager(INotifyManager notifyManager) {
		this._notifyManager = notifyManager;
	}
	
	private Integer _id;
	private String _payload;
	private Date _senddateStart;
	private Date _senddateEnd;
	private Integer _attempts;
	private Integer _sent;
	private String _sender;
	private String _recipient;
	private INotifyManager _notifyManager;
}