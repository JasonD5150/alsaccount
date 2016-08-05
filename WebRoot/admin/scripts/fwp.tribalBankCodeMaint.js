
function errorHandler(response, postdata) {
	rtrnstate = true;
	rtrnMsg = "";
	json = eval('(' + response.responseText + ')');
	if (json.actionErrors) {
		rtrnstate = false;
		for (i = 0; i < json.actionErrors.length; i++) {
			rtrnMsg += json.actionErrors[i] + '<br/>';
		}
	}
	return [ rtrnstate, rtrnMsg ];
};

$.subscribe('bankSelect', function(event, data) {
	id = event.originalEvent.id;
	$('#frmBankId').val(id)
	$('#tribeId').val(id)
	$.publish('reloadTribeBankItemDiv');
});

function addNewItem() {

	$('#addItemDialog').dialog('open');
}

function closeNewItem() {

	$('#addItemDialog').dialog('close');
}

$.subscribe('tribeBankItemTableComplete', function(event, data) {

	$("#tribeBankItemTable").jqGrid({
		pager : '#tribeBankItemTable_pager'
	}).jqGrid('navButtonAdd', '#tribeBankItemTable_pager', {
		id : "add_tribeBankItemTable",
		caption : "",
		buttonicon : "ui-icon-plus",
		onClickButton : addNewItem,
		position : "first",
		title : "Add Items",
		cursor : "pointer"
	});

});

function readMultiSelect() {

	$('#itemKey').val(
			$("#addTribeBankItem").jqGrid('getGridParam', 'selarrrow'));

}
