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
@DatabaseTable(tableName = Message.TABLE_NAME)
public class Message implements ExtendedColumnDefinition {
	
	public Message() {}
	
	@DatabaseField(id = true, columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _code;
	
	@DatabaseField(columnName = "sender", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _sender;
	
	@DatabaseField(columnName = "recipient", 
			dataType = DataType.STRING, 
			width = 40, canBeNull = false)
	private String _recipient;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.STRING, 
			width = 100, canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "body", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _body;
	
	@DatabaseField(columnName = "messagetype", 
			dataType = DataType.STRING, 
			width = 20, canBeNull = false)
	private String _messageType;
	
	@DatabaseField(columnName = "messagedate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _messageDate;
	
	@DatabaseField(columnName = "toread", 
			dataType = DataType.STRING, 
			width = 5, canBeNull = true)
	private String _toRead;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		return null;
	}
	
	public static final String TABLE_NAME = "jpforum_messages";
	
}
/*
CREATE TABLE jpforum_messages
(
  code integer NOT NULL,
  sender character varying(20) NOT NULL,
  recipient character varying(20) NOT NULL,
  title character varying(100) NOT NULL,
  body character varying NOT NULL,
  messagetype character varying(20) NOT NULL,
  messagedate timestamp without time zone NOT NULL,
  toread character varying(5),
  CONSTRAINT jpforum_messages_pkey PRIMARY KEY (code)
)
WITH (OIDS=TRUE);
*/