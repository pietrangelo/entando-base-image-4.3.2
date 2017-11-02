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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueue;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISubscriberDAO {

	public List<Integer> searchSubscribers(FieldSearchFilter[] filters);
	
	public Subscriber loadSubscriber(int id);

	public List<Integer> loadSubscribers();

	public void removeSubscriber(int id);
	
	public void updateSubscriber(Subscriber subscriber);

	/**
	 * writes the email queue
	 * @param subscriber
	 */
	public void confirmSubscriber(Subscriber subscriber, List<CourseQueue> queue);
	
	/**
	 * deletes unsent queue
	 * @param id
	 */
	public void unsubscribeSubscriber(Subscriber subscriber);

	public void insertSubscriber(Subscriber subscriber);

	public Subscriber loadSubscriber(int courseId, String email);

	public Subscriber loadSubscriber(String email, String token);
	

	/**
	 * returns a map where with:<br />key: status<br />value: number of subscribers with that status
	 * @param courseId thr course
	 * @param statuses a collection of subscriber status: <li>unconfirmed<li>confirmed<li>unsubscribed<li>unregistered (those that are not in the flow)
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> loadOccurrencesBySubscriberStatus(int courseId,	Collection<String> statuses);

	/**
	 * Returns the list of the subscribers
	 * @param courseId the course
	 * @param statuses a collection of subscriber status: <li>unconfirmed<li>confirmed<li>unsubscribed<li>unregistered (those that are not in the flow)
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> loadSubscribersByStatus(int courseId, Collection<String> statuses);

	public InputStream loadSubscriberCSV(int courseId) throws Throwable;


}