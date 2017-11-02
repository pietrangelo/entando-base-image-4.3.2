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

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface ISubscriberManager {

	public Subscriber getSubscriber(int id) throws ApsSystemException;

	public Subscriber getSubscriber(int courseId, String email) throws ApsSystemException;

	public Subscriber getSubscriber(String email, String token) throws ApsSystemException;

	public List<Integer> getSubscribers() throws ApsSystemException;

	public List<Integer> searchSubscribers(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addSubscriber(int courseId, String name, String email) throws ApsSystemException;

	public void addUnregisteredSubscriber(int courseId, String name, String email) throws ApsSystemException;

	public void addSubscriber(Subscriber subscriber) throws ApsSystemException;

	public void updateSubscriber(Subscriber subscriber) throws ApsSystemException;

	public void confirmSubscriber(Subscriber subscriber) throws ApsSystemException;
	
	public void unsubscribeSubscriber(Subscriber subscriber) throws ApsSystemException;

	public void deleteSubscriber(int id) throws ApsSystemException;


	/**
	 * returns a map where with:<br />key: status<br />value: number of subscribers with that status
	 * @param courseId thr course
	 * @param statuses a collection of subscriber status: <li>unconfirmed<li>confirmed<li>unsubscribed<li>unregistered (those that are not in the flow)
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getOccurrencesBySubscriberStatus(int courseId, Collection<String> statuses) throws ApsSystemException;

	public InputStream getSubscriberCSV(int courseId) throws ApsSystemException;
	
	/**
	 * Returns the list of the subscribers
	 * @param courseId the course
	 * @param statuses a collection of subscriber status: <li>unconfirmed<li>confirmed<li>unsubscribed<li>unregistered (those that are not in the flow)
	 * @return
	 * @throws ApsSystemException
	 */
	public List<Integer> getSubscribersByStatus(int courseId, Collection<String> statuses) throws ApsSystemException;

}