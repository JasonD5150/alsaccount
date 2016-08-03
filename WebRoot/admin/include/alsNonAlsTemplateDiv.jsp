<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<form id='lstFrm'>
	<s:hidden id="frmAccountLst" name="accountLst"/>		
	<s:hidden id="frmFundLst" name="fundLst"/>
	<s:hidden id="frmSubClassLst" name="subClassLst"/>
	<s:hidden id="frmJlrLst" name="jlrLst"/>
	<s:hidden id="frmProjectGrantLst" name="projectGrantLst"/>
</form>
	
   	<s:url id="alsNonAlsTemplateGrid" action="alsAccount/alsNonAlsTemplateGrid_buildgrid" />
	<s:url id="alsNonAlsTemplateGridEdit" action="alsAccount/alsNonAlsTemplateGridEdit_execute" />    
	<sjg:grid
		id="alsNonAlsTemplateTable"
		caption="Templates"
		href="%{alsNonAlsTemplateGrid}"
		editurl="%{alsNonAlsTemplateGridEdit}"		
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
    						  beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#drGridBudgYear').val();
		    	                      	return[true, ''];
		    	              },	   
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Template Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Template Info',
    	                       beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#drGridBudgYear').val();
		    	                      	return[true, ''];
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
		height="200"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadGrids"
		onSelectRowTopics="templateSelected"
		onBeforeTopics="setTemplateGridLists">

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.anatBudgetYear" index="idPk.anatBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="idPk.anatCd" index="idPk.anatCd" title =" Template Code" width="25" sortable="false" hidden="false" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="anatDesc" index="anatDesc" title ="Description" width="25" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="anatDrAccount" index="anatDrAccount" title ="Debit Account" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}" align="right"/>
			<sjg:gridColumn name="anatCrAccount" index="anatCrAccount" title ="Credit Account" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}" align="right"/>
			<sjg:gridColumn name="anatFund" index="anatFund" title ="Fund" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
			<sjg:gridColumn name="anatDrSubclass" index="anatDrSubclass" title ="Debit Sub-Class" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="anatDrProjectGrant" index="anatDrProjectGrant" title ="Debit Project Grant" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="anatDrJournalLineRefr" index="anatDrJournalLineRefr" title ="Debit Journal Line Reference" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="anatDrJournalLineRefrDesc" index="anatDrJournalLineRefrDesc" title ="Debit Journal Line Reference" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="anatDrLineDesc" index="anatDrLineDesc" title ="Debit Line Desc." width="25" sortable="false" hidden="false" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="anatCrSubclass" index="anatCrSubclass" title ="Credit Sub-Class" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="anatCrProjectGrant" index="anatCrProjectGrant" title ="Credit Project Grant" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="anatCrJournalLineRefr" index="anatCrJournalLineRefr" title ="Credit Journal Line Reference" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="anatCrJournalLineRefrDesc" index="anatCrJournalLineRefrDesc" title ="Credit Journal Line Reference" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" align="right"/>
			<sjg:gridColumn name="anatCrLineDesc" index="anatCrLineDesc" title ="Credit Line Desc." width="25" sortable="false" hidden="false" editable="true" editrules="{required:true}"/>
			
			

	</sjg:grid>

