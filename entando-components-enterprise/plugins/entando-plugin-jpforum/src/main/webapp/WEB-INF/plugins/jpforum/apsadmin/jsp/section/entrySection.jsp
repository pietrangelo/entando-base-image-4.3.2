<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><a href="<s:url action="viewTree" namespace="/do/jpforum/Section/" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpforum.title.forumManagement" />"><s:text name="jpforum.title.forumManagement" /></a></h1>
<div id="main">
	<h2 class="margin-bit-bottom">
		<s:if test="%{strutsAction==1}"><s:text name="jpforum.title.newSection" /></s:if>
		<s:elseif test="%{strutsAction==2}"><s:text name="jpforum.title.editSection" /></s:elseif>
	</h2>

	<s:form action="save">
		<s:if test="hasActionErrors()">
			<div class="message message_error">
			<h3><s:text name="message.title.ActionErrors" /></h3>	
				<ul>
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>	
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
			<h3><s:text name="messages.confirm" /></h3>	
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property/></li>
				</s:iterator>
			</ul>
			</div>
		</s:if>		
		
		<fieldset>
			<legend><s:text name="jpforum.section.infoSection" /></legend>
			
				<p class="noscreen">
					<wpsf:hidden name="strutsAction" />
					<wpsf:hidden name="selectedNode" />
					<wpsf:hidden name="position" />
					<wpsf:hidden name="parentCode" />
				</p>
			
				<s:if test="%{strutsAction==1}">
					<p>
						<label for="jpforum_section_name" class="basic-mint-label"><s:text name="code"  />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpforum_section_name" name="code" />
					</p>
				</s:if>
				<s:elseif test="%{strutsAction==2}">
					<p>
						<wpsf:hidden name="code" />
						<label for="jpforum_section_name" class="basic-mint-label"><s:text name="code"  />:</label>
						<input id="jpforum_section_name" class="text" type="text" name="code_fake" value="<s:property value="%{code}" />" readonly="readonly" disabled="disabled" />
					</p>
				</s:elseif>
			
				<s:iterator value="langs">
					<p>
						<label for="jpforum_section_title<s:property value="code" />" class="basic-mint-label"><span class="monospace">(<s:property value="code" />)</span>&#32;<s:text name="jpforum.name.sectionTitle" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="%{'title'+code}" id="jpforum_section_%{'title'+code}" value="%{titles.get(code)}" cssClass="text" />
					</p>
					<p>
						<label for="jpforum_section_description<s:property value="code" />" class="basic-mint-label"><span class="monospace">(<s:property value="code" />)</span>&#32;<s:text name="jpforum.name.sectionDescription" />:</label>
						<wpsf:textarea useTabindexAutoIncrement="true" cols="50" rows="3" name="%{'description'+code}" id="jpforum_section_%{'description'+code}" value="%{descriptions.get(code)}" cssClass="text"/>
					</p>
				</s:iterator>
			
				<p>
					<wpsf:checkbox useTabindexAutoIncrement="true" id="jpforum_section_isopen" name="open" cssClass="radiocheck" />&#32;
					<label for="jpforum_section_isopen"><s:text name="jpforum.name.open" /></label>
				</p>
				
				<p>
					<label for="jpforum_section_unauth" class="basic-mint-label"><s:text name="jpforum.name.unauthbehaviour" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="jpforum_section_unauth" list="#{0:getText('jpforum.name.unauthbehaviour.hidden'),1:getText('jpforum.name.unauthbehaviour.readonly')}" name="unauthBahaviour"  />
				</p>
		</fieldset>
	
			
		<fieldset>
			<legend id="jpforum_section_groups"><s:text name="jpforum.section.groups" /></legend>
			<s:if test="%{groups != null && (groups.size() > 0)}">
				<ul>
					<s:set name="removeIcon" id="removeIcon"><wp:resourceURL/>administration/common/img/icons/list-remove.png</s:set>
					<s:iterator var="group" value="groups">
						<li>
							<wpsf:hidden cssClass="noscreen" name="groups" value="%{#group}" />
							<wpsa:actionParam action="removeGroup" var="actionName">
								<wpsa:actionSubParam name="selectedGroup" value="%{#group}" />
							</wpsa:actionParam>
							<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />:&#32;
							<s:property value="%{getGroup(#group).descr}" />
						</li>
					</s:iterator>
				</ul>
			</s:if>
			
			<p>
				<label for="jpforum_section_groups" class="basic-mint-label"><s:text name="jpforum.label.addGroups" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpforum_section_groups" cssStyle="text" name="selectedGroup" list="availableGroups" listKey="name" listValue="descr" cssClass="text" />&#32;
				<wpsf:submit useTabindexAutoIncrement="true" type="submit" action="addGroup" name="ADD" title="%{getText('label.join')}" value="%{getText('label.join')}" cssClass="button" />
			</p>
			
		</fieldset>
	
		<fieldset>
			<legend><s:text name="jpforum.label.moderators" /></legend>
			<s:if test="availableModerators.size() > 0">
				<ul>
					<s:iterator var="moderator" value="availableModerators">
						<li><s:property value="#moderator"/></li>
					</s:iterator>
				</ul>
			</s:if>
			<s:else>
				<p><s:text name="jpforum.label.noModerators" /></p>
			</s:else>
		</fieldset>
	
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.save')}" />
		</p>
		
	</s:form>
</div>