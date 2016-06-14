<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags"%>

<fwp:template>

	<fwp:head>
		<sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery" />
		<script src='scripts/fieldEdits.js' type='text/javascript'></script>
		<script type="text/javascript" src= "/alsaccount/admin/scripts/fwp.fiscalYearEnd.js"></script>  
	</fwp:head>
	<s:hidden id="curBudgetYear" name="curBudgetYear" />
	<s:hidden id="copyAccTablesCompleted" name="copyAccTablesCompleted" />
	<s:hidden id="copyAlsNonAlsTemplatesCompleted" name="copyAlsNonAlsTemplatesCompleted" />
	
	<form id = 'stepFrm' action="fiscalYearEnd_input.action">
		<s:hidden id="yearEndStep" name="yearEndStep" />
		<s:hidden id="upfStep" name="upf" />
		<s:hidden id="uptStep" name="upt" />
		<s:hidden id="itcStep" name="itc" />
		<s:hidden id="budgYearStep" name="budgYear" />
	</form>
	
	<form id='rptFrm'>
		<s:hidden id="frmRptType" name="rptType" />
		<s:hidden id="frmRptBody" name="rptBody" />
	</form>
	
	<form id = 'adjParamFrm'>
		<s:hidden id="adjParamFrmYear" name="budgYear" />
	</form>
	
	

	<div style="width:800px;text-align:center">
		<h2 class="title">Fiscal Year End Process</h2>
	</div>

	<div id ='stepHtmlDiv'>
		<span id='stepHtml'></span>
	</div>

	<sj:tabbedpanel id="ictTabs" selectedTab="0" useSelectedTabCookie="false"  cssStyle="width:995px;position:inherit" onChangeTopics="tabChanged">
		<sj:tab id="tab1" target="tone" title="Copy Accounting Table" label="Copy Accounting Table" tabindex="1" />
		<sj:tab id="tab2" target="ttwo" title="Update Usage Period" label="Update Usage Period" tabindex="2" />
		<sj:tab id="tab3" target="tthree" title="Copy Templates" label="Copy Templates" tabindex="3" />
		<sj:tab id="tab4" target="tfour" title="Adjustment Parameters" label="Adjustment Parameters" tabindex="4" />
		
		<div id="tone">
	 		<h5> This process will create the next budget year data.<br /> 
			 Org Control, Accounting Code Control, System Activity control, System Activity 
		     Code, Account Master, Activity Account Linkage, Appendix M(Notation 
			 Only)<br/>
			 If button is disable, the proccess has already been run for this year.
			</h5>
			<input id='copyAccTables' type='button' value='Copy Accounting Tables' onclick='fiscalYearEndAction(id)'> 
		</div>
		<div id="ttwo">
			<form id = 'itemTypeFrm'>
			<s:hidden id="upf" name="upf" />
			<s:hidden id="upt" name="upt" />
			</form>
			<h5> Enter Usage Period of item(s) to be updated.<br/>
				 Enter a single item type code, if desired, or use a wildcard (%).<br/>
				 Leave Item Type blank to update all items for usage period.
			</h5>
			<table>
				<tr>
					<td class="label">Item Type List:</td>
					<td><s:url id="itemTypeListUrl" action="getItemTypeList" /> 
						<sj:select id="itemTypeList"
							   name="itemTypeList"
							   href="%{itemTypeListUrl}"
							   label="Item Types"
							   listKey="itemVal" 
							   listValue="itemLabel"
							   list="itemTypes"
							   headerKey="0"
							   headerValue=" "			   
							   formIds="itemTypeFrm"
							   reloadTopics="reloadItemTypes"
							   onChangeTopics="itemTypeSelected"/></td>
				</tr>
			</table>
			<br>
			<table>
				<tr>
					<td class="label">Usage From</td>
					<td class="label">Usage To</td>
					<td class="label">Item Type</td>
					<td></td>
					<td class="label">Current Budget Year</td>
				<tr>
					<td><input type='text' id='updateAccCdFrom' placeholder='MM/DD/YYYY' size='12' maxlength='10' onchange="testDate(this)"/></td>	
					<td><input type='text' id='updateAccCdTo' placeholder='MM/DD/YYYY' size='12' maxlength='10' onchange="testDate(this)"/></td>
					<td><input type='text' id='updateItemType' size='12' maxlength='10' /></td>
					<td><input type='text' id='updateItemTypeDesc' disabled/></td>
					<td><input type='text' id='updateBudgetYear' size='5' maxlength='4' /></td>				
				</tr>
				<tr>
					<td colspan='2'><input id='updateAccCd' type='button' value='Update Usage Period' onclick='fiscalYearEndAction(id)'></td>
				</tr>
			</table>
		</div>
		<div id="tthree">
	   		<h5>This process will copy all current year budget codes into the next year.<br />
			Only the budget year will change all other attributes of the code will remain the same.<br/>
			 If button is disable, the proccess has already been run for this year.
			</h5>
			<input id='copyAlsNonAlsTemplates' type='button' value='Copy Templates to Next Budget Year' onclick='fiscalYearEndAction(id)'>
		</div> 
		<s:url id="adjParamTabUrl" action="adjParamTabAction_input.action"/>
		<sj:div id="tfour" style='width:800px;'
		    		href="%{adjParamTabUrl}" 
		    		reloadTopics="reloadAdjParam" 
		    		formIds="adjParamFrm"
		    		onCompleteTopics="adjParamFrmComplete">
		</sj:div> 
	</sj:tabbedpanel>		
</fwp:template>
