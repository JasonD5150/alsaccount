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
	
   <s:url id="accCodeControlGrid" action="alsAccount/accCodeControlGrid_buildgrid" />
	<s:url id="accCodeControlGridEdit" action="alsAccount/accCodeControlGridEdit_execute" />    
	<sjg:grid
		id="accCodeControlTable"
		caption="Account Codes"
		href="%{accCodeControlGrid}"
		editurl="%{accCodeControlGridEdit}"		
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
    						  afterShowForm:setEnabled,
    						  beforeSubmit:checkForm,  	    
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterShowForm:setDisabled,
    	                       beforeSubmit:checkForm,	    
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
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadGrids"
		onBeforeTopics="accCodeControlComplete">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacBudgetYear" index="idPk.asacBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="idPk.aaccAccCd" index="idPk.aaccAccCd" title =" Account Code" width="25" sortable="false" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:4,maxlength:3}"/>
			<sjg:gridColumn name="idPk.aaccSeqNo" index="idPk.aaccSeqNo" title =" Seq No" width="25" sortable="false" hidden="false" editable="false" editrules="{number:true,required:true}" editoptions="{size:3,maxlength:2}"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title =" Account" width="40" sortable="false" hidden="false" editable="true" editrules="{required:true}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aaccFund" index="aaccFund" title =" Fund" width="30" sortable="false" hidden="false" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="aaccAllocatedAmt" index="aaccAllocatedAmt" title =" Allocated Amount" width="40" sortable="false" hidden="false" editable="true" />
			<sjg:gridColumn name="aaccJlrRequired" index="aaccJlrRequired" title =" Open Item Key Indicator" width="20" sortable="false" hidden="false" editable="true" 
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title =" Org" width="20" sortable="false" hidden="false" editable="true"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title =" Subclass" width="25" sortable="false" hidden="false" editable="true" />
			<sjg:gridColumn name="aaccOrgFlag" index="aaccOrgFlag" title =" Multiple Orgs" width="20" sortable="false" hidden="false" editable="true" 
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aaccBalancingAmtFlag" index="aaccBalancingAmtFlag" title =" Balancing Amt Flag" width="20" sortable="false" hidden="false" editable="true"  
				edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="aaccRemarks" index="aaccRemarks" title =" Remarks" width="100" sortable="false" hidden="false" editable="true" edittype="textarea"/>
			

	</sjg:grid>

