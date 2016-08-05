<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>


	<s:url id="tribeBankItemDivGridURL" action="alsAccount/tribeBankItemGrid_buildgrid" /> 
	<s:url id="tribeBankItemDivGridEditURL" action="alsAccount/tribeBankItemGridEdit_execute" />    
	<sjg:grid 
	  		gridModel="model"               
			id="tribeBankItemTable"
			caption="Items" 
			loadonce="true"      			
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
		    viewrecords="true"	  
		    scroll="true"                       
		    scrollrows="true">
		        
	        <sjg:gridColumn name="itemKey" index="itemKey" key="true" title =" itemKey" width="6" sortable="false" hidden="true"  />
			<sjg:gridColumn name="aictUsagePeriodFrom" index="aictUsagePeriodFrom" title =" Usage Period From" width="15" sortable="true" hidden="false"    formatter = 'date' formatoptions =" {newformat : 'm/d/Y'}"  />
			<sjg:gridColumn name="aictUsagePeriodTo" index="aictUsagePeriodTo" title =" Usage Period To" width="15" sortable="true"   formatter = 'date' formatoptions =" {newformat : 'm/d/Y'}"/>
			<sjg:gridColumn name="aictItemTypeCd" index="aictItemTypeCd" title =" Item Code" width="15" sortable="true" align="right" />
			<sjg:gridColumn name="aitTypeDesc" index="aitTypeDesc" title =" Item Description" width="30" sortable="true" />
  		</sjg:grid>
	
	
	