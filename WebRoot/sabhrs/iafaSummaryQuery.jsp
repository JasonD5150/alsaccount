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
	   	<script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.iafaSummaryQuery.js"></script>   
	   	<style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
        </style>
    </fwp:head>
	
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">IAFA Summary by Amount / Item Type Query</h2>
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
	<div id="itemTypeDiv">
		<sjg:grid
			id="iafaSummaryByItemTypeQueryTable"
			caption="IAFA Summary by Item Type"
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
			reloadTopics="reloadIafaSumItemTypeQueryTable"
			onCompleteTopics="iafaSumItemTypeQueryTableComplete"
			formIds="gridFrm"
			loadonce="true">
				<sjg:gridColumn name="gridKey" title ="id" width="25" hidden="true" key="true"/>
				<sjg:gridColumn id="itemTypeCd" name="itemTypeCd" title ="Item Type Code" width="25" align="right"/>
				<sjg:gridColumn id="itemTypeDesc" name="itemTypeDesc" title ="Item Type Desc" width="55" />
				<sjg:gridColumn id="processTypeCd" name="processTypeCd" title ="Process Type" width="20" />
				<sjg:gridColumn id="count" name="count" title ="Count" width="10" align="right" />
				<sjg:gridColumn id="amount" name="amount" title ="Amount" width="15" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
		</sjg:grid>
	</div>
	<div id="amountTypeDiv" style="display:none">
		<sjg:grid
			id="iafaSummaryByAmountTypeQueryTable"
			caption="IAFA Summary by Amount Type"
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
			shrinkToFit="false"
        	autowidth="true"
        	resizable="true"
			rowNum="1000"
			reloadTopics="reloadIafaSumAmountTypeQueryTable"
			onCompleteTopics="iafaSumAmountTypeQueryTableComplete"
			formIds="gridFrm"
			loadonce="true"
			groupField="['provNo','deviceNo']"
    		groupColumnShow="[true]"
    		groupCollapse="true"
    		groupText="['<b>Provider No {0}</b>','<b>Device Id {0}</b>']"
    		groupSummary="[true]" >
				<sjg:gridColumn name="gridKey" title ="id" width="1" hidden="true" key="true"/>
				<sjg:gridColumn id="provNo" name="provNo" title ="Issuing Provider No" width="40" align="right"/>
				<sjg:gridColumn id="provNm" name="provNm" title ="Issuing Provider Name" width="150" />
				<sjg:gridColumn id="bpFromDt" name="bpFromDt" title ="Billing Period From" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
				<sjg:gridColumn id="bpToDt" name="bpToDt" title ="Billing Period To" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
				<sjg:gridColumn id="procCatCd" name="procCatCd" title ="Process Category Code" width="20" />
				<sjg:gridColumn id="procCatDesc" name="procCatDesc" title ="Process Category Desc" width="20" />
				<sjg:gridColumn id="deviceNo" name="deviceNo" title ="Device No" width="70" />
				<sjg:gridColumn id="amtType" name="amtType" title ="Amount Type" width="20" />
				<sjg:gridColumn id="processTypeCd" name="processTypeCd" title ="Process Type" width="20" />
				<sjg:gridColumn id="resStatus" name="resStatus" title ="Residency Status" width="20" />
				
				<sjg:gridColumn id="itemTypeCd" name="itemTypeCd" title ="Item Type Code" width="70" align="right"/>
				<sjg:gridColumn id="itemTypeDesc" name="itemTypeDesc" title ="Item Type Desc" width="150" />
				<sjg:gridColumn id="upFromDt" name="upFromDt" title ="Usage Period From" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
				<sjg:gridColumn id="upToDt" name="upToDt" title ="Usage Period To" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
				<sjg:gridColumn id="nonResCostGrpDesc" name="nonResCostGrpDesc" title ="Cost Group Code" width="50" />
				<sjg:gridColumn id="nonResPrereqCostDesc" name="nonResPrereqCostDesc" title ="Cost Prerequisite" width="50" />
				<sjg:gridColumn id="nonResCount" name="nonResCount" title ="Count" width="30" align="right" />
				<sjg:gridColumn id="nonResAmount" name="nonResAmount" title ="Amount" width="50" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
				<sjg:gridColumn id="resCostGrpDesc" name="resCostGrpDesc" title ="Cost Group Code" width="50" />
				<sjg:gridColumn id="resPrereqCostDesc" name="resPrereqCostDesc" title ="Cost Prerequisite" width="50 " />
				<sjg:gridColumn id="resCount" name="resCount" title ="Count" width="30" align="right" />
				<sjg:gridColumn id="resAmount" name="resAmount" title ="Amount" width="50" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
				<sjg:gridColumn id="totalCount" name="totalCount" title ="Total Count" width="30" align="right"/>
				<sjg:gridColumn id="totalAmount" name="totalAmount" title ="Total Amount" width="50" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
				
		</sjg:grid>
	</div>
</fwp:template>
