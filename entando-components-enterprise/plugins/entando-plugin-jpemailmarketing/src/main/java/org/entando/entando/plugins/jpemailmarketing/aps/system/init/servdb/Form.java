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

@DatabaseTable(tableName = Form.TABLE_NAME)
public class Form implements ExtendedColumnDefinition {

	public Form() {}

	@DatabaseField(columnName = "courseid", 
			dataType = DataType.INTEGER, 
			canBeNull= false,  id = true)
	private int _courseId;

	@DatabaseField(columnName = "filecovername", 
			dataType = DataType.STRING,
			canBeNull= true)
	private String _fileCoverName;

	@DatabaseField(columnName = "fileincentivename", 
			dataType = DataType.STRING,
			canBeNull= true)
	private String _fileIncentiveName;

	@DatabaseField(columnName = "emailsubject", 
			dataType = DataType.LONG_STRING,
			canBeNull=false)
	private String _emailSubject;

	@DatabaseField(columnName = "emailtext", 
			dataType = DataType.LONG_STRING,
			canBeNull=false)
	private String _emailText;

	@DatabaseField(columnName = "emailbutton",
			dataType = DataType.LONG_STRING,
			 canBeNull=false)
		private String _emailButton;
		
		@DatabaseField(columnName = "emailmessagefriendly",
			dataType = DataType.LONG_STRING,
			 canBeNull= true)
		private String _emailMessageFriendly;
	
	@DatabaseField(columnName = "createdat", 
			dataType = DataType.DATE,
			canBeNull= false)
	private Date _createdat;

	@DatabaseField(columnName = "updatedat", 
			dataType = DataType.DATE,
			canBeNull= false)
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
				+ "ADD CONSTRAINT jpemailmarketing_formcourse_fkey FOREIGN KEY (courseid) "
				+ "REFERENCES " + courseTablename + " (id)"};
	}

	public static final String TABLE_NAME = "jpemailmarketing_form";
}

/*

-- Table: jpemailmarketing_form

-- DROP TABLE jpemailmarketing_form;

CREATE TABLE jpemailmarketing_form
(
  courseid integer NOT NULL,
  filecovername character varying(255),
  fileincentivename character varying(255),
  emailsubject text NOT NULL,
  emailtext text NOT NULL,
  emailbutton text NOT NULL,
  emailmessagefriendly text,
  createdat timestamp without time zone NOT NULL,
  updatedat timestamp without time zone NOT NULL,
  CONSTRAINT jpemailmarketing_form_pkey PRIMARY KEY (courseid),
  CONSTRAINT jpemailmarketing_formcourse_fkey FOREIGN KEY (courseid)
      REFERENCES jpemailmarketing_course (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE jpemailmarketing_form
  OWNER TO agile;


*/
