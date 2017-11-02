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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.entando.entando.plugins.jpsalesforce.aps.system.services.salesforce.model.Campaign;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CampaignDAO implements ICampaignDAO {

	private static final Logger _logger =  LoggerFactory.getLogger(CampaignDAO.class);
	
	/**
	 * Get the list of the Accounts. The list returned is empty if no leads are found
	 * @param sad
	 * @return
	 * @throws Throwable
	 */
	@Override
	public List<Campaign> getCampaigns(SalesforceRestResources sarr, SalesforceAccessDescriptor sad) throws Throwable {
		List<Campaign> list = new ArrayList<Campaign>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String queryUrl = sad.getInstaceUrl().concat(sarr.getQuery());
		URIBuilder builder = new URIBuilder(queryUrl);
		builder.addParameter("q", GET_CAMPAINS);
		HttpGet httpGet = new HttpGet(builder.build());
		httpGet.setHeader("Authorization", "OAuth " + sad.getAccessToken());

		try {
			_logger.debug("Querying '{}' for Campaigns", httpGet.getURI().toString());
			HttpResponse response = httpclient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				_logger.info("status OK");

//				InputStream ris = response.getEntity().getContent();
//				JSONTokener jtok = new JSONTokener(new InputStreamReader(ris));
//				JSONObject jobj = new JSONObject(jtok);
				String body = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				_logger.debug("Parsing (list) response: '{}'", body);
				
				JSONObject jobj = new JSONObject(body);
				JSONArray jrec = jobj.getJSONArray("records");

				for (int i = 0; i < jrec.length(); i++) {
					JSONObject jcur = jrec.getJSONObject(i);
					Campaign campaign = new Campaign(jcur);
					list.add(campaign);
				}
			} else {
				_logger.info("server reported error status '{}'", response.getStatusLine().getStatusCode());
				throw new RuntimeException("HTTP status error while querying accounts");
			}
		} catch (Throwable t) {
			_logger.error("Error querying accounts", t);
			throw new RuntimeException("Error querying accounts");
		} finally {
			httpGet.releaseConnection();
		}
		return list;
	}
	
	private static final String GET_CAMPAINS = "SELECT Name, Id from Campaign";
	
}
