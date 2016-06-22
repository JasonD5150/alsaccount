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
	<s:hidden id="curBudgetYearChangeDt" name="curBudgetYearChangeDt" />
	<s:hidden id="copyAccTablesCompleted" name="copyAccTablesCompleted" />
	<s:hidden id="copyAlsNonAlsTemplatesCompleted" name="copyAlsNonAlsTemplatesCompleted" />
	
	<form id = 'stepFrm' action="fiscalYearEnd_input.action">
		<s:hidden id="yearEndStep" name="yearEndStep" />
		<s:hidden id="upfStep" name="upf" />
		<s:hidden id="uptStep" name="upt" />
		<s:hidden id="itcStep" name="itc" />
		<s:hidden id="budgYearStep" name="budgYear" />
		<s:hidden id="budgYearChngDtStep" name="budgYearChangeDt" />
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
		<sj:tab id="tab2" target="ttwo" title="Update Item Accounting Codes" label="Update Item Accounting Codes" tabindex="2" />
		<sj:tab id="tab3" target="tthree" title="Generate FY ADjusting Entries" label="Gen FY Adjusting Entries" tabindex="3" />
		<sj:tab id="tab4" target="tfour" title="Update Budget Year Change Date" label="Update Change Date" tabindex="4" />
		<sj:tab id="tab5" target="tfive" title="Copy Templates" label="Copy Templates" tabindex="5" />
		
		
		<div id="tone">
	 		<h5> This process will create the next budget year Accounting Table data.<br/> 
			 Org Control,<br/>
			 Accounting Code Control<br/>
			 System Activity Code<br/>
			 Account Master<br/>
			 Activity Account Linkage<br/>
			 Appendix M(Notation Only)<br/><br/>
			 If button is disable, the proccess has already been run for this year.
			</h5>
			<input id='copyAccTables' type='button' value='Copy Accounting Tables' onclick='fiscalYearEndAction(id)'> 
		</div>
		<div id="ttwo">
			<div id="stepTwoAllDiv">
			<h5> Update All Item Accounting Codes<br/>
			     This process will update all Item Accounting Codes, with a valid usage period.
			</h5>
			<input id='updateAccCdAll' type='button' value='Update All' onclick='fiscalYearEndAction(id)'> 
			<input id='indvDiv' type='button' value='Update Individual' onclick='stepTwoToggle(id)'>
			</div> 
			
			<div id="stepTwoIndvDiv"style="display: none;">
			<input id='allDiv' type='button' value='Back' onclick='stepTwoToggle(id)'>
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
					<td class="label">Item Type List:</td>
					<td colspan="4"><s:url id="itemTypeListUrl" action="getItemTypeList" /> 
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
				<tr><td><br/></td></tr>
				<tr>
					<td colspan='2'><input id='updateAccCd' type='button' value='Update Usage Period' onclick='fiscalYearEndAction(id)'></td>
				</tr>
			</table>
			</div>
		</div>
		<s:url id="adjParamTabUrl" action="adjParamTabAction_input.action"/>
		<sj:div id="tthree" style='width:800px;'
		    		href="%{adjParamTabUrl}" 
		    		reloadTopics="reloadAdjParam" 
		    		formIds="adjParamFrm"
		    		onCompleteTopics="adjParamFrmComplete">
		</sj:div> 
		<div id="tfour">
	   		<h5>Note: This updates when job ALSU1030 runs.
			</h5>
			<table>
				<tr>
					<td class="label">Budget Year Change Date:</td>
					<td><input type='text' id='updateBudgetYearChangeDt' placeholder='07/01/YYYY' size='12' maxlength='10' onchange="testDate(this)"/></td>
				</tr>
				<tr><td><br/></td></tr>
				<tr>
					<td colspan="2"><input id='updateBudgYrChngDt' type='button' value='Update Budget Year Change Date' onclick='fiscalYearEndAction(id)'></td>
				</tr>
			</table>	
		</div> 
		<div id="tfive">
	   		<h5>This process will copy all current year Non-ALS SABHRS Template Codes into the next year.<br />
			 Only the budget year will change all other attributes of the code will remain the same.<br/>
			 If button is disable, the proccess has already been run for this year.<br/><br/>
			 Note: Do not run until cashier is done with Internal Provider Remittance for current budget year(usually mid July).
			</h5>
			<input id='copyAlsNonAlsTemplates' type='button' value='Copy Templates to Next Budget Year' onclick='fiscalYearEndAction(id)'>
		</div> 
	</sj:tabbedpanel>		
</fwp:template>
