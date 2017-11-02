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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.plugins.jpsalesforce.apsadmin.portal.specialwidget.form.model.FormConfiguration;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;



public class SalesforceFormDAO extends AbstractSearcherDAO implements ISalesforceFormDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceFormDAO.class);
	
	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpsalesforce_formconfig";
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
	public List<Integer> searchSalesforceForms(FieldSearchFilter[] filters) {
		List salesforceFormsId = null;
		List<Integer> result = new ArrayList<Integer>();
		try {
			salesforceFormsId  = super.searchId(filters);
			Iterator itr = salesforceFormsId.iterator();
			
			while (itr.hasNext()) {
				String value = (String) itr.next();
				result.add(Integer.valueOf(value));
			}
		} catch (Throwable t) {
			throw new RuntimeException("error in searchSalesforceForms", t);
		}
		return result;
	}

	@Override
	public List<Integer> loadSalesforceForms() {
		List<Integer> salesforceFormsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SALESFORCEFORMS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				salesforceFormsId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("Error loading the usernames list",  t);
			throw new RuntimeException("Error loading the usernames list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return salesforceFormsId;
	}
	
	@Override
	public void insertSalesforceForm(SalesforceForm salesforceForm) {
		PreparedStatement stat = null;
		Connection conn  = null;
		int id = -1;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			id = this.getNextId(conn);
			salesforceForm.setId(id); 
			this.insertSalesforceForm(salesforceForm, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error creating salesforceForm", t);
			throw new RuntimeException("Error creating salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	private int getNextId(Connection conn) throws Throwable {
		int id = 0;
		PreparedStatement stat = conn.prepareStatement(LOAD_SALESFORCEFORMS_MAX_ID);
		
		ResultSet res = stat.executeQuery();
		if (res.next()) {
			id = res.getInt("id");
		}
		return ++id;
	}

	public void insertSalesforceForm(SalesforceForm salesforceForm, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SALESFORCEFORM);
			int index = 1;
			stat.setInt(index++, salesforceForm.getId());
 			stat.setString(index++, salesforceForm.getCode());
 			stat.setString(index++, salesforceForm.getPagecode());
			stat.setInt(index++, salesforceForm.getFrame());
 			stat.setString(index++, salesforceForm.getConfig().toXML());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error creating salesforceForm", t);
			throw new RuntimeException("Error creating salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateSalesforceForm(SalesforceForm salesforceForm) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSalesforceForm(salesforceForm, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating salesforceForm '{}'", salesforceForm.getId());
			_logger.error("Error updating salesforceForm", t);
			throw new RuntimeException("Error updating salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateSalesforceForm(SalesforceForm salesforceForm, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SALESFORCEFORM);
			int index = 1;

 			stat.setString(index++, salesforceForm.getCode());
 			stat.setString(index++, salesforceForm.getPagecode());
			stat.setInt(index++, salesforceForm.getFrame());
 			stat.setString(index++, salesforceForm.getConfig().toXML());
			stat.setInt(index++, salesforceForm.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating salesforceForm  '{}' updateSalesforceForm", salesforceForm.getId());
			_logger.error("Error updating salesforceForm", t);
			throw new RuntimeException("Error updating salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeSalesforceForm(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeSalesforceForm(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting salesforceForm '{}' removeSalesforceForm", id);
			_logger.error("Error deleting salesforceForm ", t);
			throw new RuntimeException("Error deleting salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeSalesforceForm(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_SALESFORCEFORM);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting salesforceForm with id '{}' removeSalesforceForm", id);
			_logger.error("Error deleting salesforceForm", t);
			throw new RuntimeException("Error deleting salesforceForm", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public SalesforceForm loadSalesforceForm(int id) {
		SalesforceForm salesforceForm = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			salesforceForm = this.loadSalesforceForm(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading salesforceForm with id '{}' loadSalesforceForm", id);
			_logger.error("Error loading salesforceForm", t);
			throw new RuntimeException("Error loading salesforceForm", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return salesforceForm;
	}

	public SalesforceForm loadSalesforceForm(int id, Connection conn) {
		SalesforceForm salesforceForm = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SALESFORCEFORM);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				salesforceForm = this.buildSalesforceFormFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading salesforceForm with id '{}' loadSalesforceForm", id);
			_logger.error("Error loading salesforceForm", t);
			throw new RuntimeException("Error loading salesforceForm", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return salesforceForm;
	}

	protected SalesforceForm buildSalesforceFormFromRes(ResultSet res) {
		SalesforceForm salesforceForm = null;
		try {
			salesforceForm = new SalesforceForm();
			salesforceForm.setId(res.getInt("id"));
			salesforceForm.setCode(res.getString("code"));
			salesforceForm.setPagecode(res.getString("pagecode"));
			salesforceForm.setFrame(res.getInt("frame"));
			String xml = res.getString("config");
			JSONObject json = XML.toJSONObject(xml);
			FormConfiguration form = new FormConfiguration(json);
			salesforceForm.setConfig(form);
		} catch (Throwable t) {
			_logger.error("Error parsing the response  when loading an existing form configuration", t);
			throw new RuntimeException("Error in buildSalesforceFormFromRes", t);
		}
		return salesforceForm;
	}
	
	private static final String ADD_SALESFORCEFORM = "INSERT INTO jpsalesforce_formconfig (id, code, pagecode, frame, config ) VALUES (?, ?, ?, ?, ? )";

	private static final String UPDATE_SALESFORCEFORM = "UPDATE jpsalesforce_formconfig SET  code=?,  pagecode=?,  frame=?, config=? WHERE id = ?";

	private static final String DELETE_SALESFORCEFORM = "DELETE FROM jpsalesforce_formconfig WHERE id = ?";
	
	private static final String LOAD_SALESFORCEFORM = "SELECT id, code, pagecode, frame, config  FROM jpsalesforce_formconfig WHERE id = ?";
	
	private static final String LOAD_SALESFORCEFORMS_ID  = "SELECT id FROM jpsalesforce_formconfig";
	
	private static final String LOAD_SALESFORCEFORMS_MAX_ID  = "SELECT id FROM jpsalesforce_formconfig ORDER BY id DESC";
}