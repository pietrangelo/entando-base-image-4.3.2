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
package org.entando.entando.aps.system.services.widgettype;

import java.util.Map;

import com.agiletec.aps.util.ApsProperties;

/**
 * Interfaccia base per Data Access Object dei tipi di showlet (WidgetType).
 * @author E.Santoboni
 */
public interface IWidgetTypeDAO {
	
	/**
	 * Return the map of the widget types
	 * @return The map of the widget types
	 * @deprecated Use {@link #loadWidgetTypes()} instead
	 */
	public Map<String, WidgetType> loadShowletTypes();

	/**
	 * Return the map of the widget types
	 * @return The map of the widget types
	 */
	public Map<String, WidgetType> loadWidgetTypes();
	
	@Deprecated
	public void addShowletType(WidgetType showletType);
	
	public void addWidgetType(WidgetType widgetType);
	
	/**
	 * Delete a widget type.
	 * @param showletTypeCode The code of widget type to delete
	 * @deprecated Use {@link #deleteWidgetType(String)} instead
	 */
	public void deleteShowletType(String showletTypeCode);
	
	/**
	 * Delete a widget type.
	 * @param widgetTypeCode The code of widget type to delete
	 */
	public void deleteWidgetType(String widgetTypeCode);
	
	@Deprecated
	public void updateShowletTypeTitles(String showletTypeCode, ApsProperties titles);
	
	@Deprecated
	public void updateShowletType(String showletTypeCode, ApsProperties titles, ApsProperties defaultConfig);
	
	/**
	 * @deprecated Use {@link #updateWidgetType(String,ApsProperties,ApsProperties,String)} instead
	 */
	public void updateShowletType(String showletTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup);

	public void updateWidgetType(String showletTypeCode, ApsProperties titles, ApsProperties defaultConfig, String mainGroup);
	
	/**
	 * This method allow you to delete widget type locked also. It's built only for multisite deletion purpose.
	 * @param widgetTypeCode 
	 */
	public void deleteWidgetTypeLocked(String widgetTypeCode);
	
}