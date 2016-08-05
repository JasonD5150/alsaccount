<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags"%>

<fwp:template>

	<fwp:head>
		<sj:head locale="en" jqueryui="true" jquerytheme="smoothness"
			customBasepath="css/jquery" loadAtOnce="true" />
		<script type="text/javascript"
			src="/alsaccount/admin/scripts/fwp.tribalBankCodeMaint.js"></script>
	</fwp:head>

	<form id='tribeBankForm'>
		<s:hidden id="frmBankId" name="tribeID" />
	</form>

	<s:form id="tribeBankItemFrm" theme="simple"
		action="addTribeBankItemDiv_execute">
		<s:hidden id="itemKey" name="itemKey" />
		<s:hidden id="tribeId" name="tribeId" />
		<sj:submit id="addTribeItemToBank" hidden="true" targets="errorDiv" />
	</s:form>

	<div style="text-align:center">
		<h2 class="title">Tribe Bank Code Maintenance</h2>
	</div>

	<s:url id="alsTribeBankCodeDivUrl"
		action="tribeBankCodeDiv_input.action" />
	<sj:div id="alsTribeBankCodeDiv" href="%{alsTribeBankCodeDivUrl}"
		reloadTopics="reloadAlsTribeBankCodeDivUrl" formIds="gridFrm">
	</sj:div>


	<s:url id="alsTribeBankItemDivUrl"
		action="tribeBankItemDiv_input.action" />
	<sj:div id="alsTribeBankItemDiv" href="%{alsTribeBankItemDivUrl}"
		reloadTopics="reloadTribeBankItemDiv" deferredLoading="true">
	</sj:div>

	<s:url id="addItemDialogUrl" action="addTribeBankItemDiv_input.action" />

	<sj:dialog id="addItemDialog" href="%{addItemDialogUrl}"
		autoOpen="false" modal="true" title="Add Item" width="800"
		   
		buttons="{
            'Close': function() {
                $('#addItemDialog').dialog('close');
       	    },
       	    'Add': function() {
       	    	readMultiSelect();
       	    	$('#addTribeItemToBank').trigger('click');
       	    }
        }" />
</fwp:template>
