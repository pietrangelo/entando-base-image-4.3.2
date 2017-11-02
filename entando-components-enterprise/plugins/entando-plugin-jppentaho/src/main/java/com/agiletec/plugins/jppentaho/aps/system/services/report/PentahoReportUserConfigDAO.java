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
package com.agiletec.plugins.jppentaho.aps.system.services.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.plugins.jppentaho.aps.system.services.report.model.PentahoReportUserWidgetConfig;

/**
 * DAP for User config for showlets of Pentaho Dynamic reports
 * 
 * @author zuanni
 * */
public class PentahoReportUserConfigDAO extends AbstractDAO implements IPentahoReportUserConfigDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoReportUserConfigDAO.class);
	
	@Override
	public PentahoReportUserWidgetConfig getConfig(PentahoReportUserWidgetConfig config) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		PentahoReportUserWidgetConfig configLoaded = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET);
			stat.setString(1, config.getUsername());
			stat.setString(2, config.getPagecode());
			stat.setInt(3, config.getFramepos());
			res = stat.executeQuery();
			if (res.next()) {
				configLoaded = this.loadConfig(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading the config",  t);
			throw new RuntimeException("Error loading the config", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return configLoaded;
	}
	
	private PentahoReportUserWidgetConfig loadConfig(ResultSet res) throws SQLException {
		PentahoReportUserWidgetConfig config = new PentahoReportUserWidgetConfig();
		config.setConfig(res.getString("config"));
		config.setFramepos(res.getInt("framepos"));
		config.setPagecode(res.getString("pagecode"));
		config.setWidgetcode(res.getString("widgetcode"));
		config.setUsername(res.getString("authuser"));
		config.setName(res.getString("name"));
		return config;
	}
	
	@Override
	public void addConfig(PentahoReportUserWidgetConfig config) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(INSERT);
			stat.setString(1, config.getUsername());
			stat.setString(2, config.getPagecode());
			stat.setInt(3, config.getFramepos());
			stat.setString(4, config.getWidgetcode());
			stat.setString(5, config.getConfig());
			if (null != config.getName() && config.getName().length() > 0 ) {
				stat.setString(6, config.getName());
			} else {
				stat.setNull(6, java.sql.Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error adding the config",  t);
			throw new RuntimeException("Error adding the config", t);
			//processDaoException(t, "Error adding the config", "addConfig");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	@Override
	public void deleteConfig(PentahoReportUserWidgetConfig config) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(DELETE);
			stat.setString(1, config.getUsername());
			stat.setString(2, config.getPagecode());
			stat.setInt(3, config.getFramepos());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error removing the config",  t);
			throw new RuntimeException("Error removing the config", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	@Override
	public void updateConfig(PentahoReportUserWidgetConfig config) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(UPDATE);
			stat.setString(1, config.getConfig());
			if (null != config.getName() && config.getName().length() > 0 ) {
				stat.setString(2, config.getName());
			} else {
				stat.setNull(2, java.sql.Types.VARCHAR);
			}
			stat.setString(3, config.getUsername());
			stat.setString(4, config.getPagecode());
			stat.setInt(5, config.getFramepos());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating the config", t);
			throw new RuntimeException("Error updating the config", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private String GET = 
		"SELECT authuser, pagecode, framepos, widgetcode, config, name FROM jppentaho_showletconfig " +
		"WHERE authuser=? AND pagecode=? AND framepos=? ";
	
	private String INSERT = "INSERT INTO jppentaho_showletconfig(authuser, pagecode, framepos, widgetcode, config, name)" +
    	" VALUES (?, ?, ?, ?, ?, ?)"; 
	
    private String UPDATE = "UPDATE jppentaho_showletconfig SET config=?, name= ? WHERE authuser=? AND pagecode=? AND framepos=? ";
    
	private String DELETE = "DELETE FROM jppentaho_showletconfig WHERE authuser=? AND pagecode=? AND framepos=? ";
	
}