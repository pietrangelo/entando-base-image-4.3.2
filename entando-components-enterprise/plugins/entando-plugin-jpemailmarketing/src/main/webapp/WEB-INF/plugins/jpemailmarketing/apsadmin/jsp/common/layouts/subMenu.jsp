<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:if test="${isSuperuser}">
	<li class="margin-large-bottom"><span class="h5"><s:text name="jpemailmarketing.name" /><%-- <s:text name="jpemailmarketing.menu.admin" /> --%></span>
		<ul class="nav nav-pills nav-stacked">
			<li>
				<a title="<s:text name="jpemailmarketing.title.courseManagement" />&#32;<s:text name="label.configure" />" href="<s:url namespace="/do/jpemailmarketing/Course" action="configure" />">
					<span class="sr-only"><s:text name="jpemailmarketing.title.courseManagement" /></span>
						<s:text name="label.configure" />
				</a>
			</li>
			<li>
				<a href="<s:url namespace="/do/jpemailmarketing/Course" action="list" />">
					<s:text name="jpemailmarketing.title.courseManagement" />
				</a>
			</li>
			<li>
				<a href="<s:url namespace="/do/jpemailmarketing/Form" action="list" />" >
					<s:text name="jpemailmarketing.title.formManagement" />
				</a>
			</li>
		</ul>
	</li>
</c:if>
