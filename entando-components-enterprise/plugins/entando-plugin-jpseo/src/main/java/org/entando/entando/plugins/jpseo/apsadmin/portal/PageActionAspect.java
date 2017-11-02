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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.system.services.page.ISeoPage;
import org.entando.entando.plugins.jpseo.aps.system.services.page.SeoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.PageAction;
import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;


@Aspect
public class PageActionAspect {

	private static final Logger _logger =  LoggerFactory.getLogger(PageActionAspect.class);

	@Before("execution(* com.agiletec.plugins.jacms.apsadmin.portal.PageAction.validate())")
	public void executeExtraValidation(JoinPoint joinPoint) {
		PageAction action = (PageAction) joinPoint.getTarget();
		this.updateDescriptions(action);
		this.checkFriendlyCode(action);
		this.checkXmlMapping(action);
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setAttribute(PARAM_USE_EXTRA_DESCRIPTIONS, request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS));
	}

	@Before("execution(* com.agiletec.apsadmin.portal.PageAction.joinExtraGroup())")
	public void executeExtraJoinExtraGroup(JoinPoint joinPoint) {
		PageAction action = (PageAction) joinPoint.getTarget();
		this.updateFields(action);
	}

	@Before("execution(* com.agiletec.apsadmin.portal.PageAction.removeExtraGroup())")
	public void executeExtraRemoveExtraGroup(JoinPoint joinPoint) {
		PageAction action = (PageAction) joinPoint.getTarget();
		this.updateDescriptions(action);
		this.updateFields(action);
	}

	@After("execution(* com.agiletec.apsadmin.portal.PageAction.edit())")
	public void executeExtraValueFormForEdit(JoinPoint joinPoint) {
		HttpServletRequest request = ServletActionContext.getRequest();
		PageAction action = (PageAction) joinPoint.getTarget();
		String pageCode = action.getSelectedNode();
		IPage page = action.getPage(pageCode);
		if (null != page && page instanceof ISeoPage) {
			request.setAttribute(PARAM_FRIENDLY_CODE, ((ISeoPage)page).getFriendlyCode());
			request.setAttribute(PARAM_USE_EXTRA_DESCRIPTIONS, ((ISeoPage)page).isUseExtraDescriptions());
			request.setAttribute(PARAM_XML_CONFIG, ((ISeoPage)page).getXmlConfig());
			ApsProperties props = ((ISeoPage)page).getDescriptions();
			if (null != props) {
				Iterator<Object> it = props.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					request.setAttribute(PARAM_DESCRIPTION_PREFIX + key, props.get(key));
				}
			}
		}
	}

	private void updateFields(PageAction action) {
		this.updateDescriptions(action);
		this.updateFriendlyCode(action);
		this.updateXmlMapping(action);
		HttpServletRequest request = ServletActionContext.getRequest();
		String param = request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS);
		request.setAttribute(PARAM_USE_EXTRA_DESCRIPTIONS, param);
	}

	protected void updateDescriptions(PageAction action) {
		HttpServletRequest request = ServletActionContext.getRequest();
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = (Lang) langsIter.next();
			String titleKey = PARAM_DESCRIPTION_PREFIX + lang.getCode();
			String title = request.getParameter(titleKey);
			if (null != title) {
				request.setAttribute(titleKey, title);
			}
		}
	}

	protected void updateFriendlyCode(PageAction action) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String code = request.getParameter(PARAM_FRIENDLY_CODE);
		request.setAttribute(PARAM_FRIENDLY_CODE, code);
	}

	protected void updateXmlMapping(PageAction action) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String xmlConfig = request.getParameter(PARAM_XML_CONFIG);
		request.setAttribute(PARAM_XML_CONFIG, xmlConfig);
	}


	private void checkFriendlyCode(PageAction action) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String code = request.getParameter(PARAM_FRIENDLY_CODE);

		if (null != code && code.trim().length() > 100) {
			String[] args = {"100"};
			action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.friendlyCode.stringlength", args));
		}
		if (null != code && code.trim().length() > 0) {
			Pattern pattern = Pattern.compile("([a-z0-9_])+");
			Matcher matcher = pattern.matcher(code);
			if (!matcher.matches()) {
				action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.friendlyCode.wrongCharacters"));
			}
		}

		if (null != code && code.trim().length() > 0) {
			FriendlyCodeVO vo = this.getSeoMappingManager().getReference(code);
			if (null != vo && (vo.getPageCode() == null || !vo.getPageCode().equals(action.getPageCode()))) {
				String[] args = {code};
				action.addFieldError(PARAM_FRIENDLY_CODE, action.getText("jpseo.error.page.duplicateFriendlyCode", args));
			}
		}
		request.setAttribute(PARAM_FRIENDLY_CODE, code);
	}


	private void checkXmlMapping(PageAction action) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String xmlConfig = request.getParameter(PARAM_XML_CONFIG);
		if (null == xmlConfig || xmlConfig.trim().length() == 0) return;
		if (!this.validateXmlConfig(xmlConfig)) {
			action.addFieldError(PARAM_XML_CONFIG, action.getText("jpseo.error.page.invalidXmlConfig"));
		}
		request.setAttribute(PARAM_XML_CONFIG, xmlConfig);
	}


	private boolean validateXmlConfig(String xmlText) {
		SchemaFactory factory =	SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		InputStream schemaIs = null;
		InputStream xmlIs = null;
		boolean valid = false;
		try {
			schemaIs = this.getClass().getResourceAsStream("mapping_seo.xsd");
			Source schemaSource = new StreamSource(schemaIs);
			Schema schema = factory.newSchema(schemaSource);
			Validator validator = schema.newValidator();
			xmlIs = new ByteArrayInputStream(xmlText.getBytes("UTF-8"));
			Source source = new StreamSource(xmlIs);
			validator.validate(source);
			valid = true;
		} catch (Throwable t) {
			valid = false;
		} finally {
			try {
				if (null != schemaIs) schemaIs.close();
				if (null != xmlIs) xmlIs.close();
			} catch (IOException e) {
				_logger.error("error in validateXmlConfig. xml:{}", xmlText, e);
			}
		}
		return valid;
	}


	@Around("execution(* com.agiletec.apsadmin.portal.PageAction.save())")
	public Object executeUodateAfterSave(ProceedingJoinPoint joinPoint) {
		Object result = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			result = joinPoint.proceed();
		} catch (Throwable t) {
			_logger.error("error saving page for seo", t);
		}
		try {
			//se il salvataggio va a buon fine, aggiorna l'oggetto
			if (null != result && result instanceof String) {
				String resultStr = (String) result;
				if (resultStr.equalsIgnoreCase(Action.SUCCESS)) {
					PageAction action = (PageAction) joinPoint.getTarget();
					SeoPage seoPage = this.buildPageForUpdate(request, action);
					if (null != seoPage) {
						this.getPageManager().updatePage(seoPage);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error updating page for seo", t);
			return BaseAction.FAILURE;
		}
		return result;
	}

	protected SeoPage buildPageForUpdate(HttpServletRequest request, PageAction action) {
		SeoPage seoPage = null;
		String pagecode = action.getPageCode();
		ApsProperties descriptions = new ApsProperties();
		String friendlyCode = request.getParameter(PARAM_FRIENDLY_CODE);
		String xmlConfig = request.getParameter(PARAM_XML_CONFIG);
		Iterator<Lang> langsIter = this.getLangManager().getLangs().iterator();
		while (langsIter.hasNext()) {
			Lang lang = (Lang) langsIter.next();
			String titleKey = PARAM_DESCRIPTION_PREFIX + lang.getCode();
			String title = request.getParameter(titleKey);
			if (null != title) {
				descriptions.put(lang.getCode(), title.trim());
			}
		}
		IPage page = this.getPageManager().getPage(pagecode);
		if (null != page && page instanceof SeoPage) {
			seoPage = (SeoPage) page;
			seoPage.setFriendlyCode(friendlyCode);
			seoPage.setDescriptions(descriptions);
			seoPage.setXmlConfig(xmlConfig);
			seoPage.setUseExtraDescriptions(null != request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS) && request.getParameter(PARAM_USE_EXTRA_DESCRIPTIONS).equalsIgnoreCase("true"));
		}
		return seoPage;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	protected ISeoMappingManager getSeoMappingManager() {
		return _seoMappingManager;
	}
	public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
		this._seoMappingManager = seoMappingManager;
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	private ILangManager _langManager;
	private ISeoMappingManager _seoMappingManager;
	private IPageManager _pageManager;

	public static final String PARAM_FRIENDLY_CODE = "friendlyCode";
	public static final String PARAM_XML_CONFIG = "xmlConfig";
	public static final String PARAM_DESCRIPTION_PREFIX = "description_lang";
	public static final String PARAM_USE_EXTRA_DESCRIPTIONS = "useExtraDescriptions";
}