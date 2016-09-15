<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<fwp:template>
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
        <style type="text/css">
			@import url("/alsaccount/css/alsaccount.css");
	    </style>
	   	<script type="text/javascript">
	   		function upload() {			
				$('#rtnHtml').html('');
			
				$.ajax({
					type : "POST",
					cache : false,
					url : 'uploadSabhrsToSumJson.action',
					success : function(data) {
						if (data.rtrn.procStatus === 'SUCCESS') {
							$('#rtnHtml').html('<p style="color:blue;font-size:14px"><b>' + data.rtrn.procMsg + '</b></p>');
						} else {
							$('#rtnHtml').html('<p style="color:red;font-size:14px"><b>' + data.rtrn.procMsg + '</b></p>');
						}
					},
					complete : function() {
						// Handle the complete event
					},
					error : function(x, e) {
						ajaxErrorHandler(x, e, "Fiscal Year End", "Unexpected Process Error", "actionStsHtml");
					}
				});
			}
		</script>
    </fwp:head>
	
    <div style="width:800px;text-align:center">
    	<h2 class="title">Upload SABHRS Entries into Summary</h2>
   	</div>
   
   	<div id ='rtnHtmlDiv'>
		<span id='rtnHtml'></span>
	</div>
	
   	<h5> This process will upload the SABHRS entries created today to the Summary for review prior to the nighttime batch job.
	</h5>
	<input id='uploadSabhrs' type='button' value='Submit' onclick='upload()'>
	<input type="submit" value="Cancel"  onclick="window.location='index_input.action';" />  
	 

</fwp:template>
