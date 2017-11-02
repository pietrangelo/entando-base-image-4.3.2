<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />
<script type="text/javascript" src="<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/adapters/jquery.js"></script>
<script>
	jQuery(function(){
			$('[data-component="fckeditor"]').ckeditor({
				customConfig : '<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/jpemailmarketing-config.js',
				language: '<s:property value="locale" />'
			});

		$('[data-button="preview"]').on('click', function(ev){
			ev.preventDefault();
			var textarea = $($(this).attr('data-ref'));
			CKEDITOR.instances[
				$(textarea).attr('id')
				].execCommand('preview');
		});

	});
</script>
