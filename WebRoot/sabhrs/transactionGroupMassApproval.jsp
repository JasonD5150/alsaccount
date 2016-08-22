<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template>
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/> 
        <script src='scripts/fieldEdits.js' type='text/javascript'></script> 
        <script>
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
			
			$.subscribe('transGroupMassApprovalComplete', function(event, data) {				
				$("#transGroupMassApprovalTable")
					.jqGrid({pager:'#transGroupMassApprovalTable_pager'})
					.jqGrid('navButtonAdd'
					,'#transGroupMassApprovalTable_pager'
					,{id:"selectAll_transGroupMassApprovalTable"
					,caption:"Select All"
					,buttonicon:"ui-icon-check"
					,onClickButton:function(){ 
						var grid = $("#transGroupMassApprovalTable");
    					var rows = grid.jqGrid("getDataIDs");
    					for (i = 0; i < rows.length; i++)
					    {
							$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',1);
					    }
					}
					,position:"last"
					,title:"Select All"
					,cursor:"pointer"
					});
				$("#transGroupMassApprovalTable")
					.jqGrid({pager:'#transGroupMassApprovalTable_pager'})
					.jqGrid('navButtonAdd'
					,'#transGroupMassApprovalTable_pager'
					,{id:"deselectAll_transGroupMassApprovalTable"
					,caption:"Deselect All"
					,buttonicon:"ui-icon-minusthick"
					,onClickButton:function(){ 
						var grid = $("#transGroupMassApprovalTable");
					    var rows = grid.jqGrid("getDataIDs");
					    for (i = 0; i < rows.length; i++)
					    {
							$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',0);
					    }
					}
					,position:"last"
					,title:"Deselect All"
					,cursor:"pointer"
					});
					
				   	var grid = $("#transGroupMassApprovalTable");
	  				var rows = grid.jqGrid("getDataIDs");
	  				for (i = 0; i < rows.length; i++)
				    {
						$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',1);
				    }
			
			});
			
			function submitSearch(){
				$.publish('reloadTransGroupMassApproval');
			}
			
			function resetSearch(){
				$('#bpe').val('');
				$('#opa').val('')
				$.publish('reloadTransGroupMassApproval');
			}
			
			function submitChanges(){
				var approved = false;
				var grid = $("#transGroupMassApprovalTable");
    			var rows = grid.jqGrid("getDataIDs");
    			var appLst = "";
    			for (i = 0; i < rows.length; i++)
			    {
			        var app = grid.jqGrid ('getCell', rows[i], 'approve');
			        var key = grid.jqGrid ('getCell', rows[i], 'gridKey');
			        if(app == 1){
			        	approved = true;
			        	appLst = appLst + key +",";
			        }
			   
			    }
			    
			    if(approved){
			    	$('#appLst').val(appLst);
			    	
					url = "alsAccount/transGroupMassApprovalGridEdit_execute.action";    
	        		$.ajax({
	                  type: "POST",
	                  url: url,
	                  dataType: "json",
	                  data: $('#gridFrm').serialize(),
	                  success: function(result){
		                  if(result.actionErrors){
		                  	$('#html').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
		                  }else{
							$.publish('reloadTransGroupMassApproval');
		                  }
	                 }
	                });
			    }else{
			    	alert("Please Select At Least One Approval Check.");
			    }
			}
        </script> 
    </fwp:head>
       
    <div style="width:800px;text-align:center">
    	<h2 class="title">Internal Provider Transaction Group - Mass Approval</h2>
   	</div>
    
    <fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;font-size:larger">Search Criteria</legend>
    	<form id='gridFrm'>
    		<s:hidden id="appLst" name="appLst"></s:hidden>
    		<table>
	    		<tr>
	    			<td>Billing Period End Date</td>
		    		<td><sj:datepicker changeMonth="true" changeYear="true" id="bpe"
					   name="bpe" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Billing Period End Date" 
					   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
					<td>Offline Payment Approval Date</td>
					<td><sj:datepicker changeMonth="true" changeYear="true" id="opa"
					   name="opa" displayFormat="mm/dd/yy"
					   cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Offline Payment Approval Date" 
					   showOn="focus" onblur="testDate(this)" placeholder="mm/dd/yyyy"/></td>
	    		</tr>
    		</table>
		</form>	
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
	</fieldset>
    <br>
    <br>
    <div id ='htmlDiv'>
		<span id='html'></span>
	</div>
	<s:url id="transGroupMassApprovalGridURL" action="alsAccount/transGroupMassApprovalGrid_buildgrid" />
	<s:url id="transGroupMassApprovalGridEditURL" action="alsAccount/transGroupMassApprovalGridEdit_execute" />
	<sjg:grid
		id="transGroupMassApprovalTable"
		caption="Transaction Groups"
		href="%{transGroupMassApprovalGridURL}"
		editurl="%{transGroupMassApprovalGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true,closeAfterSearch:true}"
    	navigatorAddOptions="{width:950,reloadAfterSubmit:true,
    						  addedrow:'last',
    	                      addCaption:'Add New Code Info',
    	                      afterSubmit:errorHandler,
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:1000,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterSubmit:errorHandler,	    
    	                       closeAfterEdit:true,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
    	formIds="gridFrm"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		onCompleteTopics="transGroupMassApprovalComplete"
		reloadTopics="reloadTransGroupMassApproval"
		>
		
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="approve" index="approve" title ="Approve" width="5" sortable="true" align="center" editable = "true" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
			
			<sjg:gridColumn name="providerNo" index="providerNo" title ="Provider No" width="5" sortable="false" hidden="false" editable="true" align="right"/>
			<sjg:gridColumn name="providerName" index="providerName" title ="Provider Name" width="15" sortable="false" hidden="false" editable="true"/>
			<sjg:gridColumn name="remPerStat" index="remPerStat" title ="Remittance Period Status" width="10" sortable="false" hidden="false" editable="true"/>			
			<sjg:gridColumn name="bpe" index="bpe" title ="Billing Period End Date" width="10" sortable="false" hidden="false" editable="true" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'm/d/Y'}"/>
			<sjg:gridColumn name="atgsNetDrCr" index="atgsNetDrCr" title ="Net Cash Dr/Cr" width="5" sortable="false" hidden="false" editable="true" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right"/>
			
	</sjg:grid>	
	<br>
	<s:submit id="submitChanges" onclick="submitChanges()" value="Save" theme="simple"></s:submit>
</fwp:template>
