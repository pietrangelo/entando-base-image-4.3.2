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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.ICourseMailManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.ICourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

public class CourseAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseAction.class);

	/**
	 * ONLY FOR DEBUG PURPOSES
	 */
	public String trigger() {
		Course course;
		try {
			course = this.getCourseManager().getCourse(this.getId());
			this.getCourseManager().notifyCourseTriggerEvent(course);
		} catch (ApsSystemException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String newCourse() {
		try {
			this.setStrutsAction(ApsAdminSystemConstants.ADD);
			if (StringUtils.isBlank(this.getCrontimezoneid())) this.setCrontimezoneid(this.getDefaultTimezoneId());
		} catch (Throwable t) {
			_logger.error("error in newCourse", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String edit() {
		try {
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return INPUT;
			}
			this.populateForm(course);
			if (StringUtils.isBlank(this.getCrontimezoneid())) this.setCrontimezoneid(this.getDefaultTimezoneId());
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);
		} catch (Throwable t) {
			_logger.error("error in edit", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			Course course = this.createCourse();
			String checkActive = this.checkActive();
			if (StringUtils.isNotBlank(checkActive)) return checkActive;
			int strutsAction = this.getStrutsAction();
			if (ApsAdminSystemConstants.ADD == strutsAction) {
				this.getCourseManager().addCourse(course);
			} else if (ApsAdminSystemConstants.EDIT == strutsAction) {
				this.getCourseManager().updateCourse(course);
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String forceSuspend() {
		try {
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return INPUT;
			}
			String checkActive = this.checkActive();
			if (StringUtils.isNotBlank(checkActive)) return checkActive;
			course.setActive(0);
			this.getCourseManager().updateCourse(course);
		} catch (Throwable t) {
			_logger.error("error in forceSuspend", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private String checkActive() {
		String retval = null;
		try {
			if (this.getActive() == 1) return null;
			if (StringUtils.isNotBlank(this.getForceSuspend()) && this.getForceSuspend().equalsIgnoreCase("true")) return null;
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) return null;
			if (course.getActive() == 0) return null;

			Map<Integer, Integer> unsentMails = this.getCourseQueueManager().getOccurrencesByCourseMail(course.getId(), false);
			if (null != unsentMails) {
				Iterator<Integer> it = unsentMails.values().iterator();
				while (it.hasNext()) {
					int currentValue = it.next();
					if (currentValue > 0) {
						retval = "course_running";
						this.setForceSuspend("true");
						break;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in checkActive", t);
			return FAILURE;
		}
		return retval;
	}

	public String trash() {
		try {
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return INPUT;
			}
			this.populateForm(course);
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
				this.getCourseManager().deleteCourse(this.getId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String view() {
		try {
			Course course = this.getCourseManager().getCourse(this.getId());
			if (null == course) {
				this.addActionError(this.getText("error.course.null"));
				return INPUT;
			}
			this.populateForm(course);
		} catch (Throwable t) {
			_logger.error("error in view", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	private void populateForm(Course course) throws Throwable {
		this.setId(course.getId());
		this.setName(course.getName());
		this.setSender(course.getSender());
		this.setActive(course.getActive());
		if (StringUtils.isBlank(course.getCronexp())) {
			course.setCronexp(TimeUtils.CRON_EVERY_DAY_MIDNIGHT);
		}
		this.setCronexp(course.getCronexp());
		String hour = TimeUtils.getCronValue(course.getCronexp(), TimeUtils.CRON_HOURS);
		String minute = TimeUtils.getCronValue(course.getCronexp(), TimeUtils.CRON_MINUTES);
		if (hour.trim().length() == 1) hour = StringUtils.leftPad(hour, 2, "0");
		if (minute.trim().length() == 1) minute = StringUtils.leftPad(minute, 2, "0");
		this.setCronHour(hour + ":" + minute);

		this.setCrontimezoneid(course.getCrontimezoneid());
		this.setCreatedat(course.getCreatedat());
		this.setUpdatedat(course.getUpdatedat());
	}

	private Course createCourse() {
		Course course = new Course();
		course.setId(this.getId());
		course.setName(this.getName());
		course.setSender(this.getSender());
		course.setActive(this.getActive());
		course.setCronexp(this.getCronexp());

		String[] values = this.getCronHour().split(":");

		String actualExpr = course.getCronexp();
		String newCronExpr = TimeUtils.updateCronValue(actualExpr, TimeUtils.CRON_HOURS, StringUtils.removeStart(values[0], "0"));
		newCronExpr = TimeUtils.updateCronValue(newCronExpr, TimeUtils.CRON_MINUTES, StringUtils.removeStart(values[1], "0"));
		course.setCronexp(newCronExpr);

		course.setCrontimezoneid(this.getCrontimezoneid());
		course.setCreatedat(this.getCreatedat());
		course.setUpdatedat(this.getUpdatedat());
		return course;
	}

	/**
	 * Only unsent
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<Integer, Integer> getCourseMailOccurences() throws ApsSystemException {
		Map<Integer, Integer> subscribersList = new HashMap<Integer, Integer>();
		try {
			subscribersList = this.getCourseQueueManager().getOccurrencesByCourseMail(this.getId(), false);
		} catch (Throwable t) {
			_logger.error("error in getCourseMailOccurences", t);
		}
		return subscribersList;
	}


	public String getDefaultTimezoneId() {
		return TimeZone.getDefault().getID();
	}

	public SelectItem getSelectedItemTimezone(String timezoneId) {
		int offset = TimeZone.getTimeZone(timezoneId).getRawOffset();
		SelectItem item = new SelectItem(timezoneId, timezoneId + " "  + TimeUtils.prettyPrintMinutes(offset));
		return item;
	}

	public List<SelectItem> getTimezones() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		String[] timezones = TimeZone.getAvailableIDs();
		//Locale locale = this.getLocale();
		for (int i = 0; i < timezones.length; i++) {
			String key = timezones[i];
			SelectItem selectItem = this.getSelectedItemTimezone(key);//new SelectItem(key, key + " +xx");
			items.add(selectItem);
		}
		return items;
	}

	@SuppressWarnings("rawtypes")
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

	public int getActive() {
		return _active;
	}
	public void setActive(int active) {
		this._active = active;
	}

	public String getCronexp() {
		return cronexp;
	}

	public void setCronexp(String cronexp) {
		this.cronexp = cronexp;
	}

	public String getCrontimezoneid() {
		return _crontimezoneid;
	}
	public void setCrontimezoneid(String crontimezoneid) {
		this._crontimezoneid = crontimezoneid;
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

	public String getCronHour() {
		return cronHour;
	}
	public void setCronHour(String cronHour) {
		this.cronHour = cronHour;
	}

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	protected ICourseMailManager getCourseMailManager() {
		return _courseMailManager;
	}
	public void setCourseMailManager(ICourseMailManager courseMailManager) {
		this._courseMailManager = courseMailManager;
	}


	protected ICourseQueueManager getCourseQueueManager() {
		return _courseQueueManager;
	}
	public void setCourseQueueManager(ICourseQueueManager courseQueueManager) {
		this._courseQueueManager = courseQueueManager;
	}


	public String getForceSuspend() {
		return forceSuspend;
	}

	public void setForceSuspend(String forceSuspend) {
		this.forceSuspend = forceSuspend;
	}



	private int _strutsAction;
	private int _id;
	private String _name;
	private String _sender;
	private int _active;
	private String cronexp;
	private String _crontimezoneid;
	private Date _createdat;
	private Date _updatedat;

	private String cronHour;

	private ICourseManager _courseManager;
	private ICourseMailManager _courseMailManager;
	private ICourseQueueManager _courseQueueManager;

	/**
	 * When 'true'. the checkActive method will be skipped
	 */
	private String forceSuspend;


}
