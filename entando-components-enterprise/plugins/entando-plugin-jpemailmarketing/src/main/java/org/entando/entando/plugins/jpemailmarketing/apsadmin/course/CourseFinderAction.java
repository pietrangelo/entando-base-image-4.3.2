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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.course;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

import com.agiletec.aps.system.common.FieldSearchFilter;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseDAO;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;

import com.agiletec.apsadmin.system.BaseAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseFinderAction.class);

	public List<Integer> getCoursesId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getId()) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("id"), this.getId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getName())) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(ICourseDAO.FILTER_NAME, this.getName(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getSender())) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("sender"), this.getSender(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getActive()) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(ICourseDAO.FILTER_ACTIVE, this.getActive(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getCronexp())) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("cronexp"), this.getCronexp(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getCrontimezoneid())) {
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("crontimezoneid"), this.getCrontimezoneid(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCreatedatStart() || null != this.getCreatedatEnd()) {
				Date startDate = this.getCreatedatStart();
				Date endDate = this.getCreatedatEnd();
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("createdat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getUpdatedatStart() || null != this.getUpdatedatEnd()) {
				Date startDate = this.getUpdatedatStart();
				Date endDate = this.getUpdatedatEnd();
				//TODO add a constant into your ICourseManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("updatedat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			List<Integer> courses = this.getCourseManager().searchCourses(filters);
			return courses;
		} catch (Throwable t) {
			_logger.error("Error getting courses list", t);
			throw new RuntimeException("Error getting courses list", t);
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

	public Course getCourse(int id) {
		Course course = null;
		try {
			course = this.getCourseManager().getCourse(id);
		} catch (Throwable t) {
			_logger.error("Error getting course with id {}", id, t);
			throw new RuntimeException("Error getting course with id " + id, t);
		}
		return course;
	}


	public Integer getId() {
		return _id;
	}
	public void setId(Integer id) {
		this._id = id;
	}


	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}


	public String getSender() {
		return _sender;
	}
	public void setSender(String sender) {
		this._sender = sender;
	}


	public Integer getActive() {
		return _active;
	}
	public void setActive(Integer active) {
		this._active = active;
	}


	public String getCronexp() {
		return _cronexp;
	}
	public void setCronexp(String cronexp) {
		this._cronexp = cronexp;
	}


	public String getCrontimezoneid() {
		return _crontimezoneid;
	}
	public void setCrontimezoneid(String crontimezoneid) {
		this._crontimezoneid = crontimezoneid;
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


	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}
	
	private Integer _id;
	private String _name;
	private String _sender;
	private Integer _active;
	private String _cronexp;
	private String _crontimezoneid;
	private Date _createdatStart;
	private Date _createdatEnd;
	private Date _updatedatStart;
	private Date _updatedatEnd;
	private ICourseManager _courseManager;
}