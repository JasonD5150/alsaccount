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
    <script type="text/javascript" src= "/alsaccount/scripts/blockUI.js"></script>
    <script src='/alsaccount/scripts/menuSecurity.js' type='text/javascript'></script>  	
    <script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.intProvBankCdDepLink.js"></script>   
	<style type="text/css">
		@import url("/alsaccount/css/alsaccount.css");
    </style>
</fwp:head>    
    <s:form id="pdfFrm" action="getTdtPdf">
    	<s:hidden id="type" name="type" value="M" />
      	<s:hidden id="depositIds" name="depositIds" />
    </s:form>
    
    <form id='rptFrm'>
			<s:hidden id="frmRptType" name="rptType" />
			<s:hidden id="frmFilters" name="filters" />
			<s:hidden id="frmProvNo" name="provNo" />
	</form>
    
    <div style="width:800px;text-align:center">
    	<h2 class="title">Internal Provider Bank Code & Deposits Linkage</h2>
   	</div>
   	
   	<fieldset style="border: black 1px solid; display: inline-block;">
	<legend style="font-weight: bold;">Search Criteria</legend>
	   	<form id='divFrm'>
		   	<s:hidden id="bpFrom" name="bpFrom" />
			<s:hidden id="bpTo" name="bpTo" />
	   		<table>
   			<tr>			
				<td class="label">Provider No</td>
				<td><s:select id="provNo"
								name="provNo"
								list="providerLst"
								listKey="itemVal" 
								listValue="itemLabel" 
								headerKey=""
								headerValue=" " 
								label="Provider No"
								theme="simple"/></td>
		    </tr>
		    </table>
		</form>	
	</fieldset>
	<br>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
		<s:submit id="details" onclick="details()" value="Remittance Details" theme="simple" disabled="true"></s:submit>
		<s:submit id="getReport" onclick="exportToCSV()" theme="simple" value="Export CSV"></s:submit>
		<s:submit id="getTDT" onclick="getTDT()" theme="simple" value="Generate Deposit Tickets"></s:submit>
	</fieldset>
	<br>
	<br>
	<s:url id="intProvBankCdDepLinkDivUrl" value="intProvBankCdDepLinkDiv_input.action" />
	<sj:div id="intProvBankCdDepLinkDiv" 
			href="%{intProvBankCdDepLinkDivUrl}"
			width="950"
			formIds="divFrm"
			reloadTopics="reloadGridDiv">
	</sj:div>
</fwp:template>
