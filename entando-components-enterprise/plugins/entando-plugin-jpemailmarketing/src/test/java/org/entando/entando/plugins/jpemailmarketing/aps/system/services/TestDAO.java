package org.entando.entando.plugins.jpemailmarketing.aps.system.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

public class TestDAO extends AbstractDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(TestDAO.class);
	
	public void fullUpdateSubscriber(Subscriber subscriber) {
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

	protected void updateSubscriber(Subscriber subscriber, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SUBSCRIBER);
			int index = 1;

			stat.setInt(index++, subscriber.getCourseId());
			stat.setString(index++, subscriber.getName());
			stat.setString(index++, subscriber.getEmail());
			stat.setString(index++, subscriber.getHash());
			stat.setString(index++, subscriber.getStatus());
			Timestamp createdatTimestamp = new Timestamp(subscriber.getCreatedat().getTime());
			stat.setTimestamp(index++, createdatTimestamp);	
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

	

	private static final String UPDATE_SUBSCRIBER = "UPDATE jpemailmarketing_subscriber SET courseid=?,  name=?,  email=?, hash=?,  status=?, createdat=?, updatedat=? WHERE id = ?";
}
