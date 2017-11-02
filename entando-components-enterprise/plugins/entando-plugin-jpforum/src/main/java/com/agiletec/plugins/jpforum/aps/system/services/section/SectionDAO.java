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
package com.agiletec.plugins.jpforum.aps.system.services.section;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;

public class SectionDAO extends AbstractDAO implements ISectionDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SectionDAO.class);

	@Override
	public void addSection(Section section) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_SECTION);
			stat.setString(1, section.getCode());
			stat.setString(2, section.getParentCode());
			stat.setInt(3, section.getPosition());
			String openString = "true";
			if (!section.isOpen()) openString = "false";
			stat.setString(4, openString);
			stat.setString(5, section.getTitles().toXml());
			stat.setString(6, section.getDescriptions().toXml());
			stat.setInt(7, section.getUnauthBahaviour());
			stat.executeUpdate();

			this.addSectionGroups(conn, section);
			//this.addSectionModerators(conn, section);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di inserimento sezione",  t);
			throw new RuntimeException("Errore in fase di inserimento sezione", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void deleteSection(Section section) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);

			//this.deleteSectionModerators(conn, section.getCode());
			this.deleteSectionGroups(conn, section.getCode());

			stat = conn.prepareStatement(DELETE_SECTION);
			stat.setString(1, section.getCode());
			stat.executeUpdate();
			this.shiftSections(section.getParentCode(), section.getPosition(), conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di rimozione sezione",  t);
			throw new RuntimeException("Errore in fase di rimozione sezione", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updateSection(Section section) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);

			//this.deleteSectionModerators(conn, section.getCode());
			this.deleteSectionGroups(conn, section.getCode());

			stat = conn.prepareStatement(UPDATE_SECTION);
			String openString = "true";
			if (!section.isOpen()) openString = "false";
			stat.setString(1, openString);
			stat.setString(2, section.getTitles().toXml());
			stat.setString(3, section.getDescriptions().toXml());
			stat.setInt(4, section.getUnauthBahaviour());
			stat.setString(5, section.getCode());

			this.addSectionGroups(conn, section);
			//this.addSectionModerators(conn, section);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di aggiornamento sezione",  t);
			throw new RuntimeException("Errore in fase di aggiornamento sezione", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public List<Section> loadSections() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		List<Section> sections = new ArrayList<Section>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ALL_SECTIONS);
			while(res.next()) {
				Section section = this.loadSection(res, conn);
				sections.add(section);
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento sezioni",  t);
			throw new RuntimeException("Errore in caricamento sezioni", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sections;
	}

	@Override
	public void updatePosition(Section sectionDown, Section sectionUp) {
		Connection conn = null;
		PreparedStatement stat = null;
		PreparedStatement stat2 = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);

			stat = conn.prepareStatement(MOVE_DOWN);
			stat.setString(1, sectionDown.getCode());
			stat.executeUpdate();

			stat2 = conn.prepareStatement(MOVE_UP);
			stat2.setString(1, sectionUp.getCode());
			stat2.executeUpdate();

			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di aggiornamento delle posizioni", t);
			throw new RuntimeException("Errore in fase di aggiornamento delle posizioni", t);
		} finally {
			closeDaoResources(null, stat);
			closeDaoResources(null, stat2, conn);
		}
	}

	@Override
	public List<String> getSectionsForGroup(String groupName) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<String> sections = new ArrayList<String>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SECTIONS_FOR_GROUP);
			stat.setString(1, groupName);
			res = stat.executeQuery();
			while(res.next()) {
				sections.add(res.getString("section"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento sezioni per gruppo",  t);
			throw new RuntimeException("Errore in caricamento sezioni per gruppo", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return sections;
	}
	
	/**
	 * Costruisce e restituisce una sezione leggendo una riga di recordset.
	 * @param res Il resultset da leggere
	 * @return La sezione generata.
	 * @throws ApsSystemException 
	 */
	protected Section loadSection(ResultSet res, Connection conn) throws ApsSystemException {
		Section section = new Section();
		try {
			section.setCode(res.getString("code"));
			section.setParentCode(res.getString("parentcode"));
			section.setPosition(res.getInt("pos"));
			String openValue = res.getString("opensection");
			section.setOpen(null != openValue && openValue.equalsIgnoreCase("true"));
			ApsProperties titles = new ApsProperties();
			titles.loadFromXml(res.getString("titles"));
			section.setTitles(titles);
			ApsProperties descriptions = new ApsProperties();
			descriptions.loadFromXml(res.getString("descriptions"));
			section.setDescriptions(descriptions);
			section.setUnauthBahaviour(res.getInt("unauthbahaviour"));

			//Set<String> moderators = this.getSectionModerators(section.getCode(), conn);
			Set<String> groups = this.getSectionGroups(section.getCode(), conn);
			//section.setModerators(moderators);
			section.setGroups(groups);

		} catch (Throwable t) {
			throw new ApsSystemException("Errore in caricamento sezioni", t);
		}
		return section;
	}

	private void deleteSectionGroups(Connection conn, String code) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_SECTION_GROUPS);
			stat.setString(1, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in fase di eliminazione sezione-gruppi", t);
			throw new RuntimeException("Errore in fase di eliminazione sezione-gruppi", t);
		}		
	}

	private void addSectionGroups(Connection conn, Section section) {
		if (null == section.getGroups() || section.getGroups().size() == 0) return;
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SECTION_GROUP);
			Iterator<String> it = section.getGroups().iterator();
			while (it.hasNext()) {
				String groupName = it.next();
				stat.setString(1, section.getCode());
				stat.setString(2, groupName);
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			_logger.error("Errore in fase di inserimento sezione-gruppi", t);
			throw new RuntimeException("Errore in fase di inserimento sezione-gruppi", t);
		}
	}

	private Set<String> getSectionGroups(String code, Connection conn) {
		PreparedStatement stat = null;
		ResultSet res = null;
		Set<String> groups = new HashSet<String>();
		try {
			stat = conn.prepareStatement(GET_SECTION_GROUPS);
			stat.setString(1, code);
			res = stat.executeQuery();
			while (res.next()) {
				String username = res.getString("groupname");
				groups.add(username);
			}

		} catch (Throwable t) {
			_logger.error("errore in recupero gruppi per sezione",  t);
			throw new RuntimeException("errore in recupero gruppi per sezione", t);
		}
		return groups;
	}

	protected void shiftSections(String parentCode, int position, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(SHIFT_SECTION);
			stat.setString(1, parentCode);
			stat.setInt(2, position);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in fase di compattamento delle posizioni",  t);
			throw new RuntimeException("Errore in fase di compattamento delle posizioni", t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	private static final String LOAD_ALL_SECTIONS = 
		"SELECT code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour " +
		"FROM jpforum_sections " +
		"ORDER BY parentcode, pos";

	private static final String INSERT_SECTION = 
		"INSERT INTO jpforum_sections (code, parentcode, pos, opensection, titles, descriptions, unauthbahaviour) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String DELETE_SECTION = 
		"DELETE FROM jpforum_sections WHERE code = ?";

	private static final String UPDATE_SECTION = 
		"UPDATE jpforum_sections SET opensection = ?, titles = ?, descriptions = ?, unauthbahaviour = ? WHERE code = ?";

	private static final String DELETE_SECTION_GROUPS =
		"DELETE FROM jpforum_sectiongroups WHERE section=?";

	protected static final String ADD_SECTION_GROUP = 
		"INSERT INTO jpforum_sectiongroups (section, groupname) values (?, ?)"; 

	protected static final String GET_SECTION_GROUPS = 
		"SELECT groupname FROM jpforum_sectiongroups WHERE section=?"; 

//	private static final String DELETE_SECTION_MODERATORS = 
//		"DELETE FROM jpforum_sectionmoderators WHERE section=?";
//
//	protected static final String ADD_SECTION_MODERATOR = 
//		"INSERT INTO jpforum_sectionmoderators (username, section) values (?, ?)"; 
//
//	protected static final String GET_SECTION_MODERATORS = 
//		"SELECT username FROM jpforum_sectionmoderators WHERE section=?";

	private static final String MOVE_UP = 
		"UPDATE jpforum_sections SET pos = (pos - 1) WHERE code = ? ";

	private static final String MOVE_DOWN = 
		"UPDATE jpforum_sections SET pos = (pos + 1) WHERE code = ? ";

	private static final String SHIFT_SECTION = 
		"UPDATE jpforum_sections SET pos = (pos - 1) WHERE parentcode = ? AND pos > ? ";

	private static final String LOAD_SECTIONS_FOR_GROUP = 
		"SELECT DISTINCT section FROM jpforum_sectiongroups WHERE groupname = ?";

}
