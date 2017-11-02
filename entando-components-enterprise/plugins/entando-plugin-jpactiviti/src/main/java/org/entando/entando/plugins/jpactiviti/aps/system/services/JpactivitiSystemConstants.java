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

package org.entando.entando.plugins.jpactiviti.aps.system.services;

/**
 * @author S.Loru
 */
public class JpactivitiSystemConstants {
	
	public static final String ACTIVITI_MANAGER = "jpactivitiActivitiManager";
	public static final String VACATION_MANAGER = "jpactivitiVacationManager";
	
	/*
	 * Widget configuration
	 */
	
	public static final String WIDGET_PARAM_HEIGHT = "height";
	public static final String WIDGET_PARAM_SECTION = "section";
	
	public static final String WIDGET_PARAM_TYPE = "type";
	public static final String WIDGET_PARAM_GROUP = "group";
	
	public static enum TaskListType{ALL, USER, GROUP};
	public static enum FormIdSearchType{TASKID, PROCESSID};
	
	/*
	 * Client endpoints 
	 */
	
	public static final String PARAM_URL_EXPLORER = "jpactiviti_explorer_baseUrl";
	public static final String PARAM_URL_REST = "jpactiviti_rest_baseUrl";
	public static final String URL_BASE = "/service/";
	public static final String URL_DEPLOYMENTS = URL_BASE + "repository/deployments";
	public static final String URL_PROCESS_DEFINITIONS = URL_BASE + "repository/process-definitions";
	public static final String URL_PROCESS_INSTANCES = URL_BASE + "runtime/process-instances";
	public static final String URL_PROCESS_INSTANCES_FULL = URL_BASE + "process-instance";
	public static final String URL_TASKS = URL_BASE + "runtime/tasks";
	public static final String URL_FORM = URL_BASE + "form/form-data";
	public static final String URL_HISTORIC = URL_BASE + "history/historic-detail";
	public static final String URL_HISTORIC_INSTANCES = URL_BASE + "history/historic-process-instances";
	
	
	
	public static final String TASK_ACTION_CLAIM = "claim";
	public static final String TASK_ACTION_DELEGATE = "delegate";
	public static final String TASK_ACTION_RESOLVE = "resolve";
	public static final String TASK_ACTION_COMPLETE = "complete";
	
}
