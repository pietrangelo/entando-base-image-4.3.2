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

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Sanction.TABLE_NAME)
public class Sanction {
	
	public Sanction() {}
	
	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "startdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _startDate;
	
	@DatabaseField(columnName = "enddate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _endDate;
	
	@DatabaseField(columnName = "moderator", 
			dataType = DataType.STRING, 
			width = 20, 
			canBeNull = false)
	private String _moderator;
	
	public static final String TABLE_NAME = "jpforum_sanctions";
	
}
/*
CREATE TABLE jpforum_sanctions
(
  id integer NOT NULL,
  username character varying(20) NOT NULL,
  startdate timestamp without time zone NOT NULL,
  enddate timestamp without time zone NOT NULL,
  moderator character varying(20) NOT NULL,
  CONSTRAINT jpforum_sanctions_pkey PRIMARY KEY (id)
)
 */