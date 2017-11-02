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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.mock;

import java.util.Date;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.ISubscriberManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.subscriber.Subscriber;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IMockSubscriberManager extends ISubscriberManager {

	/**
	 * <b>Mock method</b>
	 * Like confirmSubscriber except for the date. in the real world the date is NOW
	 */
	public void confirmSubscriber(Subscriber subscriber, Date date) throws ApsSystemException;
	
	
	/**
	 * 
	 * @param subscriber
	 * @param now
	 * @throws ApsSystemException
	 */
	public void addSubscriberNoMail(Subscriber subscriber, Date now) throws ApsSystemException;

}
