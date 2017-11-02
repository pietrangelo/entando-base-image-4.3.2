/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



/**
 * @author S.Loru
 */

@DatabaseTable(tableName = SharedContent.TABLE_NAME)
public class SharedContent {
	
	public SharedContent() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "contentid", 
		dataType = DataType.STRING, 
		width=16,  canBeNull=false)
	private String _contentId;
	
	@DatabaseField(columnName = "multisitecodesrc", 
		dataType = DataType.STRING, 
		width=40,  canBeNull=false)
	private String _multisiteCodeSrc;
	
	@DatabaseField(columnName = "multisitecodeto", 
		dataType = DataType.STRING, 
		width=40,  canBeNull=false)
	private String _multisiteCodeTo;
	

	public static final String TABLE_NAME = "jpmultisite_sharedcontent";
}

