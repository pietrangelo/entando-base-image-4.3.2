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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.subscriber;

import java.util.Date;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

public class SubscriberAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SubscriberAction.class);

	public String newSubscriber() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
		} catch (Throwable t) {
			_logger.error("error in newSubscriber", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String edit() {
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getId());
			if (null == subscriber) {
				this.addActionError(this.getText("error.subscriber.null"));
				return INPUT;
			}
			this.populateForm(subscriber);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			Subscriber subscriber = this.createSubscriber();
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getSubscriberManager().addSubscriber(subscriber);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getSubscriberManager().updateSubscriber(subscriber);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trash() {
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getId());
			if (null == subscriber) {
				this.addActionError(this.getText("error.subscriber.null"));
				return INPUT;
			}
			this.populateForm(subscriber);
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
				this.getSubscriberManager().deleteSubscriber(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String view() {
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getId());
			if (null == subscriber) {
				this.addActionError(this.getText("error.subscriber.null"));
				return INPUT;
			}
			this.populateForm(subscriber);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(Subscriber subscriber) throws Throwable {
		this.setId(subscriber.getId());
		this.setCourseId(subscriber.getCourseId());
		this.setName(subscriber.getName());
		this.setEmail(subscriber.getEmail());
		this.setHash(subscriber.getHash());
		this.setStatus(subscriber.getStatus());
		this.setCreatedat(subscriber.getCreatedat());
		this.setUpdatedat(subscriber.getUpdatedat());
	}
	
	private Subscriber createSubscriber() {
		Subscriber subscriber = new Subscriber();
		subscriber.setId(this.getId());
		subscriber.setCourseId(this.getCourseId());
		subscriber.setName(this.getName());
		subscriber.setEmail(this.getEmail());
		subscriber.setHash(this.getHash());
		subscriber.setStatus(this.getStatus());
		subscriber.setCreatedat(this.getCreatedat());
		subscriber.setUpdatedat(this.getUpdatedat());
		return subscriber;
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

	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}

	public String getHash() {
		return _hash;
	}
	public void setHash(String hash) {
		this._hash = hash;
	}

	public String getStatus() {
		return _status;
	}
	public void setStatus(String status) {
		this._status = status;
	}

	public Date getCreatedat() {
		return _createdat;
	}
	public void setCreatedat(Date createdat) {
		this._createdat = createdat;
	}

	public Date getUpdatedat() {
		return _updatedat;
	}
	public void setUpdatedat(Date updatedat) {
		this._updatedat = updatedat;
	}

	
	protected ISubscriberManager getSubscriberManager() {
		return _subscriberManager;
	}
	public void setSubscriberManager(ISubscriberManager subscriberManager) {
		this._subscriberManager = subscriberManager;
	}
	
	private int _strutsAction;
	private int _id;
	private int _courseId;
	private String _name;
	private String _email;
	private String _hash;
	private String _status;
	private Date _createdat;
	private Date _updatedat;
	
	private ISubscriberManager _subscriberManager;
	
}