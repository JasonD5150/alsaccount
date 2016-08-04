<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template>
<fwp:head>
    <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
    <script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script> 
    <script>    	
    	$.subscribe('internalProviderTdtComplete', function(event, data) {				
				$("#internalProviderTdtTable")
					.jqGrid({pager:'#internalProviderTdtTable_pager'})
					.jqGrid('navButtonAdd'
					,'#internalProviderTdtTable_pager'
					,{id:"selectAll_internalProviderTdtTable"
					,caption:"Select All"
					,buttonicon:"ui-icon-check"
					,onClickButton:function(){ 
						var grid = $("#internalProviderTdtTable");
    					var rows = grid.jqGrid("getDataIDs");
    					for (i = 0; i < rows.length; i++)
					    {
							$('#internalProviderTdtTable').jqGrid('setCell',rows[i],'approve',1);
					    }
					}
					,position:"last"
					,title:"Select All"
					,cursor:"pointer"
					});
				$("#internalProviderTdtTable")
					.jqGrid({pager:'#internalProviderTdtTable_pager'})
					.jqGrid('navButtonAdd'
					,'#internalProviderTdtTable_pager'
					,{id:"deselectAll_internalProviderTdtTable"
					,caption:"Deselect All"
					,buttonicon:"ui-icon-minusthick"
					,onClickButton:function(){ 
						var grid = $("#internalProviderTdtTable");
					    var rows = grid.jqGrid("getDataIDs");
					    for (i = 0; i < rows.length; i++)
					    {
							$('#internalProviderTdtTable').jqGrid('setCell',rows[i],'approve',0);
					    }
					}
					,position:"last"
					,title:"Deselect All"
					,cursor:"pointer"
					});			
			});
		
		function submitSearch(){
			$.publish('reloadInternalProviderTdtTable');
		}
				
    	function resetSearch(){
			$('#gridFrm')[0].reset();
		}
		
		function getGenRpt() {
			var approved = false;
			var grid = $("#internalProviderTdtTable");
   			var rows = grid.jqGrid("getDataIDs");
   			var depositIdLst = "";
   			for (i = 0; i < rows.length; i++)
		    {
		        var app = grid.jqGrid ('getCell', rows[i], 'approve');
		        var depositIds = grid.jqGrid ('getCell', rows[i], 'apbdDepositId');
		        if(app == 1){
		        	approved = true;
		        	if(depositIdLst == ""){
		        		depositIdLst += "'"+depositIds+"'";
		        	}else{
		        		depositIdLst += ",'"+depositIds+"'";
		        	}
		        }
		    }
		    if(approved){
		    	$('#depositIds').val(depositIdLst);
				$('#pdfFrm').submit();
		    }else{
		    	alert("No record to print.");
		    }
		}
    </script> 
</fwp:head>
    
    <s:form id="pdfFrm" action="getTdtPdf">
    	<s:hidden id="type" name="type" value="M" />
      	<s:hidden id="depositIds" name="depositIds" />
    </s:form>
       
    <div style="width:800px;text-align:center">
    	<h2 class="title">Internal Provider Treasury Deposit Tickets</h2>
   	</div>
   	
   	<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
	   	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
	   	<s:actionerror/>
	   	<form id='gridFrm'>
	   		<table>
	   			<tr>
	   				<td class="label">BPE Date From: </td>
    				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpeFrom"
									   name="bpeFrom" displayFormat="mm/dd/yy"
									   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="BPE Date From" 
									   showOn="focus" onblur="testDate(this)"/></td>
    				<td class="label"> To: </td>
    				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpeTo"
									   name="bpeTo" displayFormat="mm/dd/yy"
									   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="BPE Date To" 
									   showOn="focus" onblur="testDate(this)"/></td>
	   			</tr>
	   			<tr>
	   				<td class="label">Offline Payment Approval Date: </td>
    				<td><sj:datepicker changeMonth="true" changeYear="true" id="opaDate"
									   name="opaDate" displayFormat="mm/dd/yy"
									   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Offline Payment Approval Date" 
									   showOn="focus" onblur="testDate(this)"/></td>
    				<td class="label"> Provider No: </td>
    				<td><s:textfield id="provNo" name="provNo" theme="simple" title="Provider No" cssStyle="width:80px"/></td>
	   			</tr>
	   		</table>
		</form>	
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
	</fieldset>
   	
   	<br>
   	<br>
	<s:url id="internalProviderTdtGridURL" action="alsAccount/internalProviderTdtGrid_buildgrid" />
	<sjg:grid
		id="internalProviderTdtTable"
		caption="Bank Details"
		href="%{internalProviderTdtGridURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
    	gridModel="model"
    	formIds="gridFrm"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="250"
		width="950"
		rowNum="1000"
		resizable="true"
		onCompleteTopics="internalProviderTdtComplete"
		reloadTopics="reloadInternalProviderTdtTable"
		>
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="approve" index="approve" title="Create TDT" width="10" sortable="true" align="center" editable = "true" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
			<sjg:gridColumn name="providerNo" index="providerNo" title =" Provider No" width="10" sortable="false" hidden="false" editable="true" align="right"/>
			<sjg:gridColumn name="providerName" index="providerName" title =" Provider Name" width="35" sortable="false" hidden="false" editable="true" />
			<sjg:gridColumn name="apbdDepositDate" index="apbdDepositDate" title =" Deposit Date" width="20" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="apbdDepositId" index="apbdDepositId" title =" Deposit Id" width="20" sortable="false" editable="true" />
			<sjg:gridColumn name="apbdAmountDeposit" index="apbdAmountDeposit" title =" Deposit Amount" width="20" sortable="false" editable="true" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
			
	</sjg:grid>
	<br>
	<input id="getRptDialog" 
			   type="button"
			   onclick="getGenRpt();" 
			   value="Generate Deposit Tickets">		
</fwp:template>
