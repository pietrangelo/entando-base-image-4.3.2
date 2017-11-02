/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify;

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

public class NotifyDAO extends AbstractSearcherDAO implements INotifyDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifyDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpnotify_notify";
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
	public List<Integer> searchNotifys(FieldSearchFilter[] filters) {
		List notifysId = null;
		try {
			notifysId  = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchNotifys",  t);
			throw new RuntimeException("error in searchNotifys", t);
		}
		return notifysId;
	}

	@Override
	public List<Integer> loadNotifys() {
		List<Integer> notifysId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_NOTIFYS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				notifysId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Notify list",  t);
			throw new RuntimeException("Error loading Notify list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return notifysId;
	}
	
	@Override
	public void insertNotify(Notify notify) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertNotify(notify, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert notify",  t);
			throw new RuntimeException("Error on insert notify", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertNotify(Notify notify, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_NOTIFY);
			int index = 1;
			stat.setInt(index++, notify.getId());
 			stat.setString(index++, notify.getPayload());
			Timestamp senddateTimestamp = new Timestamp(notify.getSenddate().getTime());
			stat.setTimestamp(index++, senddateTimestamp);	
 			stat.setInt(index++, notify.getAttempts());
			stat.setInt(index++, notify.getSent());
 			if(StringUtils.isNotBlank(notify.getSender())) {
				stat.setString(index++, notify.getSender());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(notify.getRecipient())) {
				stat.setString(index++, notify.getRecipient());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert notify",  t);
			throw new RuntimeException("Error on insert notify", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateNotify(Notify notify) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateNotify(notify, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating notify {}", notify.getId(),  t);
			throw new RuntimeException("Error updating notify", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateNotify(Notify notify, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_NOTIFY);
			int index = 1;

 			stat.setString(index++, notify.getPayload());
			Timestamp senddateTimestamp = new Timestamp(notify.getSenddate().getTime());
			stat.setTimestamp(index++, senddateTimestamp);	
 			stat.setInt(index++, notify.getAttempts());
			stat.setInt(index++, notify.getSent());
 			if(StringUtils.isNotBlank(notify.getSender())) {
				stat.setString(index++, notify.getSender());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(notify.getRecipient())) {
				stat.setString(index++, notify.getRecipient());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setInt(index++, notify.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating notify {}", notify.getId(),  t);
			throw new RuntimeException("Error updating notify", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeNotify(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeNotify(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting notify {}", id, t);
			throw new RuntimeException("Error deleting notify", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeNotify(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_NOTIFY);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting notify {}", id, t);
			throw new RuntimeException("Error deleting notify", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Notify loadNotify(int id) {
		Notify notify = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			notify = this.loadNotify(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading notify with id {}", id, t);
			throw new RuntimeException("Error loading notify with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return notify;
	}

	public Notify loadNotify(int id, Connection conn) {
		Notify notify = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_NOTIFY);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				notify = this.buildNotifyFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading notify with id {}", id, t);
			throw new RuntimeException("Error loading notify with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return notify;
	}

	protected Notify buildNotifyFromRes(ResultSet res) {
		Notify notify = null;
		try {
			notify = new Notify();				
			notify.setId(res.getInt("id"));
			notify.setPayload(res.getString("payload"));
			Timestamp senddateValue = res.getTimestamp("senddate");
			if (null != senddateValue) {
				notify.setSenddate(new Date(senddateValue.getTime()));
			}
			notify.setAttempts(res.getInt("attempts"));
			notify.setSent(res.getInt("sent"));
			notify.setSender(res.getString("sender"));
			notify.setRecipient(res.getString("recipient"));
		} catch (Throwable t) {
			_logger.error("Error in buildNotifyFromRes", t);
		}
		return notify;
	}

	private static final String ADD_NOTIFY = "INSERT INTO jpnotify_notify (id, payload, senddate, attempts, sent, sender, recipient ) VALUES (?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_NOTIFY = "UPDATE jpnotify_notify SET  payload=?,  senddate=?,  attempts=?,  sent=?,  sender=?, recipient=? WHERE id = ?";

	private static final String DELETE_NOTIFY = "DELETE FROM jpnotify_notify WHERE id = ?";
	
	private static final String LOAD_NOTIFY = "SELECT id, payload, senddate, attempts, sent, sender, recipient  FROM jpnotify_notify WHERE id = ?";
	
	private static final String LOAD_NOTIFYS_ID  = "SELECT id FROM jpnotify_notify";
	
}