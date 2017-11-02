/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify.aps.system.services.notify;

import org.entando.entando.plugins.jpnotify.aps.system.services.notify.event.NotifyChangedEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;
import org.entando.entando.plugins.jpnotify.aps.system.services.notify.api.JAXBNotify;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import java.util.Date;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyManager extends AbstractService implements INotifyManager {

	private static final Logger _logger =  LoggerFactory.getLogger(NotifyManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public Notify getNotify(int id) throws ApsSystemException {
		Notify notify = null;
		try {
			notify = this.getNotifyDAO().loadNotify(id);
		} catch (Throwable t) {
			_logger.error("Error loading notify with id '{}'", id,  t);
			throw new ApsSystemException("Error loading notify with id: " + id, t);
		}
		return notify;
	}

	@Override
	public List<Integer> getNotifys() throws ApsSystemException {
		List<Integer> notifys = new ArrayList<Integer>();
		try {
			notifys = this.getNotifyDAO().loadNotifys();
		} catch (Throwable t) {
			_logger.error("Error loading Notify list",  t);
			throw new ApsSystemException("Error loading Notify ", t);
		}
		return notifys;
	}

	@Override
	public List<Integer> searchNotifys(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> notifys = new ArrayList<Integer>();
		try {
			notifys = this.getNotifyDAO().searchNotifys(filters);
		} catch (Throwable t) {
			_logger.error("Error searching Notifys", t);
			throw new ApsSystemException("Error searching Notifys", t);
		}
		return notifys;
	}

	@Override
	public void addNotify(Notify notify) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			notify.setId(key);
			this.getNotifyDAO().insertNotify(notify);
			this.notifyNotifyChangedEvent(notify, NotifyChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error adding Notify", t);
			throw new ApsSystemException("Error adding Notify", t);
		}
	}
 
	@Override
	public void updateNotify(Notify notify) throws ApsSystemException {
		try {
			this.getNotifyDAO().updateNotify(notify);
			this.notifyNotifyChangedEvent(notify, NotifyChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error updating Notify", t);
			throw new ApsSystemException("Error updating Notify " + notify, t);
		}
	}

	@Override
	public void deleteNotify(int id) throws ApsSystemException {
		try {
			Notify notify = this.getNotify(id);
			this.getNotifyDAO().removeNotify(id);
			this.notifyNotifyChangedEvent(notify, NotifyChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error deleting Notify with id {}", id, t);
			throw new ApsSystemException("Error deleting Notify with id:" + id, t);
		}
	}


	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/notifys?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBNotify> getNotifysForApi(Properties properties) throws Throwable {
		List<JAXBNotify> list = new ArrayList<JAXBNotify>();
		List<Integer> idList = this.getNotifys();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> notifyIterator = idList.iterator();
			while (notifyIterator.hasNext()) {
				int currentid = notifyIterator.next();
				Notify notify = this.getNotify(currentid);
				if (null != notify) {
					list.add(new JAXBNotify(notify));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/notify?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
    public JAXBNotify getNotifyForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
		JAXBNotify jaxbNotify = null;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        Notify notify = this.getNotify(id);
        if (null == notify) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Notify with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbNotify = new JAXBNotify(notify);
        return jaxbNotify;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/notify 
     * @param jaxbNotify
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addNotifyForApi(JAXBNotify jaxbNotify) throws ApiException, ApsSystemException {
        if (null != this.getNotify(jaxbNotify.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Notify with id " + jaxbNotify.getId() + " already exists", Response.Status.CONFLICT);
        }
        Notify notify = jaxbNotify.getNotify();
        this.addNotify(notify);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/notify 
     * @param jaxbNotify
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateNotifyForApi(JAXBNotify jaxbNotify) throws ApiException, ApsSystemException {
        if (null == this.getNotify(jaxbNotify.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Notify with id " + jaxbNotify.getId() + " does not exist", Response.Status.CONFLICT);
        }
        Notify notify = jaxbNotify.getNotify();
        this.updateNotify(notify);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/notify?id=1
	 * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteNotifyForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        this.deleteNotify(id);
    }

	private void notifyNotifyChangedEvent(Notify notify, int operationCode) {
		NotifyChangedEvent event = new NotifyChangedEvent();
		event.setNotify(notify);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setNotifyDAO(INotifyDAO notifyDAO) {
		 this._notifyDAO = notifyDAO;
	}
	protected INotifyDAO getNotifyDAO() {
		return _notifyDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private INotifyDAO _notifyDAO;
}
