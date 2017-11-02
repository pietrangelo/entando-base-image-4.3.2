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
package org.entando.entando.plugins.jpforumremotenotifier.aps.system.services.forum;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpforumremotenotifier.aps.system.services.JpForumRemoteNotifierSystemConstants;
import org.entando.entando.plugins.jpforumremotenotifier.aps.system.services.forum.event.RemoteThreadChangingEvent;
import org.entando.entando.plugins.jpforumremotenotifier.aps.system.services.forum.event.RemoteThreadChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager;
import com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Post;
import com.agiletec.plugins.jpforum.aps.system.services.thread.Thread;
import com.agiletec.plugins.jpforum.aps.system.services.thread.event.PostOperationEvent;

@Aspect
public class ThreadChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteThreadChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(ThreadChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public void updateFromThreadChanging(RemoteThreadChangingEvent event) {
		try {
			System.out.println("Event received (forum): " + event.getEventID());
			ApsProperties properties = event.getParameters();
			String operationId = (String) properties.get(operationCode);
			if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.RELOAD_CONFIGURATION)) {
				((IManager) this.getConfigManager()).refresh();
			} else if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.UPDATE_OPERATION_CODE)) {
				int postId = new Integer((String) properties.get(codeId)).intValue();
				Post post = getThreadManager().getPost(postId);
				
				this.notifyPostChanging(post, PostOperationEvent.UPDATE_OPERATION_CODE, null);
			} else if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.INSERT_OPERATION_CODE)) {
				int postId = new Integer((String) properties.get(codeId)).intValue();
				Post post = getThreadManager().getPost(postId);
				
				this.notifyPostChanging(post, PostOperationEvent.UPDATE_OPERATION_CODE, null);
			} else if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.REMOVE_THREAD_OPERATION_CODE)) {
				int threadId = new Integer((String) properties.get(codeId)).intValue();
				Thread thread = getThreadManager().getThread(threadId);
				
				notifyPostChanging(thread.getPost(), PostOperationEvent.INSERT_OPERATION_CODE, thread.getPostCodes());
			} else if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.REMOVE_OPERATION_CODE)) {
				int postId = new Integer((String) properties.get(codeId)).intValue();
				Post post = getThreadManager().getPost(postId);
				
				this.notifyPostChanging(post, PostOperationEvent.REMOVE_OPERATION_CODE, null);
			} else if (operationId.equalsIgnoreCase(JpForumRemoteNotifierSystemConstants.REFRESH_SECTIONS_TREE)) {
//				((IManager) this.getSectionManager()).refresh();
			} 
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.config.IConfigManager.updateConfig(..))")
	public void notifyUpdateConfig() {
		ApsProperties properties = new ApsProperties();
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.RELOAD_CONFIGURATION));
		this.notifyRemoteEvent(properties);
	}
	
	/* thread // start */
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.updatePost(..)) && args(post, ..)")
	public void notifyUpdatePost(Post post) {
		ApsProperties properties = new ApsProperties();
		properties.put(codeId, new Integer (post.getCode()).toString());
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.UPDATE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.addThread(..)) && args(thread, ..)")
	public void notifyAddThread(Thread thread) {
		ApsProperties properties = new ApsProperties();
		properties.put(codeId, new Integer (thread.getCode()).toString());
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.INSERT_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.deleteThread(..)) && args(code, ..)")
	public void notifyDeleteThread(int code) {
		ApsProperties properties = new ApsProperties();
		properties.put(codeId, new Integer (code).toString());
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REMOVE_THREAD_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.deletePost(..)) && args(code, ..)")
	public void notifyDeletePost(int code) {
		ApsProperties properties = new ApsProperties();
		properties.put(codeId, new Integer (code).toString());
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REMOVE_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.thread.IThreadManager.addPost(..)) && args(post, ..)")
	public void notifyAddPost(Post post) {
		ApsProperties properties = new ApsProperties();
		properties.put(codeId, new Integer (post.getCode()).toString());
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.INSERT_OPERATION_CODE));
		this.notifyRemoteEvent(properties);
	}
	
	/* thread // end */
	
	/* sections // start */

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager.addSection(..))")
	public void notifyAddSection() {
		ApsProperties properties = new ApsProperties();
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REFRESH_SECTIONS_TREE));
		this.notifyRemoteEvent(properties);
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.section.ISectionManager.deleteSection(..))")
	public void notifyDeleteSection() {
		ApsProperties properties = new ApsProperties();
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REFRESH_SECTIONS_TREE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.section.SectionManager.updateSection(..))")
	public void notifyUpdateSection() {
		ApsProperties properties = new ApsProperties();
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REFRESH_SECTIONS_TREE));
		this.notifyRemoteEvent(properties);
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpforum.aps.system.services.section.SectionManager.moveSection(..))")
	public void notifyMoveSection() {
		ApsProperties properties = new ApsProperties();
		properties.put(operationCode, new String (JpForumRemoteNotifierSystemConstants.REFRESH_SECTIONS_TREE));
		this.notifyRemoteEvent(properties);
	}
	
	/* sections // end */
	
	protected void notifyRemoteEvent(ApsProperties properties) {
		System.out.println("Sending notification (forum)!");
		RemoteThreadChangingEvent remoteEvent = new RemoteThreadChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	/**
	 * Create a local event
	 * @param post
	 * @param operationCode
	 * @param postCodes
	 * @throws ApsSystemException
	 */
	private void notifyPostChanging(Post post, int operationCode, List<Integer> postCodes) throws ApsSystemException {
		PostOperationEvent event = new PostOperationEvent();
		event.setOperationCode(operationCode);
		event.setPost(post);
		event.setPostCodes(postCodes);
		this.notifyEvent(event);
	}
	
	public IConfigManager getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(IConfigManager configManager) {
		this._configManager = configManager;
	}
	
	public IThreadManager getThreadManager() {
		return _threadManager;
	}

	public void setThreadManager(IThreadManager threadManager) {
		this._threadManager = threadManager;
	}
	
	public ISectionManager getSectionManager() {
		return _sectionManager;
	}

	public void setSectionManager(ISectionManager sectionManager) {
		this._sectionManager = sectionManager;
	}

	private IConfigManager _configManager;
	private IThreadManager _threadManager;
	private ISectionManager _sectionManager;
	
	private final static String codeId = "codeId";
	private final static String operationCode = "operationCode";
}
