<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

  
<s:form id="tribeBankItemFrm" theme="simple" action="addQuestionDlg_execute">
			<s:hidden id="aictUsagePeriodFrom" name="aictUsagePeriodFrom"/>
			<s:hidden id="aictUsagePeriodTo" name="aictUsagePeriodTo"/>
  			<s:hidden id="aictItemTypeCd" name="aictItemTypeCd"/>
  			<s:hidden id="itemKey" name="itemKey"/>
  			<sj:submit id="addTribeItemToBank" hidden="true" targets="errorDiv" />
  		</s:form>

	
	<s:url id="tribeBankItemDivGridURL" action="alsAccount/tribeBankItemGrid_buildgrid" /> 
	<s:url id="tribeBankItemDivGridEditURL" action="alsAccount/tribeBankItemGridEdit_execute" />    
	<sjg:grid 
	  		gridModel="model"               
			id="tribeBankItemTable"
			caption="Questions"       			
			dataType="json" 
			href="%{tribeBankItemDivGridURL}"              
			editurl="%{tribeBankItemDivGridEditURL}"
			navigator="true"                    
			navigatorAdd="false"
			navigatorEdit="false"               
			navigatorDelete="true"
			navigatorRefresh="true"             
			navigatorSearch="false" 
		    navigatorView="true"
			navigatorEditOptions="{width:600,reloadAfterSubmit:true,
			                       editCaption:'Edit Question',
			                       closeAfterEdit:true,
			                       afterSubmit:errorHandler,
			                       processData:'Updating question.'}"
			navigatorViewOptions="{width:600,
								   caption:'View Question',
								   reloadAfterSubmit:false}"    	
			navigatorDeleteOptions="{width:300,afterSubmit:errorHandler,reloadAfterSubmit:true}"
			reloadTopics="tribeBankItemTable"
			onGridCompleteTopics="tribeBankItemTableComplete"	
			formIds="tribeBankForm"                    
		    editinline="false"
		    rowNum="1000"                      
		    pager="true"
		    rownumbers="false"
		    shrinkToFit="true"
		    autowidth="true"                         
		    height="300"   
		         width = "800"             
		    viewrecords="true"
		    scroll="true"                       
		    scrollrows="true">
		        
	        <sjg:gridColumn name="itemKey" index="itemKey" key="true" title =" itemKey" width="6" sortable="false" hidden="true" editable="false"  />
			<sjg:gridColumn name="aictUsagePeriodFrom" index="aictUsagePeriodFrom" title =" Usage Period From" width="6" sortable="false" hidden="false" editable="true" editrules="{required:true}" editoptions="{size:6,maxlength:5}" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y'}"/>
			<sjg:gridColumn name="aictUsagePeriodTo" index="aictUsagePeriodTo" title =" Usage Period To" width="15" sortable="false"  editable="true" editrules="{required:true}" editoptions="{size:26, maxlength:25}" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>
			<sjg:gridColumn name="aictItemTypeCd" index="aictItemTypeCd" title =" Item Type" width="20" sortable="false" editable="true" editoptions="{size:26, maxlength:100}"/>
			<sjg:gridColumn name="aitTypeDesc" index="aitTypeDesc" title =" Tribe Code" width="20" sortable="false" editable="false" editoptions="{size:26, maxlength:25}"/>
  		</sjg:grid>
	
	
	