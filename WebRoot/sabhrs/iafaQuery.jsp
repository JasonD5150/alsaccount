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
	   		$.subscribe("iafaQueryTableComplete", function (event, data) {
					var grid = $('#iafaQueryTable');
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
	    		 
					grid
					.jqGrid({pager:'#iafaQueryTable_pager'})
					.jqGrid('navButtonAdd'
					,'#iafaQueryTable_pager'
					,{id:"columnSelector_iafaQueryTable"
					,caption:""
					,buttonicon:"ui-icon-extlink"
					,onClickButton:function(){ 
						grid.jqGrid('columnChooser',{caption: "Columns: CTRL-click select/deselect a column, CTRL-A select all",
													 width: 500});
					}
					,position:"last"
					,title:"Add/Remove Columns"
					,cursor:"pointer"
					});
			});
			
			function exportToCSV(){
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("iafaQueryTable","iafaEntries","gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'iafaQueryBuildCsv.action',
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
			
			function submitSearch(){
				$('#issProvNo').val($('#issProvNo_widget').val());
				$('#entProvNo').val($('#entProvNo_widget').val());
				$('#itemTypeCd').val($('#itemTypeCd_widget').val());
				$('#itemInd').val($('#itemInd_widget').val());
				$('#itemStat').val($('#itemStat_widget').val());
				$('#itemCatCd').val($('#itemCatCd_widget').val());
				$('#procCatCd').val($('#procCatCd_widget').val());
				$('#procTypeCd').val($('#procTypeCd_widget').val());
				$('#tribeCd').val($('#tribeCd_widget').val());
				$('#appDis').val($('#appDis_widget').val());
				$('#amountTypeCd').val($('#amountTypeCd_widget').val());
				$('#costPrereqCd').val($('#costPrereqCd_widget').val());
				$('#reasonCd').val($('#reasonCd_widget').val());
				var search = true;
				if($('#fromDt').val() == null || $('#fromDt').val() == ''){
					search = false;
					alert("From Date is required.");
				}else if($('#toDt').val() == null || $('#toDt').val() == ''){
					search = false;
					alert("To Date is required.");
				}
				if(search){
					$('#iafaQueryTable').jqGrid('setGridParam',{datatype:'json'});
					$.publish('reloadIafaQueryTable');
				}
			}
			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
			

	   	</script>
	   	<style>
	   		.label {
			   text-align: right;
			}
	   	</style>
    </fwp:head>
	
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">IAFA Session Query</h2>
   	</div>	
	<s:actionerror/>
	<s:url id="searchCriteriaDivUrl" value="iafaQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			formIds="divFrm"
			reloadTopics="reloadLists"
			onCompleteTopics="searchDivComplete">
	</sj:div>
	
  	<br>
  	<br>
	<s:url id="iafaQueryGridURL" action="alsAccount/iafaQueryGrid_buildgrid" />  
	<sjg:grid
		id="iafaQueryTable"
		caption="IAFA Session"
		href="%{iafaQueryGridURL}"	
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
		shrinkToFit="false"
        autowidth="true"
        resizable="true"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadIafaQueryTable"
		onCompleteTopics="iafaQueryTableComplete"
		loadonce="true">
			<!-- DEFAULT COLUMNS -->
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title ="Issuing Provider No" width="40" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="apiBusinessNm" index="apiBusinessNm" title ="Issuing Provider Name" width="150" sortable="false" editable="true"/>
			<sjg:gridColumn name="apiAlxProvInd" index="apiAlxProvInd" title ="ALX Provider Indicator" width="40" sortable="false" editable="true"/>
			<sjg:gridColumn name="asDataEntryProviderNo" index="asDataEntryProviderNo" title ="Data Entry Provider No" width="40" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="aprBillingFrom" index="aprBillingFrom" title ="Billing Period From" width="70" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aprBillingTo" index="aprBillingTo" title ="Billing Period To" width="70" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="IAFA Seq No" width="40" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="aiafaStatus" index="aiafaStatus" title ="Session Status" width="50" sortable="false" editable="true"/>
			<!-- HIDDEN COLUMNS-->
			<sjg:gridColumn name="aictUsagePeriodFrom" index="aictUsagePeriodFrom" title ="Usage Period From" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aictUsagePeriodTo" index="aictUsagePeriodTo" title ="Usage Period To" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aictItemTypeCd" index="aictItemTypeCd" title ="Item Type Code" width="50" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="aitTypeDesc" index="aitTypeDesc" title ="Item Type Description" width="200" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aiafaAmt" index="aiafaAmt" title ="Amount" width="40" sortable="false" editable="true" hidden="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="astNochargeReason" index="astNochargeReason" title ="No Charge Reason" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aiinItemIndCd" index="aiinItemIndCd" title ="Item Indicator" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="itemIndDesc" index="itemIndDesc" title ="Item Indicator Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aisItemStatusCd" index="aisItemStatusCd" title ="Item Status" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="itemStatusDesc" index="itemStatusDesc" title ="Item Status Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="itemCatCd" index="itemCatCd" title ="Item Catagory Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="aicCategoryDesc" index="aicCategoryDesc" title ="Item Category Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aaiDispositionCd" index="aaiDispositionCd" title ="Application Disposition" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="appDispositionDesc" index="appDispositionDesc" title ="Application Disposition Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aaiBonusPointsInd" index="aaiBonusPointsInd" title ="Bonus Points" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aiiItemTxnInd" index="aiiItemTxnInd" title ="ItemTcnInd" width="40" sortable="false" editable="true" hidden="true" edittype="select" formatter="select" editoptions="{value: {I: 'Item', A: 'Application'}}"/>
			<sjg:gridColumn name="aiiSeqNo" index="aiiSeqNo" title ="Seq No Within Item Transaction" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="dob" index="dob" title ="Date of Birth" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="alsNo" index="alsNo" title ="ALS No" width="40" sortable="false" editable="true" hidden="true" align="right"/>			
			<sjg:gridColumn name="aiafaAmtType" index="aiafaAmtType" title ="Amount Type" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="amtTypeDesc" index="amtTypeDesc" title ="Amount Type Description" width="150" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="itemResidency" index="itemResidency" title ="Residency" width="40" sortable="false" editable="true" hidden="true" edittype="select" formatter="select" editoptions="{value: {R: 'Residents', O: 'Other'}, defaultValue:'O'}"/>
			<sjg:gridColumn name="acdCostGroupSeqNo" index="acdCostGroupSeqNo" title ="Cost Group Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="costGrpDesc" index="costGrpDesc" title ="Cost Group Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="apcPrerequisiteCostCd" index="apcPrerequisiteCostCd" title ="Prerequisite Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="prereqDesc" index="prereqDesc" title ="Prerequisite Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aiafaProcessCategoryCd" index="aiafaProcessCategoryCd" title ="Process Category Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="processCatDesc" index="processCatDesc" title ="Process Category Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="astProcessTypeCd" index="astProcessTypeCd" title ="Process Type Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="processTypeDesc" index="processTypeDesc" title ="Process Type Description" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="abiReconciledOn" index="abiReconciledOn" title ="Batch Reconcilation Date" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="abiBatchNO" index="abiBatchNO" title ="Batch No" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="asiSubatchNo" index="asiSubatchNo" title ="Sub-Batch No" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="reasonType" index="reasonType" title ="Reason Type" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="aiafaReasonCd" index="aiafaReasonCd" title ="Reason Code" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="reasonDesc" index="reasonDesc" title ="Reason" width="100" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="atiTribeCd" index="atiTribeCd" title ="Tribe Code" width="40" sortable="false" editable="true" hidden="true"/>            
			<sjg:gridColumn name="atgsGroupIdentifier" index="atgsGroupIdentifier" title ="Transaction Group Identifier" width="200" sortable="false" editable="true" hidden="true"/>    
			<sjg:gridColumn name="otherTxnGrp" index="otherTxnGrp" title ="Other Transaction Group" width="200" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="ahmType" index="ahmType" title ="Hardware Type" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="ahmCd" index="ahmCd" title ="Hardware Code (Device No)" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="sessionDt" index="sessionDt" title ="Session Date" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y h:m:s' }"/>
			<sjg:gridColumn name="sessionOrigin" index="sessionOrigin" title ="Session Origin" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="asSessionVoidDt" index="asSessionVoidDt" title ="Session Void Date" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/> 
			<sjg:gridColumn name="aiafaRecordVoidDt" index="aiafaRecordVoidDt" title ="Record Void Date" width="70" sortable="false" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/> 
			<sjg:gridColumn name="seqNoforPrintedItems" index="seqNoforPrintedItems" title ="Seq No for Printed Items" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="sessionTotal" index="sessionTotal" title ="Session Total" width="40" sortable="false" editable="true" hidden="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="asModePayment" index="asModePayment" title ="Mode of Payment" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="asCheckNo" index="asCheckNo" title ="Check No" width="40" sortable="false" editable="true" hidden="true" align="right"/>
			<sjg:gridColumn name="asCheckWriter" index="asCheckWriter" title ="Check Writer" width="40" sortable="false" editable="true" hidden="true"/>
			<sjg:gridColumn name="atgsSummaryStatus" index="atgsSummaryStatus" title ="Summary Approval Status" width="70" sortable="false" editable="true" formatter="select" editoptions="{value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}" hidden="true"/>   
			<sjg:gridColumn name="atgsInterfaceStatus" index="atgsInterfaceStatus" title ="Interface Approval Status" width="70" sortable="false" editable="true" formatter="select" editoptions="{value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}" hidden="true"/>
			<sjg:gridColumn name="aiafaRemarks" index="aiafaRemarks" title ="Remarks" width="100" sortable="false" editable="true" hidden="true"/>
	
	</sjg:grid>
	<br>
	<s:submit id="getReport" onclick="exportToCSV()" theme="simple" value="Export CSV"></s:submit>
</fwp:template>
