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
package org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.parse;

import org.entando.entando.plugins.jpsalesforce.aps.system.services.subscription.config.SubscriptionConfig;
import org.jdom.Document;
import org.jdom.Element;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.parse.UserRegConfigDOM;

/**
 <subscriptionConfig>
	<userProfile typeCode="ABC" redirectPageCode="xyz" />
	<activation>
		<template lang="it">
			<subject>[entando.com] : Attivazione utente</subject>
			<body><![CDATA[Gentile {name} {surname}, 
grazie per esserti registrato.
Per attivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.]]></body>
		</template>
		<template lang="en">
			<subject>[entando.com] : Attivazione utente</subject>
			<body><![CDATA[Gentile {name} {surname}, 
grazie per esserti registrato.
Per attivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.]]>
			</body>
		</template>
	</activation>
 </subscriptionConfig>
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 */
public class SubscriptionConfigDOM extends UserRegConfigDOM {
	
	public SubscriptionConfig extractSubscriptionConfig(String xml) throws ApsSystemException {
		Document doc = this.decodeDOM(xml);
		Element root = doc.getRootElement();
		SubscriptionConfig config = new SubscriptionConfig();
		Element userProfileElement = root.getChild("userProfile");
		if (null != userProfileElement) {
			String profileTypeCode = userProfileElement.getAttributeValue("typeCode");
			String redirectPageCode = userProfileElement.getAttributeValue("redirectPageCode");
			config.setRedirectPageCode(redirectPageCode);
			config.setUserprofileTypeCode(profileTypeCode);
		}
		//this.extractTokenValidityConfig(root, config);
		//this.extractMailSenderConfig(root, config);
		this.extractActivationMailConfig(root, config);
		//this.extractReactivationMailConfig(root, config);
		//this.extractUserAuthDefaults(root, config);
		return config;
	}
	
}
