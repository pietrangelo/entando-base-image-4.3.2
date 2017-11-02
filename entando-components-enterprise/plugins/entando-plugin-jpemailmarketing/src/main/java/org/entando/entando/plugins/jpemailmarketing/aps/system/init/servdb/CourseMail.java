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

@DatabaseTable(tableName = CourseMail.TABLE_NAME)
public class CourseMail implements ExtendedColumnDefinition {

	public CourseMail() {}

	@DatabaseField(columnName = "id", 
			dataType = DataType.INTEGER, 
			canBeNull=false, id = true)
	private int _id;

	@DatabaseField(columnName = "courseid", 
			dataType = DataType.INTEGER, 
			canBeNull= false, uniqueCombo=true)
	private int _courseId;

	@DatabaseField(columnName = "position", 
			dataType = DataType.INTEGER, 
			canBeNull= false, uniqueCombo=true)
	private int _position;

	@DatabaseField(columnName = "dalaydays", 
			dataType = DataType.INTEGER, 
			canBeNull=false)
	private int _dalayDays;

	@DatabaseField(columnName = "emailsubject", 
			dataType = DataType.STRING, 
			width=255,  canBeNull=false)
	private String _emailSubject;

	@DatabaseField(columnName = "emailcontent", 
			dataType = DataType.LONG_STRING,
			canBeNull=false)
	private String _emailContent;

	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
			canBeNull= true)
	private Date _createdat;

	@DatabaseField(columnName = "updatedat", 
			dataType = DataType.DATE,
			canBeNull= true)
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
				+ "ADD CONSTRAINT jpemailmarketing_coursemailcourse_fkey FOREIGN KEY (courseid) "
				+ "REFERENCES " + courseTablename + " (id)"};
	}
	
	public static final String TABLE_NAME = "jpemailmarketing_coursemail";
	
/*
-- DROP TABLE jpemailmarketing_coursemail;

CREATE TABLE jpemailmarketing_coursemail
(
  id integer NOT NULL,
  courseid integer NOT NULL,
  dalaydays integer NOT NULL,
  emailsubject character varying(255) NOT NULL,
  emailcontent text NOT NULL,
  createdat timestamp without time zone,
  updatedat timestamp without time zone,
  CONSTRAINT jpemailmarketing_coursemail_pkey PRIMARY KEY (id),
  CONSTRAINT jpemailmarketing_coursemailcourse_fkey FOREIGN KEY (courseid)
      REFERENCES jpemailmarketing_course (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE jpemailmarketing_coursemail
  OWNER TO agile;
 
 */
}
