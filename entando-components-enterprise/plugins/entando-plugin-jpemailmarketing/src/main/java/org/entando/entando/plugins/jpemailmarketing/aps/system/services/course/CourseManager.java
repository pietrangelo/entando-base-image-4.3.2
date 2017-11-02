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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpemailmarketing.aps.system.JpEmailmarketingSystemConstant;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.api.JAXBCourse;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.event.CourseChangedEvent;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.event.CourseTriggerEvent;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.portal.specialwidget.form.FormConfigAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.system.services.page.events.PageChangedObserver;
import com.agiletec.aps.util.ApsProperties;


public class CourseManager extends AbstractService implements ICourseManager, PageChangedObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseManager.class);

	@Override
	public void init() throws Exception {
		this.startCourseScheduler();
		_logger.debug("{} ready.", this.getClass().getName());
	}

	
	private void startCourseScheduler() throws Exception {
		//_logger.info("STARTCOURSESCHEDULER");
		this._runningCourses.clear();
		List<Integer> courses = this.getActiveCourses();
		if (null == courses) return;
		Iterator<Integer> it = courses.iterator();
		while (it.hasNext()) {
			int id = it.next();
			Course course = this.getCourse(id);
			this.scheduleCourse(course);
		}
		_logger.info("There are {} courses running: {}", this._runningCourses.size(), StringUtils.join(this._runningCourses.keySet(), ","));
	}

	@SuppressWarnings("rawtypes")
	protected void scheduleCourse(Course course) {
		MockSender mock_sender = new MockSender(course, this);
		ConcurrentTaskScheduler scheduler = new ConcurrentTaskScheduler();
		Trigger trigger = new CronTrigger(course.getCronexp(), TimeZone.getTimeZone(course.getCrontimezoneid()));
		ScheduledFuture scheduledFuture = scheduler.schedule(mock_sender, trigger);
		this._runningCourses.put(course.getId(), scheduledFuture);
		_logger.info("The course {} is been scheduled", course.getId());
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected void release() {
		super.release();
		Iterator it = this._runningCourses.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			int courseId = (Integer) pairs.getKey();
			ScheduledFuture scheduledFuture = (ScheduledFuture) pairs.getValue();
			boolean ok = scheduledFuture.cancel(true);
			_logger.info("The scheduler of the course {} is been removed with status {}", courseId, ok);
			this._runningCourses.remove(courseId);
		}
	}

	/**
	 * When a Form is being published the relative course is activated automatically.
	 * When a Form is being unpublished, nothing happens.
	 * 
	 */
	@Override
	public void updateFromPageChanged(PageChangedEvent event) {
		if (event.getOperationCode() != PageChangedEvent.EDIT_FRAME_OPERATION_CODE) return;
		if (0 != event.getFramePosition()) {
			IPage page = event.getPage();
			if (page == null) return;
			if (null != page.getWidgets()[event.getFramePosition()]) {
				String wtc = page.getWidgets()[event.getFramePosition()].getType().getCode();
				if (wtc.equalsIgnoreCase(JpEmailmarketingSystemConstant.WIDGET_FORM_TYPECODE)) {
					ApsProperties wConfig = page.getWidgets()[event.getFramePosition()].getConfig();
					String courseId = null;
					if (null != wConfig) {
						courseId = (String) wConfig.get(FormConfigAction.CONFIG_PARAM_COURSEID);						
					}
					if (StringUtils.isNumeric(courseId)) {
						try {
							Course course = this.getCourse(new Integer(courseId).intValue());
							course.setActive(1);
							this.updateCourse(course);
							_logger.info("The course {} - {}, is been activated due the form publishing in the page {}", course.getId(), course.getName(), page.getCode());
						} catch (Throwable t) {
							_logger.error("Error activating the course {} after page {} changed",courseId, page.getCode(), t);
							throw new RuntimeException("Error activating the course " + courseId + " in updateFromPageChanged");
						}
					} else {
						_logger.warn("The widget {} in the page {}, pos: {} does not contain a valid courseId", JpEmailmarketingSystemConstant.WIDGET_FORM_TYPECODE, page.getCode(), event.getFramePosition());
					}
				}
			}
		}
	}

	public List<Integer> getActiveCourses() throws ApsSystemException {
		List<Integer> courses = new ArrayList<Integer>();
		try {
			courses = this.getCourseDAO().loadActiveCourses();
		} catch (Throwable t) {
			_logger.error("Error loading active course list",  t);
			throw new ApsSystemException("Error loading active courses ", t);
		}
		return courses;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ScheduledFuture getScheduler(int couseId) {
		return this._runningCourses.get(couseId);
	}

	@Override
	public Course getCourse(int id) throws ApsSystemException {
		Course course = null;
		try {
			course = this.getCourseDAO().loadCourse(id);
		} catch (Throwable t) {
			_logger.error("Error loading course with id '{}'", id,  t);
			throw new ApsSystemException("Error loading course with id: " + id, t);
		}
		return course;
	}

	@Override
	public List<Integer> getCourses() throws ApsSystemException {
		List<Integer> courses = new ArrayList<Integer>();
		try {
			courses = this.getCourseDAO().loadCourses();
		} catch (Throwable t) {
			_logger.error("Error loading Course list",  t);
			throw new ApsSystemException("Error loading Course ", t);
		}
		return courses;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Integer> searchCourses(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> courses = new ArrayList<Integer>();
		try {
			courses = this.getCourseDAO().searchCourses(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Courses", t);
			throw new ApsSystemException("Error searching Courses", t);
		}
		return courses;
	}

	@Override
	public void addCourse(Course course) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			course.setId(key);
			Date now = new Date();
			course.setCreatedat(now);
			course.setUpdatedat(now);
			this.getCourseDAO().insertCourse(course);
			this.notifyCourseChangedEvent(course, CourseChangedEvent.INSERT_OPERATION_CODE);
			this.startCourseScheduler();
		} catch (Throwable t) {
			_logger.error("Error adding Course", t);
			throw new ApsSystemException("Error adding Course", t);
		}
	}

	@Override
	public void updateCourse(Course course) throws ApsSystemException {
		try {
			Date now = new Date();
			course.setUpdatedat(now);
			this.getCourseDAO().updateCourse(course);
			this.notifyCourseChangedEvent(course, CourseChangedEvent.UPDATE_OPERATION_CODE);
			this.startCourseScheduler();
		} catch (Throwable t) {
			_logger.error("Error updating Course", t);
			throw new ApsSystemException("Error updating Course " + course, t);
		}
	}

	@Override
	public void deleteCourse(int id) throws ApsSystemException {
		try {
			Course course = this.getCourse(id);
			this.getCourseDAO().removeCourse(id);
			this.notifyCourseChangedEvent(course, CourseChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting Course with id {}", id, t);
			throw new ApsSystemException("Error deleting Course with id:" + id, t);
		}
	}


	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/courses?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBCourse> getCoursesForApi(Properties properties) throws Throwable {
		List<JAXBCourse> list = new ArrayList<JAXBCourse>();
		List<Integer> idList = this.getCourses();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> courseIterator = idList.iterator();
			while (courseIterator.hasNext()) {
				int currentid = courseIterator.next();
				Course course = this.getCourse(currentid);
				if (null != course) {
					list.add(new JAXBCourse(course));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/course?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBCourse getCourseForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		JAXBCourse jaxbCourse = null;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		Course course = this.getCourse(id);
		if (null == course) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Course with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		jaxbCourse = new JAXBCourse(course);
		return jaxbCourse;
	}

	/**
	 * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/course 
	 * @param jaxbCourse
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void addCourseForApi(JAXBCourse jaxbCourse) throws ApiException, ApsSystemException {
		if (null != this.getCourse(jaxbCourse.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Course with id " + jaxbCourse.getId() + " already exists", Response.Status.CONFLICT);
		}
		Course course = jaxbCourse.getCourse();
		this.addCourse(course);
	}

	/**
	 * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/course 
	 * @param jaxbCourse
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void updateCourseForApi(JAXBCourse jaxbCourse) throws ApiException, ApsSystemException {
		if (null == this.getCourse(jaxbCourse.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Course with id " + jaxbCourse.getId() + " does not exist", Response.Status.CONFLICT);
		}
		Course course = jaxbCourse.getCourse();
		this.updateCourse(course);
	}

	/**
	 * DELETE http://localhost:8080/<portal>/api/rs/en/course?id=1
	 * @param properties
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void deleteCourseForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		this.deleteCourse(id);
	}

	private void notifyCourseChangedEvent(Course course, int operationCode) {
		CourseChangedEvent event = new CourseChangedEvent();
		event.setCourse(course);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	public void notifyCourseTriggerEvent(Course course) {
		_logger.info("notified CourseTriggerEvent");
		CourseTriggerEvent event = new CourseTriggerEvent();
		event.setCourseId(course.getId());
		this.notifyEvent(event);
	}


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setCourseDAO(ICourseDAO courseDAO) {
		this._courseDAO = courseDAO;
	}
	protected ICourseDAO getCourseDAO() {
		return _courseDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ICourseDAO _courseDAO;

	
	@SuppressWarnings("rawtypes")
	private Map<Integer, ScheduledFuture> _runningCourses = new HashMap<Integer, ScheduledFuture>();

}
