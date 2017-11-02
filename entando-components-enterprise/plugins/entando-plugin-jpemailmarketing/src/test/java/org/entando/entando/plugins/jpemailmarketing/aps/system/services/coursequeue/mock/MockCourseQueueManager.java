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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail.CourseMail;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueue;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueueManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

public class MockCourseQueueManager extends CourseQueueManager implements IMockCourseQueueManager {

	private static final Logger _logger =  LoggerFactory.getLogger(MockCourseQueueManager.class);

	/*

cerca tutte le email da mandare oggi.
select q.id from jpemailmarketing_coursequeue q inner join jpemailmarketing_coursemail m on q.coursemailid = m.id where m.courseid=1 and sent = 0 and scheduleddate < '2014-06-07' order by scheduleddate asc


itera id e fai mappa <email; destinatario>

invia la mail, con operationindex e fai le cose di mailgun.

verifica le statistiche

elimina i metodi superflui api e quantaltro di queue

	 * */


	@SuppressWarnings("rawtypes")
	public void sendMails(int courseId, Date date) throws Throwable {
		try {
			List<Integer> courseQueueList = this.getCourseQueueDAO().loadDeliveryCourseQueue(courseId, date);
			if (null == courseQueueList || courseQueueList.isEmpty()) {
				_logger.info("nothing to send for course {} and date {}",courseId, DateConverter.getFormattedDate(date, "yyyy-MM-dd"));
				return ;
			}

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
				for (int i = 0; i < courseQueueRecords.size(); i++) {
					courseQueueRecordForCourseMail.add(courseQueueRecords.get(i).getId());
				}

				int operationIndex = new Long(new Date().getTime()).hashCode();

				//this.sendEmaai
				//extractId for update
				_logger.info("invio email {} ai destinarari in del CourseQueues {}", keyCourseMailId, StringUtils.join(courseQueueRecordForCourseMail, ", "));
				boolean mailOk = true;
				if (mailOk) {
					this.getCourseQueueDAO().updateDeliveryCourseQueue(courseQueueRecordForCourseMail, operationIndex, "mg_msgid", 200);
				}

			}

		} catch (Throwable t) {
			_logger.error("Error extracting queue to send for course {}", courseId , t);
			throw new ApsSystemException("Error extracting queue to send for course " + courseId, t);
		}
	}


	@Override
	public void deleteQueue() throws Throwable {
		this.getMockCourseQueueDAO().deleteQueue();

	}

	protected IMockCourseQueueDAO getMockCourseQueueDAO() {
		return _mockCourseQueueDAO;
	}
	public void setMockCourseQueueDAO(IMockCourseQueueDAO mockCourseQueueDAO) {
		this._mockCourseQueueDAO = mockCourseQueueDAO;
	}

	private IMockCourseQueueDAO _mockCourseQueueDAO;
}
