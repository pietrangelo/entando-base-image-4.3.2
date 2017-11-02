<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />
<script src="<wp:resourceURL />plugins/jpemailmarketing/administration/js/typeahead/typeahead.bundle.min.js"></script>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpemailmarketing/administration/css/typeahead.js.css" />

<script>
var source = [
<s:iterator value="timezones" var="tz" status="status">{key:'<s:property value="#tz.key" escapeXml="false" escapeHtml="false" escapeJavaScript="true" />',value:'<s:property value="#tz.value" escapeXml="false" escapeHtml="false" escapeJavaScript="true" />'}
<s:if test="!#status.last">,</s:if>
</s:iterator>
];

jQuery(function(){
	var timezonesEngine = new Bloodhound({
		datumTokenizer: function(datum) {
			return datum.value.split(/[\/\s_]/gi);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		local: source
	});
	timezonesEngine.initialize();

	$('#course-crontimezoneid').typeahead({
			hint: true,
			minLength: 1,
			highlight: true
		},
		{
			name: 'timezones',
			displayKey: 'value',
			source: timezonesEngine.ttAdapter()
	});

	$('#course-crontimezoneid').on('typeahead:selected', function(ev, suggestion, dataset) {
		$($(this).attr('data-ref')).val(suggestion.key);
	})

});
</script>


<!-- this is for emails... -->
<script type="text/javascript" src="<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/adapters/jquery.js"></script>
	<script>
		jQuery(function(){
			//Hypertext Attribute
				$('[data-component="fckeditor"]').ckeditor({
					customConfig : '<wp:resourceURL />plugins/jpemailmarketing/administration/js/ckeditor/jpemailmarketing-config.js',
					language: '<s:property value="locale" />'
				});
		})
	</script>
