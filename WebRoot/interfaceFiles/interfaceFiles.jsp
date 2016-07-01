<!-- webroot/interfaceFiles -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true" >
    <fwp:head> 
	    <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>	
  		<style>
	  		th.ui-th-column div{
			    white-space:normal !important;
    			height:auto !important;
    			padding:2px;
			}
			
			div.ui-jqdialog-content td.form-view-label {
 			    white-space: normal !important;
    			height: auto;
    			vertical-align: middle;
    			padding-top: 2px; padding-bottom: 2px
			}
  		</style>
  		<script src='scripts/fieldEdits.js' type='text/javascript'></script> 
	    <script type="text/javascript">
		    function serverErrorHandler(response, postdata,gridName,reloadName) {
			    var success = true; 
			    var message = ""; 	            
			    var json = eval('(' + response.responseText + ')');
			    if(json.actionErrors) {
			        success = false;
			        for(i=0; i < json.actionErrors.length; i++) {
			            message += json.actionErrors[i] + '<br/>'; 
			        }             
			    } 
			    return [success,message]; 
			} 

		    function submitSearch() {   
			    jQuery("#interfaceFilesGrid").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadInterfaceFilesGrid');
	 		}   
	 		
	 		function resetPage(){
			   $("#searchAifAwgcId").val('-1');
			   $("#searchAifFileName").val('');
			   $("#createDateFrom").val('');
			   $("#createDateTo").val('');
			   $("#processDateFrom").val('');
			   $("#processDateTo").val('');
			   $("#searchAifFileSent").val('');
			           
	           $(".actionError").hide();   
	           $(".actionMessage").hide();   
	           jQuery("#interfaceFilesGrid").jqGrid('clearGridData');
	        }
	        
	        function rtrnFileTypeList(){
	        	var fileTypeListTxt = "";
				var selObj = document.getElementById('searchAifAwgcId');
				
				for(var i=0; i<selObj.length-1; i++){
  					fileTypeListTxt = fileTypeListTxt + selObj.options[i].value + ":" + selObj.options[i].text + ";";
				}
				if(selObj.length > 0){
					fileTypeListTxt = fileTypeListTxt + selObj.options[selObj.length - 1].value + ":" + selObj.options[selObj.length - 1].text;
				}
				return fileTypeListTxt;
	        }
	        
	        function rtrnFileNameList(){
	        	return $('#fileNameListTxt').val();
	        }
	        
	        function exportFile(){	  
	        	$('#fileExportFrm').attr("action","interfaceFiles_exportFile.action");
	        	$('#fileExportFrm').submit();
	        }
	    </script>
    </fwp:head>    
	<div id="Home" style="width:100%">
		<s:actionerror theme="jquery"/>
		<s:actionmessage theme="jquery"/>
		
		<fieldset style="width: 98%; display: inline-block;">
			<legend style="font-weight: bold;font-size:larger">Search Interface Files</legend>
			<s:form id="mainForm" theme="simple">
				<s:hidden name="fileNameListTxt" id="fileNameListTxt"/>
				<table style="width:100%">
					<tr>
						<td style="text-align:left; vertical-align:middle"><i>File Type:</i></td>
						<td style="padding-right:5px">
							<s:select
	                       		id="searchAifAwgcId"
	                       		name="iFile.aifAwgcId" 
	                       		label="File Type" 
	                       		headerKey="-1"
	                       		headerValue="-- Select One --" 	                       
	                       		list="fileTypeList"
	                       		listKey="itemVal"
                           		listValue="itemLabel"/></td>
						<td style="text-align:left; vertical-align:middle"><i>File Name:</i></td>
						<td style="padding-right:5px"><s:textfield id="searchAifFileName" name="iFile.aifFileName"  cssStyle="text-transform: uppercase; width:300px" title="File Name" maxlength="55" /></td>
                    </tr>
                    <tr>
						<td style="text-align:left; vertical-align:middle"><i>Date Created:</i></td>
						<td style="padding-right:5px">						
						<table>
							<tr>
								<td style="text-align:right; padding-right:10px">After:</td>
								<td style="text-align:right;padding-right:10px">
									<sj:datepicker changeMonth="true" changeYear="true" id="createDateFrom"
									name="createDateFrom" displayFormat="mm/dd/yy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Created" showOn="focus"
									onblur="testDate(this)" />
								</td>					
								<td style="text-align:right; padding-right:10px">Before:</td>
								<td style="text-align:right;padding-right:10px">
									<sj:datepicker changeMonth="true" changeYear="true" id="createDateTo"
									name="createDateTo" displayFormat="mm/dd/yy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Created" showOn="focus" 
									onblur="testDate(this)" />
								</td>						
							</tr>
						</table></td>
						<td style="text-align:left; vertical-align:middle"><i>Date Processed:</i></td>
						<td style="padding-right:5px">						
						<table>
							<tr>
								<td style="text-align:right; padding-right:10px">After:</td>
								<td style="text-align:right;padding-right:10px">
									<sj:datepicker changeMonth="true" changeYear="true" id="processDateFrom"
									name="processDateFrom" displayFormat="mm/dd/yy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Processed" showOn="focus"
									onblur="testDate(this)" />
								</td>					
								<td style="text-align:right; padding-right:10px">Before:</td>
								<td style="text-align:right;padding-right:10px">
									<sj:datepicker changeMonth="true" changeYear="true" id="processDateTo"
									name="processDateTo" displayFormat="mm/dd/yy"
									cssStyle="width:80px" maxlength="10"  maxDate="+0" title="Date Processed" showOn="focus" 
									onblur="testDate(this)" />
								</td>						
							</tr>
						</table></td>					
					</tr>
					<tr>
                        <td style="text-align:left; vertical-align:middle"><i>File Sent:</i></td>
                        <td style="padding-right:5px">
							<s:select
	                       		id="searchAifFileSent"
	                       		name="iFile.aifFileSent" 
	                       		label="File Type" 
	                       		headerKey=""
	                       		headerValue="-- Select One --" 	                       
	                       		list="#{'Y':'Yes','N':'No'}"
	                        /></td>
					</tr>
				</table>	
			</s:form>
		</fieldset>
		<div style="width=100%;padding-top:5px;clear:both">
		    <sj:a button="true" cssClass="buttonNav" onclick="submitSearch()">Search</sj:a>&nbsp;&nbsp; 
		    <sj:a button="true" cssClass="buttonNav" onclick="resetPage()">Reset</sj:a>
	    </div>
		
		<s:url id="interfaceFilesGridDivUrl" value="interfaceFilesGridDiv_input.action" />
		<sj:div id="interfaceFilesGridDiv" style="margin:4px" href="%{interfaceFilesGridDivUrl}" reloadTopics="reloadInterfaceFilesGridDiv"/>            
	</div>
</fwp:template>