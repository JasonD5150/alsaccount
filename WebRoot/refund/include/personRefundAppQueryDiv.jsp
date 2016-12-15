<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<s:form id="gridFrm" 
		cssStyle="padding-bottom: 4px;"
        theme="simple"
        validate="false"> 
<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;" >
   	<legend style="font-weight: bold;" class="fwp-exclude-from-show-hide">Search Criteria</legend>
	   	<table>
	   		<tr>
	   			<td class="label">Date of Birth: </td>
				<td><sj:datepicker id="dob" name="dob"
                                   showOn="none"
                                   cssClass="fwp-date-input" 
                                   size="12" 
                                   theme="simple"/></td>
                <td class="label">ALS No: </td>
				<td><s:textfield name="alsNo"
                                 type="number"
                                 maxlength="8" 
                                 theme="simple"/></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Usage Period From: </td>
				<td><sj:datepicker id="upFrom" name="upFrom"
                                   showOn="none"
                                   cssClass="fwp-date-input" 
                                   size="12" 
                                   theme="simple"/></td>
                <td class="label">Usage Period To: </td>
				<td><sj:datepicker id="upTo" name="upTo"
                                   showOn="none"
                                   cssClass="fwp-date-input" 
                                   size="12" 
                                   theme="simple"/></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Item Type Code: </td>
				<td class="autocompleter"><sj:autocompleter id="itemTypeCd"
															name="itemTypeCd"
															list="%{itemTypeLst}"
															listValue="itemLabel"
															listKey="itemVal"
															selectBox="true"
															selectBoxIcon="true"
															forceValidOption="true"
															loadMinimumCount="2"
															theme="simple"/></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Refund Reason: </td>
				<td class="autocompleter"><sj:autocompleter id="reasonCd"
															name="reasonCd"
															list="%{reasonTypeCdLst}"
															listValue="itemLabel"
															listKey="itemVal"
															selectBox="true"
															selectBoxIcon="true"
															forceValidOption="true"
															loadMinimumCount="2"
															theme="simple" /></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Item Indicator: </td>
				<td class="autocompleter"><sj:autocompleter id="itemIndCd"
															name="itemIndCd"
															list="%{itemIndLst}"
															listValue="itemLabel"
															listKey="itemVal"
															selectBox="true"
															selectBoxIcon="true"
															forceValidOption="true"
															loadMinimumCount="2"
															theme="simple" /></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Application Type: </td>
				<td class="autocompleter"><sj:autocompleter id="appTypeCd"
															name="appTypeCd"
															list="%{appTypeCdLst}"
															listValue="itemLabel"
															listKey="itemVal"
															selectBox="true"
															selectBoxIcon="true"
															forceValidOption="true"
															loadMinimumCount="2"
															theme="simple" /></td>
	   		</tr>
	   		<tr>
	   			<td class="label">Warrant Creation Date: </td>
				<td><sj:datepicker id="warCreateDt" name="warCreateDt"
                                   showOn="none"
                                   cssClass="fwp-date-input" 
                                   size="12" 
                                   theme="simple"/></td>
	   		</tr>
			<tr>
				<td class="label">Item Fee Refund Approve: </td>
				<td><s:radio name="itemFeeRefApp" 
							 list="#{'Y':'Yes','N':'No','O':'None'}"
							 theme="simple"/></td>
			</tr>
			<tr>
				<td class="label">Application Fee Refund Approve: </td>
				<td><s:radio name="appFeeRefApp" 
							 list="#{'Y':'Yes','N':'No','O':'None'}"
							 theme="simple"/></td>
			</tr>
			<tr>
				<td class="label">Preference Fee Refund Approve: </td>
				<td><s:radio name="prefFeeRefApp" 
							 list="#{'Y':'Yes','N':'No','O':'None'}"/></td>
			</tr>
			<tr>
				<td class="label">No Warrant: </td>
    			<td><s:checkbox id="noWarrant" name="noWarrant" theme="simple" label="No Warrant Created"></s:checkbox></td>
				<td class="label">Search All Dates: </td>
    			<td><s:checkbox id="srchAll" name="srchAll" theme="simple" label="All"></s:checkbox></td>
			</tr>
			<tr>
				<td class="label">Minimum Refund: </td>
    			<td><s:checkbox id="minRefund" name="minRefund" theme="simple" label="Minimum Refund"></s:checkbox></td>
				<td class="label">Has Comments: </td>
    			<td><s:checkbox id="hasComments" name="hasComments" theme="simple" label="Has Comments"></s:checkbox></td>
			</tr>
	   	</table>
</fieldset>
</s:form>
