<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
</script>

<s:form id="uploadDlgFrm">
	<s:hidden id="remittanceId" name="remittanceId"/>
	<s:hidden id="apbdId" name="apbdId"/>
</s:form>

<s:url id="uploadDivUrl" action="uploadDiv_input"/>
<sj:div 
	id = "uploadDiv"
	href = "%{uploadDivUrl}"
	reloadTopics = "reloadUploadDiv"
	formIds = "uploadDlgFrm"/> 