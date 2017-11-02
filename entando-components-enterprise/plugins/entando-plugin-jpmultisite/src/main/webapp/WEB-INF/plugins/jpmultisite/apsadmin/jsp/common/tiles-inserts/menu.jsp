<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jacmswpsa" uri="/jacms-apsadmin-core" %>

<p id="manage" class="sr-only"><s:text name="note.userbar.intro" />:</p>

<p>
	<a class="btn btn-info btn-block" href="<s:url namespace="/do/jpmultisite/BaseAdmin" action="settings" />"><span class="icon fa fa-cog"></span>&#32;<s:text name="menu.configure" /></a>
</p>

<ul class="nav nav-pills nav-stacked" id="backoffice-menu-main" role="menubar">

	<wp:ifauthorized permission="superuser">
		<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jpmultisite/Group" />"><s:text name="menu.accountAdmin.groups" /></a></li>
	</wp:ifauthorized>

	<wp:ifauthorized permission="manageCategories">
		<li role="presentation"><a role="menuitem" href="<s:url action="viewTree" namespace="/do/jpmultisite/Category" />"><s:text name="menu.categoryAdmin" /></a></li>
	</wp:ifauthorized>

<wp:ifauthorized permission="managePages" var="isEditPages" />
<wp:ifauthorized permission="superuser" var="isSuperuser" />

<c:if test="${isEditPages || isSuperuser}">

<wp:ifauthorized permission="managePages">
	<li role="presentation"><a role="menuitem" href="<s:url action="viewTree" namespace="/do/jpmultisite/Page" />"><s:text name="menu.pageAdmin" /></a></li>
	<li role="presentation"><a role="menuitem" href="<s:url action="viewWidgets" namespace="/do/jpmultisite/Portal/WidgetType" />"><s:text name="menu.widgetAdmin" /></a></li>
</wp:ifauthorized>

</c:if>

<wp:ifauthorized permission="editContents" var="isEditContents" />
<wp:ifauthorized permission="manageResources" var="isManageResources" />

<c:if test="${isEditContents || isManageResources}">
	<wp:ifauthorized permission="editContents">

		<li class="panel-group" role="presentation">
			<div class="panel panel-default overflow-visible" role="presentation">
				<div class="panel-heading" role="presentation">
					<a data-toggle="collapse" href="#submenu-contents" class="display-block" id="aria-menu-jacms-contentadmin"  aria-haspopup="true" role="menuitem">
						<s:text name="jacms.menu.contentAdmin" />&#32;
						<span class="icon fa fa-chevron-down pull-right"></span>
					</a>
				</div>
				<div id="submenu-contents" class="panel-collapse collapse" role="presentation">
					<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-jacms-contentadmin">

<%--
	  <wp:ifauthorized permission="superuser">
	<li><a href="<s:url namespace="/do/jpsharedcontent/SharedContent" action="list" />" ><s:text name="jpsharedcontent.title.sharedContentManagement" /></a></li>
</wp:ifauthorized>
 --%>
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jpmultisite/Content" />"><s:text name="jacms.menu.contentAdmin.list" /></a></li>
						<wpsa:entityTypes entityManagerName="jpmultisiteContentManager" var="contentTypesVar" />

						<li role="presentation"><a href="<s:url namespace="/do/jpmultisite/SharedContent" action="list" />" ><s:text name="jpmultisite.title.sharedContentManagement" /></a></li>

						<li class="dropdown hidden-xs hidden-sm visible-md visible-lg" role="presentation">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#" id="aria-menu-jacms-new" role="menuitem">
								<s:text name="label.new.male" />&#32;<s:text name="label.content" />&#32;<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menubar" aria-labelledby="aria-menu-jacms-new">

								<s:iterator var="contentTypeVar" value="#contentTypesVar">

									<li role="presentation" class="hidden-xs hidden-sm visible-md visible-lg"><a role="menuitem" href="<s:url action="createNew" namespace="/do/jpmultisite/Content" >
											   <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
										   </s:url>" ><s:text name="label.new.male" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>

								</s:iterator>
							</ul>
						</li>
						<s:iterator var="contentTypeVar" value="#contentTypesVar">
							<jacmswpsa:contentType typeCode="%{#contentTypeVar.typeCode}" property="isAuthToEdit" var="isAuthToEditVar" />
							<s:if test="%{#isAuthToEditVar}">
							<li role="presentation" class="visible-xs visible-sm hidden-md hidden-lg"><a role="menuitem" href="<s:url action="createNew" namespace="/do/jpmultisite/Content" >
									   <s:param name="contentTypeCode" value="%{#contentTypeVar.typeCode}" />
								   </s:url>" ><s:text name="label.new.male" />&#32;<s:property value="%{#contentTypeVar.typeDescr}" /></a></li>
							</s:if>
						</s:iterator>
						<li role="presentation" class="divider visible-xs visible-sm hidden-md hidden-lg"><hr role="presentation" class="margin-none" />
					</ul>
				</div>
			</div>
		</li>

	</wp:ifauthorized>

	<wp:ifauthorized permission="manageResources">
		<li role="presentation" class="panel-group">
			<div role="presentation" class="panel panel-default">
				<div role="presentation" class="panel-heading">
					<a data-toggle="collapse" href="#submenu-resources" class="display-block" id="aria-menu-jacms-resources"  aria-haspopup="true" role="menuitem">
						<s:text name="jacms.menu.resourceAdmin" />&#32;
						<span class="icon fa fa-chevron-down pull-right"></span>
					</a>
				</div>
				<div id="submenu-resources" class="panel-collapse collapse" role="presentation">
					<ul class="panel-body nav nav-pills nav-stacked" role="menubar" aria-labelledby="aria-menu-jacms-resources">
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jpmultisite/Resource"><s:param name="resourceTypeCode" >Image</s:param></s:url>"><s:text name="jacms.menu.imageAdmin" /></a></li>
						<li role="presentation"><a role="menuitem" href="<s:url action="list" namespace="/do/jpmultisite/Resource"><s:param name="resourceTypeCode" >Attach</s:param></s:url>"><s:text name="jacms.menu.attachAdmin" /></a></li>
					</ul>
				</div>
			</div>
		</li>
	</wp:ifauthorized>

</c:if>
</ul>
