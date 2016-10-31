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
	   	<script type="text/javascript" src= "/alsaccount/sales/scripts/fwp.iafaSummaryQuery.js"></script>   
	   	<style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
        </style>
    </fwp:head>
	
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">IAFA Summary Query</h2>
   	</div>	

	<s:url id="searchCriteriaDivUrl" value="iafaSummaryQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950"
			reloadTopics="reloadLists"
			onCompleteTopics="resetSearchCriteria">
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
	<s:url id="iafaSummaryGridURL" action="alsAccount/iafaSummaryGrid_buildgrid" />
	<sjg:grid
		id="iafaSummaryTable"
		caption="IAFA Summary"
		href="%{iafaSummaryGridURL}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"   	
	    gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="200"
		width="950"
		rowNum="1000"
		reloadTopics="reloadIafaSummaryTable"
		onCompleteTopics="iafaSummaryTableComplete"
		formIds="gridFrm"
		loadonce="true">
			<sjg:gridColumn name="gridKey" title ="id" width="25" hidden="true" key="true"/>
			<sjg:gridColumn id="provNo" name="provNo" title ="Provider No" width="25" align="right"/>
			<sjg:gridColumn id="provNm" name="provNm" title ="Issuing Provider Name" width="125"/>
			<sjg:gridColumn id="itemTypeCd" name="itemTypeCd" title ="Item Type Code" width="30" align="right"/>
			<sjg:gridColumn id="itemTypeDesc" name="itemTypeDesc" title ="Item Type Desc" width="125" />
			<sjg:gridColumn id="amtType" name="amtType" title ="Amount Type" width="10" align="right"/>
			<sjg:gridColumn id="count" name="count" title ="Count" width="10" align="right" />
			<sjg:gridColumn id="amount" name="amount" title ="Total" width="25" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
			<sjg:gridColumn id="upFromDt" name="upFromDt" title ="Usage Period From" width="50" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="upToDt" name="upToDt" title ="Usage Period To" width="50" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="resStatus" name="resStatus" title ="Residency Status" width="10"/>
			
			<sjg:gridColumn id="amtTypeDesc" name="amtTypeDesc" title ="Amount Type Desc" width="50" hidden="true"/>
			<sjg:gridColumn id="processTypeCd" name="processTypeCd" title ="Process Type" width="20" hidden="true"/>
			<sjg:gridColumn id="processTypeDesc" name="processTypeDesc" title ="Process Type Desc" width="20" hidden="true"/>
			<sjg:gridColumn id="procCatCd" name="procCatCd" title ="Process Category Code" width="20" hidden="true"/>
			<sjg:gridColumn id="procCatDesc" name="procCatDesc" title ="Process Category Desc" width="20" hidden="true"/>
			<sjg:gridColumn id="appTypeCd" name="appTypeCd" title ="App Type Code" width="20" hidden="true"/>
			
			
			
	</sjg:grid>	
</fwp:template>
