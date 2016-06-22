/**
 * 
 * fiscalYearEnd.jsp Javascript
 * 
 * @author cfa027
 */
/*Fiscal Year End Main Page*/
$(document).ready(function() {
	resetInputs();
	$('#updateAccCdFrom').datepicker();
	$('#updateAccCdTo').datepicker();
	$('#updateBudgetYear').val($('#curBudgetYear').val());
	$('#updateBudgetYearChangeDt').val($('#curBudgetYearChangeDt').val());
	$('#adjParamFrmYear').val($('#budgYear').val());
	

	//If steps have been completed disable step button
	if($('#copyAccTablesCompleted').val()=='true'){
		$('#copyAccTables').prop({
			disabled : true
		});
	}else{
		$('#copyAccTables').prop({
			disabled : false
		});
	}
	if($('#copyAlsNonAlsTemplatesCompleted').val()=='true'){
		$('#copyAlsNonAlsTemplates').prop({
			disabled : true
		});
	}else{
		$('#copyAlsNonAlsTemplates').prop({
			disabled : false
		});
	}
});

function resetInputs(){
	$('#updateAccCdFrom').val("");
	$('#updateAccCdTo').val("");
	$('#updateItemType').val("");
	$('#updateItemTypeDesc').val("");
	$('#updateBudgetYear').val("");
	$('#upf').val("");
	$('#upt').val("");
	
	$('#budgYear').val("");
	$('#billPeriodEnd').val("");
	$('#endFY').val("");
	$('#newFY').val("");
	$('#tranGrpNmFyeAdjust').val("");
	$('#tranGrpNmNewFY').val("");
}

$.subscribe('tabChanged', function(event,data) {
	$('#stepHtml').html('');
});

function fiscalYearEndAction(id) {
	$('#yearEndStep').val(id);
	$('#upfStep').val($('#updateAccCdFrom').val());
	$('#uptStep').val($('#updateAccCdTo').val());
	$('#itcStep').val($('#updateItemType').val());
	$('#budgYearChngDtStep').val($('#updateBudgetYearChangeDt').val());

	if (id == 'updateAccCd') {
		$('#budgYearStep').val($('#updateBudgetYear').val());
	} else {
		$('#budgYearStep').val($('#budgYear').val());
	}

	var post_string = $('#stepFrm').serialize();

	$('#stepStsHtml').html('');

	$.ajax({
		type : "POST",
		data : post_string,
		dataType : "json",
		cache : false,
		url : 'fiscalYearEndJson.action',
		success : function(data) {

			if (data.rtrn.procStatus === 'SUCCESS') {
				if (data.rtrn.fieldName == 'HTML') {
					$('#stepHtml').html(
							'<p style="color:blue;font-size:14px"><b>'
									+ data.rtrn.procMsg + '</b></p>');

					if(data.rtrn.procMsg.indexOf("Copy Accounting Tables") >= 0){
						$('#copyAccTables').prop({
							disabled : true
						});
					}else if(data.rtrn.procMsg.indexOf("Copy Als Non Als Templates") >= 0){
						$('#copyAlsNonAlsTemplates').prop({
							disabled : true
						});
					}
				} else if (data.rtrn.fieldName == 'REPORT') {
					$('#frmRptType').val('updateAccCd');
					$('#frmRptBody').val(data.rtrn.procMsg)

					$('#rptFrm').attr('action', 'genDocCreate');
					$('#rptFrm').attr('method', 'post');
					$('#rptFrm').submit();
				}
			} else {
				$('#stepHtml').html(
						'<p style="color:red;font-size:14px"><b>'
								+ data.rtrn.procMsg + '</b></p>');
			}

		},
		complete : function() {
			// Handle the complete event
		},
		error : function(x, e) {
			ajaxErrorHandler(x, e, "Fiscal Year End",
					"Unexpected Process Error", "actionStsHtml");
		}
	});
}

/*Fye Copy Accounting Table*/

/*Fye Update Usage Period*/
$.subscribe('itemTypeSelected', function(event,data) {
	var itemType = $('#itemTypeList').val();

	var select = document.getElementById('itemTypeList');
	var itemTypeDesc = select.options[select.selectedIndex].innerHTML.split(" - ")[1];

	$('#updateItemType').val(itemType);
	$('#updateItemTypeDesc').val(itemTypeDesc);
	
});

/*Fye Copy Templates*/

/*Fye Adjustment Parameters*/
datePick = function(element) {
	$(element).datepicker({
		dateFormat : 'mm/dd/yy',
		buttonImage : "calendar.gif",
		buttonImageOnly : true
	});
};

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

$.subscribe('adjParamFrmComplete', function(event, data) {
	// If it has already been processed this will not be null
	if ($('#tranGrpNmFyeAdjust').val() == "") {
		$('#createTransGroup').prop({
			disabled : false
		});
	} else {
		$('#alsSabhrsFyeAdjstDtlTable_pager_left').toggle(false);
	}

	$.publish('reloadAdjstDt');
});

function saveFyeAdjst() {
	if($('#budgYear').val().toString().length < 4){
		alert("Budget year should be at least four digits.");
	}else{
		$('#adjParamFrmYear').val($('#budgYear').val());
		$.publish('reloadAdjParam');
	}
}

function resetAdjstParamTab(){
	$('#budgYear').val("");
	$('#billPeriodEnd').val("");
	$('#endFY').val("");
	$('#newFY').val("");
	$('#tranGrpNmFyeAdjust').val("");
	$('#tranGrpNmNewFY').val("");
	$('#adjParamFrmYear').val($('#budgYear').val());
	$.publish('reloadAdjParam');
}

function stepTwoToggle(id){
	if(id = 'indvDiv'){
		$('#stepTwoAllDiv').toggle("false");
		$('#stepTwoIndvDiv').toggle("true");
	}else{
		$('#stepTwoIndvDiv').toggle("false");
		$('#stepTwoAllDiv').toggle("true");
	}
	
}


