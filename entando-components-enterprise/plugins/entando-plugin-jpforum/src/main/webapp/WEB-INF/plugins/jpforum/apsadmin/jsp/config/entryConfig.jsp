<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><a href="<s:url action="view" namespace="/do/jpforum/Config/" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpforum.title.forumManagement" />"><s:text name="jpforum.title.forumManagement" /></a></h1>

<div id="main">
	<h2><s:text name="jpforum.title.forumEditConfig" /></h2>   
	
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
		<fieldset class="margin-more-top">
			<legend><s:text name="label.info" /></legend>
				
				<p>
					<label for="jpforum_postspage" class="basic-mint-label"><s:text name="jpforum.label.postsPerPage" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="jpforum_postspage" name="config.postsPerPage" list="#{1:1,5:5,10:10,20:20,30:30,50:50,70:70,100:100}" cssClass="text" />
				</p>
				
				
				<p>
					<label for="jpforum_attachsposts" class="basic-mint-label"><s:text name="jpforum.label.attachsPerPost" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="jpforum_attachsposts" name="config.attachsPerPost" list="#{1:1,2:2,3:3,4:4,5:5,10:10}" cssClass="text" />
				</p>

				<p>
					<label for="jpforum_pt" class="basic-mint-label"><s:text name="jpforum.label.profileTypecode" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpforum_pt" name="config.profileTypecode" cssClass="text" />
				</p>
				
				<p>
					<label for="jpforum_nickattribute" class="basic-mint-label"><s:text name="jpforum.label.profileNickAttributeName" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpforum_nickattribute" name="config.profileNickAttributeName" cssClass="text" />
				</p>
			</fieldset>
			
			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
			</p>
	</s:form>
</div>