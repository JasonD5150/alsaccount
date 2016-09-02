<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
   	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
   	<form id='gridFrm'>
   		<table>
   			<tr>			
				<td class="label">Provider No: </td>
				<td class="autocompleter"><sj:autocompleter
									id="providerNo"
									name="providerNo"
									list="providerLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
				<td class="label">IAFA Seq No: </td><!-- as long as it is tied to Provider No and Billing Period From/To -->
				<td><s:textfield id="seqNo" name="seqNo" theme="simple" title="IAFA Sequence No" /></td>
			</tr>
			<tr>
    			<td>Billing Period From Date: </td>
	    		<td><sj:datepicker changeMonth="true" changeYear="true" id="bpFromDt"
				   name="bpFromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
			   	<td>Billing Period To Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
				   name="bpToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
    		</tr>
    		<tr><td colspan="4" style="border-bottom:1px solid black;"><br></td></tr>
    		<tr><td><br></td></tr>
    		<tr>
    			<td>From Date: </td>
	    		<td><sj:datepicker changeMonth="true" changeYear="true" id="fromDt"
				   name="fromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
			   	<td>To Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="toDt"
				   name="toDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
    		</tr>
    		<tr>
    			<td class="label">Budget Year: </td>
    			<td class="autocompleter"><%-- <sj:autocompleter
									id="budgYear"
									name="budgYear"
									list="budgetYearSel"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									onChangeTopics="reloadLists"/> --%>
									<s:select id="budgYear" name="budgYear" list="budgetYearSel" 
									listKey="itemVal" listValue="itemLabel" onchange="budgetYearSelected();" label="Parks" theme="simple"/></td>
    			<td class="label">Program Year: </td>
    			<td><s:textfield id="progYear" name="progYear" theme="simple" title="Program Year" /></td>
    		</tr>
    		<tr>
    			<td class="label">Journal Line Reference: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="jlr"
									name="jlr"
									list="jlrLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="false"
									loadMinimumCount="2"
									theme="simple"/></td>
    			<td class="label">Account: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="account"
									name="account"
									list="accountLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
    		</tr>
    		<tr>
    			<td class="label">Fund: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="fund"
									name="fund"
									list="fundLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
    			<td class="label">Org: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="org"
									name="org"
									list="orgLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
    		</tr>
    		<tr>
    			<td class="label">Subclass: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="subClass"
									name="subClass"
									list="subClassLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
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
									theme="simple" 
									/></td>
    		</tr>
    		<tr>
    			<td class="label">Transaction Group Identifier: </td>
    			<td><s:textfield id="txnGrpIdentifier" name="txnGrpIdentifier" theme="simple" title="Transaction Group Identifier" /></td>
    		</tr>
    		<tr>
    			<td class="label">System Activity Type Code: </td><!-- Example A3 -->
    			<td class="autocompleter"><sj:autocompleter
									id="sysActTypeCd"
									name="sysActTypeCd"
									list="sysActTypeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
    			<td class="label">Transaction Type Code: </td>
    			<td class="autocompleter"><sj:autocompleter
									id="transGrpType"
									name="transGrpType"
									list="transGrpTypeLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
    		</tr>
    		<tr>
    			<td class="label">Summary Approval Status: </td>
				<td><s:select id="sumAppStat"  name="sumAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/></td>
				<td class="label">Interface Approval Status: </td>
				<td><s:select id="intAppStat"  name="intAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}"/></td>
    		</tr>
   		</table>
	</form>	
</fieldset>
