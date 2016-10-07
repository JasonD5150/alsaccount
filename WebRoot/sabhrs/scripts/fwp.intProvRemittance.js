/**
 * 
 * intProvRemittance.jsp Javascript
 * 
 * @author cfa027
 */
var hasUserRole;
var hasIntProvRole;
var selectedRow;
$(document).ready(function() {
	hasUserRole = $('#hasUserRole').val();
	hasIntProvRole = $('#hasIntProvRole').val();
	resetSubGridForm();
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


/*GRIDS TOPICS*/
$.subscribe("internalRemittanceComplete", function(event, data) {	
	var grid = $('#alsInternalRemittance');
	var error = grid.jqGrid('getGridParam', 'userData');

	grid.jqGrid({pager:'#alsInternalRemittance_pager'}).jqGrid('navButtonAdd'
	,'#alsInternalRemittance_pager'
	,{id:"columnSelector_alsInternalRemittance"
	,caption:""
	,buttonicon:"ui-icon-extlink"
	,onClickButton:function(){ 
		grid.jqGrid('columnChooser',{caption: "Columns: CTRL-click select/deselect a column, CTRL-A select all",width: 500});
	}
	,position:"last"
	,title:"Add/Remove Columns"
	,cursor:"pointer"
	});
	
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
	if(selectedRow != null){
		grid.jqGrid("setSelection", selectedRow);
	}
});

$.subscribe("internalRemittanceSelected", function(event, data) {	
	/*LOAD SUBGRIDS*/
	var grid = $('#alsInternalRemittance');
	var sel_id = grid.jqGrid('getGridParam','selrow');
	selectedRow = sel_id;
	$('#frmProvNo').val(grid.jqGrid('getCell', sel_id, 'idPk.apiProviderNo'));
	$('#frmBPFrom').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom'));
	$('#frmBPTo').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo'));
	$('#frmBudgYear').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo').substring(6,10));
	
	$('#depositsGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
	$('#alsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
	$('#iafaGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsNonAlsTemplateTable').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadRevAlsSabhrsEntriesGrid');
	$.publish('reloadBankDepGrids');
	$.publish('reloadNonAlsDetGrids');
	$.publish('reloadSubGrids');
	
	/*SET VISIBILITY*/
	setVisibility();
	/*COPY DATA FROM GRID TO FORM*/
	var dtCompletedByProv = grid.jqGrid('getCell', sel_id, 'completeProvider');
	$('#displayProvNo').val(grid.jqGrid('getCell', sel_id, 'idPk.apiProviderNo'));
	$('#displayProvNm').val(grid.jqGrid('getCell', sel_id, 'provNm'));
	$('#displayBpFrom').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom'));
	$('#displayBpTo').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo'));
	$('#displaySysSales').val(grid.jqGrid('getCell', sel_id, 'airSystemSales'));
	$('#displayAmtRec').val(grid.jqGrid('getCell', sel_id, 'amtRec'));
	$('#displayEftDdd').val(grid.jqGrid('getCell', sel_id, 'eftddd'));
	$('#displayOtcSales').val(grid.jqGrid('getCell', sel_id, 'airOtcPhoneSales'));
	$('#displayPaes').val(grid.jqGrid('getCell', sel_id, 'airPae'));
	$('#displayNonAlsSales').val(grid.jqGrid('getCell', sel_id, 'airNonAlsSales'));
	$('#displayTotSales').val(grid.jqGrid('getCell', sel_id, 'airTotSales'));
	$('#displayTotAlsSales').val(grid.jqGrid('getCell', sel_id, 'amtDue'));
	$('#displayCCSales').val(grid.jqGrid('getCell', sel_id, 'airCreditSales'));
	$('#displayTotFundRec').val(grid.jqGrid('getCell', sel_id, 'totFundsRec'));
	$('#displayTotBankDep').val(grid.jqGrid('getCell', sel_id, 'totBankDep'));
	$('#displayBalanced').val(grid.jqGrid('getCell', sel_id, 'billingBallanced'));
	$('#displayDif').val(grid.jqGrid('getCell', sel_id, 'airDifference'));
	$('#displayTotShortOfSales').val(grid.jqGrid('getCell', sel_id, 'airShortSales'));
	$('#displayTotOverSales').val(grid.jqGrid('getCell', sel_id, 'airOverSales'));
	$('#displayNetOverShortOfSales').val(grid.jqGrid('getCell', sel_id, 'netOverShortOfSales'));
	$('#compDt').val(grid.jqGrid('getCell', sel_id, 'completeProvider'));
	if(dtCompletedByProv.length == 1){
		$('#provComp').prop('checked', false);
	}else{
		$('#provComp').prop('checked', true);
	}
	if(grid.jqGrid('getCell', sel_id, 'airOfflnPaymentApproved') == 'true'){
		$('#remApp').prop('checked', true);
	}else{
		$('#remApp').prop('checked', false);
	}
	$('#disAppBy').val(grid.jqGrid('getCell', sel_id, 'airOfflnPaymentAppBy'));
	$('#disAppDt').val(grid.jqGrid('getCell', sel_id, 'offlnPaymentAppDt'));
	$('#disAppCom').val(grid.jqGrid('getCell', sel_id, 'airOfflnPaymentAppCom'));
	if(grid.jqGrid('getCell', sel_id, 'intFileGenerated') == 'YES'){
		$('#intFileCreated').prop('checked', true);
	}else{
		$('#intFileCreated').prop('checked', false);
	}
	$('#intFileCreatedDt').val(grid.jqGrid('getCell', sel_id, 'intFileCreateDt'));
	
	$('#displayRemittanceDiv').show();
});

$.subscribe("depositsGridComplete", function(event, data) {	
	$("#depositsGrid").jqGrid({pager:'#depositsGrid_pager'}).jqGrid('navButtonAdd'
	,'#depositsGrid_pager'
	,{id:"addTemplate_depositsGrid"
	,caption:"Generate Deposit Tickets"
	,buttonicon:"ui-icon-document"
	,onClickButton:function(){ 
		getTDT();
	}
	,position:"last"
	,title:"Generate Deposit Tickets"
	,cursor:"pointer"
	});
	if ( $("#depositsGrid").length) {
		$("#depositsGrid").jqGrid('setColProp','abcBankCd', { editoptions: { value: rtrnBankCdList()}});
	}
	setVisibility();
});

$.subscribe('alsSabhrsEntriesComplete', function(event, data) {	
	$('#del_alsSabhrsEntriesGrid').bind( "click", function() {
		$('#alerthd_alsSabhrsEntriesGrid').closest('.ui-jqdialog').position({
			my: 'center',
			at: 'center',
			of: $('#alsSabhrsEntriesGrid').closest('div.ui-jqgrid')
		});
	});
	autoSelectTemplates();
	if ( $("#alsSabhrsEntriesGrid").length) {
   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','asacReference', { editoptions: { value: rtrnJLRList()}});
   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aamFund', { editoptions: { value: rtrnFundList()}});
   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','asacSubclass', { editoptions: { value: rtrnSubClassList()}});
   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aocOrg', { editoptions: { value: rtrnOrgList()}});
   		$("#alsSabhrsEntriesGrid").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
	}
});

$.subscribe("alsNonAlsDetailsComplete", function(event, data) {	
	if ( $("#alsNonAlsDetails").length) {
		$("#alsNonAlsDetails").jqGrid('setColProp','anatCd', { editoptions: { value: rtrnTmpCdList()}});  
	}
});

$.subscribe("alsNonAlsTemplateTableComplete", function(event, data) {
	var grid = $("#alsNonAlsTemplateTable");

	grid.jqGrid({pager:'#alsNonAlsTemplateTable_pager'}).jqGrid('navButtonAdd'
	,'#alsNonAlsTemplateTable_pager'
	,{id:"saveTemplates_alsNonAlsTemplateTable"
	,caption:"Save"
	,buttonicon:"ui-icon-check"
	,onClickButton:function(){ 
		saveTemplates();
		$('#alsNonAlsTempDiv').hide();
		$('#nonAlsSabhrsEntriesDiv').show();
		
	}
	,position:"last"
	,title:"Save"
	,cursor:"pointer"
	});

	grid.jqGrid({pager:'#alsNonAlsTemplateTable_pager'}).jqGrid('navButtonAdd'
	,'#alsNonAlsTemplateTable_pager'
	,{id:"selectAllTemplates_alsNonAlsTemplateTable"
	,caption:"Select All"
	,buttonicon:"ui-icon-circle-plus"
	,onClickButton:function(){ 
		var grid = $("#alsNonAlsTemplateTable");
	    var rows = grid.jqGrid("getDataIDs");
	    for (i = 0; i < rows.length; i++)
	    {
			$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',1);
	    }
	}
	,position:"last"
	,title:"Add Template"
	,cursor:"pointer"
	});

	grid.jqGrid({pager:'#alsNonAlsTemplateTable_pager'}).jqGrid('navButtonAdd'
	,'#alsNonAlsTemplateTable_pager'
	,{id:"deselectAllTemplates_alsNonAlsTemplateTable"
	,caption:"Deselect All"
	,buttonicon:"ui-icon-circle-minus"
	,onClickButton:function(){ 
		var grid = $("#alsNonAlsTemplateTable");
	    var rows = grid.jqGrid("getDataIDs");
	    for (i = 0; i < rows.length; i++)
	    {
			$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',0);
	    }
	}
	,position:"last"
	,title:"Add Template"
	,cursor:"pointer"
	});
});



$.subscribe("iafaRecordSelected", function (event, data) {
	$('#revError').html('');
	var grid =  $('#iafaGrid');
	var sel_id = grid.jqGrid('getGridParam', 'selrow');  

   	$('#frmIafaSeqNo').val(grid.jqGrid('getCell', sel_id, 'aiafaSeqNo'));
   	
   	$('#revAlsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadRevAlsSabhrsEntriesGrid');
});

$.subscribe('revAlsSabhrsEntriesComplete', function(event, data) {
	$("#revAlsSabhrsEntriesGrid")
	.jqGrid({pager:'#revAlsSabhrsEntriesGrid_pager'})
	.jqGrid('navButtonAdd'
	,'#revAlsSabhrsEntriesGrid_pager'
	,{id:"genReversals_revAlsSabhrsEntriesGrid"
	,caption:"Reverse ALS SABHRS ENTRIES"
	,buttonicon:"ui-icon-circle-minus"
	,onClickButton:function(){ 
		$('#frmOper').val('reverseAlsEntries');

		url = "alsAccount/manualProviderAdjEntriesSABHRSGridEdit_execute.action";    
		$.ajax({
	      type: "POST",
	      url: url,
	      dataType: "json",
	      data: $('#subGridFrm').serialize(),
	      success: function(result){

	          if(result.actionErrors){
	          	$('#revError').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
	          }else{
	          	$('#revError').html('');
				$.publish('reloadRevAlsSabhrsEntriesGrid');
	          }
	     }
	    });
	}
	,position:"last"
	,title:"Reverse Entries"
	,cursor:"pointer"
	});
	
   if ( $("#revAlsSabhrsEntriesGrid").length) {
   		$("#revAlsSabhrsEntriesGrid").jqGrid('setColProp','asacReference', { editoptions: { value: rtrnJLRList()}});
   		$("#revAlsSabhrsEntriesGrid").jqGrid('setColProp','aamFund', { editoptions: { value: rtrnFundList()}});
   		$("#revAlsSabhrsEntriesGrid").jqGrid('setColProp','asacSubclass', { editoptions: { value: rtrnSubClassList()}});
   		$("#revAlsSabhrsEntriesGrid").jqGrid('setColProp','aocOrg', { editoptions: { value: rtrnOrgList()}});
   		$("#revAlsSabhrsEntriesGrid").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
   }

});

/*Non Als SABHRS Entries Grid*/
function autoSelectTemplates() {	
	/*Auto select templates based on what is in the Non Als Table.*/
	var grid = $("#alsNonAlsTemplateTable");
    var rows = grid.jqGrid("getDataIDs");
    for (i = 0; i < rows.length; i++)
    {
		$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',0);
		$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'amount','');
    }
	var codes = [];
	var amts = [];
	grid = $('#alsNonAlsDetails');
	rows = grid.jqGrid("getDataIDs");
	for (i = 0; i < rows.length; i++)
    {
		var code = grid.jqGrid('getCell', rows[i], 'anatCd');
		var amt = grid.jqGrid('getCell', rows[i], 'anadAmount');
		if(code != null && code != ""){
			codes.push(code);
			amts.push(amt);
		}
    }
	grid = $('#alsNonAlsTemplateTable');
	rows = grid.jqGrid("getDataIDs");
	for (i = 0; i < rows.length; i++)
    {
		var code = grid.jqGrid('getCell', rows[i], 'idPk.anatCd');
		if(codes.indexOf(code) != -1){
			var index = codes.indexOf(code);
			$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',1);
			$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'amount',amts[index]);
		}
    }
}

function saveTemplates(){	
	var amountSet = true;
	var templateSeleted = false;
	var grid = $("#alsNonAlsTemplateTable");
    var rows = grid.jqGrid("getDataIDs");
    var tmp = "";
	for (i = 0; i < rows.length; i++)
    {
        var selected = grid.jqGrid ('getCell', rows[i], 'selected');
        var amount = grid.jqGrid ('getCell', rows[i], 'amount');
        var anatCd = grid.jqGrid ('getCell', rows[i], 'idPk.anatCd');
        if(selected == 1){
        	templateSeleted = true;
        }
        if(selected == 1 && amount <= 0){
        	amountSet = false;
        }else if(selected == 1 && amount > 0){
        	tmp = tmp + anatCd +"-"+ amount +",";
        }
    }	
   	if(templateSeleted){
	    if(amountSet){
	    	url = "alsAccount/alsSabhrsEntriesGridEdit_execute.action";    
    		$.ajax({
              type: "POST",
              url: url,
              dataType: "json",
              data: {oper:"addTemplates",
            	     templates:tmp,
            	     transGrp:8,
            	     provNo:$('#frmProvNo').val(),
            	     bpTo:$('#frmBPTo').val()},
              success: function(result){
                  if(result.actionErrors){
                  	$('#tempError').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
                  }else{
					$.publish('reloadSubGrids');
                  }
             }
            });
	    }else{
	    	alert("An amount must be entered for each selected template.");
	    }
	}	   	
}

/*Bank Details Grid*/
function getTDT() {
	var generate = false;
	var grid = $("#depositsGrid");
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

/*REMITTANCE FORM*/
function completedByProv(){
	if($('#displayBalanced').val()== "Y"){
		if($('#provComp').is(':checked')){
			var date = $.datepicker.formatDate('mm/dd/yy', new Date());
			$('#compDt').val(date);
		}else{
			$('#compDt').val('');
		}
	}else{
		alert("Remittance cannot be completed, it does not balanced.");
		$('#provComp').prop('checked', false);
	}
	
}

function remittanceApproved(){
	if($('#provComp').is(':checked')){
		var date = $.datepicker.formatDate('mm/dd/yy', new Date());
		$('#disAppDt').val(date);
	}else{
		alert("Remittance must first be completed by the provider.");
		$('#remApp').prop('checked', false);
	}
	
}

function saveRemittance(){
	var grid = $('#alsInternalRemittance');
	selectedRow = grid.jqGrid('getGridParam','selrow'); 
	url = "alsAccount/alsInternalRemittanceGridEdit_execute.action";    
	$.ajax({
      type: "POST",
      url: url,
      dataType: "json",
      data: {oper:"edit",
	    	 id:$('#alsInternalRemittance').jqGrid('getGridParam','selrow'),
	    	 provComp:$('#provComp').is(':checked'),
	    	 remApp:$('#remApp').is(':checked'),
	    	 disAppCom:$('#disAppCom').val(),
	    	 ccSales:$('#displayCCSales').val()},
      success: function(result){
          if(result.actionErrors){
          	$('#remError').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
          }else{
        	  $('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
      		  $.publish('reloadInternalRemittance');
          }
     }
    });
};

/*OTHERS*/
function setEnabledFields(id){
	if(hasUserRole == 'true'){
		$("input[name='apbdCashInd']").prop({disabled:true});
	   	$("select[name='abcBankCd']").prop({disabled:true});
	   	$("input[name='apbdAmountDeposit']").prop({disabled:true});
	}
}

function prePopulate(id){
	var grid = $("#"+id);
	var rows = grid.jqGrid("getDataIDs");
	var length = grid.jqGrid("getDataIDs").length-1;
	if(id == "depositsGrid"){
		$('#abcBankCd').val(grid.jqGrid ('getCell', rows[length], 'abcBankCd'));
	    $('#billingFrom').val(grid.jqGrid ('getCell', rows[length], 'billingFrom'));
	    $('#apbdBillingTo').val(grid.jqGrid ('getCell', rows[length], 'apbdBillingTo'));
	    $('#amtDue').val(grid.jqGrid ('getCell', rows[length], 'amtDue'));
	}else if(id == "revAlsSabhrsEntriesGrid"){
		$('#aamBusinessUnit').val("52010");
	    $('#asacProgram').val($('#curBudgYear').val());
	    $('#asacBudgetYear').val($('#curBudgYear').val());
	}
    
}

function setVisibility(){
	var grid = $('#alsInternalRemittance');
	var sel_id = grid.jqGrid('getGridParam','selrow'); 
	var dtCompletedByProv = grid.jqGrid('getCell', sel_id, 'completeProvider');
	var remittanceApproved = grid.jqGrid('getCell', sel_id, 'airOfflnPaymentApproved');
	var interfaced = grid.jqGrid('getCell', sel_id, 'intFileGenerated');
	var bankDepEditOnly = grid.jqGrid('getCell', sel_id, 'bankDepEditOnly');
	
	disableEditable();
	if(sel_id != null && interfaced != "YES" && hasIntProvRole == 'true'){
			if(dtCompletedByProv.length == 1){
				$('#add_depositsGrid').show();
				$('#view_depositsGrid').show();
				$('#del_depositsGrid').show();
				$('#edit_depositsGrid').show();
				if(bankDepEditOnly == 'false'){
					$('#alsNonAlsDetails_pager_left').show();
					$('#alsOverUnderSales_pager_left').show();
					$('#provComp').prop({disabled:false});
					$('#displayCCSales').prop({disabled:false});
				}
			}else{
				if(remittanceApproved == 'false'){
					$('#provComp').prop({disabled:false});
				}
			}
	}
	if(sel_id != null && hasUserRole == 'true'){
		$("#userTabs").show();
		if(interfaced != "YES"){
			if(remittanceApproved == 'true'){
				$('#remApp').prop({disabled:false});
			}else{
				if(dtCompletedByProv.length != 1){
					$('#edit_depositsGrid').show();
					$('#remApp').prop({disabled:false});
					$('#disAppCom').prop({disabled:false});
					$('#alsSabhrsEntriesGrid_pager_left').show();
					$('#revAlsSabhrsEntriesGrid_pager_left').show();
					$('#alsNonAlsTemplateTable_pager_left').show();
				}
			}
		}		
	}
	if(interfaced != "YES" && bankDepEditOnly == 'false'){
		$('#saveRemittance').prop({disabled:false});
	}	
}

function disableEditable(){
	//User
	$("#userTabs").hide();
	$('#alsSabhrsEntriesGrid_pager_left').hide();
	$('#revAlsSabhrsEntriesGrid_pager_left').hide();
	$('#alsNonAlsTemplateTable_pager_left').hide();
	$('#remApp').prop({disabled:true});
	$('#disAppCom').prop({disabled:true});
	//Int Prov
	$('#alsNonAlsDetails_pager_left').hide();
	$('#alsOverUnderSales_pager_left').hide();
	$('#add_depositsGrid').hide();
	$('#view_depositsGrid').hide();
	$('#del_depositsGrid').hide();
	$('#edit_depositsGrid').hide();
	$('#provComp').prop({disabled:true});
	$('#displayCCSales').prop({disabled:true});
	
	$('#saveRemittance').prop({disabled:true});
}

function resetSubGridForm(){
	$('#frmProvNo').val(null);
	$('#frmBPFrom').val(null);
	$('#frmBPTo').val(null);
	$('#frmIafaSeqNo').val(null);
}

function rtrnBankCdList() {return $("#bankCdLst").val();}
function rtrnTmpCdList() {return $("#tmpCdLst").val();}
function rtrnAccountList() {return $("#accountLst").val();}
function rtrnOrgList() {return $("#orgLst").val();}
function rtrnJLRList() {return $("#jlrLst").val();}
function rtrnFundList() {return $("#fundLst").val();}
function rtrnSubClassList() {return $("#subClassLst").val();}  

/*ACTIONS*/
function submitSearch(){
		selectedRow = null;
		$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadInternalRemittance');
}

function resetSearch(){
	selectedRow = null;
	$('#frmProvNo').val(null);
	$('#frmBPFrom').val(null);
	$('#frmBPTo').val(null);
	$('#frmIafaSeqNo').val(null);
	$('#gridFrm')[0].reset();
	$('#displayRemittanceDiv').hide();
	$('#displayRemittanceDiv').find('input:text').val(''); 
	submitSearch();
	$('#depositsGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
	$('#alsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadBankDepGrids');
	$.publish('reloadNonAlsDetGrids');
	$.publish('reloadSubGrids');
}

function gridToCSV(){
	$.ajax({
		type: "POST",
	data: JSON.stringify(exportGrid("alsInternalRemittance","remittanceRecords","gridFrm")),
	dataType: "json",
	cache: false,
	contentType: "application/json",
	url: 'internalRemittanceGridsToCsv.action',
	success: function (data) {
		window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
	}, complete: function () {
		$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
		$('#alsInternalRemittance').trigger("reloadGrid");
	},
	error: function (x, e) {
		ajaxErrorHandler(x, e, "Save", null, null);
		}
	});
}

function intRemittanceRptCSV(){
	if(selectedRow != null){
		$.ajax({
			type: "POST",
		data: JSON.stringify(exportGrid("alsInternalRemittance","remittanceRecords","gridFrm", true)),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		url: 'internalRemittanceReport.action',
		success: function (data) {
			window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
		}, complete: function () {
			$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
			$('#alsInternalRemittance').trigger("reloadGrid");
		},
		error: function (x, e) {
			ajaxErrorHandler(x, e, "Save", null, null);
			}
		});
	}else{
		alert("A record must be selected from the Internal Remittance Grid.");
	}
	
}