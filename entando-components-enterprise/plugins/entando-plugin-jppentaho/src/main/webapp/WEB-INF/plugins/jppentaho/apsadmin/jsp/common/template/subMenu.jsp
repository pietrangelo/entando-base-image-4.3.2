<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser" >
	<li class="openmenu"><a href="#" rel="fagiano_jppentaho" id="menu_jppentaho" class="subMenuToggler"><s:text name="jppentaho.admin.dataIntegration.menu" /></a>
	<div id="fagiano_jppentaho" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">		
		<ul>
			<li><a href="<s:url action="edit" namespace="/do/jppentaho/PentahoConfig" />" tabindex="<wpsa:counter />"><s:text name="jppentaho.admin.config.edit"/></a></li>
			<li><a href="<s:url action="listJob" namespace="/do/jppentaho/DataIntegration" />" tabindex="<wpsa:counter />"><s:text name="jppentaho.admin.dataIntegration.job.list"/></a></li>
			<li><a href="<s:url action="listKtr" namespace="/do/jppentaho/DataIntegration" />" tabindex="<wpsa:counter />"><s:text name="jppentaho.admin.dataIntegration.ktr.list"/></a></li>
		</ul>
	</div></div></div>
	</li>
</wp:ifauthorized>