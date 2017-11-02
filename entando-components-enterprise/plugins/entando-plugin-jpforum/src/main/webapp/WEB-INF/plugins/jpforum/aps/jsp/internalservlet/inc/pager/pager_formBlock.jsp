<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#group.size > #group.max">
	<s:if test="%{1 == #group.currItem}">
		<s:set var="goFirst"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="goFirst_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="goFirst"><wp:resourceURL />administration/img/icons/go-first.png</s:set>
		<s:set var="goFirst_class">pager_item</s:set>
	</s:else>

	<s:if test="%{1 == #group.beginItemAnchor}">
		<s:set var="jumpBackward"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="jumpBackward_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="jumpBackward"><wp:resourceURL />administration/img/icons/go-jump-backward.png</s:set>
		<s:set var="jumpBackward_class">pager_item</s:set>
	</s:else>
	<s:if test="%{1 == #group.currItem}">
		<s:set var="goPrevious"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="goPrevious_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="goPrevious"><wp:resourceURL />administration/img/icons/previous.png</s:set>
		<s:set var="goPrevious_class">pager_item</s:set>
	</s:else>
	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set var="goNext"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="goNext_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="goNext"><wp:resourceURL />administration/img/icons/next.png</s:set>
		<s:set var="goNext_class">pager_item</s:set>
	</s:else>
	<s:if test="%{#group.maxItem == #group.endItemAnchor}">
		<s:set var="jumpForward"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="jumpForward_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="jumpForward"><wp:resourceURL />administration/img/icons/go-jump-forward.png</s:set>
		<s:set var="jumpForward_class">pager_item</s:set>
	</s:else>
	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set var="goLast"><wp:resourceURL />plugins/jpforum/static/img/pager/transparent.png</s:set>
		<s:set var="goLast_class">noscreen</s:set>
	</s:if>
	<s:else>
		<s:set var="goLast"><wp:resourceURL />administration/img/icons/go-last.png</s:set>
		<s:set var="goLast_class">pager_item</s:set>
	</s:else>
 	<%--
 		label.goToFirst =Back to start
 		label.jump =Jump
		label.backward =backward
		label.forward =forward 	
		label.prev =Prev
		label.prev.full =Previous
		label.next =Next
		label.next.full =Next	
		label.goToLast= Go to the last
		label.goToPage = Go to page:
 	--%>
	<s:set var="goToFirst"><wp:i18n key="jpforum_PAGER_GO_TO_FIRST" /></s:set>
	<s:set var="goToFirstShort"><wp:i18n key="jpforum_PAGER_GO_TO_FIRST_SHORT" /></s:set>
	<s:set var="jump"><wp:i18n key="jpforum_PAGER_JUMP" /></s:set>
	<s:set var="backward"><wp:i18n key="jpforum_PAGER_BACKWARD" /></s:set>
	<s:set var="forward"><wp:i18n key="jpforum_PAGER_FORWARD" /></s:set>
	<s:set var="prev"><wp:i18n key="jpforum_PAGER_PREV" /></s:set>
	<s:set var="prevFull"><wp:i18n key="jpforum_PAGER_PREV_FULL" /></s:set>
	<s:set var="next"><wp:i18n key="jpforum_PAGER_NEXT" /></s:set>
	<s:set var="nextFull"><wp:i18n key="jpforum_PAGER_NEXT_FULL" /></s:set>
	<s:set var="goToLast"><wp:i18n key="jpforum_PAGER_GO_TO_LAST" /></s:set>
	<s:set var="goToLastShort"><wp:i18n key="jpforum_PAGER_GO_TO_LAST_SHORT" /></s:set>
	<s:set var="goToPage"><wp:i18n key="jpforum_PAGER_GO_TO_PAGE" /></s:set>
	<p class="jpforum_pager_items"> 
		<s:if test="#group.advanced">
			<wpsf:submit useTabindexAutoIncrement="true" 
				 
				name="pagerItem_1" 
				value="%{#goToFirstShort}"
				title="%{#goToFirst}" 
				src="%{#goFirst}" 
				cssClass="%{#goFirst_class}" 
				disabled="%{1 == #group.currItem}" />
			
			<wpsf:submit useTabindexAutoIncrement="true" 
				 
				name="%{'pagerItem_' + (#group.currItem - #group.offset) }" 
				value="%{#jump + ' ' + #group.offset + ' ' + #backward}" 
				title="%{#jump + ' ' + #group.offset + ' ' + #backward}" 
				src="%{#jumpBackward}" 
				cssClass="%{#jumpBackward_class}" 
				disabled="%{1 == #group.beginItemAnchor}" />
		</s:if>	
		<wpsf:submit useTabindexAutoIncrement="true" 
			 
			name="%{'pagerItem_' + #group.prevItem}" 
			value="%{#prev}" 
			title="%{#prevFull}" 
			src="%{#goPrevious}" 
			cssClass="%{#goPrevious_class}" 
			disabled="%{1 == #group.currItem}" />
		<s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
			<s:iterator id="item">
				<wpsf:submit useTabindexAutoIncrement="true" 
					name="%{'pagerItem_' + #item}" 
					value="%{#item}"
					title="%{#goToPage + ': ' + #item}"
					disabled="%{#item == #group.currItem}" 
					cssClass="pager_item pager_item_text pager_item_currentpage_%{#item == #group.currItem}" />
			</s:iterator>
		</s:subset>
		<wpsf:submit useTabindexAutoIncrement="true" 
			 
			name="%{'pagerItem_' + #group.nextItem}" 
			value="%{#next}" 
			title="%{#nextFull}" 
			src="%{#goNext}" 
			cssClass="%{#goNext_class}" 
			disabled="%{#group.maxItem == #group.currItem}" />
		<s:if test="#group.advanced">
			<s:set name="jumpForwardStep" value="#group.currItem + #group.offset"></s:set>
			<wpsf:submit useTabindexAutoIncrement="true" 
				name="%{'pagerItem_' + (#jumpForwardStep)}" 
				value="%{#jump + ' ' + #group.offset + ' ' + #forward}" 
				title="%{#jump + ' ' + #group.offset + ' ' + #forward}" 
				src="%{#jumpForward}" 
				cssClass="%{#jumpForward_class}" 
				disabled="%{#group.maxItem == #group.endItemAnchor}" />
			<wpsf:submit useTabindexAutoIncrement="true" 
				name="%{'pagerItem_' + #group.size}" 
				value="%{#goToLastShort}" 
				title="%{#goToLast}" 
				src="%{#goLast}" 
				cssClass="%{#goLast_class}" 
				disabled="%{#group.maxItem == #group.currItem}" />
		</s:if>
	</p>
</s:if>