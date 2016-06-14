<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

	<form id='lstFrm'>
		<s:hidden id="frmAccountLst" name="accountLst"/>
		<s:hidden id="frmActTypeTranCdLst" name="sysActivityTypeTransCodeLst"/>					
	</form>
	
	<s:url id="activityAccountLinkGrid" action="alsAccount/activityAccountLinkageGrid_buildgrid" />
	<s:url id="activityAccountLinkGridEdit" action="alsAccount/activityAccountLinkageGridEdit_execute" />
   
	<sjg:grid
		id="activityAccountLinkTable"
		caption="Activity Account Linkage"
		href="%{activityAccountLinkGrid}"
		editurl="%{activityAccountLinkGridEdit}"		
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
    						  afterSubmit:errorHandler,
    						  beforeShowForm: function (formid) {
    						  					document.getElementById('aamAccount').selectedIndex = '0';},
    						   beforeSubmit:checkForm,  	  
							  afterShowForm:setEnabled,	 
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       closeAfterEdit:true,
							   afterShowForm:setDisabled,
							    beforeSubmit:checkForm,  	  	
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		reloadTopics="reloadLinkTable"
		onBeforeTopics="activityAccountLinkComplete"
    	onGridCompleteTopics=""
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="500" 
		formIds="gridFrm">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacBudgetYear" index="idPk.asacBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="false" editable="false" search="false"/>
			<sjg:gridColumn name="sysActTypeTransCd" index="sysActTypeTransCd" title ="Activity Type / Transaction Code" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" />
			<%-- <sjg:gridColumn name="idPk.asacSystemActivityTypeCd" index="idPk.asacSystemActivityTypeCd" title =" Sys Activity Type Code" width="10" sortable="false" hidden="false" editable="false" editrules="{required:true}" editoptions="{size:2,maxlength:1}"/>
			<sjg:gridColumn name="idPk.asacTxnCd" index="idPk.asacTxnCd" title =" Transaction Code" width="25" sortable="false" hidden="false" editable="false" editrules="{number:true,required:true}"  editoptions="{size:4,maxlength:3}"/> --%>
			<sjg:gridColumn name="idPk.aaalDrCrCd" index="idPk.aaalDrCrCd" title =" Credit / Debit" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}" editrules="{required:true}" />
			<sjg:gridColumn name="aamAccount" index="aamAccount" title =" Account" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" />
			<sjg:gridColumn name="aaalReference" index="aaalReference" title =" Open Item Key Indicator" width="25" sortable="false" hidden="false" editable = "true" edittype="select" editoptions="{value: {'':'',999: 'YES'}}"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="25" sortable="false" hidden="false" editable="true" editoptions="{size:7,maxlength:6}"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="25" sortable="false" hidden="false" editable = "true" editoptions="{size:7,maxlength:6}"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Sub-Class" width="25" sortable="false" hidden="false" editable = "true" editoptions="{size:6,maxlength:5}"/>
			<sjg:gridColumn name="aaalAccountingCdFlag" index="aaalAccountingCdFlag" title ="Details in Accounting Code Control Table" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="budgYear" index="budgYear" title ="" width="25" sortable="false" hidden="true" editable = "true" />
			
	</sjg:grid>
	<br>
	<input id="getActivityAccountLinkageRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV">&nbsp;&nbsp;&nbsp;
			  
