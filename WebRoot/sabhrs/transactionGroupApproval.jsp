<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template >

    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
        <script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.transactionGroupApproval.js"></script>  
    	<script type="text/javascript" src= "/alsaccount/scripts/fieldEdits.js"></script>  
    </fwp:head>
    
    <s:hidden id="bankCodeLst" name="bankCodeLst"/>
    <s:hidden id="providerLst" name="providerLst"/>
    <s:hidden id="groupIdentifierLst" name="groupIdentifierLst"/>
    <s:hidden id="user" name="user"/>

	<form id='rptFrm'>
      <s:hidden id="frmRptType" name="rptType" />
      <s:hidden id="frmFilters" name="filters" />
      
    </form>
    
    <s:form action="getTreasureDepositTicketPdf">
    	<s:hidden id="type" name="type" value="S" />
    	<s:hidden id="txIdentifier" name="txIdentifier" />
      	<s:hidden id="transCd" name="transCd" />
    </s:form>
       
    <div style="width:800px;text-align:center">
    	<h2 class="title">Transaction Group Approval for Summary and Interface file</h2>
   	</div>
   	
   	<s:url id="transGrpAppQueryDivUrl" value="transGrpAppQueryDiv_input.action" />
	<sj:div id="searchCriteriaDiv" 
			href="%{transGrpAppQueryDivUrl}"
			width="950">
	</sj:div>
	
  	<br>
  	<br>
	<s:url id="transGroupMaintGridURL" action="alsAccount/transGroupApprovalGrid_buildgrid" />
	<s:url id="transGroupMaintGridEditURL" action="alsAccount/transGroupApprovalGridEdit_execute" />
	<sjg:grid
		id="transGroupApprovalTable"
		caption="Transaction Groups"
		href="%{transGroupMaintGridURL}"
		editurl="%{transGroupMaintGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true,closeAfterSearch:true}"
    	navigatorAddOptions="{width:950,reloadAfterSubmit:true,
    						  addedrow:'last',
    	                      addCaption:'Add New Code Info',
    	                      afterSubmit:errorHandler,
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:1000,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
    	                       afterSubmit:errorHandler,	    
    	                       closeAfterEdit:true,
    	                       processData:'Updating to Database'}"
    	navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
    	navigatorDeleteOptions="{afterSubmit:errorHandler}"
    	gridModel="model"
    	formIds="gridFrm"
		rownumbers="false"
		editinline="false"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		resizable="true"
		reloadTopics="reloadTransGroupMaintTable"
		onCompleteTopics="transGroupComplete"
		onSelectRowTopics="transGroupSelected"
		>
		
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.atgTransactionCd" index="idPk.atgTransactionCd" title =" Tx Group Type" width="5" sortable="false" hidden="false" editable="true" formoptions="{colpos:1,rowpos:1}"/>
			<sjg:gridColumn name="desc" index="desc" title =" Description" width="15" sortable="false" editable="true" edittype="textarea" formoptions="{colpos:2,rowpos:1}"/>
			<sjg:gridColumn name="idPk.atgsGroupIdentifier" index="idPk.atgsGroupIdentifier" title =" Transaction Group Identifier" width="25" sortable="false" editable="true" formoptions="{colpos:1,rowpos:2}" editoptions="{size:25}"/>
			<sjg:gridColumn name="atgsWhenCreated" index="atgsWhenCreated" title =" Transaction Group Created" width="20" sortable="false" editable="true" formatter="date" formatoptions="{newformat : 'm/d/Y h:m:s', srcformat : 'Y/m/d'}" formoptions="{colpos:2,rowpos:2}"/>
			<sjg:gridColumn name="programYear" index="programYear" sortable="false" editable="true" hidden="true" title ="Program Year" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:3}" editoptions="{size:5,maxlength:4}"/>
			<sjg:gridColumn name="atgsAccountingDt" index="atgsAccountingDt" title =" Accounting Date" width="15" sortable="false" editable="true" formatter="date" formatoptions="{newformat : 'm/d/Y', srcformat : 'Y/m/d'}" formoptions="{colpos:2,rowpos:3}"/>
			<sjg:gridColumn name="budgYear" index="budgYear" sortable="false" editable="true" hidden="true" title ="Budget Year" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:4}" editoptions="{size:5,maxlength:4}"/>
			<sjg:gridColumn name="atgsSummaryStatus" index="atgsSummaryStatus" title =" Summary Approval Status" width="15" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {0:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}" hidden="true" editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:5}"
    		searchtype="select" searchoptions="{searchhidden: true, value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}"/>
			<sjg:gridColumn name="atgsSummaryApprovedBy" index="atgsSummaryApprovedBy" title =" Summary Approved By" width="15" sortable="false" editable="false" hidden="true" formoptions="{colpos:2,rowpos:5}"/>
			<sjg:gridColumn name="atgsSummaryDt" index="atgsSummaryDt" title =" Summary Approval Date" width="15" sortable="false" editable="false" hidden="true" formatter="date" formatoptions="{newformat : 'm/d/Y h:m:s', srcformat : 'Y/m/d'}" formoptions="{colpos:3,rowpos:5}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="atgsInterfaceStatus" index="atgsInterfaceStatus" title =" Interface Approval Status" width="15" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {0:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}" hidden="true"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:6}" 
			searchtype="select" searchoptions="{searchhidden: true, value: {null:'',A: 'Approved', D: 'Disapproved', N: 'Not Applicable'}}"/>
			<sjg:gridColumn name="atgsInterfaceApprovedBy" index="atgsInterfaceApprovedBy" title =" Interface Approved By" width="15" sortable="false" editable="false" hidden="true" formoptions="{colpos:2,rowpos:6}"/>
			<sjg:gridColumn name="atgsInterfaceDt" index="atgsInterfaceDt" title =" Interface Approval Date" width="15" sortable="false" editable="false" hidden="true" formatter="date" formatoptions="{newformat : 'm/d/Y h:m:s', srcformat : 'Y/m/d'}" formoptions="{colpos:3,rowpos:6}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="atgsWhenUploadToSummary" index="atgsWhenUploadToSummary" sortable="false" editable="true" hidden="true" title ="Date Uploaded to Summary" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:7}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="abcBankCd" index="abcBankCd" sortable="false" editable="true" hidden="true" title ="Bank Code" width="15"  editrules="{edithidden:true}" formoptions="{colpos:2,rowpos:7}" searchtype="select" searchoptions="{searchhidden: true, value:','}"/>
			<sjg:gridColumn name="atgsBankReferenceNo" index="atgsBankReferenceNo" sortable="false" editable="true" hidden="true" title ="Bank Reference No" width="15"  editrules="{edithidden:true}" formoptions="{colpos:3,rowpos:7}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="atgsArGlFlag" index="atgsArGlFlag" sortable="false" editable="true" hidden="true" title ="Interface File" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:8}"/>
			<sjg:gridColumn name="atgsFileCreationDt" index="atgsFileCreationDt" sortable="false" editable="true" hidden="true" title ="Date File Created" width="15" formatter="date" formatoptions="{newformat : 'm/d/Y h:m:s', srcformat : 'Y/m/d'}"  editrules="{edithidden:true}" formoptions="{colpos:2,rowpos:8}"/>
			<sjg:gridColumn name="atgsFileName" index="atgsFileName" sortable="false" editable="true" hidden="true" title ="Interface File Name" width="15"  editrules="{edithidden:true}" formoptions="{colpos:3,rowpos:8}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="atgsNonAlsFlag" index="atgsNonAlsFlag" sortable="false" editable="true" hidden="true" title ="Non Als Entries" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:9}"/>
			<sjg:gridColumn name="atgsNetDrCr" index="atgsNetDrCr" sortable="false" editable="true" hidden="true" title ="Net Cash Debit/Credit" width="15" formatter="currency" formatoptions="{decimalPlaces: 2}"  editrules="{edithidden:true}" formoptions="{colpos:2,rowpos:9}"/>
			<sjg:gridColumn name="atgsDepositId" index="atgsDepositId" sortable="false" editable="true" hidden="true" title ="Deposit Id" width="15"  editrules="{edithidden:true}" formoptions="{colpos:3,rowpos:9}" searchoptions="{searchhidden: true}"/>
			<sjg:gridColumn name="atgsRemarks" index="atgsRemarks" sortable="false" editable="true" hidden="true" title ="Remarks" width="15"  editrules="{edithidden:true}" formoptions="{colpos:1,rowpos:10}"/>
			<sjg:gridColumn name="atgsWhenUploadedToSabhrs" index="atgsWhenUploadedToSabhrs" sortable="false" editable="true" hidden="true" title ="Date Uploaded to SABHRS" width="15"  editrules="{edithidden:true}" formoptions="{colpos:2,rowpos:10}"/>
			<sjg:gridColumn name="provider" index="provider" sortable="false" editable="true" hidden="true" title ="Provider" width="15" searchtype="select" searchoptions="{searchhidden: true, value:','}"/>
	</sjg:grid>	
	<br/>
	<br/>
	<div id="transGroupAppDiv" style="display:none">
		<span id='rtnAction'></span>
		<s:form id="transGroupApprovalJsonFrm" theme="simple">
		<table>
			<tr>
				<td class="label">Transaction Group Type: </td>
				<td><s:textfield id="transGroupType" name="transGroupType" theme="simple" title="Transaction Group Type" /></td>
				<td class="label">Description: </td>
				<td colspan="2"><s:textfield id="desc" name="desc" theme="simple" title="Description" size="50%"/></td>
			</tr>
			<tr>
				<td class="label">Transaction Group Identifier: </td>
				<td colspan="2"><s:textfield id="transGroupIdentifier" name="transGroupIdentifier" theme="simple" title="Transaction Group Identifier" size="50%"/></td>
				<td class="label">Transaction Group Creation Date: </td>
				<td><s:textfield id="transGroupCreateDt" name="transGroupCreateDt" theme="simple" title="Transaction Group Create Date" /></td>
			</tr>
			<tr>
				<td class="label">Program Year: </td>
				<td><s:textfield id="programYear" name="programYear" theme="simple" title="Transaction Group Type"/></td>
				<td class="label">Change Program Year: </td>
				<td><s:textfield id="changeProgramYear" name="changeProgramYear" theme="simple" title="Change Program Year"/></td>
				<td class="label">Accounting Date: </td>
				<td><s:textfield id="accountDt" name="accountDt" theme="simple" title="Description"/></td>
				<td class="label">Change Accounting Date: </td>
				<td><s:textfield id="changeAccountDt" name="changeAccountDt" theme="simple" title="Change Accounting Date"/></td>
			</tr>
			<tr>
				<td class="label">Budget Year: </td>
				<td><s:textfield id="budgYear" name="budgYear" theme="simple" title="Transaction Group Type" /></td>
			</tr>
			<tr>
				<td class="label">Summary Approval Status: </td>
				<td><s:select id="sumAppStat"  name="sumAppStat"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}" onchange="sumAppStatSelected()"/></td>
				<td><table>
					<tr>
						<td class="label">All </td>
						<td><s:checkbox id="sumAll" name="sumAll" theme="simple"/></td>
					</tr>
				</table></td>
				<td class="label">Summary Approved By: </td>
				<td><s:textfield id="sumAppBy" name="sumAppBy" theme="simple" title="Summary Approved By" /></td>
				<td class="label">Summary Approved Date: </td>
				<td><s:textfield id="sumAppDt" name="sumAppDt" theme="simple" title="Summary Approved Date" /></td>
			</tr>
			<tr>
				<td class="label">Interface Approval Status: </td>
				<td><s:select id="intAppStat"  name="intAppStat"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'A':'Approved', 'D':'Disapproved', 'N':'Not Applicable'}" onchange="intStatSelected()"/></td>
				<td><table>
					<tr>
						<td class="label">All </td>
						<td><s:checkbox id="intAll" name="intAll" theme="simple"/></td>
					</tr>
				</table></td>
				<td class="label">Interface Approved By: </td>
				<td><s:textfield id="intAppBy" name="intAppBy" theme="simple" title="Interface Approved By" /></td>
				<td class="label">Interface Approved Date: </td>
				<td><s:textfield id="intAppDt" name="intAppDt" theme="simple" title="Interface Approved Date" /></td>
			</tr>
			<tr>
				<td class="label">Date Uploaded to Summary: </td>
				<td><s:textfield id="upToSumDt" name="upToSumDt" theme="simple" title="Date Uploaded to Summary" /></td>
				<td class="label">Bank Code: </td>
				<td><s:textfield id="bankCd" name="bankCd" theme="simple" title="Bank Code" /></td>
				<td class="label">Bank Reference No: </td>
				<td><s:textfield id="bankRefNo" name="bankRefNo" theme="simple" title="Bank Reference Number"/></td>
			</tr>
			<tr>
				<td class="label">Interface File: </td>
				<td><s:select id="intFile"  name="intFile"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'AR':'Account Recievable', 'GL':'General Ledger', 'NO':'None'}"/></td>
				<td class="label">Date File Created: </td>
				<td><s:textfield id="intFileCreateDt" name="intFileCreateDt" theme="simple" title="Date File Created" /></td>
				<td class="label">Interface File Name: </td>
				<td><s:textfield id="intFileName" name="intFileName" theme="simple" title="Interface File Name" /></td>
			</tr>
			<tr>
				<td class="label">Non ALS Entries: </td>
				<td><s:select id="nonAlsEnt"  name="nonAlsEnt"
							  headerKey="-1" headerValue="" theme="simple"
							  list="#{'Y':'Yes', 'N':'No'}"/></td>
				<td class="label">Net Cash Debit/Credit: </td>
				<td><s:textfield id="netCashDrCr" name="netCashDrCr" theme="simple" title="Net Cash Debit/Credit" /></td>
				<td class="label">Deposit Id: </td>
				<td><s:textfield id="depId" name="depId" theme="simple" title="Deposit Id" /></td>
			</tr>
			<tr>
				<td class="label">Remarks: </td>
				<td colspan="2"><s:textfield id="remarks" name="remarks" theme="simple" title="Remarks" size="50%"/></td>
				<td class="label">Date Uploaded to SABHRS: </td>
				<td><s:textfield id="upToSabhrsDt" name="upToSabhrsDt" theme="simple" title="Date Uploaded to SABHRS" /></td>
			</tr>
		</table>
		</s:form>
		<input type="submit" value="Save" onclick="submitTransGroupApproval();">
		<input type="submit" value="Back" onclick="$('#transGroupAppDiv').toggle(false);">
	</div>
	<input id="getRpt" 
			   type="button"
			   onclick="getGenRpt();" 
			   value="Generate Deposit Ticket" disabled>
	
</fwp:template>
