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
package com.agiletec.plugins.jpforum.aps.system.services.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

public class StatisticDAO extends AbstractDAO implements IStatisticDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(StatisticDAO.class);
	
	@Override
	public void createRecord(int threadCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_RECORD);
			stat.setInt(1, threadCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in creazione record statistiche per thread {}", threadCode,  t);
			throw new RuntimeException("Errore in creazione record statistiche per thread", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void addView(int threadCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_VIEW);
			stat.setInt(1, threadCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore aggiunta visita per thread {}", threadCode,  t);
			throw new RuntimeException("Errore aggiunta visita per thread ", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}


	@Override
	public void deleteThreadStatistics(int threadCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_THREAD);
			stat.setInt(1, threadCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore eliminazione post in statistiche per thread {}", threadCode,  t);
			throw new RuntimeException("Errore eliminazione post in statistiche per thread", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public int loadViews(int postCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int views = 0;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_VIEWS);
			stat.setInt(1, postCode);
			res = stat.executeQuery();
			if (res.next()) {
				views = res.getInt("views");
			}
		} catch (Throwable t) {
			_logger.error("Errore caricamento statistiche per thread {}", postCode,  t);
			throw new RuntimeException("Errore caricamento statistiche per thread", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return views;
	}

	@Override
	public MostOnlineUsersRecord loadMostOnlineUsers() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		MostOnlineUsersRecord mostOnlineUsersRecord = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MOST_ONLINE_USERS);
			res = stat.executeQuery();
			if (res.next()) {
				mostOnlineUsersRecord = new MostOnlineUsersRecord();
				mostOnlineUsersRecord.setCount(res.getInt("users_count"));
				mostOnlineUsersRecord.setRecordDate(res.getTimestamp("record_date"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading most onlineUsers",  t);
			throw new RuntimeException("Error loading most onlineUsers", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mostOnlineUsersRecord;
	}

	@Override
	public void addMostOnlineUsers(MostOnlineUsersRecord record) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_MOST_ONLINE_USERS);
			stat.setInt(1, record.getCount());
			stat.setTimestamp(2, new Timestamp(record.getRecordDate().getTime()));
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error creating the MostOnlineUsersRecord",  t);
			throw new RuntimeException("Error creating the MostOnlineUsersRecord", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateMostOnlineUsers(MostOnlineUsersRecord record) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_MOST_ONLINE_USERS);
			stat.setInt(1, record.getCount());
			stat.setTimestamp(2, new Timestamp(record.getRecordDate().getTime()));
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating the MostOnlineUsersRecord",  t);
			throw new RuntimeException("Error updating the MostOnlineUsersRecord", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	private static final String ADD_RECORD = 
		"INSERT INTO jpforum_statistics (thread, views) values (?, 0)";

	private static final String ADD_VIEW = 
		"UPDATE jpforum_statistics SET views = views + 1 WHERE thread=?";

	private static final String DELETE_THREAD = 
		"DELETE FROM jpforum_statistics WHERE thread=?";

	private static final String LOAD_VIEWS = 
		"SELECT views FROM jpforum_statistics WHERE thread=?";

	private static final String ADD_MOST_ONLINE_USERS = 
			"INSERT INTO jpforum_statistics_usersonline (users_count, record_date) VALUES (?, ?)";

	private static final String UPDATE_MOST_ONLINE_USERS = 
			"UPDATE jpforum_statistics_usersonline SET users_count = ?, record_date = ?";

	private static final String LOAD_MOST_ONLINE_USERS = 
			"SELECT users_count, record_date FROM jpforum_statistics_usersonline";
}
