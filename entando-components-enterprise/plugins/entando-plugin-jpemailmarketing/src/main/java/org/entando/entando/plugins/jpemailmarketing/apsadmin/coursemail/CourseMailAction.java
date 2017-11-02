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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.ICourseMailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * This action handles the email's editing interface.
 * The interface contains a form for each mail.
 * The id field id the course field
 * The current is used the keep the panel open for INPUT result
 * 
 */
public class CourseMailAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseMailAction.class);

	@Override
	public void validate() {
		super.validate();
		try {
			CourseMail courseMail = this.getCourseMailManager().getCourseMail(this.getCourseMailId());
			if (null == courseMail) {
				this.addActionError(this.getText("error.courseMail.null"));
				return;
			}
			//check course
			Course course = this.getCourseManager().getCourse(courseMail.getCourseid());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return;
			}
			this.setCourse(course);
		} catch (Throwable t) {
			_logger.error("Error in validate", t);
			throw new RuntimeException("Error in validate", t);
		}
	}

	public String edit() {
		try {
			CourseMail courseMail = this.getCourseMailManager().getCourseMail(this.getCourseMailId());
			if (null == courseMail) {
				this.addActionError(this.getText("error.courseMail.null"));
				return INPUT;
			}
			this.populateForm(courseMail);
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String loadCourse() {
		try {
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return INPUT;
			}
			this.setCourse(course);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			//only update specific values on existing records
			int strutsAction = ApsAdminSystemConstants.EDIT;
			
			CourseMail courseMail = this.getCourseMailManager().getCourseMail(this.getCourseMailId());
			courseMail.setEmailSubject(this.getEmailSubject());
			courseMail.setEmailContent(this.getEmailContent());
			
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getCourseMailManager().addCourseMail(courseMail);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getCourseMailManager().updateCourseMail(courseMail);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	/*
	public String trash() {
		try {
			CourseMail courseMail = this.getCourseMailManager().getCourseMail(this.getId());
			if (null == courseMail) {
				this.addActionError(this.getText("error.courseMail.null"));
				return INPUT;
			}
			this.populateForm(courseMail);
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
				this.getCourseMailManager().deleteCourseMail(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			CourseMail courseMail = this.getCourseMailManager().getCourseMail(this.getId());
			if (null == courseMail) {
				this.addActionError(this.getText("error.courseMail.null"));
				return INPUT;
			}
			this.populateForm(courseMail);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}



	private CourseMail createCourseMail() {
		CourseMail courseMail = new CourseMail();
		courseMail.setId(this.getId());
		courseMail.setCourseid(this.getCourseid());
		courseMail.setDalayDays(this.getDalayDays());
		courseMail.setEmailSubject(this.getEmailSubject());
		courseMail.setEmailContent(this.getEmailContent());
		courseMail.setCreatedat(this.getCreatedat());
		courseMail.setUpdatedat(this.getUpdatedat());
		return courseMail;
	}
	 */

	private void populateForm(CourseMail courseMail) throws Throwable {
		this.setId(courseMail.getCourseid());
		this.setCourseMailId(courseMail.getId());
		this.setCourseid(courseMail.getCourseid());
		this.setPosition(courseMail.getPosition());
		this.setDalayDays(courseMail.getDalayDays());
		this.setEmailSubject(courseMail.getEmailSubject());
		this.setEmailContent(courseMail.getEmailContent());
		this.setCreatedat(courseMail.getCreatedat());
		this.setUpdatedat(courseMail.getUpdatedat());
		
		this.setSubscriberDelay(courseMail.getSubscriberDelay());
	}

	public Date getExecutionTime(int courseId) {
		Date date = null;
		try {
			Course course = this.getCourseManager().getCourse(courseId);
			if (null == course || course.getActive() != 1) return null;
			ScheduledFuture scheduledFuture = this.getCourseManager().getScheduler(courseId);
			Calendar cal = Calendar.getInstance();
			Long delay = scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
			cal.add(Calendar.MILLISECOND, delay.intValue());
			date = cal.getTime();
			
		} catch (Throwable t) {
			_logger.error("Error getting the execution time for te course {}", courseId, t);
			throw new RuntimeException("error in getExecutionTime");
		}
		return date;
	}

	public List<Integer> getCourseEmails(int courseId) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			list = this.getCourseMailManager().getCourseMails(courseId);
		} catch (Throwable t) {
			_logger.error("Error loading the course-email list for the course {}", courseId, t);
			throw new RuntimeException("Error loading the course-email list for the course " + courseId);
		}
		return list;
	}

	public CourseMail getCourseEmail(int id) {
		CourseMail courseMail = null;
		try {
			courseMail = this.getCourseMailManager().getCourseMail(id);
		} catch (Throwable t) {
			_logger.error("Error loading the course-email {}", id, t);
			throw new RuntimeException("Error loading the course-email" + id);
		}
		return courseMail;
	}

	public Course getCourse() {
		return _course;
	}
	public void setCourse(Course course) {
		this._course = course;
	}
	
	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public int getCourseMailId() {
		return _courseMailId;
	}
	public void setCourseMailId(int courseMailId) {
		this._courseMailId = courseMailId;
	}

	public int getCourseid() {
		return _courseid;
	}
	public void setCourseid(int courseid) {
		this._courseid = courseid;
	}

	public int getPosition() {
		return _position;
	}
	public void setPosition(int position) {
		this._position = position;
	}
	
	public int getDalayDays() {
		return _dalayDays;
	}
	public void setDalayDays(int dalayDays) {
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

	protected ICourseMailManager getCourseMailManager() {
		return _courseMailManager;
	}
	public void setCourseMailManager(ICourseMailManager courseMailManager) {
		this._courseMailManager = courseMailManager;
	}

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	/**
	 * this is the course id
	 * @return
	 */
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}


	public boolean isCurrentForm() {
		return _currentForm;
	}
	public void setCurrentForm(boolean currentForm) {
		this._currentForm = currentForm;
	}


	public int getSubscriberDelay() {
		return _subscriberDelay;
	}
	public void setSubscriberDelay(int subscriberDelay) {
		this._subscriberDelay = subscriberDelay;
	}


	private int _id;
	
	private int _strutsAction;
	private int _courseMailId;
	private int _courseid;
	private int _position;
	private int _dalayDays;
	private String _emailSubject;
	private String _emailContent;
	private Date _createdat;
	private Date _updatedat;

	private ICourseMailManager _courseMailManager;
	private ICourseManager _courseManager;
	private Course _course;
	private boolean _currentForm;

	private int _subscriberDelay;
}