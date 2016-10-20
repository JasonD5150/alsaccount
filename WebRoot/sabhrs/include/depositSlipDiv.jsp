<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
</script>

<s:form id="deleteSlipForm" action="uploadDiv_delete">
	<s:hidden id="delApbdsImageId" name="apbdsImageId"/>
</s:form>
										
<s:form id="uploadDivFrm" theme="simple" method="post" enctype="multipart/form-data">
	<s:hidden id="apbdsId" name="apbdsId"/>
	<s:hidden id="apbdId" name="apbdId"/>

	<div class="error"><s:actionerror theme="jquery"/></div>
	<div id="uploadMessage"><s:actionmessage theme="jquery"/></div>
	<div style="margin: 4px;">
		<div id="fileErrorMessage" hidden="true" style="color:red;">
			<p>
				File must be in pdf format and must not be larger than 12 MB. Please select a different file.
			</p>	
		</div>
		<table style="width:100%">
		<s:if test="%{apbds != null}">
			<s:if test="%{canEdit}">
				<tr>				
					<td class="label"nowrap>New File:</td>
					<td colspan="2" style="padding-right:10px"><s:file name="file" label="File" onchange="checkSizeFormat(this);"/></td>
				</tr>
			</s:if>
		</s:if>
		<s:else>
			<s:if test="%{canEdit}">
				<s:if test="%{canEdit && !provCom && !remApp}">
					<tr>				
						<td class="label"nowrap>File:</td>
						<td colspan="2" style="padding-right:10px"><s:file name="file" label="File" onchange="checkSize(this);"/></td>
					</tr>
				</s:if>
				<s:elseif test="canEdit && remApp">
					<tr>				
						<td>Deposit Slips cannot be uploaded when remittance is approved.</td>
					</tr>
				</s:elseif>
				<s:elseif test="canEdit && provCom">
					<tr>				
						<td>Deposit Slips cannot be uploaded when remittance is completed by provider.</td>
					</tr>
				</s:elseif>
			</s:if>
			<s:else>
				<tr>				
					<td>No deposit slip found.</td>
				</tr>
			</s:else>
		</s:else>
		</table>
		<s:if test="%{apbds != null}">
			<object data="<s:url action='DepositSlipAction?imageId=%{apbdsImageId}' ></s:url>" type="application/pdf" width="500" height="400"></object>
		</s:if>
	</div>
   </s:form>
   <sj:submit id="uploadSubmit" hidden="true" href="uploadDiv_execute.action" formIds="uploadDivFrm" targets="uploadDiv"/>
   <sj:submit id="deleteSubmit" hidden="true" href="uploadDiv_delete.action" formIds="uploadDivFrm" targets="uploadDiv"/>
   