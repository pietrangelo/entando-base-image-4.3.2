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
package org.entando.entando.plugins.jpmultisite.aps.system.services.multisite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.agiletec.aps.system.common.AbstractSearcherDAO;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.util.ApsProperties;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultisiteDAO extends AbstractSearcherDAO implements IMultisiteDAO {

	private static final Logger _logger = LoggerFactory.getLogger(MultisiteDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}

	@Override
	protected String getMasterTableName() {
		return "jpmultisite_multisite";
	}

	@Override
	protected String getMasterTableIdFieldName() {
		return "code";
	}

	@Override
	protected boolean isForceCaseInsensitiveLikeSearch() {
		return true;
	}

	@Override
	public List<String> searchMultisites(FieldSearchFilter[] filters) {
		List multisitesId = null;
		try {
			multisitesId = super.searchId(filters);
		} catch (Throwable t) {
			_logger.error("error in searchMultisites", t);
			throw new RuntimeException("error in searchMultisites", t);
		}
		return multisitesId;
	}

	@Override
	public List<String> loadMultisitesCode() {
		List<String> multisitesId = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MULTISITES_ID);
			res = stat.executeQuery();
			while (res.next()) {
				String code = res.getString("code");
				multisitesId.add(code);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Multisite list", t);
			throw new RuntimeException("Error loading Multisite list", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return multisitesId;
	}

	@Override
	public List<Multisite> loadMultisites() {
		List<Multisite> multisites = new ArrayList<Multisite>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MULTISITES);
			res = stat.executeQuery();
			while (res.next()) {
				Multisite multisite = multisiteFromResult(res);
				multisites.add(multisite);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Multisites", t);
			throw new RuntimeException("Error loading Multisites", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return multisites;
	}

	@Override
	public Map<String, Multisite> loadMultisitesMap() {
		Map<String, Multisite> multisitesMap = new HashMap<String, Multisite>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_MULTISITES);
			res = stat.executeQuery();
			while (res.next()) {
				Multisite multisite = multisiteFromResult(res);
				multisitesMap.put(multisite.getCode(), multisite);
			}
		} catch (Throwable t) {
			_logger.error("Error loading Multisites", t);
			throw new RuntimeException("Error loading Multisites", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return multisitesMap;
	}

	private Multisite multisiteFromResult(ResultSet res) throws Exception {
		Multisite multisite = new Multisite();
		multisite.setCode(res.getString("code"));
		ApsProperties titles = new ApsProperties();
		titles.loadFromXml(res.getString("titles"));
		multisite.setTitles(titles);
		ApsProperties descriptions = new ApsProperties();
		descriptions.loadFromXml(res.getString("descriptions"));
		multisite.setDescriptions(descriptions);
		multisite.setUrl(res.getString("url"));
		multisite.setHeaderImage(res.getString("headerimage"));
		multisite.setTemplateCss(res.getString("templatecss"));
		multisite.setCatCode(res.getString("catcode"));
		return multisite;
	}

	@Override
	public void insertMultisite(Multisite multisite) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertMultisite(multisite, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error on insert multisite", t);
			throw new RuntimeException("Error on insert multisite", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertMultisite(Multisite multisite, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_MULTISITE);
			int index = 1;
			stat.setString(index++, multisite.getCode());
			ApsProperties titles = multisite.getTitles();
			stat.setString(index++, titles != null ? titles.toXml() : null);
			ApsProperties descriptions = multisite.getDescriptions();
			stat.setString(index++, descriptions != null ? descriptions.toXml() : null);
			if (StringUtils.isNotBlank(multisite.getUrl())) {
				stat.setString(index++, multisite.getUrl());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getHeaderImage())) {
				stat.setString(index++, multisite.getHeaderImage());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getTemplateCss())) {
				stat.setString(index++, multisite.getTemplateCss());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getCatCode())) {
				stat.setString(index++, multisite.getCatCode());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error on insert multisite", t);
			throw new RuntimeException("Error on insert multisite", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateMultisite(Multisite multisite) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateMultisite(multisite, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating multisite {}", multisite.getCode(), t);
			throw new RuntimeException("Error updating multisite", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateMultisite(Multisite multisite, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_MULTISITE);
			int index = 1;
			ApsProperties titles = multisite.getTitles();
			stat.setString(index++, titles != null ? titles.toXml() : null);
			ApsProperties descriptions = multisite.getDescriptions();
			stat.setString(index++, descriptions != null ? descriptions.toXml() : null);
			if (StringUtils.isNotBlank(multisite.getUrl())) {
				stat.setString(index++, multisite.getUrl());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getHeaderImage())) {
				stat.setString(index++, multisite.getHeaderImage());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getTemplateCss())) {
				stat.setString(index++, multisite.getTemplateCss());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			if (StringUtils.isNotBlank(multisite.getCatCode())) {
				stat.setString(index++, multisite.getCatCode());
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
			stat.setString(index++, multisite.getCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating multisite {}", multisite.getCode(), t);
			throw new RuntimeException("Error updating multisite", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeMultisite(String code) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeMultisite(code, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting multisite {}", code, t);
			throw new RuntimeException("Error deleting multisite", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void removeMultisite(String code, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_MULTISITE);
			int index = 1;
			stat.setString(index++, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting multisite {}", code, t);
			throw new RuntimeException("Error deleting multisite", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public Multisite loadMultisite(String code) {
		Multisite multisite = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			multisite = this.loadMultisite(code, conn);
		} catch (Throwable t) {
			_logger.error("Error loading multisite with code {}", code, t);
			throw new RuntimeException("Error loading multisite with code " + code, t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return multisite;
	}

	public Multisite loadMultisite(String code, Connection conn) {
		Multisite multisite = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_MULTISITE);
			int index = 1;
			stat.setString(index++, code);
			res = stat.executeQuery();
			if (res.next()) {
				multisite = this.buildMultisiteFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading multisite with code {}", code, t);
			throw new RuntimeException("Error loading multisite with code " + code, t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return multisite;
	}

	protected Multisite buildMultisiteFromRes(ResultSet res) {
		Multisite multisite = null;
		try {
			multisite = new Multisite();
			multisite.setCode(res.getString("code"));
			String titlesString = res.getString("titles");
			ApsProperties titles = new ApsProperties();
			if (titlesString != null) {
				titles.loadFromXml(titlesString);
			}
			multisite.setTitles(titles);
			String descriptionsString = res.getString("descriptions");
			ApsProperties descriptions = new ApsProperties();
			if (descriptionsString != null) {
				descriptions.loadFromXml(descriptionsString);
			}
			multisite.setDescriptions(descriptions);
			multisite.setUrl(res.getString("url"));
			multisite.setHeaderImage(res.getString("headerimage"));
			multisite.setTemplateCss(res.getString("templatecss"));
			multisite.setCatCode(res.getString("catcode"));
		} catch (Throwable t) {
			_logger.error("Error in buildMultisiteFromRes", t);
		}
		return multisite;
	}

	private static final String ADD_MULTISITE = "INSERT INTO jpmultisite_multisite (code, titles, descriptions, url, headerimage, templatecss, catcode ) VALUES (?, ?, ?, ?, ?, ?, ? )";

	private static final String UPDATE_MULTISITE = "UPDATE jpmultisite_multisite SET  titles=?,  descriptions=?,  url=?,  headerimage=?,  templatecss=?, catcode=? WHERE code = ?";

	private static final String DELETE_MULTISITE = "DELETE FROM jpmultisite_multisite WHERE code = ?";

	private static final String LOAD_MULTISITE = "SELECT code, titles, descriptions, url, headerimage, templatecss, catcode  FROM jpmultisite_multisite WHERE code = ?";

	private static final String LOAD_MULTISITES = "SELECT code, titles, descriptions, url, headerimage, templatecss, catcode  FROM jpmultisite_multisite";

	private static final String LOAD_MULTISITES_ID = "SELECT code FROM jpmultisite_multisite";

}
