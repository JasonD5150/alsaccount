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
        $.subscribe("iafaRecordSelected", function(event, data) {	
        	$('#revError').html('');
			/*LOAD SUBGRIDS*/
			var grid = $('#iafaGrid');
			var sel_id = grid.jqGrid('getGridParam','selrow');

			$('#frmProvNo').val(grid.jqGrid('getCell', sel_id, 'apiProviderNo'));
		    $('#frmBPFrom').val(grid.jqGrid('getCell', sel_id, 'billingFrom'));
			$('#frmBPTo').val(grid.jqGrid('getCell', sel_id, 'billingTo'));
			$('#frmIafaSeqNo').val(grid.jqGrid('getCell', sel_id, 'aiafaSeqNo'));
			
			$('#alsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
			$.publish('reloadAlsSabhrsEntriesGrid');
			$('#alsSabhrsEntriesGrid_pager_left').show();
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
		}
        
        	/*ACTIONS*/
			function submitSearch(){
				$('#revError').html('');
				$('#alsSabhrsEntriesGrid_pager_left').hide();
				$('#tribeCd').val($('#tribeCd_widget').val());
				$('#appTypeCd').val($('#appTypeCd_widget').val());
				$('#amtTypeCd').val($('#amtTypeCd_widget').val());
				$('#reasonCd').val($('#reasonCd_widget').val());
				$('#iafaGrid').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadIafaGrid');
			}
			
			function resetSearch(){
				$('#revError').html('');
				$('#gridFrm')[0].reset();
			}
			
			function exportToCSV(){
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("iafaGrid","iafaRecords","gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'provAdjEntGridToCsv.action',
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
			
			$.subscribe('alsSabhrsEntriesComplete', function(event, data) {
				$("#alsSabhrsEntriesGrid")
				.jqGrid({pager:'#alsSabhrsEntriesGrid_pager'})
				.jqGrid('navButtonAdd'
				,'#alsSabhrsEntriesGrid_pager'
				,{id:"genReversals_alsSabhrsEntriesGrid"
				,caption:"Reverse ALS SABHRS ENTRIES"
				,buttonicon:"ui-icon-circle-minus"
				,onClickButton:function(){ 
					$('#frmOper').val('reverseAlsEntries');
			
					url = "alsAccount/provAdjEntSABHRSGridEdit_execute.action";    
					$.ajax({
				      type: "POST",
				      url: url,
				      dataType: "json",
				      data: $('#subGridFrm').serialize(),
				      success: function(result){
				          if(result.actionErrors){
				          	$('#revError').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
				          }else{
				          	$('#revError').html('');
							$.publish('reloadAlsSabhrsEntriesGrid');
				          }
				     }
				    });
				}
				,position:"last"
				,title:"Reverse Entries"
				,cursor:"pointer"
				});	
				if ( $("#alsSabhrsEntriesGrid").length) {
			   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','asacReference', { editoptions: { value: rtrnJLRList()}});
			   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aamFund', { editoptions: { value: rtrnFundList()}});
			   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','asacSubclass', { editoptions: { value: rtrnSubClassList()}});
			   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aocOrg', { editoptions: { value: rtrnOrgList()}});
			   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
				}
			});
			
			function rtrnAccountList() {return $("#accountLst").val();}
			function rtrnOrgList() {return $("#orgLst").val();}
			function rtrnJLRList() {return $("#jlrLst").val();}
			function rtrnFundList() {return $("#fundLst").val();}
			function rtrnSubClassList() {return $("#subClassLst").val();}  
        </script>
    </fwp:head>
    <s:hidden id="fundLst" name="fundLst"/>
	<s:hidden id="subClassLst" name="subClassLst"/>
	<s:hidden id="jlrLst" name="jlrLst"/>
	<s:hidden id="orgLst" name="orgLst"/>
	<s:hidden id="accountLst" name="accountLst"/>

	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Provider Adjusting Entry Posting</h2>
   	</div>
	
	<s:url id="searchCriteriaDivUrl" value="provAdjEntQueryDiv_input.action" />
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
  	<s:url id="provAdjEntIAFAGridURL" action="alsAccount/provAdjEntIAFAGrid_buildgrid" /> 
	<sjg:grid
		id="iafaGrid"
		caption="IAFA Table"
		href="%{provAdjEntIAFAGridURL}"		
		dataType="local"
		pager="true"
		navigator="false"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="910"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadIafaGrid"
		onSelectRowTopics="iafaRecordSelected"
		loadonce="true">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title ="Issuing Provider No" width="10" sortable="false" align="right"/>
			<sjg:gridColumn name="billingFrom" index="billingFrom" title ="Billing Period From" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="billingTo" index="billingTo" title ="Billing Period To" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="IAFA Seq No" width="10" sortable="false" align="right"/>	
			<sjg:gridColumn name="itemTypeCd" index="itemTypeCd" title ="Item Type Cd" width="10" sortable="false" align="right"/>	
			<sjg:gridColumn name="itemTypeDesc" index="itemTypeDesc" title ="Item Type Desc" width="25" sortable="false"/>	
	
	</sjg:grid>
	<br>
	<br>
	<div id ='revErrorDiv'>
		<span id='revError'></span>
	</div>
	<form id="subGridFrm">
		<s:hidden id="frmOper" name="oper"/>
		<s:hidden id="frmProvNo" name="provNo"/>
		<s:hidden id="frmBPFrom" name="bpFrom"/>
		<s:hidden id="frmBPTo" name="bpTo"/>
		<s:hidden id="frmIafaSeqNo" name="iafaSeqNo"/>
		<s:hidden id="frmRemittanceInd" name="remittanceInd" value="false"/>
	</form>
	<s:url id="provAdjEntSABHRSGridURL" action="alsAccount/provAdjEntSABHRSGrid_buildgrid" />
	<s:url id="provAdjEntSABHRSGridEditURL" action="alsAccount/provAdjEntSABHRSGridEdit_execute" />    
	<sjg:grid
		id="alsSabhrsEntriesGrid"
		caption="SABHRS Details"
		href="%{provAdjEntSABHRSGridURL}"
		editurl="%{provAdjEntSABHRSGridEditURL}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="false"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorAddOptions="{width:950,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
		navigatorEditOptions="{width:950,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',    
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'
    	                      }"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="910"
		rowNum="10000"
		formIds="subGridFrm"
		reloadTopics="reloadAlsSabhrsEntriesGrid"
		onCompleteTopics="alsSabhrsEntriesComplete">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:5}" align="right"/>
			<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:2,rowpos:2}"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:3}"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:2,rowpos:3}" align="right"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:4}"/>
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:2,rowpos:4}" align="right"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:5}"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:2}" align="right"/>
			<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:6}"/>
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:6}" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right" editrules="{number:true,required:true}"/>
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="false" align="right"/>
			<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}" editrules="{required:true}" formoptions="{colpos:1,rowpos:7}"/>
			<sjg:gridColumn name="idPk.aseSeqNo" index="aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false" align="right"/>
			<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true" formoptions="{colpos:2,rowpos:7}" edittype="textarea" editrules="{required:true}"/>
			
	</sjg:grid>
</fwp:template>
