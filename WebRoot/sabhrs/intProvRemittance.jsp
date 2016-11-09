<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
<fwp:head>
    <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
    <script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script> 
    <script type="text/javascript" src= "/alsaccount/scripts/exportGrid.js"></script> 
    <script type="text/javascript" src= "/alsaccount/scripts/menuSecurity.js"></script> 
    <script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.intProvRemittance.js"></script>   
	<style type="text/css">
		@import url("/alsaccount/css/alsaccount.css");
		#appCom{
			width:100%;
		}
		
		/* Fixes problems with dragging dialog */
	    body{
	        position: relative; overflow: auto;
	    }
    </style>
    <script>

    </script>
</fwp:head>
<s:hidden id="hasUserRole" value="%{hasUserRole}"></s:hidden>
<s:hidden id="hasIntProvRole" value="%{hasIntProvRole}"></s:hidden>
<s:hidden id="curBudgYear" name="curBudgYear"/>
<s:hidden id="fundLst" name="fundLst"/>
<s:hidden id="subClassLst" name="subClassLst"/>
<s:hidden id="jlrLst" name="jlrLst"/>
<s:hidden id="projectGrantLst" name="projectGrantLst"/>
<s:hidden id="orgLst" name="orgLst"/>
<s:hidden id="accountLst" name="accountLst"/>
<s:hidden id="user" name="user"/>

    <div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>    
    <s:form id="pdfFrm" action="getTdtPdf">
    	<s:hidden id="type" name="type" value="M" />
      	<s:hidden id="depositIds" name="depositIds" />
    </s:form>
    
    <div style="width:800px;text-align:center">
    	<h2 class="title">Internal Provider Remittance</h2>
   	</div>
   	
   	<s:url id="searchCriteriaDivUrl" value="intProvRemittanceQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{searchCriteriaDivUrl}"
			width="950">
	</sj:div>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;">Actions</legend>
		<s:submit id="submitSearch" onclick="submitSearch()" value="Submit" theme="simple"></s:submit>
		<s:submit id="resetSearch" onclick="resetSearch()" value="Reset" theme="simple"></s:submit>
		<s:submit id="gridToCSVRpt" onclick="gridToCSV()" theme="simple" value="Export CSV"></s:submit>
		<s:submit id="intRemittanceCSVRpt" onclick="intRemittanceRptCSV()" theme="simple" value="Internal Remittance Report"></s:submit>
	</fieldset>
	<br>
	<br>
	<s:url id="alsInternalRemittanceGridURL" action="alsAccount/alsInternalRemittanceGrid_buildgrid" />
	<s:url id="alsInternalRemittanceGridEditURL" action="alsAccount/alsInternalRemittanceGridEdit_execute" />    
	<sjg:grid
		id="alsInternalRemittance"
		caption="Internal Remittance"
		href="%{alsInternalRemittanceGridURL}"
		editurl="%{alsInternalRemittanceGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorRefresh="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorAddOptions="{width:500,reloadAfterSubmit:true,
	    					  addedrow:'last',
	                          addCaption:'Add New Code Info',
	                          afterSubmit:errorHandler,
	                          closeAfterAdd:true,
	                          processData:'Adding Row to Database'}"
	    navigatorEditOptions="{width:500,reloadAfterSubmit:false,
	                           editCaption:'Edit Code Info',
	                           afterSubmit:errorHandler,    
	                           closeAfterEdit:true,
	                           processData:'Updating to Database'}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    navigatorDeleteOptions="{afterSubmit:errorHandler}"
	    gridModel="model"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="200"
		width="950"
		rowNum="10000"
		formIds="gridFrm"
		reloadTopics="reloadInternalRemittance"
		onSelectRowTopics="internalRemittanceSelected"
		onCompleteTopics="internalRemittanceComplete"
		loadonce="true">
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.apiProviderNo" index="idPk.apiProviderNo" title="Provider No" width="60" sortable="true" editable="false" align="right"/>
			<sjg:gridColumn name="provNm" index="provNm" title="Provider Name" width="100" sortable="true" editable="false" align="right"/>
			<sjg:gridColumn name="idPk.airBillingFrom" index="airBillingFrom" title="Billing Period From" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="idPk.airBillingTo" index="airBillingTo" title="Billing Period To" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="completeProvider" index="completeProvider" title="Date Completed by Provider" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airOfflnPaymentApproved" index="airOfflnPaymentApproved" title="Remittance Approved" width="15" sortable="true" editable="true" formatter="checkbox" align="center" edittype="checkbox" editoptions="{ value: 'true:false' }"/>
			
			<!-- HIDDEN -->
			<sjg:gridColumn name="amtRec" index="amtRec" title="Amount Received" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airSystemSales" index="airSystemSales" title="System Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="eftddd" index="eftddd" title="EFT DDD" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="airOtcPhoneSales" index="airOtcPhoneSales" title="OTC SALES" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airPae" index="airPae" title="PAE's" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airNonAlsSales" index="airNonAlsSales" title="Non ALS Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airTotSales" index="airTotSales" title="Total Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="amtDue" index="amtDue" title="ALS Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airCreditSales" index="airCreditSales" title="Credit Card Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}" hidden="true"/>
			<sjg:gridColumn name="totFundsRec" index="totFundsRec" title="Total Funds Received" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="totBankDep" index="totBankDep" title="Total Bank Deposits" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="billingBallanced" index="billingBallanced" title="Billing Period had been balanced by Internal Provider" editable="false" width="25" sortable="true" edittype="select" formatter="select" editoptions="{value: {Y: 'YES', N: 'NO'}}" hidden="true"/>
			<sjg:gridColumn name="airDifference" index="airDifference" title="Difference" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airShortSales" index="airShortSales" title="Total Short of Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airOverSales" index="airOverSales" title="Total Over Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="netOverShortOfSales" index="netOverShortOfSales" title="Net Over/Short of Sales" width="60" sortable="false" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentAppBy" index="airOfflnPaymentAppBy" title="Approved By" width="60" sortable="true" editable="false" hidden="true"/>
			<sjg:gridColumn name="offlnPaymentAppDt" index="offlnPaymentAppDt" title="Date" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentAppCom" index="airOfflnPaymentAppCom" title="Comments" width="100" sortable="true" editable="true" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentReviewed" index="airOfflnPaymentReviewed" title="Remittance Reviewed" width="15" sortable="true" editable="true" hidden="true"/>
			<sjg:gridColumn name="intFileCreateDt" index="intFileCreateDt" title="SABHRS Interface File Created" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="intFileGenerated" index="intFileGenerated" title="" hidden="true" editable="false"/>
			<sjg:gridColumn name="bankDepEditOnly" index="bankDepEditOnly" title="" hidden="true" editable="false"/>
	</sjg:grid>
	<br>
	<div id="displayRemittanceDiv" style="display:none;">
		<fieldset style="border: black 1px solid; display: inline-block;">
	    	<legend style="font-weight: bold;font-size:larger">Remittance</legend>
	    	<div id ='remErrorDiv'>
				<span id='remError'></span>
			</div>
			<table>
			<tr>
				<td class="label">Provider No: </td>
				<td><s:textfield id="displayProvNo" name="displayProvNo" theme="simple" title="Provider No" disabled="true"/></td>
				<td class="label">Provider Name: </td>
				<td><s:textfield id="displayProvNm" name="displayProvNm" theme="simple" title="Provider Name" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Billing Period From: </td>
				<td><s:textfield id="displayBpFrom" name="displayBpFrom" theme="simple" title="Billing Period From" disabled="true"/></td>
				<td class="label">Billing Period To: </td>
				<td><s:textfield id="displayBpTo" name="displayBpTo" theme="simple" title="Billing Period To" disabled="true"/></td>
				<%-- <td class="label">EFT DDD: </td>
				<td><s:textfield id="displayEftDdd" name="displayEftDdd" theme="simple" title="EFT DDD" disabled="true"/></td> --%>
				
			</tr>
			<tr>
				<td class="label">System Sales: </td>
				<td><s:textfield id="displaySysSales" name="displaySysSales" theme="simple" title="System Sales" disabled="true"/></td>
				<%-- Related to external providers <td class="label">Amount Received: </td>
				<td><s:textfield id="displayAmtRec" name="displayAmtRec" theme="simple" title="Amount Recieved" disabled="true"/></td> --%>
				
			</tr>
			<tr>
				<td class="label">OTC Sales: </td>
				<td><s:textfield id="displayOtcSales" name="displayOtcSales" theme="simple" title="OTC Sales" disabled="true"/></td>
				<td class="label">PAEs: </td>
				<td><s:textfield id="displayPaes" name="displayPaes" theme="simple" title="PAEs" disabled="true"/></td>
				<td class="label">Difference: </td>
				<td><s:textfield id="displayDif" name="displayDif" theme="simple" title="Difference" disabled="true"/></td>
				
			</tr>
			<tr>
				<td class="label">ALS Sales: </td>
				<td><s:textfield id="displayTotAlsSales" name="displayTotAlsSales" theme="simple" title="Total ALS Sales" disabled="true"/></td>
				<td class="label">Credit Card Sales: </td>
				<td><s:textfield id="displayCCSales" name="displayCCSales" theme="simple" title="Credit Card Sales" /></td>
				<td class="label">Total Shortage: </td>
				<td><s:textfield id="displayTotShortOfSales" name="displayTotShortOfSales" theme="simple" title="Total Short of Sales" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Non ALS Sales: </td>
				<td><s:textfield id="displayNonAlsSales" name="displayNonAlsSales" theme="simple" title="Non ALS Sales" disabled="true"/></td>
				<td class="label">Total Bank Deposits: </td>
				<td><s:textfield id="displayTotBankDep" name="displayTotBankDep" theme="simple" title="Total Bank Deposits" disabled="true"/></td>
				<td class="label">Total Overage: </td>
				<td><s:textfield id="displayTotOverSales" name="displayTotOverSales" theme="simple" title="Total Over Sales" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Total Sales: </td>
				<td><s:textfield id="displayTotSales" name="displayTotSales" theme="simple" title="Total Sales" disabled="true"/></td>
				<td class="label">Total Funds Received: </td>
				<td><s:textfield id="displayTotFundRec" name="displayTotFundRec" theme="simple" title="Total Funds Received" disabled="true"/></td>
				<td class="label">Net Overage/Shortage: </td>
				<td><s:textfield id="displayNetOverShortOfSales" name="displayNetOverShortOfSales" theme="simple" title="Net Over/Short of Sales" disabled="true"/></td>
			</tr>
			<tr><td><br><br></td></tr>
	    	<tr>
	    		<td class="label">Completed by Provider: </td>
				<td><s:checkbox id="provComp" name="provComp" theme="simple" title="Completed by Provider" onchange="completedByProv();"></s:checkbox></td>
				<td class="label">Date: </td>
				<td><s:textfield id="compDt" name="compDt" theme="simple" title="Date Completed by Provider" disabled="true"/></td>
				<td class="label">Remittance in Balance: </td>
				<td><s:textfield id="displayBalanced" name="displayBalanced" theme="simple" title="Billing Period has been balanced by Internal Provider" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Remittance Reviewed: </td>
				<td><s:checkbox id="remRev" name="remRev" theme="simple" title="Remittance Reviewed" ></s:checkbox></td>
			</tr>
	        <tr>
				<td class="label">Remittance Approved: </td>
				<td><s:checkbox id="remApp" name="remApp" theme="simple" title="Remittance Approved" onchange="remittanceApproved();"></s:checkbox></td>
				<td class="label">Approved By: </td>
				<td><s:textfield id="disAppBy" name="disAppBy" theme="simple" title="Approved By" disabled="true"/></td>
				<td class="label">Date: </td>
				<td><s:textfield id="disAppDt" name="disAppDt" theme="simple" title="Approved Date" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Comments: </td>
				<td colspan="4"><s:textfield id="disAppCom" name="disAppCom" theme="simple" cssStyle="width:100%;"/></td>
			</tr>
			<tr>
				<td class="label">Interface File Created: </td>
				<td><s:checkbox id="intFileCreated" name="intFileCreated" theme="simple" title="Interface File Created" disabled="true"></s:checkbox></td>
				<td class="label">Date: </td>
				<td><s:textfield id="intFileCreatedDt" name="intFileCreatedDt" theme="simple" title="Interface File Creation Date" disabled="true"/></td>
			</tr>
		</table>
		<s:submit id="saveRemittance" onclick="saveRemittance();" value="Save"></s:submit>
		</fieldset>
	</div>
	<br>
	<form id="subGridFrm">
		<s:hidden id="frmOper" name="oper"/>
		<s:hidden id="frmProvNo" name="provNo"/>
		<s:hidden id="frmBPFrom" name="bpFrom"/>
		<s:hidden id="frmBPTo" name="bpTo"/>
		<s:hidden id="frmIafaSeqNo" name="iafaSeqNo"/>
		<s:hidden id="frmBudgYear" name="budgYear"/>
	</form>
	
	<sj:tabbedpanel id="intProvTabs" selectedTab="0" useSelectedTabCookie="false"  cssStyle="width:950px;position:inherit">
				<sj:tab id="intProvTab1" target="intProvTOne" title="Bank Deposits" label="Bank Deposits" tabindex="1" />
				<sj:tab id="intProvTab2" target="intProvTTwo" title="Non ALS Details" label="Non ALS Details" tabindex="2" />
				<sj:tab id="intProvTab3" target="intProvTThree" title="Overage / Shortage" label="Overage / Shortage" tabindex="3" />				
				
				<div id="intProvTOne">
			 		<s:url id="intProvRemittanceBankDepDivUrl" value="intProvRemittanceBankDepDiv_input.action" />
					<sj:div id="intProvRemittanceBankDepDiv" 
							href="%{intProvRemittanceBankDepDivUrl}"
							width="950"
							formIds="subGridFrm"
							reloadTopics="reloadBankDepGrids">
					</sj:div>
				</div>
				<div id="intProvTTwo">
					<s:url id="intProvRemittanceNonAlsDetDivUrl" value="intProvRemittanceNonAlsDetDiv_input.action" />
					<sj:div id="intProvRemittanceNonAlsDetDiv" 
							href="%{intProvRemittanceNonAlsDetDivUrl}"
							width="950"
							formIds="subGridFrm"
							reloadTopics="reloadNonAlsDetGrids">
					</sj:div>
				</div>
				<div id="intProvTThree">
						<s:url id="alsOverUnderSalesGridURL" action="alsAccount/alsOverUnderSalesGrid_buildgrid" />
					<s:url id="alsOverUnderSalesGridEditURL" action="alsAccount/alsOverUnderSalesGridEdit_execute" />    
					<sjg:grid
						id="alsOverUnderSales"
						caption="Total Funds Received Over / Short of Sales - Details"
						href="%{alsOverUnderSalesGridURL}"
						editurl="%{alsOverUnderSalesGridEditURL}"		
						dataType="local"
						pager="true"
						navigator="true"
						navigatorEdit="true"
						navigatorView="true"
						navigatorAdd="true"
						navigatorDelete="true"
						navigatorSearch="false"
						navigatorRefresh="false"
					   	navigatorAddOptions="{width:600,reloadAfterSubmit:true,
					   						  addedrow:'last',
					   						  beforeSubmit:function(postData){
				   						  			$('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
					    	                      	postData.provNo = $('#frmProvNo').val();
					    	                      	postData.apbdBillingFrom = $('#frmBPFrom').val();
					    	                      	postData.apbdBillingTo = $('#frmBPTo').val();
					    	                      	return[true, ''];
						    	              },
					   						  afterSubmit:errorHandler,
					   						  afterSubmit: function () {
												    $('#alsInternalRemittance').jqGrid('setGridParam',{datatype:'json'});
												    $.publish('reloadInternalRemittance');
												    return [true];
											  },
					   						  afterShowForm:function(postData){
					   						  		prePopulate(this.id)
						    	                    return[true, ''];
						    	              },    
					   	                      addCaption:'Add New Code Info',
					   	                      closeAfterAdd:true,
					   	                      processData:'Adding Row to Database'}"
					   	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
					   	                       editCaption:'Edit Code Info',
					   	                       beforeSubmit:function(postData){
					   	                       $('#alsOverUnderSales').jqGrid('setGridParam',{datatype:'json'});
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
											   	 }
											   	}"
					    gridModel="model"
						rownumbers="false"
						editinline="false"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="75"
						width="910"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onCompleteTopics="alsOverUnderComplete"
						loadonce="true">
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="aousdFlag" index="aousdFlag" title="Overage/Shortage" width="10" sortable="true" editable="true" edittype="select" formatter="select" editoptions="{value: {O: 'Overage', U: 'Shortage'}}" editrules="{required:true}"/>
							<sjg:gridColumn name="aousdDesc" index="aousdDesc" title="Description" width="10" sortable="true" editable="true" editrules="{required:true}"/>
							<sjg:gridColumn name="aousdAmount" index="aousdAmount" title="Amount" width="10" sortable="true" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
					</sjg:grid>	
				</div>
		 	</sj:tabbedpanel>
		 	<br>
		 	<sj:tabbedpanel id="userTabs" selectedTab="0" useSelectedTabCookie="false"  cssStyle="width:950px;position:inherit">
				<sj:tab id="userTab1" target="utOne" title="ALS SABHRS Entries" label="ALS SABHRS Entries" tabindex="1" />
				<sj:tab id="userTab2" target="utTwo" title="Reverse ALS Entries" label="Reverse ALS Entries" tabindex="2" />
				
				<div id="utOne">
					<div id ='tempErrorDiv'>
						<span id='tempError'></span>
					</div>
					<s:url id="alsSabhrsEntriesGridURL" action="alsAccount/alsSabhrsEntriesGrid_buildgrid" />
					<s:url id="alsSabhrsEntriesGridEditURL" action="alsAccount/alsSabhrsEntriesGridEdit_execute" />    
					<sjg:grid
						id="alsSabhrsEntriesGrid"
						caption="ALS SABHRS Entries"
						href="%{alsSabhrsEntriesGridURL}"
						editurl="%{alsSabhrsEntriesGridEditURL}"		
						dataType="local"
						pager="true"
						navigator="true"
						navigatorEdit="true"
						navigatorView="false"
						navigatorAdd="true"
						navigatorDelete="true"
						navigatorSearch="false"
						navigatorRefresh="false"
						navigatorDeleteOptions="{delData : {provNo : function () { return $('#frmProvNo').val();},
															bpTo: function () { return $('#frmBPTo').val();}}}"
						navigatorAddOptions="{width:950,reloadAfterSubmit:true,
				    						  addedrow:'last',
				    						   afterShowForm: function ($form) {
				 	    					  	prePopulate(this.id);
				                    		  },
				    						  beforeSubmit: function (postData) {
				    						  		postData.transIdentifier = $('#frmTransIdentifier').val();
				    						  		postData.transGrp = $('#frmTransGrp').val();
				    						  		postData.provNo = $('#frmProvNo').val();
				    						  		postData.bpTo = $('#frmBPTo').val();
				    						  		return[true, ''];
				    						  }, 
				    						  afterSubmit:errorHandler,
				    	                      addCaption:'Add New Code Info',
				    	                      closeAfterAdd:true,
				    	                      processData:'Adding Row to Database'}"
						navigatorEditOptions="{width:950,reloadAfterSubmit:true,
				    	                       editCaption:'Edit Code Info',    
				    	                       closeAfterEdit:true,
				    	                       afterSubmit:errorHandler,
				    	                       processData:'Updating to Database'}"
						navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
					    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
					    gridModel="model"
						rownumbers="true"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="100"
						width="910"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onCompleteTopics="alsSabhrsEntriesComplete">
							
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:1}" align="right"/>
							<sjg:gridColumn name="jlr" index="jlr" title ="JLR" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:1}" edittype="select" formatter="select" editoptions="{value:','}"/>
							<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" formoptions="{colpos:1,rowpos:2}" edittype="select" formatter="select" editoptions="{value:','}" align="right" />
							<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" formoptions="{colpos:2,rowpos:2}" edittype="select" formatter="select" editoptions="{value:','}" align="right"/>
							<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:3}"  edittype="select" formatter="select" editoptions="{value:','}"/>
							<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:3}" editoptions="{size:5,maxlength:4}" align="right"/>
							<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:4}" edittype="select" formatter="select" editoptions="{value:','}"/>
							<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:4}" editrules="{required:true}" align="right"/>
							<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:5}"/>
							<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true" formatter="number" formatoptions="{decimalPlaces: 2}" formoptions="{colpos:2,rowpos:5}" editrules="{number:true,required:true}" align="right"/>
							<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
							<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="false" align="right"/>
							<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}" editrules="{required:true}" formoptions="{colpos:1,rowpos:6}"/>
							<sjg:gridColumn name="idPk.aseSeqNo" index="idPk.aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false" align="right"/>
							<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true" edittype="textarea" formoptions="{colpos:2,rowpos:6}" editrules="{required:true}"/>
					
					</sjg:grid>	
					<br>
					<s:url id="alsNonAlsTemplateGrid" action="alsAccount/alsNonAlsTemplateGrid_buildgrid" /> 
					<sjg:grid
						autoencode="false"
						id="alsNonAlsTemplateTable"
						caption="Templates"
						href="%{alsNonAlsTemplateGrid}"
						editurl="clientArray"		
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
						rownumbers="false"
						editinline="true"
						navigatorInlineEditButtons="false"
						loadonce="true"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="100"
						width="910"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onCompleteTopics="alsNonAlsTemplateTableComplete">
					
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="idPk.anatBudgetYear" index="idPk.anatBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
							<sjg:gridColumn name="idPk.anatCd" index="idPk.anatCd" title =" Template Code" width="15" sortable="false" hidden="false" editable="false" editrules="{required:true}"/>
							<sjg:gridColumn name="anatDesc" index="anatDesc" title ="Description" width="55" sortable="false" editable="false" edittype="textarea" editrules="{required:true}"/>
							
							<sjg:gridColumn name="amount" index="amount" title ="Amount" width="15" sortable="true" editable = "true" editrules="{number:true}" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
							<sjg:gridColumn name="selected" index="selected" title ="Select" width="15" sortable="true" align="center" editable = "true" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
							
					</sjg:grid>	
				</div>
				<div id="utTwo">
					<s:url id="manualProviderAdjEntriesIAFAGridURL" action="alsAccount/manualProviderAdjEntriesIAFAGrid_buildgrid" /> 
					<sjg:grid
						id="iafaGrid"
						caption="IAFA Table"
						href="%{manualProviderAdjEntriesIAFAGridURL}"		
						dataType="local"
						pager="true"
						navigator="false"
						navigatorEdit="false"
						navigatorView="false"
						navigatorAdd="false"
						navigatorDelete="false"
						
					    gridModel="model"
						rownumbers="true"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="100"
						width="910"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onSelectRowTopics="iafaRecordSelected">
							
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="apiProviderNo" index="apiProviderNo" title ="Issuing Provider No" width="10" sortable="false" align="right"/>
							<sjg:gridColumn name="aprBillingFrom" index="aprBillingFrom" title ="Billing Period From" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
							<sjg:gridColumn name="aprBillingTo" index="aprBillingTo" title ="Billing Period To" width="10" sortable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
							<sjg:gridColumn name="aiafaSeqNo" index="aiafaSeqNo" title ="IAFA Seq No" width="10" sortable="false" align="right"/>	
					
					</sjg:grid>
					<br>
					<div id ='revErrorDiv'>
						<span id='revError'></span>
					</div>
					<s:url id="iafaManualProviderAdjEntriesGridURL" action="alsAccount/manualProviderAdjEntriesSABHRSGrid_buildgrid" />
					<s:url id="iafaManualProviderAdjEntriesGridEditURL" action="alsAccount/manualProviderAdjEntriesSABHRSGridEdit_execute" />    
					<sjg:grid
						id="revAlsSabhrsEntriesGrid"
						caption="ALS SABHRS Entries"
						href="%{iafaManualProviderAdjEntriesGridURL}"
						editurl="%{iafaManualProviderAdjEntriesGridEditURL}"		
						dataType="local"
						pager="true"
						navigator="true"
						navigatorEdit="true"
						navigatorView="false"
						navigatorAdd="true"
						navigatorDelete="true"
						navigatorAddOptions="{width:950,reloadAfterSubmit:true,
				    						  addedrow:'last',
				 	    					  afterShowForm:function(postData){
					   						  	prePopulate(this.id);
						    	              }, 
				                    		  beforeSubmit: function (postData) {
				                    		  		$('#revAlsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
				    						  		postData.transIdentifier = $('#frmTransIdentifier').val();
				    						  		postData.transGrp = $('#frmTransGrp').val();
				    						  		postData.bpFrom = $('#frmBPFrom').val();
				    						  		postData.bpTo = $('#frmBPTo').val();
				    						  		return[true, ''];
				    						  },  
				    						  afterSubmit:errorHandler,
				    	                      addCaption:'Add New Code Info',
				    	                      closeAfterAdd:true,
				    	                      processData:'Adding Row to Database'}"
						navigatorEditOptions="{width:950,reloadAfterSubmit:false,
				    	                       editCaption:'Edit Code Info',    
				    	                       closeAfterEdit:true,
				    	                       beforeSubmit: function (postData) {
				                    		  		$('#revAlsSabhrsEntriesGrid').jqGrid('setGridParam',{datatype:'json'});
				    						  		return[true, ''];
				    						  },  
				    	                       afterSubmit:errorHandler,
				    	                       processData:'Updating to Database'
				    	                      }"
						navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
					    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
					    gridModel="model"
						rownumbers="true"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="100"
						width="910"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadRevAlsSabhrsEntriesGrid"
						onCompleteTopics="revAlsSabhrsEntriesComplete">
							
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:4}" align="right"/>
							<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:2,rowpos:1}"/>
							<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:2}"/>
							<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:2,rowpos:2}" align="right"/>
							<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:3}"/>
							<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:2,rowpos:3}" align="right"/>
							<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value:','}" formoptions="{colpos:1,rowpos:4}"/>
							<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:1}" align="right"/>
							<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:5}"/>
							<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:5}" formatter= "number" formatoptions="{decimalPlaces: 2}" align="right" editrules="{number:true,required:true}"/>
							<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
							<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="false" align="right"/>
							<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}" editrules="{required:true}" formoptions="{colpos:1,rowpos:6}"/>
							<sjg:gridColumn name="idPk.aseSeqNo" index="aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false" align="right"/>
							<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true" formoptions="{colpos:2,rowpos:6}" edittype="textarea" editrules="{required:true}"/>
					</sjg:grid>
				</div>
		 	</sj:tabbedpanel>
<s:form id="addSlipFrm">
	<s:hidden id="remittanceId" name="remittanceId" value=""/>
	<s:hidden id="apbdId" name="apbdId" value=""/>
</s:form>

<s:url id="depositSlipUrl" action="depositSlipDlg_input"/>
<sj:dialog id="depositSlipDlg" 
    autoOpen="false"
    href="%{depositSlipUrl}"
    modal="true" 
    resizable="false"
    height="auto" 
    width="auto"
    title="Deposit Slip"
    formIds="addSlipFrm" 
    onCloseTopics="addDocumentDlgClose"
    buttons="{
    	'Save': {
    			text: 'Save',
    			id: 'depositSlipSave',
    			click :function() {
   	    			$('#uploadSubmit').trigger('click');
   	    		}
   	    },
   	    'Delete': {
   	    		text: 'Delete',
   	    		id: 'depositSlipDel',
   	    		click :function() {
   	    			$('#deleteSubmit').trigger('click');
   	    		}
   	    },
        'Close': function() {
            $('#depositSlipDlg').dialog('close');
            $('#depositSlipDlg').empty();
   	    }
    }"/>
</fwp:template>
