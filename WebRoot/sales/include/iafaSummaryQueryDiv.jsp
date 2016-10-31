<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
   	<legend style="font-weight: bold;">Search Criteria</legend>
   	<form id='gridFrm'>
   		<table>
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
    		</tr>
    		<tr>
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
    			<td class="label">Item Type Code: </td>
				<td class="autocompleter"><sj:autocompleter
						id="itemTypeCd"
						name="itemTypeCd"
						list="itemTypeLst"
						listValue="itemLabel"
						listKey="itemVal"
						selectBox="true"
						selectBoxIcon="true"
						forceValidOption="true"
						loadMinimumCount="2"
						theme="simple"/></td>
    		</tr>
    		<tr>
				<td class="label">App Type Code: </td>
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
    		</tr>
    		<%-- <tr>
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
    		</tr> --%>
    		<tr>
    			<td class="label">Usage Period From: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="upFromDt"
				   name="upFromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
				<td class="label">Usage Period To: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="upToDt"
				   name="upToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" /></td>
    		</tr>
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
   		</table>
	</form>	
</fieldset>
