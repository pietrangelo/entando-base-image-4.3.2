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
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ICourseQueueDAO {

	public List<Integer> searchCourseQueues(FieldSearchFilter[] filters);

	public CourseQueue loadCourseQueue(int id);

	public List<Integer> loadCourseQueues();

	public void removeCourseQueue(int id);

	//public void updateCourseQueue(CourseQueue courseQueue);

	//public void insertCourseQueue(CourseQueue courseQueue);

	//public void insertCourseQueues(List<CourseQueue> list);
	public void insertCourseQueues(List<CourseQueue> list, Connection conn);

	
	public void removeUnsent(int subscriberid, Connection conn);

	/**
	 * returns a map where with:<br />key: CourseMail id<br />value: number of email sent
	 * @param courseId the course
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<Integer, Integer> loadOccurrencesByCourseMail(int courseId, Boolean sent);

	public List<Integer> loadCourseQueueBySubscriber(int subscriberId);

	
	/**
	 * Load the elements do be sent.
	 * @param courseId
	 * @param date
	 * @return
	 */
	public List<Integer> loadDeliveryCourseQueue(int courseId, Date date);

	/**
	 * 
	 * @param queueRecordsId 
	 * @param operationIndex
	 * @param string
	 * @param string2
	 */
	public void updateDeliveryCourseQueue(List<Integer> queueRecordsId, Integer operationIndex, String messageid, Integer messagestatus);

	public InputStream loadEmailsCSV(int id) throws Throwable;

}