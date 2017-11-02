package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.JpemailmarketingBaseTestCase;
import org.entando.entando.plugins.jpemailmarketing.aps.system.JpEmailmarketingSystemConstant;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.TestDAO;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.mock.IMockSubscriberManager;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public class TestCourseManager extends JpemailmarketingBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testNotify() throws Throwable {
		assertNotNull(_courseManager);
		Course course = this._courseManager.getCourse(1);
		assertNotNull(course);
		this.deleteMockSubsctribers();
		this.addMockSubscribers("x", course.getId());
		((CourseManager)this._courseManager).notifyCourseTriggerEvent(course);
		this.waitNotifyingThread();
		Thread.sleep(3000);
		this.deleteMockSubsctribers();
	}

	public void deleteMockSubsctribers() throws ApsSystemException {
		FieldSearchFilter name = new FieldSearchFilter("name", "deleteme", true);
		List<Integer> list = this._subscriberManager.searchSubscribers(new FieldSearchFilter[]{name});
		for (int i = 0; i < list.size(); i++) {
			System.out.println(this._subscriberManager.getSubscriber(list.get(i)).getUpdatedat());
			this._subscriberManager.deleteSubscriber(list.get(i));
		}
	}

	public void addMockSubscribers(String prefix, int course) throws ApsSystemException {
		for (int i = 0; i < 10; i++) {
			Subscriber subscriber = new Subscriber();
			subscriber.setName(prefix + "_deleteme" + i + "@entando.com");
			subscriber.setEmail("deleteme" + i + "@entando.com");
			subscriber.setStatus(Subscriber.STATUS_CONFIRMED);
			subscriber.setCourseId(course);
			System.out.println("add");
			this._subscriberManager.addSubscriber(subscriber);
			
			subscriber.setUpdatedat(DateUtils.addDays(subscriber.getUpdatedat(), -i));
			_testDAO.fullUpdateSubscriber(subscriber);
		}
	}
	
	private void init() {
		this._courseManager = (ICourseManager) this.getService(JpEmailmarketingSystemConstant.COURSE_MANAGER);
		this._subscriberManager = (IMockSubscriberManager) this.getService(JpEmailmarketingSystemConstant.SUBSCRIBER_MANAGER);
		this._testDAO =  (TestDAO) this.getApplicationContext().getBean("jpemailmarketingTestDAO");
	} 
	
	private ICourseManager _courseManager;
	private IMockSubscriberManager _subscriberManager;
	private TestDAO _testDAO;

}
