/*
 *
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpmultisite.aps.system.services.content;

import org.entando.entando.plugins.jpmultisite.aps.system.services.content.model.SharedContent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedContentDAO extends AbstractSearcherDAO implements ISharedContentDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SharedContentDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpmultisite_sharedcontent";
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
	public List<Integer> searchSharedContents(FieldSearchFilter[] filters) {
		List<String> sharedContentsStringId = null;
		List<Integer> sharedContentsId = new ArrayList<Integer>();
		try {
			sharedContentsStringId  = super.searchId(filters);
			for (String element : sharedContentsStringId) {
				sharedContentsId.add(Integer.valueOf(element));
			}
		} catch (Throwable t) {
			_logger.error("error in searchSharedContents",  t);
			throw new RuntimeException("error in searchSharedContents", t);
		}
		return sharedContentsId;
	}

	@Override
	public List<Integer> loadSharedContents() {
		List<Integer> sharedContentsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SHAREDCONTENTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				sharedContentsId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading SharedContent list",  t);
			throw new RuntimeException("Error loading SharedContent list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sharedContentsId;
	}
	
	@Override
	public void insertSharedContent(SharedContent sharedContent) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertSharedContent(sharedContent, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert sharedContent",  t);
			throw new RuntimeException("Error on insert sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertSharedContent(SharedContent sharedContent, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SHAREDCONTENT);
			int index = 1;
			stat.setInt(index++, sharedContent.getId());
 			stat.setString(index++, sharedContent.getContentId());
 			stat.setString(index++, sharedContent.getMultisiteCodeSrc());
 			stat.setString(index++, sharedContent.getMultisiteCodeTo());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert sharedContent",  t);
			throw new RuntimeException("Error on insert sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateSharedContent(SharedContent sharedContent) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSharedContent(sharedContent, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating sharedContent {}", sharedContent.getId(),  t);
			throw new RuntimeException("Error updating sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateSharedContent(SharedContent sharedContent, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SHAREDCONTENT);
			int index = 1;
 			stat.setString(index++, sharedContent.getContentId());
 			stat.setString(index++, sharedContent.getMultisiteCodeSrc());
 			stat.setString(index++, sharedContent.getMultisiteCodeTo());
			stat.setInt(index++, sharedContent.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating sharedContent {}", sharedContent.getId(),  t);
			throw new RuntimeException("Error updating sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeSharedContent(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeSharedContent(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting sharedContent {}", id, t);
			throw new RuntimeException("Error deleting sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeSharedContent(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_SHAREDCONTENT);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting sharedContent {}", id, t);
			throw new RuntimeException("Error deleting sharedContent", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public SharedContent loadSharedContent(int id) {
		SharedContent sharedContent = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			sharedContent = this.loadSharedContent(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading sharedContent with id {}", id, t);
			throw new RuntimeException("Error loading sharedContent with id " + id, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sharedContent;
	}

	public SharedContent loadSharedContent(int id, Connection conn) {
		SharedContent sharedContent = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SHAREDCONTENT);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				sharedContent = this.buildSharedContentFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading sharedContent with id {}", id, t);
			throw new RuntimeException("Error loading sharedContent with id " + id, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return sharedContent;
	}

	protected SharedContent buildSharedContentFromRes(ResultSet res) {
		SharedContent sharedContent = null;
		try {
			sharedContent = new SharedContent();				
			sharedContent.setId(res.getInt("id"));
			sharedContent.setContentId(res.getString("contentid"));
			sharedContent.setMultisiteCodeSrc(res.getString("multisitecodesrc"));
			sharedContent.setMultisiteCodeTo(res.getString("multisitecodeto"));
		} catch (Throwable t) {
			_logger.error("Error in buildSharedContentFromRes", t);
		}
		return sharedContent;
	}

	private static final String ADD_SHAREDCONTENT = "INSERT INTO jpmultisite_sharedcontent (id, contentid, multisitecodesrc, multisitecodeto ) VALUES (?, ?, ?, ? )";

	private static final String UPDATE_SHAREDCONTENT = "UPDATE jpmultisite_sharedcontent SET  contentid=?,  multisitecodesrc=?, multisitecodeto=? WHERE id = ?";

	private static final String DELETE_SHAREDCONTENT = "DELETE FROM jpmultisite_sharedcontent WHERE id = ?";
	
	private static final String LOAD_SHAREDCONTENT = "SELECT id, contentid, multisitecodesrc, multisitecodeto  FROM jpmultisite_sharedcontent WHERE id = ?";
	
	private static final String LOAD_SHAREDCONTENTS_ID  = "SELECT id FROM jpmultisite_sharedcontent";
	
}