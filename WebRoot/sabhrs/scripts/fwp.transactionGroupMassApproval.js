/**
 * 
 * transactionGroupMassApproval.jsp Javascript
 * 
 * @author cfa027
 */
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

$.subscribe('transGroupMassApprovalComplete', function(event, data) {				
	$("#transGroupMassApprovalTable")
		.jqGrid({pager:'#transGroupMassApprovalTable_pager'})
		.jqGrid('navButtonAdd'
		,'#transGroupMassApprovalTable_pager'
		,{id:"selectAll_transGroupMassApprovalTable"
		,caption:"Select All"
		,buttonicon:"ui-icon-check"
		,onClickButton:function(){ 
			var grid = $("#transGroupMassApprovalTable");
			var rows = grid.jqGrid("getDataIDs");
			for (i = 0; i < rows.length; i++)
		    {
				$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',1);
		    }
		}
		,position:"last"
		,title:"Select All"
		,cursor:"pointer"
		});
	$("#transGroupMassApprovalTable")
		.jqGrid({pager:'#transGroupMassApprovalTable_pager'})
		.jqGrid('navButtonAdd'
		,'#transGroupMassApprovalTable_pager'
		,{id:"deselectAll_transGroupMassApprovalTable"
		,caption:"Deselect All"
		,buttonicon:"ui-icon-minusthick"
		,onClickButton:function(){ 
			var grid = $("#transGroupMassApprovalTable");
		    var rows = grid.jqGrid("getDataIDs");
		    for (i = 0; i < rows.length; i++)
		    {
				$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',0);
		    }
		}
		,position:"last"
		,title:"Deselect All"
		,cursor:"pointer"
		});
		
	   	var grid = $("#transGroupMassApprovalTable");
		var rows = grid.jqGrid("getDataIDs");
		for (i = 0; i < rows.length; i++)
	    {
			$('#transGroupMassApprovalTable').jqGrid('setCell',rows[i],'approve',1);
	    }

});

/*ACTIONS*/
function submitSearch(){
	$.publish('reloadTransGroupMassApproval');
}

function resetSearch(){
	$('#bpe').val('');
	$('#opa').val('')
	$.publish('reloadTransGroupMassApproval');
}

function submitChanges(){
	var approved = false;
	var grid = $("#transGroupMassApprovalTable");
	var rows = grid.jqGrid("getDataIDs");
	var appLst = "";
	for (i = 0; i < rows.length; i++)
    {
        var app = grid.jqGrid ('getCell', rows[i], 'approve');
        var key = grid.jqGrid ('getCell', rows[i], 'gridKey');
        if(app == 1){
        	approved = true;
        	appLst = appLst + key +",";
        }
   
    }
    
    if(approved){
    	$('#appLst').val(appLst);
    	
		url = "alsAccount/transGroupMassApprovalGridEdit_execute.action";    
		$.ajax({
          type: "POST",
          url: url,
          dataType: "json",
          data: $('#gridFrm').serialize(),
          success: function(result){
              if(result.actionErrors){
              	$('#html').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
              }else{
				$.publish('reloadTransGroupMassApproval');
              }
         }
        });
    }else{
    	alert("Please Select At Least One Approval Check.");
    }
}
