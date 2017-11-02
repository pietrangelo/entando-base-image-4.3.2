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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.event.CourseTriggerEvent;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.event.CourseTriggerObserver;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.ICourseMailManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.event.CourseQueueChangedEvent;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.IMailgunManager;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunMailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.util.DateConverter;


public class CourseQueueManager extends AbstractService implements ICourseQueueManager, CourseTriggerObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseQueueManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}

	public List<CourseQueue> createQueue(Subscriber subscriber) throws ApsSystemException {
		List<CourseQueue> list = new ArrayList<CourseQueue>();
		try {
			Date now = new Date();
			List<Integer> courseMailList = this.getCourseMailManager().getCourseMails(subscriber.getCourseId());
			for (int i = 0; i < courseMailList.size(); i++) {
				CourseMail currentCourseMail = this.getCourseMailManager().getCourseMail(courseMailList.get(i));
				CourseQueue courseQueue = new CourseQueue();
				courseQueue.setId(this.getKeyGeneratorManager().getUniqueKeyCurrentValue());
				courseQueue.setOperationindex(null);
				courseQueue.setSubscriberid(subscriber.getId());
				courseQueue.setCoursemailid(currentCourseMail.getId());
				courseQueue.setScheduledDate(DateUtils.addDays(subscriber.getUpdatedat(), currentCourseMail.getSubscriberDelay()));
				courseQueue.setSent(false);
				courseQueue.setCreatedat(now);
				courseQueue.setUpdatedat(now);
				list.add(courseQueue);
			}
		} catch (Throwable t) {
			_logger.error("Error in createQueue for subscriber {}", subscriber.getId() , t);
			throw new ApsSystemException("Error in createQueue for subscriber", t);
		}
		return list;
	}

	@Override
	public void updateFromCourseTrigger(CourseTriggerEvent event) {
		try {
			_logger.info("The scheduler fired the course with id:{}", event.getCourseId());
			this.sendMails(event.getCourseId());
		} catch (Throwable t) {
			_logger.error("Error in updateFromCourseTrigger for course: {}", event.getCourseId() , t);
		}
	}

	public void sendMails(int courseId) throws Throwable {
		this.sendMails(courseId, new Date());
	}

	@SuppressWarnings("rawtypes")
	public void sendMails(int courseId, Date date) throws Throwable {
		try {
			Course course = this.getCourseManager().getCourse(courseId);

			List<Integer> courseQueueList = this.getCourseQueueDAO().loadDeliveryCourseQueue(courseId, date);
			if (null == courseQueueList || courseQueueList.isEmpty()) {
				_logger.info("Nothing to send for course {} and date {}",courseId, DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
				return;
			}
			_logger.info("Found {} emails to send for course {}", courseQueueList.size(), courseId);

			Map<Integer, List<CourseQueue>> mapByEmailId = new HashMap<Integer, List<CourseQueue>>();
			Iterator<Integer> it = courseQueueList.iterator();
			while (it.hasNext()) {
				int courseQId = it.next();
				CourseQueue queueRecord = this.getCourseQueue(courseQId);
				int key = queueRecord.getCoursemailid();
				if (!mapByEmailId.containsKey(key)) {
					mapByEmailId.put(key, new ArrayList<CourseQueue>());
				}
				mapByEmailId.get(key).add(queueRecord);
			}
			
			Iterator it1 = mapByEmailId.entrySet().iterator();
			while (it1.hasNext()) {
				Map.Entry pairs = (Map.Entry)it1.next();
				int keyCourseMailId = (Integer) pairs.getKey();
				List<CourseQueue> courseQueueRecords = (List<CourseQueue>) pairs.getValue();
				List<Integer>courseQueueRecordForCourseMail = new ArrayList<Integer>();

				Map<String, Map<String, Object>> recipientVariables = new HashMap<String, Map<String,Object>>();
				for (int i = 0; i < courseQueueRecords.size(); i++) {
					courseQueueRecordForCourseMail.add(courseQueueRecords.get(i).getId());
					int subscriberId = courseQueueRecords.get(i).getSubscriberid();
					Subscriber subscriber = this.getSubscriberManager().getSubscriber(subscriberId);
					recipientVariables.put(subscriber.getEmail(), new HashMap<String, Object>());
					
					String unsubscribeURL = this.buildUsubscribeURL(subscriber);
					recipientVariables.get(subscriber.getEmail()).put("jpemailmarketing_unsubscribelink", unsubscribeURL);
				}
				int operationIndex = new Long(new Date().getTime()).hashCode();
				CourseMail courseMail = this.getCourseMailManager().getCourseMail(keyCourseMailId);
				_logger.info("sending email {}/{} to {} recipients: {}", courseMail.getId(), courseMail.getEmailSubject(), recipientVariables.keySet().size(), StringUtils.join(recipientVariables.keySet(), ", "));
				MailgunMailResponse response = this.getMailgunManager().sendBatchWithRecipientVariables(course.getSender(), courseMail.getEmailSubject(), courseMail.getEmailContent(), recipientVariables);
				if (response.getStatus() == MailgunMailResponse.SUCCESS) {
					this.getCourseQueueDAO().updateDeliveryCourseQueue(courseQueueRecordForCourseMail, operationIndex, response.getId(), response.getStatus());
				} else {
					_logger.warn("WARNING: email not sent. Mailgun response status is: {} Full response:", response.getStatus(), response.getCompleteJSONMessage());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting queue to send for course {}", courseId , t);
			throw new ApsSystemException("Error extracting queue to send for course " + courseId, t);
		}
	}

	protected String buildUsubscribeURL(Subscriber subscriber) {
		String baseURL = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		if (baseURL.endsWith("/")) baseURL = StringUtils.removeEnd(baseURL, "/");
		StringBuffer sbBuffer = new StringBuffer(baseURL).append("/do/FrontEnd/jpemailmarketing/Subscriber/unsubscribe?").append("email=").append(subscriber.getEmail()).append("&token=").append(subscriber.getHash());
		return sbBuffer.toString();
	}


	@Override
	public CourseQueue getCourseQueue(int id) throws ApsSystemException {
		CourseQueue courseQueue = null;
		try {
			courseQueue = this.getCourseQueueDAO().loadCourseQueue(id);
		} catch (Throwable t) {
			_logger.error("Error loading courseQueue with id '{}'", id,  t);
			throw new ApsSystemException("Error loading courseQueue with id: " + id, t);
		}
		return courseQueue;
	}

	@Override
	public List<Integer> getCourseQueues() throws ApsSystemException {
		List<Integer> courseQueues = new ArrayList<Integer>();
		try {
			courseQueues = this.getCourseQueueDAO().loadCourseQueues();
		} catch (Throwable t) {
			_logger.error("Error loading CourseQueue list",  t);
			throw new ApsSystemException("Error loading CourseQueue ", t);
		}
		return courseQueues;
	}

	@Override
	public List<Integer> getCourseQueueBySubscriber(int subscriberId) throws ApsSystemException {
		List<Integer> courseQueues = new ArrayList<Integer>();
		try {

			courseQueues = this.getCourseQueueDAO().loadCourseQueueBySubscriber(subscriberId);
		} catch (Throwable t) {
			_logger.error("Error loading CourseQueue list",  t);
			throw new ApsSystemException("Error loading CourseQueue ", t);
		}
		return courseQueues;
	}

	@Override
	public List<Integer> searchCourseQueues(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> courseQueues = new ArrayList<Integer>();
		try {
			courseQueues = this.getCourseQueueDAO().searchCourseQueues(filters);
		} catch (Throwable t) {
			_logger.error("Error searching CourseQueues", t);
			throw new ApsSystemException("Error searching CourseQueues", t);
		}
		return courseQueues;
	}

	//	@Override
	//	public void addCourseQueue(CourseQueue courseQueue) throws ApsSystemException {
	//		try {
	//			Date now = new Date();
	//			courseQueue.setCreatedat(now);
	//			courseQueue.setUpdatedat(now);
	//			courseQueue.setOperationindex(now.getTime());
	//			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
	//			courseQueue.setId(key);
	//			this.getCourseQueueDAO().insertCourseQueue(courseQueue);
	//			this.notifyCourseQueueChangedEvent(courseQueue, CourseQueueChangedEvent.INSERT_OPERATION_CODE);
	//		} catch (Throwable t) {
	//			_logger.error("Error adding CourseQueue", t);
	//			throw new ApsSystemException("Error adding CourseQueue", t);
	//		}
	//	}

	/*
	public void addCourseQueues(List<CourseQueue> list) throws ApsSystemException {
		try {
			Date now = new Date();
			for (int i = 0; i < list.size(); i++) {
				CourseQueue courseQueue = list.get(i);
				int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				courseQueue.setId(key);
				courseQueue.setCreatedat(now);
				courseQueue.setUpdatedat(now);
				courseQueue.setOperationindex(now.getTime());
			}

			this.getCourseQueueDAO().insertCourseQueues(list);
			//this.notifyCourseQueueChangedEvent(courseQueue, CourseQueueChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding CourseQueue", t);
			throw new ApsSystemException("Error adding CourseQueue", t);
		}
	}
	 */
	//	@Override
	//	public void updateCourseQueue(CourseQueue courseQueue) throws ApsSystemException {
	//		try {
	//			Date now = new Date();
	//			courseQueue.setUpdatedat(now);
	//			this.getCourseQueueDAO().updateCourseQueue(courseQueue);
	//			this.notifyCourseQueueChangedEvent(courseQueue, CourseQueueChangedEvent.UPDATE_OPERATION_CODE);
	//		} catch (Throwable t) {
	//			_logger.error("Error updating CourseQueue", t);
	//			throw new ApsSystemException("Error updating CourseQueue " + courseQueue, t);
	//		}
	//	}

	@Override
	public void deleteCourseQueue(int id) throws ApsSystemException {
		try {
			CourseQueue courseQueue = this.getCourseQueue(id);
			this.getCourseQueueDAO().removeCourseQueue(id);
			this.notifyCourseQueueChangedEvent(courseQueue, CourseQueueChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting CourseQueue with id {}", id, t);
			throw new ApsSystemException("Error deleting CourseQueue with id:" + id, t);
		}
	}

	@Override
	public Map<Integer, Integer> getOccurrencesByCourseMail(int courseId, Boolean sent) throws ApsSystemException {
		Map<Integer, Integer> occurrencesByCourseMail = new HashMap<Integer, Integer>();
		try {
			occurrencesByCourseMail = this.getCourseQueueDAO().loadOccurrencesByCourseMail(courseId, sent);
		} catch (Throwable t) {
			_logger.error("Error searching occurrencesByCourseMail for course {}", courseId, t);
			throw new ApsSystemException("Error searching occurrencesByCourseMail for course " + courseId, t);
		}
		return occurrencesByCourseMail;
	}

	@Override
	public InputStream getEmailsCSV(int id) throws ApsSystemException {
		try {
			return this.getCourseQueueDAO().loadEmailsCSV(id);
		} catch (Throwable t) {
			_logger.error("Error loading CSV for course {}", id, t);
			throw new RuntimeException("Error loading CSV for course " + id, t);
		}
	}
	

	private void notifyCourseQueueChangedEvent(CourseQueue courseQueue, int operationCode) {
		CourseQueueChangedEvent event = new CourseQueueChangedEvent();
		event.setCourseQueue(courseQueue);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setCourseQueueDAO(ICourseQueueDAO courseQueueDAO) {
		this._courseQueueDAO = courseQueueDAO;
	}
	protected ICourseQueueDAO getCourseQueueDAO() {
		return _courseQueueDAO;
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

	protected ICourseMailManager getCourseMailManager() {
		return _courseMailManager;
	}
	public void setCourseMailManager(ICourseMailManager courseMailManager) {
		this._courseMailManager = courseMailManager;
	}

	protected IMailgunManager getMailgunManager() {
		return _mailgunManager;
	}
	public void setMailgunManager(IMailgunManager mailgunManager) {
		this._mailgunManager = mailgunManager;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private ICourseQueueDAO _courseQueueDAO;
	private ICourseManager _courseManager;
	private ISubscriberManager _subscriberManager;
	private ICourseMailManager _courseMailManager;
	private IMailgunManager _mailgunManager;
	private ConfigInterface _configManager;
}
