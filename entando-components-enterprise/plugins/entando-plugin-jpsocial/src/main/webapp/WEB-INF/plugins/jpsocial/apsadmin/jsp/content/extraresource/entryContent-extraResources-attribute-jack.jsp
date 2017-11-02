<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<!-- jpsocial start -->
<s:set var="myClient"><wpsa:backendGuiClient /></s:set>
<script type="text/javascript">
<!--//--><![CDATA[//><!--
//per attributo Hypertext
//per attributo Hypertext
<s:if test="htmlEditorCode != 'none'">

	<s:iterator value="langs" id="lang">
		<s:iterator value="content.attributeList" id="attribute">
		<%-- INIZIALIZZAZIONE TRACCIATORE --%>
		<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

		<s:if test="#attribute.type == 'SocialHypertext'">
			<s:if test="htmlEditorCode == 'fckeditor'">
				window.addEvent('domready', function() {
					var ofckeditor = CKEDITOR.replace("<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />", {
						customConfig : '<wp:resourceURL />administration/common/js/ckeditor/entando-ckeditor_config.js',
						//EntandoLinkActionPath: CKEDITOR.basePath+"/jAPS/saved-japslink.html"
						EntandoLinkActionPath: "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/Hypertext/entandoInternalLink.action",
						language: '<s:property value="locale" />',
						<s:if test="#myClient == 'advanced'">
							width: "660px"
						</s:if>
						<s:else>
							width: "540px"
						</s:else>
					});
				});
			</s:if>
			<s:elseif test="htmlEditorCode == 'hoofed'">
				window.addEvent('domready', function() {
					var ohoofed = new HoofEd({
						basePath: '<wp:resourceURL />administration/common/js/moo-japs/hoofed',
						lang: '<s:property value="currentLang.code" />',
						textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
						buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
						toolPosition: "after",
						toolElement: "span"
					});
				});
			</s:elseif>
		</s:if>

		<s:elseif test="#attribute.type == 'Monolist'">
			<s:set name="masterAttributeTracer" value="#attributeTracer" />
			<s:set name="masterAttribute" value="#attribute" />
			<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
				<s:set name="attributeTracer" value="#masterAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>
				<s:set name="elementIndex" value="#elementStatus.index" />


				<s:if test="#attribute.type == 'Composite'">
					<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
					<s:set name="masterCompositeAttribute" value="#attribute" />
					<s:iterator value="#attribute.attributes" id="attribute">
						<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
						<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
						<s:if test="#attribute.type == 'SocialHypertext'">
							<s:if test="htmlEditorCode == 'fckeditor'">
								window.addEvent('domready', function() {
									var ofckeditor = CKEDITOR.replace("<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />", {
										customConfig : '<wp:resourceURL />administration/common/js/ckeditor/entando-ckeditor_config.js',
										//EntandoLinkActionPath: CKEDITOR.basePath+"/jAPS/saved-japslink.html"
										EntandoLinkActionPath: "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/Hypertext/entandoInternalLink.action",
										language: '<s:property value="locale" />',
										width: "410px"
									});
									/* FCKEDITOR IS DEPRECATED!
									var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" );
									ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
									ofckeditor.BasePath = "<wp:resourceURL />administration/common/js/fckeditor/";
									ofckeditor.ToolbarSet = "Entando";
									ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />administration/common/js/fckeditor/EntandoConfig.js";
									ofckeditor.Height = 250;
									ofckeditor.ReplaceTextarea();
									*/
								});
							</s:if>
							<s:elseif test="htmlEditorCode == 'hoofed'">
								window.addEvent('domready', function() {
									var ohoofed = new HoofEd({
										basePath: '<wp:resourceURL />administration/common/js/moo-japs/hoofed',
										lang: '<s:property value="currentLang.code" />',
										textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
										buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
										toolPosition: "after",
										toolElement: "span"
									});
								});
							</s:elseif>
						</s:if>
					</s:iterator>
					<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
					<s:set name="attribute" value="#masterCompositeAttribute" />
					<s:set name="parentAttribute" value=""></s:set>
				</s:if>


				<s:elseif test="#attribute.type == 'SocialHypertext'">
					<s:if test="htmlEditorCode == 'fckeditor'">
						window.addEvent('domready', function() {
							var ofckeditor = CKEDITOR.replace("<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />", {
								customConfig : '<wp:resourceURL />administration/common/js/ckeditor/entando-ckeditor_config.js',
								//EntandoLinkActionPath: CKEDITOR.basePath+"/jAPS/saved-japslink.html"
								EntandoLinkActionPath: "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/Hypertext/entandoInternalLink.action",
								language: '<s:property value="locale" />',
								<s:if test="#myClient == 'advanced'">
									width: "660px"
								</s:if>
								<s:else>
									width: "540px"
								</s:else>
							});
						});
					</s:if>
					<s:elseif test="htmlEditorCode == 'hoofed'">
						window.addEvent('domready', function() {
							var ohoofed = new HoofEd({
								basePath: '<wp:resourceURL />administration/common/js/moo-japs/hoofed',
								lang: '<s:property value="currentLang.code" />',
								textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
								buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
								toolPosition: "after",
								toolElement: "span"
							});
						});
					</s:elseif>
				</s:elseif>
			</s:iterator>
			<s:set name="attributeTracer" value="#masterAttributeTracer" />
			<s:set name="attribute" value="#masterAttribute" />
		</s:elseif>
		</s:iterator>
	</s:iterator>

