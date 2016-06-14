/**
 * 
 * systemActivityAccountMaster.jsp Javascript
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
	
	window.onresize = widthFunction;  
	function widthFunction() {
        $.each($('.ui-jqgrid'), function(index, value) {
            newWidth = $(this).closest(".ui-jqgrid").parent().width(); //grabs the main grid div gets parent div width
            tmpId = $(this).prop('id').replace('gbox_',''); // remove the prefix so that have actual grid id
           
            $('#'+tmpId).jqGrid("setGridWidth", newWidth, true); // set grid to width now
        });
	};
	   
	   
	//Reset budget year select on page load   
	$(document).ready(function(){
   		document.getElementById("budgetYearSel").selectedIndex = "0";
   		setGridFrm();
	});
	
	//Handle Export CSV button for each tab
	function getGenRpt(id) {
 		if(id == "getSysActivityControlRpt"){
 			var postFilters = $("#sysActivityControlTable").jqGrid('getGridParam', 'postData').filters;
 			$('#frmRptType').val("sysActivityControl");
 		}else if(id == "getAccountMasterRpt"){
 			var postFilters = $("#accountMasterTable").jqGrid('getGridParam', 'postData').filters;
 			$('#frmRptType').val("accountMaster");
 		}else if(id == "getActivityAccountLinkageRpt"){
 			var postFilters = $("#activityAccountLinkTable").jqGrid('getGridParam', 'postData').filters;
 			$('#frmRptType').val("activityAccountLinkage");
 		}else if(id == "getAppendixMRpt"){
 			var postFilters = $("#appendixM2Table").jqGrid('getGridParam', 'postData').filters;
 			var sel_id = jQuery("#appendixM1Table").jqGrid('getGridParam', 'selrow');					    				  
	    	var satc = jQuery("#appendixM1Table").jqGrid('getCell', sel_id, 'asacSystemActivityTypeCd');
 			$('#frmRptType').val("appendixM");
 			$('#frmActTypCd').val(satc);
 		}
 		$('#frmRptBudgYear').val($('#budgetYearSel').val());
 		$('#frmFilters').val(postFilters);
 		
		$('#rptFrm').attr('action', 'genDocCreate');
		$('#rptFrm').submit();	 
    } 
    
	//Set edit dialog fields disable when editing for all tabs
    function setDisabled(id){
	   	//Activity Control Table
	    	$("input[name='idPk.asacBudgetYear']").prop({disabled:true});
		   	$("input[name='idPk.asacSystemActivityTypeCd']").prop({disabled:true});
		   	$("input[name='idPk.asacTxnCd']").prop({disabled:true});
	    //Account Master Table
	    	$("input[name='idPk.asacBudgetYear']").prop({disabled:true});
		   	$("input[name='idPk.aamAccount']").prop({disabled:true}); 
	    //Activity Account Linkage Table
	    	$("input[name='idPk.asacBudgetYear']").prop({disabled:true});
		   	$("input[name='idPk.asacSystemActivityTypeCd']").prop({disabled:true});
		   	$("input[name='idPk.asacTxnCd']").prop({disabled:true});
		   	//document.getElementById("idPk.aaalDrCrCd").disabled=true;
    }
    
    //Set edit dialog fields enabled when adding for all tabs
    function setEnabled(id){
	    //Activity Control Table
	    	$("input[name='idPk.asacBudgetYear']").prop({disabled:false});
		   	$("input[name='idPk.asacSystemActivityTypeCd']").prop({disabled:false});
		   	$("input[name='idPk.asacTxnCd']").prop({disabled:false}); 
	    //Account Master Table
	     	$("input[name='idPk.asacBudgetYear']").prop({disabled:false});
		   	$("input[name='idPk.aamAccount']").prop({disabled:false}); 
	    //Activity Account Linkage Table
	    	$("input[name='idPk.asacBudgetYear']").prop({disabled:false});
		   	$("input[name='idPk.asacSystemActivityTypeCd']").prop({disabled:false});
		   	$("input[name='idPk.asacTxnCd']").prop({disabled:false});
		   	//document.getElementById("idPk.aaalDrCrCd").disabled=false;
    }
	
    function setGridFrm(){
    	$('#frmBudgYear').val($('#budgetYearSel').val());
    	$('#tstFrmBudgYear').val($('#budgetYearSel').val())
    }
    
	function reloadGrids(event,data) {
		setGridFrm();
		$.publish('reloadGrids');
		$.publish('reloadLinkTab');  
	}
	
	$.subscribe('activityAccountLinkComplete', function(event,data) {
		   if ( $("#activityAccountLinkTable").length) {
	       		$("#activityAccountLinkTable").jqGrid('setColProp','aamAccount', { editoptions: { value: rtrnAccountList()}});
	       		$("#activityAccountLinkTable").jqGrid('setColProp','sysActTypeTransCd', { editoptions: { value: rtrnActTypeTransCodeList()}});
		   }   
		   
	});
	
	function rtrnAccountList() {
				var rslt = $("#frmAccountLst").val();
				return rslt;
	}
	
	function rtrnActTypeTransCodeList() {
		var rslt = $("#frmActTypeTranCdLst").val();
		return rslt;
	}
	
	//Set Sys Activity Control Code / Transaction Code tooltip
	$.subscribe('sysActivityControlComplete', function(event,data) {
		$('#sysActivityControlTable tr').each(function(i) {
		    var sysActivityTypeCode = $(this).children('td[aria-describedby="sysActivityControlTable_idPk.asacSystemActivityTypeCd"]');
		    var sysActivityTypeCodeDesc = $(this).children('td[aria-describedby="sysActivityControlTable_sysActivityTypeDesc"]');
		   	sysActivityTypeCode.attr("title",$('<div>').html(sysActivityTypeCodeDesc.text()).text());
		   	var sysTxnCode = $(this).children('td[aria-describedby="sysActivityControlTable_idPk.asacTxnCd"]');
		    var sysTxnCodeDesc = $(this).children('td[aria-describedby="sysActivityControlTable_sysTranCodeDesc"]');
		   	sysTxnCode.attr("title",$('<div>').html(sysTxnCodeDesc.text()).text());
	    });
    });
	
	$.subscribe('activityTypeCodesSelect', function(event,data) {
    	id = event.originalEvent.id;
    	$('#frmActivityType').val(id)
		$.publish('reloadTranCdsDiv');   
	});
	
	function activityControlChanged(response, postdata) {
		rtrnstate = true; 
	    rtrnMsg = ""; 
		$.publish('reloadLinkTab'); 
		errorHandler(response, postdata);
		return [rtrnstate,rtrnMsg]; 
	};
	
	function checkForm(postData){
    	rtrnstate = true; 
		rtrnMsg = ''; 
  		
		postData.budgYear = $('#budgetYearSel').val();
		
		if(postData.aamAccount == '002504' || postData.aamAccount == '002505'){
			if(postData.aaalReference == ''){
				rtrnstate = false;
				rtrnMsg = 'Account Codes 002504 and 002505 require a Open Item Key of Yes.'; 
			}
		}

		if(postData.aaalAccountingCdFlag == 'N'){
			if(postData.aamFund == ''){
				rtrnstate = false;
				rtrnMsg = 'A Fund must be specified when Details in Accounting Code Control Table is set to NO.'; 
			}
		}else{
			if(postData.aamFund != '' || postData.aocOrg != '' || postData.asacSubclass != '' ){
				rtrnstate = false;
				rtrnMsg = 'No Fund, Org, or Sub-Class may be specified when Details in Accounting Code Control Table is set to YES.'; 
			}
		}
        return [rtrnstate,rtrnMsg]; 
    } 