<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>


<h5> To add a template select the check box and enter an amount.<br/> 
</h5>
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
	width="400"
	autowidth="true"
	rowNum="1000">

		<sjg:gridColumn name="gridKey" title ="id" width="55" hidden="true" key="true"/>
		<sjg:gridColumn name="idPk.anatBudgetYear" index="idPk.anatBudgetYear" title =" Budget Year" width="25" sortable="false" hidden="true"/>
		<sjg:gridColumn name="idPk.anatCd" index="idPk.anatCd" title =" Template Code" width="15" sortable="false" hidden="false" editable="false" editrules="{required:true}"/>
		<sjg:gridColumn name="anatDesc" index="anatDesc" title ="Description" width="55" sortable="false" editable="false" edittype="textarea" editrules="{required:true}"/>
		
		<sjg:gridColumn name="amount" index="amount" title ="Amount" width="15" sortable="true" editable = "true" editrules="{number:true}" formatter= "number" formatoptions="{decimalPlaces: 2}"/>
		<sjg:gridColumn name="selected" index="selected" title ="Select" width="15" sortable="true" align="center" editable = "true" edittype="checkbox" editoptions="{ value: '1:0' }" formatter= "checkbox" formatoptions="{disabled : false}"/>
		
</sjg:grid>	
<div id ='htmlDiv'>
	<span id='html'></span>
</div>
