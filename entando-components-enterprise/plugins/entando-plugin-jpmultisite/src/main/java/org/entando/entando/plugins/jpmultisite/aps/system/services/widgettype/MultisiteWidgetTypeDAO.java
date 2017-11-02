/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/

package org.entando.entando.plugins.jpmultisite.aps.system.services.widgettype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru
 */
public class MultisiteWidgetTypeDAO extends WidgetTypeDAO {
	
	private static final Logger _logger = LoggerFactory.getLogger(MultisiteWidgetTypeDAO.class);

	@Override
	public void deleteWidgetTypeLocked(String widgetTypeCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_WIDGET_TYPE_NO_CHECK_FOR_LOCKED);
			stat.setString(1, widgetTypeCode);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error deleting showlet type '{}'", widgetTypeCode, t);
			throw new RuntimeException("Error deleting showlet type", t);
			//processDaoException(t, "Error deleting showlet type", "deleteShowletType");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private final String DELETE_WIDGET_TYPE_NO_CHECK_FOR_LOCKED = "DELETE FROM widgetcatalog WHERE code = ?";

}
