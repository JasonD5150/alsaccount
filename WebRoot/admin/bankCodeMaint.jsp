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
  
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Bank Code Maintenance</h2>
   	</div>
   
	<s:url id="bankCodeMaintGridURL" action="alsAccount/bankCodeGrid_buildgrid" />
	<s:url id="bankCodeMaintGridEditURL" action="alsAccount/bankCodeGridEdit_execute" />    
	<sjg:grid
		id="bankCodeMaintTable"
		caption="Bank Code Mainenance"
		href="%{bankCodeMaintGridURL}"
		editurl="%{bankCodeMaintGridEditURL}"		
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
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="300"
		width="950"
		rowNum="1000"
		resizable="true">
		
			<sjg:gridColumn name="abcBankCd" index="abcBankCd" key="true" title =" Bank Code" width="15" sortable="false" hidden="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:4,maxlength:3}"/>
			<sjg:gridColumn name="abcActive" index="abcActive" title =" Active" width="10" sortable="false" editable="true" edittype="select" editoptions="{value: {Y: 'Yes', N: 'No'}, defaultValue:'Y'}"/>
			<sjg:gridColumn name="abcBankNm" index="abcBankNm" title =" Bank Name" width="45" sortable="false"  editable="true" editrules="{required:true}" editoptions="{size:26, maxlength:25}"/>
			<sjg:gridColumn name="abcCompanyId" index="abcCompanyId" title =" Company Id" width="20" sortable="false" editable="true" editoptions="{size:11, maxlength:10}" editrules="{number:true}"/>
			<sjg:gridColumn name="abcAccountNo" index="abcAccountNo"  title =" Account No" width="25" sortable="false" hidden="false"  editable="true" editoptions="{size:18, maxlength:17}" editrules="{number:true}"/>		
			<sjg:gridColumn name="azcZipCd" index="azcZipCd" title =" Zip Code" width="13" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:6,maxlength:5}"/>
			<sjg:gridColumn name="azcCityNm" index="azcCityNm" title =" City" width="20" sortable="false" editable="false" />
			<sjg:gridColumn name="abcWhenLog" index="abcWhenLog" title =" Updated On" width="35" sortable="false" editable="false" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y H:i'}"/>			
			<sjg:gridColumn name="abcWhoLog" index="abcWhoLog" title =" Updated By" width="20" sortable="false" editable="false" />
			<sjg:gridColumn name="abcCreatePersonid" index="abcCreatePersonid" title =" Updated Username" width="25" sortable="false" editable="false" />
			
	</sjg:grid>
</fwp:template>  