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
package org.entando.entando.plugins.jpmultisite.aps.system.init.portdb;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Multisite.TABLE_NAME)
public class Multisite {

	public Multisite() {
	}

	@DatabaseField(columnName = "code",
			dataType = DataType.STRING,
			width = 40, canBeNull = false, id = true)
	private String _code;

	@DatabaseField(columnName = "titles",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _titles;

	@DatabaseField(columnName = "descriptions",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _descriptions;

	@DatabaseField(columnName = "url",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _url;

	@DatabaseField(columnName = "headerimage",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _headerImage;

	@DatabaseField(columnName = "templatecss",
			dataType = DataType.LONG_STRING,
			canBeNull = true)
	private String _templateCss;

	@DatabaseField(columnName = "catcode",
			dataType = DataType.STRING,
			width = 30,
			canBeNull = false,
			unique = true)
	private String _catCode;

	public static final String TABLE_NAME = "jpmultisite_multisite";
}
