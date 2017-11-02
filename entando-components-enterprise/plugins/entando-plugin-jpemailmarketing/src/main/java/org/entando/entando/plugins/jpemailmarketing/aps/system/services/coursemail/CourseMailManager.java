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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail;

import org.apache.commons.lang.time.DateUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.event.CourseMailChangedEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.api.JAXBCourseMail;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

import java.util.Date;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseMailManager extends AbstractService implements ICourseMailManager {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseMailManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}
	
	/**
	 * Estrae, se presente, il CM che cade oggi in funzione della data passata come parametro piu i delaydays
	 * @param courseId
	 * @param date la data sulla quale effettuare il calcolo. la data di 
	 * @return
	 * @throws ApsSystemException
	 */
	public CourseMail getCourseMailForToday(int courseId, Date date) throws ApsSystemException {
		CourseMail mailToSend = null;
		List<Integer> cmid = this.getCourseMails(courseId);
		if (null != cmid && !cmid.isEmpty()) {
			Iterator<Integer> it = cmid.iterator();
			while (it.hasNext()) {
				int id = it.next();
				CourseMail cm = this.getCourseMail(id);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_MONTH, cm.getDalayDays());
				if (DateUtils.isSameDay(cal.getTime(), new Date())) {
					mailToSend = cm;
					break;
				}
			}
		}
		return mailToSend;
	}

	@Override
	public CourseMail getCourseMail(int id) throws ApsSystemException {
		CourseMail courseMail = null;
		try {
			courseMail = this.getCourseMailDAO().loadCourseMail(id);
		} catch (Throwable t) {
			_logger.error("Error loading courseMail with id '{}'", id,  t);
			throw new ApsSystemException("Error loading courseMail with id: " + id, t);
		}
		return courseMail;
	}

	@Override
	public List<Integer> getCourseMails() throws ApsSystemException {
		List<Integer> courseMails = new ArrayList<Integer>();
		try {
			courseMails = this.getCourseMailDAO().loadCourseMails();
		} catch (Throwable t) {
			_logger.error("Error loading CourseMail list",  t);
			throw new ApsSystemException("Error loading CourseMail ", t);
		}
		return courseMails;
	}

	@Override
	public List<Integer> getCourseMails(int courseId) throws ApsSystemException {
		List<Integer> courseMails = new ArrayList<Integer>();
		try {
			courseMails = this.getCourseMailDAO().loadCourseMails(courseId);
		} catch (Throwable t) {
			_logger.error("Error loading CourseMail list for course {}", courseId,  t);
			throw new ApsSystemException("Error loading CourseMail for course " + courseId, t);
		}
		return courseMails;
	}

	@Override
	public List<Integer> searchCourseMails(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> courseMails = new ArrayList<Integer>();
		try {
			courseMails = this.getCourseMailDAO().searchCourseMails(filters);
		} catch (Throwable t) {
			_logger.error("Error searching CourseMails", t);
			throw new ApsSystemException("Error searching CourseMails", t);
		}
		return courseMails;
	}

	@Override
	public void addCourseMail(CourseMail courseMail) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			courseMail.setId(key);
			this.getCourseMailDAO().insertCourseMail(courseMail);
			this.notifyCourseMailChangedEvent(courseMail, CourseMailChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding CourseMail", t);
			throw new ApsSystemException("Error adding CourseMail", t);
		}
	}

	@Override
	public void updateCourseMail(CourseMail courseMail) throws ApsSystemException {
		try {
			Date now = new Date();
			courseMail.setUpdatedat(now);
			this.getCourseMailDAO().updateCourseMail(courseMail);
			this.notifyCourseMailChangedEvent(courseMail, CourseMailChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating CourseMail", t);
			throw new ApsSystemException("Error updating CourseMail " + courseMail, t);
		}
	}

	@Override
	public void deleteCourseMail(int id) throws ApsSystemException {
		try {
			CourseMail courseMail = this.getCourseMail(id);
			this.getCourseMailDAO().removeCourseMail(id);
			this.notifyCourseMailChangedEvent(courseMail, CourseMailChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting CourseMail with id {}", id, t);
			throw new ApsSystemException("Error deleting CourseMail with id:" + id, t);
		}
	}


	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/courseMails?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBCourseMail> getCourseMailsForApi(Properties properties) throws Throwable {
		List<JAXBCourseMail> list = new ArrayList<JAXBCourseMail>();
		List<Integer> idList = this.getCourseMails();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> courseMailIterator = idList.iterator();
			while (courseMailIterator.hasNext()) {
				int currentid = courseMailIterator.next();
				CourseMail courseMail = this.getCourseMail(currentid);
				if (null != courseMail) {
					list.add(new JAXBCourseMail(courseMail));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/courseMail?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBCourseMail getCourseMailForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		JAXBCourseMail jaxbCourseMail = null;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		CourseMail courseMail = this.getCourseMail(id);
		if (null == courseMail) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "CourseMail with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		jaxbCourseMail = new JAXBCourseMail(courseMail);
		return jaxbCourseMail;
	}

	/**
	 * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/courseMail 
	 * @param jaxbCourseMail
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void addCourseMailForApi(JAXBCourseMail jaxbCourseMail) throws ApiException, ApsSystemException {
		if (null != this.getCourseMail(jaxbCourseMail.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "CourseMail with id " + jaxbCourseMail.getId() + " already exists", Response.Status.CONFLICT);
		}
		CourseMail courseMail = jaxbCourseMail.getCourseMail();
		this.addCourseMail(courseMail);
	}

	/**
	 * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/courseMail 
	 * @param jaxbCourseMail
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void updateCourseMailForApi(JAXBCourseMail jaxbCourseMail) throws ApiException, ApsSystemException {
		if (null == this.getCourseMail(jaxbCourseMail.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "CourseMail with id " + jaxbCourseMail.getId() + " does not exist", Response.Status.CONFLICT);
		}
		CourseMail courseMail = jaxbCourseMail.getCourseMail();
		this.updateCourseMail(courseMail);
	}

	/**
	 * DELETE http://localhost:8080/<portal>/api/rs/en/courseMail?id=1
	 * @param properties
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void deleteCourseMailForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		this.deleteCourseMail(id);
	}

	private void notifyCourseMailChangedEvent(CourseMail courseMail, int operationCode) {
		CourseMailChangedEvent event = new CourseMailChangedEvent();
		event.setCourseMail(courseMail);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setCourseMailDAO(ICourseMailDAO courseMailDAO) {
		this._courseMailDAO = courseMailDAO;
	}
	protected ICourseMailDAO getCourseMailDAO() {
		return _courseMailDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ICourseMailDAO _courseMailDAO;
}
