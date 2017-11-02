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
package org.entando.entando.plugins.jpseo.aps.util;

public class FriendlyCodeGenerator {
	
	public static String generateFriendlyCode(String text) {
		String friendlyCode = null;
		if (text!=null) {
			text = text.trim().replaceAll("(\\s)+", "_");
			text = text.replace("à", "a").replace("è", "e").replace("é", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u");
			text = text.toLowerCase().replaceAll("([^a-z0-9_])+", "");
			if (text.length()>0) {
				friendlyCode = text;
			}
		}
		return friendlyCode;
	}
	
}