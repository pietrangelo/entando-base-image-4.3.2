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
package org.entando.entando.plugins.jpemailmarketing.aps.system.init.servdb;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Course.TABLE_NAME)
public class Course {
	
	public Course() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "name", 
		dataType = DataType.STRING, 
		width=255,  canBeNull=false)
	private String _name;
	
	@DatabaseField(columnName = "sender", 
		dataType = DataType.STRING, 
		width=255,  canBeNull=false)
	private String _sender;
	
	@DatabaseField(columnName = "active", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _active;
	
	@DatabaseField(columnName = "cronexp", 
		dataType = DataType.STRING, 
		width=255,  canBeNull= true)
	private String _cronexp;
	
	@DatabaseField(columnName = "crontimezoneid", 
		dataType = DataType.STRING, 
		width=255,  canBeNull= true)
	private String _crontimezoneid;
	
	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
		 canBeNull= true)
	private Date _createdat;
	
	@DatabaseField(columnName = "updatedat", 
			dataType = DataType.DATE,
		 canBeNull= true)
	private Date _updatedat;
	

public static final String TABLE_NAME = "jpemailmarketing_course";
}
