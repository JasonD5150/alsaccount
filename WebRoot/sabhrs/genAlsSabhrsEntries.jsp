<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/> 
    </fwp:head>
    <style>
    	.autocompleter {
			padding-right:35px;
		}   	
		
		a.fwp-show-hide-section {
		   cursor: pointer;
		   color: blue;
		}
    </style>
    
	<script src="scripts/jquery.are-you-sure.js" type="text/javascript"></script>
    <script src="scripts/jquery.inputmask.bundle.min.js" type="text/javascript"></script>
	<script src="scripts/fwp.alsFormWidget.js"></script>
	<script src="/alsaccount/sabhrs/scripts/fwp.genAlsEntries.js"></script>
	
    <div id="errorMessage" style="font-weight:bold; color:#FF0000;" hidden="true"></div>
    <div id="genAlsSabhrsEntries">
    <s:hidden id="hidBudgYear" name="hidBudgYear"/>
    <s:hidden id="fundLst" name="fundLst"/>
	<s:hidden id="subClassLst" name="subClassLst"/>
	<s:hidden id="jlrLst" name="jlrLst"/>
	<s:hidden id="projectGrantLst" name="projectGrantLst"/>
	<s:hidden id="orgLst" name="orgLst"/>
	<s:hidden id="accountLst" name="accountLst"/>
	
	<form id="alsSabhrsEntriesGridForm">
		<s:hidden id="frmOper" name="oper"/>
		<s:hidden id="frmTransGrp" name="transGrp"/>
		<s:hidden id="frmTransIdentifier" name="transIdentifier"/>
		<s:hidden id="frmNonAlsEntries" name="templates"/>
	</form>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Generate ALS SABHRS Entries</h2>
   	</div>

	<fieldset style="border: black 1px solid; display: inline-block;margin: 0 5px;">
	   	<legend style="font-weight: bold;" class="fwp-exclude-from-show-hide">Search Criteria</legend>
	   	<form id='gridFrm'>
	   		<table>
   			<tr>			
				<td class="label">Budget Year: </td>
				<td class="autocompleter"><sj:autocompleter
									id="budgYear"
									name="budgYear"
									list="budgetYearSel"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
				<td class="label">Provider No:</td>
				<td class="autocompleter"><sj:autocompleter
									id="provNo"
									name="provNo"
									list="providerLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
		    </tr>
		    <tr>			
				<td class="label">Transaction Group Type: </td>
				<td class="autocompleter"><sj:autocompleter
									id="transGrpType"
									name="transGrpType"
									list="transGroupTypeLst"
									listValue="itemLabel"
									listKey="itemVal"
									selectBox="true"
									selectBoxIcon="true"
									forceValidOption="true"
									loadMinimumCount="2"
									theme="simple" 
									/></td>
				<td class="label">Transaction Group Identifier:</td>
				<td><s:textfield id="transGrpId" name="transGrpId" theme="simple" title="Transaction Group Identifier" /></td>
		    </tr>
		    </table>
		</form>	
	</fieldset>
	<br>
	<br>
	<fieldset style="border: black 1px solid; display: inline-block;">
    	<legend style="font-weight: bold;" class="fwp-exclude-from-show-hide">Actions</legend>
    	<button type="button" id="searchButton">Search</button>
    	<button type="button" id="resetButton">Reset</button>
	</fieldset>
	<br>
	<br>
   	<s:url id="transGroupDtlsGridURL" action="alsAccount/transGroupDtlsGrid_buildgrid" />
	<sjg:grid
		id="transGroupDtlsTable"
		caption="Transaction Groups Details"
		href="%{transGroupDtlsGridURL}"	
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="false"
		navigatorView="true"
		navigatorAdd="false"
		navigatorDelete="false"
		navigatorSearch="false"
	    navigatorViewOptions="{width:500,reloadAfterSubmit:false}"    	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="200"
		width="950"
		rowNum="1000"
		onSelectRowTopics="transGroupDtlsSelected"
		reloadTopics="reloadTransGroupDtlsTable"
		onCompleteTopics="transGroupDtlsComplete"
		formIds="gridFrm">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="idPk.atgTransactionCd" index="idPk.atgTransactionCd" title =" Tx Group Type" width="10" sortable="false" editable="true" align="right"/>
			<sjg:gridColumn name="desc" index="desc" title =" Description" width="40" sortable="false" editable="true" search="false"/>
			<sjg:gridColumn name="idPk.atgsGroupIdentifier" index="idPk.atgsGroupIdentifier" title =" Tx Group Indentifier" width="20" sortable="false" editable="true"/>
			<sjg:gridColumn name="atgsWhenCreated" index="atgsWhenCreated" title =" Transaction Group Create Year" width="10" sortable="false" editable="true" formatter="date" formatoptions="{srcformat:'y-m-d:H:i' , newformat : 'Y'}" align="right"/>
			<sjg:gridColumn name="atgsNetDrCr" index="atgsNetDrCr" title =" Net Cash Debit/Credit" width="10" sortable="false" editable="true" align="right" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
	
	</sjg:grid>
	<br/>
	<s:url id="alsSabhrsEntriesGridURL" action="alsAccount/genAlsSabhrsEntriesGrid_buildgrid" />
	<s:url id="alsSabhrsEntriesGridEditURL" action="alsAccount/genAlsSabhrsEntriesGridEdit_execute" />    
	<sjg:grid
		id="alsSabhrsEntriesTable"
		caption="ALS SABHRS Entries"
		href="%{alsSabhrsEntriesGridURL}"
		editurl="%{alsSabhrsEntriesGridEditURL}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="false"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearch="false"
		navigatorDeleteOptions="{delData : {transIdentifier : function () { return $('#frmTransIdentifier').val();},
											transGrp: function () { return $('#frmTransGrp').val();}},
								 afterSubmit:function (postData) {
    						  		$.publish('reloadTransGroupDtlsTable');
    						  		return[true, ''];
    						  	 }}"
		navigatorAddOptions="{width:950,reloadAfterSubmit:true,
    						  addedrow:'last',
 	    					  afterShowForm: function ($form) {
 	    					  	$('#aamBusinessUnit').val('52010');
							    $('#asacProgram').val($('#hidBudgYear').val());
							    $('#asacBudgetYear').val($('#hidBudgYear').val());
                    			$form.closest('.ui-jqdialog').position({
                        			my: 'center',
                        			at: 'center',
        							of: $('#transGroupDtlsTable').closest('div.ui-jqgrid')
                    			});
                    		  },
                    		  beforeSubmit: function (postData) {
    						  		postData.transIdentifier = $('#frmTransIdentifier').val();
    						  		postData.transGrp = $('#frmTransGrp').val();
    						  		return[true, ''];
    						  }, 
    						  afterSubmit:function (postData) {
    						  		$.publish('reloadTransGroupDtlsTable');
    						  		return[true, ''];
    						  },  
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
		navigatorEditOptions="{width:950,reloadAfterSubmit:true,
    	                       editCaption:'Edit Code Info',    
    	                       closeAfterEdit:true,
    	                       beforeSubmit: function (postData) {
    						  		postData.transIdentifier = $('#frmTransIdentifier').val();
    						  		postData.transGrp = $('#frmTransGrp').val();
    						  		return[true, ''];
    						  },
    	                       afterSubmit:function (postData) {
    						  		$.publish('reloadTransGroupDtlsTable');
    						  		return[true, ''];
    						  }, 
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
	    navigatorExtraButtons="{
	                            viewbutton: {title : 'Add Template', icon: 'ui-icon-circle-plus', caption : 'Add Template', onclick: function(){ $('#accMasterDialog').dialog('open');}}
	                        	}"   	
	    gridModel="model"
		rownumbers="true"
		viewrecords="true"
		scroll="true"
		scrollrows="true"
		height="100"
		width="950"
		rowNum="1000"
		formIds="alsSabhrsEntriesGridForm"
		reloadTopics="reloadAlsSabhrsEntriesTable"
		onCompleteTopics="alsSabhrsEntriesComplete">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" editoptions="{size:5,maxlength:4}" formoptions="{colpos:1,rowpos:1}" align="right"/>
			<sjg:gridColumn name="jlr" index="jlr" title ="JLR" width="10" sortable="false" editable="true" formoptions="{colpos:2,rowpos:1}" edittype="select" formatter="select" editoptions="{value:','}"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true" editrules="{number:true,required:true}" formoptions="{colpos:1,rowpos:2}" edittype="select" formatter="select" editoptions="{value:','}" align="right"/>
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
	<script type="text/javascript">
        $("div#genAlsSabhrsEntries").genAlsSabhrsEntries();
    </script>
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
       	    	$('#genAlsSabhrsEntries').genAlsSabhrsEntries('exitNonAlsMasterDialog');
       	    },
       	    'Select All': function() {
               $('#genAlsSabhrsEntries').genAlsSabhrsEntries('selectAll');
       	    },
       	    'Deselect All': function() {
               $('#genAlsSabhrsEntries').genAlsSabhrsEntries('deselectAll');
       	    }
        }">
	</sj:dialog>
</fwp:template>
