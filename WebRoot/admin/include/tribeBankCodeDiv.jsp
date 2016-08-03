<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

    
 

	<s:url id="tribeBankCodeDivGridURL" action="alsAccount/tribeBankCodeGrid_buildgrid" />
	<s:url id="tribeBankCodeDivGridEditURL" action="alsAccount/tribeBankCodeGridEdit_execute" />    
	<sjg:grid
		id="tribeBankCodeMaintTable"
		caption="TribeBank Code Mainenance"
		href="%{tribeBankCodeDivGridURL}"
		editurl="%{tribeBankCodeDivGridEditURL}"		
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
    						  
    	                      addCaption:'Add New Bank Code',
    	                      afterSubmit:errorHandler,
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Bank Code',
    	                       afterSubmit:errorHandler,	    
    	                       closeAfterEdit:true,
    	                       
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		onSelectRowTopics="bankSelect"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="300"
		width = "800"
		rowNum="1000">
		
		
		
			<sjg:gridColumn name="atiTribeCd" index="atiTribeCd" key="true" title =" Tribe Code" width="6" sortable="false" hidden="false" editable="true" editrules="{required:true}" editoptions="{size:6,maxlength:5}"/>
			<sjg:gridColumn name="abcBankCd" index="abcBankCd" title =" Bank Code" width="15" sortable="false"  editable="true" editrules="{required:true}" editoptions="{size:26, maxlength:25}"/>
			<sjg:gridColumn name="atiDirectorNm" index="atiDirectorNm" title =" Director Name" width="20" sortable="false" editable="true" editoptions="{size:26, maxlength:100}"/>
			<sjg:gridColumn name="atiTribeNm" index="atiTribeNm" title =" Tribe Name" width="20" sortable="false" editable="true" editoptions="{size:26, maxlength:25}"/>
			<sjg:gridColumn name="atiTribeAcctBankNm" index="atiTribeAcctBankNm" title =" Bank Name" width="35" sortable="false" editable="true" editoptions="{size:24, maxlength:23}"/>
			<sjg:gridColumn name="atiWhoLog" index="atiWhoLog"  title =" Who Log" width="25" sortable="false" hidden="true"  editable="true" />		
			<sjg:gridColumn name="atiWhenLog" index="atiWhenLog" title =" When Log" width="13" sortable="false" hidden="true" editable="true" />	
			<sjg:gridColumn name="atiTribeAcctRoutingNo" index="atiTribeAcctRoutingNo" title =" Routing Number" width="20" sortable="false" editable="true" hidden="false" editoptions="{size:10, maxlength:9}"/>			
			<sjg:gridColumn name="atiTribeAcctNo" index="atiTribeAcctNo" title =" Account Number" width="20" sortable="false" editable="true" hidden="false" editoptions="{size:18, maxlength:17}"/>
			
			
	</sjg:grid>
	

	
	
	