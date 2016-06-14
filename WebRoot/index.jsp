<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>



<fwp:template >
  <fwp:head>
    <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="css/jquery"/>
    <script>
	  function refreshDocs() {
	    $('#delLandDiv').remove();
	    $.publish('reloadLanding');
	  }
	</script>	
		
  </fwp:head>

   <c:if test="${authorizationFailure !=null}"><br/><br/>
   		<span style="color:red; font-weight:bold; font-size:medium">${authorizationFailure}</span>
   </c:if>		    
       
    <div style="width:800px" align="center">
    	<h1>ALS Accounting</h1>
   	</div>
  
    <s:url id="landingURl" value="../fwpCommon/landingAct_input.action"/>
    <sj:div id="landingDiv" 
    		href="%{landingURl}" 
    		reloadTopics="reloadLanding" >
   </sj:div>    
  
  </fwp:template>
