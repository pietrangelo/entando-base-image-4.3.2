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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

public class CourseMailDAO extends AbstractSearcherDAO implements ICourseMailDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseMailDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}

	@Override
	protected String getMasterTableName() {
		return "jpemailmarketing_coursemail";
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
	public List<Integer> searchCourseMails(FieldSearchFilter[] filters) {
		List courseMailsId = null;
		try {
			courseMailsId  = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchCourseMails",  t);
			throw new RuntimeException("error in searchCourseMails", t);
		}
		return courseMailsId;
	}

	@Override
	public List<Integer> loadCourseMails() {
		List<Integer> courseMailsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COURSEMAILS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				courseMailsId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading CourseMail list",  t);
			throw new RuntimeException("Error loading CourseMail list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseMailsId;
	}

	@Override
	public List<Integer> loadCourseMails(int courseId) {
		List<Integer> courseMailsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COURSEMAILS_ID_BY_COURSE);
			int index = 1;
			stat.setInt(index++, courseId);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				courseMailsId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading CourseMail list for course {}", courseId ,  t);
			throw new RuntimeException("Error loading CourseMail list for " + courseId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseMailsId;
	}

	@Override
	public void insertCourseMail(CourseMail courseMail) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertCourseMail(courseMail, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert courseMail",  t);
			throw new RuntimeException("Error on insert courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertCourseMail(CourseMail courseMail, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_COURSEMAIL);
			int index = 1;
			stat.setInt(index++, courseMail.getId());
			stat.setInt(index++, courseMail.getCourseid());
			stat.setInt(index++, courseMail.getPosition());
			stat.setInt(index++, courseMail.getDalayDays());
			stat.setString(index++, courseMail.getEmailSubject());
			stat.setString(index++, courseMail.getEmailContent());
			if(null != courseMail.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(courseMail.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
			if(null != courseMail.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(courseMail.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert courseMail",  t);
			throw new RuntimeException("Error on insert courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateCourseMail(CourseMail courseMail) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateCourseMail(courseMail, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating courseMail {}", courseMail.getId(),  t);
			throw new RuntimeException("Error updating courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateCourseMail(CourseMail courseMail, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_COURSEMAIL);
			int index = 1;

			stat.setInt(index++, courseMail.getCourseid());
			stat.setInt(index++, courseMail.getPosition());
			stat.setInt(index++, courseMail.getDalayDays());
			stat.setString(index++, courseMail.getEmailSubject());
			stat.setString(index++, courseMail.getEmailContent());
			if(null != courseMail.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(courseMail.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
			if(null != courseMail.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(courseMail.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
			stat.setInt(index++, courseMail.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating courseMail {}", courseMail.getId(),  t);
			throw new RuntimeException("Error updating courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeCourseMail(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeCourseMail(id, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting courseMail {}", id, t);
			throw new RuntimeException("Error deleting courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeCourseMail(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_COURSEMAIL);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting courseMail {}", id, t);
			throw new RuntimeException("Error deleting courseMail", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public CourseMail loadCourseMail(int id) {
		CourseMail courseMail = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			courseMail = this.loadCourseMail(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading courseMail with id {}", id, t);
			throw new RuntimeException("Error loading courseMail with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return courseMail;
	}

	public CourseMail loadCourseMail(int id, Connection conn) {
		CourseMail courseMail = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_COURSEMAIL);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				courseMail = this.buildCourseMailFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading courseMail with id {}", id, t);
			throw new RuntimeException("Error loading courseMail with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return courseMail;
	}

	protected CourseMail buildCourseMailFromRes(ResultSet res) {
		CourseMail courseMail = null;
		try {
			courseMail = new CourseMail();				
			courseMail.setId(res.getInt("id"));
			courseMail.setCourseid(res.getInt("courseid"));
			courseMail.setPosition(res.getInt("position"));
			courseMail.setDalayDays(res.getInt("dalaydays"));
			courseMail.setEmailSubject(res.getString("emailsubject"));
			courseMail.setEmailContent(res.getString("emailcontent"));
			Timestamp createdatValue = res.getTimestamp("createdat");
			if (null != createdatValue) {
				courseMail.setCreatedat(new Date(createdatValue.getTime()));
			}
			Timestamp updatedatValue = res.getTimestamp("updatedat");
			if (null != updatedatValue) {
				courseMail.setUpdatedat(new Date(updatedatValue.getTime()));
			}
			courseMail.setSubscriberDelay(res.getInt("subscriberdelay"));
		} catch (Throwable t) {
			_logger.error("Error in buildCourseMailFromRes", t);
		}
		return courseMail;
	}

	private static final String ADD_COURSEMAIL = "INSERT INTO jpemailmarketing_coursemail (id, courseid, position, dalaydays, emailsubject, emailcontent, createdat, updatedat ) VALUES (?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_COURSEMAIL = "UPDATE jpemailmarketing_coursemail SET  courseid=?, position=?,  dalaydays=?,  emailsubject=?,  emailcontent=?,  createdat=?, updatedat=? WHERE id = ?";

	private static final String DELETE_COURSEMAIL = "DELETE FROM jpemailmarketing_coursemail WHERE id = ?";

	//private static final String LOAD_COURSEMAIL = "SELECT id, courseid, position, dalaydays, emailsubject, emailcontent, createdat, updatedat  FROM jpemailmarketing_coursemail WHERE id = ?";

	private static final String LOAD_COURSEMAILS_ID  = "SELECT id FROM jpemailmarketing_coursemail";

	private static final String LOAD_COURSEMAILS_ID_BY_COURSE  = "SELECT id FROM jpemailmarketing_coursemail where courseid =? order by position ASC";
	
	private static final String LOAD_COURSEMAIL = "SELECT t1.id, t1.courseid, t1.position, t1.dalaydays, t1.emailsubject, t1.emailcontent, t1.createdat, t1.updatedat, "
			+ "(SELECT sum(dalaydays) from jpemailmarketing_coursemail t2 WHERE t2.position <= t1.position and t1.courseid = t2.courseid) as subscriberdelay "
			+ "FROM jpemailmarketing_coursemail t1 WHERE t1.id = ? "
			+ "ORDER BY t1.position ASC";
	

}