<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>


	<s:url id="accountMasterGrid" action="alsAccount/accountMasterGrid_buildgrid" />
	<s:url id="accountMasterGridEdit" action="alsAccount/accountMasterGridEdit_execute" />    
	<sjg:grid
		id="accountMasterTable"
		caption="Account Master"
		href="%{accountMasterGrid}"
		editurl="%{accountMasterGridEdit}"		
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
    						  beforeSubmit: function (postData) {
    						  		postData.budgYear = $('#budgetYearSel').val();
    						  		return[true, ''];
    						   },  
    						  afterSubmit:errorHandler,
    						  afterShowForm:setEnabled,	 
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       afterShowForm:setDisabled,	
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
    	reloadTopics="reloadGrids"
    	onGridCompleteTopics="reloadLinkTab"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="500"
		formIds="gridFrm" >

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacBudgetYear" index="idPk.asacBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="false" editable="false" search="false" align="right"/>
			<sjg:gridColumn name="idPk.aamAccount" index="idPk.aamAccount" title =" Account" width="25" sortable="false" hidden="false" editable="true" editrules="{required:true}" editoptions="{size:7,maxlength:6}" align="right"/>
			<sjg:gridColumn name="aamAccountDesc" index="aamAccountDesc" title =" Desc" width="100" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="budgYear" index="budgYear" title ="" width="25" sortable="false" hidden="true" editable = "true" />
			
	</sjg:grid>
	<br>
	<input id="getAccountMasterRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV">&nbsp;&nbsp;&nbsp;
