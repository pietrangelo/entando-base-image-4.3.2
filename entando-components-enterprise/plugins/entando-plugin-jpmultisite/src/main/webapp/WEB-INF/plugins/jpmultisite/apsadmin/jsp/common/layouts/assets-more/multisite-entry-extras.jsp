<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />
<script>
	jQuery(function(){
		$('#jpmultisite-img-preview-button').popover();
		$('body').delegate('#jpmultisite-img-preview-image', 'click', function(){
			$('#jpmultisite-img-preview-button').popover('toggle');
		})
	})
</script>
