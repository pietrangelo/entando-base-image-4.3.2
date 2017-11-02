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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.apsadmin.system.BaseAction;

public class SubscriberFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(SubscriberFinderAction.class);

	public List<Integer> getSubscribersId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCourseId()) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("courseId"), this.getCourseId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getName())) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("name"), this.getName(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmail())) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("email"), this.getEmail(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getHash())) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("hash"), this.getHash(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getStatus())) {
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("status"), this.getStatus(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCreatedatStart() || null != this.getCreatedatEnd()) {
				Date startDate = this.getCreatedatStart();
				Date endDate = this.getCreatedatEnd();
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("createdat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getUpdatedatStart() || null != this.getUpdatedatEnd()) {
				Date startDate = this.getUpdatedatStart();
				Date endDate = this.getUpdatedatEnd();
				//TODO add a constant into your ISubscriberManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("updatedat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			List<Integer> subscribers = this.getSubscriberManager().searchSubscribers(filters);
			return subscribers;
		} catch (Throwable t) {
			_logger.error("Error getting subscribers list", t);
			throw new RuntimeException("Error getting subscribers list", t);
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

	public Subscriber getSubscriber(int id) {
		Subscriber subscriber = null;
		try {
			subscriber = this.getSubscriberManager().getSubscriber(id);
		} catch (Throwable t) {
			_logger.error("Error getting subscriber with id {}", id, t);
			throw new RuntimeException("Error getting subscriber with id " + id, t);
		}
		return subscriber;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public Integer getCourseId() {
		return _courseId;
	}
	public void setCourseId(Integer courseId) {
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


	public Date getCreatedatStart() {
		return _createdatStart;
	}
	public void setCreatedatStart(Date createdatStart) {
		this._createdatStart = createdatStart;
	}


	public Date getCreatedatEnd() {
		return _createdatEnd;
	}
	public void setCreatedatEnd(Date createdatEnd) {
		this._createdatEnd = createdatEnd;
	}


	public Date getUpdatedatStart() {
		return _updatedatStart;
	}
	public void setUpdatedatStart(Date updatedatStart) {
		this._updatedatStart = updatedatStart;
	}


	public Date getUpdatedatEnd() {
		return _updatedatEnd;
	}
	public void setUpdatedatEnd(Date updatedatEnd) {
		this._updatedatEnd = updatedatEnd;
	}


	protected ISubscriberManager getSubscriberManager() {
		return _subscriberManager;
	}
	public void setSubscriberManager(ISubscriberManager subscriberManager) {
		this._subscriberManager = subscriberManager;
	}
	
	private Integer _id;
	private Integer _courseId;
	private String _name;
	private String _email;
	private String _hash;
	private String _status;
	private Date _createdatStart;
	private Date _createdatEnd;
	private Date _updatedatStart;
	private Date _updatedatEnd;
	private ISubscriberManager _subscriberManager;
}