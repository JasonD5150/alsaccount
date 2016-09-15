/**
 * 
 * intProvRemittance.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function() {
	$('#details').prop({disabled:true});   
	$('#inProvNo').val(getUrlParameter("provNo"));
	$('#inBpFrom').val(getUrlParameter("bpFrom"));	
	$('#inBpTo').val(getUrlParameter("bpTo"));		
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

$.subscribe("internalRemittanceComplete", function(event, data) {	
	var grid = $('#alsInternalRemittance');
	var error = grid.jqGrid('getGridParam', 'userData');
	
	if (error != null && error.length > 0) {
		$("#errorMessage").html(error);
		$("#errorMessage").dialog({
			title:"Search Error",
		    resizable: false,
		    height:"auto",
		    modal: true,
		    buttons: {
	            "Ok": function() {
	                $(this).dialog("close");
	                 }
	             }
			});
		}
});

$.subscribe("qryDivComplete", function(event, data) {	
	if($('#inProvNo').val() != null && $('#inProvNo').val() != ""){
		$('#provNo').val($('#inProvNo').val());
		$('#bpFrom').val($('#inBpFrom').val());
		$('#bpTo').val($('#inBpTo').val());
		$('#frmProvNo').val($('#inProvNo').val());
		$('#frmBPFrom').val($('#inBpFrom').val());
		$('#frmBPTo').val($('#inBpTo').val());
		submitSearch();
		$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
		$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadSubGrids');
	}		    		
});

$.subscribe("internalRemittanceSelected", function(event, data) {	
	var grid = $('#alsInternalRemittance');
	$('#details').prop({disabled:false});
	var sel_id = grid.jqGrid('getGridParam','selrow'); 
	
	$('#frmProvNo').val(grid.jqGrid('getCell', sel_id, 'idPk.apiProviderNo'));
	$('#frmBPFrom').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom'));
	$('#frmBPTo').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo'));
	
	$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
	$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadSubGrids');
});
	
function setEditableFields(formid) {
	var grid = $('#alsInternalRemittance');
	var sel_id = grid.jqGrid('getGridParam','selrow'); 
	var intFileGenerated = grid.jqGrid('getCell', sel_id, 'intFileGenerated');
	var offlinePaymentApproved = grid.jqGrid('getCell', sel_id, 'airOfflnPaymentApproved');
	var completeProvider = grid.jqGrid('getCell', sel_id, 'completeProvider');
	//alert(intFileGenerated);
	if(intFileGenerated == 'YES'){
		disableItems(formid);
		$('#tr_completeProvider', formid).hide();
	}else{
		//alert(offlinePaymentApproved);
		if(offlinePaymentApproved == 'YES'){
			if($('#inProvNo').val() == null){
				$('#tr_completeProvider', formid).hide();
				if(completeProvider != null){
					$('#tr_airOfflnPaymentAppCom', formid).show();
				}else{
					$('#tr_airOfflnPaymentAppCom', formid).hide();
				}
			}else{
				disableItems(formid);
				$('#details').prop({disabled:false});
			}
		}else{
			//alert($('#provNo').val());
			if($('#provNo').val() == null){
				//alert(completeProvider);
				if(completeProvider != null){
					enableItems(formid,offlinePaymentApproved);
				}else{
					disableItems(formid);
				}
			}else{
				enableItems(formid,offlinePaymentApproved);
				$('#details').prop({disabled:false});
			}
		}
		$('#tr_completeProvider', formid).show();
	}			   
}; 	

function disableItems(formid) {
	$('#tr_airCreditSales', formid).hide();	
	$('#tr_completeProvider', formid).hide();  
	$('#tr_airOfflnPaymentApproved', formid).hide();   
	$('#tr_airOfflnPaymentAppBy', formid).hide();   
	$('#tr_offlnPaymentAppDt', formid).hide();    
	$('#tr_airOfflnPaymentAppCom', formid).hide();    
}; 

function enableItems(formid, offlinePaymentApproved) {
	if($('#inProvNo').val() == null){
		$('#tr_airOfflnPaymentApproved', formid).show(); 
		$('#tr_completeProvider', formid).hide(); 
		if(offlinePaymentApproved){
			$('#tr_airOfflnPaymentAppCom', formid).show();
		}else{
			$('#tr_airOfflnPaymentAppCom', formid).hide();
		}
	}else{
		$('#tr_completeProvider', formid).show();  
		$('#tr_airOfflnPaymentApproved', formid).show();
		$('#tr_airOfflnPaymentAppCom', formid).show(); 
	}
	$('#tr_airCreditSales', formid).show();	  
};			

/*ACTIONS*/
function submitSearch(){
	$('#details').prop({disabled:true}); 
	$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadInternalRemittance');
}

function resetSearch(){
	$('#inProvNo').val("");
	$('#inBpFrom').val("");
	$('#inBpTo').val("");
	$('#gridFrm')[0].reset();
}

function details(){
	window.open("/alsaccount/intProvBankCdDepLink_input.action?provNo="+$('#frmProvNo').val()+"&bpFrom="+$('#frmBPFrom').val()+"&bpTo="+$('#frmBPTo').val()+"&prntMenu=ALSACCOUNT");
}

function exportToCSV(){
	$.ajax({
		type: "POST",
	data: JSON.stringify(exportGrid("alsInternalRemittance","remittanceRecords","gridFrm")),
	dataType: "json",
	cache: false,
	contentType: "application/json",
	url: 'intProvRemittanceBuildCsv.action',
	success: function (data) {
		window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
	}, complete: function () {
		$('#details').prop({disabled:true}); 
		$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
		$('#alsInternalRemittance').trigger("reloadGrid");
	},
	error: function (x, e) {
		ajaxErrorHandler(x, e, "Save", null, null);
		}
	});
}