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

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IFormDAO {

	public List<Integer> searchForms(FieldSearchFilter[] filters);
	
	public Form loadForm(int courseId);

	public List<Integer> loadForms();

	public void removeForm(int courseId);
	
	public void updateForm(Form form);

	public void insertForm(Form form);
	

}