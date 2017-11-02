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

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Thread.TABLE_NAME)
public class Thread implements ExtendedColumnDefinition {
	
	public Thread() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _code;
	
	@DatabaseField(foreign = true, columnName = "sectionid", 
			width = 30, 
			canBeNull = false)
	private Section _section;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "openthread", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = true)
	private String _open;
	
	@DatabaseField(columnName = "pin", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = true)
	private String _pin;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String sectionTableName = Section.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			sectionTableName = "`" + sectionTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_sect_fkey FOREIGN KEY (sectionid) "
				+ "REFERENCES " + sectionTableName + " (code)"};
	}
	
	public static final String TABLE_NAME = "jpforum_threads";
	
}
/*
CREATE TABLE jpforum_threads
(
  code integer NOT NULL,
  sectionid character varying(30) NOT NULL,
  username character varying(20) NOT NULL,
  openthread character varying(5),
  pin character varying(5),
  CONSTRAINT jpforum_threads_pkey PRIMARY KEY (code),
  CONSTRAINT fk_jpforum_threads FOREIGN KEY (sectionid)
      REFERENCES jpforum_sections (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS=TRUE);
 */
