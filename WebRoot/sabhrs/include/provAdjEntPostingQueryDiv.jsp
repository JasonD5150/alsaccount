<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;">
	<legend style="font-weight: bold;">Search Criteria</legend>
	   	<form id='gridFrm'>
	   		<s:hidden id="frmRemittanceInd" name="remittanceInd" value="false"/>
	   		<table>
   			<tr>			
				<td class="label">Provider No</td>
				<td><s:select 
			  			id="provNo"
			  			name="provNo"
			  			list="provLst"
			  			listKey="itemVal" 
			  			listValue="itemLabel" 
		       			headerKey=""
						headerValue=" " 
		       			theme="simple"/></td>
		    </tr>
		    <tr>			
				<td class="label">Billing Period From</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpFrom"
									name="bpFrom" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period End Date" 
									showOn="focus" onblur="testDate(this)" /></td>
				<td class="label">Billing Period To</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpTo"
									name="bpTo" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period End Date" 
									showOn="focus" onblur="testDate(this)" /></td>
		    </tr>
		    <tr>
				<td class="label">Tribe Code</td>
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
			</tr>
			<tr>
				<td class="label">App Type Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="appTypeCd"
									name="appTypeCd"
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
    			<td class="label">Amount Type Code: </td>
				<td class="autocompleter"><sj:autocompleter
									id="amtTypeCd"
									name="amtTypeCd"
									list="amountTypeCdLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" /></td>
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
    		</tr>
		    </table>
		</form>	
</fieldset>