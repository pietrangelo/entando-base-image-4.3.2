/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.apsadmin.notify;

import org.entando.entando.plugins.jpnotify.aps.system.services.notify.Notify;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.INotifyManager;

import java.util.Date;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifyAction.class);

	public String newNotify() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newNotify", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			Notify notify = this.getNotifyManager().getNotify(this.getId());
			if (null == notify) {
				this.addActionError(this.getText("error.notify.null"));
				return INPUT;
			}
			this.populateForm(notify);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			Notify notify = this.createNotify();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getNotifyManager().addNotify(notify);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getNotifyManager().updateNotify(notify);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			Notify notify = this.getNotifyManager().getNotify(this.getId());
			if (null == notify) {
				this.addActionError(this.getText("error.notify.null"));
				return INPUT;
			}
			this.populateForm(notify);
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String delete() {
		try {
			if (this.getStrutsAction() == ApsAdminSystemConstants.DELETE) {
				this.getNotifyManager().deleteNotify(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			Notify notify = this.getNotifyManager().getNotify(this.getId());
			if (null == notify) {
				this.addActionError(this.getText("error.notify.null"));
				return INPUT;
			}
			this.populateForm(notify);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(Notify notify) throws Throwable {
		this.setId(notify.getId());
		this.setPayload(notify.getPayload());
		this.setSenddate(notify.getSenddate());
		this.setAttempts(notify.getAttempts());
		this.setSent(notify.getSent());
		this.setSender(notify.getSender());
		this.setRecipient(notify.getRecipient());
	}
	
	private Notify createNotify() {
		Notify notify = new Notify();
		notify.setId(this.getId());
		notify.setPayload(this.getPayload());
		notify.setSenddate(this.getSenddate());
		notify.setAttempts(this.getAttempts());
		notify.setSent(this.getSent());
		notify.setSender(this.getSender());
		notify.setRecipient(this.getRecipient());
		return notify;
	}
	

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getPayload() {
		return _payload;
	}
	public void setPayload(String payload) {
		this._payload = payload;
	}

	public Date getSenddate() {
		return _senddate;
	}
	public void setSenddate(Date senddate) {
		this._senddate = senddate;
	}

	public int getAttempts() {
		return _attempts;
	}
	public void setAttempts(int attempts) {
		this._attempts = attempts;
	}

	public int getSent() {
		return _sent;
	}
	public void setSent(int sent) {
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
	
	private int _strutsAction;
	private int _id;
	private String _payload;
	private Date _senddate;
	private int _attempts;
	private int _sent;
	private String _sender;
	private String _recipient;
	
	private INotifyManager _notifyManager;
	
}