<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
   	<legend style="font-weight: bold">Search Criteria</legend>
   	<s:actionerror/>
   	<form id='gridFrm'>
   	<p><b>Query defaults to the past 12 months, if "Search All Dates" is not selected or a date is not used in the query.</b></p>
   		<table>
   			<tr>
    			<td class="label">Tx Group type: </td>
				<td colspan="3"><s:select 
    					id="srchTransGrpType"
						name="srchTransGrpType"
						list="transGroupTypeLst"
						listKey="itemVal" 
				  		listValue="itemLabel" 
			       		headerKey=" "
						headerValue=" " 
			       		theme="simple"
			       		onchange="transGrpTypeChanged(this)"/></td>
    		</tr>
    		<tr>
    			<td class="label">Provider: </td>
    			<td><s:url id="provListUrl" action="getProvLst" /> 
					<sj:select id="srchProviderNo"
							   name="srchProviderNo"
							   theme="simple"
							   href="%{provListUrl}"
						  	   listKey="itemVal" 
						  	   listValue="itemLabel"
						  	   headerKey=" "
							   headerValue=" " 
						  	   list="providerLst"
						  	   formIds="lstFrm"
						  	   reloadTopics="reloadProvLst"
						  	   onchange="provNoChanged(this)"/></td>
    		</tr>
			<tr>
    			<td>Transaction Group Identifier: </td>
	    		<td><s:url id="transGrpIdListUrl" action="getTransGrpIdLst" /> 
					<sj:select id="srchTranGrpId"
							   name="srchTranGrpId"
							   theme="simple"
							   href="%{transGrpIdListUrl}"
						  	   listKey="itemVal" 
						  	   listValue="itemLabel"
						  	   headerKey=" "
							   headerValue=" " 
						  	   list="transGrpIdLst"
						  	   formIds="lstFrm"
						  	   reloadTopics="reloadTransGrpIdLst"
						  	   deferredLoading="true"/></td>
			   	<td>Transaction Group Created: </td>
	    	 	<td><s:select 
    					id="srchTranGrpCreated"
						name="srchTranGrpCreated"
						list="budgYearLst"
						listKey="itemVal" 
				  		listValue="itemLabel" 
			       		headerKey=" "
						headerValue=" " 
			       		theme="simple"/><%-- <sj:datepicker changeMonth="true" changeYear="true" id="srchTranGrpCreated"
				   name="srchTranGrpCreated" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Transaction Group Created" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/> --%></td>
				<td>Accounting Date: </td>
	    		<td><sj:datepicker changeMonth="true" changeYear="true" id="srchAccDt"
				   name="srchAccDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Accounting Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
    		</tr>
			<tr>
			   	<td>Summary Approval Status: </td>
			   	<td><s:select id="srchSumAppStat"  name="srchSumAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable', 'NS':'No Status'}"/></td>
	    	 	<td>Summary Approval Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="srchSumAppDt"
				   name="srchSumAppDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Summary Approval Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
    		</tr>
    		<tr>
			   	<td>Interface Approval Status: </td>
			   	<td><s:select id="srchIntAppStat"  name="srchIntAppStat"
						  headerKey="" headerValue="" theme="simple"
						  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable', 'NS':'No Status'}"/></td>
	    	 	<td>Summary Approval Date: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="srchIntAppDt"
				   name="srchIntAppDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Interface Approval Date" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
    		</tr>
    		<tr>
    			<td>Date Uploaded to Summary: </td>
	    	 	<td><sj:datepicker changeMonth="true" changeYear="true" id="srchUpToSumDt"
				   name="srchUpToSumDt" displayFormat="mm/dd/yy"
				   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Uploaded to Summary:" 
				   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
				<td class="label">Bank Code: </td>
    			<td><s:select 
			  			id="srchBankCd"
			  			name="srchBankCd"
			  			list="bankCdLst"
			  			listKey="itemVal" 
			  			listValue="itemLabel" 
		       			headerKey=""
						headerValue=" " 
		       			theme="simple"
		       			/></td>
    			<td class="label">Bank Reference No: </td>
    			<td><s:textfield id="srchBankRefNo" name="srchBankRefNo" theme="simple" title="Bank Reference No" /></td>
    		</tr>
    		<tr>
    			<td class="label">Interface File Name: </td>
    			<td><s:textfield id="srchIntFileNm" name="srchIntFileNm" theme="simple" title="Interface File Name" /></td>
    			<td class="label">Deposit Id: </td>
    			<td><s:textfield id="srchDepId" name="srchDepId" theme="simple" title="Deposit Id" /></td>
    			<td class="label">Search All Dates: </td>
    			<td><s:checkbox id="srchAll" name="srchAll" theme="simple" label="All"></s:checkbox></td>
    		</tr>    		
   		</table>
	</form>	
</fieldset>
