<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<% pageContext.setAttribute("carriageReturn", "\r"); %>
<% pageContext.setAttribute("newLine", "\n"); %>
<% pageContext.setAttribute("tabChar", "\t"); %>
<%-- reading the list from mainBody.jsp with: <wpsa:activityStream var="activityStreamListVar" /> --%>
<c:set var="ajax" value="${param.ajax eq 'true'}" />
<s:set var="ajax" value="#attr.ajax" />
<s:if test="#ajax"><%-- ajax eh? so set the #activityStreamListVar variable accordingly --%>
	<s:set var="activityStreamListVar" value="%{getActionRecordIds()}" />
</s:if>
<s:else><%-- use the #activityStreamListVar from mainBody.jsp --%></s:else>
<c:set var="currentUsernameVar" value="${session.currentUser.username}" />
<s:set var="currentUsernameVar" value="#attr.currentUsernameVar" />
<wp:userProfileAttribute username="${currentUsernameVar}" attributeRoleName="userprofile:fullname" var="browserUserFullnameVar" />
<wp:userProfileAttribute username="${currentUsernameVar}" attributeRoleName="userprofile:email" var="browserUserEmailAttributeVar" />
<wp:ifauthorized permission="superuser" var="browserIsSuperUser" />
<wpsa:activityStreamLastUpdateDate var="lastUpdateDateVar" />
<s:date name="%{#lastUpdateDateVar}" format="yyyy-MM-dd HH:mm:ss|SSS" var="lastUpdateDateVar" />
<s:iterator value="#activityStreamListVar" var="actionLogRecordIdVar" status="currentEvent">
	<wpsa:actionLogRecord key="%{#actionLogRecordIdVar}" var="actionLogRecordVar" />
	<s:set var="usernameVar" value="#actionLogRecordVar.username" scope="page" />
	<wp:userProfileAttribute username="${usernameVar}" attributeRoleName="userprofile:fullname" var="fullnameVar" />
	<wp:userProfileAttribute username="${usernameVar}" attributeRoleName="userprofile:email" var="emailAttributeVar" />
	<s:set var="fullnameVar" value="#attr.fullnameVar" />
	<s:set var="emailAttributeVar" value="#attr.emailAttributeVar" />
	<li
		class="media row padding-large-bottom"
		data-entando-id="<s:property value="#actionLogRecordVar.id" />"
		data-entando-creationdate="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm:ss|SSS" />"
		data-entando-updatedate="<s:property value="#lastUpdateDateVar" />"
	>
		<div class="col-xs-12 col-sm-2 col-lg-1 margin-small-bottom activity-stream-picture">
			<img alt=" " src="<s:url action="avatarStream" namespace="/do/jpmultisite/user/avatar">
				<s:param name="gravatarSize">56</s:param>
				<s:param name="username" value="#actionLogRecordVar.username" />
			</s:url>" width="56" height="56" class="img-circle media-object" />
		</div>
		<div class="media-body col-xs-12 col-sm-10 col-lg-11 activity-stream-event <s:if test="#currentEvent.first && !#ajax">event-first</s:if>">
			<s:set var="activityStreamInfoVar" value="#actionLogRecordVar.activityStreamInfo" />
			<s:set var="authGroupNameVar" value="#activityStreamInfoVar.linkAuthGroup" scope="page" />
			<s:set var="authPermissionNameVar" value="#activityStreamInfoVar.linkAuthPermission" scope="page" />
			<wp:ifauthorized groupName="${authGroupNameVar}" permission="${authPermissionNameVar}" var="isAuthorizedVar" />
			<div class="popover right display-block" data-entando="ajax-update">
				<div class="arrow"></div>
				<div class="popover-content">
					<c:choose>
						<c:when test="${isAuthorizedVar}">
							<a
								href="<s:url action="view" namespace="/do/jpmultisite/userprofile"><s:param name="username" value="#actionLogRecordVar.username"/></s:url>"
								title="<s:text name="label.viewProfile" />: <s:property value="#actionLogRecordVar.username" />">
									<s:if test="null != #fullnameVar && #fullnameVar.length() > 0">
										<s:property value="#fullnameVar" />
									</s:if>
									<s:else>
										<s:property value="#actionLogRecordVar.username" />
									</s:else>
							</a>
						</c:when>
						<c:otherwise>
							<s:if test="null != #fullnameVar && #fullnameVar.length() > 0">
								<s:property value="#fullnameVar" />
							</s:if>
							<s:else>
								<s:property value="#actionLogRecordVar.username" />
							</s:else>
						</c:otherwise>
					</c:choose>
					&#32;&middot;&#32;
					<wpsa:activityTitle actionName="%{#actionLogRecordVar.actionName}" namespace="%{#actionLogRecordVar.namespace}" actionType="%{#activityStreamInfoVar.actionType}" />:&#32;
					<s:set var="linkTitleVar" value="%{getTitle('view/edit', #activityStreamInfoVar.objectTitles)}" />
					<c:choose>
						<c:when test="${isAuthorizedVar}">
							<s:url
								action="%{#activityStreamInfoVar.linkActionName}"
								namespace="%{#activityStreamInfoVar.linkNamespace}"
								var="actionUrlVar">
									<wpsa:paramMap map="#activityStreamInfoVar.linkParameters" />
							</s:url>
							<a href="<s:property value="#actionUrlVar" escape="false" />"><s:property value="#linkTitleVar" /></a>
						</c:when>
						<c:otherwise>
							<s:property value="#linkTitleVar" />
						</c:otherwise>
					</c:choose>
					<wpsa:activityStreamLikeRecords recordId="%{#actionLogRecordIdVar}" var="activityStreamLikeRecordsVar" />
					<s:set value="%{#activityStreamLikeRecordsVar.containsUser(#currentUsernameVar)}" var="likeRecordsContainsUserVar" />
					<p class="margin-small-vertical">
						<time datetime="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm" />" title="<s:date name="#actionLogRecordVar.actionDate" format="yyyy-MM-dd HH:mm" />" class="text-info">
							<s:date name="#actionLogRecordVar.actionDate" nice="true" />
						</time>
						<s:if test="#activityStreamLikeRecordsVar.size() > 0">
							&#32;&middot;&#32;
							<span
								data-toggle="tooltip"
								data-placement="bottom"
								data-original-title="<s:iterator value="#activityStreamLikeRecordsVar" var="activityStreamLikeRecordVar"><s:property value="#activityStreamLikeRecordVar.displayName" />&#32;
									</s:iterator><s:text name="label.like.likesthis" />">
								<s:property value="#activityStreamLikeRecordsVar.size()" />
								&#32;
								<s:text name="label.like.number" />
							</span>
						</s:if>
						&#32;&middot;&#32;
						<%-- like / unlike --%>
						<a
							data-entando="like-link"
							href="<s:url namespace="/do/jpmultisite/ActivityStream" action="%{#likeRecordsContainsUserVar ? 'unlikeActivity' : 'likeActivity'}">
								<s:param name="recordId" value="%{#actionLogRecordIdVar}" />
								</s:url>">
								<s:if test="%{#likeRecordsContainsUserVar}" ><s:text name="label.like.unlike" /></s:if>
								<s:else><s:text name="label.like.like" /></s:else>
						</a>
					</p>
				</div>
			</div>

			<%-- comments --%>
			<wpsa:activityStreamCommentRecords recordId="%{#actionLogRecordIdVar}" var="activityStreamCommentListVar" />
			<div class="padding-base-left" style="margin-left: 20px" data-entando="ajax-update">
				<h4 class="sr-only"><s:text name="activity.stream.title.comments" /></h4>
				<s:iterator value="#activityStreamCommentListVar" var="activityStreamCommentVar">
					<div class="media"
 						data-entando-commentid="<s:property value="#activityStreamCommentVar.id" />"
						data-entando-commentdate="<s:date name="%{#activityStreamCommentVar.commentDate}" format="yyyy-MM-dd HH:mm:ss|SSS" />">
						<a
							class="pull-left"
							href="<s:url action="view" namespace="/do/jpmultisite/userprofile"><s:param name="username" value="#activityStreamCommentVar.username"/></s:url>"
							title="<s:text name="label.viewProfile" />:&#32;<s:property value="#activityStreamCommentVar.displayName" />"
							>
							<img
								class="img-circle media-object stream-img-small"
								src="<s:url action="avatarStream" namespace="/do/jpmultisite/user/avatar">
									<s:param name="gravatarSize">56</s:param>
									<s:param name="username" value="#activityStreamCommentVar.username" />
								</s:url>" />
						</a>
						<div class="media-body">
							<h5 class="media-heading">
								<a
									href="<s:url action="view" namespace="/do/jpmultisite/userprofile"><s:param name="username" value="#activityStreamCommentVar.username"/></s:url>"
									title="<s:text name="label.viewProfile" />:&#32;<s:property value="#activityStreamCommentVar.displayName" />">
									<s:property value="#activityStreamCommentVar.displayName" /></a>
								&#32;&middot;&#32;<time datetime="<s:date name="#activityStreamCommentVar.commentDate" format="yyyy-MM-dd HH:mm" />" title="<s:date name="#activityStreamCommentVar.commentDate" format="yyyy-MM-dd HH:mm" />" class="text-info">
									<s:date name="%{#activityStreamCommentVar.commentDate}" nice="true" />
								</time>
								<%-- delete comment --%>
								<s:if test="#activityStreamCommentVar.username == #currentUsernameVar || #attr.browserIsSuperUser">
									<a href="#delete" data-entando="delete-comment-ajax" class="pull-right">
										<span class="icon fa fa-icon fa-times-circle-o"></span>
										&nbsp;<s:text name="activity.stream.button.delete" />
									</a>
								</s:if>
							</h5>
							<%-- comment text --%>
							<span class="media-text">
							<c:set var="STRING_TO_ESCAPE"><s:property value="#activityStreamCommentVar.commentText" /></c:set>
							<c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(fn:replace(fn:trim(STRING_TO_ESCAPE), carriageReturn, ' ') , newLine, '<br />'), tabChar, '&emsp;')}" />
							<c:set var="ESCAPED_STRING" value="${fn:replace(ESCAPED_STRING,'<br /><br /><br />','<br />')}" />
							<c:out value="${ESCAPED_STRING}" escapeXml="false" />
							</span>
						</div>
					</div>
				</s:iterator>
			</div>
			<div class="padding-base-left margin-small-top" style="margin-left: 20px">
				<div class="insert-comment media <s:if test="#ajax"> hide hidden </s:if>">
					<span class="pull-left" >
						<img
							class="img-circle media-object stream-img-small"
							src="<s:url action="avatarStream" namespace="/do/jpmultisite/user/avatar">
								<s:param name="gravatarSize">56</s:param>
								<s:param name="username" value="#currentUsernameVar" />
							</s:url>" />
					</span>
					<div class="media-body">
						<%-- insert comment --%>
						<form action="<s:url action="addComment" namespace="/do/jpmultisite/ActivityStream" />" class="form-horizontal">
							<wpsf:hidden name="streamRecordId" value="%{#actionLogRecordIdVar}" />
							<label for="textarea-stream-<s:property value="#actionLogRecordIdVar" />" class="sr-only"><s:text name="activity.stream.comment.insert.tip" /></label>
							<textarea
								id="textarea-stream-<s:property value="#actionLogRecordIdVar" />"
								role="textbox"
								aria-multiline="true"
								class="col-xs-12 col-sm-12 col-md-12 col-lg-12 form-control"
								cols="30"
								rows="1"
								placeholder="<s:property value="%{getText('activity.stream.comment.insert.tip')}" />"
								title="<s:property value="%{getText('activity.stream.comment.insert.tip')}" />"
								name="commentText"></textarea>
							<wpsf:submit
								type="button"
								data-entando="add-comment-button"
								data-loading-text="%{getText('activity.stream.note.loading')}"
								value="%{getText('activity.stream.button.submit.comment')}"
								cssClass="margin-small-top pull-right btn btn-sm btn-default">
									<span class="icon fa fa-comment"></span>&#32;<s:text name="activity.stream.button.submit.comment" />
							</wpsf:submit>
						</form>
					</div>
				</div>
			</div>
		</div>
	</li>
</s:iterator>