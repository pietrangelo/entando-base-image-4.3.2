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
package org.entando.entando.plugins.jpblogremotenotifier.aps.system.services.blog;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.plugins.jpblogremotenotifier.aps.system.services.blog.event.RemoteBlogChangingEvent;
import org.entando.entando.plugins.jpblogremotenotifier.aps.system.services.blog.event.RemoteBlogChangingObserver;
import org.entando.entando.plugins.jpremotenotifier.aps.system.services.remotenotify.AbstractNotifierInterceptorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager;

@Aspect
public class BlogChangingNotifierManager extends AbstractNotifierInterceptorManager implements RemoteBlogChangingObserver {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogChangingNotifierManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());	
	}

	@Override
	public void updateFromBlogChanging(RemoteBlogChangingEvent event) {
		try {
			System.out.println("Received event (Blog) :" + event.getEventID());
			((IManager) this.getBlogManager()).refresh();
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager.updateConfig(..))")
	public void notifyUpdateConfig() {
		notifyBlogChanging(null);
	}
	
	protected void notifyBlogChanging(ApsProperties properties) {
		System.out.println("Sending notification (blog)!");
		RemoteBlogChangingEvent remoteEvent = new RemoteBlogChangingEvent();
		this.getRemoteNotifyManager().sendEvent(remoteEvent);
	}
	
	public IBlogManager getBlogManager() {
		return _blogManager;
	}

	public void setBlogManager(IBlogManager blogManager) {
		this._blogManager = blogManager;
	}

	private IBlogManager _blogManager; // 
}
