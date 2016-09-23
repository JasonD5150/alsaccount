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
    <script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.intProvRemittance.js"></script>   
	<style type="text/css">
		@import url("/alsaccount/css/alsaccount.css");
		#appCom{
			width:100%;
		}
    </style>
    <script>

    </script>
</fwp:head>
<s:hidden id="hasUserRole" value="%{hasUserRole}"></s:hidden>
<s:hidden id="hasIntProvRole" value="%{hasIntProvRole}"></s:hidden>
<s:hidden id="fundLst" name="fundLst"/>
<s:hidden id="subClassLst" name="subClassLst"/>
<s:hidden id="jlrLst" name="jlrLst"/>
<s:hidden id="projectGrantLst" name="projectGrantLst"/>
<s:hidden id="orgLst" name="orgLst"/>
<s:hidden id="accountLst" name="accountLst"/>

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
			<sjg:gridColumn name="amtRec" index="amtRec" title="Amount Received" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}"/>
			<sjg:gridColumn name="completeProvider" index="completeProvider" title="Date Completed by Provider" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }"/>
			<sjg:gridColumn name="airOfflnPaymentApproved" index="airOfflnPaymentApproved" title="RemittanceApproved" width="15" sortable="true" editable="true" formatter="checkbox" align="center" edittype="checkbox" editoptions="{ value: 'true:false' }"/>
			
			<!-- HIDDEN -->
			<sjg:gridColumn name="airSystemSales" index="airSystemSales" title="System Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="eftddd" index="eftddd" title="EFT DDD" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="airOtcPhoneSales" index="airOtcPhoneSales" title="OTC SALES" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airPae" index="airPae" title="PAE's" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airNonAlsSales" index="airNonAlsSales" title="Non ALS Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airTotSales" index="airTotSales" title="Total Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="amtDue" index="amtDue" title="Total ALS Sales" width="60" sortable="true" editable="false" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airCreditSales" index="airCreditSales" title="Credit Card Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}" hidden="true"/>
			<sjg:gridColumn name="totFundsRec" index="totFundsRec" title="Total Funds Received" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="totBankDep" index="totBankDep" title="Total Bank Deposits" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="billingBallanced" index="billingBallanced" title="Billing Period had been balanced by Internal Provider" editable="false" width="25" sortable="true" edittype="select" formatter="select" editoptions="{value: {Y: 'YES', N: 'NO'}}" hidden="true"/>
			<sjg:gridColumn name="airDifference" index="airDifference" title="Difference" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airShortSales" index="airShortSales" title="Total Short of Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airOverSales" index="airOverSales" title="Total Over Sales" width="60" sortable="true" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="netOverShortOfSales" index="netOverShortOfSales" title="Net Over/Short of Sales" width="60" sortable="false" editable="false" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentApproved" index="airOfflnPaymentApproved" title="Approved" width="60" sortable="true" editable="false" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentAppBy" index="airOfflnPaymentAppBy" title="Approved By" width="60" sortable="true" editable="false" hidden="true"/>
			<sjg:gridColumn name="offlnPaymentAppDt" index="offlnPaymentAppDt" title="Date" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="airOfflnPaymentAppCom" index="airOfflnPaymentAppCom" title="Comments" width="100" sortable="true" editable="true" hidden="true"/>
			<sjg:gridColumn name="intFileCreateDt" index="intFileCreateDt" title="SABHRS Interface File Created" width="70" sortable="true" editable="false" formatter="date" formatoptions="{srcformat:'ISO8601Long' , newformat:'m/d/Y' }" hidden="true"/>
			<sjg:gridColumn name="intFileGenerated" index="intFileGenerated" title="Interface File Created" hidden="true" editable="false"/>
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
				<td class="label">Total Sales: </td>
				<td><s:textfield id="displayTotSales" name="displayTotSales" theme="simple" title="Total Sales" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">System Sales: </td>
				<td><s:textfield id="displaySysSales" name="displaySysSales" theme="simple" title="System Sales" disabled="true"/></td>
				<td class="label">Amount Received: </td>
				<td><s:textfield id="displayAmtRec" name="displayAmtRec" theme="simple" title="Amount Recieved" disabled="true"/></td>
				<td class="label">EFT DDD: </td>
				<td><s:textfield id="displayEftDdd" name="displayEftDdd" theme="simple" title="EFT DDD" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">OTC Sales: </td>
				<td><s:textfield id="displayOtcSales" name="displayOtcSales" theme="simple" title="OTC Sales" disabled="true"/></td>
				<td class="label">PAEs: </td>
				<td><s:textfield id="displayPaes" name="displayPaes" theme="simple" title="PAEs" disabled="true"/></td>
				<td class="label">Non ALS Sales: </td>
				<td><s:textfield id="displayNonAlsSales" name="displayNonAlsSales" theme="simple" title="Non ALS Sales" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Total ALS Sales: </td>
				<td><s:textfield id="displayTotAlsSales" name="displayTotAlsSales" theme="simple" title="Total ALS Sales" disabled="true"/></td>
				<td class="label">Credit Card Sales: </td>
				<td><s:textfield id="displayCCSales" name="displayCCSales" theme="simple" title="Credit Card Sales" /></td>
				<td class="label">Total Funds Received: </td>
				<td><s:textfield id="displayTotFundRec" name="displayTotFundRec" theme="simple" title="Total Funds Received" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Total Bank Deposits: </td>
				<td><s:textfield id="displayTotBankDep" name="displayTotBankDep" theme="simple" title="Total Bank Deposits" disabled="true"/></td>
				<td class="label">Balanced by Internal Provider: </td>
				<td><s:textfield id="displayBalanced" name="displayBalanced" theme="simple" title="Billing Period has been balanced by Internal Provider" disabled="true"/></td>
				<td class="label">Difference: </td>
				<td><s:textfield id="displayDif" name="displayDif" theme="simple" title="Difference" disabled="true"/></td>
			</tr>
			<tr>
				<td class="label">Total Short of Sales: </td>
				<td><s:textfield id="displayTotShortOfSales" name="displayTotShortOfSales" theme="simple" title="Total Short of Sales" disabled="true"/></td>
				<td class="label">Total Over Sales: </td>
				<td><s:textfield id="displayTotOverSales" name="displayTotOverSales" theme="simple" title="Total Over Sales" disabled="true"/></td>
				<td class="label">Net Over/Short of Sales: </td>
				<td><s:textfield id="displayNetOverShortOfSales" name="displayNetOverShortOfSales" theme="simple" title="Net Over/Short of Sales" disabled="true"/></td>
			</tr>
	    	<tr>
	    		<td class="label">Completed by Provider: </td>
				<td><s:checkbox id="provComp" name="provComp" theme="simple" title="Completed by Provider" onchange="completedByProv();"></s:checkbox></td>
				<td class="label">Date: </td>
				<td><s:textfield id="compDt" name="compDt" theme="simple" title="Date Completed by Provider" disabled="true"/></td>
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
				<td colspan="4"><s:textfield id="disAppCom" name="disAppCom" theme="simple"/></td>
			</tr>
		</table>
		<s:submit onclick="saveRemittance();" value="Save"></s:submit>
		</fieldset>
	</div>
	<br>
	<form id="subGridFrm">
		<s:hidden id="frmProvNo" name="provNo"/>
		<s:hidden id="frmBPFrom" name="bpFrom"/>
		<s:hidden id="frmBPTo" name="bpTo"/>
	</form>
	<sj:tabbedpanel id="remittanceTabs" selectedTab="0" useSelectedTabCookie="false"  cssStyle="width:995px;position:inherit">
				<sj:tab id="tab1" target="tone" title="Bank Deposits" label="Bank Deposits" tabindex="1" />
				<sj:tab id="tab2" target="ttwo" title="Non Als Details" label="Non Als Details" tabindex="2" />
				<sj:tab id="tab3" target="tthree" title="Non Als SABHRS Entries" label="Non Als SABHRS Entries" tabindex="3" />
				<sj:tab id="tab4" target="tfour" title="Over / Short of Sales" label="Over / Short of Sales" tabindex="4" />
				
				<div id="tone">
			 		<s:url id="intProvBankCdDepLinkDivUrl" value="intProvBankCdDepLinkDiv_input.action" />
					<sj:div id="intProvBankCdDepLinkDiv" 
							href="%{intProvBankCdDepLinkDivUrl}"
							width="950"
							formIds="subGridFrm"
							reloadTopics="reloadSubGrids">
					</sj:div>
				</div>
				<div id="ttwo">
					<s:url id="alsNonAlsDetailsGridURL" action="alsAccount/alsNonAlsDetailsGrid_buildgrid" />
					<s:url id="alsNonAlsDetailsGridEditURL" action="alsAccount/alsNonAlsDetailsGridEdit_execute" />    
					<sjg:grid
						id="alsNonAlsDetails"
						caption="Non ALS Details"
						href="%{alsNonAlsDetailsGridURL}"
						editurl="%{alsNonAlsDetailsGridEditURL}"		
						dataType="local"
						pager="true"
						navigator="true"
						navigatorEdit="true"
						navigatorView="true"
						navigatorAdd="true"
						navigatorDelete="true"
						navigatorSearch="false"
						navigatorRefresh="false"
						navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
					   	navigatorAddOptions="{width:600,reloadAfterSubmit:true,
					   						  addedrow:'last',
					   						  beforeSubmit:function(postData){
					   						  					var grid = $('#alsInternalRemittance');
																var sel_id = grid.jqGrid('getGridParam','selrow'); 
					    	                      				postData.provNo = $('#provNo').val();
					    	                      				postData.apbdBillingFrom = grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom');
					    	                      				postData.apbdBillingTo = grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo');
						    	                      			return[true, ''];
						    	              },
					   						  afterSubmit:errorHandler,
					   						  afterShowForm:prePopulate,   
					   	                      addCaption:'Add New Code Info',
					   	                      closeAfterAdd:true,
					   	                      processData:'Adding Row to Database'}"
					   	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
					   	                       editCaption:'Edit Code Info',
					   	                        beforeSubmit:function(postData){
						    	                      	postData.provNo = $('#provNo').val();
						    	                      	return[true, ''];
						    	              },
					   	                       closeAfterEdit:true,
					   	                       afterSubmit:errorHandler,
					   	                       processData:'Updating to Database'}"
					   	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
					   	navigatorDeleteOptions="{afterSubmit:errorHandler}"
					    gridModel="model"
						rownumbers="false"
						editinline="false"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="75"
						width="950"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onCompleteTopics="alsNonAlsDetailsComplete"
						loadonce="true">
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="anatCd" index="anatCd" title="Code" width="10" sortable="true" editable="true"/>
							<sjg:gridColumn name="anadDesc" index="anadDesc" title="Description" width="10" sortable="true" editable = "true" editrules="{required:true}"/>
							<sjg:gridColumn name="anadAmount" index="anadAmount" title="Amount" width="10" sortable="true" editable = "true" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
					</sjg:grid>
				</div>
				<div id="tthree">
					<div id ='tempErrorDiv'>
						<span id='tempError'></span>
					</div>
					<div id="nonAlsSabhrsEntriesDiv">
					<s:url id="alsSabhrsEntriesGridURL" action="alsAccount/alsSabhrsEntriesGrid_buildgrid" />
					<s:url id="alsSabhrsEntriesGridEditURL" action="alsAccount/alsSabhrsEntriesGridEdit_execute" />    
					<sjg:grid
						id="alsSabhrsEntriesTable"
						caption="Non Als SABHRS Entries"
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
						navigatorAddOptions="{width:950,reloadAfterSubmit:true,
				    						  addedrow:'last',
				 	    					  afterShowForm: function ($form) {
				                    			$form.closest('.ui-jqdialog').position({
				                        			my: 'center',
				                        			at: 'center',
				        							of: $('#alsInternalRemittance').closest('div.ui-jqgrid')
				                    			});
				                    		  },
				    						  afterSubmit:errorHandler,
				    	                      addCaption:'Add New Code Info',
				    	                      closeAfterAdd:true,
				    	                      processData:'Adding Row to Database'}"
						navigatorEditOptions="{width:950,reloadAfterSubmit:true,
				    	                       editCaption:'Edit Code Info',    
				    	                       closeAfterEdit:true,
				    	                       afterSubmit:errorHandler,
				    	                       processData:'Updating to Database',
				    	                       afterShowForm: function ($form) {
				                    			$form.closest('.ui-jqdialog').position({
				                        			my: 'center',
				                        			at: 'center',
				        							of: $('#transGroupDtlsTable').closest('div.ui-jqgrid')
				                    			});
				                    		   }}"
						navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
					    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
					    gridModel="model"
						rownumbers="true"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="100"
						width="950"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						onCompleteTopics="alsSabhrsEntriesComplete">
							
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:1}" align="right"/>
							<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" hidden="true" editable="true" formoptions="{colpos:2,rowpos:1}" edittype="select" formatter="select" editoptions="{edithidden: true,value:','}"/>
							<sjg:gridColumn name="jlr" index="jlr" title ="JLR" width="10" sortable="false" editable="false"/>
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
					</div>
					<div id="alsNonAlsTempDiv" style="display:none;">
					<s:url id="alsNonAlsTemplateGrid" action="alsAccount/alsNonAlsTemplateGrid_buildgrid" /> 
					<sjg:grid
						autoencode="false"
						id="alsNonAlsTemplateTable"
						caption="Templates"
						href="%{alsNonAlsTemplateGrid}"
						editurl="clientArray"		
						dataType="json"
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
						height="200"
						width="950"
						rowNum="1000"
						onCompleteTopics="alsNonAlsTemplateTableComplete">
					
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="idPk.anatBudgetYear" index="idPk.anatBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
							<sjg:gridColumn name="idPk.anatCd" index="idPk.anatCd" title =" Template Code" width="15" sortable="false" hidden="false" editable="false" editrules="{required:true}"/>
							<sjg:gridColumn name="anatDesc" index="anatDesc" title ="Description" width="55" sortable="false" editable="false" edittype="textarea" editrules="{required:true}"/>
							
							<sjg:gridColumn name="amount" index="amount" title ="Amount" width="15" sortable="true" editable = "true" editrules="{number:true}" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
							<sjg:gridColumn name="selected" index="selected" title ="Select" width="15" sortable="true" align="center" editable = "true" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
							
					</sjg:grid>	
					</div>		
				</div>
				<div id="tfour">
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
						    	                      	var grid = $('#alsInternalRemittance');
														var sel_id = grid.jqGrid('getGridParam','selrow'); 
					    	                      		postData.provNo = $('#provNo').val();
					    	                      		postData.apbdBillingFrom = grid.jqGrid('getCell', sel_id, 'idPk.airBillingFrom');
					    	                      		postData.apbdBillingTo = grid.jqGrid('getCell', sel_id, 'idPk.airBillingTo');
						    	                      	return[true, ''];
						    	              },
					   						  afterSubmit:errorHandler,
					   						  afterShowForm:prePopulate,   
					   	                      addCaption:'Add New Code Info',
					   	                      closeAfterAdd:true,
					   	                      processData:'Adding Row to Database'}"
					   	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
					   	                       editCaption:'Edit Code Info',
					   	                        beforeSubmit:function(postData){
						    	                      	postData.provNo = $('#provNo').val();
						    	                      	return[true, ''];
						    	              },
					   	                       closeAfterEdit:true,
					   	                       afterSubmit:errorHandler,
					   	                       processData:'Updating to Database'}"
					   	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
					   	navigatorDeleteOptions="{afterSubmit:errorHandler}"
					    gridModel="model"
						rownumbers="false"
						editinline="false"
						viewrecords="true"
						scroll="true"
						scrollrows="true"
						height="75"
						width="950"
						rowNum="1000"
						formIds="subGridFrm"
						reloadTopics="reloadSubGrids"
						loadonce="true">
							<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
							<sjg:gridColumn name="aousdFlag" index="aousdFlag" title="Over/Short of Sales" width="10" sortable="true" editable="true" edittype="select" formatter="select" editoptions="{value: {O: 'Over Sale', U: 'Short of Sales'}}" editrules="{required:true}"/>
							<sjg:gridColumn name="aousdDesc" index="aousdDesc" title="Description" width="10" sortable="true" editable="true" editrules="{required:true}"/>
							<sjg:gridColumn name="aousdAmount" index="aousdAmount" title="Amount" width="10" sortable="true" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
					</sjg:grid>
				</div>
		 	</sj:tabbedpanel>
</fwp:template>
