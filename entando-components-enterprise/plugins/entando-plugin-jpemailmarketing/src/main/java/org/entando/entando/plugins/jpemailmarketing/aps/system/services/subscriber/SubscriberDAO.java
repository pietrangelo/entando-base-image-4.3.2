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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueue;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.ICourseQueueDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

public class SubscriberDAO extends AbstractSearcherDAO implements ISubscriberDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SubscriberDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}

	@Override
	protected String getMasterTableName() {
		return "jpemailmarketing_subscriber";
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
	public List<Integer> searchSubscribers(FieldSearchFilter[] filters) {
		List<Integer> subscribersId = new ArrayList<Integer>();
		try {
			List<String> subscribersIdString  = super.searchId(filters);
			if (null != subscribersIdString && !subscribersIdString.isEmpty()) {
				for (int i = 0; i < subscribersIdString.size(); i++) {
					subscribersId.add(new Integer(subscribersIdString.get(i)));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in searchSubscribers",  t);
			throw new RuntimeException("error in searchSubscribers", t);
		}
		return subscribersId;
	}

	@Override
	public List<Integer> loadSubscribers() {
		List<Integer> subscribersId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SUBSCRIBERS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				subscribersId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Subscriber list",  t);
			throw new RuntimeException("Error loading Subscriber list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return subscribersId;
	}

	@Override
	public void insertSubscriber(Subscriber subscriber) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertSubscriber(subscriber, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert subscriber",  t);
			throw new RuntimeException("Error on insert subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertSubscriber(Subscriber subscriber, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SUBSCRIBER);
			int index = 1;
			stat.setInt(index++, subscriber.getId());
			stat.setInt(index++, subscriber.getCourseId());
			stat.setString(index++, subscriber.getName());
			stat.setString(index++, subscriber.getEmail());
			stat.setString(index++, subscriber.getHash());
			stat.setString(index++, subscriber.getStatus());
			Timestamp createdatTimestamp = new Timestamp(subscriber.getCreatedat().getTime());
			stat.setTimestamp(index++, createdatTimestamp);	
			Timestamp updatedatTimestamp = new Timestamp(subscriber.getUpdatedat().getTime());
			stat.setTimestamp(index++, updatedatTimestamp);	
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert subscriber",  t);
			throw new RuntimeException("Error on insert subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateSubscriber(Subscriber subscriber) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSubscriber(subscriber, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating subscriber {}", subscriber.getId(),  t);
			throw new RuntimeException("Error updating subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void confirmSubscriber(Subscriber subscriber, List<CourseQueue> queue) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSubscriber(subscriber, conn);
			this.getCourseQueueDAO().insertCourseQueues(queue, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating subscriber {}", subscriber.getId(),  t);
			throw new RuntimeException("Error updating subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void unsubscribeSubscriber(Subscriber subscriber) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSubscriber(subscriber, conn);
			this.getCourseQueueDAO().removeUnsent(subscriber.getId(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating subscriber {}", subscriber.getId(),  t);
			throw new RuntimeException("Error updating subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}

	}


	public void updateSubscriber(Subscriber subscriber, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SUBSCRIBER);
			int index = 1;

			//stat.setInt(index++, subscriber.getCourseId());
			//stat.setString(index++, subscriber.getName());
			//stat.setString(index++, subscriber.getEmail());
			//stat.setString(index++, subscriber.getHash());
			stat.setString(index++, subscriber.getStatus());
			//Timestamp createdatTimestamp = new Timestamp(subscriber.getCreatedat().getTime());
			//stat.setTimestamp(index++, createdatTimestamp);	
			Timestamp updatedatTimestamp = new Timestamp(subscriber.getUpdatedat().getTime());
			stat.setTimestamp(index++, updatedatTimestamp);	
			stat.setInt(index++, subscriber.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating subscriber {}", subscriber.getId(),  t);
			throw new RuntimeException("Error updating subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeSubscriber(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeSubscriber(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting subscriber {}", id, t);
			throw new RuntimeException("Error deleting subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeSubscriber(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_SUBSCRIBER);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting subscriber {}", id, t);
			throw new RuntimeException("Error deleting subscriber", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Subscriber loadSubscriber(int id) {
		Subscriber subscriber = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			subscriber = this.loadSubscriber(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with id {}", id, t);
			throw new RuntimeException("Error loading subscriber with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return subscriber;
	}

	@Override
	public Subscriber loadSubscriber(int courseId, String email) {
		Subscriber subscriber = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			subscriber = this.loadSubscriber(courseId, email, conn);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with courseid {} and email {}", courseId, email, t);
			throw new RuntimeException("Error loading subscriber with courseId/email " + courseId + "/" + email, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return subscriber;
	}

	@Override
	public Subscriber loadSubscriber(String email, String hash) {
		Subscriber subscriber = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			subscriber = this.loadSubscriber(email, hash, conn);
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with email {} and hash {}", email, hash, t);
			throw new RuntimeException("Error loading subscriber with email/hash " + email + "/" + hash, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return subscriber;
	}

	@Override
	public Map<String, Integer> loadOccurrencesBySubscriberStatus(int courseId,	Collection<String> statuses) {
		Map<String, Integer> occurrencesByCourseMail = new HashMap<String, Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			//and status in ('confirmed','unconfirmed','unregistered')
			String query = LOAD_OCCURRENCES_BY_SUBSCRIBER_STATUS;
			if (null != statuses && !statuses.isEmpty()) {
				String qblock = StringUtils.repeat("?", ", ", statuses.size());
				StringBuffer sbBuffer = new StringBuffer(" AND status IN ").append("(").append(qblock).append(")");
				query = query.replace("#FILTER_STATUS#", sbBuffer.toString());
			} else {				
				query = query.replace("#FILTER_STATUS#", "");
			}
			stat = conn.prepareStatement(query);
			int index = 1;
			stat.setInt(index++, courseId);
			if (null != statuses && !statuses.isEmpty()) {
				Iterator<String> it = statuses.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}
			res = stat.executeQuery();
			while (res.next()) {
				String status = res.getString("status");
				int count = res.getInt("count");
				occurrencesByCourseMail.put(status, count);
			}
		} catch (Throwable t) {
			_logger.error("Error loading occurrencesBySubscriberStatus for course {}", courseId,  t);
			throw new RuntimeException("Error occurrencesBySubscriberStatus for course " + courseId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return occurrencesByCourseMail;
	}

	@Override
	public List<Integer> loadSubscribersByStatus(int courseId,	Collection<String> statuses) {
		List<Integer> subscribersId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			String query = LOAD_SUBSCRIBERS_BY_STATUS;
			if (null != statuses && !statuses.isEmpty()) {
				String qblock = StringUtils.repeat("?", ", ", statuses.size());
				StringBuffer sbBuffer = new StringBuffer(" AND status IN ").append("(").append(qblock).append(")");
				query = query.replace("#FILTER_STATUS#", sbBuffer.toString());
			} else {				
				query = query.replace("#FILTER_STATUS#", "");
			}
			stat = conn.prepareStatement(query);
			int index = 1;
			stat.setInt(index++, courseId);
			if (null != statuses && !statuses.isEmpty()) {
				Iterator<String> it = statuses.iterator();
				while (it.hasNext()) {
					stat.setString(index++, it.next());
				}
			}
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				subscribersId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Subscriber list by status",  t);
			throw new RuntimeException("Error loading Subscriber bt status", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return subscribersId;
	}


	public Subscriber loadSubscriber(int id, Connection conn) {
		Subscriber subscriber = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SUBSCRIBER);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				subscriber = this.buildSubscriberFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading subscriber with id {}", id, t);
			throw new RuntimeException("Error loading subscriber with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return subscriber;
	}

	public Subscriber loadSubscriber(int courseId, String email, Connection conn) throws Throwable {
		Subscriber subscriber = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SUBSCRIBER_BY_COURSE_EMAIL);
			int index = 1;
			stat.setInt(index++, courseId);
			stat.setString(index++, email);
			res = stat.executeQuery();
			if (res.next()) {
				subscriber = this.buildSubscriberFromRes(res);
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(res, stat, null);
		}
		return subscriber;
	}

	public Subscriber loadSubscriber(String email, String hash, Connection conn) throws Throwable {
		Subscriber subscriber = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SUBSCRIBER_BY_EMAIL_HASH);
			int index = 1;
			stat.setString(index++, email);
			stat.setString(index++, hash);
			res = stat.executeQuery();
			if (res.next()) {
				subscriber = this.buildSubscriberFromRes(res);
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(res, stat, null);
		}
		return subscriber;
	}

	protected Subscriber buildSubscriberFromRes(ResultSet res) {
		Subscriber subscriber = null;
		try {
			subscriber = new Subscriber();				
			subscriber.setId(res.getInt("id"));
			subscriber.setCourseId(res.getInt("courseid"));
			subscriber.setName(res.getString("name"));
			subscriber.setEmail(res.getString("email"));
			subscriber.setHash(res.getString("hash"));
			subscriber.setStatus(res.getString("status"));
			Timestamp createdatValue = res.getTimestamp("createdat");
			if (null != createdatValue) {
				subscriber.setCreatedat(new Date(createdatValue.getTime()));
			}
			Timestamp updatedatValue = res.getTimestamp("updatedat");
			if (null != updatedatValue) {
				subscriber.setUpdatedat(new Date(updatedatValue.getTime()));
			}
		} catch (Throwable t) {
			_logger.error("Error in buildSubscriberFromRes", t);
		}
		return subscriber;
	}

	@Override
	public InputStream loadSubscriberCSV(int courseId) throws Throwable {
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		InputStream is = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SUBSCRIBERS_CSV);
			int index = 1;
			stat.setInt(index++, courseId);

			res = stat.executeQuery();
			
			StringBuffer sbBuffer = new StringBuffer();
			int colsNumber = res.getMetaData().getColumnCount();
			for(int i=1; i<(colsNumber+1); i++) {
				sbBuffer.append(res.getMetaData().getColumnName(i));
				if (i < colsNumber) {
					sbBuffer.append(";");
				} else {
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
			_logger.error("Error loading csv subscriber export for course {}", courseId, t);
			throw new RuntimeException("Error loading csv subscriber export for course " + courseId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return is;
	}

	protected ICourseQueueDAO getCourseQueueDAO() {
		return _courseQueueDAO;
	}
	public void setCourseQueueDAO(ICourseQueueDAO courseQueueDAO) {
		this._courseQueueDAO = courseQueueDAO;
	}

	private static final String ADD_SUBSCRIBER = "INSERT INTO jpemailmarketing_subscriber (id, courseid, name, email, hash, status, createdat, updatedat ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

	//hash=?, createdat=?,courseid=?,  name=?,  email=?,  
	private static final String UPDATE_SUBSCRIBER = "UPDATE jpemailmarketing_subscriber SET status=?, updatedat=? WHERE id = ?";

	private static final String DELETE_SUBSCRIBER = "DELETE FROM jpemailmarketing_subscriber WHERE id = ?";

	private static final String LOAD_SUBSCRIBER = "SELECT id, courseid, name, email, hash, status, createdat, updatedat  FROM jpemailmarketing_subscriber WHERE id = ?";

	private static final String LOAD_SUBSCRIBER_BY_COURSE_EMAIL = "SELECT id, courseid, name, email, hash, status, createdat, updatedat  FROM jpemailmarketing_subscriber WHERE courseid = ? AND email = ?";

	private static final String LOAD_SUBSCRIBER_BY_EMAIL_HASH = "SELECT id, courseid, name, email, hash, status, createdat, updatedat  FROM jpemailmarketing_subscriber WHERE email = ? AND hash = ?";

	private static final String LOAD_SUBSCRIBERS_ID  = "SELECT id FROM jpemailmarketing_subscriber";

	private static final String LOAD_SUBSCRIBERS_BY_STATUS  = "SELECT id FROM jpemailmarketing_subscriber WHERE courseid = ? #FILTER_STATUS# ORDER BY updatedat DESC";

	private static final String LOAD_OCCURRENCES_BY_SUBSCRIBER_STATUS  = "SELECT status, count(id) as count FROM jpemailmarketing_subscriber WHERE courseid = ? #FILTER_STATUS# GROUP BY status";

	private static final String LOAD_SUBSCRIBERS_CSV  = "SELECT s.id, s.name, s.email, s.status, s.createdat, s.updatedat, c.id AS course_id, c.name AS course_name FROM jpemailmarketing_subscriber s INNER JOIN jpemailmarketing_course c ON s.courseid = c.id WHERE c.id = ? ORDER BY s.updatedat DESC";

	private ICourseQueueDAO _courseQueueDAO;
}


