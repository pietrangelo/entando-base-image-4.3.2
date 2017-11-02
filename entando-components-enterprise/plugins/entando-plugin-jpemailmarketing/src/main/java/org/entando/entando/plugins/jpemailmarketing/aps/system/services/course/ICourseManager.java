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

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ICourseManager {

	public Course getCourse(int id) throws ApsSystemException;

	public List<Integer> getCourses() throws ApsSystemException;

	public List<Integer> searchCourses(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addCourse(Course course) throws ApsSystemException;

	public void updateCourse(Course course) throws ApsSystemException;

	public void deleteCourse(int id) throws ApsSystemException;
	
	public ScheduledFuture getScheduler(int couseId);
	
	public void notifyCourseTriggerEvent(Course course);

}