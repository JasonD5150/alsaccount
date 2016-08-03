<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template>
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
	   	<script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script>  
	   	<script type="text/javascript" src= "/alsaccount/scripts/exportGrid.js"></script>  
	   	<script>
	   		$.subscribe("alsSabhrsQueryTableComplete", function (event, data) {
	   		
	   		var grid = $('#alsSabhrsQueryTable');
			             
             $("#alsSabhrsQueryTable")
				.jqGrid({pager:'#alsSabhrsQueryTable_pager'})
				.jqGrid('navButtonAdd'
				,'#alsSabhrsQueryTable_pager'
				,{id:"columnSelector_alsSabhrsQueryTable"
				,caption:""
				,buttonicon:"ui-icon-extlink"
				,onClickButton:function(){ 
					grid.jqGrid('columnChooser');
				}
				,position:"last"
				,title:"Add/Remove Columns"
				,cursor:"pointer"
				});

			});
			
			function exportToCSV(){
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("alsSabhrsQueryTable","sabhrsEntries")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'sabhrsQueryBuildCsv.action',
					success: function (data) {
						window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
					}, complete: function () {
						$('#alsSabhrsQueryTable').jqGrid('setGridParam',{datatype:'json'});
						$('#alsSabhrsQueryTable').trigger("reloadGrid");
					},
					error: function (x, e) {
						ajaxErrorHandler(x, e, "Save", null, null);
					}
				});
			}
			
			function submitSearch(){
				var search = true;
				if(($('#seqNo').val() != null && $('#seqNo').val() != "" )&& 
					(($('#providerNo').val() == null || $('#providerNo').val() == "") || 
					($('#bpFromDt').val() == null || $('#bpFromDt').val() == "")|| 
					($('#bpToDt').val() == null || $('#bpToDt').val() == ""))){
					search = false;
					alert("To search by IAFA Seq No you must also enter a Provider No, Billing Period From and Billing Period To");
				}
				if(search){
					$('#alsSabhrsQueryTable').jqGrid('setGridParam',{datatype:'json'});
					$.publish('reloadAlsSabhrsEntriesTable')
				}
			}
			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
			
			function reloadLists(){
				$('#frmBudgYear').val($('#budgYear').val());
				$.publish('reloadLists');
			}

	   	</script>
    </fwp:head>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">SABHRS Query</h2>
   	</div>
	
	<form id='divFrm'>
		<s:hidden id="frmBudgYear" name="budgYear"/>
	</form>
	<s:url id="searchCriteriaDivUrl" value="sabhrsQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			formIds="divFrm"
			reloadTopics="reloadLists">
	</sj:div>
	
  	<br>
  	<br>
	<s:url id="alsSabhrsQueryGridURL" action="alsAccount/alsSabhrsQueryGrid_buildgrid" />  
	<sjg:grid
		id="alsSabhrsQueryTable"
		caption="SABHRS Entries"
		href="%{alsSabhrsQueryGridURL}"	
		dataType="json"
		pager="true"
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
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadAlsSabhrsEntriesTable"
		onCompleteTopics="alsSabhrsQueryTableComplete"
		loadonce="true">
			<!-- DEFAULT COLUMNS -->
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="jlr" index="jlr" title ="JLR" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Cd" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="aseDrCrCd" index="aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}"/>
			<sjg:gridColumn name="aseSeqNo" index="aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true" edittype="textarea" />
			
			<!-- HIDDEN COLUMNS-->
			<sjg:gridColumn name="aseWhenEntryPosted" index="aseWhenEntryPosted" title ="When Entry Posted" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aseAllowUploadToSummary" index="aseAllowUploadToSummary" title ="Allow Upload To Summary" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="upToSummDt" index="upToSummDt" title ="When Uploaded To Summary" width="40" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="asesSeqNo" index="asesSeqNo" title ="Seq No" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title ="Provider No" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="bpFromDt" index="bpFromDt" title ="Billing From" width="40" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="bpToDt" index="bpToDt" title ="Billing To" width="40" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="AIAFA Seq No" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="atgTransactionCd" index="atgTransactionCd" title ="Transaction Type Cd" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="atgsGroupIdentifier" index="atgsGroupIdentifier" title ="Group Identifier" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aseNonAlsFlag" index="aseNonAlsFlag" title ="Non Als Flag" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="atiTribeCd" index="atiTribeCd" title ="Tribe Cd" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="anatCd" index="anatCd" title ="ANAT Cd" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="sumStat" index="sumStat" title ="Summary Status" width="40" sortable="false" editable="true" hidden="true" formatter="select" editoptions="{value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}"/>
			<sjg:gridColumn name="intStat" index="intStat" title ="Interface Status" width="40" sortable="false" editable="true" hidden="true" formatter="select" editoptions="{value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}"/>
	
	</sjg:grid>
	<br>
	<s:submit id="getReport" onclick="exportToCSV()" theme="simple" value="Export CSV"></s:submit>
</fwp:template>