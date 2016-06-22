<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>


<h5> Note: Run after July 1st and/or after BPE that includes July 1st.
</h5>
<s:actionerror theme="jquery"/>
<table>
	<tr>
		<td class="label">Prior Budget Year:</td>
		<td><s:textfield id="budgYear" name="budgYear" theme="simple" size='4' maxlength='4' title="Budget Year" placeholder='YYYY' onblur="saveFyeAdjst()"/></td>
	</tr>
	<tr>
		<td class="label">Bill Period End Date:</td>
		<td><s:textfield id="billPeriodEnd" name="billPeriodEnd" theme="simple" size='12' maxlength='10' title="Bill Period End Date" placeholder='MM/DD/YYYY'/></td>
		<td>Last BPE in June(06/25/2015)</td>
	</tr>
	<tr>
		<td class="label">Fiscal Year End:</td>
		<td><s:textfield id="endFY" name="endFY" theme="simple" size='12' maxlength='10' title="Fiscal Year End" placeholder='MM/DD/YYYY' disabled="true"/></td>
	<tr>
		<td class="label">New Fiscal Year:</td>
		<td><s:textfield id="newFY" name="newFY" theme="simple" size='12' maxlength='10' title="New Fiscal Year" placeholder='MM/DD/YYYY' disabled="true"/></td>
	</tr>
</table>
<br>
<fieldset style="border: black 1px solid;display: inline-block;">
		<legend style="font-weight: bold;" dir="ltr">Transaction Group Name</legend>
	<s:textfield id="tranGrpNmFyeAdjust" name="tranGrpNmFyeAdjust" title="FYE Adjustment" label="FYE Adjustment" disabled="true"/><br>
	<s:textfield id="tranGrpNmNewFY" name="tranGrpNmNewFY" title="New Fiscal Year" label="New Fiscal Year" disabled="true"/>
</fieldset>
<br>
<h5> Note: Add dates after last BPE that are still in June(6/26-6/30 2015)<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If necessary to rerun for same budget year check with support staff prior to running, to ensure duplicates aren't created.<br>
	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The ALSs sales for the following days will be included in the FYE interface file.
</h5>
	<s:url id="alsSabhrsFyeAdjstDtlGrid" action="alsAccount/alsSabhrsFyeAdjstDtlGrid_buildgrid" />
	<s:url id="alsSabhrsFyeAdjstDtlGridEdit" action="alsAccount/alsSabhrsFyeAdjstDtlGridEdit_execute" />    
	<sjg:grid
		id="alsSabhrsFyeAdjstDtlTable"
		caption="Adjustment Dates"
		href="%{alsSabhrsFyeAdjstDtlGrid}"
		editurl="%{alsSabhrsFyeAdjstDtlGridEdit}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearch="false"
    	navigatorAddOptions="{width:500,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  beforeSubmit: function (postData) {
    						  		postData.budgYear = $('#budgYear').val();
    						  		return[true, ''];
    						   },
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Adjustment Date',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"  	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="400"
		rowNum="1000"
		formIds="adjParamFrm"
		reloadTopics="reloadAdjstDt">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asfaBudgetYear" index="idPk.asfaBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="idPk.asfadAdjstDt" index="asfadAdjstDt" title =" Adjustment Date" width="25" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y'}" editoptions="{size: 20, maxlength: 10, dataInit:datePick,attr:{title:'Select the date'} }"/>

	</sjg:grid>
<br>
<input id='createTransGroup' type='button' value='Create Transaction Group' onclick='fiscalYearEndAction(id)' disabled>
<input id='resetAdjstParamTab' type='button' value='Reset' onclick='resetAdjstParamTab()' >
	
	