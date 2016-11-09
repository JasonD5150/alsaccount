<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<form id='bankCdLstFrm'>
	<s:hidden id="bankCdLst" name="bankCdLst"/>		
</form>

<s:url id="internalProviderBankCdDepLinkGridURL" action="alsAccount/internalProviderBankCdDepLinkGrid_buildgrid" />
<s:url id="internalProviderBankCdDepLinkEditGridURL" action="alsAccount/internalProviderBankCdDepLinkGridEdit_execute" />  
<sjg:grid
	id="depositsGrid"
	caption="Bank Details"
	href="%{internalProviderBankCdDepLinkGridURL}"	
	editurl="%{internalProviderBankCdDepLinkEditGridURL}"			
	dataType="json"
	pager="true"
	navigator="true"
	navigatorEdit="true"
	navigatorView="true"
	navigatorAdd="true"
	navigatorDelete="true"
	navigatorSearch="false"
	navigatorRefresh="false"
   	navigatorAddOptions="{width:600,reloadAfterSubmit:false,
   						  addedrow:'last',
   						  beforeSubmit:function(postData){
	    	                      	postData.provNo = $('#frmProvNo').val();
	    	                      	postData.billingFrom = $('#frmBPFrom').val();
	    	                      	postData.apbdBillingTo = $('#frmBPTo').val();
	    	                      	return[true, ''];
	    	              },
   						  afterSubmit:errorHandler,
   						  afterSubmit: function () {
							    $('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
							    $.publish('reloadInternalRemittance');
							    return [true];
						  },
   						  afterShowForm:prePopulate(this.id),   
   	                      addCaption:'Add New Code Info',
   	                      closeAfterAdd:true,
   	                      processData:'Adding Row to Database'}"
   	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
   	                       editCaption:'Edit Code Info',
   	                       afterShowForm:setEnabledFields,
   	                       beforeSubmit:function(postData){
	    	                      	postData.provNo = $('#frmProvNo').val();
	    	                      	postData.billingFrom = $('#frmBPFrom').val();
	    	                      	postData.apbdBillingTo = $('#frmBPTo').val();
	    	                      	return[true, ''];
	    	               },
   	                       closeAfterEdit:true,
   	                       afterSubmit:errorHandler,
   	                       afterSubmit: function () {
							    $('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
							    $.publish('reloadInternalRemittance');
							    return [true];
						   },
   	                       processData:'Updating to Database'}"
   	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
   	navigatorDeleteOptions="{afterSubmit:errorHandler,
   							 afterSubmit: function () {
							    $('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
							    $.publish('reloadInternalRemittance');
							    return [true];
						  	}}"   	
   	gridModel="model"
   	formIds="subGridFrm"
	rownumbers="false"
	editinline="false"
	viewrecords="true"
	scroll="true"
	scrollrows="true"
	height="150"
	width="910"
	rowNum="1000"
	onBeforeTopics="setBankCdList"
	onSelectRowTopics="intProvBankCdDepLinkSelected"
	onCompleteTopics="depositsGridComplete"
	reloadTopics="reloadDepositsGrid"
	loadonce="true">
		<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
		<sjg:gridColumn name="genTDT" index="genTDT" title ="Generate Treasury Deposit Ticket" width="5" sortable="true" align="center" editable = "false" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
		
		<sjg:gridColumn name="idPk.apbdSeqNo" index="apbdSeqNo" title="Seq No" width="5" sortable="true" align="right" editable="false"/>
		<sjg:gridColumn name="apbdCashInd" index="apbdCashInd" title="Cash" width="10" sortable="true" align="center" editable="true" edittype="checkbox" formatter="checkbox" editoptions="{value:'Y:N'}"/>
		<sjg:gridColumn name="abcBankCd" index="abcBankCd" title="Bank Code" width="10" sortable="true" align="right" editable="true" edittype="select" editoptions="{value:','}" editrules="{required:true}"/>
		<sjg:gridColumn name="bankName" index="bankName" title="Bank Name" width="20" sortable="true" editable="false"/>
		<sjg:gridColumn name="apbdAmountDeposit" index="apbdAmountDeposit" title="Amount Deposited" width="10" sortable="true" align="right" editable = "true" formatter= "number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}"/>
		<sjg:gridColumn name="depositDate" index="depositDate" title="Deposit Date" width="10" sortable="true" editable = "true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" editrules="{required:true}" editoptions="{size:12, maxlength: 19, dataInit: function(elem){$(elem).datepicker({dateFormat:'mm/dd/yy'});}}"/>
		<sjg:gridColumn name="billingFrom" index="billingFrom" title="Billing Period From Date" width="10" sortable="true" editable = "false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" editrules="{required:true}" editoptions="{size:12, maxlength: 19, dataInit: function(elem){$(elem).datepicker({dateFormat:'mm/dd/yy'});}}"/>
		<sjg:gridColumn name="apbdBillingTo" index="apbdBillingTo" title="Billing Period To Date" width="10" sortable="true" editable = "false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" editrules="{required:true}" editoptions="{size:12, maxlength: 19, dataInit: function(elem){$(elem).datepicker({dateFormat:'mm/dd/yy'});}}"/>
		<sjg:gridColumn name="deadlineDate" index="deadlineDate" title="Billing Period End Date" width="10" sortable="true" editable="true" hidden="true" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
		<sjg:gridColumn name="amtDue" index="amtDue" title="Amount Due" width="10" sortable="true" align="right" editable="false" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
		<sjg:gridColumn name="apbdDepositId" index="apbdDepositId" title="Deposit Id" width="10" sortable="true" editable="false"/>
		<sjg:gridColumn name="depositSlip" title ="Deposit Slip" width="10" hidden="false" editable="false" align="center" formatter="slipDlg"/>
		<sjg:gridColumn name="hasDepositSlip" index="hasDepositSlip" title ="Has Deposit Slip" width="10" hidden="true" editable="false"/>
</sjg:grid>

