<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<wp:ifauthorized permission="superuser">
	<li class="openmenu"><a href="#" rel="fagiano_jpforum" id="menu_jpforum" class="subMenuToggler"><s:text name="jpforum.admin.menu" /></a>
		<div id="fagiano_jpforum" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">
			<ul>
				<li><a href="<s:url action="viewTree" namespace="/do/jpforum/Section" />" ><s:text name="jpforum.admin.menu.section" /></a></li>
				<li><a href="<s:url action="view" namespace="/do/jpforum/Config" />" ><s:text name="jpforum.admin.menu.config" /></a></li>		
			</ul>
		</div></div></div>
	</li>
</wp:ifauthorized>	