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
			
			//Reset budget year select on page load   
			$(document).ready(function(){
		   		document.getElementById("budgetYearSel").selectedIndex = "0";
		   		setGridFrm();
			});
			
			function setGridFrm(){
		    	$('#frmBudgYear').val($('#budgetYearSel').val());
	    	}
	    	
			function reloadGrids(event,data) {
				setGridFrm();
				$.publish('reloadOrgControlDiv');
			}
			
			function getGenRpt(id) {
	 			var postFilters = $("#orgControlTable").jqGrid('getGridParam', 'postData').filters;
	 			$('#frmRptType').val("orgControl");
		 		
		 		$('#frmRptBudgYear').val($('#budgetYearSel').val());
		 		$('#frmFilters').val(postFilters);
		 		
				$('#rptFrm').attr('action', 'genDocCreate');
				$('#rptFrm').submit();	 
		    }
	
			//Set edit dialog fields disable when editing for all tabs
		    function setDisabled(id){
		    	document.getElementById("idPk.aaccAccCd").disabled = true;
		    	document.getElementById("idPk.apiProviderNo").disabled = true;
		    }
		    
		    //Set edit dialog fields enabled when adding for all tabs
		    function setEnabled(id){
				document.getElementById("idPk.aaccAccCd").disabled = false;
		    	document.getElementById("idPk.apiProviderNo").disabled = false;
		    }
		    
		    $.subscribe('orgControlComplete', function(event,data) {
				   if ( $("#orgControlTable").length) {
			       		$("#orgControlTable").jqGrid('setColProp','idPk.aaccAccCd', { editoptions: { value: rtrnAccountList()}});
			       		$("#orgControlTable").jqGrid('setColProp','idPk.apiProviderNo', { editoptions: { value: rtrnProviderList()}});
				   }   
			});
			
			function rtrnAccountList() {
				var rslt = $("#frmAccountLst").val();
				return rslt;
			}
			function rtrnProviderList() {
				var rslt = $("#frmProviderLst").val();
				return rslt;
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
	
    <div style="width:800px; text-align:center">
    	<h2 class="title">Org Control Table Maintenance</h2>
   	</div>
   
   <s:select id="budgetYearSel" name="budgetYearSel" list="budgetYearSel" listKey="itemVal" listValue="itemLabel" title="Budget Year" label="Budget Year " cssStyle="width:130px" onchange="reloadGrids()" onCompleteTopics="reloadGrids()"/>
   
  <br>
  <br>
	
	<s:url id="orgControlDivUrl" action="orgControlDiv_input.action"/>
   	<sj:div id="orgControlDiv" style='width:800px;'
	  		href="%{orgControlDivUrl}"
	  		reloadTopics="reloadOrgControlDiv" 
	  		formIds="gridFrm">
	</sj:div>
	<br>
	<input id="getOrgControlRpt" 
			   type="button"
			   onclick="getGenRpt(id)" 
			   value="Export CSV"> 
				
</fwp:template>
