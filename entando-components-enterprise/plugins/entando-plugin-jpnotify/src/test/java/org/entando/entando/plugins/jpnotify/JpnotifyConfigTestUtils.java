/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpnotify;

import org.apache.commons.lang.ArrayUtils;

import com.agiletec.ConfigTestUtils;

public class JpnotifyConfigTestUtils extends ConfigTestUtils {

	@Override
	protected String[] getSpringConfigFilePaths() {
		String[] baseFiles = super.getSpringConfigFilePaths();
		
		//TODO EDIT THIS
		String[] filePaths = new String[0];
		String[] newFiles = (String[]) ArrayUtils.addAll(baseFiles, filePaths);
		return newFiles;
    }
}


