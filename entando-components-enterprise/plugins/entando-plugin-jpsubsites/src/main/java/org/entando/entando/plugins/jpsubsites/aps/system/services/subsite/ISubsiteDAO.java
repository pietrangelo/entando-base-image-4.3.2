/*
*
* Copyright 2017 Entando Inc. (http://www.entando.com) All rights reserved.
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
* Copyright 2015 Entando Inc. (http://www.entando.com) All rights reserved.
*
 */
package org.entando.entando.plugins.jpsubsites.aps.system.services.subsite;

import java.util.Map;

import org.entando.entando.plugins.jpsubsites.aps.system.services.subsite.model.Subsite;

/**
 * Interface of DAO for CRUD operations on subsites.
 *
 * @author E.Mezzano
 */
public interface ISubsiteDAO {

	/**
	 * Load the map of subsites
	 *
	 * @return The map of subsites
	 */
	public Map<String, Subsite> loadSubsites();

	/**
	 * Load the subsite associated to the content of given id. The relationship
	 * is obtained through the associated categories.
	 *
	 * @param contentId Thje id of the content.
	 * @return The subsite associated to the content of given id.
	 */
	public String loadSubsiteCodeForContent(String contentId);

	/**
	 * Load the subsite of given code.
	 *
	 * @param code The code of the subsite.
	 * @return The Desired subsite.
	 */
	public Subsite loadSubsite(String code);

	/*
	 * Returns the code of the subsite associated to the given category.
	 * @param categoryCode The code of the category.
	 * @return The code of the subsite associated to the given category.
	 */
	//public String getSubsiteForCategory(String categoryCode);
	/**
	 * Returns the code of the subsite associated to the given page.
	 *
	 * @param pageCode The code of the page.
	 * @return The code of the subsite associated to the given page.
	 */
	public String getSubsiteForPage(String pageCode);

	/**
	 * Add the given subsite.
	 *
	 * @param subsite The new subsite.
	 */
	public void addSubsite(Subsite subsite);

	/**
	 * Update the given subsite.
	 *
	 * @param subsite The subsite to update.
	 */
	public void updateSubsite(Subsite subsite);

	/**
	 * Remove the subsite of given code.
	 *
	 * @param code The code of the subsite to remove.
	 */
	public void deleteSubsite(String code);

}
