<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;">
	<legend style="font-weight: bold;">Search Criteria</legend>
	   	<form id='gridFrm'>
	   	<p><b>Query defaults to the past 12 months, if "Search All Dates" is not selected or a date is not used in the query.</b></p>
	   		<table>
   			<tr>			
				<td class="label">Provider No</td>
				<td><s:select 
			  			id="provNo"
			  			name="provNo"
			  			list="providerLst"
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
		    	<td class="label">Completed By Provider</td>
				<td><s:radio id="comByProv" name="comByProv" theme="simple" label="Completed By Provider" list="#{'Y':'Yes','N':'No'}" /></td>
				<td class="label">Date</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="comByProvDt"
									name="comByProvDt" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Completed by Provider" 
									showOn="focus" onblur="testDate(this)" /></td>	
		    </tr>
		    <tr>
		    	<td class="label">Remittance Approved</td>
				<td><s:radio id="app" name="app" theme="simple" label="Remittance Approved" list="#{'Y':'Yes','N':'No'}" /></td>
		    	<td class="label">By</td>
				<td><s:textfield id="appBy" name="appBy" theme="simple" title="" maxlength="6" size="7"/></td>
				<td class="label">Date</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="appDt"
									name="appDt" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date" 
									showOn="focus" onblur="testDate(this)" /></td>
		    	<td class="label">Comments</td>
				<td><s:textfield id="appCom" name="appCom" title="Comments" theme="simple"/></td>
		    </tr> 
		    </table>
		    <table>
		    <tr>
		    	<td class="label">Has Non Als Details</td>
				<td><s:checkbox id="hasNonAlsDetails" name="hasNonAlsDetails" theme="simple" label="Has Non Als Details"></s:checkbox></td>
				<td class="label">Has Over/Short Details</td>
				<td><s:checkbox id="hasOverShortDetails" name="hasOverShortDetails" theme="simple" label="Has Over/Short Details"></s:checkbox></td>
				<td class="label">Has PAE Amount</td>
				<td><s:checkbox id="hasPaeAmt" name="hasPaeAmt" theme="simple" label="Has PAE Amount"></s:checkbox></td>
				<td class="label">Search All Dates: </td>
    			<td><s:checkbox id="srchAll" name="srchAll" theme="simple" label="All"></s:checkbox></td>
		    </tr>
		    </table>
		</form>	
</fieldset>