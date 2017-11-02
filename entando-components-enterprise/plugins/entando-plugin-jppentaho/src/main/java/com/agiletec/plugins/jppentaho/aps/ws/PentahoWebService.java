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
package com.agiletec.plugins.jppentaho.aps.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PentahoWebService implements IPentahoWebService {

	private static final Logger _logger =  LoggerFactory.getLogger(PentahoWebService.class);
	
	@Override
	public String getList(String solution, String path, String server, String contextPath, 
			String username, String password, String target) throws ServiceException, MalformedURLException, RemoteException {
		return this.invoke(solution, path, server, contextPath, username, password, target, "getList");
	}
	
	@Override
	public String getSolutionDirectoryList(String solution, String path, String server, String contextPath, 
			String username, String password, String target) throws ServiceException, MalformedURLException, RemoteException {
		return this.invoke(solution, path, server, contextPath, username, password, target, "getSolutionDirectoryList");
	}
	
	private String invoke(String solution, String path, String server, String contextPath, String username, String password, String target, String method) throws ServiceException, MalformedURLException, RemoteException {
		String message = "";
		//		try {
		// Creo il canale per effettuare la richiesta
		Call call;
		try {
			call = (Call) new Service().createCall();
			//Conosciamo l'url dove il servizio risponde
			//*
			String pentahoContextPath = (null != contextPath) ? contextPath : "pentaho";
			call.setTargetEndpointAddress(new URL(server+"/"+ pentahoContextPath + "/content/ws-run/japs/" + method));
			//Definiamo il metodo da chiamare
			call.setOperationName(new javax.xml.namespace.QName("http://webservices.japs.plugin.pentaho.bnova.it", "getList"));
			//*/
			/*
			call.setTargetEndpointAddress(new URL(server+"/pentaho/content/ws-run/entando/" + method));
			//Definiamo il metodo da chiamare
			call.setOperationName(new javax.xml.namespace.QName("http://ws.biserver.jppentaho.plugins.agiletec.com", method));
			//*/

			//Chiamiamo il metodo sul web service, passando un parametro
			call.setUsername(username);
			call.setPassword(password);

			Object rispostaWS;
			rispostaWS = call.invoke(new Object[] { solution, path, target });
			message = (String) rispostaWS;

		} catch (ServiceException t) {
			_logger.error("Error during invocation of method", t);
			throw t;
		} catch (MalformedURLException t) {
			_logger.error("Error during invocation of method", t);
			throw t;
		} catch (RemoteException t) {
			_logger.error("Error during invocation of method", t);
			throw t;
		}
		return message;
	}

	/*
	@Override
	public String convert(String xml) throws TransformerException, ApsSystemException {
		ApsSystemUtils.getLogger().info(" # - - # " + xml);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		StreamSource theXSL = new StreamSource(new ByteArrayInputStream(XSL));
		StreamSource theXML = new StreamSource(new ByteArrayInputStream(xml.getBytes()));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		Result res = new StreamResult(byteStream);
		//StringOutputStream out = new StringOutputStream();

		// Use the TransformerFactory to instantiate a transformer that will work with  
		// the style sheet you specify. This method call also processes the style sheet
		// into a compiled Templates object.
		Transformer transformer = tFactory.newTransformer(theXSL);

		// Use the transformer to apply the associated templates object to an XML document
		// and write the output to a file with the same name as the XSL template file that 
		// was passed in sXSLFile.
		transformer.transform(theXML, res);
		String ris = byteStream.toString();

		//Aggiustamenti per rendere l'html standard www.w3.org
		ris = ris.replace("<br>", "<br/>");
		ris = ris.replace("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">","");
		ris = ris.replace("&","&amp;");
		ris = ris.replace("amp;amp;","amp;");
		ris = ris.replace("<a","</img><a");
		return ris;
	}

	private static final byte[] XSL = ("<?xml version=\"1.0\"?>" + 
			"<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">" + 
			"	<xsl:output method=\"html\"></xsl:output>" + 
			"	<xsl:template match=\"/\">" + 
			"			<xsl:apply-templates />" + 
			"	</xsl:template>" + 
			"	<xsl:template match=\"tree\">" + 
			"			<div>" + 
			"				<xsl:apply-templates />" + 
			"			</div>" + 
			"	</xsl:template>" + 
			"	<xsl:template match=\"branch\">" + 
			"	</xsl:template>" + 
			"	<xsl:template match=\"leaf\">" + 
			"		<img src=\"../resources/plugins/jppentaho/images/doc.gif\" >" + 
			"		</img>" + 
			"			<a>" + 
			"		    <xsl:attribute name=\"href\">" + 
			"		     <xsl:value-of select=\"link\" />" + 
			"			</xsl:attribute>" + 
			"			<xsl:attribute name=\"target\">" + 
			"				<xsl:value-of select=\"target\" />" + 
			"			</xsl:attribute>" + 
			"			<xsl:value-of select=\"leafText\" />" + 
			"		" + 
			"		</a>" + 
			"		<br/>" + 
			"	</xsl:template>" + 
			"	<xsl:template match=\"branchText\" />" + 
			"</xsl:stylesheet>").getBytes();
	 */
}