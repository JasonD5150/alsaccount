<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="/css/jquery"/>
	   	<style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
        </style>
    </fwp:head>
    
	<script src='scripts/fieldEdits.js' type='text/javascript'></script>
    <script src='scripts/exportGrid.js' type='text/javascript'></script>
    <script src="scripts/jquery.are-you-sure.js" type="text/javascript"></script>
    <script src="scripts/jquery.inputmask.bundle.min.js" type="text/javascript"></script>
    <script src="scripts/fwp.alsFormWidget.js"></script>
	<script src="/alsaccount/refund/scripts/fwp.personRefundApp.js"></script>
	
	<s:hidden id="paymentStatusLst" name="paymentStatusLst"/>
	<s:hidden id="cancelActionLst" name="cancelActionLst"/>
	<s:hidden id="reconStatusLst" name="reconStatusLst"/>
	<s:hidden id="staledateStatusLst" name="staledateStatusLst"/>
	<s:hidden id="paymentMethodLst" name="paymentMethodLst"/>
	<s:hidden id="warrantTypeLst" name="warrantTypeLst"/>
    
	<div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
	<div id="personRefundApp">
	<div style="width:800px;text-align:center">
    	<h2 class="title">Person Refund Approval / Disapproval</h2>
   	</div>
	<s:include value="include/personRefundAppQueryDiv.jsp"/>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;" class="fwp-exclude-from-show-hide">Actions</legend>
    	<button type="button" id="searchButton">Search</button>
    	<button type="button" id="resetButton">Reset</button>
    	<button type="button" id="exportButton">Export CSV</button>
	</fieldset>
	<br>
	<div style="display:inline-block;" class="fwpPageStatusContainer"></div>
  	<br>
  	<br>
  	<s:url id="personRefundAppGridURL" action="alsAccount/personRefundAppGrid_buildgrid" />  
  	<s:url id="personRefundAppGridEditURL" action="alsAccount/personRefundAppGridEdit_execute" />    
	<sjg:grid
		id="personRefundGrid"
		caption="Person Refund"
		href="%{personRefundAppGridURL}"
		editurl="%{personRefundAppGridEditURL}"			
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorRefresh="false"
		navigatorViewOptions="{width:750}"
		navigatorEditOptions="{width:500,reloadAfterSubmit:true,
		    	                       editCaption:'Edit Refund Info',
		    	                       beforeShowForm:function(){
			    	                        var grid = $('#personRefundGrid');
			    	                        var sel_id = grid.jqGrid('getGridParam','selrow');
			    	                        if(grid.jqGrid('getCell', sel_id, 'downloadDate').length != 1){
			    	                        	$('#ariRefundApproved').prop('disabled', true );
			    	                        	$('#ariDrawingFeeApproved').prop('disabled', true );
			    	                        	$('#ariPreferenceFeeApproved').prop('disabled', true );
			    	                        }else{
			    	                        	$('#ariRefundApproved').prop('disabled', false );
			    	                        	$('#ariDrawingFeeApproved').prop('disabled', false );
			    	                        	$('#ariPreferenceFeeApproved').prop('disabled', false );
			    	                        }
			    	                        return[true, ''];
		    	                       },	
		    	                       beforeSubmit:function(){
		    	                       		var grid = $('#personRefundGrid');
		    	                       		grid.jqGrid('setGridParam',{datatype:'json'});
		    	                       		return[true, ''];
		    	                       },    
		    	                       closeAfterEdit:true,
		    	                       afterSubmit:errorHandler,
		    	                       processData:'Updating to Database'}"  
	    navigatorExtraButtons="{    
	    						columnsbutton : { title : 'Add/Remove Columns', icon: ' ui-icon-extlink', topic: 'personRefundAddRemoveColumns'}
	                        }"	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="false"
		scrollrows="false"
		height="200"
		width="950"
		rowNum="25"
		formIds="gridFrm"
		reloadTopics="reloadPersonRefundGrid"
		onCompleteTopics="personRefundGridComplete"
		onSelectRowTopics="personRefundRowSelected"
		loadonce="true"
		autowidth="true"
	    shrinkToFit="false">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn id="designatedRefundeeDob" name="designatedRefundeeDob" title ="Date of Birth" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="ariDesignatedRefundeeAlsNo" name="ariDesignatedRefundeeAlsNo" title ="ALS No" width="25" align="right"/>
			<sjg:gridColumn id="refundeeName" name="refundeeName" title ="Person Name" width="150" />
			<sjg:gridColumn id="usagePeriodFrom" name="usagePeriodFrom" title ="Usage Period From" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="usagePeriodTo" name="usagePeriodTo" title ="Usage Period To" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="itemTypeCd" name="itemTypeCd" title ="Item Type Code" width="50" align="right"/>
			<sjg:gridColumn id="itemTypeDesc" name="itemTypeDesc" title ="Item Type Desc" width="175" />
			<sjg:gridColumn id="itemIndCd" name="itemIndCd" title ="Item Indicator" width="150" hidden="true"/>
			<sjg:gridColumn id="itemIndDesc" name="itemIndDesc" title ="Item Indicator" width="150" />
			<sjg:gridColumn id="reasonDesc" name="reasonDesc" title ="Refund Reason" width="175" />
			<sjg:gridColumn id="refundRequestDt" name="refundRequestDt" title ="Refund Request Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="refundProcessDt" name="refundProcessDt" title ="Refund Processing Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>	
			<sjg:gridColumn id="ariRefundAmt" name="ariRefundAmt" title ="Item Fee" width="75" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>	
			<sjg:gridColumn id="ariRefundApproved" name="ariRefundApproved" title ="Item Fee Refund Approved" width="35" editable="true"
							edittype="select" formatter="select" editoptions="{value:':;Y:Yes;N:No'}"/>
			<sjg:gridColumn id="ariDrawingFee" name="ariDrawingFee" title ="Application Fee" width="75" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>	
			<sjg:gridColumn id="ariDrawingFeeApproved" name="ariDrawingFeeApproved" title ="Application Fee Refund Approved" width="35" editable="true"
							edittype="select" formatter="select" editoptions="{value:':;Y:Yes;N:No'}"/>
			<sjg:gridColumn id="ariPreferenceFee" name="ariPreferenceFee" title ="Preference Fee" width="75" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>	
			<sjg:gridColumn id="ariPreferenceFeeApproved" name="ariPreferenceFeeApproved" title ="Preference Fee Refund Approved" width="35" editable="true"
							edittype="select" formatter="select" editoptions="{value:':;Y:Yes;N:No'}"/>
			<sjg:gridColumn id="downloadDate" name="downloadDate" title ="ALS created Warrant file on" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="batchNo" name="batchNo" title ="Batch No" width="60" align="right"/>
			<sjg:gridColumn id="subBatchNo" name="subBatchNo" title ="Sub Batch No" width="50" align="right"/>
			<sjg:gridColumn id="appType" name="appType" title ="Application Type" width="150" />
			<sjg:gridColumn id="ariReasonDisapproval" name="ariReasonDisapproval" title ="Reason for Disapproval" width="150" />
			<sjg:gridColumn id="ariRemarks" name="ariRemarks" title ="Remarks" width="150" editable="true"/>
	</sjg:grid>
	<br>
	<form id="warrentStatusFrm">
		<s:hidden id="downloadDt" name="downloadDt"/>
		<s:hidden id="apiDob" name="apiDob"/>
		<s:hidden id="apiAlsNo" name="apiAlsNo"/>
	</form>
	<s:url id="warrantStatusGridURL" action="alsAccount/warrantStatusGrid_buildgrid" />  
	<sjg:grid
		id="warrantStatusGrid"
		caption="Warrant Status"
		href="%{warrantStatusGridURL}"	
		dataType="local"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorRefresh="false"
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		rowNum="1000"
		formIds="warrentStatusFrm"
		reloadTopics="reloadWarrantStatusGrid"
		onCompleteTopics="warrantStatusGridComplete"
		loadonce="true"
		autowidth="true"
	    shrinkToFit="false">
			<sjg:gridColumn name="awsId" title ="id" width="55" hidden="true" key="true"/>	
			<sjg:gridColumn id="awsWarrantType" name="awsWarrantType" title ="Warrant Type" width="125" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsPaidAmount" name="awsPaidAmount" title ="Paid Amount" width="50" formatter="currency" formatoptions="{decimalPlaces: 2}" align="right"/>
			<sjg:gridColumn id="awsPaymentStatus" name="awsPaymentStatus" title ="Payment Status" width="100" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsCancelAction" name="awsCancelAction" title ="Cancel Action" width="100" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsPaymentMethod" name="awsPaymentMethod" title ="Payment Method" width="100" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsReconStatus" name="awsReconStatus" title ="Recon Status" width="100" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsVendorId" name="awsVendorId" title ="Vendor Id" width="75" align="right"/>
			<sjg:gridColumn id="awsStaledateStatus" name="awsStaledateStatus" title ="Staledate Status" width="100" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn id="awsProcessDate" name="awsProcessDate" title ="Process Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="awsRsamCode" name="awsRsamCode" title ="Warrant No.?" width="100" />
			<sjg:gridColumn id="awsVoucherId" name="awsVoucherId" title ="Voucher Id" width="75" align="right"/>
			<sjg:gridColumn id="awsPaymentDate" name="awsPaymentDate" title ="Warrant Create Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="awsApBusinessUnit" name="awsApBusinessUnit" title ="Ap Business Unit" width="50" />
			<sjg:gridColumn id="" name="" title ="Date Cashed" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="awsPaymentGrossAmount" name="awsPaymentGrossAmount" title ="Payment Gross Amount" width="50" formatter="currency" formatoptions="{decimalPlaces: 2}" align="right"/>
			<sjg:gridColumn id="awsStaledateDate" name="awsStaledateDate" title ="Staledate Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" />
			<sjg:gridColumn id="awsPreviousPaymentId" name="awsPreviousPaymentId" title ="Previous Payment Id" width="150" />
			<sjg:gridColumn id="awsStaledateActDate" name="awsStaledateActDate" title ="Staledate Act Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="awsProcessFromDate" name="awsProcessFromDate" title ="Process From Date" width="70" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn id="awsComment" name="awsComment" title ="Comment" width="150" />
	</sjg:grid>
  	</div>
	<script type="text/javascript">
		$(function () {
		    $("div#personRefundApp").personRefundApp();
		});
	</script>
</fwp:template>
