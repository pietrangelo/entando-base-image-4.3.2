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
package org.entando.entando.plugins.jpforum.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Statistic.TABLE_NAME)
public class Statistic {
	
	public Statistic() {}
	
	@DatabaseField(columnName = "thread", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _thread;
	
	@DatabaseField(columnName = "views", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _views;
	
	public static final String TABLE_NAME = "jpforum_statistics";
	
}
/*
CREATE TABLE jpforum_statistics
(
  thread integer NOT NULL,
  views integer NOT NULL
)
 */