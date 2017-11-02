/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;

public interface INotifyManager {

	public Notify getNotify(int id) throws ApsSystemException;

	public List<Integer> getNotifys() throws ApsSystemException;

	public List<Integer> searchNotifys(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addNotify(Notify notify) throws ApsSystemException;

	public void updateNotify(Notify notify) throws ApsSystemException;

	public void deleteNotify(int id) throws ApsSystemException;

}