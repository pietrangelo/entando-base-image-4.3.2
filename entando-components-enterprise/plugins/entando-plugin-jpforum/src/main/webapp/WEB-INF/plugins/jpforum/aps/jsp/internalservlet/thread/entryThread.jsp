<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:set var="currentLang"><wp:info key="currentLang" /></s:set>
<s:set var="section" value="currentSection" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<%-- JS! hidden 'cause already include with pagemodel.
 <wp:headInfo type="JS" info="../../plugins/jpforum/static/js/lib/124.js" />
--%>
<wp:headInfo type="JS" info="../../plugins/jpforum/static/js/lib/moo-jAPS-0.2.js" />
<c:set var="JS_EDITOR">
	window.addEvent('domready', function() {
		new HoofEd({
			basePath: "<wp:resourceURL/>plugins/jpforum/static/js/lib/hoofed",
			lang: "<wp:info key="currentLang" />",
			toolClass: 'jpforum_editor',
			textareaID: 'jpforum_post_body',
			buttons: [ "h1", "h2" , "h3" , "code" , "bold" , "italic" , "underline" , "link" , "img" , "quote" , "small" , "big", "red", "green", "blue", "ul", "ol", "br" ],
			toolPosition: "after",
			toolElement: "p"
		});
	});
</c:set>
<wp:headInfo type="JS_RAW" info="${JS_EDITOR}" />


