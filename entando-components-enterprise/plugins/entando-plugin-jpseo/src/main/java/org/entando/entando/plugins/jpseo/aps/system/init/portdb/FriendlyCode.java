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
package org.entando.entando.plugins.jpseo.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = FriendlyCode.TABLE_NAME)
public class FriendlyCode {
	
	public FriendlyCode() {}
	
	@DatabaseField(columnName = "friendlycode", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false, id = true)
	private String _friendlyCode;
	
	@DatabaseField(columnName = "pagecode", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = true)
	private String _pageCode;
	
	@DatabaseField(columnName = "contentid", 
			dataType = DataType.STRING, 
			width = 16, 
			canBeNull = true)
	private String _contentId;
	
	@DatabaseField(columnName = "langcode", 
			dataType = DataType.STRING, 
			width = 2, 
			canBeNull = true)
	private String _langcode;
	
	public static final String TABLE_NAME = "jpseo_friendlycode";
	
}
/*
CREATE TABLE jpseo_friendlycode
(
  friendlycode character varying(100) NOT NULL,
  pagecode character varying(30),
  contentid character varying(16),
  langcode character varying(2),
  CONSTRAINT jpseo_friendlycode_pkey PRIMARY KEY (friendlycode),
--  CONSTRAINT jpseo_friendlycode_contentid_fkey FOREIGN KEY (contentid)
--      REFERENCES contents (contentid) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION,
--  CONSTRAINT jpseo_friendlycode_pagecode_fkey FOREIGN KEY (pagecode)
--      REFERENCES pages (code) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION
)
 */