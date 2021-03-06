<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<form id='tmpCdLstFrm'>
	<s:hidden id="tmpCdLst" name="tmpCdLst"/>		
</form>

<s:url id="alsNonAlsDetailsGridURL" action="alsAccount/alsNonAlsDetailsGrid_buildgrid" />
<s:url id="alsNonAlsDetailsGridEditURL" action="alsAccount/alsNonAlsDetailsGridEdit_execute" />    
<sjg:grid
	id="alsNonAlsDetails"
	caption="Non ALS Details"
	href="%{alsNonAlsDetailsGridURL}"
	editurl="%{alsNonAlsDetailsGridEditURL}"		
	dataType="json"
	pager="true"
	navigator="true"
	navigatorEdit="true"
	navigatorView="true"
	navigatorAdd="true"
	navigatorDelete="true"
	navigatorSearch="false"
	navigatorRefresh="false"
	navigatorSearchOptions="{sopt:['cn','bw','eq','ne','lt','gt','ew'],multipleSearch:true}"
   	navigatorAddOptions="{width:600,reloadAfterSubmit:false,
   						  addedrow:'last',
   						  beforeSubmit:function(postData){
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
						  					prePopulate(this.id);
  	                      			return[true, ''];
	    	              },   
   	                      addCaption:'Add New Code Info',
   	                      closeAfterAdd:true,
   	                      processData:'Adding Row to Database'}"
   	navigatorEditOptions="{width:600,reloadAfterSubmit:false,
   	                       editCaption:'Edit Code Info',
   	                       beforeSubmit:function(postData){
	    	                    postData.provNo = $('#frmProvNo').val();
	    	                    postData.apbdBillingFrom = $('#frmBPFrom').val();
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
	rownumbers="false"
	editinline="false"
	viewrecords="true"
	scroll="true"
	scrollrows="true"
	height="75"
	width="910"
	rowNum="1000"
	formIds="subGridFrm"

	reloadTopics="reloadNonAlsDetailsGrid"
	onBeforeTopics="alsNonAlsDetailsComplete"
	onCompleteTopics="autoSelectTemplates">
		<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
		<sjg:gridColumn name="anatCd" index="anatCd" title="Code" width="10" sortable="true" editable="true" edittype="select" formatter="select" editoptions="{value:',',dataEvents: [{type: 'change', fn: function(e) 
																																								    {
																																								    	var index = this.selectedIndex;
																																								    	if(index != 0){
																																								    		var desc = this.options[this.selectedIndex].innerHTML.split('  -  ')[1].replace('amp;','');
																																								    		$('#anadDesc').val(desc);
																																								    		$('#anadDesc').prop({disabled:true});     
																																								    	}else{
																																								    		$('#anadDesc').val('');
																																								    		$('#anadDesc').prop({disabled:false});     
																																								    	}
																																								    }}] }"/>
		<sjg:gridColumn name="anadDesc" index="anadDesc" title="Description" width="10" sortable="true" editable = "true" editrules="{required:true}"/>
		<sjg:gridColumn name="anadAmount" index="anadAmount" title="Amount" width="10" sortable="true" editable = "true" align="right" formatter="number" formatoptions="{decimalPlaces: 2}" editrules="{number:true,required:true}" editoptions="{minValue:0}"/>
</sjg:grid>