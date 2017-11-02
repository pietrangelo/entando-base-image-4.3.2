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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.mock;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueue;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.SubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.event.SubscriberChangedEvent;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailgunMailResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

public class MockSubscriberManager extends SubscriberManager implements IMockSubscriberManager {

	private static final Logger _logger =  LoggerFactory.getLogger(MockSubscriberManager.class);

	@Override
	public void confirmSubscriber(Subscriber subscriber, Date date) throws ApsSystemException {
		try {
			subscriber.setUpdatedat(date);
			List<CourseQueue> queue = this.getCourseQueueManager().createQueue(subscriber);
			this.getSubscriberDAO().confirmSubscriber(subscriber, queue);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error in confirm Subscriber", t);
			throw new ApsSystemException("Error in confirm Subscriber " + subscriber, t);
		}
	}
	
	@Override
	public void addSubscriberNoMail(Subscriber subscriber, Date now) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			subscriber.setCreatedat(now);
			subscriber.setUpdatedat(now);
			subscriber.setId(key);
			subscriber.setHash(RandomStringUtils.randomNumeric(8));
			this.getSubscriberDAO().insertSubscriber(subscriber);
			this.notifySubscriberChangedEvent(subscriber, SubscriberChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding Subscriber", t);
			throw new ApsSystemException("Error adding Subscriber", t);
		}
	}

}
