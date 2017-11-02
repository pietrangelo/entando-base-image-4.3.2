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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseDAO extends AbstractSearcherDAO implements ICourseDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CourseDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpemailmarketing_course";
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
	public List<Integer> searchCourses(FieldSearchFilter[] filters) {
		List coursesId = null;
		try {
			coursesId  = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchCourses",  t);
			throw new RuntimeException("error in searchCourses", t);
		}
		return coursesId;
	}

	@Override
	public List<Integer> loadCourses() {
		List<Integer> coursesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_COURSES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				coursesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Course list",  t);
			throw new RuntimeException("Error loading Course list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return coursesId;
	}

	@Override
	public List<Integer> loadActiveCourses() {
		List<Integer> coursesId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_ACTIVE_COURSES_ID);
			int index = 1;
			stat.setInt(index++, 1);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				coursesId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading active courses list",  t);
			throw new RuntimeException("Error loading active course list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return coursesId;
	}
	
	@Override
	public void insertCourse(Course course) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertCourse(course, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert course",  t);
			throw new RuntimeException("Error on insert course", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertCourse(Course course, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_COURSE);
			int index = 1;
			stat.setInt(index++, course.getId());
 			stat.setString(index++, course.getName());
 			stat.setString(index++, course.getSender());
			stat.setInt(index++, course.getActive());
 			if(StringUtils.isNotBlank(course.getCronexp())) {
				stat.setString(index++, course.getCronexp());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(course.getCrontimezoneid())) {
				stat.setString(index++, course.getCrontimezoneid());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if(null != course.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(course.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			if(null != course.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(course.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert course",  t);
			throw new RuntimeException("Error on insert course", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateCourse(Course course) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateCourse(course, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating course {}", course.getId(),  t);
			throw new RuntimeException("Error updating course", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateCourse(Course course, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_COURSE);
			int index = 1;

 			stat.setString(index++, course.getName());
 			stat.setString(index++, course.getSender());
			stat.setInt(index++, course.getActive());
 			if(StringUtils.isNotBlank(course.getCronexp())) {
				stat.setString(index++, course.getCronexp());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(course.getCrontimezoneid())) {
				stat.setString(index++, course.getCrontimezoneid());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if(null != course.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(course.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			if(null != course.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(course.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			stat.setInt(index++, course.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating course {}", course.getId(),  t);
			throw new RuntimeException("Error updating course", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeCourse(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeCourse(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting course {}", id, t);
			throw new RuntimeException("Error deleting course", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeCourse(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_COURSE);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting course {}", id, t);
			throw new RuntimeException("Error deleting course", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Course loadCourse(int id) {
		Course course = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			course = this.loadCourse(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading course with id {}", id, t);
			throw new RuntimeException("Error loading course with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return course;
	}

	public Course loadCourse(int id, Connection conn) {
		Course course = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_COURSE);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				course = this.buildCourseFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading course with id {}", id, t);
			throw new RuntimeException("Error loading course with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return course;
	}

	protected Course buildCourseFromRes(ResultSet res) {
		Course course = null;
		try {
			course = new Course();				
			course.setId(res.getInt("id"));
			course.setName(res.getString("name"));
			course.setSender(res.getString("sender"));
			course.setActive(res.getInt("active"));
			course.setCronexp(res.getString("cronexp"));
			course.setCrontimezoneid(res.getString("crontimezoneid"));
			Timestamp createdatValue = res.getTimestamp("createdat");
			if (null != createdatValue) {
				course.setCreatedat(new Date(createdatValue.getTime()));
			}
			Timestamp updatedatValue = res.getTimestamp("updatedat");
			if (null != updatedatValue) {
				course.setUpdatedat(new Date(updatedatValue.getTime()));
			}
		} catch (Throwable t) {
			_logger.error("Error in buildCourseFromRes", t);
		}
		return course;
	}

	private static final String ADD_COURSE = "INSERT INTO jpemailmarketing_course (id, name, sender, active, cronexp, crontimezoneid, createdat, updatedat ) VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_COURSE = "UPDATE jpemailmarketing_course SET  name=?,  sender=?,  active=?,  cronexp=?,  crontimezoneid=?,  createdat=?, updatedat=? WHERE id = ?";

	private static final String DELETE_COURSE = "DELETE FROM jpemailmarketing_course WHERE id = ?";
	
	private static final String LOAD_COURSE = "SELECT id, name, sender, active, cronexp, crontimezoneid, createdat, updatedat  FROM jpemailmarketing_course WHERE id = ?";
	
	private static final String LOAD_COURSES_ID  = "SELECT id FROM jpemailmarketing_course";

	private static final String LOAD_ACTIVE_COURSES_ID  = LOAD_COURSES_ID + " WHERE active = ?";
	
}