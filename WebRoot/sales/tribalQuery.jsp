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
	   		$.subscribe("tribalSalesQueryTableComplete", function (event, data) {
					var grid = $('#tribalSalesQueryTable');
					var error = grid.jqGrid('getGridParam', 'userData');
		    		
		    		if (error != null && error.length > 0) {
		    			$("#errorMessage").html(error);
						$("#errorMessage").dialog({
							title:"Search Error",
						    resizable: false,
						    height:"auto",
						    modal: true,
						    buttons: {
				                "Ok": function() {
				                    $(this).dialog("close");
				                 }
				             }
						});
		    		}
			});
			
			function exportToCSV(){
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("tribalSalesQueryTable","tribalSalesEntries","gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'tribalSalesQueryBuildCsv.action',
					success: function (data) {
						window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
					}, complete: function () {
						$.unblockUI();
						//$('#tribalSalesQueryTable').jqGrid('setGridParam',{datatype:'json'});
						//$('#tribalSalesQueryTable').trigger("reloadGrid");
					},
					error: function (x, e) {
						ajaxErrorHandler(x, e, "Save", null, null);
					}
				});
			}
			
			function submitSearch(){
				$('#tribeCd').val($('#tribeCd_widget').val());
				$('#provNo').val($('#provNo_widget').val());
				$('#itemTypeCd').val($('#itemTypeCd_widget').val());
				$('#usagePeriod').val($('#usagePeriod_widget').val());
				
				
				$('#tribalSalesQueryTable').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadTribalSalesQueryTable');
			}
			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
	   	</script>
    </fwp:head>
	
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Tribal Sales Query/Report</h2>
   	</div>
	<s:actionerror/>
	<s:url id="searchCriteriaDivUrl" value="tribalQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			reloadTopics="reloadLists"
			onCompleteTopics="searchDivComplete">
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
	<s:url id="tribalSalesQueryGridURL" action="alsAccount/tribalSalesGrid_buildgrid" />  
	<sjg:grid
		id="tribalSalesQueryTable"
		caption="Tribal Sales"
		href="%{tribalSalesQueryGridURL}"	
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
		height="100"
		width="950"
        resizable="true"
		rowNum="25"
		formIds="gridFrm"
		reloadTopics="reloadTribalSalesQueryTable"
		onCompleteTopics="tribalSalesQueryTableComplete"
		loadonce="true">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="tribeCd" index="tribeCd" title ="Tribe Code" width="40" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="tribeNm" index="tribeNm" title ="Tribe Name" width="150" sortable="false" editable="true"/>
			<sjg:gridColumn name="eftDepDeadline" index="eftDepDeadline" title ="EFT Deposit Deadline" width="40" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="dueTo" index="dueTo" title ="Due To" width="40" sortable="false" editable="true" />
			<sjg:gridColumn name="bpEndDt" index="bpEndDt" title ="Billing Period End" width="70" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="provNo" index="provNo" title ="Provider No" width="70" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="provNm" index="provNm" title ="Provider Name" width="40" sortable="false" editable="true"/>
			<sjg:gridColumn name="itemTypeCd" index="itemTypeCd" title ="Item Type" width="50" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="itemTypeDesc" index="itemTypeDesc" title ="Item Type Desc" width="50" sortable="false" editable="true" hidden="false" align="right"/>
			<sjg:gridColumn name="salesCount" index="salesCount" title ="Sales Count" width="40" sortable="false" editable="true" hidden="false" align="right"/>
			<sjg:gridColumn name="salesAmt" index="salesAmt" title ="Sales Amount" width="70" sortable="false" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="adjEntries" index="adjEntries" title ="Adjusted Entries" width="70" sortable="false" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="netAmt" index="netAmt" title ="Net Amount" width="70" sortable="false" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
	
	</sjg:grid>
</fwp:template>
