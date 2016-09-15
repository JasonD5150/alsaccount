<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
        <script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script> 
        <script type="text/javascript" src= "/alsaccount/scripts/menuSecurity.js"></script>  
		<style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
        </style>
        <script>
        	$(document).ready(function() {
	        	$('#transCd').val(getUrlParameter("transCd"));
	        	$('#groupId').val(getUrlParameter("groupId"));		
			});
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
			}
			
			$.subscribe("iafaRecordSelected", function (event, data) {
				$('#reverseAlsEntries').prop({disabled:false});
				var grid =  $('#iafaGrid');
				var sel_id = grid.jqGrid('getGridParam', 'selrow');  
				$('#selProvNo').val(grid.jqGrid('getCell', sel_id, 'apiProviderNo'));
			   	$('#selBpFromDt').val(grid.jqGrid('getCell', sel_id, 'aprBillingFrom'));
			   	$('#selBpToDt').val(grid.jqGrid('getCell', sel_id, 'aprBillingTo'));
			   	$('#selIafaSeqNo').val(grid.jqGrid('getCell', sel_id, 'aiafaSeqNo'));
			   	
			   	$('#sabhrsGrid').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadSabhrsGrid');
			});
			
			/*ACTIONS*/
			function submitSearch(){ 
				$('#iafaGrid').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadIafaGrid');				
			}
			
			function resetSearch(){
				$('#gridFrm')[0].reset();
			}
			
			function reverseAlsEntries(){
				$('#frmOper').val('reverseAlsEntries');
				
				url = "alsAccount/manualProviderAdjEntriesSABHRSGridEdit_execute.action";    
        		$.ajax({
                  type: "POST",
                  url: url,
                  dataType: "json",
                  data: $('#gridFrm').serialize(),
                  success: function(result){

	                  if(result.actionErrors){
	                  	$('#rtnHtml').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
	                  }else{
	                  	$('#rtnHtml').html('');
						$.publish('reloadSabhrsGrid');
	                  }
                 }
                });
			}
        </script>
    </fwp:head>	
    <div style="width:800px;text-align:center">
    	<h2 class="title">IAFA Manual Provider Adjusting Entries</h2>
   	</div>

	<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
	   	<legend style="font-weight: bold;">Search Criteria</legend>
	   	<form id='gridFrm'>
	   	<s:hidden id="frmOper" name="oper"/>
	   	<s:hidden id="transCd" name="transCd"/>
	   	<s:hidden id="groupId" name="groupId"/>
	   	<s:hidden id="selProvNo" name="selProvNo"/>
	   	<s:hidden id="selBpFromDt" name="selBpFromDt"/>
	   	<s:hidden id="selBpToDt" name="selBpToDt"/>
	   	<s:hidden id="selIafaSeqNo" name="selIafaSeqNo"/>
	   		<table>
   			<tr>			
				<td class="label">Provider No:</td>
				<td class="autocompleter"><sj:autocompleter
									id="provNo"
									name="provNo"
									list="providerLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
				<td class="label">IAFA Seq No: </td>
				<td><s:textfield id="iafaSeqNo" name="iafaSeqNo" theme="simple" title="IAFA Seq No" /></td>
		    </tr>
		    <tr>
		    	<td class="label">Billing Period From: </td>
		    	<td>
		    		<sj:datepicker changeMonth="true" changeYear="true" id="bpFromDt"
							name="bpFromDt" displayFormat="mm/dd/yy"
							cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period From" 
							showOn="focus" onblur="testDate(this)" />
		    	</td>	
		    	<td class="label">Billing Period To: </td>	
				<td>
		    		<sj:datepicker changeMonth="true" changeYear="true" id="bpToDt"
							name="bpToDt" displayFormat="mm/dd/yy"
							cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period To" 
							showOn="focus" onblur="testDate(this)" />
		    	</td>	
		    </tr>
		    </table>
		</form>	
	</fieldset>
	<br>
	<br>
	<div id ='rtnHtmlDiv'>
		<span id='rtnHtml'></span>
	</div>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
		<s:submit id="reverseAlsEntries" onclick="reverseAlsEntries()" value="Reverse Als Entries" theme="simple" disabled="true"></s:submit>
	</fieldset>
	<br>
	<br>
	<s:url id="manualProviderAdjEntriesIAFAGridURL" action="alsAccount/manualProviderAdjEntriesIAFAGrid_buildgrid" /> 
	<sjg:grid
		id="iafaGrid"
		caption="IAFA Table"
		href="%{manualProviderAdjEntriesIAFAGridURL}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadIafaGrid"
		onSelectRowTopics="iafaRecordSelected">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title ="Issuing Provider No" width="10" sortable="false" align="right"/>
			<sjg:gridColumn name="aprBillingFrom" index="aprBillingFrom" title ="Billing Period From" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aprBillingTo" index="aprBillingTo" title ="Billing Period To" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="IAFA Seq No" width="10" sortable="false" align="right"/>	
	
	</sjg:grid>
	<br>
	<br>
	<s:url id="iafaManualProviderAdjEntriesGridURL" action="alsAccount/manualProviderAdjEntriesSABHRSGrid_buildgrid" />
	<s:url id="iafaManualProviderAdjEntriesGridEditURL" action="alsAccount/manualProviderAdjEntriesSABHRSGridEdit_execute" />    
	<sjg:grid
		id="sabhrsGrid"
		caption="Non Als SABHRS Entries"
		href="%{iafaManualProviderAdjEntriesGridURL}"
		editurl="%{iafaManualProviderAdjEntriesGridEditURL}"		
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="false"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorAddOptions="{width:950,reloadAfterSubmit:true,
    						  addedrow:'last',
 	    					  afterShowForm: function ($form) {
                    			$form.closest('.ui-jqdialog').position({
                        			my: 'center',
                        			at: 'center',
        							of: $('#transGroupDtlsTable').closest('div.ui-jqgrid')
                    			});
                    		  },
                    		  beforeSubmit: function (postData) {
    						  		postData.transIdentifier = $('#frmTransIdentifier').val();
    						  		postData.transGrp = $('#frmTransGrp').val();
    						  		return[true, ''];
    						  },  
    						  afterSubmit:errorHandler,
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
		navigatorEditOptions="{width:950,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',    
    	                       closeAfterEdit:true,
    	                       afterSubmit:errorHandler,
    	                       processData:'Updating to Database',
    	                       afterShowForm: function ($form) {
                    			$form.closest('.ui-jqdialog').position({
                        			my: 'center',
                        			at: 'center',
        							of: $('#transGroupDtlsTable').closest('div.ui-jqgrid')
                    			});
                    		   }}"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadSabhrsGrid"
		onCompleteTopics="alsSabhrsEntriesComplete">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" />
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="idPk.aseSeqNo" index="idPk.aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true"/>
	</sjg:grid>
</fwp:template>
