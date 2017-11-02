/*
*
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
 */
package org.entando.entando.plugins.jpsubsites.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
@DatabaseTable(tableName = Subsite.TABLE_NAME)
public class Subsite {

	public Subsite() {
	}

	/*
  code character varying(30) NOT NULL,
  titles character varying,
  descriptions character varying,
  homepage character varying(30) NOT NULL,
	 */
	@DatabaseField(columnName = "code",
			dataType = DataType.STRING,
			width = 30,
			canBeNull = false, id = true)
	private String _code;

	@DatabaseField(columnName = "titles",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _titles;

	@DatabaseField(columnName = "descriptions",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _descriptions;

	@DatabaseField(columnName = "homepage",
			dataType = DataType.STRING,
			width = 30, canBeNull = false, unique = true)
	private String _homepage;

	/*
  groupname character varying(20),
  catcode character varying(30) NOT NULL,
  contentviewerpage character varying(30),
  cssfilename character varying(50),
  imgfilename character varying(50),
	 */
	@DatabaseField(columnName = "visibility",
			dataType = DataType.SHORT, canBeNull = true)
	private short _visibility;

	@DatabaseField(columnName = "groupname",
			dataType = DataType.STRING,
			width = 20,
			canBeNull = true)
	private String _groupName;

	@DatabaseField(columnName = "catcode",
			dataType = DataType.STRING,
			width = 30, canBeNull = false, unique = true)
	private String _categoryCode;

	@DatabaseField(columnName = "contentviewerpage",
			dataType = DataType.STRING,
			width = 30, canBeNull = true)
	private String _contentViewerPage;

	@DatabaseField(columnName = "viewerpages",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _viewerPages;

	@DatabaseField(columnName = "cssfilename",
			dataType = DataType.STRING,
			width = 50, canBeNull = true)
	private String _cssfilename;

	@DatabaseField(columnName = "imgfilename",
			dataType = DataType.STRING,
			width = 50, canBeNull = true)
	private String _imgfilename;

	public static final String TABLE_NAME = "jpsubsites_subsites";

}
/*
CREATE TABLE jpsubsites_subsites (
  code character varying(30) NOT NULL,
  titles character varying,
  descriptions character varying,
  homepage character varying(30) NOT NULL,
  groupname character varying(20),
  catcode character varying(30) NOT NULL,
  contentviewerpage character varying(30),
  viewerpages character varying,
  cssfilename character varying(50),
  imgfilename character varying(50),
  CONSTRAINT jpsubsite_subsites_pkey PRIMARY KEY (code),
  CONSTRAINT jpsubsites_subsites_catcode_key UNIQUE (catcode),
  CONSTRAINT jpsubsites_subsites_homepage_key UNIQUE (homepage)
);
 */
