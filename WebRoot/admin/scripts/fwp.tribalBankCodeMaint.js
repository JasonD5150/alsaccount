

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

$.subscribe('bankSelect', function(event,data) {
	id = event.originalEvent.id;
	$('#frmBankId').val(id)
	$.publish('reloadTribeBankItemDiv');   
});



function setDisabled(id){
   	
    	$("input[name='aictUsagePeriodFrom']").prop({disabled:true});
	   	$("input[name='aictUsagePeriodTo']").prop({disabled:true});
	   	$("input[name='tribeID']").prop({disabled:true});
	   	//document.getElementById("idPk.aaalDrCrCd").disabled=true;
}

function addNewItem(){
   
      
    $('#addItemDialog').dialog('open');   
}



$.subscribe('tribeBankItemTableComplete', function(event, data) {
	
	
		if($("#add_tribeBankItemTable").length == 0) {
			$("#tribeBankItemTable")
				.jqGrid({pager:'#tribeBankItemTable_pager'})
				.jqGrid('navButtonAdd'
				,'#tribeBankItemTable_pager'
				,{id:"add_tribeBankItemTable"
				,caption:""
				,buttonicon:"ui-icon-plus"
				,onClickButton:addNewItem
				,position:"first"
				,title:"Add Questions"
				,cursor:"pointer"}
				);
			$("#add_tribeBankItemTable").addClass('ui-state-disabled');
		} else if ($("#icId").val() != null || $("#icId").val() != "") {
			$("#add_tribeBankItemTable").removeClass('ui-state-disabled');
		} else {
			$("#add_tribeBankItemTable").addClass('ui-state-disabled');
		}
		
	
});	

function readMultiSelect(){
    $('#itemKey').val($("#addTribeBankItem").jqGrid('getGridParam','selarrrow'));
}