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
package com.agiletec.plugins.jppentaho.aps.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jppentaho.aps.system.JpPentahoSystemConstants;
import com.agiletec.plugins.jppentaho.aps.system.services.report.IPentahoDynamicReportManager;

/**
 * Erogazione delle immagini dei report quando il server pentaho non è raggiungibile direttamente dai client.
 * */
public class ReportStaticResourcesPentaho  extends HttpServlet {

	private static final Logger _logger =  LoggerFactory.getLogger(ReportStaticResourcesPentaho.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		_logger.debug("Request: {}", request.getServletPath());
		IPentahoDynamicReportManager pentahoReportManager =
			(IPentahoDynamicReportManager) ApsWebApplicationUtils.getBean(JpPentahoSystemConstants.PENTAHO_DYNAMIC_REPORT_MANAGER, request);

		String image = request.getParameter("image");
		_logger.info("Richiesta per risorsa statica image {}", image);
		this.getImage(pentahoReportManager, image, response);
	}

	private void getImage(IPentahoDynamicReportManager pentahoReportManager, String image, HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			pentahoReportManager.getReportImage(image, out);
		} catch (Throwable t) {
			throw new ServletException("Errore in erogazione risorsa protetta", t);
		} finally {
			out.close();
		}
	}

}
