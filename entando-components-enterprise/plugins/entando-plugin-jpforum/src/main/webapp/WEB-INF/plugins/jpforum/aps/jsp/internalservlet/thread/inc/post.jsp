<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<s:include value="/WEB-INF/plugins/jpforum/aps/jsp/internalservlet/inc/forum_settings.jsp" />
<%--
In order to work, this jsp needs a variable called "post".
--%>
<div class="jpforum_post_body">
	
	<div class="jpforum_post_body_col_sx">
		<%-- user info? --%>	
		<s:set var="currentUsername"><s:property value="#post.username"/></s:set>
		<jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  username="${currentUsername}"/>
		<s:if test="null != #request.currentAvatar">
			<p>
				<img src="<s:property value="#request.currentAvatar"/>" alt=""/>
			</p>
		</s:if>
	</div>
	
	<div class="jpforum_post_body_col_dx">
		<div class="jpforum_post_body_text_container">
			<%-- post title --%>
			<s:if test='%{#post.title != null && #post.title!=""}'>
				<h<s:property value="jpforumTitle4" /> class="title_level4"><s:property value="#post.title"/></h<s:property value="jpforumTitle4" />>
			</s:if>
			<s:else>
				<p class="noscreen"><wp:i18n key="jpforum_POST_UNTITLED" /></p>
			</s:else>
	
			<%-- post body --%>
			<div class="jpforum_post_body_text"><s:property escape="false" value="%{getHtml(#post.text)}"/></div>
			
			<%-- post attachments --%>
			<s:set var="attachs" value="%{getAttachs(#post.code)}" />
			<s:if test="%{#attachs.size > 0}">
				<div class="jpforum_post_body_attaches_container"> 
					<h<s:property value="jpforumTitle4" /> class="title_level4"><wp:i18n key="jpforum_TITLE_POST_ATTACHMENTS" /></h<s:property value="jpforumTitle4" />>
					<ol class="jpforum_post_body_attaches">
						<s:set var="currentPage" ><wp:currentPage param="code"/></s:set>
						<s:set var="currentLang" ><wp:info key="currentLang" /></s:set>
						<s:set var="applBaseURL" ><wp:info key="systemParam" paramName="applicationBaseURL" /></s:set>
						<s:iterator value="#attachs" var="attach">
							<li>
								<a href="<s:property value="applBaseURL" /><s:url action="downloadAttach" namespace="do/jpforum/Front/Attach" ><s:param name="post" value="#post.code" /><s:param name="name" value="#attach.name" /><s:param name="page" value="#currentPage" /><s:param name="lang" value="#currentLang" /></s:url>">
									<s:property value="#attach.name"/> (<s:property  value="#attach.fileSize / 1024"/> kb)
								</a><br />
							</li>
						</s:iterator>
					</ol>
				</div>
			</s:if>
		</div>
	</div>
	
</div>