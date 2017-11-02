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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;


public class CourseTriggerEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((CourseTriggerObserver) srv).updateFromCourseTrigger(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return CourseTriggerObserver.class;
	}
	
	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	private int _courseId;

}
