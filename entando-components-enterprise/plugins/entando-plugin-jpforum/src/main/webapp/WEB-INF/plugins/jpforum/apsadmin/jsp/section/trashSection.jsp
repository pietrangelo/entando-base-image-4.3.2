<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><a href="<s:url action="viewTree" namespace="/do/jpforum/Section/" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpforum.title.forumManagement" />"><s:text name="jpforum.title.forumManagement" /></a></h1>
<div id="main">
	<h2><s:text name="jpforum.title.trashSection" /></h2>
	<s:form action="delete">
		<p>
			<s:hidden name="strutsAction" />
			<s:hidden name="code" />
			
			<%-- confirm text: do you really want to delete? --%>
			<s:text name="jpforum.label.trash.confirm" />&#32;
			<em class="important"><s:property value="%{getTitle(code, titles)}" /></em>&#32;?&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.confirm')}" cssClass="button" />
		</p>
		
		<%-- backlink to section tree --%>
		<p>
			<a href="<s:url action="viewTree" />"><s:text name="jpforum.label.trashSection.dont.back.goback" /></a>
		</p>
	</s:form>
</div>