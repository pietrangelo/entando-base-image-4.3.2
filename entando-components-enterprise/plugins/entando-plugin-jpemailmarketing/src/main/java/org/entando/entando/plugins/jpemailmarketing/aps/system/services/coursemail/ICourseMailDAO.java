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
package org.entando.entando.plugins.jpemailmarketing.aps.system.services.coursemail;

import java.util.List;
import java.util.Date;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface ICourseMailDAO {

	public List<Integer> searchCourseMails(FieldSearchFilter[] filters);

	public CourseMail loadCourseMail(int id);

	public List<Integer> loadCourseMails();

	public void removeCourseMail(int id);

	public void updateCourseMail(CourseMail courseMail);

	public void insertCourseMail(CourseMail courseMail);

	public List<Integer> loadCourseMails(int courseId);

}