<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/fwp/tags" prefix="fwp" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="sjg" uri="/struts-jquery-grid-tags" %>
<%@ taglib prefix="sf" uri="/struts-tags" %>

<fwp:template loadJquery="false" useFwpJqueryUI="true">
    <fwp:head>
        <sj:head locale="en" jqueryui="true" jquerytheme="smoothness" customBasepath="/css/jquery" compressed="true"/>
        <style type="text/css">
            @import url("/alsaccount/css/alsaccount.css");
        </style>
    </fwp:head>

    <script src="scripts/jquery.are-you-sure.js" type="text/javascript"></script>
    <script src="scripts/jquery.inputmask.bundle.min.js" type="text/javascript"></script>

    <script src="scripts/fwp.alsFormWidget.js"></script>
    <script src="scripts/exportGrid.js"></script>
    <script src="/alsaccount/refund/scripts/fwp.massRefundApproval.js"></script>

    <sf:url id="massApprovalActionUrl" action="alsAccount/personRefundAppGrid_buildgrid"/>
    <sf:url id="remoteurl" action="buildMassRefundApprovalGrid"/>

    <div id="massRefundApproval">
        <h2 class="title">Mass Refund Approval</h2>

        <sf:form action="massRefundApproval" id="massRefundApprovalForm">
            <label for="applicationTypeCode">Application Type:</label>
            <sj:select id="applicationTypeCode"
                       name="applicationTypeCode"
                       list="preappTypeList"
                       listKey="itemVal"
                       listValue="itemLabel"/>
            <label for="dispositionCode">Disposition:</label>
            <sj:select id="dispositionCode"
                       name="dispositionCode"
                       list="dispositionList"
                       listKey="itemVal"
                       listValue="itemLabel"/>
            <label for="dispositionCode">Refund Reason:</label>
            <sj:select id="refundReasonCode"
                       name="refundReasonCode"
                       list="refundReasonList"
                       listKey="itemVal"
                       listValue="itemLabel"/>
        </sf:form>
        <fieldset style="border: black 1px solid; display: inline-block;">
            <legend style="font-weight: bold;" class="fwp-exclude-from-show-hide">Actions</legend>
            <button type="button" id="searchButton">Search</button>
            <button type="button" id="resetButton">Reset</button>
            <button type="button" id="exportButton">Export CSV</button>
        </fieldset>

        <h2>Application Items</h2>
        <sjg:grid
                id="applicationItemGrid"
                caption="Application Items"
                dataType="json"
                href="%{remoteurl}"
                pager="true"
                navigator="true"
                navigatorEdit="false"
                navigatorView="false"
                navigatorAdd="false"
                navigatorDelete="false"
                navigatorExtraButtons="{
                                editpage: { title : 'Edit Selected Row', icon: 'ui-icon-pencil', topic: 'applicationInformationEditRow'},
                                seperator: { title : 'seperator' },
                                columnsbutton : { title : 'Add/Remove Columns', icon: ' ui-icon-extlink', topic: 'applicationInformationAddRemoveColumns'}
                            }"
                navigatorSearch="true"
                navigatorSearchOptions="{multipleSearch:true}"
                gridModel="model"
                rownumbers="true"
                editinline="false"
                shrinkToFit="false"
                autowidth="true"
                onSelectRowTopics="applicationItemRowSelect"
                onSelectAllTopics="applicationItemSelectAllRows"
                onGridCompleteTopics="applicationItemGridComplete"
                reloadTopics="popGrid"
                loadonce="true"
                viewrecords="true"
                scroll="true"
                scrollrows="true"
                height="350"
                rowNum="500"
                resizable="true"
                onErrorTopics="applicationItemGridError"
                formIds="massRefundApprovalForm"
                multiselect="true">
            <sjg:gridColumn name="idPk.aptAppTypeCd"
                            index="idPk.aptAppTypeCd"
                            title="App Type Code"
                            sortable="true"
                            sorttype="integer"
                            hidden="true"
                            width="60"/>

            <sjg:gridColumn name="appTypeCodeDescription"
                            index="appTypeCodeDescription"
                            title="App Type"
                            sortable="true"
                            sorttype="text"
                            hidden="true" width="200"/>

            <sjg:gridColumn name="idPk.aptDataEntry"
                            index="idPk.aptDataEntry"
                            title="ALS No"
                            sortable="true"
                            sorttype="integer"
                            hidden="true"
                            width="60"/>

            <sjg:gridColumn name="idPk.apitlItemTypeCd"
                            index="idPk.apitlItemTypeCd"
                            title="Item Type Code"
                            sortable="true"
                            sorttype="text"
                            hidden="false"
                            width="50"/>
            <sjg:gridColumn name="itemTypeCodeDescription"
                            index="itemTypeCodeDescription"
                            title="Item Type"
                            sortable="true"
                            sorttype="text"
                            hidden="false"
                            width="200"/>

        </sjg:grid>

    </div>
    <script type="text/javascript">
		$(function () {
			$("div#massRefundApproval").alsMassRefundApproval();
		});
    </script>


</fwp:template>
