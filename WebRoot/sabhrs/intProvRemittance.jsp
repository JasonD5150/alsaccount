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
    <script type="text/javascript" src= "/alsaccount/scripts/exportGrid.js"></script> 
    <script type="text/javascript" src= "/alsaccount/scripts/menuSecurity.js"></script> 
    <script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.intProvRemittance.js"></script>   
	<style type="text/css">
		@import url("/alsaccount/css/alsaccount.css");
    </style>
</fwp:head>
    <div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
    <s:hidden id="inProvNo" name="inProvNo" />
    <s:hidden id="inBpFrom" name="inBpFrom" />
    <s:hidden id="inBpTo" name="inBpTo" />
    <div style="width:800px;text-align:center">
    	<h2 class="title">Internal Provider Remittance</h2>
   	</div>
   	
   	<s:url id="searchCriteriaDivUrl" value="intProvRemittanceQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			onCompleteTopics="qryDivComplete">
	</sj:div>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
		<s:submit id="details" onclick="details()" value="Details" theme="simple" disabled="true"></s:submit>
		<s:submit id="getReport" onclick="exportToCSV()" theme="simple" value="Export CSV"></s:submit>
	</fieldset>
	<br>
	<br>
	<s:url id="alsInternalRemittanceGridURL" action="alsAccount/alsInternalRemittanceGrid_buildgrid" />
	<s:url id="alsInternalRemittanceGridEditURL" action="alsAccount/alsInternalRemittanceGridEdit_execute" />    
	<sjg:grid
		id="alsInternalRemittance"
		caption="Internal Remittance"
		href="%{alsInternalRemittanceGridURL}"
		editurl="%{alsInternalRemittanceGridEditURL}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorAddOptions="{width:500,reloadAfterSubmit:true,
	    					  addedrow:'last',
	                          addCaption:'Add New Code Info',
	                          afterSubmit:errorHandler,
	                          closeAfterAdd:true,
	                          processData:'Adding Row to Database'}"
	    navigatorEditOptions="{width:500,reloadAfterSubmit:false,
	                           editCaption:'Edit Code Info',
	                           afterSubmit:errorHandler,
	                           beforeShowForm:setEditableFields,	    
	                           closeAfterEdit:true,
	                           processData:'Updating to Database'}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    navigatorDeleteOptions="{afterSubmit:errorHandler}"
	    gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="200"
		width="950"
		rowNum="10000"
		formIds="gridFrm"
		reloadTopics="reloadInternalRemittance"
		onSelectRowTopics="internalRemittanceSelected"
		onCompleteTopics="internalRemittanceComplete"
		shrinkToFit="false"
		loadonce="true">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.apiProviderNo" index="idPk.apiProviderNo" title="Provider No" width="60" sortable="true" editable="false" align="right"/>
			<sjg:gridColumn name="provNm" index="provNm" title="Provider Name" width="100" sortable="true" editable="false" align="right"/>
			<sjg:gridColumn name="idPk.airBillingFrom" index="airBillingFrom" title="Billing Period From" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="idPk.airBillingTo" index="airBillingTo" title="Billing Period To" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airSystemSales" index="airSystemSales" title="System Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="amtRec" index="amtRec" title="Amount Reseived" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="eftddd" index="eftddd" title="EFT DDD" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airOtcPhoneSales" index="airOtcPhoneSales" title="OTC SALES" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airPae" index="airPae" title="PAE's" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airNonAlsSales" index="airNonAlsSales" title="Non ALS Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airTotSales" index="airTotSales" title="Total Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="amtDue" index="amtDue" title="Total ALS Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airCreditSales" index="airCreditSales" title="Credit Card Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
			<sjg:gridColumn name="totFundsRec" index="totFundsRec" title="Total Funds Received" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="totBankDep" index="totBankDep" title="Total Bank Deposits" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="billingBallanced" index="billingBallanced" title="Billing Period had been balanced by Internal Provider" editable="false" width="25" sortable="true" edittype="select" formatter="select" editoptions="{value: {Y: 'YES', N: 'NO'}}"/>
			<sjg:gridColumn name="airDifference" index="airDifference" title="Difference" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airShortSales" index="airShortSales" title="Total Short of Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="airOverSales" index="airOverSales" title="Total Over Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="netOverShortOfSales" index="netOverShortOfSales" title="Net Over/Short of Sales" width="60" sortable="false" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="completeProvider" index="completeProvider" title="Date Completed by Provider" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airOfflnPaymentApproved" index="airOfflnPaymentApproved" title="RemittanceApproved" width="15" sortable="true" editable="true" formatter="checkbox" align="center" edittype="checkbox" editoptions="{ value: 'true:false' }"/>
			<sjg:gridColumn name="airOfflnPaymentAppBy" index="airOfflnPaymentAppBy" title="Approved By" width="60" sortable="true" editable="false"/>
			<sjg:gridColumn name="offlnPaymentAppDt" index="offlnPaymentAppDt" title="Date" width="70" sortable="true" editable="flase" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airOfflnPaymentAppCom" index="airOfflnPaymentAppCom" title="Comments" width="100" sortable="true" editable="true"/>
			<sjg:gridColumn name="intFileCreateDt" index="intFileCreateDt" title="SABHRS Interface File Created" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="intFileGenerated" index="intFileGenerated" title="Interface File Created" hidden="true" editable="false" />
	</sjg:grid>
	<br>
	<form id="subGridFrm">
		<s:hidden id="frmProvNo" name="provNo"/>
		<s:hidden id="frmBPFrom" name="bpFrom"/>
		<s:hidden id="frmBPTo" name="bpTo"/>
	</form>
   	<s:url id="alsNonAlsDetailsGridURL" action="alsAccount/alsNonAlsDetailsGrid_buildgrid" />
	<s:url id="alsNonAlsDetailsGridEditURL" action="alsAccount/alsNonAlsDetailsGridEdit_execute" />    
	<sjg:grid
		id="alsNonAlsDetails"
		caption="Non ALS Details"
		href="%{alsNonAlsDetailsGridURL}"
		editurl="%{alsNonAlsDetailsGridEditURL}"		
		dataType="local"
		pager="true"
		navigator="false"
	    gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="75"
		width="950"
		rowNum="1000"
		formIds="subGridFrm"
		reloadTopics="reloadSubGrids">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="anatCd" index="anatCd" title="Code" width="10" sortable="true" editable="true"/>
			<sjg:gridColumn name="anadDesc" index="anadDesc" title="Description" width="10" sortable="true" editable = "true" editrules="{required:true}"/>
			<sjg:gridColumn name="anadAmount" index="anadAmount" title="Amount" width="10" sortable="true" editable = "true" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
	</sjg:grid>
 	<br>
	<s:url id="alsOverUnderSalesGridURL" action="alsAccount/alsOverUnderSalesGrid_buildgrid" />
	<s:url id="alsOverUnderSalesGridEditURL" action="alsAccount/alsOverUnderSalesGridEdit_execute" />    
	<sjg:grid
		id="alsOverUnderSales"
		caption="Total Funds Received Over / Short of Sales - Details"
		href="%{alsOverUnderSalesGridURL}"
		editurl="%{alsOverUnderSalesGridEditURL}"		
		dataType="local"
		pager="true"
		navigator="false"
	    gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="75"
		width="950"
		rowNum="1000"
		formIds="subGridFrm"
		reloadTopics="reloadSubGrids">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="aousdFlag" index="aousdFlag" title="Over/Short of Sales" width="10" sortable="true" editable="true" edittype="select" formatter="select" editoptions="{value: {O: 'Over Sale', U: 'Short of Sales'}}" editrules="{required:true}"/>
			<sjg:gridColumn name="aousdDesc" index="aousdDesc" title="Description" width="10" sortable="true" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="aousdAmount" index="aousdAmount" title="Amount" width="10" sortable="true" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
	</sjg:grid>	
</fwp:template>
