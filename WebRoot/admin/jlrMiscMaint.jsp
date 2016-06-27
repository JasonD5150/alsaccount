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
	   	<script type="text/javascript">
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
			
			function setGridFrm(){
		    	$('#frmBudgYear').val($('#budgetYearSel').val());
	    	}
	    	
			function reloadGrids(event,data) {
				setGridFrm();
				$.publish('reloadGrids');
			}
		</script>
    </fwp:head>
	
	<form id='rptFrm'>
			<s:hidden id="frmRptType" name="rptType" />
			<s:hidden id="frmFilters" name="filters" />
	</form>
	
    <div style="width:800px; text-align:center">
    	<h2 class="title">Miscellaneous Maintenance: Non-ALS Journal Line Reference</h2>
   	</div>
   
	<s:url id="jlrMiscMaintGrid" action="alsAccount/jlrMiscMaintGrid_buildgrid" />
	<s:url id="jlrMiscMaintGridEdit" action="alsAccount/jlrMiscMaintGridEdit_execute" />
   
	<sjg:grid
		id="jlrMiscMaintTable"
		caption="Als JLR Misc"
		href="%{jlrMiscMaintGrid}"
		editurl="%{jlrMiscMaintGridEdit}"		
		dataType="json"
		pager="true"
		navigator="true"
		navigatorEdit="true"
		navigatorView="true"
		navigatorAdd="true"
		navigatorDelete="true"
		navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
    	navigatorAddOptions="{width:600,reloadAfterSubmit:true,
    						  addedrow:'last',
    						  afterSubmit:errorHandler, 	    
    	                      addCaption:'Add New Code Info',
    	                      closeAfterAdd:true,
    	                      processData:'Adding Row to Database'}"
    	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
    	                       editCaption:'Edit Code Info',
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
		height="400"
		width="950"
		rowNum="500" 
		resizable="true">

			<sjg:gridColumn name="amSeqNo" title ="amSeqNo" width="55" hidden="true" key="true"/>
			<sjg:gridColumn name="amValDesc" index="amValDesc" title ="JLR ID" width="10" sortable="false" hidden="false" editable="true" editrules="{required:true}"/>
			<sjg:gridColumn name="amDesc2" index="amDesc2" title ="JLR Description" width="100" sortable="false" hidden="false" editable="true" edittype="textarea" editrules="{required:true}"/>
			<sjg:gridColumn name="amParVal" index="amParVal" title ="JLR Seq No" width="15" sortable="false" hidden="false" editable = "true" editrules="{number:true,required:true,minValue:0,maxValue:999}"/>
			
	</sjg:grid>


				
</fwp:template>