<div class="jpforum showlet">
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>

	<%--
	<h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />>
	--%>

	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<s:if test="#section != null">
		<%-- INCLUSIONE BRICIOLE --%>
		<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/breadcrumbs.jsp" />
		<hr />
	</s:if>

	<div class="jpforum_block">

		<%-- title: new thread or new post --%>
		<s:if test="%{section != null}">
			<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_THREAD_NEW" /></h<s:property value="jpforumTitle2" />>
			<%-- <p>STAI CREANDO UN THREAD</p> --%>
		</s:if>
		<s:else>
			<%-- get the thread for rendering title --%>
			<s:set var="thread" value="%{getThread(thread)}" />

			<%-- if it's a new post --%>
			<s:if test="strutsAction == 1">
				<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_POST_NEW" /></h<s:property value="jpforumTitle2" />>
				<p><wp:i18n key="jpforum_SEND_POST_TO" />&#32;<em><s:property value="%{#thread.post.title}" /></em></p>
			</s:if>

			<%-- if it's edit action --%>
			<s:elseif test="strutsAction == 2">
				<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_POST_EDIT" /></h<s:property value="jpforumTitle2" />>
				<p><wp:i18n key="jpforum_EDIT_POST_FROM" />&#32;<em><s:property value="%{#thread.post.title}" /></em></p>
			</s:elseif>
		</s:else>
		<div class="jpforum_entryPost">

			<s:set var="saveThreadActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/savePost.action" /><s:if test='%{post != null && post != "" && strutsAction == 2}'>#<s:property value="jpforumPostHtmlId" /><s:property value="post" /></s:if></s:set>
			<form action="<s:property value="%{#saveThreadActionPath}" escape="false" />" method="post" enctype="multipart/form-data">
			<s:token name="w" />
				<s:if test="hasActionErrors()">
					<div class="message message_error">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.ActionErrors" /></h<s:property value="jpforumTitle3" />>
						<ul>
						<s:iterator value="actionErrors">
							<li><s:property escape="false" /></li>
						</s:iterator>
						</ul>
					</div>
				</s:if>

				<s:if test="hasFieldErrors()">
					<div class="message message_error">
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="message.title.FieldErrors" /></h<s:property value="jpforumTitle3" />>
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
						<h<s:property value="jpforumTitle3"/> class="title_level3"><s:text name="messages.confirm" /></h<s:property value="jpforumTitle3" />>
						<ul>
							<s:iterator value="actionMessages">
								<li><s:property/></li>
							</s:iterator>
						</ul>
					</div>
				</s:if>

				<p class="noscreen">
					<wpsf:hidden name="strutsAction" />
					<wpsf:hidden name="section" />
					<wpsf:hidden name="thread" />
					<wpsf:hidden name="post" />
					<wpsf:hidden name="open" />
					<wpsf:hidden name="pin" />
				</p>
				<%--
					does the user need this?
					OPEN: <s:property value="open" /><br />
					PIN: <s:property value="pin" /><br />
				--%>
				<div class="jpforum_block">
					<s:set var="htmlSectionCode" value="#section.code" />
					<fieldset>
						<legend><wp:i18n key="jpforum_POST_TEXT" /></legend>
						<p>
							<label for="jpforum_post_title<s:property value="#htmlSectionCode" />"><wp:i18n key="jpforum_LABEL_POST_TITLE" /></label>:<br />
							<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpforum_post_title%{#htmlSectionCode}" name="title" />
						</p>
						<p>
							<label for="jpforum_post_body"><wp:i18n key="jpforum_LABEL_POST_BODY" /></label>:<br />
							<wpsf:textarea useTabindexAutoIncrement="true" id="jpforum_post_body" cols="20" rows="10" name="body" value="%{getMarkup(body)}" /><br />
						</p>
					</fieldset>
				</div>

				<%-- attachments --%>
				<div class="jpforum_block">
					<fieldset>
						<legend><wp:i18n key="jpforum_POST_ATTACHMENTS" /></legend>
						<s:if test="%{strutsAction == 2}">
							<s:set var="attachs" value="%{getAttachs(post)}" />
						</s:if>

						<s:set var="items" value="attachsPerPost" />
						<s:if test="null != #attachs.size">
							<s:set var="items" value="#items - #attachs.size"></s:set>
						</s:if>
						<div class="jpforum_post_attachments">
							<s:iterator var="attach" value="#attachs" status="alreadyAttached" >
								<p>
									<s:if test="%{isAllowedOnEditPost(#attach.postCode)}">
										<s:set var="trashAttachActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/Attach/trashAttach.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter><wp:parameter name="post"><s:property value="#attach.postCode" /></wp:parameter><wp:parameter name="name"><s:property value="#attach.name" /></wp:parameter></wp:action></s:set>
										<a title="<wp:i18n key="jpforum_POST_ATTACH_DELETE" />:&#32;<s:property value="#attach.name"/>" href="<s:property value="%{#trashAttachActionPath}" escape="false" />"><img class="linkedImg" src="<wp:resourceURL/>plugins/jpforum/static/img/icons/attach_remove.png" alt="<wp:i18n key="jpforum_POST_ATTACH_DELETE" />" /></a>
									</s:if>
									<wp:i18n key="jpforum_LABEL_POST_ATTACH" />&#32;<s:property value="#alreadyAttached.count"/>:
									<s:property value="#attach.name"/>,&#32;<wp:i18n key="jpforum_POST_ATTACH_SIZE_KB" />&#32;<s:property value="#attach.fileSize / 1024" /> kb
									<%-- post: <s:property value="#attach.postCode"/> --%>
								</p>
							</s:iterator>
							<s:if test="#attachs == null">
								<s:set var="attachSize" value="0" />
							</s:if>
							<s:else>
								<s:set var="attachSize" value="#attachs.size" />
							</s:else>
							<s:iterator value="(#items).{ #this }" status="newFile">
								<s:set var="currentCount" value="%{#newFile.count+#attachSize}" />
								<p>
									<label for="jpforum_post_new_file<s:property value="#htmlSectionCode" /><s:property value="#currentCount" />"><wp:i18n key="jpforum_LABEL_POST_ADD_ATTACH" />&#32;<s:property value="#currentCount"/></label>
									<input id="jpforum_post_new_file<s:property value="#htmlSectionCode" /><s:property value="#currentCount"/>" type="file" name="myDoc" />
								</p>
							</s:iterator>
						</div>
					</fieldset>
				</div>

				<p>
					<input type="submit" class="button" value="<wp:i18n key="jpforum_BUTTON_POST_SAVE" />" />
				</p>

				<p>
					<%-- back link to section or thread (where user is replying) --%>
					<s:if test='%{post != null && post != ""}'>
						<s:set var="tmp_msg"><wp:i18n key="jpforum_GOTO_CANCEL_TO_THREAD" /></s:set>
						<s:set var="backLinkPost"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="post" /></wp:parameter><wp:parameter name="thread"><s:property value="thread" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="post" /></s:set>
					</s:if>
					<s:else>
						<s:set var="tmp_msg"><wp:i18n key="jpforum_GOTO_CANCEL_TO_SECTION" /></s:set>
						<s:set var="backLinkPost"><wp:action path="/ExtStr2/do/jpforum/Front/Browse/viewSection.action" ><wp:parameter name="section"><s:property value="section" /></wp:parameter></wp:action></s:set>
					</s:else>
					<a  href="<s:property value="%{#backLinkPost}" escape="false" />"><s:property value="#tmp_msg" /></a>
				</p>

			</form>
		</div>

	</div>

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>

</div>