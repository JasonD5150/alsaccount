<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/fwp/tags" prefix="fwp"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>

<script type="text/javascript">
$('#addItemDialog').dialog('close');
$.publish('reloadTribeBankItemDiv');
//$("input.edit").attr("disabled", false);
</script>
	<s:actionmessage theme="jquery"/>
