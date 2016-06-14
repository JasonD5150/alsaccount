/**
 * 
 * alsNonAlsTemplate.jsp Javascript
 * 
 * @author cfa027
 */
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

function setGridFrm() {
	$('#frmBudgYear').val($('#budgetYearSel').val());
	$('#drGridBudgYear').val($('#budgetYearSel').val());
	$('#crGridBudgYear').val($('#budgetYearSel').val());
}

function reloadGrids(event, data) {
	setGridFrm();
	// $('#alsNonAlsOrgControlDiv').toggle(false);
	$('#drGridTempCd').val("");
	$('#crGridTempCd').val("");
	$.publish('reloadGrids');
}

// Reset budget year select on page load
$(document).ready(function() {
	$('#drGridTempCd').val("");
	$('#crGridTempCd').val("");
	document.getElementById("budgetYearSel").selectedIndex = "0";

	$('#rptProviderNum').val("");
	$('#rptOrg').val("");
	$('#rptJLR').val("");
	$('#rptAccount').val("");
	var tempToReport = document.getElementsByName("tempToReport");
	tempToReport[0].checked = true;
	setGridFrm();
});

$.subscribe('templateSelected', function(event, data) {
	tmpid = event.originalEvent.id;
	tmp = $("#alsNonAlsTemplateTable").jqGrid('getCell', tmpid, 'idPk.anatCd');

	$('#drGridTempCd').val(tmp);
	$('#crGridTempCd').val(tmp);

	$.publish('reloadAlsNonAlsOrgcontrolDiv');
});

$.subscribe('alsNonOrgControlDrComplete', function(event, data) {
	if ($("#alsNonOrgControlDrTable").length) {
		$("#alsNonOrgControlDrTable").jqGrid('setColProp', 'anaocOrg', {
			editoptions : {
				value : rtrnOrgList()
			}
		});
		$("#alsNonOrgControlDrTable").jqGrid('setColProp', 'apiProviderNo', {
			editoptions : {
				value : rtrnProviderList()
			}
		});
	}
});

$.subscribe('alsNonOrgControlCrComplete', function(event, data) {
	if ($("#alsNonOrgControlCrTable").length) {
		$("#alsNonOrgControlCrTable").jqGrid('setColProp', 'anaocOrg', {
			editoptions : {
				value : rtrnOrgList()
			}
		});
		$("#alsNonOrgControlCrTable").jqGrid('setColProp', 'apiProviderNo', {
			editoptions : {
				value : rtrnProviderList()
			}
		});
	}
});

$.subscribe('setTemplateGridLists', function(event, data) {
	if ($("#alsNonAlsTemplateTable").length) {
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatDrAccount', {
			editoptions : {
				value : rtrnAccountList()
			}
		});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatCrAccount', {
			editoptions : {
				value : rtrnAccountList()
			}
		});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatFund', {
			editoptions : {
				value : rtrnFundList()
			}
		});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatDrSubclass', {
			editoptions : {
				value : rtrnSubClassList()
			}
		});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatCrSubclass', {
			editoptions : {
				value : rtrnSubClassList()
			}
		});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp',
				'anatDrJournalLineRefrDesc', {
					editoptions : {
						value : rtrnJLRList()
					}
				});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp',
				'anatCrJournalLineRefrDesc', {
					editoptions : {
						value : rtrnJLRList()
					}
				});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatDrProjectGrant',
				{
					editoptions : {
						value : rtrnProjectGrantList()
					}
				});
		$("#alsNonAlsTemplateTable").jqGrid('setColProp', 'anatCrProjectGrant',
				{
					editoptions : {
						value : rtrnProjectGrantList()
					}
				});
	}
});

function rtrnOrgList() {
	var rslt = $("#frmOrgLst").val();
	return rslt;
}
function rtrnProviderList() {
	var rslt = $("#frmProviderLst").val();
	return rslt;
}

function rtrnAccountList() {
	var rslt = $("#frmAccountLst").val();
	return rslt;
}

function rtrnFundList() {
	var rslt = $("#frmFundLst").val();
	return rslt;
}

function rtrnSubClassList() {
	var rslt = $("#frmSubClassLst").val();
	return rslt;
}

function rtrnJLRList() {
	var rslt = $("#frmJlrLst").val();
	return rslt;
}

function rtrnProjectGrantList() {
	var rslt = $("#frmProjectGrantLst").val();
	return rslt;
}

function openReportDialog() {
	$('#rptDialog').dialog('open');
}

function getGenRpt() {
	$('#rptDialog').dialog('close');
	$('#frmRptType').val("alsNonAlsTemplate");

	$('#rptFrm').attr('action', 'genDocCreate');
	$('#rptFrm').submit();
}

function copyTemplates(){
	if (confirm("Are you sure you want to copy all the templates to the next budget year") == true) {
    	$('#copyTemplateFrm').trigger('submit');
    } 
}