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
   	<script type="text/javascript" src= "/alsaccount/admin/scripts/fwp.systemActivityAccountMaster.js"></script>    
  	
  	<style type="text/css" media="screen">
	    th.ui-th-column div{
	        white-space:normal !important;
	        height:auto !important;
	    }
	</style>
  
  </fwp:head>
  
  	<form id='rptFrm'>
			<s:hidden id="frmRptType" name="rptType" />
			<s:hidden id="frmFilters" name="filters" />
			<s:hidden id="frmActTypCd" name="actTypCd" />
			<s:hidden id="frmRptBudgYear" name="budgYear" />
	</form>
	
	<form id='gridFrm'>
			<s:hidden id="frmBudgYear" name="budgYear" />
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">System Activity and Account Master</h2>
   	</div>
  
	<s:select id="budgetYearSel" name="budgetYearSel" list="budgetYearSel" listKey="itemVal" listValue="itemLabel" title="Budget Year" label="Budget Year " cssStyle="width:130px" onchange="reloadGrids()" onCompleteTopics="setGridFrm()"/>
	<br>
	<br>

	<s:url id="actAccLinkTabUrl" action="actAccLinkTabAction_input.action"/>

	<div id='saamTabPages'>
			<sj:tabbedpanel id="ictTabs" selectedTab="0" useSelectedTabCookie="false"  cssStyle="width:995px;position:inherit">
				<sj:tab id="tab1" target="tone" title="System Activity Codes" label="System Activity Codes" tabindex="1" />
				<sj:tab id="tab2" target="ttwo" title="Account Master" label="Account Master" tabindex="2" />
				<sj:tab id="tab3" target="tthree" title="Activity Account Linkage" label="Activity Account Linkage" tabindex="3" />
				<sj:tab id="tab4" target="tfour" title="Appendix M" label="Appendix M" tabindex="4" />
				
				<div id="tone">
			 		<c:import url="/admin/include/activityControlTab.jsp"/>
				</div>
				<div id="ttwo">
					<c:import url="/admin/include/accountMasterTab.jsp"/>
				</div>
				<sj:div id="tthree" style='width:800px;'
		    		href="%{actAccLinkTabUrl}" 
		    		reloadTopics="reloadLinkTab" 
		    		formIds="gridFrm">
				</sj:div> 
				<div id="tfour">
					<c:import url="/admin/include/appendixMTab.jsp"/>  			
				</div>
				
		 	</sj:tabbedpanel>
	</div>
</fwp:template>  