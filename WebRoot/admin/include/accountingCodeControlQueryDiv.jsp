<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<form id='lstFrm'>
	<s:hidden id="frmAccountLst" name="accountLst"/>			
</form>

 <fieldset style="border: black 1px solid; display: inline-block;">
<legend style="font-weight: bold;">Search Criteria</legend>
   	<form id='gridFrm'>
   		<table>
   			<tr>
   				<td class="label">Budget Period: </td>
   				<td><s:select id="budgYear" name="budgYear" list="budgYearLst" listKey="itemVal" listValue="itemLabel" title="Budget Year" theme="simple" label="Budget Year " cssStyle="width:130px" onchange="$.publish('reloadAccCodeControlQueryDiv')"/></td>
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
	    </tr>
	    <tr>
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
	    	<td class="label">Accounting Code: </td>
			<td class="autocompleter" colspan="3"><sj:autocompleter
					id="accCd"
					name="accCd"
					list="accCdLst"
					listValue="itemLabel"
					listKey="itemVal"
					selectBox="true"
					selectBoxIcon="true"
					forceValidOption="true"
					loadMinimumCount="2"
					theme="simple"/>
			</td>
	    </tr>
	    </table>
	</form>	
</fieldset>

