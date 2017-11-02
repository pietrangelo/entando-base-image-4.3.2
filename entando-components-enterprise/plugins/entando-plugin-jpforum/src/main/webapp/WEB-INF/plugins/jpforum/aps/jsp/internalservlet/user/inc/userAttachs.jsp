<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<wp:headInfo type="CSS" info="../../plugins/jpforum/static/css/jpforum.css" />
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />

<div class="jpforum showlet">

	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoStart.jsp" /> --%>
	
	<%-- 
	h<s:property value="jpforumTitle1"/> class="title_level1"><a href="<wp:url />"><wp:i18n key="jpforum_FORUM_TITLE" /></a></h<s:property value="jpforumTitle1" />> 
	--%>
	
	<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/welcome_user.jsp" />

	<div class="jpforum_block">
		<h<s:property value="jpforumTitle2"/> class="title_level2"><wp:i18n key="jpforum_TITLE_MANAGE_ATTACHMENTS" /></h<s:property value="jpforumTitle2" />> 
			
		<s:set var="attachs" value="%{getUserAttachs(username)}" />
		<p>
			<wp:i18n key="jpforum_ATTACHMENTS_COUNT" />:&#32;<s:property value="#attachs.size"/>
		</p> 
		
		<s:set var="usedSpace" value="%{getUsedSpace(username) / 1024}" />
		<s:set var="totalSpace" value="%{getUserAttachSpace(username) / 1024}" />
		<s:set var="availableSpace" value="(#totalSpace - #usedSpace)" />
		<p>
			<wp:i18n key="jpforum_ATTACHMENTS_USED_SPACE" />:&#32;<s:property value="#usedSpace"/>  /  <s:property value="#totalSpace"/>KB 
		</p>
		<s:if test="#attachs.size > 0">
			<table class="jpforum_table_generic" summary="<wp:i18n key="jpforum_ATTACHMENTS_LIST_SUMMARY" />">
				<caption><wp:i18n key="jpforum_ATTACHMENTS_LIST" /></caption>
				<tr>
					<th><wp:i18n key="jpforum_LABEL_ATTACHMENT_FILE" /></th>
					<th><wp:i18n key="jpforum_LABEL_ATTACHMENT_SIZE" /></th>
					<th><wp:i18n key="jpforum_LABEL_ATTACHMENT_POST" /></th>
					<th><wp:i18n key="jpforum_LABEL_ATTACHMENT_DELETE" /></th>
				</tr>
				
				<s:set var="currentPage" ><wp:currentPage param="code"/></s:set>
				<s:set var="currentLang" ><wp:info key="currentLang" /></s:set>
				<s:set var="applBaseURL" ><wp:info key="systemParam" paramName="applicationBaseURL" /></s:set>
				<s:iterator value="#attachs" var="attach">
					<s:set var="attachPost" value="getPost(#attach.postCode)" />
					<tr>
						<td>
							<a href="<s:property value="applBaseURL" /><s:url action="downloadAttach" namespace="do/jpforum/Front/Attach" ><s:param name="post" value="#attach.postCode" /><s:param name="name" value="#attach.name" /><s:param name="page" value="%{#currentPage}" /><s:param name="lang" value="%{#currentLang}" /></s:url>">
								<s:property value="#attach.name"/>
							</a>
						</td> 
						<td><s:property value="#attach.fileSize / 1024"/> KB</td> 
						<td>
							<s:set var="viewPostActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/Post/viewPost.action" ><wp:parameter name="post"><s:property value="#attach.postCode" /></wp:parameter><wp:parameter name="thread"><s:property value="#attachPost.threadId" /></wp:parameter></wp:action>#<s:property value="jpforumPostHtmlId" /><s:property value="#attach.postCode" /></s:set>
							<a title="<wp:i18n key="jpforum_ATTACHMENTS_GO_TO_POST" />: <s:property value="#attach.postCode"/>" href="<s:property value="#viewPostActionPath" escape="false" />"><s:property value="#attach.postCode"/></a>
						</td> 
						<td>
							<s:if test="%{isAllowedOnEditPost(#attach.postCode)}">
								<s:set var="trashAttachActionPath"><wp:action path="/ExtStr2/do/jpforum/Front/User/Attach/trashAttach.action" ><wp:parameter name="username"><s:property value="username" /></wp:parameter><wp:parameter name="post"><s:property value="#attach.postCode" /></wp:parameter><wp:parameter name="name"><s:property value="#attach.name" /></wp:parameter></wp:action></s:set>
								<a title="<wp:i18n key="jpforum_LABEL_ATTACHMENT_DELETE" />: <s:property value="#attach.name"/>" href="<s:property value="%{#trashAttachActionPath}" escape="false" />">
									<img src="<wp:resourceURL />plugins/jpforum/static/img/icons/attach_trash.png" alt="<wp:i18n key="jpforum_LABEL_ATTACHMENT_DELETE" />" />
								</a>
							</s:if>
							<s:else>
								<abbr title="<wp:i18n key="jpforum_ATTACHMENTS_OPERATION_NOT_AVAILABLE" />">&ndash;</abbr>
							</s:else>
						</td> 
					</tr>
				</s:iterator>
			</table>
		</s:if>
		
	</div>
	
	<%-- jAPSIntra Showlet Decoration --%>
	<%-- <jsp:include page="/WEB-INF/aps/jsp/widgets/inc/protoEnd.jsp" /> --%>	
	
</div>