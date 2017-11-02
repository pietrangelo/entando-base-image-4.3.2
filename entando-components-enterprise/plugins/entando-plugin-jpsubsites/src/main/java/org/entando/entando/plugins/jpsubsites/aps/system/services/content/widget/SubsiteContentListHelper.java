/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpsubsites.aps.system.services.content.widget;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListTagBean;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;

import java.util.Collection;
import java.util.List;

import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.plugins.jpsubsites.aps.system.JpsubsitesSystemConstants;
import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author E.Santoboni
 */
public class SubsiteContentListHelper extends ContentListHelper {
	
	private static final Logger _logger = LoggerFactory.getLogger(SubsiteContentListHelper.class);
	
	@Override
	@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, 
			key = "T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx)", 
			condition = "#bean.cacheable && !T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).isUserFilterExecuted(#bean)")
	@CacheEvict(value = ICacheInfoManager.DEFAULT_CACHE_NAME, 
			key = "T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx)", 
			beforeInvocation = true, 
			condition = "T(org.entando.entando.aps.system.services.cache.CacheInfoManager).isExpired(T(com.agiletec.plugins.jacms.aps.system.services.content.widget.ContentListHelper).buildCacheKey(#bean, #reqCtx))")
	@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentListCacheGroupsCsv(#bean, #reqCtx)", expiresInMinute = 30)
	public List<String> getContentsId(IContentListTagBean bean, RequestContext reqCtx) throws Throwable {
		List<String> contentsId = null;
		try {
			contentsId = this.extractContentsId(bean, reqCtx);
			contentsId = this.executeFullTextSearch(bean, contentsId, reqCtx);
		} catch (Throwable t) {
			_logger.error("Error extracting contents id", t);
			throw new ApsSystemException("Error extracting contents id", t);
		}
		return contentsId;
	}
	
	@Override
	protected List<String> extractContentsId(IContentListTagBean bean, RequestContext reqCtx) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			List<UserFilterOptionBean> userFilters = bean.getUserFilterOptions();
			Widget widget = (Widget) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET);
			ApsProperties config = (null != widget) ? widget.getConfig() : null;
			if (null == bean.getContentType() && null != config) {
				bean.setContentType(config.getProperty(WIDGET_PARAM_CONTENT_TYPE));
			}
			if (null == bean.getContentType()) {
				throw new ApsSystemException("Tipo contenuto non definito");
			}
			if (null == bean.getCategory() && null != config && null != config.getProperty(SHOWLET_PARAM_CATEGORY)) {
				bean.setCategory(config.getProperty(SHOWLET_PARAM_CATEGORY));
			}
			this.addWidgetFilters(bean, config, WIDGET_PARAM_FILTERS, reqCtx);
			if (null != userFilters && userFilters.size() > 0) {
				for (int i = 0; i < userFilters.size(); i++) {
					UserFilterOptionBean userFilter = userFilters.get(i);
					EntitySearchFilter filter = userFilter.getEntityFilter();
					if (null != filter) {
						bean.addFilter(filter);
					}
				}
			}
			String[] categories = this.getCategories(bean.getCategories(), config, userFilters, reqCtx);
			Collection<String> userGroupCodes = this.getAllowedGroups(reqCtx);
			boolean orCategoryFilterClause = this.extractOrCategoryFilterClause(config);
			contentsId = this.getContentManager().loadPublicContentsId(bean.getContentType(), 
                                categories, orCategoryFilterClause, bean.getFilters(), userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error extracting contents id", t);
			throw new ApsSystemException("Error extracting contents id", t);
		}
		return contentsId;
	}
	
	protected String[] getCategories(String[] categories, ApsProperties config, List<UserFilterOptionBean> userFilters, RequestContext reqCtx) {
		String[] categoryCodes = super.getCategories(categories, config, userFilters);
		Subsite subsite = (Subsite) reqCtx.getRequest().getAttribute(JpsubsitesSystemConstants.REQUEST_PARAM_CURRENT_SUBSITE);
		if (null != subsite && null != subsite.getCategoryCode()) {
			categoryCodes = this.addCategory(categoryCodes, subsite.getCategoryCode());
		}
		return categoryCodes;
	}
	
	private String[] addCategory(String[] categoryCodes, String categoryToAdd) {
		int len = (null != categoryCodes) ? categoryCodes.length : 0;
		String[] newCodes = new String[len + 1];
		if (null != categoryCodes) {
			for (int i=0; i < len; i++) {
				newCodes[i] = categoryCodes[i];
			}
		}
		newCodes[len] = categoryToAdd;
		return newCodes;
	}
	
}
