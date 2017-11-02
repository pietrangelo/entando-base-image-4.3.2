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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.ICourseMailManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.ICourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;

public class CourseStatisticAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseStatisticAction.class);
	
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
	
	public List<Integer> getSubscribers() throws ApsSystemException {
		List<Integer> subscribersList = new ArrayList<Integer>();
		try {
			Set<String> filter = this.getFilterStatus();
			filter.remove("");
			
			if (null == filter || filter.isEmpty()) {
				filter = new HashSet<String>(Arrays.asList(Subscriber.STATUS_SUBSCRIBERS));
			}
			subscribersList = this.getSubscriberManager().getSubscribersByStatus(this.getId(), filter);
		} catch (Throwable t) {
			_logger.error("error in getSubscribers", t);
		}
		return subscribersList;
	}

	public Map<String, Integer> getSubscribersOccurences() throws ApsSystemException {
		Map<String, Integer> subscribersList = new HashMap<String, Integer>();
		try {
			subscribersList = this.getSubscriberManager().getOccurrencesBySubscriberStatus(this.getId(), new HashSet<String>(Arrays.asList(Subscriber.STATUS_SUBSCRIBERS)));
		} catch (Throwable t) {
			_logger.error("error in getSubscribersOccurences", t);
		}
		return subscribersList;
	}

	
	public Map<Integer, Integer> getCourseMailOccurences() throws ApsSystemException {
		Map<Integer, Integer> subscribersList = new HashMap<Integer, Integer>();
		try {
			subscribersList = this.getCourseQueueManager().getOccurrencesByCourseMail(this.getId(), true);
		} catch (Throwable t) {
			_logger.error("error in getCourseMailOccurences", t);
		}
		return subscribersList;
	}

	public List<Integer> getCourseEmails() {
		List<Integer> list = new ArrayList<Integer>();
		try {
			list = this.getCourseMailManager().getCourseMails(this.getId());
		} catch (Throwable t) {
			_logger.error("Error loading the course-email list for the course {}", this.getId(), t);
			throw new RuntimeException("Error loading the course-email list for the course " + this.getId());
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

	public String exportSubscribers() throws ApsSystemException {
		try {
			this.setInputStream(this.getSubscriberManager().getSubscriberCSV(this.getId()));
		} catch (Throwable t) {
			_logger.error("error in exportSubscribers", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String exportEmails() throws ApsSystemException {
		try {
			this.setInputStream(this.getCourseQueueManager().getEmailsCSV(this.getId()));
		} catch (Throwable t) {
			_logger.error("error in exportEmails", t);
			return FAILURE;
		}
		return SUCCESS;
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

	
	public Course getCourse() {
		return _course;
	}
	public void setCourse(Course course) {
		this._course = course;
	}
	
	public InputStream getInputStream() {
		return _inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}

	public Set<String> getFilterStatus() {
		return _filterStatus;
	}
	public void setFilterStatus(Set<String> filterStatus) {
		this._filterStatus = filterStatus;
	}

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	protected ISubscriberManager getSubscriberManager() {
		return _subscriberManager;
	}
	public void setSubscriberManager(ISubscriberManager subscriberManager) {
		this._subscriberManager = subscriberManager;
	}

	protected ICourseQueueManager getCourseQueueManager() {
		return _courseQueueManager;
	}
	public void setCourseQueueManager(ICourseQueueManager courseQueueManager) {
		this._courseQueueManager = courseQueueManager;
	}

	protected ICourseMailManager getCourseMailManager() {
		return _courseMailManager;
	}
	public void setCourseMailManager(ICourseMailManager courseMailManager) {
		this._courseMailManager = courseMailManager;
	}


	private int _id;
	private Set<String> _filterStatus = new HashSet<String>();
	
	private Course _course;
	
	//download cvs reports
	private InputStream _inputStream;
	
	private ICourseManager _courseManager;
	private ISubscriberManager _subscriberManager;
	private ICourseQueueManager _courseQueueManager;
	private ICourseMailManager _courseMailManager;
	
}
