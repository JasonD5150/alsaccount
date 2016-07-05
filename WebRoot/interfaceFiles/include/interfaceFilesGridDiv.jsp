<!--  WebRoot/tech/include -->
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

 <div style="margin:4px;">
    
    <s:url id="interfaceFilesGridUrl"  namespace="/alsInterface" action="interfaceFilesGrid_buildgrid" />
    <s:url id="interfaceFilesGridEditUrl" namespace="/alsInterface" action="interfaceFilesGridEdit_execute" />
    <sjg:grid
        id="interfaceFilesGrid" 
        caption="Interface Files" 
        dataType="json"
        formIds="mainForm"
        editinline="false"
        gridModel="gridModel"
        height="400" 
        href="%{interfaceFilesGridUrl}" 
        editurl="%{interfaceFilesGridEditUrl}" 	 
        loadonce="true"
        navigator="true"
        navigatorAdd="false"
        navigatorDelete="false"
        navigatorEdit="true" 
        navigatorRefresh="false" 
        navigatorSearch="false"
        navigatorView="true" 
        pager="true" 
        reloadTopics="reloadInterfaceFilesGrid"
        onCompleteTopics="gridFinish"
        rowNum="1000" 
        rownumbers="false" 
        scroll="true" 
        scrollrows="true"
        shrinkToFit="true" 
        viewrecords="true" 
        autowidth="true" 
        navigatorEditOptions="{width:700,
                               editCaption:'Edit Interface File',
                               closeAfterEdit:true,
                               afterSubmit: function(response, postdata){
                             	    $(this).jqGrid('setGridParam',{datatype:'json'}).trigger('reloadInterfaceFilesGrid');
			                        return serverErrorHandler(response, postdata);},
                               processData:'Updating Interface File'}"	
        navigatorViewOptions="{width:700,
                               caption:'View Record',
                               reloadAfterSubmit:false}"
	>    
		<sjg:gridColumn name="aifId" index="aifId" title="Id" sortable="false" hidden="true" editable="false" key="true" width="30" />
	    <sjg:gridColumn name="aifFileName" index="aifFileName" title="File Name" sortable="true" hidden="false"   editable="false" width="200"/>
	    <sjg:gridColumn name="aifAwgcId" index="aifAwgcId" title="File Type" sortable="true" hidden="false" edittype="select" formatter="select" editoptions="{value: rtrnFileTypeList()}"  editable="false" width="200" />
	    <sjg:gridColumn name="aifCreateDate" index="aifCreateDate" title="Date Created" sortable="true" hidden="false"   editable="false" width="200" formatter="date" sorttype="datetime"
	    						formatoptions="{newformat : 'm/d/Y  h:i:s', srcformat : 'Y/m/d h:i:s'}"/>
	    <sjg:gridColumn name="aifOrigfileId" index="aifOrigfileId" title="Original File Name and Date" sortable="true" hidden="false"   editable="false" width="300"  formatter="select" edittype="select" editoptions="{value: rtrnFileNameList()}"/>
	    <sjg:gridColumn name="aifParentProcess" index="aifParentProcess" title="Parent Process" sortable="true" hidden="false"   editable="false" width="150" />
	    <sjg:gridColumn name="aifSendFlag" index="aifSendFlag" title="Flag to Send" sortable="true" hidden="false" editable="true" width="50" edittype="checkbox" editoptions="{value:'Y:N'}"/>
	    <sjg:gridColumn name="aifFileSent" index="aifFileSent" title="File Sent" sortable="true" hidden="false"   editable="false" width="50" />
	    <sjg:gridColumn name="aifProcessDate" index="aifProcessDate" title="Date Processed" sortable="true" hidden="false"   editable="false" width="200" formatter="date" sorttype="datetime"
	    						formatoptions="{newformat : 'm/d/Y  h:i:s', srcformat : 'Y/m/d h:i:s'}"/>	    
	    <sjg:gridColumn name="aifNotes" index="aifNotes" title="Notes" sortable="true" hidden="false" editable="true" width="200"  edittype="textarea" editoptions="{maxLength:500, rows:'4', cols:'40'}" editrules="{edithidden: true}"/>
		<sjg:gridColumn name="aifFile" index="aifFile" title="File" sortable="false" hidden="true" editable="true" width="30" edittype="textarea" editoptions="{maxLength:2000, rows:'5', cols:'150'}" editrules="{edithidden: true}"/>
	</sjg:grid>   
	<script>

	</script> 
</div>

	