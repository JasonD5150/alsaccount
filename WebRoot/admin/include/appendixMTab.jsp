<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

    <form id='tranCodeFrm'>
			<s:hidden id="frmActivityType" name="activityType" />
	</form>
  

	<s:url id="appendixM1Grid" action="alsAccount/appendixM1Grid_buildgrid" />
	<s:url id="appendixM1GridEdit" action="alsAccount/appendixM1GridEdit_execute" />    
	<sjg:grid
		id="appendixM1Table"
		caption="Activity Type Codes"
		href="%{appendixM1Grid}"
		editurl="%{appendixM1GridEdit}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
    	navigatorAddOptions="{width:500,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:true,
    	                       editCaption:'Edit Code Info',
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="typeCodeModel"
		rownumbers="false"
		editinline="false"
    	onSelectRowTopics="activityTypeCodesSelect"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="250"
		width="950"
		rowNum="500"
		formIds="gridFrm">

			<sjg:gridColumn name="asacSystemActivityTypeCd" title ="Activity Type" width="25" hidden="false" editable="true" key="true" editoptions="{size:2,maxlength:1}"/>
			<sjg:gridColumn name="asatcDesc" index="asatcDesc" title =" Desc" width="125" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="asatcWhoLog" index="asatcWhoLog" title =" Create" width="20" sortable="false" hidden="false" editable="false" />
			<sjg:gridColumn name="asatcWhenLog" index="asatcWhenLog" title =" Create Date" width="40" sortable="false" hidden="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>
			<sjg:gridColumn name="asatcWhoModi" index="asatcWhoModi" title =" Mod" width="30" sortable="false" hidden="false" editable="false" />
			<sjg:gridColumn name="asatcWhenModi" index="asatcWhenModi" title =" Mod Date" width="40" sortable="false" hidden="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>

	</sjg:grid>
	
	<br>
	<s:url id="alsSysActivityTypeTranCdsDivUrl" value="alsSysActivityTypeTranCdsDiv_input.action"/>
	<sj:div id="alsSysActivityTypeTranCdsDiv" style='width:800px;'
		    		href="%{alsSysActivityTypeTranCdsDivUrl}" 
		    		reloadTopics="reloadTranCdsDiv" 
		    		deferredLoading="true">
	</sj:div> 
	<br>
	<input id="getAppendixMRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV">&nbsp;&nbsp;&nbsp;
