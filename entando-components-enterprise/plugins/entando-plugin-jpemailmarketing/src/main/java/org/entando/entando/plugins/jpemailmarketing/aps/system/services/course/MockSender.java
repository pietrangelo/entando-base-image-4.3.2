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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main task
 *
 */
public class MockSender implements Runnable {

	private static final Logger _logger =  LoggerFactory.getLogger(MockSender.class);
	
	public MockSender(Course course, ICourseManager courseManager) {
		super();
		this._course = course;
		this._courseManager = courseManager;
	}

	@Override
	public void run() {
		this._courseManager.notifyCourseTriggerEvent(_course);
	}

	private Course _course;
	private ICourseManager _courseManager;
}
