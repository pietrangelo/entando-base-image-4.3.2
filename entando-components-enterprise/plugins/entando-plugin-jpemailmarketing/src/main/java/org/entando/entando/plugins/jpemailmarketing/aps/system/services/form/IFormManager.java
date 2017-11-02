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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.form;

import java.io.InputStream;
import java.util.List;

import org.entando.entando.plugins.jpemailmarketing.apsadmin.form.model.FormUpload;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

public interface IFormManager {

	public Form getForm(int courseId) throws ApsSystemException;

	public List<Integer> getForms() throws ApsSystemException;

	public List<Integer> searchForms(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addForm(Form form) throws ApsSystemException;

	public void addForm(Form form, FormUpload fileCover, FormUpload fileIncentive) throws ApsSystemException;

	/**
	 * Update a form
	 * @param form
	 * @throws ApsSystemException
	 */
	public void updateForm(Form form) throws ApsSystemException;

	/**
	 * Update a form.
	 * @param form the Form to update
	 * @param fileCover If present, delete the previous one and save this new file. When null, it does nothing
	 * @param fileIncentive If present, delete the previous one and save this new file. When null, it does nothing
	 * @throws ApsSystemException
	 */
	public void updateForm(Form form, FormUpload fileCover, FormUpload fileIncentive) throws ApsSystemException;

	public void deleteForm(int courseId) throws ApsSystemException;
	
	/**
	 * No filename needed since there is only 1 file
	 * @param courseId
	 * @return
	 * @throws ApsSystemException
	 */
	public InputStream getCover(int courseId) throws ApsSystemException;

	/**
	 * No filename needed since there is only 1 file
	 * @param courseId
	 * @return
	 * @throws ApsSystemException
	 */
	public InputStream getIncentive(int courseId) throws ApsSystemException;

}