<%@ taglib prefix="wp" uri="/aps-core" %>
<%/*  //TODO: check if this jsp is useless */%>
<div class="alert alert-block">
	<p>
		<wp:i18n key="jpjasper_CONNECTION_ERROR" /> - url '<wp:info key="systemParam" paramName="jpjasper_baseUrl" />'
	</p>
	<p>
		<wp:i18n key="jpjasper_USER_NOT_ALLOWED" />
	</p>
</div>