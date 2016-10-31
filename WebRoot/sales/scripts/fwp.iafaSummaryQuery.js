/**
 * 
 * ifaSummaryQuery.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function(){
	$("#iafaSummaryByAmountTypeQueryTable").bind("jqGridInitGrid", function (e) {
	    $(this).jqGrid("setColProp", "tCount", { summaryType: "sum" });
	    $(this).jqGrid("setColProp", "tAmount", { summaryType: "sum" });  
	});
});

$.subscribe('resetSearchCriteria', function(event,data)  {
	$('#amountTypeDiv').toggle(false);
	$('#itemTypeDiv').toggle(true);
	/*$('.itemType').hide();
	$('.amountType').hide();
	if(getQryType()==1){
		$('.itemType').show();
		$('#amountTypeDiv').toggle(false);
		$('#itemTypeDiv').toggle(true);
	}else{
		$('.amountType').show();
		$('#amountTypeDiv').toggle(true);
		$('#itemTypeDiv').toggle(false);
	}*/
});

$.subscribe("iafaSummaryTableComplete", function (event, data) {
		var grid = $('#iafaSummaryTable');
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
		   
     grid.jqGrid({pager:'#iafaSummaryTable_pager'})
		.jqGrid('navButtonAdd'
		,'#iafaSummaryTable_pager'
		,{id:"columnSelector_iafaSummaryTable"
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

function getQryType(){
	var qryType=document.getElementsByName("qryType");
	var selectedQryType;
	for(var i=0;i<qryType.length;i++){
		if(qryType[i].checked){
			selectedQryType = qryType[i].value;
		}
	}
	return selectedQryType;
}

/*ACTIONS*/
function submitSearch(){
	$('#issProvNo').val($('#issProvNo_widget').val());
	$('#itemTypeCd').val($('#itemTypeCd_widget').val());
	$('#itemStat').val($('#itemStat_widget').val());
	$('#itemCatCd').val($('#itemCatCd_widget').val());
	$('#procCatCd').val($('#procCatCd_widget').val());
	$('#procTypeCd').val($('#procTypeCd_widget').val());
	$('#tribeCd').val($('#tribeCd_widget').val());
	$('#appDis').val($('#appDis_widget').val());
	$('#amountTypeCd').val($('#amountTypeCd_widget').val());
	$('#costPrereqCd').val($('#costPrereqCd_widget').val());
	$('#reasonCd').val($('#reasonCd_widget').val());
	$('#appType').val($('#appType_widget').val());
	
	$('#iafaSummaryTable').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadIafaSummaryTable');
	/*if(getQryType()==1){
		$('#iafaSummaryByItemTypeQueryTable').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadIafaSumItemTypeQueryTable');
	}else{
		$('#iafaSummaryByAmountTypeQueryTable').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadIafaSumAmountTypeQueryTable');
	}*/
}

function resetSearch(){
	$('#gridFrm')[0].reset();
}

function exportToCSV(){
	$.ajax({
		type: "POST",
		data: JSON.stringify(exportGrid("iafaSummaryTable","iafaSummaryRecords","gridFrm")),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		url: 'iafaSummaryQueryBuildCsv.action',
		success: function (data) {
			window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
		}, complete: function () {
			$.unblockUI();
			//$('#iafaSummaryTable').jqGrid('setGridParam',{datatype:'json'});
			//$.publish('reloadIafaSummaryTable');
		},
	 	error: function (x, e) {
			ajaxErrorHandler(x, e, "Save", null, null);
		} 
	});
}