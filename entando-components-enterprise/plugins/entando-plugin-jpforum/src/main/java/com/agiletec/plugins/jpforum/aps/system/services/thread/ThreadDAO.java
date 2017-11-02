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
package com.agiletec.plugins.jpforum.aps.system.services.thread;

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

public class ThreadDAO extends AbstractDAO  implements IThreadDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(ThreadDAO.class);

	@Override
	public void insertThread(Thread thread) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertThread(conn, thread);
			this.insertPost(conn, thread.getPost());
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in inserimento thread-post",  t);
			throw new RuntimeException("Errore in inserimento thread-post", t);
		} finally {
			closeDaoResources(null, null, conn);
		}
	}
	
	@Override
	public void insertPost(Post post) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.insertPost(conn, post);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in inserimento thread-post",  t);
			throw new RuntimeException("Errore in inserimento thread-post", t);
		} finally {
			closeDaoResources(null, null, conn);
		}
	}

	@Override
	public void deletePost(int code) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_POST);
			stat.setInt(1, code);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in eliminazione post",  t);
			throw new RuntimeException("Errore in eliminazione post", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void updatePost(Post post) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_POST);
			stat.setString(1, post.getTitle());
			stat.setString(2, post.getText());
			stat.setInt(3, post.getCode());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in aggiornameto thread-post",  t);
			throw new RuntimeException("Errore in aggiornameto thread-post", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeThread(int code) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deletePosts(conn, code);
			this.deleteThread(conn, code);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in inserimento thread-post",  t);
			throw new RuntimeException("Errore in inserimento thread-post", t);
		} finally {
			closeDaoResources(null, null, conn);
		}
	}
	
	@Override
	public void updateThread(Thread thread) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_THREAD);
			stat.setString(1, thread.isOpen() ? "true" : "false");
			stat.setString(2, thread.isPin() ? "true" : "false");
			stat.setInt(3, thread.getCode());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in aggiornameto thread",  t);
			throw new RuntimeException("Errore in aggiornameto thread", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public Map<Integer, List<Integer>> loadThreadsForSection(String sectionCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<Integer, List<Integer>> threads = new HashMap<Integer, List<Integer>>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_THREADS_FOR_SECTION);
			stat.setString(1, sectionCode);
			res = stat.executeQuery();
			while (res.next()) {
				Integer key = res.getInt("thread");
				if (!threads.containsKey(key)) {
					threads.put(key, new ArrayList<Integer>());
				}
				threads.get(key).add(res.getInt("post"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento mappa threads-post per sezione {}", sectionCode,  t);
			throw new RuntimeException("Errore in fase di caricamento mappa threads-post per sezione ", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
		return threads;
	}
	
	@Override
	public List<Integer> loadThreadsForSection(String sectionCode, Boolean pin) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> threads = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			StringBuffer query = new StringBuffer(LOAD_THREADS_FOR_SECTION_BY_PIN);
			if (null != pin) {
				query.append("AND t.pin = ? ");
			}
			query.append("GROUP BY p.threadid ORDER BY postdate DESC");
			
			stat = conn.prepareStatement(query.toString());
			stat.setString(1, sectionCode);
			if (null != pin) {
				stat.setString(2, pin ? "true" : "false");
			}
			res = stat.executeQuery();
			while (res.next()) {
				threads.add(res.getInt("code"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento threads pinnati per sezione {}", sectionCode,  t);
			throw new RuntimeException("Errore in fase di caricamento threads pinnati per sezione", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return threads;
	}

	@Override
	public Thread loadThread(int code) {
		Thread thread = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			thread = this.loadThread(conn, code);
			if (null != thread) {
				thread.setPost(this.loadThreadPost(conn, code));
				thread.setPostCodes(this.loadThreadPostCodes(conn, code));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in caricamento thread",  t);
			throw new RuntimeException("Errore in caricamento thread", t);
		} finally {
			closeDaoResources(null, null, conn);
		}
		return thread;
	}

	@Override
	public Post loadPost(int code) {
		Post post = null;
		Connection conn = null;
		ResultSet res = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_POST);
			stat.setInt(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				post = new Post();
				post.setCode(code);
				post.setUsername(res.getString("username"));
				post.setTitle(res.getString("title"));
				post.setText(res.getString("text"));
				post.setPostDate(new Date(res.getTimestamp("postdate").getTime()));
				post.setThreadId(res.getInt("threadid"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in caricamento post",  t);
			throw new RuntimeException("Errore in caricamento post", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return post;
	}

	private void insertPost(Connection conn, Post post) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(INSERT_POST);
			stat.setInt(1, post.getCode());
			stat.setString(2, post.getUsername());
			stat.setString(3, post.getTitle());
			stat.setString(4, post.getText());
			stat.setTimestamp(5, new Timestamp(post.getPostDate().getTime()));
			stat.setInt(6, post.getThreadId());
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in inserimento post",  t);
			throw new RuntimeException("Errore in inserimento post", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}

	private void insertThread(Connection conn, Thread thread) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(INSERT_THREAD);
			stat.setInt(1, thread.getCode());
			stat.setString(2, thread.getSectionId());
			stat.setString(3, thread.getUsername());
			stat.setString(4, thread.isOpen() ? "true" : "false");
			stat.setString(5, thread.isPin() ? "true" : "false");
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in inserimento thread",  t);
			throw new RuntimeException("Errore in inserimento thread", t);
		} finally {
			this.closeDaoResources(null, stat);
		}
	}

	private void deletePosts(Connection conn, int code) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_THREAD_POSTS);
			stat.setInt(1, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione posts in rimozione thread {}",code, t);
			throw new RuntimeException("Errore in eliminazione posts in rimozione thread ", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	private void deleteThread(Connection conn, int code) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_THREAD);
			stat.setInt(1, code);
			stat.executeUpdate();
		} catch (Throwable t) {
			_logger.error("Errore in eliminazione thread", t);
			throw new RuntimeException("Errore in eliminazione thread", t);
		} finally {
			closeDaoResources(null, stat, null);
		}
	}

	private Thread loadThread(Connection conn, int code) {
		Thread thread = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_THREAD);
			stat.setInt(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				thread = new Thread();
				thread.setCode(code);
				thread.setSectionId(res.getString("sectionid"));
				thread.setUsername(res.getString("username"));
				thread.setOpen(res.getString("openthread").equalsIgnoreCase("true"));
				thread.setPin(res.getString("pin").equalsIgnoreCase("true"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento thread {}", code,  t);
			throw new RuntimeException("Errore in caricamento thread", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return thread;
	}

	private Post loadThreadPost(Connection conn, int code) {
		Post post = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_POST);
			stat.setInt(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				//code, username, title, text, postdate, threadid FROM jpforum_posts WHERE code
				post = new Post();
				post.setCode(code);
				post.setUsername(res.getString("username"));
				post.setTitle(res.getString("title"));
				post.setText(res.getString("text"));
				post.setPostDate(new Date(res.getTimestamp("postdate").getTime()));
				post.setThreadId(res.getInt("threadid"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento post {}", code,  t);
			throw new RuntimeException("Errore in caricamento post ", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return post;
	}

	private List<Integer> loadThreadPostCodes(Connection conn, int code) {
		List<Integer> postCodes = new ArrayList<Integer>();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(LOAD_POSTS_FOR_THREAD);
			stat.setInt(1, code);
			res = stat.executeQuery();
			while (res.next()) {
				postCodes.add(res.getInt("code"));
			}
		} catch (Throwable t) {
			_logger.error("Errore in caricamento identificativi post per thread {}", code, t);
			throw new RuntimeException("Errore in caricamento identificativi post per thread", t);
		} finally {
			closeDaoResources(res, stat, null);
		}
		return postCodes;
	}
	
	@Override
	public List<Integer> getUserPosts(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> posts = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_POSTS_FOR_USER);
			stat.setString(1, username);
			res = stat.executeQuery();
			while (res.next()) {
				posts.add(res.getInt("code"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento posts per utente {}", username, t);
			throw new RuntimeException("Errore in fase di caricamento posts per utente", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return posts;
	}

	@Override
	public List<Integer> getUserThreads(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> threads = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_THREADS_FOR_USER);
			stat.setString(1, username);
			res = stat.executeQuery();
			while (res.next()) {
				threads.add(res.getInt("code"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento threads per utente {}", username, t);
			throw new RuntimeException("Errore in fase di caricamento threads per utente", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return threads;
	}

	@Override
	public List<Integer> getPosts() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> posts = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_POSTS);
			res = stat.executeQuery();
			while (res.next()) {
				posts.add(res.getInt("code"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento posts",  t);
			throw new RuntimeException("Errore in fase di caricamento posts", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return posts;
	}

	@Override
	public List<Integer> getPosts(String sectionId) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<Integer> posts = new ArrayList<Integer>();
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_THREADS_FOR_SECTION);
			//FIXME
			stat.setString(1, sectionId);
			res = stat.executeQuery();
			while (res.next()) {
				posts.add(res.getInt("post"));
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Errore in fase di caricamento posts per sezione {}", sectionId,  t);
			throw new RuntimeException("Errore in fase di caricamento posts per sezione", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return posts;
	}

	@Override
	public List<String> loadAllUsers() {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		List<String> usernames = new ArrayList<String>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_ALL_USERNAMES);
			res = stat.executeQuery();
			while (res.next()) {
				usernames.add(res.getString("username"));
			}
		} catch (Throwable t) {
			_logger.error("Error loading the list of all users from posts",  t);
			throw new RuntimeException("Error loading the list of all users from posts", t);
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return usernames;
	}

	private static final String INSERT_THREAD =
		"INSERT INTO jpforum_threads(code, sectionid, username, openthread, pin) VALUES (?, ?, ?, ?, ?)";

	private static final String INSERT_POST =
		"INSERT INTO jpforum_posts(code, username, title, text, postdate, threadid) VALUES (?, ?, ?, ?, ?, ?)";

	private static final String DELETE_THREAD_POSTS =
		"DELETE FROM jpforum_posts WHERE threadId = ?";

	private static final String DELETE_THREAD =
		"DELETE FROM jpforum_threads WHERE code = ?";

	private static final String LOAD_THREADS_FOR_SECTION =
		"SELECT t.code AS thread, p.code AS post " +
		"FROM jpforum_posts p " +
		"INNER JOIN jpforum_threads t ON t.code = p.threadid " +
		"INNER JOIN jpforum_sections s ON s.code = t.sectionid " +
		"WHERE s.code=? ORDER BY p.postdate desc";

	private static final String LOAD_THREADS_FOR_SECTION_BY_PIN =
		"SELECT p.threadid as code, max(p.postdate) AS postdate " +
		"FROM jpforum_threads t INNER JOIN jpforum_posts p ON t.code = p.threadid " +
		"WHERE t.sectionid = ? ";//AND t.pin = ? " +
//		"GROUP BY p.threadid " +
//		"ORDER BY postdate DESC";

	private static final String LOAD_THREAD =
		"SELECT code, sectionid, username, openthread, pin  FROM jpforum_threads WHERE code = ?";
	
	private static final String LOAD_POST =
		"SELECT code, username, title, text, postdate, threadid FROM jpforum_posts WHERE code = ?";
	
	private static final String LOAD_POSTS_FOR_THREAD =
		"SELECT code FROM jpforum_posts WHERE threadId = ? ORDER BY postdate ASC";
	
	private static final String UPDATE_POST =
		"UPDATE jpforum_posts  SET title=?, text=? WHERE code=?";
	
	private static final String UPDATE_THREAD =
		"UPDATE jpforum_threads SET openthread = ?, pin = ? WHERE code = ?";
	
	private static final String DELETE_POST =
		"DELETE FROM jpforum_posts WHERE code = ?";

	private static final String LOAD_POSTS_FOR_USER =
		"SELECT code FROM jpforum_posts WHERE username = ? ORDER BY postdate ASC";

	private static final String LOAD_THREADS_FOR_USER =
		"SELECT code FROM jpforum_posts WHERE username = ? AND threadid = code ORDER BY postdate ASC";
	
	private static final String LOAD_POSTS =
		"SELECT code FROM jpforum_posts ORDER BY postdate ASC";
	
	private static final String LOAD_ALL_USERNAMES =
			"SELECT DISTINCT username FROM jpforum_posts";
	
}
