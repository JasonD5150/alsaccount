/**
 * 
 * genNonAlsEntries.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function(){
	resetSearch();
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

$.subscribe('transGroupDtlsSelected', function(event, data) {	
	$('#reverseAlsEntries').prop({disabled:false});
	var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');					    				  
	$('#frmTransGrp').val($("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgTransactionCd'));
	$('#frmTransIdentifier').val($("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgsGroupIdentifier'));
	
	$("#alsSabhrsEntriesTable").jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadAlsSabhrsEntriesTable');
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
			$('#accMasterDialog').dialog('open');
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
	   
	   var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');
	   if(sel_id != null){
		   $("td[id='add_alsSabhrsEntriesTable']").toggle(true);
		   $("td[id='addTemplate_alsSabhrsEntriesTable']").toggle(true);
	   }else{
		   $("td[id='add_alsSabhrsEntriesTable']").toggle(false);
		   $("td[id='addTemplate_alsSabhrsEntriesTable']").toggle(false);
	   }
	   menuSec("genNonAlsEntries");
});

function rtrnAccountList() {
			var rslt = $("#accountLst").val();
			return rslt;
}

function rtrnOrgList() {
			var rslt = $("#orgLst").val();
			return rslt;
}

function rtrnJLRList() {
			var rslt = $("#jlrLst").val();
			return rslt;
}

function rtrnFundList() {
			var rslt = $("#fundLst").val();
			return rslt;
}

function rtrnSubClassList() {
			var rslt = $("#subClassLst").val();
			return rslt;
}
		
function selectAll(){
	var grid = $("#alsNonAlsTemplateTable");
    var rows = grid.jqGrid("getDataIDs");
    for (i = 0; i < rows.length; i++)
    {
		$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',1);
    }
}

function deselectAll(){
	var grid = $("#alsNonAlsTemplateTable");
    var rows = grid.jqGrid("getDataIDs");
    for (i = 0; i < rows.length; i++)
    {
		$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',0);
    }
}

function exitNonAlsMasterDialog(){	
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
	   	
	   	$('#frmNonAlsEntries').val(tmp);
	   	
	   	if(templateSeleted){
		    if(amountSet){
		    	$('#frmOper').val('addTemplates');
		    	
				url = "alsAccount/alsSabhrsEntriesGridEdit_execute.action";    
        		$.ajax({
                  type: "POST",
                  url: url,
                  dataType: "json",
                  data: $('#alsSabhrsEntriesGridForm').serialize(),
                  success: function(result){
	                  if(result.actionErrors){
	                  	$('#html').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
	                  }else{
	                  	$('#accMasterDialog').dialog('close');
						$.publish('reloadAlsSabhrsEntriesTable');
	                  }
                 }
                });
		    }else{
		    	alert("An amount must be entered for each selected template.");
		    }
  		}else{
  			$('#accMasterDialog').dialog('close');
  		}
	   	
}

function prePopulate(){
    $('#aamBusinessUnit').val('52010');
    $('#asacProgram').val($('#hidBudgYear').val());
    $('#asacBudgetYear').val($('#hidBudgYear').val());
}

/*ACTIONS*/
function submitSearch(){
	$('#reverseAlsEntries').prop({disabled:false});
	$('#budgYear').val($('#budgYear_widget').val());
	$('#provNo').val($('#provNo_widget').val());
	$('#transGrpType').val($('#transGrpType_widget').val());
	$('#transGrpIdentifier').val($('#transGrpIdentifier_widget').val());
	$('#transGroupDtlsTable').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadTransGroupDtlsTable');
}
function resetSearch(){
	$('#frmTransGrp').val('');
	$('#frmTransIdentifier').val('');
	$('#frmNonAlsEntries').val('');
	
	$('#gridFrm')[0].reset();
	
	$('#budgYear').val('');
	$('#provNo').val('');
	$('#transGrpType').val('');
	$('#transGrpIdentifier').val('');
}
function reverseAlsEntries(){
	var grid =  $('#transGroupDtlsTable');
	var sel_id = grid.jqGrid('getGridParam', 'selrow'); 
	var transCd = grid.jqGrid('getCell', sel_id, 'idPk.atgTransactionCd'); 
	var groupId = grid.jqGrid('getCell', sel_id, 'idPk.atgsGroupIdentifier'); 
	window.open("/alsaccount/manualProviderAdjEntries_input.action?transCd="+transCd+"&groupId="+groupId+"&prntMenu=ALSACCOUNT");
}