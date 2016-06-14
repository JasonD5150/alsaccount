<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

 


	<s:url id="appendixM2Grid" action="alsAccount/appendixM2Grid_buildgrid" />
	<s:url id="appendixM2GridEdit" action="alsAccount/appendixM2GridEdit_execute" />    
	<sjg:grid
		id="appendixM2Table"
		caption="Transaction Codes"
		href="%{appendixM2Grid}"
		editurl="%{appendixM2GridEdit}"		
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
        							postData.asacSystemActivityTypeCd = $('#frmActivityType').val();
        							return [true, ''];
    						   },
    						  afterSubmit:errorHandler,
    						  afterShowForm:setEnabled,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:true,
    	                       editCaption:'Edit Code Info',
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       afterShowForm:setDisabled,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="tranCodeModel"
		rownumbers="false"
		editinline="false"
    	onGridCompleteTopics="accountMasterComplete"
    	reloadTopics="reloadGrids"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="400"
		width="950"
		rowNum="500"
		formIds="tranCodeFrm" >

			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.asacSystemActivityTypeCd" index="idPk.asacSystemActivityTypeCd" title =" Activity Type Code" width="25" sortable="false" hidden="true"/>
			<sjg:gridColumn name="idPk.asacTxnCd" index="idPk.asacTxnCd" title =" Transaction Code" width="25" sortable="false" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:2,maxlength:1}"/>
			<sjg:gridColumn name="asattcDesc" index="asattcDesc" title =" Desc" width="100" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="asattcWhoLog" index="asattcWhoLog" title =" Create" width="20" sortable="false" hidden="false" editable="false"/>
			<sjg:gridColumn name="asattcWhenLog" index="asattcWhenLog" title =" Create Date" width="40" sortable="false" hidden="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>
			<sjg:gridColumn name="asattcWhoModi" index="asattcWhoModi" title =" Modified" width="30" sortable="false" hidden="false" editable="false"/>
			<sjg:gridColumn name="asattcWhenModi" index="asattcWhenModi" title =" Modified Date" width="40" sortable="false" hidden="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>

	</sjg:grid>