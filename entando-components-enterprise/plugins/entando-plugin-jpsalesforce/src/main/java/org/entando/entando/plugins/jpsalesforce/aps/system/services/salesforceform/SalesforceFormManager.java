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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.Response;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.ISalesforceManager;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.api.JAXBSalesforceForm;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforceform.event.SalesforceFormChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;


/**
 * This class handles the operations related to the Salesforce web to lead form 
 * @author entando
 *
 */
@Aspect
public class SalesforceFormManager extends AbstractService implements ISalesforceFormManager {

	private static final Logger _logger =  LoggerFactory.getLogger(SalesforceFormManager.class);

	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().debug(this.getClass().getName() + ": ready");
	}

	@Override
	public SalesforceForm getSalesforceForm(int id) throws ApsSystemException {
		SalesforceForm salesforceForm = null;
		try {
			salesforceForm = this.getSalesforceFormDAO().loadSalesforceForm(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSalesforceForms");
			throw new ApsSystemException("Error loading salesforceForm with id: " + id, t);
		}
		return salesforceForm;
	}

	@Override
	public List<Integer> getSalesforceForms() throws ApsSystemException {
		List<Integer> salesforceForms = new ArrayList<Integer>();
		try {
			salesforceForms = this.getSalesforceFormDAO().loadSalesforceForms();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSalesforceForms");
			throw new ApsSystemException("Error loading SalesforceForm ", t);
		}
		return salesforceForms;
	}

	@Override
	public List<Integer> searchSalesforceForms(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> salesforceForms = new ArrayList<Integer>();
		try {
			salesforceForms = this.getSalesforceFormDAO().searchSalesforceForms(filters);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchSalesforceForms");
			throw new ApsSystemException("Error searching SalesforceForms ", t);
		}
		return salesforceForms;
	}

	@Override
	public void addSalesforceForm(SalesforceForm salesforceForm) throws ApsSystemException {
		try {
			this.getSalesforceFormDAO().insertSalesforceForm(salesforceForm);
			this.notifySalesforceFormChangedEvent(salesforceForm, SalesforceFormChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addSalesforceForm");
			throw new ApsSystemException("Error adding SalesforceForm", t);
		}
	}

	@Override
	public void updateSalesforceForm(SalesforceForm salesforceForm) throws ApsSystemException {
		try {
			this.getSalesforceFormDAO().updateSalesforceForm(salesforceForm);
			this.notifySalesforceFormChangedEvent(salesforceForm, SalesforceFormChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateSalesforceForm");
			throw new ApsSystemException("Error updating SalesforceForm " + salesforceForm, t);
		}
	}

	@Override
	public void deleteSalesforceForm(int id) throws ApsSystemException {
		try {
			SalesforceForm salesforceForm = this.getSalesforceForm(id);
			this.getSalesforceFormDAO().removeSalesforceForm(id);
			this.notifySalesforceFormChangedEvent(salesforceForm, SalesforceFormChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSalesforceForm");
			throw new ApsSystemException("Error deleting SalesforceForm wiwh id:" + id, t);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String addFrameForm(SalesforceForm config) throws Throwable {
		String prototypeCode = null;
		try {
			Integer frame = config.getFrame();
			// check whether there is already a record for this configuration
			FieldSearchFilter frameFilter = new FieldSearchFilter("frame", frame, false);
			FieldSearchFilter pageFilter = new FieldSearchFilter("pagecode", config.getPagecode(), false);
			FieldSearchFilter filters[] = {frameFilter, pageFilter};
			List<Integer> ids = getSalesforceFormDAO().searchSalesforceForms(filters);
			
			if (null != ids
					&& !ids.isEmpty()) {
				int id = ids.get(0);
				SalesforceForm entry = getSalesforceFormDAO().loadSalesforceForm(id);
				_logger.debug("Updating entity '{}'", entry.getCode());
				// set the ID of the configuration
				_logger.debug("Setting the id '{}'", id);
				config.setId(id);
				// create a new entity
				getMessageDAO().updateMessageEntity(config.getConfig(), entry.getCode());
				_logger.debug("Updated entity '{}'", prototypeCode);
				config.setCode(entry.getCode());
				// update the form record
				getSalesforceFormDAO().updateSalesforceForm(config);
				_logger.debug("Updated form configuration with id '{}'", config.getId());
				
				// the code hasn't changed
				prototypeCode = entry.getCode();
			} else {
				// create a new entity
				prototypeCode = getMessageDAO().createMessageEntity(config.getConfig());
				_logger.debug("Created new entity '{}'", prototypeCode);
				config.setCode(prototypeCode);
				// add the new configuration
				getSalesforceFormDAO().insertSalesforceForm(config);
				_logger.debug("Created new form configuration with id '{}'", config.getId());
			}
		} catch (Throwable t) {
			_logger.error("Error while saving a form configuration with entity creation", t);
			throw new ApsSystemException("Error while saving a form configuration with entity creation", t);
		}
		return prototypeCode;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public String replaceFrameForm(SalesforceForm config) throws Throwable {
		String prototypeCode = null;
		try {
			Integer frame = config.getFrame();
			// check whether there is already a record for this configuration
			FieldSearchFilter frameFilter = new FieldSearchFilter("frame", frame, false);
			FieldSearchFilter pageFilter = new FieldSearchFilter("pagecode", config.getPagecode(), false);
			FieldSearchFilter filters[] = {frameFilter, pageFilter};
			List<Integer> ids = getSalesforceFormDAO().searchSalesforceForms(filters);
			
			if (null != ids
					&& !ids.isEmpty()) {
				int id = ids.get(0);
				SalesforceForm entry = getSalesforceFormDAO().loadSalesforceForm(id);
				// voiding frame position
				entry.setFrame(Integer.valueOf(-1));
				// update
				getSalesforceFormDAO().updateSalesforceForm(entry);
			}
			// create a new entity
			prototypeCode = getMessageDAO().createMessageEntity(config.getConfig());
			_logger.debug("Created new entity '{}'", prototypeCode);
			config.setCode(prototypeCode);
			// add the new configuration
			getSalesforceFormDAO().insertSalesforceForm(config);
			_logger.debug("Created new form configuration with id '{}'", config.getId());
		} catch (Throwable t) {
			_logger.error("Error while saving a form configuration with entity creation", t);
			throw new ApsSystemException("Error while saving a form configuration with entity creation", t);
		}
		return prototypeCode;
	}
	
	@SuppressWarnings("rawtypes")
	@AfterReturning(pointcut = "execution(* com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager.sendMessage(..)) && args(message,..)")
	public void submitWebToLeadForm(Message message) throws Throwable {
		try {
			if (null != message) {
				String type = message.getEntityPrototype().getTypeCode();
				FieldSearchFilter filter = new FieldSearchFilter("code", type, false);
				FieldSearchFilter filters[] = {filter};
				List<Integer> ids = getSalesforceFormDAO().searchSalesforceForms(filters);
				if (null != ids 
						&& !ids.isEmpty()) {
					int id = ids.get(0);

					SalesforceForm form = getSalesforceFormDAO().loadSalesforceForm(id);
					Map<String, String> args = getMessageDAO().prepareWebToLeadForm(message, form.getConfig());
					boolean submitted = 
							getSalesforceManager().submitWeb2LeadForm(form.getConfig().getCampaignId(), args);
					if (submitted) {
						_logger.info("Web to lead form successfully submitted to salesforce");
					} else {
						_logger.error("Web to lead form could not be submitted to salesforce");
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error submitting a web to lead form", t);
			throw new ApsSystemException("Error submitting a web to lead form", t);
		}
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean addUpdateSalesforceForm(SalesforceForm form) throws Throwable {
		boolean isAdded = false;
		try {
			SalesforceForm target = getSalesforceFormDAO().loadSalesforceForm(form.getId());
			if (null != target) {
				this.updateSalesforceForm(form);
			} else {
				this.addSalesforceForm(form);
				isAdded = true;
			}
		} catch (Throwable t) {
			_logger.error("Unexpected error handling the form to add or update", t);
			throw new ApsSystemException("Unexpected error handling the form to add or update", t);
		}
		return isAdded;
	}
	
	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/salesforceForms?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBSalesforceForm> getSalesforceFormsForApi(Properties properties) throws Throwable {
		List<JAXBSalesforceForm> list = new ArrayList<JAXBSalesforceForm>();
		List<Integer> idList = this.getSalesforceForms();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> salesforceFormIterator = idList.iterator();
			while (salesforceFormIterator.hasNext()) {
				int currentid = salesforceFormIterator.next();
				SalesforceForm salesforceForm = this.getSalesforceForm(currentid);
				if (null != salesforceForm) {
					list.add(new JAXBSalesforceForm(salesforceForm));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/salesforceForm?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public JAXBSalesforceForm getSalesforceFormForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		JAXBSalesforceForm jaxbSalesforceForm = null;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		SalesforceForm salesforceForm = this.getSalesforceForm(id);
		if (null == salesforceForm) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SalesforceForm with id '" + idString + "' does not exist", Response.Status.CONFLICT);
		}
		jaxbSalesforceForm = new JAXBSalesforceForm(salesforceForm);
		return jaxbSalesforceForm;
	}

	/**
	 * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/salesforceForm 
	 * @param jaxbSalesforceForm
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void addSalesforceFormForApi(JAXBSalesforceForm jaxbSalesforceForm) throws ApiException, ApsSystemException {
		if (null != this.getSalesforceForm(jaxbSalesforceForm.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SalesforceForm with id " + jaxbSalesforceForm.getId() + " already exists", Response.Status.CONFLICT);
		}
		SalesforceForm salesforceForm = jaxbSalesforceForm.getSalesforceForm();
		this.addSalesforceForm(salesforceForm);
	}

	/**
	 * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/salesforceForm 
	 * @param jaxbSalesforceForm
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void updateSalesforceFormForApi(JAXBSalesforceForm jaxbSalesforceForm) throws ApiException, ApsSystemException {
		if (null == this.getSalesforceForm(jaxbSalesforceForm.getId())) {
			throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "SalesforceForm with id " + jaxbSalesforceForm.getId() + " does not exist", Response.Status.CONFLICT);
		}
		SalesforceForm salesforceForm = jaxbSalesforceForm.getSalesforceForm();
		this.updateSalesforceForm(salesforceForm);
	}

	/**
	 * DELETE http://localhost:8080/<portal>/api/rs/en/salesforceForm?id=1
	 * @param properties
	 * @throws ApiException
	 * @throws ApsSystemException
	 */
	public void deleteSalesforceFormForApi(Properties properties) throws Throwable {
		String idString = properties.getProperty("id");
		int id = 0;
		try {
			id = Integer.parseInt(idString);
		} catch (NumberFormatException e) {
			throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
		}
		this.deleteSalesforceForm(id);
	}

	private void notifySalesforceFormChangedEvent(SalesforceForm salesforceForm, int operationCode) {
		SalesforceFormChangedEvent event = new SalesforceFormChangedEvent();
		event.setSalesforceForm(salesforceForm);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

	public ISalesforceManager getSalesforceManager() {
		return _salesforceManager;
	}

	public void setSalesforceManager(ISalesforceManager salesforceManager) {
		this._salesforceManager = salesforceManager;
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setSalesforceFormDAO(ISalesforceFormDAO salesforceFormDAO) {
		this._salesforceFormDAO = salesforceFormDAO;
	}

	protected ISalesforceFormDAO getSalesforceFormDAO() {
		return _salesforceFormDAO;
	}

	public ISalesforceMessageDAO getMessageDAO() {
		return _messageDAO;
	}

	public void setMessageDAO(ISalesforceMessageDAO messageDAO) {
		this._messageDAO = messageDAO;
	}
	

	private ISalesforceManager _salesforceManager;
	private IKeyGeneratorManager _keyGeneratorManager;
	private ISalesforceFormDAO _salesforceFormDAO;
	private ISalesforceMessageDAO _messageDAO;
}
