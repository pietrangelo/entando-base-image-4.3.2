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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.course;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ICourseDAO {

	public List<Integer> searchCourses(FieldSearchFilter[] filters);
	
	public Course loadCourse(int id);

	public List<Integer> loadCourses();

	public void removeCourse(int id);
	
	public void updateCourse(Course course);

	public void insertCourse(Course course);
	
	public List<Integer> loadActiveCourses();

	public static final String FILTER_ACTIVE = "active";
	public static final String FILTER_NAME = "name";
}