<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpjs" uri="/jpjasper-aps-core" %>
<wp:headInfo type="CSS" info="../../plugins/jpjasper/static/css/jpjasper.css" />
<div class="jpjasper-report">
	<s:if test="%{showletTitleParam!=null}">
		<h1><s:property value="showletTitleParam" /></h1>
	</s:if>
	<div class="row-fluid">
		<div class="span12">
			<iframe
				src="<s:property value="iframeSrcUrl" escapeXml="false" />"
				class="jpjasper-iframe-detail"
				></iframe>
		</div>
	</div>
	<s:if test="null != showletDownloadFormats && showletDownloadFormats.size > 0">
		<div class="row-fluid jpjasper-download-formats">
			<div class="span12">
				<p>
					<span class="label"><wp:i18n key="jpjasper_DOWNLOAD_AS" /></span>

					<s:iterator var="currentFormat" value="showletDownloadFormats" status="statusVar">
						<s:url namespace="/do/jpjasper/FrontEnd/Report/Download" action="downloadReport" var="downloadReportActionVar">
							<s:param name="format" value="#currentFormat" />
							<s:param name="uriString" value="actionUriString" />
							<s:param name="showletInputControlValues" value="formInputControlParams" />
						</s:url>
							<a class="btn btn-default" href="<s:property value="#downloadReportActionVar" escapeHtml="false" escapeXml="false" />">
								<span class="icon icon-download-alt"></span>&#32;<s:property value="#currentFormat"/>
							</a>
							<s:if test="!#statusVar.last">&#32;</s:if>
					</s:iterator>
				</p>
			</div>
		</div>
	</s:if>
</div>