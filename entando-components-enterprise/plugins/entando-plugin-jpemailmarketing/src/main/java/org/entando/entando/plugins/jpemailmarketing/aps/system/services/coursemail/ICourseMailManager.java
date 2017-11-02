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

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.Date;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ICourseMailManager {

	public CourseMail getCourseMail(int id) throws ApsSystemException;

	public List<Integer> getCourseMails() throws ApsSystemException;

	public List<Integer> searchCourseMails(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addCourseMail(CourseMail courseMail) throws ApsSystemException;

	public void updateCourseMail(CourseMail courseMail) throws ApsSystemException;

	public void deleteCourseMail(int id) throws ApsSystemException;

	public List<Integer> getCourseMails(int courseId) throws ApsSystemException;

	
	/**
	 * Returns the {@link CourseMail} that is meant to be sent today, according on the date parameter and the delayDays value.<br />
	 * In other words if (<b>date</b> + delayDay of one of the {@link CourseMail} in the course) is today, the relative object is returned
	 * @param courseId the id of the course to inspect
	 * @param date the date do be processed (typically the updatedat date of the subscriber)
	 * @return A courseMail, or null
	 * @throws ApsSystemException
	 */
	public CourseMail getCourseMailForToday(int courseId, Date date) throws ApsSystemException;
}