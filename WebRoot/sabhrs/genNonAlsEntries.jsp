<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template>
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
	   	<script type="text/javascript" src= "/alsaccount/sabhrs/scripts/fwp.genNonAlsEntries.js"></script>  
    </fwp:head>
    
    <s:hidden id="fundLst" name="fundLst"/>
	<s:hidden id="subClassLst" name="subClassLst"/>
	<s:hidden id="jlrLst" name="jlrLst"/>
	<s:hidden id="projectGrantLst" name="projectGrantLst"/>
	<s:hidden id="orgLst" name="orgLst"/>
	<s:hidden id="accountLst" name="accountLst"/>
    
    <form id='gridFrm'>
		<s:hidden id="frmOper" name="oper"/>
		<s:hidden id="frmTransGrp" name="transGrp"/>
		<s:hidden id="frmTransIdentifier" name="transIdentifier"/>
		<s:hidden id="frmNonAlsEntries" name="templates"/>
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Generate Non Als Entries</h2>
   	</div>

   	<s:url id="transGroupDtlsGridURL" action="alsAccount/transGroupDtlsGrid_buildgrid" />
	<s:url id="transGroupDtlsGridEditURL" action="alsAccount/transGroupDtlsGridEdit_execute" />    
	<sjg:grid
		id="transGroupDtlsTable"
		caption="Transaction Groups Details"
		href="%{transGroupDtlsGridURL}"
		editurl="%{transGroupDtlsGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="true"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="200"
		width="950"
		rowNum="1000"
		onSelectRowTopics="transGroupDtlsSelected">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.atgTransactionCd" index="idPk.atgTransactionCd" title =" Tx Group Type" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="desc" index="desc" title =" Description" width="40" sortable="false" editable="true" search="false"/>
			<sjg:gridColumn name="idPk.atgsGroupIdentifier" index="idPk.atgsGroupIdentifier" title =" Tx Group Indentifier" width="20" sortable="false" editable="true"/>
			<sjg:gridColumn name="atgsWhenCreated" index="atgsWhenCreated" title =" Transaction Group Create Year" width="10" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'Y'}"/>
			<sjg:gridColumn name="atgsNetDrCr" index="atgsNetDrCr" title =" Net Cash Debit/Credit" width="10" sortable="false" editable="true"/>
	
	</sjg:grid>
	<br/>
	<s:url id="alsSabhrsEntriesGridURL" action="alsAccount/alsSabhrsEntriesGrid_buildgrid" />
	<s:url id="alsSabhrsEntriesGridEditURL" action="alsAccount/alsSabhrsEntriesGridEdit_execute" />    
	<sjg:grid
		id="alsSabhrsEntriesTable"
		caption="Non Als SABHRS Entries"
		href="%{alsSabhrsEntriesGridURL}"
		editurl="%{alsSabhrsEntriesGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="false"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorEditOptions="{width:950,reloadAfterSubmit:false,
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
		formIds="gridFrm"
		reloadTopics="reloadAlsSabhrsEntriesTable"
		onCompleteTopics="alsSabhrsEntriesComplete">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:1}"/>
			<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:1}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" formoptions="{colpos:1,rowpos:2}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" formoptions="{colpos:2,rowpos:2}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:3}"  edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:3}" editoptions="{size:5,maxlength:4}"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:4}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:4}"/>
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true" formoptions="{colpos:1,rowpos:5}" editrules="{number:true,required:true}"/>
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true" edittype="select" formatter="select" editoptions="{value: {D: 'Debit', C: 'Credit'}}" editrules="{required:true}" formoptions="{colpos:2,rowpos:5}"/>
			<sjg:gridColumn name="idPk.aseSeqNo" index="idPk.aseSeqNo" title ="Seq No" width="10" sortable="false" editable="false"/>
			<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true" edittype="textarea" formoptions="{colpos:1,rowpos:6}"/>
	
	</sjg:grid>

	<s:url id="accMasterDialogURL" action="accMasterDialog_input"/>
	<sj:dialog 
    	id="accMasterDialog" 
    	href="%{accMasterDialogURL}"
    	autoOpen="false" 
    	modal="true" 
    	title="Non Als Sabhrs Master"
    	width="480" 
    	height="400"
    	buttons="{
       	    'Submit': function() {
       	    	exitNonAlsMasterDialog();
       	    },
       	    'Select All': function() {
               selectAll();
       	    },
       	    'Deselect All': function() {
               deselectAll();
       	    }
        }">
	</sj:dialog>

</fwp:template>