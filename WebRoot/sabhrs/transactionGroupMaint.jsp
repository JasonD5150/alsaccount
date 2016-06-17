<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template >

    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
	   	<script type="text/javascript">
			function errorHandler(response, postdata) {
			    rtrnstate = true; 
			    rtrnMsg = ""; 
				json = eval('(' + response.responseText + ')'); 
					if(json.actionErrors) {
						rtrnstate = false; 
					    for(i=0; i < json.actionErrors.length; i++) {
					    	rtrnMsg += json.actionErrors[i] + '<br/>'; 
					    } 
					} 
				return [rtrnstate,rtrnMsg]; 
			};
		</script>
    </fwp:head>
    <form id='gridFrm'>
			<s:hidden id="frmBudgYear" name="budgYear"/>
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Transaction Group Maintenance</h2>
   	</div>
   
	<s:url id="transGroupMaintGridURL" action="alsAccount/transGroupMaintGrid_buildgrid" />
	<s:url id="transGroupMaintGridEditURL" action="alsAccount/transGroupMaintGridEdit_execute" />    
	<sjg:grid
		id="transGroupMaintTable"
		caption="Transaction Groups"
		href="%{transGroupMaintGridURL}"
		editurl="%{transGroupMaintGridEditURL}"		
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
    	                      addCaption:'Add New Code Info',
    	                      afterSubmit:errorHandler,
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterSubmit:errorHandler,	    
    	                       closeAfterEdit:true,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="300"
		width="950"
		rowNum="1000"
		resizable="true">
		
			<sjg:gridColumn name="atgBusinessProcessType" index="atgBusinessProcessType" title =" Business Process Type" width="25" sortable="false" hidden="false" editable="true" editrules="{required:true}" edittype="select" formatter="select" editoptions="{value: {0:'',1: 'External Provider', 4: 'Internal Provider OTC', 3: 'Mail Drawing', 2: 'Mail Non Drawing'}}"/>
			<sjg:gridColumn name="atgTransactionCd" index="atgTransactionCd" key="true" title =" Tx Group Type" width="10" sortable="false" hidden="false"  editable="false"/>
			<sjg:gridColumn name="atgTransactionDesc" index="atgTransactionDesc" title =" Description" width="50" sortable="false" edittype="textarea" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="atgIdentifierString" index="atgIdentifierString" title =" Identifier String" width="50" sortable="false" edittype="textarea" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="atgInterfaceFile" index="atgInterfaceFile" title =" Interface File" width="25" sortable="false" editable="true" editrules="{required:true}" edittype="select" formatter="select" editoptions="{value: {0:'',AR: 'Account Receivable', GL: 'General Ledger', NO: 'None'}}"/>
			<sjg:gridColumn name="atgWhoLog" index="atgWhoLog" title =" Who Modified" width="10" sortable="false" editable="false" />
			<sjg:gridColumn name="atgWhenLog" index="atgWhenLog" title =" When Modified" width="20" sortable="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y g:i A'}"/>
			
	</sjg:grid>

</fwp:template>
