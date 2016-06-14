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
			function setGridFrm(){
		    	$('#frmBudgYear').val($('#budgetYearSel').val());
	    	}
	    	
	    	function reloadGrids(event,data) {
				setGridFrm();
				$.publish('reloadGrids');
			}
			
			//Set edit dialog fields disable when editing for all tabs
		    function setDisabled(id){
				   	$("input[name='idPk.aaccAccCd']").prop({disabled:true});
				   	$("input[name='idPk.aaccSeqNo']").prop({disabled:true});
		    }
		    
		    //Set edit dialog fields enabled when adding for all tabs
		    function setEnabled(id){
				   	$("input[name='idPk.aaccAccCd']").prop({disabled:false});
				   	$("input[name='idPk.aaccSeqNo']").prop({disabled:false});
		    }
		    
		    //Reset budget year select on page load   
			$(document).ready(function(){
		   		document.getElementById("budgetYearSel").selectedIndex = "0";
		   		setGridFrm();
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
			};
			
			$.subscribe('accCodeControlComplete', function(event,data) {
				   if ( $("#accCodeControlTable").length) {
			       		$("#accCodeControlTable").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
				   }   
			});
			
			function rtrnAccountList() {
				var rslt = $("#frmAccountLst").val();
				return rslt;
			}
			
			function getGenRpt() {
		 		
	 			var postFilters = $("#accCodeControlTable").jqGrid('getGridParam', 'postData').filters;
	 			$('#frmRptType').val("accCodeControl");
		 		
		 		$('#frmRptBudgYear').val($('#budgetYearSel').val());
		 		$('#frmFilters').val(postFilters);
		 		
				$('#rptFrm').attr('action', 'genDocCreate');
				$('#rptFrm').submit();	 
		    }
		    
		    function checkForm(postData){
		    	rtrnstate = true; 
				rtrnMsg = ''; 
		  		
	  			postData.budgYear = $('#budgetYearSel').val();
		  		
		  		if(postData.aaccOrgFlag == 'Y'){
		  			if(postData.aocOrg != null){
		  				rtrnstate = false;
						rtrnMsg = 'Org cannot be entered if Multiple Orgs selected as Yes';
		  			}
				}
				
				if(postData.aaccBalancingAmtFlag == 'Y'){
					if(postData.aaccAllocatedAmt != ''){
						rtrnstate = false;
						rtrnMsg = 'Allocated Amount should not be entered if Balancing Amount Flag is selected as Yes';
					}
				}
				if(postData.aaccBalancingAmtFlag == 'N'){
					if(postData.aaccAllocatedAmt == '' || postData.aaccAllocatedAmt <= 0){
						rtrnstate = false;
						rtrnMsg = 'Allocated Amount must be greater than zero, if Balancing Amount Flag is  selected as No';
					}
				}
				
				if(postData.aamAccount == '002504' || postData.aamAccount == '002505'){
					if(postData.aaccJlrRequired == 'N'){
						rtrnstate = false;
						rtrnMsg = 'Account Codes 002504 and 002505 require a Open Item Key of Yes.'; 
					}
				}
				
				return [rtrnstate,rtrnMsg]; 
		    } 
		</script>
    </fwp:head>
    <form id='gridFrm'>
			<s:hidden id="frmBudgYear" name="budgYear"/>
	</form>
	
	<form id='rptFrm'>
			<s:hidden id="frmRptType" name="rptType" />
			<s:hidden id="frmFilters" name="filters" />
			<s:hidden id="frmActTypCd" name="actTypCd" />
			<s:hidden id="frmRptBudgYear" name="budgYear" />
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Accounting Code Control Table Maintenance</h2>
   	</div>
   
   <s:select id="budgetYearSel" name="budgetYearSel" list="budgetYearSel" listKey="itemVal" listValue="itemLabel" title="Budget Year" label="Budget Year " cssStyle="width:130px" onchange="reloadGrids()" onCompleteTopics="reloadGrids()"/>
   
  <br>
  <br>
	
	<s:url id="accCodeControlDivUrl" action="accCodeControlDiv_input.action"/>
   	<sj:div id="accCodeControlDiv" style='width:800px;'
	  		href="%{accCodeControlDivUrl}" 
	  		reloadTopics="reloadAccCodeControlDiv" 
	  		formIds="gridFrm">
	</sj:div>
	<br>
	<input id="getActivityAccountLinkageRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV"> 
				
</fwp:template>
