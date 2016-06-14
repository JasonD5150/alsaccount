<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>
  

	<s:url id="sysActivityControlGrid" action="alsAccount/sysActivityControlGrid_buildgrid" />
	<s:url id="sysActivityControlGridEdit" action="alsAccount/sysActivityControlGridEdit_execute" />    
	<sjg:grid
		id="sysActivityControlTable"
		caption="System Activity Control"
		href="%{sysActivityControlGrid}"
		editurl="%{sysActivityControlGridEdit}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
    	navigatorAddOptions="{width:500,reloadAfterSubmit:true, 
    						  beforeSubmit: function (postData) {
    						  		postData.budgYear = $('#budgetYearSel').val();
    						  		return[true, ''];
    						   },   
    						  afterSubmit:activityControlChanged,
    						  afterShowForm:setEnabled,	    	   
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       closeAfterEdit:true,
    	                       afterShowForm:setDisabled,	    	   
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:activityControlChanged}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
    	onGridCompleteTopics="sysActivityControlComplete"
    	onSelectRowTopics="reprintSelect"
    	reloadTopics="reloadGrids"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="500"
		formIds="gridFrm">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacBudgetYear" index="idPk.asacBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="false" editable="false" search="false"/>
			<sjg:gridColumn name="idPk.asacSystemActivityTypeCd" index="idPk.asacSystemActivityTypeCd" title ="Activity Type Code" width="10" sortable="false" hidden="false" editable="true" editrules="{required:true}" editoptions="{size:2,maxlength:1}"/>
			<sjg:gridColumn name="idPk.asacTxnCd" index="idPk.asacTxnCd" title =" Transaction Code" width="25" sortable="false" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:4,maxlength:3}"/>
			<sjg:gridColumn name="asacSysActivityTypeCdDesc" index="asacSysActivityTypeCdDesc" title =" Desc" width="100" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="asacProgram" index="asacProgram" title =" Program" width="25" sortable="false" hidden="false" editable = "false"/>
			<sjg:gridColumn name="sysActivityTypeDesc" index="sysActivityTypeDesc" title ="" width="25" sortable="false" hidden="true" editable = "false" />
			<sjg:gridColumn name="sysTranCodeDesc" index="sysTranCodeDesc" title ="" width="25" sortable="false" hidden="true" editable = "false" />
			
			<sjg:gridColumn name="budgYear" index="budgYear" title ="" width="25" sortable="false" hidden="true" editable = "true" />
	</sjg:grid>
	<br>
	<input id="getSysActivityControlRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV">&nbsp;&nbsp;&nbsp;
