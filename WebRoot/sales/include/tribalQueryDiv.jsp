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
				<td class="label">Tribe Code</td>
				<td class="autocompleter"><sj:autocompleter
									id="tribeCd"
									name="tribeCd"
									value="1SK"
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
    			<td class="label">Provider No: </td>
				<td class="autocompleter"><sj:autocompleter
									id="provNo"
									name="provNo"
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
    			<td class="label">Usage Period: </td>
				<td class="autocompleter"><sj:autocompleter
						id="usagePeriod"
						name="usagePeriod"
						list="usagePeriodLst"
						listValue="itemLabel"
						listKey="itemVal"
						selectBox="true"
						selectBoxIcon="true"
						forceValidOption="true"
						loadMinimumCount="2"
						theme="simple"/></td>
    		</tr>
    		<tr>			
				<td class="label">From Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="fromDt"
				   name="fromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
				<td class="label">To Date: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="toDt"
				   name="toDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
			</tr>
    		<tr>			
				<td class="label">Billing Period From: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpFromDt"
				   name="bpFromDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
				<td class="label">Billing Period To: </td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
				   name="bpToDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy" requiredLabel="true"/></td>
			</tr>
   		</table>
	</form>	
</fieldset>
