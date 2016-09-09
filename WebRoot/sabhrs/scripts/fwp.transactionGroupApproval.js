/**
 * 
 * transactionGroupApproval.jsp Javascript
 * 
 * @author cfa027
 */
$(document).ready(function(){
	menuSec("transGrpApp");
	$('#txGrpType').val("");
	$('#provNo').val("");
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

function submitSearch(){
	$('#transGroupAppDiv').toggle(false);
	$('#transGroupApprovalTable').jqGrid('setGridParam',{datatype:'json'});
	$.publish('reloadTransGroupMaintTable')
	
}
function resetSearch(){
	$('#gridFrm')[0].reset();
}

function enableDisableElements() {
	if(($('#transGroupType').val() == 1 || $('#transGroupType').val() == 3) && ($('#bankRefNo').val() != null && $('#bankRefNo').val() != "")){
		$('#getRpt').prop({disabled:false});
	}else{
		$('#getRpt').prop({disabled:true});
	}
	
	$('#transGroupType').prop({disabled:true});
	$('#desc').prop({disabled:true});
	$('#transGroupIdentifier').prop({disabled:true});
	$('#transGroupCreateDt').prop({disabled:true});
	$('#sumAppStat').prop({disabled:true});
	$('#sumAll').prop({disabled:true});
	$('#sumAppBy').prop({disabled:true});
	$('#sumAppDt').prop({disabled:true});
	$('#intAppStat').prop({disabled:true});
	$('#intAll').prop({disabled:true});
	$('#intAppBy').prop({disabled:true});
	$('#intAppDt').prop({disabled:true});
	$('#upToSumDt').prop({disabled:true});
	$('#upToSabhrsDt').prop({disabled:true});
	$('#bankCd').prop({disabled:true});
	$('#bankRefNo').prop({disabled:true});
	$('#intFile').prop({disabled:true});
	$('#intFileCreateDt').prop({disabled:true});
	$('#intFileName').prop({disabled:true});
	$('#nonAlsEnt').prop({disabled:true});
	$('#netCashDrCr').prop({disabled:true});
	$('#depId').prop({disabled:true}); 
	
	
	$('#programYear').prop({disabled:true});
	$('#accountDt').prop({disabled:true});
	$('#budgYear').prop({disabled:true});
	var nums;				
	if($('#sumAppStat').val() == 'N'){
		$('#changeProgramYear').prop({disabled:true});
		$('#changeAccountDt').prop({disabled:true});
		$('#sumAppStat').prop({disabled:true});
		$('#sumAll').prop({disabled:true});
		$('#intAppStat').prop({disabled:true});
		$('#intAll').prop({disabled:true});
		$('#upToSabhrsDt').prop({disabled:true});
		$('#depId').prop({disabled:true});
		$('#bankRefNo').prop({disabled:true});
	}else{
		if($.trim($("#intFileCreateDt").val())==''){
			nums = [2,4,6,7];
			if(nums.indexOf($('#transGroupType').val()) != -1){
				$('#changeProgramYear').prop({disabled:true});
				$('#changeAccountDt').prop({disabled:true});
			}else{
				$('#changeProgramYear').prop({disabled:false});
				$('#changeAccountDt').prop({disabled:false});
			}
			
			$('#sumAppStat').prop({disabled:false});
			if($('#transGroupType').val() == 8){
				if(document.getElementById('sumAll').disabled){
					$('#sumAll').prop({disabled:false});
				}
				if(document.getElementById('intAll').disabled){
					$('#intAll').prop({disabled:false});	
				}
			}else{
				$('#sumAll').prop({disabled:true});
			}
			if($('#transGroupType').val() == 1 || $('#transGroupType').val() == 3){
				$('#bankRefNo').prop({disabled:false});
			}else{
				$('#bankRefNo').prop({disabled:true});
			}
			if($('#sumAppStat').val() == 'A'){
				if($('#transGroupType').val() == 2 || $('#transGroupType').val() == 6){
					$('#intAppStat').prop({disabled:true});
				}else if($('#transGroupType').val() == 8){
					$('#intAppStat').prop({disabled:false});
					if(document.getElementById('sumAll').disabled){
						$('#sumAll').prop({disabled:false});
					}
				}else{
					$('#intAppStat').prop({disabled:false});
				}
			}else{
				$('#intAppStat').prop({disabled:true});
			}
			$('#depId').prop({disabled:true});
			$('#upToSabhrsDt').prop({disabled:true});
		}else{
			$('#changeProgramYear').prop({disabled:true});
			$('#changeAccountDt').prop({disabled:true});
			$('#sumAppStat').prop({disabled:true});
			$('#sumAll').prop({disabled:true});
			$('#intAppStat').prop({disabled:true});
			$('#intAll').prop({disabled:true});
			$('#upToSabhrsDt').prop({disabled:false});
			$('#bankRefNo').prop({disabled:true});
			if($('#transGroupType').val() == 1 || $('#transGroupType').val() == 3 ){
				$('#depId').prop({disabled:false});
			}else{
				$('#depId').prop({disabled:true});
			}
		}
	}
}

$.subscribe('transGroupSelected', function(event, data) {
		$('#rtnAction').html('');
		var sel_id = $("#transGroupApprovalTable").jqGrid('getGridParam', 'selrow');
							    				  
		$('#transGroupType').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'idPk.atgTransactionCd'));
		$('#desc').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'desc'));
		$('#transGroupIdentifier').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'idPk.atgsGroupIdentifier'));
		$('#transGroupCreateDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsWhenCreated'));
		$('#programYear').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'programYear'));
		$('#accountDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsAccountingDt'));
		$('#budgYear').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'budgYear'));
		$('#sumAppStat').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsSummaryStatus'));
		$('#sumAppBy').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsSummaryApprovedBy'));
		$('#sumAppDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsSummaryDt'));
		$('#intAppStat').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsInterfaceStatus'));
		$('#intAppBy').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsInterfaceApprovedBy'));
		$('#intAppDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsInterfaceDt'));
		$('#upToSumDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsWhenUploadToSummary'));
		$('#bankCd').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'abcBankCd'));
		$('#bankRefNo').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsBankReferenceNo'));
		$('#intFile').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsArGlFlag'));
		$('#intFileCreateDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsFileCreationDt'));
		$('#intFileName').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsFileName'));
		$('#nonAlsEnt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsNonAlsFlag'));
		$('#netCashDrCr').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsNetDrCr'));
		$('#depId').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsDepositId'));
		$('#remarks').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsRemarks'));
		$('#upToSabhrsDt').val($("#transGroupApprovalTable").jqGrid('getCell', sel_id, 'atgsWhenUploadedToSabhrs'));
		
		enableDisableElements();
		$('#transGroupAppDiv').toggle(true);
});



