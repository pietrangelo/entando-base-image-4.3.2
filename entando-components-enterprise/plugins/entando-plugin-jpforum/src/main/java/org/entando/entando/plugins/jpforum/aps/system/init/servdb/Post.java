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
@DatabaseTable(tableName = Post.TABLE_NAME)
public class Post implements ExtendedColumnDefinition {
	
	public Post() {}
	
	@DatabaseField(id = true, columnName = "code", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _keyCode;
	
	@DatabaseField(columnName = "username", 
			dataType = DataType.STRING, 
			width = 40, 
			canBeNull = false)
	private String _username;
	
	@DatabaseField(columnName = "title", 
			dataType = DataType.STRING, 
			width = 100, 
			canBeNull = false)
	private String _title;
	
	@DatabaseField(columnName = "text", 
			dataType = DataType.LONG_STRING, 
			canBeNull = false)
	private String _text;
	
	@DatabaseField(columnName = "postdate", 
			dataType = DataType.DATE, 
			canBeNull = false)
	private Date _postDate;
	
	@DatabaseField(foreign = true, columnName = "threadid", 
			canBeNull = false)
	private Thread _threadId;
	
	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String threadTableName = Thread.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			threadTableName = "`" + threadTableName + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " " 
				+ "ADD CONSTRAINT " + TABLE_NAME + "_thread_fkey FOREIGN KEY (threadid) "
				+ "REFERENCES " + threadTableName + " (code)"};
	}
	
	public static final String TABLE_NAME = "jpforum_posts";
	
}
/*
CREATE TABLE jpforum_posts
(
  code integer NOT NULL,
  username character varying(20) NOT NULL,
  title character varying(100) NOT NULL,
  "text" character varying NOT NULL,
  postdate timestamp without time zone NOT NULL,
  threadid integer NOT NULL,
  CONSTRAINT jpforum_posts_pkey PRIMARY KEY (code)
)
WITH (OIDS=TRUE);
 */