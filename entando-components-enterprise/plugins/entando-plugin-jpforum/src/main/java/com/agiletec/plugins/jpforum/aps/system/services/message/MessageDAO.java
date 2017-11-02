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
package com.agiletec.plugins.jpforum.aps.system.services.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;

public class MessageDAO extends AbstractDAO implements IMessageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(MessageDAO.class);

	@Override
	public void insertMessage(Message message) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_MESSAGE);
			stat.setInt(1, message.getCode());
			stat.setString(2, message.getSender());
			stat.setString(3, message.getRecipient());
			stat.setString(4, message.getTitle());
			stat.setString(5, message.getBody());
			stat.setString(6, message.getMessageType());
			stat.setTimestamp(7, new Timestamp(message.getMessageDate().getTime()));
			stat.setString(8, message.isToRead() ? "true" : "false");
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in inserimento Messaggio",  t);
			throw new RuntimeException("Errore in inserimento Messaggio", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeMessage(int code) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_MESSAGE);
			stat.setInt(1, code);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			
			_logger.error("Errore in eliminazione Messaggio {}", code,  t);
			throw new RuntimeException("Errore in eliminazione Messaggio ", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeMessages(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_MESSAGES);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in eliminazione messaggio per {}", username,  t);
			throw new RuntimeException("Errore in eliminazione messaggio", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public Message loadMessage(int code) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Message message = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MESSAGE);
			stat.setInt(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				message = this.createMessageFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento message ", code,  t);
			throw new RuntimeException("Errore in caricamento message", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return message;
	}

	private Message createMessageFromRes(ResultSet res) throws Throwable {
		Message message = new Message();
		message.setBody(res.getString("body"));
		message.setCode(res.getInt("code"));
		message.setMessageDate(new Date(res.getTimestamp("messageDate").getTime()));
		message.setMessageType(res.getString("messageType"));
		message.setRecipient(res.getString("recipient"));
		message.setSender(res.getString("sender"));
		message.setTitle(res.getString("title"));
		message.setToRead(res.getString("toread").equalsIgnoreCase("true"));
		return message;
	}

	@Override
	public Message readMessage(int code) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Message message = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateReadStatus(code, false, conn);
			stat = conn.prepareStatement(LOAD_MESSAGE);
			stat.setInt(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				message = this.createMessageFromRes(res);
			}
			//stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			_logger.error("Errore in caricamento message {}", code,  t);
			throw new RuntimeException("Errore in caricamento message", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return message;
	}

	private void updateReadStatus(int code, boolean toread, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(READ_MESSAGE);
			stat.setString(1, toread ? "true" : "false");
			stat.setInt(2, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in updateReadStatus per messaggio {}", code,  t);
			throw new RuntimeException("Errore in updateReadStatus per messaggio", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	@Override
	public Map<String, List<Integer>> loadIncomingMessages(String recipient) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, List<Integer>> incomingMessages = new HashMap<String, List<Integer>>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_INCOMING_MESSAGES);
			stat.setString(1, recipient);
			res = stat.executeQuery();
			while (res.next()) {
				String type = res.getString("messagetype");
				if (!incomingMessages.containsKey(type)) {
					incomingMessages.put(type, new ArrayList<Integer>());
				}
				incomingMessages.get(type).add(res.getInt("code"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento messaggi ricevuti per {}", recipient,  t);
			throw new RuntimeException("Errore in caricamento messaggi ricevuti", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return incomingMessages;
	}

	@Override
	public Map<String, Integer> loadNewMessages(String recipient) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> newMessages = new HashMap<String, Integer>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_NEW_MESSAGES);
			stat.setString(1, recipient);
			res = stat.executeQuery();
			while (res.next()) {
				String type = res.getString("messagetype");
				if (!newMessages.containsKey(type)) {
					newMessages.put(type, res.getInt("messages"));
				}
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento nuovi messaggi per {}", recipient,  t);
			throw new RuntimeException("Errore in caricamento nuovi messaggi", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return newMessages;
	}
	
	private static final String INSERT_MESSAGE = 
		"INSERT INTO jpforum_messages(code, sender, recipient, title, body, messagetype, messagedate, toread) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String DELETE_MESSAGE = 
		"DELETE FROM jpforum_messages WHERE code=?";

	private static final String DELETE_MESSAGES = 
		"DELETE FROM jpforum_messages WHERE recipient=?";

	private static final String LOAD_MESSAGE = 
		"SELECT code, sender, recipient, title, body, messagetype, messagedate, toread FROM jpforum_messages WHERE code = ?";

	private static final String READ_MESSAGE = 
		"UPDATE jpforum_messages SET toread=? WHERE code = ?";
	
	private static final String LOAD_INCOMING_MESSAGES = 
		"SELECT code, messagetype FROM jpforum_messages WHERE recipient = ? ORDER BY messagedate DESC";

	private static final String LOAD_NEW_MESSAGES = 
		"SELECT count(code) as messages, messagetype " +
		"FROM jpforum_messages " +
		"WHERE recipient = ? AND toread = 'true' GROUP BY messagetype";
}
