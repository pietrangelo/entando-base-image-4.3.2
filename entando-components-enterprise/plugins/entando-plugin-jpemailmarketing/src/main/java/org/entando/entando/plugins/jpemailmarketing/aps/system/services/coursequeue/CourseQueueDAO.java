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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

public class CourseQueueDAO extends AbstractSearcherDAO implements ICourseQueueDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseQueueDAO.class);



	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}

	@Override
	protected String getMasterTableName() {
		return "jpemailmarketing_coursequeue";
	}

	@Override
	protected String getMasterTableIdFieldName() {
		return "id";
	}

	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}

	@Override
	public List<Integer> searchCourseQueues(FieldSearchFilter[] filters) {
		List courseQueuesId = null;
		try {
			courseQueuesId  = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchCourseQueues",  t);
			throw new RuntimeException("error in searchCourseQueues", t);
		}
		return courseQueuesId;
	}


	@Override
	public List<Integer> loadCourseQueues() {
		List<Integer> courseQueuesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COURSEQUEUES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				courseQueuesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading CourseQueue list",  t);
			throw new RuntimeException("Error loading CourseQueue list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseQueuesId;
	}

	@Override
	public List<Integer> loadDeliveryCourseQueue(int courseId, Date date) {
		List<Integer> courseQueuesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_DELIVERY_QUEUE_ID);

			int index = 1;
			stat.setInt(index++, courseId);
			date = DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
			//date = DateUtils.ceiling(date, Calendar.DAY_OF_MONTH);
			Timestamp deteTimestamp = new Timestamp(date.getTime());
			stat.setTimestamp(index++, deteTimestamp);

			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				courseQueuesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading CourseQueue list",  t);
			throw new RuntimeException("Error loading CourseQueue list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseQueuesId;
	}

	@Override
	public List<Integer> loadCourseQueueBySubscriber(int subscriberId) {
		List<Integer> courseQueuesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COURSEQUEUE_ID_BY_SUBSCRIBER);
			int index = 1;
			stat.setInt(index++, subscriberId);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				courseQueuesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading CourseQueue list",  t);
			throw new RuntimeException("Error loading CourseQueue list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseQueuesId;
	}

	public void insertCourseQueues(List<CourseQueue> list, Connection conn) {
		if (list != null && list.size()>0) {
			PreparedStatement stat = null;
			try {
				Iterator<CourseQueue> qIterator = list.iterator();
				stat = conn.prepareStatement(ADD_COURSEQUEUE);
				while (qIterator.hasNext()) {
					CourseQueue courseQueue = qIterator.next();
					int index = 1;

					//id, operationindex, subscriberid, coursemailid, scheduleddate, sent, createdat, updatedat, mgmessageid, mgstatus

					stat.setInt(index++, courseQueue.getId());

					if(null != courseQueue.getOperationindex()) {
						stat.setLong(index++, courseQueue.getOperationindex());
					} else {
						stat.setNull(index++, Types.NUMERIC);
					}

					stat.setInt(index++, courseQueue.getSubscriberid());

					stat.setInt(index++, courseQueue.getCoursemailid());

					Timestamp scheduledTimestamp = new Timestamp(courseQueue.getScheduledDate().getTime());
					stat.setTimestamp(index++, scheduledTimestamp);

					int sent = (courseQueue.getSent().booleanValue() == true ? 1 : 0);
					stat.setInt(index++, sent);


					Timestamp createdatTimestamp = new Timestamp(courseQueue.getCreatedat().getTime());
					stat.setTimestamp(index++, createdatTimestamp);

					Timestamp updatedatTimestamp = new Timestamp(courseQueue.getUpdatedat().getTime());
					stat.setTimestamp(index++, updatedatTimestamp);

					if(StringUtils.isNotBlank(courseQueue.getMgmessageid())) {
						stat.setString(index++, courseQueue.getMgmessageid());
					} else {
						stat.setNull(index++, Types.VARCHAR);
					}

					if(null != courseQueue.getMgstatus()) {
						stat.setInt(index++, courseQueue.getMgstatus());
					} else {
						stat.setNull(index++, Types.INTEGER);
					}

					stat.addBatch();
					stat.clearParameters();
				}
				stat.executeBatch();
			} catch (Throwable t) {
				_logger.error("Error on insert courseQueue list",  t);
				throw new RuntimeException("Error on insert courseQueue list", t);
			} finally {
				closeDaoResources(null, stat);
			}
		}
	}

	@Override
	public void updateDeliveryCourseQueue(List<Integer> queueRecordsId, Integer operationIndex, String messageId, Integer messageStatus) {
		Connection conn = null;
		PreparedStatement stat = null;
		if (queueRecordsId != null && queueRecordsId.size()>0) {
			try {
				Iterator<Integer> qIterator = queueRecordsId.iterator();
				conn = this.getConnection();
				stat = conn.prepareStatement(UPDATE_DELIVERY_QUEUE);
				while (qIterator.hasNext()) {
					int id = qIterator.next();
					int index = 1;

					//perationindex=?, updatedat=? mgmessageid=?, mgstatus=?  WHERE id = ?

					//stat.setInt(index++, courseQueue.getId());

					if(null != operationIndex) {
						stat.setLong(index++, operationIndex);
					} else {
						stat.setNull(index++, Types.NUMERIC);
					}
					Timestamp updatedatTimestamp = new Timestamp(new Date().getTime());
					stat.setTimestamp(index++, updatedatTimestamp);

					if(StringUtils.isNotBlank(messageId)) {
						stat.setString(index++, messageId);
					} else {
						stat.setNull(index++, Types.VARCHAR);
					}

					if (null != messageStatus) {
						stat.setInt(index++, messageStatus);
					} else {
						stat.setNull(index++, Types.INTEGER);
					}

					stat.setInt(index++, id);

					stat.addBatch();
					stat.clearParameters();
				}
				stat.executeBatch();
			} catch (Throwable t) {
				_logger.error("Error on updateDeliveryCourseQueue list ",  t);
				throw new RuntimeException("Error on updateDeliveryCourseQueue list", t);
			} finally {
				closeDaoResources(null, stat, conn);
			}
		}

	}

	@Override
	public void removeUnsent(int subscriberid, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_UNSENT);
			int index = 1;
			stat.setInt(index++, subscriberid);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting insent Queue for subscriber{}", subscriberid, t);
			throw new RuntimeException("Error deleting unsent courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}


	/*
	@Override
	public void insertCourseQueue(CourseQueue courseQueue) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			//this.insertCourseQueue(courseQueue, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert courseQueue",  t);
			throw new RuntimeException("Error on insert courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertCourseQueue(CourseQueue courseQueue, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_COURSEQUEUE);
			int index = 1;
			stat.setInt(index++, courseQueue.getId());
			stat.setLong(index++, courseQueue.getOperationindex());
			stat.setInt(index++, courseQueue.getSubscriberid());
			stat.setInt(index++, courseQueue.getCourseid());
			stat.setInt(index++, courseQueue.getCoursemailid());
			Timestamp createdatTimestamp = new Timestamp(courseQueue.getCreatedat().getTime());
			stat.setTimestamp(index++, createdatTimestamp);
			Timestamp updatedatTimestamp = new Timestamp(courseQueue.getUpdatedat().getTime());
			stat.setTimestamp(index++, updatedatTimestamp);
			if(StringUtils.isNotBlank(courseQueue.getMgmessageid())) {
				stat.setString(index++, courseQueue.getMgmessageid());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if(StringUtils.isNotBlank(courseQueue.getMgstatus())) {
				stat.setString(index++, courseQueue.getMgstatus());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert courseQueue",  t);
			throw new RuntimeException("Error on insert courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}
	 */


	//	@Override
	//	public void updateCourseQueue(CourseQueue courseQueue) {
	//		PreparedStatement stat = null;
	//		Connection conn = null;
	//		try {
	//			conn = this.getConnection();
	//			conn.setAutoCommit(false);
	//			this.updateCourseQueue(courseQueue, conn);
	//			conn.commit();
	//		} catch (Throwable t) {
	//			this.executeRollback(conn);
	//			_logger.error("Error updating courseQueue {}", courseQueue.getId(),  t);
	//			throw new RuntimeException("Error updating courseQueue", t);
	//		} finally {
	//			this.closeDaoResources(null, stat, conn);
	//		}
	//	}
	//
	//	public void updateCourseQueue(CourseQueue courseQueue, Connection conn) {
	//		PreparedStatement stat = null;
	//		try {
	//			stat = conn.prepareStatement(UPDATE_COURSEQUEUE);
	//			int index = 1;
	//			/*
	//			stat.setInt(index++, courseQueue.getOperationindex());
	//			stat.setInt(index++, courseQueue.getSubscriberid());
	//			stat.setInt(index++, courseQueue.getCourseid());
	//			stat.setInt(index++, courseQueue.getCoursemailid());
	//			Timestamp createdatTimestamp = new Timestamp(courseQueue.getCreatedat().getTime());
	//			stat.setTimestamp(index++, createdatTimestamp);
	//			 */
	//			Timestamp updatedatTimestamp = new Timestamp(courseQueue.getUpdatedat().getTime());
	//			stat.setTimestamp(index++, updatedatTimestamp);
	//			/*
	//			if(StringUtils.isNotBlank(courseQueue.getMgmessageid())) {
	//				stat.setString(index++, courseQueue.getMgmessageid());
	//			} else {
	//				stat.setNull(index++, Types.VARCHAR);
	//			}
	//			 */
	//			if(StringUtils.isNotBlank(courseQueue.getMgstatus())) {
	//				stat.setString(index++, courseQueue.getMgstatus());
	//			} else {
	//				stat.setNull(index++, Types.VARCHAR);
	//			}
	//			stat.setInt(index++, courseQueue.getId());
	//			stat.executeUpdate();
	//		} catch (Throwable t) {
	//			_logger.error("Error updating courseQueue {}", courseQueue.getId(),  t);
	//			throw new RuntimeException("Error updating courseQueue", t);
	//		} finally {
	//			this.closeDaoResources(null, stat, null);
	//		}
	//	}

	@Override
	public void removeCourseQueue(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeCourseQueue(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting courseQueue {}", id, t);
			throw new RuntimeException("Error deleting courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeCourseQueue(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_COURSEQUEUE);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting courseQueue {}", id, t);
			throw new RuntimeException("Error deleting courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public Map<Integer, Integer> loadOccurrencesByCourseMail(int courseId, Boolean sent) {
		Map<Integer, Integer> occurrencesByCourseMail = new HashMap<Integer, Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_OCCURRENCES_BY_COURSEMAIL);
			int index = 1;
			stat.setInt(index++, courseId);
			int sentValue = sent.booleanValue() == true ? 1 : 0;
			stat.setInt(index++, sentValue);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("coursemailid");
				int count = res.getInt("count");
				occurrencesByCourseMail.put(id, count);
			}
		} catch (Throwable t) {
			_logger.error("Error loading occurrencesByCourseMail for course {}", courseId,  t);
			throw new RuntimeException("Error occurrencesByCourseMail for course " + courseId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return occurrencesByCourseMail;
	}

	public CourseQueue loadCourseQueue(int id) {
		CourseQueue courseQueue = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			courseQueue = this.loadCourseQueue(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading courseQueue with id {}", id, t);
			throw new RuntimeException("Error loading courseQueue with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseQueue;
	}

	public CourseQueue loadCourseQueue(int id, Connection conn) {
		CourseQueue courseQueue = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_COURSEQUEUE);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				courseQueue = this.buildCourseQueueFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading courseQueue with id {}", id, t);
			throw new RuntimeException("Error loading courseQueue with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return courseQueue;
	}

	protected CourseQueue buildCourseQueueFromRes(ResultSet res) {
		CourseQueue courseQueue = null;
		try {
			courseQueue = new CourseQueue();
			courseQueue.setId(res.getInt("id"));
			courseQueue.setOperationindex(res.getLong("operationindex"));
			courseQueue.setSubscriberid(res.getInt("subscriberid"));

			courseQueue.setCoursemailid(res.getInt("coursemailid"));

			Timestamp scheduledDateValue = res.getTimestamp("scheduleddate");
			if (null != scheduledDateValue) {
				courseQueue.setScheduledDate(new Date(scheduledDateValue.getTime()));
			}
			int sent = res.getInt("sent");
			courseQueue.setSent(new Boolean(sent == 1));


			Timestamp createdatValue = res.getTimestamp("createdat");
			if (null != createdatValue) {
				courseQueue.setCreatedat(new Date(createdatValue.getTime()));
			}
			Timestamp updatedatValue = res.getTimestamp("updatedat");
			if (null != updatedatValue) {
				courseQueue.setUpdatedat(new Date(updatedatValue.getTime()));
			}
			courseQueue.setMgmessageid(res.getString("mgmessageid"));
			courseQueue.setMgstatus(res.getInt("mgstatus"));
		} catch (Throwable t) {
			_logger.error("Error in buildCourseQueueFromRes", t);
		}
		return courseQueue;
	}


	@Override
	public InputStream loadEmailsCSV(int id) throws Throwable {
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		InputStream is = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_EMAILS_CSV);
			int index = 1;
			stat.setInt(index++, id);

			res = stat.executeQuery();

			StringBuffer sbBuffer = new StringBuffer();
			int colsNumber = res.getMetaData().getColumnCount();
			for(int i=1; i<(colsNumber+1); i++) {
				sbBuffer.append(res.getMetaData().getColumnName(i));
				if (i < colsNumber) {
					sbBuffer.append(";");
				}else {
					sbBuffer.append("\r\n");
				}
			}
			while (res.next()) {
				for(int i=1; i < (colsNumber+1); i++) {
					res.getMetaData().getColumnType(i);
					sbBuffer.append(res.getString(i));
					if (i < colsNumber) {
						sbBuffer.append(";");
					} else {
						sbBuffer.append("\r\n");
					}
				}
			}
			is = IOUtils.toInputStream(sbBuffer.toString());

		} catch (Throwable t) {
			_logger.error("Error loading csv subscriber export for course {}", id, t);
			throw new RuntimeException("Error loading csv subscriber export for course " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return is;
	}

	//private static final String ADD_COURSEQUEUE_OLD = "INSERT INTO jpemailmarketing_coursequeue (id, operationindex, subscriberid, courseid, coursemailid, createdat, updatedat, mgmessageid, mgstatus ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";
	private static final String ADD_COURSEQUEUE = "INSERT INTO jpemailmarketing_coursequeue (id, operationindex, subscriberid, coursemailid, scheduleddate, sent, createdat, updatedat, mgmessageid, mgstatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String LOAD_COURSEQUEUE_ID_BY_SUBSCRIBER = "SELECT q.id FROM jpemailmarketing_coursequeue q INNER JOIN jpemailmarketing_coursemail m ON q.coursemailid = m.id WHERE subscriberid = ? ORDER BY m.position ASC";


	//operationindex=?,  subscriberid=?,  courseid=?,  coursemailid=?,  createdat=?,  mgmessageid=?,
	private static final String UPDATE_COURSEQUEUE = "UPDATE jpemailmarketing_coursequeue SET  updatedat=?,  mgstatus=? WHERE id = ?";

	private static final String DELETE_COURSEQUEUE = "DELETE FROM jpemailmarketing_coursequeue WHERE id = ?";

	private static final String DELETE_UNSENT = "DELETE FROM jpemailmarketing_coursequeue WHERE subscriberid = ? and sent=0";

	private static final String LOAD_COURSEQUEUE = "SELECT id, operationindex, subscriberid, coursemailid, scheduleddate, sent, createdat, updatedat, mgmessageid, mgstatus  FROM jpemailmarketing_coursequeue WHERE id = ?";

	private static final String LOAD_COURSEQUEUES_ID  = "SELECT id FROM jpemailmarketing_coursequeue";

	private static final String LOAD_OCCURRENCES_BY_COURSEMAIL  = "SELECT coursemailid, count(q.coursemailid) as count "
			+ "FROM jpemailmarketing_coursequeue q	INNER JOIN jpemailmarketing_coursemail m on q.coursemailid = m.id "
			+ "WHERE m.courseid = ? AND q.sent = ? "
			+ "GROUP BY coursemailid ORDER BY coursemailid";

	private static final String LOAD_DELIVERY_QUEUE_ID = "SELECT q.id FROM jpemailmarketing_coursequeue q INNER JOIN jpemailmarketing_coursemail m ON q.coursemailid = m.id WHERE m.courseid=? AND sent = 0 AND scheduleddate <= ? ORDER BY scheduleddate ASC";

	private static final String UPDATE_DELIVERY_QUEUE = "UPDATE jpemailmarketing_coursequeue SET sent=1, operationindex=?, updatedat=?, mgmessageid=?, mgstatus=?  WHERE id = ?";

	private static final String LOAD_EMAILS_CSV = "SELECT s.id as subscriber_id, s.name, s.email, c.id as course_id, c.name as course_name, q.updatedat as queue_updatedat, q.mgstatus, m.emailsubject, m.position FROM jpemailmarketing_coursequeue q INNER JOIN jpemailmarketing_coursemail m ON q.coursemailid = m.id INNER JOIN jpemailmarketing_subscriber s ON q.subscriberid = s.id INNER JOIN jpemailmarketing_course c ON m.courseid = c.id WHERE q.sent = 1 AND c.id = ? ORDER BY q.updatedat ASC";

}


/*

-- DROP TABLE jpemailmarketing_coursequeue;

CREATE TABLE jpemailmarketing_coursequeue
(
  id integer NOT NULL,
  operationindex bigint,
  subscriberid integer NOT NULL,
  coursemailid integer NOT NULL,
  scheduleddate timestamp without time zone NOT NULL,
  sent integer NOT NULL,
  createdat timestamp without time zone NOT NULL,
  updatedat timestamp without time zone NOT NULL,
  mgmessageid character varying(255),
  mgstatus integer,
  CONSTRAINT jpemailmarketing_coursequeue_pkey PRIMARY KEY (id),
  CONSTRAINT jpemailmarketing_coursequeuecoursemail_fkey FOREIGN KEY (coursemailid)
      REFERENCES jpemailmarketing_coursemail (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT jpemailmarketing_coursequeuesubscrib_fkey FOREIGN KEY (subscriberid)
      REFERENCES jpemailmarketing_subscriber (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE jpemailmarketing_coursequeue
  OWNER TO agile;

*/
