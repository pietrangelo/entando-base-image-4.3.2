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
package org.entando.entando.plugins.jpseo.apsadmin.content.helper;

import java.util.Iterator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.entando.entando.plugins.jpseo.aps.util.FriendlyCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.opensymphony.xwork2.ActionSupport;

@Aspect
public class ContentActionHelperAspect {

	private static final Logger _logger =  LoggerFactory.getLogger(ContentActionHelperAspect.class);

	@Before("execution(* com.agiletec.plugins.jacms.apsadmin.content.helper.ContentActionHelper.scanEntity(..))")
	public void executeExtraScanEntity(JoinPoint joinPoint) {
		try {
			this.checkFriendlyCodes((IApsEntity)joinPoint.getArgs()[0], (ActionSupport)joinPoint.getArgs()[1]);
		} catch (Throwable t) {
			_logger.error("error in executeExtraScanEntity", t);
			throw new RuntimeException("Error executing Extra Scan Entity", t);
		}
	}

	/*
	La validazione comporta il controllo sull'univocità del codice
	Se il codice risulta già in uso viene restituito un errore.
	Può capitare di avere il contenuto X pubblicato con friendly code fc_1 e di modificarlo in fc_2, senza ri-pubblicare il contenuto;
	in questo caso, sul DB, il codice resterebbe fc_1.
	Qualora nel mentre si pubblicasse il contenuto Y con friendly code fc_2, all'atto di pubblicare X si avrebbe un errore.
	 */
	protected void checkFriendlyCodes(IApsEntity entity, ActionSupport action) {
		if (null == entity) {
			_logger.error("Invocazione di scansione/salvataggio contenuto nullo");
			return;
		}
		AttributeInterface attribute = entity.getAttributeByRole(JpseoSystemConstants.ATTRIBUTE_ROLE_FRIENDLY_CODE);
		if (null != attribute && attribute instanceof ITextAttribute) {
			String contentId = entity.getId();
			String msgPrefix = action.getText("EntityAttribute.singleAttribute.errorMessage.prefix", new String[] { attribute.getName() });
			ITextAttribute textAttr = (ITextAttribute) attribute;
			if (attribute.isMultilingual()) {
				String attributeName = attribute.getName();
				Iterator<Lang> langs = this.getLangManager().getLangs().iterator();
				while (langs.hasNext()) {
					Lang currentLang = langs.next();
					this.checkFriendlyCode(textAttr.getTextForLang(currentLang.getCode()), currentLang, attributeName, msgPrefix, contentId, action);
				}
			} else {
				this.checkFriendlyCode(textAttr.getText(), null, attribute.getName(), msgPrefix, contentId, action);
			}
		}
	}

	private void checkFriendlyCode(String text, Lang currentLang, String attributeName, String msgPrefix, String contentId, ActionSupport action) {
		String friendlyCode = FriendlyCodeGenerator.generateFriendlyCode(text);
		if (null != friendlyCode) {
			FriendlyCodeVO fcVO = this.getSeoMappingManager().getReference(friendlyCode);
			if (null != fcVO && (contentId==null || !contentId.equals(fcVO.getContentId()))) {
				String errorMsg = null;
				if (currentLang == null) {
					errorMsg = action.getText("jpseo.error.content.friendlyCode.alreadyInUse", new String[] { friendlyCode });
				} else {
					errorMsg = action.getText("jpseo.error.content.friendlyCodeForLang.alreadyInUse", new String[] { friendlyCode, currentLang.getDescr() });
				}
				String fcUtilizer = null;
				if (fcVO.getPageCode() != null) {
					fcUtilizer = action.getText("jpseo.error.content.friendlyCode.utilizer.page", new String[] { fcVO.getPageCode() });
				} else {
					fcUtilizer = action.getText("jpseo.error.content.friendlyCode.utilizer.content", new String[] { fcVO.getContentId() });
				}
				action.addFieldError(attributeName, msgPrefix + " " + errorMsg + " " + fcUtilizer);
			}
		}
	}

	protected ISeoMappingManager getSeoMappingManager() {
		return _seoMappingManager;
	}
	public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
		this._seoMappingManager = seoMappingManager;
	}

	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	private ISeoMappingManager _seoMappingManager;
	private ILangManager _langManager;
}