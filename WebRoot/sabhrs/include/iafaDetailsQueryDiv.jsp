<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;width: 98%";>
   	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
   	<s:actionerror/>
   	<form id='gridFrm'>
   		<table>
   			<tr>			
				<td class="label">From Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="fromDt"
				   name="fromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
				<td class="label">To Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="toDt"
				   name="toDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
			</tr>
			<tr>
    			<td class="label">Billing Period From Date: </td>
	    		<td><sj:datepicker changeMonth="true" changeYear="true" id="bpFromDt"
				   name="bpFromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
			   	<td class="label">Billing Period To Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
				   name="bpToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
				<td class="label">IAFA Seq No: </td>
				<td><s:textfield id="iafaSeqNo" name="iafaSeqNo" theme="simple" title="Issuing Provider Name" /></td>
    		</tr>
    		<tr>
    			<td class="label">Usage Period From: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="upFromDt"
				   name="upFromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
				<td class="label">Usage Period To: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="upToDt"
				   name="upToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
				   <td class="label">Item Type Code: </td>
				<td class="autocompleter" colspan="3">
				<div class="item-type-cd-div"><sj:autocompleter
						id="itemTypeCd"
						name="itemTypeCd"
						list="itemTypeLst"
						listValue="itemLabel"
						listKey="itemVal"
						selectBox="true"
						selectBoxIcon="true"
						forceValidOption="true"
						loadMinimumCount="2"
						theme="simple"/>
				</div></td>
    		</tr>
			<tr>
				<td class="label">Issuing Provider No: </td>
				<td class="autocompleter"><sj:autocompleter
									id="issProvNo"
									name="issProvNo"
									list="provLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
		       	<td class="label">Data Entry Provider No: </td>
				<td class="autocompleter"><sj:autocompleter
									id="entProvNo"
									name="entProvNo"
									list="provLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
			</tr>
			<tr>
				<td class="label">Date of Birth: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="dob"
				   name="dob" displayFormat="dd-mm-yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date of Birth" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/>
				<td class="label">ALS No: </td>
				<td><s:textfield id="alsNo" name="alsNo" theme="simple" title="Issuing Provider Name" /></td>
			</tr>
			<tr>
				<td class="label">Item Indicator: </td>
				<td class="autocompleter"><sj:autocompleter
						id="itemInd"
						name="itemInd"
						list="itemIndicatorLst"
						listValue="itemLabel"
						listKey="itemVal"
						selectBox="true"
						selectBoxIcon="true"
						forceValidOption="true"
						loadMinimumCount="2" 
						theme="simple"/></td>
		       	<td class="label">Item Status: </td>
				<td class="autocompleter"><sj:autocompleter
									id="itemStat"
									name="itemStat"
									list="itemStatusLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>				
    		</tr>
    		<tr>
    			<td class="label">Item Transaction Indicator: </td>
				<td><s:select id="itemTransInd"  name="itemTransInd"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'I':'ITEM', 'A':'APPLICATION'}"/>
				<td class="label">Item Category Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="itemCatCd"
									name="itemCatCd"
									list="itemCategoryCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
    		</tr>
    		<tr>
    			<td class="label">Process Category Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="procCatCd"
									name="procCatCd"
									list="processCategoryCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Process Type Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="procTypeCd"
									name="procTypeCd"
									list="processTypeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>				
    		</tr>
    		<tr>
    			<td class="label">Residency Indicator: </td>
				<td><s:select id="resIndicator"  name="resIndicator"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'R':'Residents', 'N':'Nonresidents','O':'Other'}"/></td>
				<td class="label">Bonus Points: </td>
				<td><s:select id="bonusPoints"  name="bonusPoints"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'Y':'YES', 'N':'NO'}"/></td>
    		</tr>
    		<tr>
    			<td class="label">Session Status: </td>
				<td><s:select id="sessStat"  name="sessStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Active', 'V':'Voided'}"/></td>
				<td class="label">Session Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="sessDt"
				   name="sessDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
				<td class="label">Session Void Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="sessVoidDt"
				   name="sessVoidDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
    		</tr>
		</table>
		<div id="advancedSearchDiv" style="display:none">
		<fieldset style="border: black 1px solid; display: inline-block;">
    		<legend style="font-weight: bold;font-size:larger">Advanced Search</legend>
		<table>
			<tr>
				<td class="label">No Charge: </td>
				<td><s:select id="noCharge"  name="noCharge"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'Y':'YES', 'N':'NO'}"/></td>
				<td class="label">Amount: </td>
				<td><s:textfield id="amount" name="amount" theme="simple" title="Issuing Provider Name" /></td>
			</tr>
    		<tr>
    			<td class="label">Mode of Payment: </td>
				<td><s:textfield id="modeOfPayment" name="modeOfPayment" theme="simple" title="Issuing Provider Name" />
				<td class="label">Check No: </td>
				<td><s:textfield id="chckNo" name="chckNo" theme="simple" title="Issuing Provider Name" /></td>
				<td class="label">Check Writer: </td>
				<td><s:textfield id="chckWriter" name="chckWriter" theme="simple" title="Issuing Provider Name" />
				<td class="label">Remarks: </td>
				<td><s:textfield id="remarks" name="remarks" theme="simple" title="Issuing Provider Name" /></td>
    		</tr>
    		<tr>
    			<td class="label">ALX Indicators Provider/Session </td>
				<td><s:checkbox id="alxInd" name="alxInd" title="Exclude Informational" theme="simple" fieldValue="false"/></td>
				<td class="label">Tribe Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="tribeCd"
									name="tribeCd"
									list="tribeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Application Type: </td>
				<td class="autocompleter"><sj:autocompleter
									id="appType"
									name="appType"
									list="applicationTypeLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
    		</tr>
    		<tr>
    			<td class="label">Application Disposition: </td>
				<td class="autocompleter"><sj:autocompleter
									id="appDis"
									name="appDis"
									list="applicationDispositionLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Record Void Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="recordVoidDt"
				   name="recordVoidDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Record Void Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
			</tr>
    		<tr>
    			
				<td class="label">Seq No Within Item Transaction: </td>
				<td><s:textfield id="seqNoInItemTrans" name="seqNoInItemTrans" theme="simple" title="Issuing Provider Name" /></td>
    			<td class="label">Amount Type Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="amountTypeCd"
									name="amountTypeCd"
									list="amountTypeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Cost Prereq Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="costPrereqCd"
									name="costPrereqCd"
									list="costPrerequisiteCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Batch Reconciliation Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="batchRecDt"
				   name="batchRecDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Batch Reconciliation Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
    		</tr>
    		<tr>
    			<td class="label">Reason Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="reasonCd"
									name="reasonCd"
									list="reasonTypeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
				<td class="label">Transaction Group Identifier: </td>
				<td><s:textfield id="transGrpIdentifier" name="transGrpIdentifier" theme="simple" title="Issuing Provider Name" /></td>
				<td class="label">NULL TGI?: </td>
				<td><s:checkbox id="nullTGI" name="nullTGI" theme="simple" title="Issuing Provider Name"></s:checkbox></td>
				<td class="label">Hardware Type/Code(Device No): </td>
				<td><s:textfield id="ahmType" name="ahmType" theme="simple" title="Issuing Provider Name" size="2" maxlength="1"/><s:textfield id="ahmCd" name="ahmCd" theme="simple" title="Issuing Provider Name" />
    		</tr>
    		<tr>
    			<td class="label">Summary Approval Status: </td>
				<td><s:select id="sumAppStat"  name="sumAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/>
				<td class="label">Interface Approval Status: </td>
				<td><s:select id="intAppStat"  name="intAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/></td>
				<td class="label">Other Transaction Group: </td>
				<td><s:textfield id="otherTransGrp" name="otherTransGrp" theme="simple" title="Issuing Provider Name" /></td>
				<td class="label">Session Origin: </td>
				<td><s:textfield id="sessOrigin" name="sessOrigin" theme="simple" title="Issuing Provider Name" /></td>
    		</tr>
   		</table>
		</fieldset>
		</div>
		
	</form>		
</fieldset>
