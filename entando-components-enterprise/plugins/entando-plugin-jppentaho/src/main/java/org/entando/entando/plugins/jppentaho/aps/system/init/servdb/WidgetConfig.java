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
package org.entando.entando.plugins.jppentaho.aps.system.init.servdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.IDatabaseManager;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = WidgetConfig.TABLE_NAME)
public class WidgetConfig implements ExtendedColumnDefinition {
	
	public WidgetConfig() {}
	
	@DatabaseField(columnName = "authuser", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _authUser;
	
	@DatabaseField(columnName = "pagecode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false)
	private String _pageCode;
	
	@DatabaseField(columnName = "framepos", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _framePos;
	
	@DatabaseField(columnName = "widgetcode", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _showlet;
	
	@DatabaseField(columnName = "config", 
			dataType = DataType.LONG_STRING)
	private String _config;
	
	@DatabaseField(columnName = "name", 
			dataType = DataType.STRING)
	private String _name;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return new String[]{"ALTER TABLE " + TABLE_NAME + " ADD CONSTRAINT " + TABLE_NAME + "_pkey PRIMARY KEY(authuser, pagecode, framepos)"};
	}
	
	public static final String TABLE_NAME = "jppentaho_widgetconfig";
	
}
/*
CREATE TABLE jppentaho_widgetconfig
(
  authuser character varying(40) NOT NULL,
  pagecode character varying(30) NOT NULL,
  framepos integer NOT NULL,
  widgetcode character varying(40) NOT NULL,
  config character varying,
  "name" character varying(254),
  CONSTRAINT jppentaho_showletconfig_pkey PRIMARY KEY (authuser, pagecode, framepos)
) WITH (OIDS=TRUE);
*/