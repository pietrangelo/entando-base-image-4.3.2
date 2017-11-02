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
import java.util.Date;

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = UserStatistic.TABLE_NAME)
public class UserStatistic implements ExtendedColumnDefinition {
	
	public UserStatistic() {}
	
	@DatabaseField(columnName = "users_count", 
			dataType = DataType.INTEGER, 
			canBeNull = false, defaultValue = "0")
	private int _usersCount;
	
	@DatabaseField(columnName = "record_date", 
			dataType = DataType.DATE, 
			canBeNull = true)
	private Date _recordDate;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return null;
	}
	
	public static final String TABLE_NAME = "jpforum_statistics_usersonline";
	
}

/*
CREATE TABLE jpforum_statistics_usersonline
(
  users_count integer NOT NULL DEFAULT 0,
  record_date timestamp without time zone
)
WITH (OIDS=TRUE);
 */