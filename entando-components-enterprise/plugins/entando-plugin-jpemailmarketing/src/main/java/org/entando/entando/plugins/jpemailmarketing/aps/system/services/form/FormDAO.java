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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.form;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;

public class FormDAO extends AbstractSearcherDAO implements IFormDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(FormDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpemailmarketing_form";
	}
	
	@Override
	protected String getMasterTableIdFieldName() {
		return "courseId";
	}
	
	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}

	@Override
	public List<Integer> searchForms(FieldSearchFilter[] filters) {
		List<Integer> formsId = new ArrayList<Integer>();
		try {
			//XXX avoid class cast exception 
			List<String> formsIdString = super.searchId(filters);
			if (null != formsIdString) {
				for (int i = 0; i < formsIdString.size(); i++) {
					formsId.add(new Integer(formsIdString.get(i)));
				}
			}
		} catch (Throwable t) {
			_logger.error("error in searchForms",  t);
			throw new RuntimeException("error in searchForms", t);
		}
		return formsId;
	}

	@Override
	public List<Integer> loadForms() {
		List<Integer> formsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_FORMS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int courseId = res.getInt("courseid");
				formsId.add(courseId);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Form list",  t);
			throw new RuntimeException("Error loading Form list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return formsId;
	}
	
	@Override
	public void insertForm(Form form) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertForm(form, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert form",  t);
			throw new RuntimeException("Error on insert form", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertForm(Form form, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_FORM);
			int index = 1;
			stat.setInt(index++, form.getCourseId());
 			if(StringUtils.isNotBlank(form.getFileCoverName())) {
				stat.setString(index++, form.getFileCoverName());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(form.getFileIncentiveName())) {
				stat.setString(index++, form.getFileIncentiveName());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, form.getEmailSubject());
 			stat.setString(index++, form.getEmailText());
 			stat.setString(index++, form.getEmailButton());
 			if(StringUtils.isNotBlank(form.getEmailMessageFriendly())) {
				stat.setString(index++, form.getEmailMessageFriendly());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if(null != form.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(form.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			if(null != form.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(form.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert form",  t);
			throw new RuntimeException("Error on insert form", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateForm(Form form) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateForm(form, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating form {}", form.getCourseId(),  t);
			throw new RuntimeException("Error updating form", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateForm(Form form, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_FORM);
			int index = 1;

 			if(StringUtils.isNotBlank(form.getFileCoverName())) {
				stat.setString(index++, form.getFileCoverName());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(form.getFileIncentiveName())) {
				stat.setString(index++, form.getFileIncentiveName());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, form.getEmailSubject());
 			stat.setString(index++, form.getEmailText());
 			stat.setString(index++, form.getEmailButton());
 			if(StringUtils.isNotBlank(form.getEmailMessageFriendly())) {
				stat.setString(index++, form.getEmailMessageFriendly());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			/*
 			if(null != form.getCreatedat()) {
				Timestamp createdatTimestamp = new Timestamp(form.getCreatedat().getTime());
				stat.setTimestamp(index++, createdatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}*/
 			if(null != form.getUpdatedat()) {
				Timestamp updatedatTimestamp = new Timestamp(form.getUpdatedat().getTime());
				stat.setTimestamp(index++, updatedatTimestamp);	
			} else {
				stat.setNull(index++, Types.DATE);
			}
 			stat.setInt(index++, form.getCourseId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating form {}", form.getCourseId(),  t);
			throw new RuntimeException("Error updating form", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeForm(int courseId) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeForm(courseId, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting form {}", courseId, t);
			throw new RuntimeException("Error deleting form", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeForm(int courseId, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_FORM);
			int index = 1;
			stat.setInt(index++, courseId);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting form {}", courseId, t);
			throw new RuntimeException("Error deleting form", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Form loadForm(int courseId) {
		Form form = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			form = this.loadForm(courseId, conn);
		} catch (Throwable t) {
			_logger.error("Error loading form with courseId {}", courseId, t);
			throw new RuntimeException("Error loading form with courseId " + courseId, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return form;
	}

	public Form loadForm(int courseId, Connection conn) {
		Form form = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_FORM);
			int index = 1;
			stat.setInt(index++, courseId);
			res = stat.executeQuery();
			if (res.next()) {
				form = this.buildFormFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading form with courseId {}", courseId, t);
			throw new RuntimeException("Error loading form with courseId " + courseId, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return form;
	}

	protected Form buildFormFromRes(ResultSet res) {
		Form form = null;
		try {
			form = new Form();				
			form.setCourseId(res.getInt("courseid"));
			form.setFileCoverName(res.getString("filecovername"));
			form.setFileIncentiveName(res.getString("fileincentivename"));
			form.setEmailSubject(res.getString("emailsubject"));
			form.setEmailText(res.getString("emailtext"));
			form.setEmailButton(res.getString("emailbutton"));
			form.setEmailMessageFriendly(res.getString("emailmessagefriendly"));
			Timestamp createdatValue = res.getTimestamp("createdat");
			if (null != createdatValue) {
				form.setCreatedat(new Date(createdatValue.getTime()));
			}
			Timestamp updatedatValue = res.getTimestamp("updatedat");
			if (null != updatedatValue) {
				form.setUpdatedat(new Date(updatedatValue.getTime()));
			}
		} catch (Throwable t) {
			_logger.error("Error in buildFormFromRes", t);
		}
		return form;
	}

	private static final String ADD_FORM = "INSERT INTO jpemailmarketing_form (courseid, filecovername, fileincentivename, emailsubject, emailtext, emailbutton, emailmessagefriendly, createdat, updatedat ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_FORM = "UPDATE jpemailmarketing_form SET  filecovername=?,  fileincentivename=?,  emailsubject=?,  emailtext=?, emailbutton=?,  emailmessagefriendly=?, updatedat=? WHERE courseid = ?";

	private static final String DELETE_FORM = "DELETE FROM jpemailmarketing_form WHERE courseid = ?";
	
	private static final String LOAD_FORM = "SELECT courseid, filecovername, fileincentivename, emailsubject, emailtext, emailbutton, emailmessagefriendly, createdat, updatedat  FROM jpemailmarketing_form WHERE courseid = ?";
	
	private static final String LOAD_FORMS_ID  = "SELECT courseid FROM jpemailmarketing_form";
	
}