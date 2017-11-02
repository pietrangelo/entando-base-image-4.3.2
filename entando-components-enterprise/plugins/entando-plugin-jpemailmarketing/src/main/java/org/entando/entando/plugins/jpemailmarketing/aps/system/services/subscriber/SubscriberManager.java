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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueue;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.ICourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.api.JAXBSubscriber;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.event.SubscriberChangedEvent;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.IMailgunManager;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailGunManager;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunMailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class SubscriberManager extends AbstractService implements ISubscriberManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SubscriberManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}

	@Override
	public Subscriber getSubscriber(int id) throws ApsSystemException {
		Subscriber subscriber = null;
		try {
			subscriber = this.getSubscriberDAO().loadSubscriber(id);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with id '{}'", id,  t);
			throw new ApsSystemException("Error loading subscriber with id: " + id, t);
		}
		return subscriber;
	}

	@Override
	public Subscriber getSubscriber(int courseId, String email) throws ApsSystemException {
		Subscriber subscriber = null;
		try {
			subscriber = this.getSubscriberDAO().loadSubscriber(courseId, email);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with courseid '{}' and email {}", courseId, email,  t);
			throw new ApsSystemException("Error loading subscriber by course/email: " + courseId + "/" + email, t);
		}
		return subscriber;
	}

	@Override
	public Subscriber getSubscriber(String email, String token) throws ApsSystemException {
		Subscriber subscriber = null;
		try {
			subscriber = this.getSubscriberDAO().loadSubscriber(email, token);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with email '{}' and token {}", email, token,  t);
			throw new ApsSystemException("Error loading subscriber by email/token: " + email+ "/" + token, t);
		}
		return subscriber;
	}

	@Override
	public List<Integer> getSubscribers() throws ApsSystemException {
		List<Integer> subscribers = new ArrayList<Integer>();
		try {
			subscribers = this.getSubscriberDAO().loadSubscribers();
		} catch (Throwable t) {
			_logger.error("Error loading Subscriber list",  t);
			throw new ApsSystemException("Error loading Subscriber ", t);
		}
		return subscribers;
	}

	@Override
	public List<Integer> searchSubscribers(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> subscribers = new ArrayList<Integer>();
		try {
			subscribers = this.getSubscriberDAO().searchSubscribers(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Subscribers", t);
			throw new ApsSystemException("Error searching Subscribers", t);
		}
		return subscribers;
	}

	@Override
	public void addSubscriber(Subscriber subscriber) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			subscriber.setId(key);
			Date now = new Date();
			subscriber.setCreatedat(now);
			subscriber.setUpdatedat(now);
			subscriber.setHash(RandomStringUtils.randomNumeric(8));
			MailgunMailResponse result = this.sendSubscriptionEmail(subscriber);
			if (null != result && result.getStatus() == MailgunMailResponse.SUCCESS) {
				this.getSubscriberDAO().insertSubscriber(subscriber);
				this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.INSERT_OPERATION_CODE);
			} else {
				//??
				_logger.error("Invalid result sending email to subscriber");
				throw new ApsSystemException("Invalid result sending email to subscriber");

			}
		} catch (Throwable t) {
			_logger.error("Error adding Subscriber", t);
			throw new ApsSystemException("Error adding Subscriber", t);
		}
	}

	@Override
	public void addSubscriber(int courseId, String name, String email) throws ApsSystemException {
		try {
			Subscriber subscriber = this.createSubscriberForInsert(courseId, name, email, Subscriber.STATUS_UNCONFIRMED);
			this.addSubscriber(subscriber);
		} catch (Throwable t) {
			_logger.error("Error adding Subscriber", t);
			throw new ApsSystemException("Error adding Subscriber", t);
		}
	}

	@Override
	public void addUnregisteredSubscriber(int courseId, String name, String email) throws ApsSystemException {
		try {
			Subscriber subscriber = this.createSubscriberForInsert(courseId, name, email, Subscriber.STATUS_UNREGISTERED);
			this.addSubscriber(subscriber);;
		} catch (Throwable t) {
			_logger.error("Error adding Unregistered Subscriber", t);
			throw new ApsSystemException("Error adding Unregistered Subscriber", t);
		}
	}

	private MailgunMailResponse sendSubscriptionEmail(Subscriber subscriber) throws ApsSystemException {
		Form form = this.getFormManager().getForm(subscriber.getCourseId());
		if (subscriber.getStatus().equalsIgnoreCase(Subscriber.STATUS_UNREGISTERED)) {
			_logger.debug("the subscriber {} - {} is unregistered for the course {}", subscriber.getId(), subscriber.getEmail(), subscriber.getCourseId());
			return null;
		}
		Course course = this.getCourseManager().getCourse(subscriber.getCourseId());
		List<String> addressList = new ArrayList<String>();
		addressList.add(subscriber.getPrettyEmailAddress());

		Map<String, Map<String, Object>> recipientVariables = new HashMap<String, Map<String,Object>>();
		recipientVariables.put(subscriber.getEmail(), new HashMap<String, Object>());
		String baseURL = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		if (baseURL.endsWith("/")) baseURL = StringUtils.removeEnd(baseURL, "/");
		StringBuffer sbBuffer = new StringBuffer(baseURL).append("/do/FrontEnd/jpemailmarketing/Subscriber/activate?").append("email=").append(subscriber.getEmail()).append("&token=").append(subscriber.getHash());
		recipientVariables.get(subscriber.getEmail()).put("jpemailmarketing_activatelink", sbBuffer.toString());
		recipientVariables.get(subscriber.getEmail()).put("jpemailmarketing_rewardButtonText", form.getEmailButton());
		recipientVariables.get(subscriber.getEmail()).put("jpemailmarketing_friendlyMessageText", form.getEmailMessageFriendly());

		MailgunMailResponse response = ((MailGunManager) this.getMailgunManager()).sendBatchWithRecipientVariables(course.getSender(), form.getEmailSubject(), form.getEmailText(), recipientVariables);
		return response;
	}


	protected Subscriber createSubscriberForInsert(int courseId, String name, String email, String status) {
		Subscriber subscriber = new Subscriber();
		subscriber.setCourseId(courseId);
		subscriber.setEmail(email);
		subscriber.setName(name);
		subscriber.setStatus(status);
		return subscriber;
	}

	/**
	 * 
	 * @param date
	 * @param key
	 * @return
	 */
	protected String generateHash(Date date, int key) {
		//TODO is very weak
		int value = new Long(new Date().getTime()).toString().hashCode();
		return RandomStringUtils.randomNumeric(10) + value;
	}

	@Override
	public void confirmSubscriber(Subscriber subscriber) throws ApsSystemException {
		try {
			Date now = new Date();
			subscriber.setUpdatedat(now);
			subscriber.setStatus(Subscriber.STATUS_CONFIRMED);
			List<CourseQueue> queue = this.getCourseQueueManager().createQueue(subscriber);
			this.getSubscriberDAO().confirmSubscriber(subscriber, queue);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error in confirm Subscriber", t);
			throw new ApsSystemException("Error in confirm Subscriber " + subscriber, t);
		}
	}

	@Override
	public void unsubscribeSubscriber(Subscriber subscriber) throws ApsSystemException {
		try {
			Date now = new Date();
			subscriber.setUpdatedat(now);
			subscriber.setStatus(Subscriber.STATUS_UNSUBSCRIBED);
			
			this.getSubscriberDAO().unsubscribeSubscriber(subscriber);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error in confirm Subscriber", t);
			throw new ApsSystemException("Error in confirm Subscriber " + subscriber, t);
		}
	}

	@Override
	public void updateSubscriber(Subscriber subscriber) throws ApsSystemException {
		try {
			Date now = new Date();
			subscriber.setUpdatedat(now);
			this.getSubscriberDAO().updateSubscriber(subscriber);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Subscriber", t);
			throw new ApsSystemException("Error updating Subscriber " + subscriber, t);
		}
	}

	@Override
	public void deleteSubscriber(int id) throws ApsSystemException {
		try {
			Subscriber subscriber = this.getSubscriber(id);
			this.getSubscriberDAO().removeSubscriber(id);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting Subscriber with id {}", id, t);
			throw new ApsSystemException("Error deleting Subscriber with id:" + id, t);
		}
	}

	@Override
	public Map<String, Integer> getOccurrencesBySubscriberStatus(int courseId, Collection<String> statuses) throws ApsSystemException {
		Map<String, Integer> occurrencesBySubscriberStatus = new HashMap<String, Integer>();
		try {
			occurrencesBySubscriberStatus = this.getSubscriberDAO().loadOccurrencesBySubscriberStatus(courseId, statuses);
		} catch (Throwable t) {
			_logger.error("Error searching occurrencesBySubscriberStatus for course {}", courseId, t);
			throw new ApsSystemException("Error searching occurrencesBySubscriberStatus for course " + courseId, t);
		}
		return occurrencesBySubscriberStatus;
	}

	@Override
	public List<Integer> getSubscribersByStatus(int courseId, Collection<String> statuses) throws ApsSystemException {
		List<Integer> subscriberByStatus = new ArrayList<Integer>();
		try {
			subscriberByStatus = this.getSubscriberDAO().loadSubscribersByStatus(courseId, statuses);
		} catch (Throwable t) {
			_logger.error("Error searching subscriberByStatus for course {}", courseId, t);
			throw new ApsSystemException("Error searching subscriberByStatus for course " + courseId, t);
		}
		return subscriberByStatus;
	}

	@Override
	public InputStream getSubscriberCSV(int courseId) throws ApsSystemException {
		try {
			return this.getSubscriberDAO().loadSubscriberCSV(courseId);
		} catch (Throwable t) {
			_logger.error("Error loading CSV for course {}", courseId, t);
			throw new RuntimeException("Error loading CSV for course " + courseId, t);
		}
	}
	
	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/subscribers?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBSubscriber> getSubscribersForApi(Properties properties) throws Throwable {
		List<JAXBSubscriber> list = new ArrayList<JAXBSubscriber>();
		List<Integer> idList = this.getSubscribers();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> subscriberIterator = idList.iterator();
			while (subscriberIterator.hasNext()) {
				int currentid = subscriberIterator.next();
				Subscriber subscriber = this.getSubscriber(currentid);
				if (null != subscriber) {
					list.add(new JAXBSubscriber(subscriber));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/subscriber?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBSubscriber getSubscriberForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		JAXBSubscriber jaxbSubscriber = null;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		Subscriber subscriber = this.getSubscriber(id);
		if (null == subscriber) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Subscriber with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		jaxbSubscriber = new JAXBSubscriber(subscriber);
		return jaxbSubscriber;
	}

	/**
	 * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/subscriber 
	 * @param jaxbSubscriber
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void addSubscriberForApi(JAXBSubscriber jaxbSubscriber) throws ApiException, ApsSystemException {
		if (null != this.getSubscriber(jaxbSubscriber.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Subscriber with id " + jaxbSubscriber.getId() + " already exists", Response.Status.CONFLICT);
		}
		Subscriber subscriber = jaxbSubscriber.getSubscriber();
		this.addSubscriber(subscriber);
	}

	/**
	 * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/subscriber 
	 * @param jaxbSubscriber
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void updateSubscriberForApi(JAXBSubscriber jaxbSubscriber) throws ApiException, ApsSystemException {
		if (null == this.getSubscriber(jaxbSubscriber.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Subscriber with id " + jaxbSubscriber.getId() + " does not exist", Response.Status.CONFLICT);
		}
		Subscriber subscriber = jaxbSubscriber.getSubscriber();
		this.updateSubscriber(subscriber);
	}

	/**
	 * DELETE http://localhost:8080/<portal>/api/rs/en/subscriber?id=1
	 * @param properties
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void deleteSubscriberForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		this.deleteSubscriber(id);
	}

	protected void notifySubscriberChangedEvent(Subscriber subscriber, int operationCode) {
		SubscriberChangedEvent event = new SubscriberChangedEvent();
		event.setSubscriber(subscriber);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setSubscriberDAO(ISubscriberDAO subscriberDAO) {
		this._subscriberDAO = subscriberDAO;
	}
	protected ISubscriberDAO getSubscriberDAO() {
		return _subscriberDAO;
	}

	protected IMailgunManager getMailgunManager() {
		return _mailgunManager;
	}
	public void setMailgunManager(IMailgunManager mailgunManager) {
		this._mailgunManager = mailgunManager;
	}

	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}

	protected ICourseQueueManager getCourseQueueManager() {
		return _courseQueueManager;
	}
	public void setCourseQueueManager(ICourseQueueManager courseQueueManager) {
		this._courseQueueManager = courseQueueManager;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ISubscriberDAO _subscriberDAO;
	private IMailgunManager _mailgunManager;
	private IFormManager _formManager;
	private ConfigInterface _configManager;
	private ICourseManager _courseManager;
	private ICourseQueueManager _courseQueueManager;

}
