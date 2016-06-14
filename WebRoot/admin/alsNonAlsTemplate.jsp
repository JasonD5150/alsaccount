<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template >

    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
	   	<script type="text/javascript" src= "/alsaccount/admin/scripts/fwp.alsNonAlsTemplate.js"></script>    
    </fwp:head>
    <form id='gridFrm'>
			<s:hidden id="frmBudgYear" name="budgYear"/>
	</form>
	
	<form id='drGridFrm'>
			<s:hidden id="drGridTranCd" name="crDrCd" value="D"/>
			<s:hidden id="drGridBudgYear" name="budgYear"/>
			<s:hidden id="drGridTempCd" name="tempCd"/>
	</form> 
	
	<form id='crGridFrm'>
			<s:hidden id="crGridTranCd" name="crDrCd" value="C"/>
			<s:hidden id="crGridBudgYear" name="budgYear"/>
			<s:hidden id="crGridTempCd" name="tempCd"/>
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Non-ALS SABHRS Entry Template Maintenance</h2>
   	</div>
			   
   <s:select id="budgetYearSel" name="budgetYearSel" list="budgetYearSel" listKey="itemVal" listValue="itemLabel" title="Budget Year" label="Budget Year " cssStyle="width:130px" onchange="reloadGrids()" onCompleteTopics="reloadGrids()"/>
   
  <br>
  <br>
	
	<s:url id="alsNonAlsTemplateDivUrl" action="alsNonAlsTemplateDiv_input.action"/>
   	<sj:div id="alsNonAlsTemplateDiv" style='width:800px;'
	  		href="%{alsNonAlsTemplateDivUrl}" 
	  		reloadTopics="reloadAlsNonAlsTemplateDiv" 
	  		formIds="gridFrm">
	</sj:div>

	<s:url id="alsNonAlsOrgControlDivUrl" action="alsNonAlsOrgControlDiv_input.action"/>
   	<sj:div id="alsNonAlsOrgControlDiv" style='width:800px;'
	  		href="%{alsNonAlsOrgControlDivUrl}" 
	  		reloadTopics="reloadAlsNonAlsOrgcontrolDiv" 
	  		formIds="gridFrm">
	</sj:div>
	<br>
	<input id="getRptDialog" 
			   type="button"
			   onclick="openReportDialog()" 
			   value="Generate Report">
	

    <sj:dialog id="rptDialog"
        autoOpen="false"
        modal="true" 
        draggable="true"
        resizable="true"
        height="275" width="500"
        title="Non ALS Sabhrs Entry Template Report">
        <form id='rptFrm'>
        <s:hidden id="frmRptType" name="rptType" />
	  	<table>
	  		<tr><td><s:select id="rptBudgetYear" name="rptBudgetYear" label="Budget Year" list="budgetYearSel" listKey="itemVal" listValue="itemLabel" title="Budget Year" cssStyle="width:130px" /></td></tr>		
			<tr><td><s:textfield  id="rptProviderNum" name="rptProviderNum" label="Provider #" size="11" maxlength="10"  title="Provider Number"/></td></tr>		
			<tr><td><s:textfield  id="rptOrg" name="rptOrg" label="Org" size="7" maxlength="6"  title="Org"/></td></tr>
			<tr><td><s:textfield  id="rptJLR" name="rptJLR" label="JLR" size="6" maxlength="5"  title="JLR"/></td></tr>
			<tr><td><s:textfield  id="rptAccount" name="rptAccount" label="Account" size="7" maxlength="6"  title="Account"/></td></tr>
			 <tr><td><s:radio id="tempToReport" name="tempToReport" label="Template to Report" list="#{'A':'All','S':'Single Org','M':'Multiple Orgs','N':'No Orgs'}" value="'A'" /></td></tr>
		</table>
       </form>
		<input id="getAlsNonAlsTemplateRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV"> 
     </sj:dialog>
				
</fwp:template>
