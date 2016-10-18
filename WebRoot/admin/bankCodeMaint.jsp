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
			
			function reloadBankCodeGrid(){
         $("#bankCodeMaintTable").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
    }
			

		</script>
    </fwp:head>
  
	
    <div style="text-align:center">
    	<h2 class="title">Bank Code Maintenance</h2>
   	</div>
   
	<s:url id="bankCodeMaintGridURL" action="alsAccount/bankCodeGrid_buildgrid" />
	<s:url id="bankCodeMaintGridEditURL" action="alsAccount/bankCodeGridEdit_execute" />    
	<sjg:grid
		id="bankCodeMaintTable"
		caption="Bank Code Maintenance"
		href="%{bankCodeMaintGridURL}"
		editurl="%{bankCodeMaintGridEditURL}"		
		dataType="json"
		loadonce="true"
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
    	                       afterComplete:reloadBankCodeGrid,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:500,reloadAfterSubmit:false,
    	                       editCaption:'Edit Bank Code',
    	                       afterSubmit:errorHandler,	    
    	                       closeAfterEdit:true,
    	                        reloadAfterSubmit:true,
    	                        afterComplete:reloadBankCodeGrid,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler, reloadAfterSubmit:true}"
    	gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="300"
		shrinkToFit="true"
		autowidth="true" 
		rowNum="1000"
		sortable="true"
		sortorder="asc"
		sortname="abcBankCd"
		resizable="true">
		
			<sjg:gridColumn name="abcBankCd" index="abcBankCd" key="true" title =" Bank Code" width="15" sortable="true" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:4,maxlength:3}"  align="right"/>
			<sjg:gridColumn name="abcActive" index="abcActive" title =" Active" width="10" sortable="false" editable="true" edittype="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'Y'}"/>
			<sjg:gridColumn name="abcAvblToAllProv" index="abcAvblToAllProv" title =" Available to All" width="10" sortable="false" editable="true" edittype="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'N'}"/>
			<sjg:gridColumn name="abcBankNm" index="abcBankNm" title =" Bank Name" width="45" sortable="true"  editable="true" editrules="{required:true}" editoptions="{size:26, maxlength:25}"/>
			<sjg:gridColumn name="abcCompanyId" index="abcCompanyId" title =" Company Id" width="20" sortable="false" editable="true" editoptions="{size:11, maxlength:10}" editrules="{number:true}" align="right"/>
			<sjg:gridColumn name="abcAccountNo" index="abcAccountNo"  title =" Account No" width="25" sortable="false" hidden="false"  editable="true" editoptions="{size:18, maxlength:17}" editrules="{number:true, required:true}" align="right"/>		
			<sjg:gridColumn name="azcZipCd" index="azcZipCd" title =" Zip Code" width="13" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:6,maxlength:5}" align="right"/>
			<sjg:gridColumn name="azcCityNm" index="azcCityNm" title =" City" width="20" sortable="false" editable="false" />
			<sjg:gridColumn name="abcWhenLog" index="abcWhenLog" title =" Updated On" width="35" sortable="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>	
			<sjg:gridColumn name="abcWhoLog" index="abcWhoLog" title =" Database User" width="20" sortable="false" editable="false"/>
			<sjg:gridColumn name="updatedUsername" index="updatedUsername" title =" Updated By" width="20" sortable="false" editable="false" />		
			<sjg:gridColumn name="abcCreatePersonid" index="abcCreatePersonid" title =" Updated Username" width="25" sortable="false" editable="false" hidden="true"/>
			
	</sjg:grid>
</fwp:template>  