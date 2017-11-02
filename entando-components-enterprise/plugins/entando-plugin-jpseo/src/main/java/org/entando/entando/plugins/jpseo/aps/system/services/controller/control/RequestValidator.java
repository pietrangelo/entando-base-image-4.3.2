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
package org.entando.entando.plugins.jpseo.aps.system.services.controller.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.entando.entando.plugins.jpseo.aps.system.JpseoSystemConstants;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.FriendlyCodeVO;
import org.entando.entando.plugins.jpseo.aps.system.services.mapping.ISeoMappingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;

/**
 * @author E.Santoboni
 */
public class RequestValidator extends com.agiletec.aps.system.services.controller.control.RequestValidator {

	private static final Logger _logger =  LoggerFactory.getLogger(RequestValidator.class);
	
	@Override
	public int service(RequestContext reqCtx, int status) {
		_logger.trace("{} ready", this.getClass().getName());
		int retStatus = ControllerManager.INVALID_STATUS;
		// Se si è verificato un errore in un altro sottoservizio, termina subito
		if (status == ControllerManager.ERROR) {
			return status;
		}
		try { // non devono essere rilanciate eccezioni
			boolean ok = this.isRightPath(reqCtx);
			if (ok) {
				if (null == reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE)) {
					retStatus = this.redirect(this.getNotFoundPageCode(), reqCtx);
				} else if (null == reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG)) {
					retStatus = this.redirect(this.getErrorPageCode(), reqCtx);
				} else {
					retStatus = ControllerManager.CONTINUE;
				}
			} else {
				retStatus = this.redirect(this.getErrorPageCode(), reqCtx);
			}
		} catch (Throwable t) {
			retStatus = ControllerManager.SYS_ERROR;
			reqCtx.setHTTPError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			_logger.error("Error while validating the client request", t);
		}
		return retStatus;
	}

	private boolean isRightPath(RequestContext reqCtx) {
		boolean ok = false;
		String resourcePath;
		Matcher matcher;
		Lang lang = null;
		IPage page = null;
		if (this.getResourcePath(reqCtx).equals("/page")) {
			resourcePath = this.getFullResourcePath(reqCtx);
			matcher = this._patternSeoPath.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				lang = getLangManager().getLang(sect1);
				String friendlyCode = matcher.group(2).substring(1);
				FriendlyCodeVO vo = this.getSeoMappingManager().getReference(friendlyCode);
				if (null != vo) {
					if (null != vo.getPageCode()) {
						page = this.getPageManager().getPage(vo.getPageCode());
					} else if (null != vo.getContentId() && null != lang && lang.getCode().equals(vo.getLangCode())) {
						String contentId = vo.getContentId();
						if (lang.getCode().equals(vo.getLangCode())) {
							String viewPageCode = this.getContentManager().getViewPage(contentId);
							page = this.getPageManager().getPage(viewPageCode);
							reqCtx.addExtraParam(JpseoSystemConstants.EXTRAPAR_HIDDEN_CONTENT_ID, contentId);
						}
					}
				}
			}
		} else if (this.getResourcePath(reqCtx).equals("/pages")) {
			resourcePath = getFullResourcePath(reqCtx);
			matcher = this._patternFullPath.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				lang = getLangManager().getLang(sect1);
				page = this.getPage(matcher);
			}
		} else {
			resourcePath = getResourcePath(reqCtx);
			matcher = this._pattern.matcher(resourcePath);
			if (matcher.lookingAt()) {
				ok = true;
				String sect1 = matcher.group(1);
				String sect2 = matcher.group(2);
				lang = getLangManager().getLang(sect1);
				page = this.getPageManager().getPage(sect2);
			} else {
				//to preserve url with ".wp" suffix
				matcher = this._oldPattern.matcher(resourcePath);
				if (matcher.lookingAt()) {
					ok = true;
					String sect1 = matcher.group(1);
					String sect2 = matcher.group(2);
					lang = getLangManager().getLang(sect1);
					page = this.getPageManager().getPage(sect2);
				}
			}
		}
		if (!ok) {
			return false;
		}
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
		reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);
		return true;
	}
	
	/**
	 * Qualora si usasse il mapping /pages/*
	 * restituisce un'oggetto IPage solo nel caso
	 * in cui il path completo della pagina risulti corretto.
	 * Qualora il path sia di lunghezza pari a zero
	 * verrà restituita l'homepage.
	 * @param Matcher il matcher valorizzato come segue<br>
	 * matcher.group(1) -> lang_code<br>
	 * matcher.group(2) -> /paginaX/paginaY<br>
	 * matcher.group(3) -> /paginaY<br>
	 * @return un oggetto Page oppure null
	 */
	private IPage getPage(Matcher matcher) {
		IPage page = null;
		String rootCode = this.getPageManager().getRoot().getCode();
		String path = matcher.group(2);
		//Se il path è di tipo /it o /it/ o /it/homepage
		if (path.trim().length() == 0 || path.substring(1).equals(rootCode)) {
			return this.getPageManager().getRoot();
		}
		String pageCode = matcher.group(3).substring(1);
		IPage tempPage = this.getPageManager().getPage(pageCode);
		if (null != tempPage) {
			//la pagina esiste ed è di livello 1
			//if(tempPage.getParentCode().equals(rootCode)) return tempPage;
			//la pagina è di livello superiore al primo e il path è corretto
			String fullPath = matcher.group(2).substring(1).trim();
			String createdlFullPath = PageUtils.getFullPath(tempPage, "/").toString();
			if (createdlFullPath.equals(fullPath) ){
				page = tempPage;
			}
		}
		return page;
	}
	
	protected ISeoMappingManager getSeoMappingManager() {
		return _seoMappingManager;
	}
	public void setSeoMappingManager(ISeoMappingManager seoMappingManager) {
		this._seoMappingManager = seoMappingManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected Pattern _patternSeoPath = Pattern.compile("^/page/(\\w+)((/\\w+)*)");
	
	private ISeoMappingManager _seoMappingManager;
	private IContentManager _contentManager;
	
}