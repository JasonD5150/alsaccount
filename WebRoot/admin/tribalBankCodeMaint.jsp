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
    	<h2 class="title">Tribal Bank Code Maintenance</h2>
   	</div>
   
	<s:url id="tribeBankCodeMaintGridURL" action="alsAccount/tribeBankCodeGrid_buildgrid" />
	<s:url id="tribeBankCodeMaintGridEditURL" action="alsAccount/tribeBankCodeGridEdit_execute" />    
	<sjg:grid
		id="tribeBankCodeMaintTable"
		caption="TribeBank Code Mainenance"
		href="%{tribeBankCodeMaintGridURL}"
		editurl="%{tribeBankCodeMaintGridEditURL}"		
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
		
			<sjg:gridColumn name="atiTribeCd" index="atiTribeCd" key="true" title =" Tribe Code" width="15" sortable="false" hidden="false" editable="true" editrules="{required:true}" editoptions="{size:4,maxlength:3}"/>
			<sjg:gridColumn name="abcBankCd" index="abcBankCd" title =" Bank Code" width="15" sortable="false"  editable="true" editrules="{required:true}" editoptions="{size:4, maxlength:3}"/>
			<sjg:gridColumn name="atiDirectorNm" index="atiDirectorNm" title =" Director Name" width="20" sortable="false" editable="true" editoptions="{size:11, maxlength:10}"/>
			<sjg:gridColumn name="atiTribeNm" index="atiTribeNm" title =" Tribe Name" width="20" sortable="false" editable="true"/>
			<sjg:gridColumn name="atiTribeAcctBankNm" index="atiTribeAcctBankNm" title =" Bank Name" width="35" sortable="false" editable="false" />
			<sjg:gridColumn name="atiWhoLog" index="atiWhoLog"  title =" Who Log" width="25" sortable="false" hidden="true"  editable="true" />		
			<sjg:gridColumn name="atiWhenLog" index="atiWhenLog" title =" When Log" width="13" sortable="false" hidden="true" editable="true" />
			
			<sjg:gridColumn name="atiTribeAcctRoutingNo" index="atiTribeAcctRoutingNo" title =" Routing Number" width="20" sortable="false" editable="true" hidden="false"/>			
			<sjg:gridColumn name="atiTribeAcctNo" index="atiTribeAcctNo" title =" Account Number" width="20" sortable="false" editable="true" hidden="false"/>
			
			
	</sjg:grid>
</fwp:template>  