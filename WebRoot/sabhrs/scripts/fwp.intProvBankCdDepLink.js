/**
 * 
 * intProvBankCdDepLink.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function(){
	$('#details').prop({disabled:true});
	var provNo = getUrlParameter("provNo");
	var bpFrom = getUrlParameter("bpFrom");
	var bpTo = getUrlParameter("bpTo");
	if(provNo == null){
		$('#provNo').val('');
	}else{
		$('#provNo').val(provNo);
	}
	$('#bpFrom').val(bpFrom);
	$('#bpTo').val(bpTo);
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

function prePopulate(){
	var grid = $("#intProvBankCdDepLinkGrid");
	var rows = grid.jqGrid("getDataIDs");
	var length = grid.jqGrid("getDataIDs").length-1;

    $('#abcBankCd').val(grid.jqGrid ('getCell', rows[length], 'abcBankCd'));
    $('#billingFrom').val(grid.jqGrid ('getCell', rows[length], 'billingFrom'));
    $('#apbdBillingTo').val(grid.jqGrid ('getCell', rows[length], 'apbdBillingTo'));
    $('#amtDue').val(grid.jqGrid ('getCell', rows[length], 'amtDue'));
}

$.subscribe('setBankCdList', function(event,data) {
	    if ( $("#intProvBankCdDepLinkGrid").length) {
       		$("#intProvBankCdDepLinkGrid").jqGrid('setColProp','abcBankCd', { editoptions: { value: rtrnBankCdList()}});
	   } 
});

function rtrnBankCdList() {
			var rslt = $("#bankCdLst").val();
			return rslt;
}

$.subscribe("intProvBankCdDepLinkSelected", function(event, data) {	
	$('#details').prop({disabled:false});
});
	


/*ACTIONS*/
function submitSearch(){
	$('#details').prop({disabled:true});
	$.publish('reloadGridDiv')
}

function resetSearch(){
	$('#divFrm')[0].reset();
}

function details(){
	var grid =  $('#intProvBankCdDepLinkGrid');
	var sel_id = grid.jqGrid('getGridParam', 'selrow'); 
	var bpFrom = grid.jqGrid('getCell', sel_id, 'billingFrom'); 
	var bpTo = grid.jqGrid('getCell', sel_id, 'apbdBillingTo'); 
	window.open("/alsaccount/intProvRemittance_input.action?provNo="+$('#provNo').val()+"&bpFrom="+bpFrom+"&bpTo="+bpTo+"&prntMenu=ALSACCOUNT");
}

function exportToCSV(){
	var postFilters = $("#intProvBankCdDepLinkGrid").jqGrid('getGridParam', 'postData').filters;
	$('#frmRptType').val("intProvBankCdDepLink");
	$('#frmFilters').val(postFilters);
	$('#frmProvNo').val($('#provNo').val());
	
	$('#rptFrm').attr('action', 'genDocCreate');
	$('#rptFrm').submit();	 
}	

function getTDT() {
	var generate = false;
	var grid = $("#intProvBankCdDepLinkGrid");
	var rows = grid.jqGrid("getDataIDs");
	var depositIdLst = "";
	for (i = 0; i < rows.length; i++)
    {
    	var gen = grid.jqGrid ('getCell', rows[i], 'genTDT');
        var depositId = grid.jqGrid ('getCell', rows[i], 'apbdDepositId');
        if(gen == 1){
        	generate = true;
			if(depositIdLst == ""){
        		depositIdLst += "'"+depositId+"'";
        	}else{
        		depositIdLst += ",'"+depositId+"'";
        	}
        }
    }
    if(generate){
    	$('#depositIds').val(depositIdLst);
		$('#pdfFrm').submit();
    }else{
    	alert("No record selected to print.");
    }
}