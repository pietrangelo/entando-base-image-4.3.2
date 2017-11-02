<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><s:text name="jpforum.title.forumManagement" /></h1>
<div id="main">
	<s:form cssClass="action-form">
		<s:if test="hasActionErrors()">
			<div class="message message_error">
			<h2><s:text name="message.title.ActionErrors" /></h2>	
				<ul>
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
			<h2><s:text name="message.title.FieldErrors" /></h2>	
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
			<h2><s:text name="messages.confirm" /></h2>	
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
			</div>
		</s:if>
		
		<p><s:text name="jpforum.label.sectionTree.intro" /></p>
	
		<fieldset class="margin-more-top">
			<legend><s:text name="jpforum.label.sectionTree" /></legend>
			<ul id="jpforum_sectionTree">
				<s:set var="inputFieldName" value="'selectedNode'" />
				<s:set var="selectedTreeNode" value="selectedNode" />
				<s:set var="liClassName" value="'page'" />
				<s:set var="currentRoot" value="treeRootNode" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
			</ul>
		</fieldset>
		
		<fieldset id="actions-container">
			<legend><s:text name="jpforum.title.sectionActions" /></legend>
	
			<p class="buttons">
				<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/page-new.png</s:set>
				<wpsf:submit useTabindexAutoIncrement="true" action="new" type="image" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.new')}" title="%{getText('jpforum.section.options.new')}" />
				
				<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/page-edit.png</s:set>
				<wpsf:submit useTabindexAutoIncrement="true" action="edit" type="image" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.modify')}" title="%{getText('jpforum.section.options.modify')}" />
				
				<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/delete.png</s:set>	
				<wpsf:submit useTabindexAutoIncrement="true" action="trash" type="image" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.delete')}" title="%{getText('jpforum.section.options.delete')}" />
				
				<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/go-up.png</s:set>
				<wpsf:submit useTabindexAutoIncrement="true" action="moveUp" type="image" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.moveUp')}" title="%{getText('jpforum.section.options.moveUp')}" />
				
				<s:set var="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/go-down.png</s:set>
				<wpsf:submit useTabindexAutoIncrement="true" action="moveDown" type="image" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.moveDown')}" title="%{getText('jpforum.section.options.moveDown')}" />
			</p>
		</fieldset>
		
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" action="reloadIndex" type="submit" src="%{#iconImagePath}" value="%{getText('jpforum.section.options.reloadIndex')}" title="%{getText('jpforum.section.options.reloadIndex')}" />
		</p>
	</s:form>
</div>