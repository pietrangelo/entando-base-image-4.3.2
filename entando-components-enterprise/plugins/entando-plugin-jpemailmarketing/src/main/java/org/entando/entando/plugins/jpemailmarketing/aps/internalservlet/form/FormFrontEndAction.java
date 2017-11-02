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
package org.entando.entando.plugins.jpemailmarketing.aps.internalservlet.form;

import java.io.InputStream;
import java.net.URLConnection;

import org.entando.entando.plugins.jpemailmarketing.aps.system.services.form.Form;
import org.entando.entando.plugins.jpemailmarketing.apsadmin.form.FormAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormFrontEndAction extends FormAction {

	private static final Logger _logger =  LoggerFactory.getLogger(FormFrontEndAction.class);
	
	/**
	 * In front end, results other than SUCCESS IS NULL
	 */
	public String viewFile(String type) {
		try {
			Form form = this.getFormManager().getForm(this.getCourseId());
			if (null == form) {
				_logger.warn("form {} is null", this.getCourseId());
				return null;
			}
			if (type.equalsIgnoreCase("cover")) {
				InputStream is = this.getFormManager().getCover(form.getCourseId());
				this.setDownloadInputStream(is);
				this.setDownloadFileName(form.getFileCoverName());
				this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
			} else if (type.equalsIgnoreCase("incentive")) {
				InputStream is = this.getFormManager().getIncentive(form.getCourseId());
				this.setDownloadInputStream(is);
				this.setDownloadFileName(form.getFileIncentiveName());
				this.setDownloadContentType(URLConnection.guessContentTypeFromStream(is));
			}
		} catch (Throwable t) {
			_logger.error("Error in view file of type {}", type, t);
			return null;
		}
		return SUCCESS;
	}




}
