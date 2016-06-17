<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>


<s:url id="alsNonAlsOrgControlGrid" action="alsAccount/alsNonAlsOrgControlGrid_buildgrid" />
<s:url id="alsNonAlsOrgControlGridEdit" action="alsAccount/alsNonAlsOrgControlGridEdit_execute" /> 

<form id='lstFrm'>
	<s:hidden id="frmOrgLst" name="orgLst"/>
	<s:hidden id="frmProviderLst" name="providerLst"/>			
</form>
	
<div style="display: inline-block">
			<sjg:grid
				id="alsNonOrgControlDrTable"
				caption="Debit"
				href="%{alsNonAlsOrgControlGrid}"
				editurl="%{alsNonAlsOrgControlGridEdit}"		
				dataType="json"
				pager="true"
				navigator="true"
				navigatorEdit="true"
				navigatorView="true"
				navigatorAdd="true"
				navigatorDelete="true"
				navigatorSearch="false"
				navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
		    	navigatorAddOptions="{width:500,reloadAfterSubmit:true,
		    						  addedrow:'last',
		    						  beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#drGridBudgYear').val();
		    	                      	postData.crDrCd = $('#drGridTranCd').val();
		    	                      	postData.tempCd = $('#drGridTempCd').val();
		    	                      	return[true, ''];
		    	                      },	    
		    						  afterSubmit:errorHandler,
		    	                      addCaption:'Add Debit Info',
		    	                      closeAfterAdd:true,
		    	                      processData:'Adding Row to Database'}"
		    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
		    	                       editCaption:'Edit Debit Info',
		    	                       beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#crGridBudgYear').val();
		    	                      	postData.crDrCd = $('#crGridTranCd').val();
		    	                      	postData.tempCd = $('#crGridTempCd').val();
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
				height="100"
				width="472"
				rowNum="1000"
				formIds="drGridFrm"
				reloadTopics="reloadGrids"
				onBeforeTopics="alsNonOrgControlDrComplete"
				cssStyle="display: inline-block">
		
					<sjg:gridColumn name="anaocId" title ="id" width="55" hidden="true" key="true"/>
					<sjg:gridColumn name="anaocOrg" index="anaocOrg" title =" Org" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
					<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title =" Provider No" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}" />
					<sjg:gridColumn name="apiProviderName" index="apiProviderName" title =" Provider Name" width="25" sortable="false" hidden="false" editable="false" />
					<sjg:gridColumn name="budgYear" index="budgYear" title ="" width="25" sortable="false" hidden="true"/>
					<sjg:gridColumn name="crDrCd" index="crDrCd" title ="" width="25" sortable="false" hidden="true"/>
					<sjg:gridColumn name="tempCd" index="tempCd" title ="" width="25" sortable="false" hidden="true"/>
					
			</sjg:grid>
</div>
<div style="display: inline-block">
			<sjg:grid
				id="alsNonOrgControlCrTable"
				caption="Credit"
				href="%{alsNonAlsOrgControlGrid}"
				editurl="%{alsNonAlsOrgControlGridEdit}"		
				dataType="json"
				pager="true"
				navigator="true"
				navigatorEdit="true"
				navigatorView="true"
				navigatorAdd="true"
				navigatorDelete="true"
				navigatorSearch="false"
				navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
		    	navigatorAddOptions="{width:500,reloadAfterSubmit:true,
		    						  addedrow:'last',	
		    						  beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#crGridBudgYear').val();
		    	                      	postData.crDrCd = $('#crGridTranCd').val();
		    	                      	postData.tempCd = $('#crGridTempCd').val();
		    	                      	return[true, ''];
		    	                      },	    
		    						  afterSubmit:errorHandler,
		    	                      addCaption:'Add Credit Info',
		    	                      closeAfterAdd:true,
		    	                      processData:'Adding Row to Database'}"
		    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
		    	                       editCaption:'Edit Credit Info',
		    	                       beforeSubmit:function(postData){
		    	                      	postData.budgYear = $('#crGridBudgYear').val();
		    	                      	postData.crDrCd = $('#crGridTranCd').val();
		    	                      	postData.tempCd = $('#crGridTempCd').val();
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
				height="100"
				width="472"
				rowNum="1000"
				formIds="crGridFrm"
				reloadTopics="reloadGrids"
				onBeforeTopics="alsNonOrgControlCrComplete"
				cssStyle="display: inline-block">
		
					<sjg:gridColumn name="anaocId" title ="id" width="55" hidden="true" key="true"/>
					<sjg:gridColumn name="anaocOrg" index="anaocOrg" title =" Org" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
					<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title =" Provider No" width="25" sortable="false" hidden="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" editrules="{required:true}"/>
					<sjg:gridColumn name="apiProviderName" index="apiProviderName" title =" Provider Name" width="25" sortable="false" hidden="false" editable="false" />
					
			</sjg:grid>

	</div>