</s:if>
//fine attributo Hypertext


//--><!]]></script>

<!-- jpsocial -->
<s:iterator value="content.attributeList" var="attribute" status="status">
	<s:if test="(!#jpsocialExtrasLoaded) && (#attribute.type == 'SocialHypertext' || #attribute.type == 'SocialMonotext' || #attribute.type == 'SocialText' || #attribute.type == 'SocialImage' || #attribute.type == 'SocialLongtext' || #attribute.type == 'SocialAttach')">
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpsocial/administration/common/css/jpsocial-administration.css" media="screen" />	
	
	<script type="text/javascript"><!--//--><![CDATA[//><!--
	window.addEvent("domready", function(){
		var sharesList = document.getElements(".jpsocial-contentshare");
		
		window.jpsocialShow = function(togg, target) {
			var scrollBefore =  window.getScrollSize().x;
			var windowDim = window.getSize();
			var menu = togg.retrieve("menu");
			var buttonsList = document.getElements(".jpsocial-share-menu");
			buttonsList.erase(menu);
			buttonsList.addClass("noscreen");
			buttonsList.removeClass("jpsocial-share-menu-open");
			buttonsList.setStyle("left", null);
			buttonsList.setStyle("top", null);
			menu.removeClass("noscreen");
			menu.addClass("jpsocial-share-menu-open");
			
			var toggPos = togg.getPosition(document.getElement("body"));
			var menuDim = menu.getDimensions();
			menu.position({
				relativeTo: target||togg,
				offset: {x: 5, y: 5}, 
				position: "topRight",
				edge: {x: 'left', y: 'top'},
				//ignoreMargins: true
			});
			//calcolo della finestra
			var scrollAfter =  window.getScrollSize().x;
			if (scrollAfter > scrollBefore) {
				menu.setStyle("left", new Number(menu.getStyle("left").replace("px", ""))-(scrollAfter - scrollBefore) - 20 +"px");
				menu.setStyle("top", new Number(menu.getStyle("top").replace("px", ""))+25+"px");
			}
		};
		
		window.jpsocialHide = function(togg) {
			var menu = togg.retrieve("menu");
			menu.addClass("noscreen");
			menu.removeClass("jpsocial-share-menu-open");
			menu.setStyle("left", null);
			menu.setStyle("top", null);
		};
		
		Array.each(sharesList, function(item, index){
			var buttons = item.getElement(".share-list");
			var toggler = item.getElement(".togg");
			var position = toggler.getPosition();
	
			var sharemenu = new Element("div", {"class": "noscreen jpsocial-share-menu"});
			var title = new Element("h4", {text: toggler.get("title"), "class": "jpsocial-title"});
			title.inject(sharemenu);		
			var ul = new Element("ul").inject(sharemenu);
			
			var buttonsTogglers = buttons.getChildren();
			Array.each(buttonsTogglers, function(sss, index){
				var li = new Element("li");
				li.inject(ul);
				sss.inject(li);
				if (index==buttonsTogglers.length-1) {
					li.addClass("last");
				}
			});
			toggler.store("menu", sharemenu);
			sharemenu.store("togg", toggler);
			sharemenu.inject(toggler.getParent(".contentAttributeBox")); 
	
			toggler.addEvent("click", function(ev){
				ev.preventDefault();
				var menu = this.retrieve("menu");
				if (menu.hasClass("noscreen")) {
					window.jpsocialShow(this, ev.target);
				}
				else {
					window.jpsocialHide(this);
				}
			}.bind(toggler));
			
			window.addEvent("click", function(ev) {
				var menu = document.getElement(".jpsocial-share-menu-open");
				if (menu!=null) {
					var toggler = menu.retrieve("togg");
					if (!menu.contains(ev.target) && (ev.target != menu) && (ev.target != toggler) && !(toggler.contains(ev.target))) {
						window.jpsocialHide(toggler);
					}
				}
			});
			
		});
	});
	//--><!]]></script>
	<s:set var="jpsocialExtrasLoaded" value="%{true}" />
</s:if>

</s:iterator>