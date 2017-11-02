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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.coursemail;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.ICourseMailManager;
import com.agiletec.apsadmin.system.BaseAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseMailFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseMailFinderAction.class);

	public List<Integer> getCourseMailsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCourseid()) {
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("courseid"), this.getCourseid(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getDalayDays()) {
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("dalayDays"), this.getDalayDays(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailSubject())) {
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailSubject"), this.getEmailSubject(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailContent())) {
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailContent"), this.getEmailContent(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCreatedatStart() || null != this.getCreatedatEnd()) {
				Date startDate = this.getCreatedatStart();
				Date endDate = this.getCreatedatEnd();
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("createdat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getUpdatedatStart() || null != this.getUpdatedatEnd()) {
				Date startDate = this.getUpdatedatStart();
				Date endDate = this.getUpdatedatEnd();
				//TODO add a constant into your ICourseMailManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("updatedat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			List<Integer> courseMails = this.getCourseMailManager().searchCourseMails(filters);
			return courseMails;
		} catch (Throwable t) {
			_logger.error("Error getting courseMails list", t);
			throw new RuntimeException("Error getting courseMails list", t);
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

	public CourseMail getCourseMail(int id) {
		CourseMail courseMail = null;
		try {
			courseMail = this.getCourseMailManager().getCourseMail(id);
		} catch (Throwable t) {
			_logger.error("Error getting courseMail with id {}", id, t);
			throw new RuntimeException("Error getting courseMail with id " + id, t);
		}
		return courseMail;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public Integer getCourseid() {
		return _courseid;
	}
	public void setCourseid(Integer courseid) {
		this._courseid = courseid;
	}


	public Integer getDalayDays() {
		return _dalayDays;
	}
	public void setDalayDays(Integer dalayDays) {
		this._dalayDays = dalayDays;
	}


	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}


	public String getEmailContent() {
		return _emailContent;
	}
	public void setEmailContent(String emailContent) {
		this._emailContent = emailContent;
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


	protected ICourseMailManager getCourseMailManager() {
		return _courseMailManager;
	}
	public void setCourseMailManager(ICourseMailManager courseMailManager) {
		this._courseMailManager = courseMailManager;
	}

	private Integer _id;
	private Integer _courseid;
	private Integer _dalayDays;
	private String _emailSubject;
	private String _emailContent;
	private Date _createdatStart;
	private Date _createdatEnd;
	private Date _updatedatStart;
	private Date _updatedatEnd;
	private ICourseMailManager _courseMailManager;
}