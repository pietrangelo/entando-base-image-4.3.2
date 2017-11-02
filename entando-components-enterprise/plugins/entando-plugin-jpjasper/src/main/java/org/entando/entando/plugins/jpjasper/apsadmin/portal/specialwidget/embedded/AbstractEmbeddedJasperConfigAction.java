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
package org.entando.entando.plugins.jpjasper.apsadmin.portal.specialwidget.embedded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.entando.entando.plugins.jpjasper.aps.services.jasperserver.IJasperServerManager;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptor;
import org.entando.entando.plugins.jpjasper.aps.services.model.JasperResourceDescriptorProperty;
import org.entando.entando.plugins.jpjasper.aps.services.model.WsTypeParams;
import org.entando.entando.plugins.jpjasper.apsadmin.utils.JpJasperApsadminUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

public abstract class AbstractEmbeddedJasperConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractEmbeddedJasperConfigAction.class);
	
	@Override
	public void validate() {
		super.validate();
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			super.extractInitConfig();
		}
	}
	
	@Override
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		this.setJasperTheme(this.getWidget().getConfig().getProperty(SHOWLET_PARAM_THEME));
		String h = this.getWidget().getConfig().getProperty(SHOWLET_PARAM_FRAME_HEIGHT);
		if (StringUtils.isNotBlank(h)) {
			this.setFrameHeight(new Integer(h));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<JasperResourceDescriptor> getFolders() {
		List<JasperResourceDescriptor> parsedResourceDescriptors = null;
		try {
			String jasperFolderName = "themes";
			String cookieHeader = JpJasperApsadminUtils.getCookieHeader(this.getRequest(), this.getJasperServerManager());
			List<JasperResourceDescriptor> resourceDescriptors = this.getJasperServerManager().searchRestResources(cookieHeader, null, WsTypeParams.FOLDER, jasperFolderName, false);

			parsedResourceDescriptors = new ArrayList<JasperResourceDescriptor>();
			if (null != resourceDescriptors && !resourceDescriptors.isEmpty()) {
				for (int i = 0; i < resourceDescriptors.size(); i++) {
					JasperResourceDescriptor res = resourceDescriptors.get(i);
					JasperResourceDescriptorProperty parentFolder = res.getProperty("PROP_PARENT_FOLDER");
					if (parentFolder.getValue().equalsIgnoreCase("/" + jasperFolderName)) {
						parsedResourceDescriptors.add(res);
					}
				}
			}
			
			if (null != parsedResourceDescriptors && !parsedResourceDescriptors.isEmpty()) {
				Collections.sort(parsedResourceDescriptors, new BeanComparator("uriString"));
			}

		} catch (Throwable t) {
			_logger.error("error in getFolders", t);
			throw new RuntimeException("error in getFolders", t);
		}
		return parsedResourceDescriptors;
	}
	

	protected IJasperServerManager getJasperServerManager() {
		return _jasperServerManager;
	}
	public void setJasperServerManager(IJasperServerManager jasperServerManager) {
		this._jasperServerManager = jasperServerManager;
	}
	
	public abstract String getServerEndpoint();
	
	public String getJasperTheme() {
		return _jasperTheme;
	}
	public void setJasperTheme(String jasperTheme) {
		this._jasperTheme = jasperTheme;
	}
	public Integer getFrameHeight() {
		return _frameHeight;
	}
	public void setFrameHeight(Integer frameHeight) {
		this._frameHeight = frameHeight;
	}

	private String _jasperTheme;
	private Integer _frameHeight;
	
	
	private IJasperServerManager _jasperServerManager;

	public static final String SHOWLET_PARAM_THEME = "jasperTheme";
	public static final String SHOWLET_PARAM_ENDPOINT = "serverEndpoint";
	public static final String SHOWLET_PARAM_FRAME_HEIGHT = "frameHeight";
	
}
