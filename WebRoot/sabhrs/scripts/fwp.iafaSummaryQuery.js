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
	$('.itemType').hide();
	$('.amountType').hide();
	if(getQryType()==1){
		$('.itemType').show();
		$('#amountTypeDiv').toggle(false);
		$('#itemTypeDiv').toggle(true);
	}else{
		$('.amountType').show();
		$('#amountTypeDiv').toggle(true);
		$('#itemTypeDiv').toggle(false);
	}
});

$.subscribe("iafaSumItemTypeQueryTableComplete", function (event, data) {
		var grid = $('#iafaSummaryByItemTypeQueryTable');
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

$.subscribe("iafaSumAmountTypeQueryTableComplete", function (event, data) {
		var grid = $('#iafaSummaryByItemTypeQueryTable');
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
	
	if(getQryType()==1){
		$('#iafaSummaryByItemTypeQueryTable').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadIafaSumItemTypeQueryTable');
	}else{
		$('#iafaSummaryByAmountTypeQueryTable').jqGrid('setGridParam',{datatype:'json'});
		$.publish('reloadIafaSumAmountTypeQueryTable');
	}
}

function resetSearch(){
	$('#gridFrm')[0].reset();
}

function exportToCSV(){
	var tableNm;
	if(getQryType()==1){
		tableNm = "iafaSummaryByItemTypeQueryTable";
	}else{
		tableNm = "iafaSummaryByAmountTypeQueryTable";
	}
	$.ajax({
		type: "POST",
		data: JSON.stringify(exportGrid(tableNm,"iafaSummaryRecords","gridFrm")),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		url: 'iafaSummaryQueryBuildCsv.action',
		success: function (data) {
			window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
		}, complete: function () {
			$('#iafaSummaryQueryTable').jqGrid('setGridParam',{datatype:'json'});
			$.publish('reloadIafaSummaryQueryTable');
		},
	 	error: function (x, e) {
			ajaxErrorHandler(x, e, "Save", null, null);
		} 
	});
}