/**
 * 
 * alsNonAlsTemplate.jsp Javascript
 * 
 * @author cfa027
 */
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
	//document.getElementById("budgetYearSel").selectedIndex = "0";
	//setGridFrm();
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

function exportToCSV(){
	var gridList = ["accCodeControlTable","accCdDistByItemType"];
	var dataLabelList = ["accCodeControlEntries","accCdDistByItemTypeEntries"];
	
	$.ajax({
		type: "POST",
		data: JSON.stringify(exportMultipleGrids(gridList,dataLabelList,"gridFrm")),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		url: 'accountingCodeControlBuildCsv.action',
		success: function (data) {
			window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
		}, complete: function () {
			$.unblockUI();
			//$('#iafaQueryTable').jqGrid('setGridParam',{datatype:'json'});
			//$('#iafaQueryTable').trigger("reloadGrid");
		},
		error: function (x, e) {
			ajaxErrorHandler(x, e, "Save", null, null);
		}
	});
}

function checkForm(postData){
	rtrnstate = true; 
	rtrnMsg = ''; 
	
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

function submitSearch(){
	$('#frmBudgYear').val($('#budgetYearSel').val());
	$('#accCd').val($('#accCd_widget').val());
	$('#itemTypeCd').val($('#itemTypeCd_widget').val());
	$('#accCodeControlTable').jqGrid('setGridParam',{datatype:'json'});
	$('#accCdDistByItemType').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadGrids')
}	

function resetSearch(){
	$('#gridFrm')[0].reset();
}