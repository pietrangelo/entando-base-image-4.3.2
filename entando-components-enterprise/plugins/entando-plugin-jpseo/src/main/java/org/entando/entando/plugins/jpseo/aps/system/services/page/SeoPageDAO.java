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
package org.entando.entando.plugins.jpseo.aps.system.services.page;

import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageDAO;
import com.agiletec.aps.system.services.page.PageExtraConfigDOM;
import com.agiletec.aps.util.ApsProperties;

/**
 * Data Access Object for the 'page' objects
 * @author E.Santoboni
 */
public class SeoPageDAO extends PageDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(SeoPageDAO.class);

	@Override
	protected PageExtraConfigDOM getExtraConfigDOM() {
		return new SeoPageExtraConfigDOM();
	}

	@Override
	protected Page createPage(String code, ResultSet res) throws Throwable {
		SeoPage page = new SeoPage();
		page.setCode(code);
		page.setParentCode(res.getString(1));
		page.setPosition(res.getInt(2));
		Integer showable = new Integer (res.getInt(4));
		page.setShowable(showable.intValue() == 1);
		page.setModel(this.getPageModelManager().getPageModel(res.getString(5)));
		String titleText = res.getString(6);
		ApsProperties titles = new ApsProperties();
		try {
			titles.loadFromXml(titleText);
		} catch (Throwable t) {
			_logger.error("IO error detected while parsing the titles of the page {}", page.getCode(), t);
			String msg = "IO error detected while parsing the titles of the page '" + page.getCode()+"'";
			throw new ApsSystemException(msg, t);
		}
		page.setTitles(titles);
		page.setGroup(res.getString(7));
		String extraConfig = res.getString(8);
		if (null != extraConfig && extraConfig.trim().length() > 0) {
			try {
				SeoPageExtraConfigDOM configDom = new SeoPageExtraConfigDOM();
				configDom.addExtraConfig(page, extraConfig);
			} catch (Throwable t) {
				_logger.error("IO error detected while parsing the extra config of the page {}", page.getCode(), t);
				String msg = "IO error detected while parsing the extra config of the page '" + page.getCode()+"'";
				throw new ApsSystemException(msg, t);
			}
		}
		return page;
	}

}