<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;">
	<legend style="font-weight: bold;">Search Criteria</legend>
	   	<form id='gridFrm'>
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
		       			theme="simple"
		       			/></td>
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
				<td class="label">System Sales</td>
				<td><s:textfield id="sysSales" name="sysSales" theme="simple" title="System Sales" type="number" step="any" min="0"/></td>
				<td class="label">EFT DDD</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="eftDdd"
									name="eftDdd" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period End Date" 
									showOn="focus" onblur="testDate(this)" /></td>
		    </tr>
		    <tr>			
				<td class="label">OTC Sales</td>
				<td><s:textfield id="otcSales" name="otcSales" theme="simple" title="OTC Sales" type="number" step="any" min="0"/></td>
				<td class="label">PAEs</td>
				<td><s:textfield id="pae" name="pae" theme="simple" title="PAEs" type="number" step="any" min="0"/></td>
		    </tr>
		    
		    <tr>	
		    	<td class="label">Non ALS Sales</td>
				<td><s:textfield id="nonAlsSales" name="nonAlsSales" theme="simple" title="Non ALS Sales" type="number" step="any" min="0"/></td>		
				<td class="label">Credit Card Sales</td>
				<td><s:textfield id="crCardSales" name="crCardSales" theme="simple" title="Credit Card Sales" type="number" step="any" min="0"/></td>
		    </tr>
		    <tr>			
				<td class="label">Total Short of Sales</td>
				<td><s:textfield id="totShortOfSales" name="totShortOfSales" theme="simple" title="Total Short of Sales" type="number" step="any" min="0"/></td>
				<td class="label">Total Over Sales</td>
				<td><s:textfield id="totOverSales" name="totOverSales" theme="simple" title="Total Over Sales" type="number" step="any" min="0"/></td>
		    </tr>
		   <tr>	
				<td class="label">Date Completed by Provider</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="comByProvDt"
									name="comByProvDt" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Completed by Provider" 
									showOn="focus" onblur="testDate(this)" /></td>	
				<td class="label">Remittance Approved</td>
				<td><s:checkbox id="app" name="app" theme="simple"/></td>
		    </tr>
		    <tr>
		    	<td class="label">Approved By</td>
				<td><s:textfield id="appBy" name="appBy" theme="simple" title="" /></td>
				<td class="label">Date</td>
				<td><sj:datepicker changeMonth="true" changeYear="true" id="appDt"
									name="appDt" displayFormat="mm/dd/yy" placeholder="mm/dd/yyyy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date" 
									showOn="focus" onblur="testDate(this)" /></td>
		    </tr> 
		    <tr>
		    	<td class="label">Comments</td>
				<td><s:textfield id="appCom" name="appCom" title="Comments" theme="simple"/></td>
		    </tr>
		    </table>
		</form>	
</fieldset>