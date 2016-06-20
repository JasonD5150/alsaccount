<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template>
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
	   	<script type="text/javascript">
	   		$.subscribe('transGroupDtlsSelected', function(event, data) {				
				var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');					    				  
	      		var atgTransCd = $("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgTransactionCd');
	      		var atgTransIdentifier = $("#transGroupDtlsTable").jqGrid('getCell', sel_id, 'idPk.atgsGroupIdentifier');
				$('#frmTransGrp').val(atgTransCd);
				$('#frmTransIdentifier').val(atgTransIdentifier);
				
				
				$("#alsSabhrsEntriesTable").jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadAlsSabhrsEntriesTable');
			});
			
			$.subscribe('test', function(event, data) {
			alert("complete")
				$('#alsNonAlsTemplateTable').jqGrid('setGridParam',{datatype:'local'});
			});
			
			function selectAll(){
				//alert("select");
				var grid = $("#alsNonAlsTemplateTable");
			    var rows = grid.jqGrid("getDataIDs");
			
			    for (i = 0; i < rows.length; i++)
			    {
			        var rowData = grid.jqGrid("getRowData", rows[i]);
					$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',1);
			    }
			}
			
			function deselectAll(){
				//alert("deselect");
				var grid = $("#alsNonAlsTemplateTable");
			    var rows = grid.jqGrid("getDataIDs");
			
			    for (i = 0; i < rows.length; i++)
			    {
			        var rowData = grid.jqGrid("getRowData", rows[i]);
					$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',0);
			    }
			}
			
			function exitNonAlsMasterDialog(){				
				var rtn = true;
				var grid = $("#alsNonAlsTemplateTable");
			    var rows = grid.jqGrid("getDataIDs");
			    var selectedList = "";
			    
			    
			
			    for (i = 0; i < rows.length; i++)
			    {
			        var selected = grid.jqGrid ('getCell', rows[i], 'selected');
			        var amount = grid.jqGrid ('getCell', rows[i], 'amount');
			        var anatCd = grid.jqGrid ('getCell', rows[i], 'idPk.anatCd');
			        if(selected == 1 && amount <= 0){
			        	selectList = "";
			        	rtn = false;
			        }else if(selected == 1 && amount > 0){
			        	selectedList = selectedList + anatCd +",";
			        }
			    } 
			    
			    if(rtn){
			    	alert(selectedList);
			    	$('#accMasterDialog').dialog('close');
			    }else{
			    	alert("Amount must be entered.");
			    }
			}
		</script>
    </fwp:head>
	
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
			<sjg:gridColumn name="desc" index="desc" title =" Description" width="40" sortable="false" editable="true"/>
			<sjg:gridColumn name="idPk.atgsGroupIdentifier" index="idPk.atgsGroupIdentifier" title =" Tx Group Indentifier" width="20" sortable="false" editable="true"/>
			<sjg:gridColumn name="createYear" index="createYear" title =" Transaction Group Create Year" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="atgsNetDrCr" index="atgsNetDrCr" title =" Net Cash Debit/Credit" width="10" sortable="false" editable="true"/>
	
	</sjg:grid>
	

	<form id='gridFrm'>
		<s:hidden id="frmTransGrp" name="transGrp"/>
		<s:hidden id="frmTransIdentifier" name="transIdentifier"/>
	</form>

	<input id="openAccMasterDialog" type="button" value="Non Als Master" onclick="$('#accMasterDialog').dialog('open');">

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
		height="50"
		width="950"
		rowNum="1000"
		formIds="gridFrm"
		reloadTopics="reloadAlsSabhrsEntriesTable">
			
			<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="asacBudgetYear" index="asacBudgetYear" title ="Budget Year" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacReference" index="asacReference" title ="JLR" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aamAccount" index="aamAccount" title ="Account" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aamFund" index="aamFund" title ="Fund" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aocOrg" index="aocOrg" title ="Org" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacProgram" index="asacProgram" title ="Program" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacSubclass" index="asacSubclass" title ="Subclass" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aamBusinessUnit" index="aamBusinessUnit" title ="Business Unit" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacProjectGrant" index="asacProjectGrant" title ="Project Grant" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aseAmt" index="aseAmt" title ="Amount" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacSystemActivityTypeCd" index="asacSystemActivityTypeCd" title ="Sys Activity Type Code" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="asacTxnCd" index="asacTxnCd" title ="Transaction Code" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="idPk.aseDrCrCd" index="idPk.aseDrCrCd" title ="Dr/Cr Code" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="idPk.aseSeqNo" index="idPk.aseSeqNo" title ="Seq No" width="10" sortable="false" editable="true"/>
			<sjg:gridColumn name="aseLineDescription" index="aseLineDescription" title ="Line Desc" width="40" sortable="false" editable="true"/>
	
	</sjg:grid>

	
	<s:url id="accMasterDialogURL" action="accMasterDialog_input"/>
	<sj:dialog 
    	id="accMasterDialog" 
    	href="%{accMasterDialogURL}"
    	autoOpen="false" 
    	modal="true" 
    	title="Non Als Sabhrs Master"
    	width="480" 
    	height="550">
	</sj:dialog>

</fwp:template>
