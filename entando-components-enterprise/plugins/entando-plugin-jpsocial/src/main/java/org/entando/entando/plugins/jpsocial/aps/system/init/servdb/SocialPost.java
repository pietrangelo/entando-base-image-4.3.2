package org.entando.entando.plugins.jpsocial.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

/**
 * @author scaffolder
 */
@DatabaseTable(tableName = SocialPost.TABLE_NAME)
public class SocialPost {
	
	public SocialPost() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "text", 
		dataType = DataType.LONG_STRING,
		 canBeNull=false)
	private String _text;
	
	@DatabaseField(columnName = "date", 
			dataType = DataType.DATE,
		 canBeNull=false)
	private Date _date;
	
	@DatabaseField(columnName = "permalink", 
		dataType = DataType.LONG_STRING,
		 canBeNull= true)
	private String _permalink;
	
	@DatabaseField(columnName = "service", 
		dataType = DataType.LONG_STRING,
		 canBeNull= true)
	private String _service;
	
	@DatabaseField(columnName = "objectid", 
		dataType = DataType.LONG_STRING,
		 canBeNull= true)
	private String _objectid;
	
	@DatabaseField(columnName = "provider", 
		dataType = DataType.LONG_STRING,
		 canBeNull=false)
	private String _provider;
	
	@DatabaseField(columnName = "socialid", 
		dataType = DataType.LONG_STRING,
		 canBeNull= false)
	private String _socialid;
	
	@DatabaseField(columnName = "username", 
		dataType = DataType.LONG_STRING,
		 canBeNull=false)
	private String _username;
	

public static final String TABLE_NAME = "jpsocial_socialpost";
}
