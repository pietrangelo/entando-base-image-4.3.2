package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.mock;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursequeue.CourseQueueDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockCourseQueueDAO extends CourseQueueDAO implements IMockCourseQueueDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(MockCourseQueueDAO.class);
	
	@Override
	public void deleteQueue() {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement("delete from jpemailmarketing_coursequeue");
			//int index = 1;
			
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting courseQueue ", t);
			throw new RuntimeException("Error deleting courseQueue", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
		
	}



}
