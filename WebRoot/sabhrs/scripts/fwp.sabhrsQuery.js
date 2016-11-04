/**
 * 
 * sabhrsQuery.jsp Javascript
 * 
 * @author cfa027
 */
$.subscribe("alsSabhrsQueryTableComplete", function (event, data) {
	var error = $("#alsSabhrsQueryTable").jqGrid('getGridParam', 'userData');
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

	var grid = $('#alsSabhrsQueryTable');         
     $("#alsSabhrsQueryTable")
		.jqGrid({pager:'#alsSabhrsQueryTable_pager'})
		.jqGrid('navButtonAdd'
		,'#alsSabhrsQueryTable_pager'
		,{id:"columnSelector_alsSabhrsQueryTable"
		,caption:""
		,buttonicon:"ui-icon-extlink"
		,onClickButton:function(){ 
			grid.jqGrid('columnChooser',{width: 500});
		}
		,position:"last"
		,title:"Add/Remove Columns"
		,cursor:"pointer"
		});

});

function budgetYearSelected(){
	$('#frmBudgYear').val($('#budgYear').val());
	$.publish('reloadLists');
}

/*ACTIONS*/
function submitSearch(){
	$('#providerNo').val($('#providerNo_widget').val());
	$('#budgYear').val($('#budgYear_widget').val());
	$('#jlr').val($('#jlr_widget').val());
	$('#account').val($('#account_widget').val());
	$('#fund').val($('#fund_widget').val());
	$('#org').val($('#org_widget').val());
	$('#subClass').val($('#subClass_widget').val());
	$('#tribeCd').val($('#tribeCd_widget').val());
	$('#sysActTypeCd').val($('#sysActTypeCd_widget').val());
	$('#transGrpType').val($('#transGrpType_widget').val());
	var search = true;
	if(($('#seqNo').val() != null && $('#seqNo').val() != "" )&& 
		(($('#providerNo').val() == null || $('#providerNo').val() == "") || 
		($('#bpFromDt').val() == null || $('#bpFromDt').val() == "")|| 
		($('#bpToDt').val() == null || $('#bpToDt').val() == ""))){
		search = false;
		alert("To search by IAFA Seq No you must also enter a Provider No, Billing Period From and Billing Period To");
	}
	if(search){
		$('#alsSabhrsQueryTable').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadAlsSabhrsEntriesTable')
	}
}
function resetSearch(){
	$('#gridFrm')[0].reset();
}

function exportToCSV(){
	$.ajax({
		type: "POST",
		data: JSON.stringify(exportGrid("alsSabhrsQueryTable","sabhrsEntries","gridFrm")),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		url: 'sabhrsQueryBuildCsv.action',
		success: function (data) {
			window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
		}, complete: function () {
			$.unblockUI();
			//$('#alsSabhrsQueryTable').jqGrid('setGridParam',{datatype:'json'});
			//$('#alsSabhrsQueryTable').trigger("reloadGrid");
		},
		error: function (x, e) {
			ajaxErrorHandler(x, e, "Save", null, null);
		}
	});
}