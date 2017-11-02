/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
 */
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.util.ApsProperties;
import java.sql.Types;
import java.util.HashMap;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

/**
 * DAO class for CRUD operations on subsites.
 *
 * @author E.Mezzano
 */
public class SubsiteDAO extends AbstractDAO implements ISubsiteDAO {

	@Override
	public Map<String, Subsite> loadSubsites() {
		Map<String, Subsite> subsites = new HashMap<String, Subsite>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_SUBSITES);
			while (res.next()) {
				Subsite subsite = this.createSubsite(res);
				subsites.put(subsite.getCode(), subsite);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading subsites", "loadSubsites");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return subsites;
	}

	@Override
	public String loadSubsiteCodeForContent(String contentId) {
		String subsiteId = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SUBSITECODES_FOR_CONTENT);
			stat.setString(1, contentId);
			res = stat.executeQuery();
			if (res.next()) {
				subsiteId = res.getString(1);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading subsite associated to the content of id " + contentId, "loadSubsiteForContent");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return subsiteId;
	}

	@Override
	public Subsite loadSubsite(String code) {
		Subsite subsite = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SUBSITE);
			stat.setString(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				subsite = this.createSubsite(res);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading subsite of code " + code, "loadSubsite");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return subsite;
	}

	@Override
	public String getSubsiteForPage(String pageCode) {
		String code = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SUBSITECODE_FROM_PAGE);
			stat.setString(1, pageCode);
			res = stat.executeQuery();
			if (res.next()) {
				code = res.getString(1);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error loading subsite code from page code " + pageCode, "getSubsiteFromPage");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return code;
	}

	@Override
	public void addSubsite(Subsite subsite) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_SUBSITE);
			stat.setString(1, subsite.getCode());
			ApsProperties titles = subsite.getTitles();
			if (titles != null && !titles.isEmpty()) {
				stat.setString(2, titles.toXml());
			} else {
				stat.setNull(2, Types.VARCHAR);
			}
			//stat.setString(2, titles != null ? titles.toXml() : null);
			ApsProperties descriptions = subsite.getDescriptions();
			if (descriptions != null && !descriptions.isEmpty()) {
				stat.setString(3, descriptions.toXml());
			} else {
				stat.setNull(3, Types.VARCHAR);
			}
			//stat.setString(3, descriptions != null ? descriptions.toXml() : null);
			stat.setString(4, subsite.getHomepage());
			stat.setString(5, subsite.getGroupName());
			stat.setString(6, subsite.getCategoryCode());
			stat.setString(7, subsite.getContentViewerPage());
			stat.setString(8, subsite.getCssFileName());
			stat.setString(9, subsite.getImageFileName());
			ApsProperties viewerPages = subsite.getViewerPages();
			if (viewerPages != null && !viewerPages.isEmpty()) {
				stat.setString(10, viewerPages.toXml());
			} else {
				stat.setNull(10, Types.VARCHAR);
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Error adding subsite", "addSubsite");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updateSubsite(Subsite subsite) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_SUBSITE);
			ApsProperties titles = subsite.getTitles();
			if (titles != null && !titles.isEmpty()) {
				stat.setString(1, titles.toXml());
			} else {
				stat.setNull(1, Types.VARCHAR);
			}
			ApsProperties descriptions = subsite.getDescriptions();
			if (descriptions != null && !descriptions.isEmpty()) {
				stat.setString(2, descriptions.toXml());
			} else {
				stat.setNull(2, Types.VARCHAR);
			}
			stat.setString(3, subsite.getHomepage());
			stat.setString(4, subsite.getGroupName());
			stat.setString(5, subsite.getCategoryCode());
			stat.setString(6, subsite.getContentViewerPage());
			stat.setString(7, subsite.getCssFileName());
			stat.setString(8, subsite.getImageFileName());
			int visibility = (subsite.getVisibility()) ? 1 : 0;
			stat.setInt(9, visibility);
			ApsProperties viewerPages = subsite.getViewerPages();
			if (viewerPages != null && !viewerPages.isEmpty()) {
				stat.setString(10, viewerPages.toXml());
			} else {
				stat.setNull(10, Types.VARCHAR);
			}
			stat.setString(11, subsite.getCode());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Error updating subsite", "updateSubsite");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void deleteSubsite(String code) {
		super.executeQueryWithoutResultset(DELETE_SUBSITE, code);
	}

	private Subsite createSubsite(ResultSet res) throws SQLException, ApsSystemException, IOException {
		Subsite subsite = new Subsite();
		subsite.setCode(res.getString(1));
		String titlesString = res.getString(2);
		ApsProperties titles = new ApsProperties();
		if (titlesString != null) {
			titles.loadFromXml(titlesString);
		}
		subsite.setTitles(titles);
		String descriptionsString = res.getString(3);
		ApsProperties descriptions = new ApsProperties();
		if (descriptionsString != null) {
			descriptions.loadFromXml(descriptionsString);
		}
		subsite.setDescriptions(descriptions);
		subsite.setHomepage(res.getString(4));
		subsite.setGroupName(res.getString(5));
		subsite.setCategoryCode(res.getString(6));
		subsite.setContentViewerPage(res.getString(7));
		subsite.setCssFileName(res.getString(8));
		subsite.setImageFileName(res.getString(9));
		int visibility = res.getInt(10);
		subsite.setVisibility(visibility == 1);
		String viewerPagesString = res.getString(11);
		ApsProperties viewerPages = new ApsProperties();
		if (viewerPagesString != null) {
			viewerPages.loadFromXml(viewerPagesString);
		}
		subsite.setViewerPages(viewerPages);
		return subsite;
	}

	private static final String LOAD_SUBSITES
			= "SELECT code, titles, descriptions, homepage, groupname, catcode, contentviewerpage, "
			+ "cssfilename, imgfilename, visibility, viewerpages FROM jpsubsites_subsites ";

	private static final String LOAD_SUBSITECODES_FOR_CONTENT
			= "SELECT code FROM jpsubsites_subsites WHERE catcode IN "
			+ "( SELECT refcategory FROM contentrelations WHERE contentid = ? ) ";

	private static final String LOAD_SUBSITE
			= "SELECT code, titles, descriptions, homepage, groupname, catcode, contentviewerpage, cssfilename, imgfilename , viewerpages FROM jpsubsites_subsites WHERE code = ? ";

	private static final String LOAD_SUBSITECODE_FROM_PAGE
			= "SELECT code FROM jpsubsites_subsites WHERE homepage = ? ";

	private static final String ADD_SUBSITE
			= "INSERT INTO jpsubsites_subsites ( code, titles, descriptions, homepage, groupname, catcode, contentviewerpage, cssfilename, imgfilename , viewerpages) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? , ?) ";

	private static final String UPDATE_SUBSITE
			= "UPDATE jpsubsites_subsites SET titles = ?, descriptions = ?, homepage = ?, groupname = ? , catcode = ?, contentviewerpage = ?, cssfilename = ?, imgfilename = ?, visibility = ?, viewerpages = ? WHERE code = ? ";

	private static final String DELETE_SUBSITE
			= "DELETE FROM jpsubsites_subsites WHERE code = ? ";

}
