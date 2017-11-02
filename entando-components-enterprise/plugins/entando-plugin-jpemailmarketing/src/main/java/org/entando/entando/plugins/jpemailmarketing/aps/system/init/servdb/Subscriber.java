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
package org.entando.entando.plugins.jpemailmarketing.aps.system.init.servdb;
import java.util.Date;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Subscriber.TABLE_NAME)
public class Subscriber implements ExtendedColumnDefinition {

	public Subscriber() {}

	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull=false, id = true)
	private int _id;

	@DatabaseField(columnName = "courseid", 
			dataType = DataType.INTEGER, 
			canBeNull=false)
	private int _courseId;

	@DatabaseField(columnName = "name", 
			dataType = DataType.STRING, 
			width=255,  canBeNull=false)
	private String _name;

	@DatabaseField(columnName = "email", 
			dataType = DataType.STRING, 
			width=255,  canBeNull=false)
	private String _email;

	@DatabaseField(columnName = "hash", 
			dataType = DataType.STRING, 
			width=255,  canBeNull=false)
	private String _hash;

	@DatabaseField(columnName = "status", 
			dataType = DataType.STRING, 
			width=40,  canBeNull=false)
	private String _status;

	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
			canBeNull=false)
	private Date _createdat;

	@DatabaseField(columnName = "updatedat", 
			dataType = DataType.DATE,
			canBeNull=false)
	private Date _updatedat;

	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String courseTablename = Course.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			courseTablename = "`" + courseTablename + "`";
		}
		return new String[]{"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT jpemailmarketing_subscr_course_fkey FOREIGN KEY (courseid) "
				+ "REFERENCES " + courseTablename + " (id)"};
	}

	public static final String TABLE_NAME = "jpemailmarketing_subscriber";
}
