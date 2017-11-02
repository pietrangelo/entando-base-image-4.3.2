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

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.JpemailmarketingBaseTestCase;
import org.entando.entando.plugins.jpemailmarketing.aps.system.JpEmailmarketingSystemConstant;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.ICourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.mock.IMockSubscriberManager;
import org.entando.entando.plugins.jpmailgun.aps.system.JpMailgunSystemConstants;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.IMailgunManager;
import org.entando.entando.plugins.jpmailgun.aps.system.services.mailgun.MailGunManager;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

public class TestSubscriberManager extends JpemailmarketingBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		try {
			this.deleteTestSubscribes(SUBSCRIBER_NAME_PREFIX);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	//INSERT INTO jpemailmarketing_subscriber(id, courseid, name, email, hash, status, createdat, updatedat) VALUES (1, 1, 'unconfirmed_user', 'unconfirmed_user@entando.com', 123, 'unconfirmed', '2014-01-01 10:10:10', '2014-01-01 10:10:10');
	public void testGetSubscriber() throws Throwable {
		Subscriber subscriber = this._subscriberManager.getSubscriber(1);
		assertNotNull(subscriber);
		assertEquals("unconfirmed_user", subscriber.getName());
		assertEquals("unconfirmed_user@entando.com", subscriber.getEmail());
		assertEquals(1, subscriber.getCourseId());
		assertEquals(Subscriber.STATUS_UNCONFIRMED, subscriber.getStatus());
		assertNotNull(subscriber.getHash());
		Date expectedCreatedDate = DateConverter.parseDate("01/01/2014", "dd/MM/yyyy");
		assertTrue (DateUtils.isSameDay(expectedCreatedDate, subscriber.getCreatedat()));
	}

	public void testGetSubscribers() throws Throwable {
		assertEquals(1, _subscriberManager.getSubscribers().size());
	}
	
	@SuppressWarnings("rawtypes")
	public void testSearchSubscribers() throws ApsSystemException {
		String partialName = "ncon";
		FieldSearchFilter nameFilter = new FieldSearchFilter("name", partialName, true);
		List<Integer> list = this._subscriberManager.searchSubscribers(new FieldSearchFilter[] {nameFilter});
		assertEquals(1, list.size());
	}

	
	public void testAddDeleteSubscriber() throws Throwable {
		Integer currentSize = this._subscriberManager.getSubscribers().size();
		for (int i = 0; i < 5; i++) {
			this._subscriberManager.addSubscriberNoMail(TestSubscriberManager.createMockSubscriber(SUBSCRIBER_NAME_PREFIX + i), new Date());
		}
		assertEquals(5 + currentSize, this._subscriberManager.getSubscribers().size());
	}

	public void testConfirm() throws Throwable {
		Subscriber subscriber = TestSubscriberManager.createMockSubscriber(SUBSCRIBER_NAME_PREFIX + "user");
		try {
			this._subscriberManager.addSubscriberNoMail(subscriber, new Date());

			Subscriber mock = this._subscriberManager.getSubscriber(subscriber.getId());
			assertNotNull(mock);
			this._subscriberManager.confirmSubscriber(subscriber, DateConverter.parseDate("2014/01/01", "yyyy/MM/dd"));
			List<Integer> queue = this._courseQueueManager.getCourseQueueBySubscriber(subscriber.getId());
			assertEquals(7, queue.size());
			this._subscriberManager.unsubscribeSubscriber(subscriber);
			
			queue = this._courseQueueManager.getCourseQueueBySubscriber(subscriber.getId());
			assertEquals(0, queue.size());
			
		} finally {
			this._subscriberManager.deleteSubscriber(subscriber.getId());
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
	public void deleteTestSubscribes(String name) throws Throwable {
		FieldSearchFilter nameFilter = new FieldSearchFilter("name", name, true);
		List<Integer> list = this._subscriberManager.searchSubscribers(new FieldSearchFilter[] {nameFilter});
		Iterator<Integer> it = list.iterator();
		while (it.hasNext()) {
			this._subscriberManager.deleteSubscriber(it.next());
		}
	}

	private void init() {
		this._subscriberManager =  (IMockSubscriberManager) this.getService(JpEmailmarketingSystemConstant.SUBSCRIBER_MANAGER);
		this._mailgunManager = (IMailgunManager) this.getService(JpMailgunSystemConstants.MAILGUN_MANAGER);
		((MailGunManager)this._mailgunManager).setActive(false);
		this._courseQueueManager= (ICourseQueueManager) this.getService(JpEmailmarketingSystemConstant.COURSE_QUEUE_MANAGER);
	}
	
	private IMockSubscriberManager _subscriberManager;
	private IMailgunManager _mailgunManager;
	private ICourseQueueManager _courseQueueManager;
	
	private static final String SUBSCRIBER_NAME_PREFIX = "mock_";
}

