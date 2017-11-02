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
@DatabaseTable(tableName = Section.TABLE_NAME)
public class Section {

	public Section() {}
	
	@DatabaseField(columnName = "code", 
			dataType = DataType.STRING, 
			width = 30, 
			canBeNull = false, id = true)
	private String _code;
	
	@DatabaseField(columnName = "parentcode", 
			dataType = DataType.STRING, 
			width = 30, canBeNull = true)
	private String _parentCode;
	
	@DatabaseField(columnName = "pos", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _pos;
	
	@DatabaseField(columnName = "opensection", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = true)
	private String _open;
	/*
  code character varying(30) NOT NULL,
  parentcode character varying(30),
  pos integer NOT NULL,
  opensection character varying(5),
	 */
	
	@DatabaseField(columnName = "titles", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _titles;
	
	@DatabaseField(columnName = "descriptions", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _descriptions;
	
	@DatabaseField(columnName = "unauthbahaviour", 
			dataType = DataType.INTEGER, 
			canBeNull = true, defaultValue = "0")
	private int _unauthbahaviour;
	/*
  titles character varying NOT NULL,
  descriptions character varying NOT NULL,
  unauthbahaviour integer DEFAULT 0,
	 */
	
	public static final String TABLE_NAME = "jpforum_sections";
	
}
/*
CREATE TABLE jpforum_sections
(
  code character varying(30) NOT NULL,
  parentcode character varying(30),
  pos integer NOT NULL,
  open character varying(5),
  titles character varying NOT NULL,
  descriptions character varying NOT NULL,
  unauthbahaviour integer DEFAULT 0,
  CONSTRAINT jpforum_sections_pkey PRIMARY KEY (code)
)
 */