<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
        <script>
        	/*ACTIONS*/
			function submitSearch(){
				$('#provNo').val($('#provNo_widget').val());
				$('#itemTypeCd').val($('#itemTypeCd_widget').val());
				$('#distributionQueryTable').jqGrid('setGridParam',{datatype:'json'});//.trigger("reloadGrid");
				$.publish('reloadDistributionQueryTable');
			}
			
			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
			
			function exportToCSV(){
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("iafaSummaryTable","iafaSummaryRecords","gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'iafaSummaryQueryBuildCsv.action',
					success: function (data) {
						window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
					}, complete: function () {
						$.unblockUI();
					},
				 	error: function (x, e) {
						ajaxErrorHandler(x, e, "Save", null, null);
					} 
				});
			}
        </script>
    </fwp:head>
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Distribution Query</h2>
   	</div>
	
	<s:url id="searchCriteriaDivUrl" value="distQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			formIds="divFrm"
			reloadTopics="reloadLists">
	</sj:div>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
			<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
			<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
			<s:submit id="getReport" onclick="exportToCSV()" theme="simple" value="Export CSV"></s:submit>
	</fieldset>
  	<br>
  	<br>
	<s:url id="distributionQueryGridURL" action="alsAccount/distributionQueryGrid_buildgrid" />  
	<sjg:grid
		id="distributionQueryTable"
		caption="Distribution Entries"
		href="%{distributionQueryGridURL}"	
		dataType="local"
		pager="true"
		pagerButtons="true"
		pagerInput="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="false"
		scrollrows="false"
		autowidth="false"
	    shrinkToFit="false"
		height="200"
		width="950"
		rowNum="25"
		formIds="gridFrm"
		reloadTopics="reloadDistributionQueryTable"
		onCompleteTopics="distributionQueryTableComplete"
		loadonce="true">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="50" sortable="false" align="right"/>
			<sjg:gridColumn name="asSessionDt" index="asSessionDt" title ="Session Date" width="75" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="itemTypeCode" index="itemTypeCode" title ="Item Type Cd" width="55" sortable="false" align="right"/>
			<sjg:gridColumn name="itemDesc" index="itemDesc" title ="Item Type Desc" width="150" sortable="false"  />
			<sjg:gridColumn name="upFrom" index="upFrom" title ="Usage Period From" width="75" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="upTo" index="upTo" title ="Usage Period To" width="75" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" />
			<sjg:gridColumn name="apiDob" index="apiDob" title ="DOB" width="75" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" />
			<sjg:gridColumn name="apiAlsNo" index="apiAlsNo" title ="ALS No" width="25" sortable="false"  align="right"/>
			<sjg:gridColumn name="aseDrCrCd" index="aseDrCrCd" title ="Dr/Cr Cd" width="25" sortable="false"  />
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="50" sortable="false" align="right"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="50" sortable="false"  align="right"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="50" sortable="false"  />
			<sjg:gridColumn name="aiafaAppType" index="aiafaAppType" title ="App Type" width="50" sortable="false"  />
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="50" sortable="false" align="right"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="50" sortable="false"  />
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="50" sortable="false"  formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
			<sjg:gridColumn name="astRemarks" index="astRemarks" title ="Remarks" width="150" sortable="false"  />
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Cd" width="25" sortable="false"  />
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Txn Cd" width="25" sortable="false" align="right"/>
			<sjg:gridColumn name="aseSeqNo" index="aseSeqNo" title ="Seq No" width="25" sortable="false" align="right"/>
			<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="AIAFA Seq No" width="25" sortable="false" align="right"/>
	</sjg:grid>
</fwp:template>
