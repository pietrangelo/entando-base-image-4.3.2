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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ICourseQueueManager {

	/**
	 * Create the queue list when a subscriber confirms. This does not persist the list
	 * @param subscriber
	 * @return the list of CourseQueue to be added (in tx)
	 * @throws ApsSystemException
	 */
	public List<CourseQueue> createQueue(Subscriber subscriber) throws ApsSystemException;
	
	public List<Integer> getCourseQueueBySubscriber(int id) throws ApsSystemException;
	
	
	
	public CourseQueue getCourseQueue(int id) throws ApsSystemException;

	@Deprecated
	public List<Integer> getCourseQueues() throws ApsSystemException;

	@Deprecated
	public List<Integer> searchCourseQueues(FieldSearchFilter filters[]) throws ApsSystemException;

//	@Deprecated
//	public void addCourseQueue(CourseQueue courseQueue) throws ApsSystemException;

//	@Deprecated
//	public void updateCourseQueue(CourseQueue courseQueue) throws ApsSystemException;

	@Deprecated
	public void deleteCourseQueue(int id) throws ApsSystemException;

	
	/**
	 * returns a map where with:<br />key: CourseMail id<br />value: number of email sent or unsent
	 * @param courseId the course
	 * @param sent boolean
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<Integer, Integer> getOccurrencesByCourseMail(int courseId, Boolean sent) throws ApsSystemException;

	public InputStream getEmailsCSV(int id) throws ApsSystemException;




}