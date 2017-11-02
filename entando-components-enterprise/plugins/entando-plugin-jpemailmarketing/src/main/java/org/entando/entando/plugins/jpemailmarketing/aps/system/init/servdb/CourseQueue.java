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

@DatabaseTable(tableName = CourseQueue.TABLE_NAME)
public class CourseQueue implements ExtendedColumnDefinition {

	public CourseQueue() {}

	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull = false, id = true)
	private int _id;

	@DatabaseField(columnName = "operationindex", 
			dataType = DataType.LONG, 
			canBeNull = true)
	private long _operationindex;

	@DatabaseField(columnName = "subscriberid", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _subscriberid;

	@DatabaseField(columnName = "coursemailid", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _coursemailid;

	@DatabaseField(columnName = "scheduleddate", 
			dataType = DataType.DATE,
			canBeNull = false)
	private Date _scheduledDate;

	@DatabaseField(columnName = "sent", 
			dataType = DataType.INTEGER, 
			canBeNull = false)
	private int _sent;
	
	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
			canBeNull = false)
	private Date _createdat;

	@DatabaseField(columnName = "updatedat", 
			dataType = DataType.DATE,
			canBeNull = false)
	private Date _updatedat;

	@DatabaseField(columnName = "mgmessageid", 
			dataType = DataType.STRING, 
			width=255,  canBeNull= true)
	private String _mgmessageid;

	@DatabaseField(columnName = "mgstatus", 
			dataType = DataType.INTEGER, 
			canBeNull = true)
	private int _mgstatus;

	@Override
	public String[] extensions(IDatabaseManager.DatabaseType type) {
		String tableName = TABLE_NAME;
		String subscriberTablename = Subscriber.TABLE_NAME;
		String courseMailTablename = CourseMail.TABLE_NAME;
		if (IDatabaseManager.DatabaseType.MYSQL.equals(type)) {
			tableName = "`" + tableName + "`";
			subscriberTablename = "`" + subscriberTablename + "`";
			courseMailTablename = "`" + courseMailTablename + "`";
		}
		return new String[]{
				"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT jpemailmarketing_coursequeuecoursemail_fkey FOREIGN KEY (coursemailid) "
				+ "REFERENCES " + courseMailTablename + " (id)",
				
				"ALTER TABLE " + tableName + " "
				+ "ADD CONSTRAINT jpemailmarketing_coursequeuesubscrib_fkey FOREIGN KEY (subscriberid) "
				+ "REFERENCES " + subscriberTablename + " (id)"
		};
	}

	public static final String TABLE_NAME = "jpemailmarketing_coursequeue";
}
