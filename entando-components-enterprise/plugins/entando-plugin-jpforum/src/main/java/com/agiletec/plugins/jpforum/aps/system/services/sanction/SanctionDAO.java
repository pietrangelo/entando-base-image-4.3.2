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
package com.agiletec.plugins.jpforum.aps.system.services.sanction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

public class SanctionDAO extends AbstractDAO implements ISanctionDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SanctionDAO.class);

	@Override
	public void insertSanction(Sanction sanction) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_SANCTION);
			stat.setInt(1, sanction.getId());
			stat.setString(2, sanction.getUsername());
			stat.setTimestamp(3, new Timestamp(sanction.getStartDate().getTime()));
			stat.setTimestamp(4, new Timestamp(sanction.getEndDate().getTime()));
			stat.setString(5, sanction.getModerator());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in inserimento sanzione",  t);
			throw new RuntimeException("Errore in inserimento sanzione", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public Sanction loadSanction(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Sanction sanction = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SANCTION);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				sanction = this.createSanctionFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento sanzione {}", id,  t);
			throw new RuntimeException("Errore in caricamento sanzione", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sanction;
	}

	private Sanction createSanctionFromRes(ResultSet res) throws Throwable {
		Sanction sanction = null;
		try {
			//id, username, startdate, enddate, moderator
			sanction = new Sanction();
			sanction.setId(res.getInt("id"));
			sanction.setUsername(res.getString("username"));
			sanction.setStartDate(new Date(res.getTimestamp("startdate").getTime()));
			sanction.setEndDate(new Date(res.getTimestamp("enddate").getTime()));
			sanction.setModerator(res.getString("moderator"));
		} catch (Throwable t) {
			_logger.error("error in createSanctionFromRes", t);
			throw new Throwable(t);
		}
		return sanction;
	}

	@Override
	public List<Integer> loadSanctions(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> sanctions = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SANCTIONS);
			stat.setString(1, username);
			res = stat.executeQuery();
			while (res.next()) {
				sanctions.add(res.getInt("id"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento identificativi sanzioni per {}", username,  t);
			throw new RuntimeException("Errore in caricamento identificativi sanzioni", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sanctions;
	}

	@Override
	public void removeSanction(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_SANCTION);
			stat.setInt(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in rimozione sanzione",  t);
			throw new RuntimeException("Errore in rimozione sanzione", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeSanctions(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_SANCTIONS);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in rimozione sanzioni per {}", username,  t);
			throw new RuntimeException("Errore in rimozione sanzioni", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public boolean isUnderSanction(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> sanctions = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SANCTIONS_BY_DATE);
			stat.setString(1, username);
			stat.setTimestamp(2, new Timestamp(new Date().getTime()));
			stat.setTimestamp(3, new Timestamp(new Date().getTime()));
			res = stat.executeQuery();
			while (res.next()) {
				sanctions.add(res.getInt("id"));
			}
		} catch (Throwable t) {
			
			_logger.error("Errore in caricamento identificativi sanzioni per {}", username,  t);
			throw new RuntimeException("Errore in caricamento identificativi sanzioni per", t);
			//processDaoException(t, "Errore in caricamento identificativi sanzioni per " + username, "loadSanctions");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sanctions.size() > 0;
	}

	
	private static final String ADD_SANCTION = 
		"INSERT INTO jpforum_sanctions(id, username, startdate, enddate, moderator) VALUES (?, ?, ?, ?, ?)";

	private static final String DELETE_SANCTION = 
		"DELETE FROM jpforum_sanctions WHERE id = ?";

	private static final String DELETE_SANCTIONS = 
		"DELETE FROM jpforum_sanctions WHERE username = ?";

	private static final String LOAD_SANCTION = 
		"SELECT id, username, startdate, enddate, moderator FROM jpforum_sanctions WHERE id = ?";

	private static final String LOAD_SANCTIONS = 
		"SELECT id FROM jpforum_sanctions WHERE username = ? ORDER BY startdate DESC";

	private static final String LOAD_SANCTIONS_BY_DATE = 
		"SELECT id FROM jpforum_sanctions WHERE username = ? AND startdate <= ? and enddate >= ? ORDER BY startdate ASC";

}
