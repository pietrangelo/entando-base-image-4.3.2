/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify;

import java.util.List;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;

public interface INotifyDAO {

	public List<Integer> searchNotifys(FieldSearchFilter[] filters);
	
	public Notify loadNotify(int id);

	public List<Integer> loadNotifys();

	public void removeNotify(int id);
	
	public void updateNotify(Notify notify);

	public void insertNotify(Notify notify);
	

}