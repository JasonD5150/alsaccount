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

			$(document).ready(function() {
				resetSearch();
			});
	   	</script>
    </fwp:head>
    
    <s:hidden id="fundLst" name="fundLst"/>
	<s:hidden id="subClassLst" name="subClassLst"/>
	<s:hidden id="jlrLst" name="jlrLst"/>
	<s:hidden id="projectGrantLst" name="projectGrantLst"/>
	<s:hidden id="orgLst" name="orgLst"/>
	<s:hidden id="accountLst" name="accountLst"/>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">SABHRS Query</h2>
   	</div>
	
	<s:url id="sabhrsSelectLstUrl" action="sabhrsSelectLst" /> 
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
    	<s:actionerror/>
    	<form id='gridFrm'>
    		<table>
    			<tr>			
					<td class="label">Provider No: </td>
					<td><s:select 
				  			id="providerNo"
				  			name="providerNo"
				  			list="providerLst"
				  			listKey="itemVal" 
				  			listValue="itemLabel" 
			       			headerKey=""
							headerValue=" " 
			       			theme="simple"
			       			/></td>
					<td class="label">IAFA Seq No: </td><!-- as long as it is tied to Provider No and Billing Period From/To -->
					<td><s:textfield id="seqNo" name="seqNo" theme="simple" title="IAFA Sequence No" /></td>
				</tr>
				<tr>
	    			<td>Billing Period From Date</td>
		    		<td><sj:datepicker changeMonth="true" changeYear="true" id="bpFromDt"
					   name="bpFromDt" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
					   showOn="focus" onblur="testDate(this)" /></td>
				   	<td>Billing Period To Date</td>
		    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
					   name="bpToDt" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
					   showOn="focus" onblur="testDate(this)" /></td>
	    		</tr>
	    		<tr><td colspan="4" style="border-bottom:1px solid black;"><br></td></tr>
	    		<tr><td><br></td></tr>
	    		<tr>
	    			<td>From Date</td>
		    		<td><sj:datepicker changeMonth="true" changeYear="true" id="fromDt"
					   name="fromDt" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="From Date" 
					   showOn="focus" onblur="testDate(this)"/></td>
				   	<td>To Date</td>
		    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="toDt"
					   name="toDt" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="To Date" 
					   showOn="focus" onblur="testDate(this)"/></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Journal Line Reference: </td>
	    			<td><s:select 
	    					id="jlr"
							name="jlr"
							list="jlrLst"
							listKey="itemVal" 
					  		listValue="itemLabel" 
				       		headerKey=""
							headerValue=" " 
				       		theme="simple"/></td>
	    			<td class="label">Account: </td>
	    			<td><s:textfield id="account" name="account" theme="simple" title="Account" /></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Fund: </td>
	    			<td><s:textfield id="fund" name="fund" theme="simple" title="Fund" /></td>
	    			<td class="label">Org: </td>
	    			<td><s:textfield id="org" name="org" theme="simple" title="Org" /></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Subclass: </td>
	    			<td><s:textfield id="subClass" name="subClass" theme="simple" title="Sub Class" /></td>
	    			<td class="label">Tribe Code: </td>
	    			<td><s:textfield id="tribeCd" name="tribeCd" theme="simple" title="Tribe Code" /></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Budget Year: </td>
	    			<td><s:textfield id="budgYear" name="budgYear" theme="simple" title="Budget Year" /></td>
	    			<td class="label">Program Year: </td>
	    			<td><s:textfield id="progYear" name="progYear" theme="simple" title="Program Year" /></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Transaction Group Identifier : </td>
	    			<td><s:textfield id="txnGrpIdentifier" name="txnGrpIdentifier" theme="simple" title="Transaction Group Identifier" /></td>
	    		</tr>
	    		<tr>
	    			<td class="label">System Activity Type Code: </td><!-- Example A3 -->
	    			<td><s:textfield id="sysActTypeCd" name="sysActTypeCd" theme="simple" title="System Activity Type Code" /></td>
	    			<td class="label">Transaction Type Code: </td>
	    			<td><s:select 
	    					id="transGrpType"
							name="transGrpType"
							list="transGroupTypeLst"
							listKey="itemVal" 
					  		listValue="itemLabel" 
				       		headerKey=""
							headerValue=" " 
				       		theme="simple"/><%-- <s:textfield id="transGrpType" name="transGrpType" theme="simple" title="Transaction Group Type" /> --%></td>
	    		</tr>
	    		<tr>
	    			<td class="label">Summary Approval Status: </td>
					<td><s:select id="sumAppStat"  name="sumAppStat"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/></td>
					<td class="label">Interface Approval Status: </td>
					<td><s:select id="intAppStat"  name="intAppStat"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/></td>
	    		</tr>
    		</table>
		</form>	
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
	</fieldset>
	<s:actionerror/>
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
