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
package org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.form;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;

public class FormSubscriberAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormSubscriberAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getCourseId(), this.getEmail());
			if (null != subscriber) {
				this.addActionError(this.getText("error.subscriber.exists"));
			}
		} catch (Throwable t) {
			_logger.error("Error in validate, t");
			throw new RuntimeException("error in validate");
		}
	}

	public String entryForm() {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			this.setJoinCourse(true);
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			this.setStrutsAction(ACTION_ENTRY);
		} catch (Throwable t) {
			_logger.error("error in entryForm", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String signup() {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				this.addActionError(this.getText("error.form.null"));
				return INPUT;
			}
			if (isJoinCourse()) {				
				this.getSubscriberManager().addSubscriber(this.getCourseId(), this.getName(), this.getEmail());
			} else {
				this.getSubscriberManager().addUnregisteredSubscriber(this.getCourseId(), this.getName(), this.getEmail());				
			}
			this.setStrutsAction(ACTION_SUCCESS);
		} catch (Throwable t) {
			_logger.error("error in signup", t);
			this.setStrutsAction(ACTION_ERROR);
			//return "signup_failure";
		}
		return SUCCESS;
	}

	/**
	 * genegeneralmente è il result di  org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.subscriber.SubscriberManageFrontAction.unsubscribe()
	 * quindi con possibile esecuzione dell'action.
	 * @return
	 */
	public String unsubscribe() {
		try {
			this.setStrutsAction(ACTION_UNSUBSCIBE_SUCCESS);
			Subscriber subscriber = this.getSubscriberManager().getSubscriber(this.getEmail(), this.getToken());
			if (null != subscriber) {
				if (subscriber.getStatus().equalsIgnoreCase(Subscriber.STATUS_CONFIRMED)) {
					subscriber.setStatus(Subscriber.STATUS_UNSUBSCRIBED);
					this.getSubscriberManager().unsubscribeSubscriber(subscriber);
				} else {
					_logger.trace("The subscriber with id:{} and email {} cannot be unsubscribed. Current status is {}", subscriber.getId(), subscriber.getEmail(), subscriber.getStatus());
				}
			} else {
				_logger.warn("No subscriber found with email {} and token {}", this.getEmail(), this.getToken());
				this.setStrutsAction(ACTION_UNSUBSCRIBE_ERROR);
			}

		} catch (Throwable t) {
			this.setStrutsAction(ACTION_UNSUBSCRIBE_ERROR);
			_logger.error("Error on unsubscribe subscriber {} and token {}", this.getEmail(), this.getToken(), t);
			//return null;
		}
		return SUCCESS;
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

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}

	protected ISubscriberManager getSubscriberManager() {
		return _subscriberManager;
	}
	public void setSubscriberManager(ISubscriberManager subscriberManager) {
		this._subscriberManager = subscriberManager;
	}

	public boolean isJoinCourse() {
		return _joinCourse;
	}
	public void setJoinCourse(boolean joinCourse) {
		this._joinCourse = joinCourse;
	}

	public String getToken() {
		return _token;
	}
	public void setToken(String token) {
		this._token = token;
	}

	private IFormManager _formManager;
	private int _courseId;
	private String _name;
	private String _email;
	private boolean _joinCourse;
	private int _strutsAction;
	private String _token;
	private ISubscriberManager _subscriberManager;

	public static final int ACTION_ENTRY = 100;
	public static final int ACTION_SUCCESS = 200;
	public static final int ACTION_UNSUBSCIBE_SUCCESS = 210;
	public static final int ACTION_ERROR = 400;
	public static final int ACTION_UNSUBSCRIBE_ERROR = 410;
}
