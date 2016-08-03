<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
   	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
   	<s:actionerror/>
   	<form id='gridFrm'>
   		<table>
   			<tr>			
				<td class="label">Provider No: </td>
				<td><s:select 
			  			id="providerNo"
			  			name="providerNo"
			  			list="providerLst"
			  			listKey="itemVal" 
			  			listValue="itemLabel" 
		       			headerKey=""
						headerValue=" " 
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
				   showOn="focus" onblur="testDate(this)" /></td>
			   	<td>Billing Period To Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
				   name="bpToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" /></td>
    		</tr>
    		<tr><td colspan="4" style="border-bottom:1px solid black;"><br></td></tr>
    		<tr><td><br></td></tr>
    		<tr>
    			<td>From Date: </td>
	    		<td><sj:datepicker changeMonth="true" changeYear="true" id="fromDt"
				   name="fromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="From Date" 
				   showOn="focus" onblur="testDate(this)"/></td>
			   	<td>To Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="toDt"
				   name="toDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="To Date" 
				   showOn="focus" onblur="testDate(this)"/></td>
    		</tr>
    		<tr>
    			<td class="label">Budget Year: </td>
    			<td><s:select id="budgYear" 
    							name="budgYear" 
    							list="budgetYearSel" 
    							listKey="itemVal" 
    							listValue="itemLabel"
    							title="Budget Year" 
    							label="Budget Year " 
    							cssStyle="width:130px" 
    							onchange="reloadLists()" 
    							theme="simple"/></td>
    			<td class="label">Program Year: </td>
    			<td><s:textfield id="progYear" name="progYear" theme="simple" title="Program Year" /></td>
    		</tr>
    		<tr>
    			<td class="label">Journal Line Reference: </td>
    			<td><s:select 
    					id="jlr"
						name="jlr"
						list="jlrLst"
						listKey="itemVal" 
				  		listValue="itemLabel" 
			       		headerKey=" "
						headerValue=" " 
			       		theme="simple"/></td>
    			<td class="label">Account: </td>
    			<td><s:select id="account" 
							   name="account"
							   list="accountLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="Account"
							   theme="simple"/></td>
    		</tr>
    		<tr>
    			<td class="label">Fund: </td>
    			<td><s:select id="fund" 
							   name="fund"
							   list="fundLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="Fund"
							   theme="simple"/></td>
    			<td class="label">Org: </td>
    			<td><s:select id="org" 
							   name="org"
							   list="orgLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="Fund"
							   theme="simple"/></td>
    		</tr>
    		<tr>
    			<td class="label">Subclass: </td>
    			<td><s:select id="subClass" 
							   name="subClass"
							   list="subclassLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="Fund"
							   theme="simple"/></td>
    			<td class="label">Tribe Code: </td>
    			<td><s:select id="tribeCd" 
							   name="tribeCd"
							   list="tribeCdLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="Tribe Code"
							   theme="simple"/></td>
    		</tr>
    		<tr>
    			<td class="label">Transaction Group Identifier: </td>
    			<td><s:textfield id="txnGrpIdentifier" name="txnGrpIdentifier" theme="simple" title="Transaction Group Identifier" /></td>
    		</tr>
    		<tr>
    			<td class="label">System Activity Type Code: </td><!-- Example A3 -->
    			<td><s:select id="sysActTypeCd" 
							   name="sysActTypeCd"
							   list="sysActTypeCdLst" 
							   listKey="itemVal" 
							   listValue="itemLabel"
							   headerKey=" "
							   headerValue=" " 
							   label="System Activity Type Code"
							   theme="simple"/></td>
    			<td class="label">Transaction Type Code: </td>
    			<td><s:select 
    					id="transGrpType"
						name="transGrpType"
						list="transGroupTypeLst"
						listKey="itemVal" 
				  		listValue="itemLabel" 
			       		headerKey=" "
						headerValue=" " 
			       		theme="simple"/></td>
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
	<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
	<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
</fieldset>