function submitTransGroupApproval(){
	var row = $('#idPk.atgTransactionCd').val+"_"+$('#idPk.atgsGroupIdentifier').val;
	url = "alsAccount/transGroupApprovalGridEdit_execute.action";    
	$.ajax({
      type: "POST",
      url: url,
      dataType: "json",
      data: {oper:"edit",
    	     transGroupType:$('#transGroupType').val(),
    	  	 desc:$('#desc').val(),
    	  	 transGroupIdentifier:$('#transGroupIdentifier').val(),
    	  	 transGroupCreateDt:$('#transGroupCreateDt').val(),
    	  	 programYear:$('#programYear').val(),
    	  	 accountDt:$('#accountDt').val(),
    	  	 budgYear:$('#budgYear').val(),
    	  	 sumAppStat:$('#sumAppStat').val(),
    	  	 sumAppBy:$('#sumAppBy').val(),
    	  	 sumAppDt:$('#sumAppDt').val(),
    	  	 intAppStat:$('#intAppStat').val(),
    	  	 intAppBy:$('#intAppBy').val(),
    	  	 intAppDt:$('#intAppDt').val(),
    	  	 upToSumDt:$('#upToSumDt').val(),
    	  	 bankCd:$('#bankCd').val(),
    	  	 bankRefNo:$('#bankRefNo').val(),
    	  	 intFile:$('#intFile').val(),
    	  	 intFileCreateDt:$('#intFileCreateDt').val(),
    	  	 intFileName:$('#intFileName').val(),
    	  	 nonAlsEnt:$('#nonAlsEnt').val(),
    	  	 netCashDrCr:$('#netCashDrCr').val(),
    	  	 depId:$('#depId').val(),
    	  	 remarks:$('#remarks').val(),
    	  	 upToSabhrsDt:$('#upToSabhrsDt').val(),
    	  	 sumAll:$('#sumAll').val(),
    	  	 intAll:$('#intAll').val(),
    	  	 changeProgramYear:$('#changeProgramYear').val(),
  			 changeAccountDt:$('#changeAccountDt').val()},
      success: function(result){
          if(result.actionErrors){
        	  $('#rtnAction').html(
						'<p style="color:red;font-size:14px"><b>'
								+ result.actionErrors+ '</b></p>');
          }else{
        	  $('#rtnAction').html(
						'<p style="color:blue;font-size:14px"><b>Transaction Group succesfully updated.</b></p>');
        	  $("#transGroupApprovalTable").trigger("reloadGrid");
        	  $("#transGroupApprovalTable").jqGrid('setSelection', row);
          }
     }
    });
}

