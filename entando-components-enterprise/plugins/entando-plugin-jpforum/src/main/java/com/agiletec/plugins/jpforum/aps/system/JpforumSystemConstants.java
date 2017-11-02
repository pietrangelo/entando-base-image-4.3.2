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
package com.agiletec.plugins.jpforum.aps.system;

public interface JpforumSystemConstants {
	
	/**
	 * Servizio gestore delle sezioni
	 */
	public static final String SECTION_MANAGER = "jpforumSectionManager";
	
	/**
	 * Servizio gestore threads e post
	 */
	public static final String THREAD_MANAGER = "jpforumThreadManager";
	
	/**
	 * Servizio gestore delle statistiche
	 */
	public static final String STATISTIC_MANAGER = "jpforumStatisticManager";
	
	/**
	 * Parser bbcode
	 */
	public static final String MARKUP_PARSER = "jpforumMarkupParser";
	
	/**
	 * Servigio gestore dei messaggi privati
	 */
	public static final String MESSAGE_MANAGER = "jpforumMessageManager";
	
	/**
	 * Servizio gestore delle sanzioni a carico degli utenti del forum
	 */
	public static final String SANCTION_MANAGER = "jpforumSanctionManager";
	
	/**
	 * Servizio gestore degli allegati
	 */
	public static final String ATTACH_MANAGER = "jpforumAttachManager";
	
	/**
	 * Motore di ricerca
	 */
	public static final String SEARCH_MANAGER = "jpforumSearchManager";
	
	/**
	 * Gestisce la configuarazione del forum da backoffice
	 */
	public static final String CONFIG_MANAGER = "jpforumConfigManager";
	
	
	public static final String PERMISSION_FORUM_USER = "jpforum_forumUser";

	public static final String PERMISSION_SECTION_MODERATOR = "jpforum_sectionModerator";

	public static final String PERMISSION_SUPERUSER = "jpforum_superuser";

	
	public static final String ROLE_FORUM_USER = "jpforum_roleUser";

	public static final String ROLE_SECTION_MODERATOR = "jpforum_moderator";
	
	public static final String ROLE_SUPERUSER = "jpforum_superuser";
	
	
	public static final String ATTR_PAGER_ITEM_NUMBER = "jpforum_pager_item_number";
	
	public static final String ATTR_SHOW_LAST_BREADCRUMB= "jpforum_show_last_breadcrumb";

	public static final String FORUM_CONFIG_ITEM = "jpforum_config";

	public static final String POSTS_PER_PAGE_CONFIG_ITEM = "postsPerPage";
	public static final String ATTACHS_PER_POST_CONFIG_ITEM = "attachsPerPost";

	public static final String PROFILE_TYPECODE_CONFIG_ITEM = "profileTypecode";

	public static final String PROFILE_NICK_ATTRIBUTENAME_CONFIG_ITEM = "profileNickAttributeName";


}
