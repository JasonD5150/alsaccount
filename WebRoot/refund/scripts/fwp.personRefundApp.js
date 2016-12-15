function errorHandler(response, postdata) {
    rtrnstate = true; 
    rtrnMsg = ""; 
	json = eval('(' + response.responseText + ')'); 
	if(json.actionErrors) {
		rtrnstate = false; 
	    for(i=0; i < json.actionErrors.length; i++) {
	    	rtrnMsg += json.actionErrors[i] + '<br/>'; 
		} 
	} 
	return [rtrnstate,rtrnMsg]; 
}
/**
 *
 * jQuery UI widget to provide behavior for the Person Information search page.
 *
 * @author cfa027
 */
(function ($) {
	$.widget("fwp.personRefundApp", $.fwp.alsFormWidget, {
		options: {},
		_create: function () {
			this._setOptions({ "dirtyCheck": "silent", "allowRadiosOff": true });
			this._super();			
			this._wireClickHandlers();
			this._wireEventHandlers();
			this._setGridSelectLists();
			widthFunction();
		},
		_wireClickHandlers: function () {
			var self = this, _editForm;
			this.element.find("button#searchButton").on("click", function () {
				self._handleSearch();
			});
			this.element.find("button#resetButton").on("click", function () {
				window.location.reload(true);
			});
			this.element.find("button#exportButton").on("click", function () {
				$.ajax({
					type: "POST",
					data: JSON.stringify(exportGrid("personRefundGrid","refundRecords","gridFrm")),
					dataType: "json",
					cache: false,
					contentType: "application/json",
					url: 'personRefundGridToCsv.action',
					success: function (data) {
						window.location = "downloadCsv.action?csvFileName=" + data.csvFileName+"&fileName="+data.fileName;
					}, complete: function () {
						$.unblockUI();
					},
				 	error: function (x, e) {
						ajaxErrorHandler(x, e, "Save", null, null);
					} 
				});
			});
		},
		_wireEventHandlers: function () {
			var self = this;
			$.subscribe("personRefundAddRemoveColumns", function (event, data) {
				self._jqGrid.jqGrid("columnChooser",{
					width: 500
				});
			});
			$.subscribe("personRefundGridComplete", function (event, data) {
				self._jqGrid = $(data);
				var error = self._jqGrid.jqGrid('getGridParam', 'userData');
	    		if (error != null && error.length > 0) {
	    			$("#errorMessage").html(error);
					$("#errorMessage").dialog({
						title:"Search Error",
					    resizable: false,
					    height:"auto",
					    modal: true,
					    buttons: {
			                "Ok": function() {
			                    $(this).dialog("close");
			                 }
			             }
					});
	    		}
			});
			$.subscribe("personRefundRowSelected", function (event, data) {
				self._jqGrid = $(data);
				var sel_id = self._jqGrid.jqGrid('getGridParam','selrow'); 
	    		$('#downloadDt').val(self._jqGrid.jqGrid('getCell', sel_id, 'downloadDate'));
	    		$('#apiDob').val(self._jqGrid.jqGrid('getCell', sel_id, 'designatedRefundeeDob'));
	    		$('#apiAlsNo').val(self._jqGrid.jqGrid('getCell', sel_id, 'ariDesignatedRefundeeAlsNo'));
	    		if($('#downloadDt').val().length > 1 &&
	    		   $('#apiDob').val().length > 1 &&
	    		   $('#apiAlsNo').val().length > 0){
	    			//$('#edit_personRefundGrid').hide();
	    			$('#warrantStatusGrid').jqGrid('setGridParam',{datatype:'json'});
		    		$.publish('reloadWarrantStatusGrid');
	    		}else{
	    			//$('#edit_personRefundGrid').show();
	    			$('#warrantStatusGrid').jqGrid('clearGridData');
	    		}
			});
			$.subscribe("warrantStatusGridComplete", function (event, data) {
				self._jqGrid = $(data);
				var error = self._jqGrid.jqGrid('getGridParam', 'userData');
	    		if (error != null && error.length > 0) {
	    			$("#errorMessage").html(error);
					$("#errorMessage").dialog({
						title:"Search Error",
					    resizable: false,
					    height:"auto",
					    modal: true,
					    buttons: {
			                "Ok": function() {
			                    $(this).dialog("close");
			                 }
			             }
					});
	    		}
			});
		},
		_setGridSelectLists: function () {
			var self = this;			
			self._jqGrid = $('#warrantStatusGrid');
			self._jqGrid.jqGrid('setColProp','awsPaymentStatus', { editoptions: { value: $("#paymentStatusLst").val()}});
			self._jqGrid.jqGrid('setColProp','awsCancelAction', { editoptions: { value: $("#cancelActionLst").val()}});
			self._jqGrid.jqGrid('setColProp','awsReconStatus', { editoptions: { value: $("#reconStatusLst").val()}});
			self._jqGrid.jqGrid('setColProp','awsStaledateStatus', { editoptions: { value: $("#staledateStatusLst").val()}});
			self._jqGrid.jqGrid('setColProp','awsPaymentMethod', { editoptions: { value: $("#paymentMethodLst").val()}});
			self._jqGrid.jqGrid('setColProp','awsWarrantType', { editoptions: { value: $("#warrantTypeLst").val()}});
		},
		_handleSearch: function (countOnly) {
			this.clearPageMessage();
			var self = this, _messageStyle = {
				"font-size": "1.35em",
				"font-weight": "bold"
			};
			if(!this._hasEnteredSearchCriteria()) {
				$("#errorMessage").html("At Least One Field Must be Queried.");
				$("#errorMessage").dialog({
					title:"Search Error",
				    resizable: false,
				    height:"auto",
				    modal: true,
				    buttons: {
		                "Ok": function() {
		                    $(this).dialog("close");
		                 }
		             }
				});
				//this.addPageMessage("ERROR", "At Least One Field Must be Queried.", _messageStyle);
			} else {
				$('#itemTypeCd').val($('#itemTypeCd_widget').val());
				$('#reasonCd').val($('#reasonCd_widget').val());
				$('#itemIndCd').val($('#itemIndCd_widget').val());
				//$('#personRefundGrid').jqGrid('setGridParam',{datatype:'json'});
				//$.publish("reloadPersonRefundGrid");
				jQuery("#personRefundGrid").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadPersonRefundGrid');
			}
		},
		_hasEnteredSearchCriteria: function() {
			return this.getChangedFields().length;
		},
		//Finds a all radio button's from a group and enables the non-checked options. Used for read only Radio reverse.
		 enableUncheckedRadio: function(radio_group) {
		    for (var i = 0; i < radio_group.length; i++) {
		        var button = radio_group[i];
		        	button.disabled = false; 
		    }
		}

	});
})(jQuery);