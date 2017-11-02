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
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.servdb.Group;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = SectionGroup.TABLE_NAME)
public class SectionGroup implements ExtendedColumnDefinition {
	
	public SectionGroup() {}
	
	@DatabaseField(columnName = "section", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _code;
	
	@DatabaseField(foreign = true, columnName = "groupname", 
			width = 20, 
			canBeNull = false)
	private Group _group;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String groupTableName = Group.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			groupTableName = "`" + groupTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_pkey PRIMARY KEY (section, groupname)", 
			"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_fkey FOREIGN KEY (groupname) "
				+ "REFERENCES " + groupTableName + " (groupname)"};
	}
	
	public static final String TABLE_NAME = "jpforum_sectiongroups";
	
}
/*
CREATE TABLE jpforum_sectiongroups
(
  section character varying(30) NOT NULL,
  groupname character varying(20) NOT NULL,
  CONSTRAINT jpforum_sectiongroups_pkey PRIMARY KEY (section, groupname)
)
WITH (OIDS=TRUE);

ALTER TABLE jpforum_sectiongroups
  ADD CONSTRAINT jpforum_sectiongroups_groups FOREIGN KEY (groupname)
      REFERENCES authgroups (groupname) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
 */
