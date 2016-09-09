<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
        <script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script>  
	   	<script type="text/javascript" src= "/alsaccount/scripts/exportGrid.js"></script> 
        <style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
        </style>
	   	<script type="text/javascript">
			function setGridFrm(){
		    	$('#frmBudgYear').val($('#budgetYearSel').val());
	    	}
	    	
	    	function reloadGrids(event,data) {
				setGridFrm();
				$.publish('reloadGrids');
			}
			
			//Set edit dialog fields disable when editing for all tabs
		    function setDisabled(id){
				   	$("input[name='idPk.aaccAccCd']").prop({disabled:true});
				   	$("input[name='idPk.aaccSeqNo']").prop({disabled:true});
		    }
		    
		    //Set edit dialog fields enabled when adding for all tabs
		    function setEnabled(id){
				   	$("input[name='idPk.aaccAccCd']").prop({disabled:false});
				   	$("input[name='idPk.aaccSeqNo']").prop({disabled:false});
		    }
		    
		    //Reset budget year select on page load   
			$(document).ready(function(){
		   		//document.getElementById("budgetYearSel").selectedIndex = "0";
		   		//setGridFrm();
			});
	    	
	    	function errorHandler(response, postdata) {
			    rtrnstate = true; 
			    rtrnMsg = ""; 
				json = eval('(' + response.responseText + ')'); 
					if(json.actionErrors) {
						rtrnstate = false; 
					    for(i=0; i < json.actionErrors.length; i++) {
					    	rtrnMsg += json.actionErrors[i] + '<br/>'; 
					    } 
					} 
				return [rtrnstate,rtrnMsg]; 
			};
			
			$.subscribe('accCodeControlComplete', function(event,data) {
				   if ( $("#accCodeControlTable").length) {
			       		$("#accCodeControlTable").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
				   }   
			});
			
			function rtrnAccountList() {
				var rslt = $("#frmAccountLst").val();
				return rslt;
			}
		    
		    function exportToCSV(){
		    	var gridList = ["accCodeControlTable","accCdDistByItemType"];
		    	var dataLabelList = ["accCodeControlEntries","accCdDistByItemTypeEntries"];
		    	
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportMultipleGrids(gridList,dataLabelList,"gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'accountingCodeControlBuildCsv.action',
					success: function (data) {
						window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
					}, complete: function () {
						$('#iafaQueryTable').jqGrid('setGridParam',{datatype:'json'});
						$('#iafaQueryTable').trigger("reloadGrid");
					},
					error: function (x, e) {
						ajaxErrorHandler(x, e, "Save", null, null);
					}
				});
			}
		    
		    function checkForm(postData){
		    	rtrnstate = true; 
				rtrnMsg = ''; 
		  		
	  			postData.budgYear = $('#budgetYearSel').val();
		  		
		  		if(postData.aaccOrgFlag == 'Y'){
		  			if(postData.aocOrg != null){
		  				rtrnstate = false;
						rtrnMsg = 'Org cannot be entered if Multiple Orgs selected as Yes';
		  			}
				}
				if(postData.aaccBalancingAmtFlag == 'Y'){
					if(postData.aaccAllocatedAmt != ''){
						rtrnstate = false;
						rtrnMsg = 'Allocated Amount should not be entered if Balancing Amount Flag is selected as Yes';
					}
				}
				if(postData.aaccBalancingAmtFlag == 'N'){
					if(postData.aaccAllocatedAmt == '' || postData.aaccAllocatedAmt <= 0){
						rtrnstate = false;
						rtrnMsg = 'Allocated Amount must be greater than zero, if Balancing Amount Flag is  selected as No';
					}
				}
				if(postData.aamAccount == '002504' || postData.aamAccount == '002505'){
					if(postData.aaccJlrRequired == 'N'){
						rtrnstate = false;
						rtrnMsg = 'Account Codes 002504 and 002505 require a Open Item Key of Yes.'; 
					}
				}
				return [rtrnstate,rtrnMsg]; 
		    } 
		    
		    function submitSearch(){
		    	$('#accCd').val($('#accCd_widget').val());
		    	$('#itemTypeCd').val($('#itemTypeCd_widget').val());
				$('#accCodeControlTable').jqGrid('setGridParam',{datatype:'json'});
				$('#accCdDistByItemType').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadGrids')
			}

			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
		</script>
    </fwp:head>	
	<form id='rptFrm'>
			<s:hidden id="frmRptType" name="rptType" />
			<s:hidden id="frmFilters" name="filters" />
			<s:hidden id="frmActTypCd" name="actTypCd" />
			<s:hidden id="frmRptBudgYear" name="budgYear" />
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Accounting Code Control Table Maintenance</h2>
   	</div>
    
    <s:url id="accCodeControlQueryDivUrl" action="accCodeControlQueryDiv_input.action"/>
   	<sj:div id="accCodeControlQueryDiv" style='width:800px;'
	  		href="%{accCodeControlQueryDivUrl}" 
	  		reloadTopics="reloadAccCodeControlQueryDiv"
	  		formIds="gridFrm">
	</sj:div>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
		<s:submit id="getActivityAccountLinkageRpt" onclick="exportToCSV()" value="Export CSV" theme="simple"></s:submit>
	</fieldset>
	<br>
	<br>
	<s:url id="accCodeControlGrid" action="alsAccount/accCodeControlGrid_buildgrid" />
	<s:url id="accCodeControlGridEdit" action="alsAccount/accCodeControlGridEdit_execute" />    
	<sjg:grid
		id="accCodeControlTable"
		caption="Account Codes"
		href="%{accCodeControlGrid}"
		editurl="%{accCodeControlGridEdit}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearch="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
    	navigatorAddOptions="{width:500,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  afterShowForm:setEnabled,
    						  beforeSubmit:checkForm,  	    
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterShowForm:setDisabled,
    	                       beforeSubmit:checkForm,	    
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadGrids"
		onBeforeTopics="accCodeControlComplete"
		resizable="true"
		loadonce="true">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacBudgetYear" index="idPk.asacBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="idPk.aaccAccCd" index="idPk.aaccAccCd" title =" Account Code" width="25" sortable="false" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:4,maxlength:3}" align="right"/>
			<sjg:gridColumn name="idPk.aaccSeqNo" index="idPk.aaccSeqNo" title =" Seq No" width="25" sortable="false" hidden="false" editable="false" editrules="{number:true,required:true}" editoptions="{size:3,maxlength:2}" align="right"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title =" Account" width="40" sortable="false" hidden="false" editable="true" editrules="{required:true}" edittype="select" formatter="select" editoptions="{value:','}" align="right"/>
			<sjg:gridColumn name="aaccFund" index="aaccFund" title =" Fund" width="30" sortable="false" hidden="false" editable="true" editrules="{required:true}" align="right"/>
			<sjg:gridColumn name="aaccAllocatedAmt" index="aaccAllocatedAmt" title =" Allocated Amount" width="40" sortable="false" hidden="false" editable="true" align="right"/>
			<sjg:gridColumn name="aaccJlrRequired" index="aaccJlrRequired" title =" Open Item Key Indicator" width="20" sortable="false" hidden="false" editable="true" 
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title =" Org" width="20" sortable="false" hidden="false" editable="true"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title =" Subclass" width="25" sortable="false" hidden="false" editable="true" />
			<sjg:gridColumn name="aaccOrgFlag" index="aaccOrgFlag" title =" Multiple Orgs" width="20" sortable="false" hidden="false" editable="true" 
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aaccBalancingAmtFlag" index="aaccBalancingAmtFlag" title =" Balancing Amt Flag" width="20" sortable="false" hidden="false" editable="true"  
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aaccRemarks" index="aaccRemarks" title =" Remarks" width="100" sortable="false" hidden="false" editable="true" edittype="textarea"/>
	</sjg:grid>	
	<br>
	<s:url id="accCdDistByItemTypeGrid" action="alsAccount/accCdDistByItemTypeGrid_buildgrid" />
	<sjg:grid
		id="accCdDistByItemType"
		caption="Accounting Codes Distribution by Item Type"
		href="%{accCdDistByItemTypeGrid}"
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
    	gridModel="acdbitModel"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="1000"
		resizable="true"
		formIds="gridFrm"
		reloadTopics="reloadGrids"
		loadonce="true"
		shrinkToFit="false">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="itemTypeCd" index="itemTypeCd" title ="Item Type" width="55" sortable="false" align="right"/>
			<sjg:gridColumn name="itemTypeDesc" index="itemTypeDesc" title ="Item Description" width="150" sortable="false"/>
			<sjg:gridColumn name="upFromDt" index="upFromDt" title ="Usage Period From" width="70" sortable="false" />
			<sjg:gridColumn name="upToDt" index="upToDt" title ="Usage Period To" width="70" sortable="false" />
			<sjg:gridColumn name="itemCost" index="itemCost" title ="Item Cost" width="45" sortable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="costPrereq" index="costPrereq" title ="Cost Prerequisite Code" width="25" sortable="false" align="right"/>
			<sjg:gridColumn name="costPrereqDesc" index="costPrereqDesc" title ="Cost Prerequisite Description" width="150" sortable="false"/>
			<sjg:gridColumn name="residency" index="residency" title ="Residency" width="25" sortable="false"/>
			<sjg:gridColumn name="budgYear" index="budgYear" title ="Budget Year" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="partialCost" index="partialCost" title ="Partial Cost" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="drawFee" index="drawFee" title ="Drawing Fee" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="accCd" index="accCd" title ="Accounting Code" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="account" index="account" title ="Account" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="fund" index="fund" title ="Fund" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="balancing" index="balancing" title ="Balancing" width="25" sortable="false"/>
			<sjg:gridColumn name="dist" index="dist" title ="Distribution" width="45" sortable="false" align="right" formatter="currency" formatoptions="{decimalPlaces:2}"/>
			<sjg:gridColumn name="subclass" index="subclass" title ="Subclass" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="org" index="org" title ="Org" width="45" sortable="false" align="right"/>
			<sjg:gridColumn name="orgMult" index="orgMult" title ="Org Multiple" width="45" sortable="false" align="right"/>
			
	</sjg:grid>							
</fwp:template>
