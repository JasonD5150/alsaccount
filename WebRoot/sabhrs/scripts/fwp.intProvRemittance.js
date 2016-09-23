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
	$('#frmProvNo').val(null);
	$('#frmBPFrom').val(null);
	$('#frmBPTo').val(null);
	hasUserRole = $('#hasUserRole').val();
	hasIntProvRole = $('#hasIntProvRole').val();
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

	grid
	.jqGrid({pager:'#alsInternalRemittance_pager'})
	.jqGrid('navButtonAdd'
	,'#alsInternalRemittance_pager'
	,{id:"columnSelector_alsInternalRemittance"
	,caption:""
	,buttonicon:"ui-icon-extlink"
	,onClickButton:function(){ 
		grid.jqGrid('columnChooser',{caption: "Columns: CTRL-click select/deselect a column, CTRL-A select all",
									 width: 500});
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

$.subscribe("depositsGridComplete", function(event, data) {	
	$("#depositsGrid")
	.jqGrid({pager:'#depositsGrid_pager'})
	.jqGrid('navButtonAdd'
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
	$("#depositsGrid").jqGrid('setColProp','abcBankCd', { editoptions: { value: rtrnBankCdList()}});
	setVisibility();
});

$.subscribe('alsSabhrsEntriesComplete', function(event, data) {	
	$('#del_alsSabhrsEntriesTable').bind( "click", function() {
		$('#alerthd_alsSabhrsEntriesTable').closest('.ui-jqdialog').position({
			my: 'center',
			at: 'center',
			of: $('#alsSabhrsEntriesTable').closest('div.ui-jqgrid')
		});
	});
	$("#alsSabhrsEntriesTable")
	.jqGrid({pager:'#alsSabhrsEntriesTable_pager'})
	.jqGrid('navButtonAdd'
	,'#alsSabhrsEntriesTable_pager'
	,{id:"addTemplate_alsSabhrsEntriesTable"
	,caption:"Add Template"
	,buttonicon:"ui-icon-circle-plus"
	,onClickButton:function(){ 
		autoSelectTemplates();
		$('#nonAlsSabhrsEntriesDiv').hide();
		$('#alsNonAlsTempDiv').show();
	}
	,position:"last"
	,title:"Add Template"
	,cursor:"pointer"
	});
	
   if ( $("#alsSabhrsEntriesTable").length) {
   		$("#alsSabhrsEntriesTable").jqGrid('setColProp','asacReference', { editoptions: { value: rtrnJLRList()}});
   		$("#alsSabhrsEntriesTable").jqGrid('setColProp','aamFund', { editoptions: { value: rtrnFundList()}});
   		$("#alsSabhrsEntriesTable").jqGrid('setColProp','asacSubclass', { editoptions: { value: rtrnSubClassList()}});
   		$("#alsSabhrsEntriesTable").jqGrid('setColProp','aocOrg', { editoptions: { value: rtrnOrgList()}});
   		$("#alsSabhrsEntriesTable").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
   }

});

$.subscribe("alsNonAlsTemplateTableComplete", function(event, data) {	
	$("#alsNonAlsTemplateTable")
	.jqGrid({pager:'#alsNonAlsTemplateTable_pager'})
	.jqGrid('navButtonAdd'
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
	$("#alsNonAlsTemplateTable")
	.jqGrid({pager:'#alsNonAlsTemplateTable_pager'})
	.jqGrid('navButtonAdd'
	,'#alsNonAlsTemplateTable_pager'
	,{id:"exitTemplates_alsNonAlsTemplateTable"
	,caption:"Exit"
	,buttonicon:"ui-icon-close"
	,onClickButton:function(){ 
		$('#alsNonAlsTempDiv').hide();
		$('#nonAlsSabhrsEntriesDiv').show();
		
	}
	,position:"last"
	,title:"Exit"
	,cursor:"pointer"
	});
	$("#alsNonAlsTemplateTable")
	.jqGrid({pager:'#alsNonAlsTemplateTable_pager'})
	.jqGrid('navButtonAdd'
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
	$("#alsNonAlsTemplateTable")
	.jqGrid({pager:'#alsNonAlsTemplateTable_pager'})
	.jqGrid('navButtonAdd'
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

$.subscribe("internalRemittanceSelected", function(event, data) {	
	/*LOAD SUBGRIDS*/
	var grid = $('#alsInternalRemittance');
	var sel_id = grid.jqGrid('getGridParam','selrow'); 
	$('#frmProvNo').val(grid.jqGrid('getCell', sel_id, 'idPk.apiProviderNo'));
	$('#frmBPFrom').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom'));
	$('#frmBPTo').val(grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo'));
	
	$('#depositsGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
	$('#alsSabhrsEntriesTable').jqGrid('setGridParam',{datatype:'json'});
	$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
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
	
	$('#displayRemittanceDiv').show();
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
        	  submitSearch();
          }
     }
    });
};

/*OTHERS*/
function prePopulate(){
	var grid = $("#intProvBankCdDepLinkGrid");
	var rows = grid.jqGrid("getDataIDs");
	var length = grid.jqGrid("getDataIDs").length-1;

    $('#abcBankCd').val(grid.jqGrid ('getCell', rows[length], 'abcBankCd'));
    $('#billingFrom').val(grid.jqGrid ('getCell', rows[length], 'billingFrom'));
    $('#apbdBillingTo').val(grid.jqGrid ('getCell', rows[length], 'apbdBillingTo'));
    $('#amtDue').val(grid.jqGrid ('getCell', rows[length], 'amtDue'));
}

function setVisibility(){
	var grid = $('#alsInternalRemittance');
	var sel_id = grid.jqGrid('getGridParam','selrow'); 
	var dtCompletedByProv = grid.jqGrid('getCell', sel_id, 'completeProvider');
	var remittanceApproved = grid.jqGrid('getCell', sel_id, 'airOfflnPaymentApproved');
	if(dtCompletedByProv.length == 1 && hasIntProvRole == 'true'){
		$('#alsNonAlsDetails_pager_left').show();
		$('#alsOverUnderSales_pager_left').show();
		$('#depositsGrid_pager_left').show();
		$('#provComp').prop({disabled:false});
		$('#displayCCSales').prop({disabled:false});
	}else if(dtCompletedByProv.length != 1 && hasIntProvRole == 'true'){
		$('#alsNonAlsDetails_pager_left').hide();
		$('#alsOverUnderSales_pager_left').hide();
		$('#depositsGrid_pager_left').hide();
		if(remittanceApproved == 'true'){
			$('#provComp').prop({disabled:true});
		}else{
			$('#provComp').prop({disabled:false});
		}
		$('#displayCCSales').prop({disabled:true});
	}else{
		$('#alsNonAlsDetails_pager_left').hide();
		$('#alsOverUnderSales_pager_left').hide();
		$('#depositsGrid_pager_left').hide();
		$('#provComp').prop({disabled:true});
		$('#displayCCSales').prop({disabled:true});
	}
	if(hasUserRole == 'true'){
		$("#tab3").show();
		$('#remApp').prop({disabled:false});
		$('#disAppCom').prop({disabled:false});
	}else{
		$("#tab3").hide();
		$('#remApp').prop({disabled:true});
		$('#disAppCom').prop({disabled:true});
	}
	if(dtCompletedByProv.length != 1 && hasUserRole == 'true'){
		$('#alsNonAlsDetails_pager_left').show();
	}
}

function rtrnBankCdList() {return $("#bankCdLst").val();}
function rtrnAccountList() {return $("#accountLst").val();}
function rtrnOrgList() {return $("#orgLst").val();}
function rtrnJLRList() {return $("#jlrLst").val();}
function rtrnFundList() {return $("#fundLst").val();}
function rtrnSubClassList() {return $("#subClassLst").val();}  

/*ACTIONS*/
function submitSearch(){
		$('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadInternalRemittance');

}

function resetSearch(){
	selectedRow = null;
	$('#frmProvNo').val(null);
	$('#frmBPFrom').val(null);
	$('#frmBPTo').val(null);
	$('#gridFrm')[0].reset();
	$('#displayRemittanceDiv').hide();
	$('#displayRemittanceDiv').find('input:text').val(''); 
	submitSearch();
	$('#depositsGrid').jqGrid('setGridParam',{datatype:'json'});
	$('#alsNonAlsDetails').jqGrid('setGridParam',{datatype:'json'});
	$('#alsSabhrsEntriesTable').jqGrid('setGridParam',{datatype:'json'});
	$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadSubGrids');
}

function gridToCSV(){
	var gridList = ["alsInternalRemittance","depositsGrid","alsNonAlsDetails","alsOverUnderSales"];
	var dataLabelList = ["alsInternalRemittanceEntries","depositsEntries","alsNonAlsEntries","alsOverUnderSalesEntries"];
	
	_data = {};	
	_columnsList=[];
	for (i=0;i<gridList.length;i++){
		var _jqGrid=$("#"+gridList[i]);
		var _rowNum = _jqGrid.jqGrid("getGridParam", "rowNum"),
		_columnModel = _jqGrid.jqGrid("getGridParam", "colModel"),
		_columnNames = _jqGrid.jqGrid("getGridParam", "colNames"),
		_columns = [];
		_data[dataLabelList[i]] = _jqGrid.jqGrid("getGridParam", "data");
		
		$.each(_columnModel, function (index) {
			if (!_columnModel[index].hidden) {
				_columns.push({
					"itemVal": _columnModel[index].name,
					"itemLabel": _columnNames[index]
				});
			}
		});
		if(gridList[i] == "alsInternalRemittance"){
			_data["alsIntRemittanceSelectedColumns"] = _columns;
		}else{
			_data["columnNameValues"] = _columns;
		}
		/* Null empty strings in non-text fields */
		_columnsList.push(_columns);
		$.each(_data[dataLabelList[i]],function(index, item){
			$.each(_columnModel, function(cidx, citem){
				if(_columnModel[cidx].sorttype !=="text" &&
					item[_columnModel[cidx].name]==="") {
					item[_columnModel[cidx].name] = null;
				}
			});
		});
	}
	_data["columnsList"]=_columnsList;
	
	var filters = $('#gridFrm').serialize();
	_data["filters"] =  filters;
	_data["selRow"] = $('#alsInternalRemittance').jqGrid("getGridParam", "selrow");
	
	
	$.ajax({
		type: "POST",
	data: JSON.stringify(_data),
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
	$.ajax({
		type: "POST",
	data: JSON.stringify(exportGrid("alsInternalRemittance","remittanceRecords","gridFrm")),
	dataType: "json",
	cache: false,
	contentType: "application/json",
	url: 'internalRemittanceReport.action',
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