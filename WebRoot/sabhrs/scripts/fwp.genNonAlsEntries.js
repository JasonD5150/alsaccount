/**
 * 
 * genNonAlsEntries.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function(){
	$('#frmTransGrp').val('');
	$('#frmTransIdentifier').val('');
	$('#frmNonAlsEntries').val('');
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
};

$.subscribe('transGroupDtlsSelected', function(event, data) {				
	var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');					    				  
	$('#frmTransGrp').val($("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgTransactionCd'));
	$('#frmTransIdentifier').val($("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgsGroupIdentifier'));
	
	$("#alsSabhrsEntriesTable").jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadAlsSabhrsEntriesTable');
});


$.subscribe('alsSabhrsEntriesComplete', function(event, data) {				
		$("#alsSabhrsEntriesTable")
		.jqGrid({pager:'#alsSabhrsEntriesTable_pager'})
		.jqGrid('navButtonAdd'
		,'#alsSabhrsEntriesTable_pager'
		,{id:"add_alsSabhrsEntriesTable"
		,caption:""
		,buttonicon:"ui-icon-plus"
		,onClickButton:function(){ 
			var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');
			if(sel_id != null){
				$('#accMasterDialog').dialog('open');
			}else{
				alert("You must select a Transaction Group before you can add a SABHRS Entry.");
			}
		}
		,position:"first"
		,title:"Add"
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
		    	$('#frmOper').val('add');
		    	
				url = "alsAccount/alsSabhrsEntriesGridEdit_execute.action";    
        		$.ajax({
                  type: "POST",
                  url: url,
                  dataType: "json",
                  data: $('#gridFrm').serialize(),
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