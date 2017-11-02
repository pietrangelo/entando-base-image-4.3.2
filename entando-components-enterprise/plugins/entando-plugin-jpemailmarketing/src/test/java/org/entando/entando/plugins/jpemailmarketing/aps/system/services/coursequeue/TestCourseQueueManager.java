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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.JpemailmarketingBaseTestCase;
import org.entando.entando.plugins.jpemailmarketing.aps.system.JpEmailmarketingSystemConstant;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.mock.IMockCourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.TestSubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.mock.IMockSubscriberManager;
import org.entando.entando.plugins.jpmailgun.aps.system.JpMailgunSystemConstants;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.IMailgunManager;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailGunManager;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.util.DateConverter;

public class TestCourseQueueManager extends JpemailmarketingBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		try {
			this._courseQueueManager.deleteQueue();
			this.deleteTestSubscribers(SUBSCRIBER_NAME_PREFIX);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/*
	public void testA() {
		assertTrue(true);
	}
	*/

	public void testQueue() throws Throwable {
		try {
			Date refDate = DateConverter.parseDate("2014/01/01", "yyyy/MM/dd");
			Map<String, Subscriber> map = new HashMap<String, Subscriber>();
			for (int i = 0; i < 10; i++) {
				Subscriber subscriber = TestSubscriberManager.createMockSubscriber(SUBSCRIBER_NAME_PREFIX + "user" + i);
				this._subscriberManager.addSubscriberNoMail(subscriber, DateUtils.addDays(refDate, i));
				this._subscriberManager.confirmSubscriber(subscriber, DateUtils.addDays(refDate, i + 9));
				map.put(subscriber.getName(), subscriber);
			}

			assertEquals(7, this._courseQueueManager.getCourseQueueBySubscriber(map.get(SUBSCRIBER_NAME_PREFIX + "user1").getId()).size());

			Date targetDate = DateConverter.parseDate("2014-01-11", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

			targetDate = DateConverter.parseDate("2014-01-12", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

			targetDate = DateConverter.parseDate("2014-01-13", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

			targetDate = DateConverter.parseDate("2014-01-14", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

			targetDate = DateConverter.parseDate("2014-01-15", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

			targetDate = DateConverter.parseDate("2014-01-17", "yyyy-MM-dd");
			this._courseQueueManager.sendMails(1, targetDate);;

		} finally {
			this.usubscribeTestSubscribers(SUBSCRIBER_NAME_PREFIX);
			this._courseQueueManager.deleteQueue();
			this.deleteTestSubscribers(SUBSCRIBER_NAME_PREFIX);
		}
	}

	public static final Subscriber createMockSubscriber(String name) {
		Subscriber subscriber = new Subscriber();
		subscriber.setCourseId(1);
		subscriber.setName(name);
		subscriber.setEmail(name + "@entando.com");
		subscriber.setStatus(Subscriber.STATUS_UNCONFIRMED);
		return subscriber;
	}



	@SuppressWarnings("rawtypes")
	public void deleteTestSubscribers(String name) throws Throwable {
		FieldSearchFilter nameFilter = new FieldSearchFilter("name", name, true);
		List<Integer> list = this._subscriberManager.searchSubscribers(new FieldSearchFilter[] {nameFilter});
		Iterator<Integer> it = list.iterator();
		while (it.hasNext()) {
			this._subscriberManager.deleteSubscriber(it.next());
		}
	}

	@SuppressWarnings("rawtypes")
	public void usubscribeTestSubscribers(String name) throws Throwable {
		FieldSearchFilter nameFilter = new FieldSearchFilter("name", name, true);
		List<Integer> list = this._subscriberManager.searchSubscribers(new FieldSearchFilter[] {nameFilter});
		Iterator<Integer> it = list.iterator();
		while (it.hasNext()) {
			Subscriber subscriber = this._subscriberManager.getSubscriber(it.next());
			this._subscriberManager.unsubscribeSubscriber(subscriber);
		}
	}

	private void init() {
		this._subscriberManager =  (IMockSubscriberManager) this.getService(JpEmailmarketingSystemConstant.SUBSCRIBER_MANAGER);
		this._mailgunManager = (IMailgunManager) this.getService(JpMailgunSystemConstants.MAILGUN_MANAGER);
		((MailGunManager)this._mailgunManager).setActive(false);
		this._courseQueueManager= (IMockCourseQueueManager) this.getService(JpEmailmarketingSystemConstant.COURSE_QUEUE_MANAGER);
	}

	private IMockSubscriberManager _subscriberManager;
	private IMailgunManager _mailgunManager;
	private IMockCourseQueueManager _courseQueueManager;

	private static final String SUBSCRIBER_NAME_PREFIX = "mock_";
}
