<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

	<form id='lstFrm'>
		<s:hidden id="frmAccountLst" name="accountCdLst"/>
		<s:hidden id="frmProviderLst" name="providerLst"/>			
	</form>
	
	<s:url id="orgControlGrid" action="alsAccount/orgControlGrid_buildgrid" />
	<s:url id="orgControlGridEdit" action="alsAccount/orgControlGridEdit_execute" />
   
	<sjg:grid
		id="orgControlTable"
		caption="Org Control"
		href="%{orgControlGrid}"
		editurl="%{orgControlGridEdit}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
    	navigatorAddOptions="{width:600,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  afterShowForm:setEnabled,
    						  afterSubmit:errorHandler,
    						   beforeSubmit: function (postData) {
    						  		rtrnstate = true; 
	    							rtrnMsg = ''; 
    						  		
   						  			postData.budgYear = $('#budgetYearSel').val();

   						  			return [rtrnstate,rtrnMsg]; 
    						   },  	    
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterShowForm:setDisabled,
    	                       beforeSubmit: function (postData) {
    						  		rtrnstate = true; 
	    							rtrnMsg = ''; 
    						  		
   						  			postData.budgYear = $('#budgetYearSel').val();

   						  			return [rtrnstate,rtrnMsg]; 
    						   }, 
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="500" 
		formIds="gridFrm"
		reloadTopics="reloadGrids"
		onBeforeTopics="orgControlComplete">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.aaccAccCd" index="idPk.aaccAccCd" title ="Account Code" width="10" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
			<sjg:gridColumn name="idPk.apiProviderNo" index="idPk.apiProviderNo" title ="Issuing Provider Num" width="15" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="15" sortable="false" hidden="false" editable = "true" editrules="{required:true}"/>
			<sjg:gridColumn name="provRegion" index="provRegion" title ="Provider Region Num" width="10" sortable="false" hidden="false" editable = "false" search="false"/>
			<sjg:gridColumn name="provName" index="provName" title ="Provider Name" width="50" sortable="false" hidden="false" editable = "false" search="false"/>
			
			
	</sjg:grid>
