/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.init.servdb;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable(tableName = Notify.TABLE_NAME)
public class Notify {
	
	public Notify() {}
	
	@DatabaseField(columnName = "id", 
		dataType = DataType.INTEGER, 
		 canBeNull=false, id = true)
	private int _id;
	
	@DatabaseField(columnName = "payload", 
		dataType = DataType.LONG_STRING,
		 canBeNull=false)
	private String _payload;
	
	@DatabaseField(columnName = "senddate", 
			dataType = DataType.DATE,
		 canBeNull=false)
	private Date _senddate;
	
	@DatabaseField(columnName = "attempts", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _attempts;
	
	@DatabaseField(columnName = "sent", 
		dataType = DataType.INTEGER, 
		 canBeNull= true)
	private int _sent;
	
	@DatabaseField(columnName = "sender", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _sender;
	
	@DatabaseField(columnName = "recipient", 
		dataType = DataType.STRING, 
		width=50,  canBeNull= true)
	private String _recipient;
	

public static final String TABLE_NAME = "jpnotify_notify";
}
