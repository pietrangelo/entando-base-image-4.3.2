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
package org.entando.entando.plugins.jpsalesforce.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = SalesforceForm.TABLE_NAME)
public class SalesforceForm {
	
	public SalesforceForm() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "code", 
		dataType = DataType.STRING, 
		width=3,  canBeNull=false)
	private String _code;
	
	@DatabaseField(columnName = "pagecode", 
		dataType = DataType.STRING,
			width=30,  canBeNull=false)
	private String _pagecode;
	
	@DatabaseField(columnName = "frame", 
		dataType = DataType.INTEGER, 
		 canBeNull=false)
	private int _frame;
	
	@DatabaseField(columnName = "config", 
		dataType = DataType.LONG_STRING,
		 canBeNull=false)
	private String _config;
	

public static final String TABLE_NAME = "jpsalesforce_formconfig";
}