function getGenRpt() {
	$('#txIdentifier').val($('#transGroupIdentifier').val());
	$('#transCd').val($('#transGroupType').val());
	$('#pdfFrm').submit();
}

function sumAppStatSelected(){
	var d = new Date();
	var date = ("0"+(d.getMonth()+1)).slice(-2)+"/"+("0"+d.getDate()).slice(-2)+"/"+d.getFullYear()+" "+("0"+d.getHours()).slice(-2)+":"+("0"+d.getMinutes()).slice(-2)+":"+("0"+d.getSeconds()).slice(-2);
	if($('#sumAppStat').val() == 'A'){
		$('#sumAppBy').val($('#user').val());
		$('#sumAppDt').val(date);
	}else{
		$('#sumAppBy').val('');
		$('#sumAppDt').val('');
	}
}
function intStatSelected(){
	var d = new Date();
	var date = ("0"+(d.getMonth()+1)).slice(-2)+"/"+("0"+d.getDate()).slice(-2)+"/"+d.getFullYear()+" "+("0"+d.getHours()).slice(-2)+":"+("0"+d.getMinutes()).slice(-2)+":"+("0"+d.getSeconds()).slice(-2);
	if($('#intAppStat').val() == 'A'){
		$('#intAppBy').val($('#user').val());
		$('#intAppDt').val(date);
	}else{
		$('#intAppBy').val('');
		$('#intAppDt').val('');
	}
}
function transGrpTypeChanged(selected){
	var reload = false
	if(selected.value == " "){
		$('#txGrpType').val("");
		if($('#provNo').val() != null){reload = true;}
	}else{
		$('#txGrpType').val(selected.value);
		reload = true;
	}
	if(reload){
		$.publish('reloadTransGrpIdLst');
		$.publish('reloadProvLst');
	}	
}
function provNoChanged(selected){
	var reload = false
	if(selected.value == " "){
		$('#provNo').val("");
		if($('#txGrpType').val() != null){reload = true;}
	}else{
		$('#provNo').val(selected.value);
		reload = true;
	}
	if(reload){
		$.publish('reloadTransGrpIdLst');
	}	
}
$.subscribe("transGroupComplete", function (event, data) {
	var error = $("#transGroupApprovalTable").jqGrid('getGridParam', 'userData');
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