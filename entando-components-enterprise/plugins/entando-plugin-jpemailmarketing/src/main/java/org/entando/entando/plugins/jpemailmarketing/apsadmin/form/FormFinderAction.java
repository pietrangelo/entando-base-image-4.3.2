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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.form;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.apsadmin.system.BaseAction;

public class FormFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormFinderAction.class);

	public List<Integer> getFormsId() {
		try {
			FieldSearchFilter[] filters = new FieldSearchFilter[0];
			if (null != this.getCourseId()) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("courseId"), this.getCourseId(), false);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getFileCoverName())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("fileCoverName"), this.getFileCoverName(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getFileIncentiveName())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("fileIncentiveName"), this.getFileIncentiveName(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailSubject())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailSubject"), this.getEmailSubject(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailText())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailText"), this.getEmailText(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailButton())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailButton"), this.getEmailButton(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (StringUtils.isNotBlank(this.getEmailMessageFriendly())) {
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("emailMessageFriendly"), this.getEmailMessageFriendly(), true);
				filters = this.addFilter(filters, filterToAdd);
			}
			if (null != this.getCreatedatStart() || null != this.getCreatedatEnd()) {
				Date startDate = this.getCreatedatStart();
				Date endDate = this.getCreatedatEnd();
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("createdat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			if (null != this.getUpdatedatStart() || null != this.getUpdatedatEnd()) {
				Date startDate = this.getUpdatedatStart();
				Date endDate = this.getUpdatedatEnd();
				//TODO add a constant into your IFormManager class
				FieldSearchFilter filterToAdd = new FieldSearchFilter(("updatedat"), startDate, endDate);
				filters = this.addFilter(filters, filterToAdd);
			}

			List<Integer> forms = this.getFormManager().searchForms(filters);
			return forms;
		} catch (Throwable t) {
			_logger.error("Error getting forms list", t);
			throw new RuntimeException("Error getting forms list", t);
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

	public Form getForm(int courseId) {
		Form form = null;
		try {
			form = this.getFormManager().getForm(courseId);
		} catch (Throwable t) {
			_logger.error("Error getting form with courseId {}", courseId, t);
			throw new RuntimeException("Error getting form with courseId " + courseId, t);
		}
		return form;
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

	public Integer getCourseId() {
		return _courseId;
	}
	public void setCourseId(Integer courseId) {
		this._courseId = courseId;
	}


	public String getFileCoverName() {
		return _fileCoverName;
	}
	public void setFileCoverName(String fileCoverName) {
		this._fileCoverName = fileCoverName;
	}


	public String getFileIncentiveName() {
		return _fileIncentiveName;
	}
	public void setFileIncentiveName(String fileIncentiveName) {
		this._fileIncentiveName = fileIncentiveName;
	}


	public String getEmailSubject() {
		return _emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this._emailSubject = emailSubject;
	}


	public String getEmailText() {
		return _emailText;
	}
	public void setEmailText(String emailText) {
		this._emailText = emailText;
	}


	public String getEmailButton() {
		return _emailButton;
	}
	public void setEmailButton(String emailButton) {
		this._emailButton = emailButton;
	}


	public String getEmailMessageFriendly() {
		return _emailMessageFriendly;
	}
	public void setEmailMessageFriendly(String emailMessageFriendly) {
		this._emailMessageFriendly = emailMessageFriendly;
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


	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	private Integer _courseId;
	private String _fileCoverName;
	private String _fileIncentiveName;
	private String _emailSubject;
	private String _emailText;
	private String _emailButton;
	private String _emailMessageFriendly;
	private Date _createdatStart;
	private Date _createdatEnd;
	private Date _updatedatStart;
	private Date _updatedatEnd;
	private IFormManager _formManager;
	private ICourseManager _courseManager;
}