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
package org.entando.entando.plugins.jpremotenotifier.aps.system.services.page;

import org.entando.entando.plugins.jpremotenotifier.aps.system.services.page.event.RemotePageChangingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.events.PageChangedEvent;
import com.agiletec.aps.util.ApsProperties;

public class SeoPageChangingNotifierManager extends PageChangingNotifierManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoPageChangingNotifierManager.class);
	
	@Override
	public void updateFromPageChanging(RemotePageChangingEvent event) {
		try {
			System.out.println("Event received: " + event.getEventID());
			((IManager) this.getPageManager()).refresh();
			ApsProperties properties = event.getParameters();
			String pageCode = (String) properties.get("pageCode");
			IPage page = this.getPageManager().getPage(pageCode);
			PageChangedEvent localEvent = new PageChangedEvent();
			localEvent.setPage(page);
			// JPSEO will ignore the update event
			localEvent.setSource(REMOTE_EVENT);
			this.notifyEvent(localEvent);
		} catch (Throwable t) {
			_logger.error("Errore in aggiornamento da evento remoto", t);
		}
	}
	
	public final static String REMOTE_EVENT = "remote";
}
