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
package org.entando.entando.plugins.jpemailmarketing.apsadmin.portal.specialwidget.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.Course;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.course.ICourseManager;
import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.IFormManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public class FormConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String courseId = this.getWidget().getConfig().getProperty(CONFIG_PARAM_COURSEID);
		if (StringUtils.isNotBlank(courseId)) {
			this.setCourseId(new Integer(courseId));
		}
		return result;
	}

	public List<SelectItem> getFormsId() {
		try {
			//XXX SHOULD BE SMARTER
			List<SelectItem> list = new ArrayList<SelectItem>();
			List<Integer> forms = this.getFormManager().searchForms(null);
			if (null != forms) {
				Iterator<Integer> it = forms.iterator();
				while (it.hasNext()) {
					int code = it.next();
					Course course = this.getCourseManager().getCourse(code);
					SelectItem item = new SelectItem(new Integer(code).toString(), course.getName());
					list.add(item);
				}
			}
			return list;
		} catch (Throwable t) {
			_logger.error("error in getFormsId", t);
			throw new RuntimeException("Error getting forms list", t);
		}
	}
	
	public int getCourseId() {
		return _courseId;
	}
	public void setCourseId(int courseId) {
		this._courseId = courseId;
	}

	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}


	public ICourseManager getCourseManager() {
		return _courseManager;
	}
	public void setCourseManager(ICourseManager courseManager) {
		this._courseManager = courseManager;
	}


	private int _courseId;
	private IFormManager _formManager;
	private ICourseManager _courseManager;
	
	public static final String CONFIG_PARAM_COURSEID = "courseId";
}

