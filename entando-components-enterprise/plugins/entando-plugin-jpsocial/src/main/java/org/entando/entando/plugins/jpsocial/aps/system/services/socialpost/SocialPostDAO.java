package org.entando.entando.plugins.jpsocial.aps.system.services.socialpost;

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

public class SocialPostDAO extends AbstractSearcherDAO implements ISocialPostDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SocialPostDAO.class);

	@Override
	protected String getTableFieldName(String metadataFieldKey) {
		return metadataFieldKey;
	}
	
	@Override
	protected String getMasterTableName() {
		return "jpsocial_socialpost";
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
	public List<Integer> searchSocialPosts(FieldSearchFilter[] filters) {
		List socialPostsId = null;
		try {
			socialPostsId  = super.searchId(filters);
		} catch (Throwable t) {
			throw new RuntimeException("error in searchSocialPosts", t);
		}
		return socialPostsId;
	}

	@Override
	public List<Integer> loadSocialPosts() {
		List<Integer> socialPostsId = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_SOCIALPOSTS_ID);
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt("id");
				socialPostsId.add(id);
			}
		} catch (Throwable t) {
			_logger.error("error in loadSocialPosts",  t);
			throw new RuntimeException("error in loadSocialPosts", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return socialPostsId;
	}
	
	@Override
	public void insertSocialPost(SocialPost socialPost) {
		PreparedStatement stat = null;
		Connection conn  = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertSocialPost(socialPost, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error creating socialPost",  t);
			throw new RuntimeException("Error creating socialPost", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void insertSocialPost(SocialPost socialPost, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_SOCIALPOST);
			int index = 1;
			stat.setInt(index++, socialPost.getId());
			stat.setString(index++, socialPost.getText());
			Timestamp dateTimestamp = new Timestamp(System.currentTimeMillis());
			stat.setTimestamp(index++, dateTimestamp);	
  			if(StringUtils.isNotBlank(socialPost.getPermalink())) {
				stat.setString(index++, socialPost.getPermalink());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(socialPost.getService())) {
				stat.setString(index++, socialPost.getService());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(socialPost.getObjectid())) {
				stat.setString(index++, socialPost.getObjectid());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, socialPost.getProvider());
 			stat.setString(index++, socialPost.getSocialid());
 			stat.setString(index++, socialPost.getUsername());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error creating socialPost",  t);
			throw new RuntimeException("Error creating socialPost", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void updateSocialPost(SocialPost socialPost) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateSocialPost(socialPost, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error updating socialPost ",  t);
			throw new RuntimeException("Error updating socialPost ", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}

	public void updateSocialPost(SocialPost socialPost, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_SOCIALPOST);
			int index = 1;
			stat.setString(index++, socialPost.getText());
			Timestamp dateTimestamp = new Timestamp(System.currentTimeMillis());
			stat.setTimestamp(index++, dateTimestamp);	
  			if(StringUtils.isNotBlank(socialPost.getPermalink())) {
				stat.setString(index++, socialPost.getPermalink());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(socialPost.getService())) {
				stat.setString(index++, socialPost.getService());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			if(StringUtils.isNotBlank(socialPost.getObjectid())) {
				stat.setString(index++, socialPost.getObjectid());				
			} else {
				stat.setNull(index++, Types.VARCHAR);
			}
 			stat.setString(index++, socialPost.getProvider());
 			stat.setString(index++, socialPost.getSocialid());
 			stat.setString(index++, socialPost.getUsername());
			stat.setInt(index++, socialPost.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error updating socialPost",  t);
			throw new RuntimeException("Error updating socialPost", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	@Override
	public void removeSocialPost(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeSocialPost(id, conn);
 			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting socialPost {}", id,  t);
			throw new RuntimeException("Error deleting socialPost", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void removeSocialPost(int id, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_SOCIALPOST);
			int index = 1;
			stat.setInt(index++, id);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Error deleting socialPost {}", id,  t);
			throw new RuntimeException("Error deleting socialPost", t);
		} finally {
			this.closeDaoResources(null, stat, null);
		}
	}

	public SocialPost loadSocialPost(int id) {
		SocialPost socialPost = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			socialPost = this.loadSocialPost(id, conn);
		} catch (Throwable t) {
			_logger.error("Error loading socialPost with id {}", id,  t);
			throw new RuntimeException("Error loading socialPost with id", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return socialPost;
	}

	public SocialPost loadSocialPost(int id, Connection conn) {
		SocialPost socialPost = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_SOCIALPOST);
			int index = 1;
			stat.setInt(index++, id);
			res = stat.executeQuery();
			if (res.next()) {
				socialPost = this.buildSocialPostFromRes(res);
			}
		} catch (Throwable t) {
			_logger.error("Error loading socialPost with id {}",id,  t);
			throw new RuntimeException("Error loading socialPost with id", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return socialPost;
	}

	protected SocialPost buildSocialPostFromRes(ResultSet res) {
		SocialPost socialPost = null;
		try {
			socialPost = new SocialPost();				
			socialPost.setId(res.getInt("id"));
			socialPost.setText(res.getString("text"));
			Timestamp dateValue = res.getTimestamp("date");
			if (null != dateValue) {
				socialPost.setDate(new Date(dateValue.getTime()));
			}
			socialPost.setPermalink(res.getString("permalink"));
			socialPost.setService(res.getString("service"));
			socialPost.setObjectid(res.getString("objectid"));
			socialPost.setProvider(res.getString("provider"));
			socialPost.setSocialid(res.getString("socialid"));
			socialPost.setUsername(res.getString("username"));
		} catch (Throwable t) {
			throw new RuntimeException("Error in buildSocialPostFromRes", t);
		}
		return socialPost;
	}

	private static final String ADD_SOCIALPOST = "INSERT INTO jpsocial_socialpost (id, text, date, permalink, service, objectid, provider, socialid, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_SOCIALPOST = "UPDATE jpsocial_socialpost SET  text=?,  date=?,  permalink=?,  service=?,  objectid=?,  provider=?, socialid=?, username=? WHERE id = ?";

	private static final String DELETE_SOCIALPOST = "DELETE FROM jpsocial_socialpost WHERE id = ?";
	
	private static final String LOAD_SOCIALPOST = "SELECT id, text, date, permalink, service, objectid, provider, socialid, username  FROM jpsocial_socialpost WHERE id = ?";
	
	private static final String LOAD_SOCIALPOSTS_ID  = "SELECT id FROM jpsocial_socialpost";
	
}
