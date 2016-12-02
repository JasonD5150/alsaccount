/**
 * 
 * genNonAlsEntries.jsp Javascript
 * 
 * @author cfa027
 */
(function ($) {
	$.widget("fwp.genAlsSabhrsEntries", $.fwp.alsFormWidget,{
		options: {
			selectedRow: null
		},
		_create: function () {
			this._setOptions({ "dirtyCheck": "silent", "allowRadiosOff": true });
			this._super();	
			this._wireClickHandlers();
			this._wireEventHandlers();
		},
		_wireClickHandlers: function() {
			var self = this, _editForm;
			this.element.find("button#searchButton").on("click", function () {
				self._handleSearch();
			});
			this.element.find("button#resetButton").on("click", function () {
				window.location.reload(true);
			});
		},
		_wireEventHandlers: function () {
			var self = this;
			$.subscribe("transGroupDtlsComplete", function (event, data) {
				self._jqGrid = $(data);
				if(self.options.selectedRow != null){
					self._jqGrid.jqGrid("setSelection", self.options.selectedRow);
				}
			});
			$.subscribe('transGroupDtlsSelected', function(event, data) {
				self._jqGrid = $(data);
				$('#reverseAlsEntries').prop({disabled:false});
				self.options.selectedRow = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');
				$('#frmTransGrp').val($("#transGroupDtlsTable").jqGrid('getCell', self.options.selectedRow, 'idPk.atgTransactionCd'));
				$('#frmTransIdentifier').val($("#transGroupDtlsTable").jqGrid('getCell', self.options.selectedRow, 'idPk.atgsGroupIdentifier'));
				$("#alsSabhrsEntriesTable").jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadAlsSabhrsEntriesTable');
			});
			$.subscribe('alsSabhrsEntriesComplete', function(event, data) {			
				$('#alsSabhrsEntriesTable').jqGrid('setColProp','jlr', { editoptions: { value: $("#jlrLst").val()}});
				$('#alsSabhrsEntriesTable').jqGrid('setColProp','aamFund', { editoptions: { value: $("#fundLst").val()}});
				$('#alsSabhrsEntriesTable').jqGrid('setColProp','asacSubclass', { editoptions: { value: $("#subClassLst").val()}});
				$('#alsSabhrsEntriesTable').jqGrid('setColProp','aocOrg', { editoptions: { value: $("#orgLst").val()}});
				$('#alsSabhrsEntriesTable').jqGrid('setColProp','aamAccount', { editoptions: { value: $("#accountLst").val()}});
				var sel_id = $("#transGroupDtlsTable").jqGrid('getGridParam', 'selrow');
				if(sel_id != null){
				   $("td[id='add_alsSabhrsEntriesTable']").toggle(true);
				   $("td[id='addTemplate_alsSabhrsEntriesTable']").toggle(true);
				}else{
				   $("td[id='add_alsSabhrsEntriesTable']").toggle(false);
				   $("td[id='addTemplate_alsSabhrsEntriesTable']").toggle(false);
				}
			   //menuSec("genNonAlsEntries");
		});
		},
		_handleSearch: function (countOnly) {
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
			} else {
				selectedRow = null;
				$('#reverseAlsEntries').prop({disabled:false});
				$('#budgYear').val($('#budgYear_widget').val());
				$('#provNo').val($('#provNo_widget').val());
				$('#transGrpType').val($('#transGrpType_widget').val());

				$('#transGroupDtlsTable').jqGrid('setGridParam',{datatype:'json'});
				$.publish('reloadTransGroupDtlsTable');
			}
		},
		_hasEnteredSearchCriteria: function() {
			return this.getChangedFields().length;
		},
		_prePoplulate: function() {
			$('#aamBusinessUnit').val('52010');
		    $('#asacProgram').val($('#hidBudgYear').val());
		    $('#asacBudgetYear').val($('#hidBudgYear').val());
		},
		selectAll: function() {
			var grid = $("#alsNonAlsTemplateTable");
		    var rows = grid.jqGrid("getDataIDs");
		    for (i = 0; i < rows.length; i++)
		    {
				$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',1);
		    }
		},
		deselectAll: function() {
			var grid = $("#alsNonAlsTemplateTable");
		    var rows = grid.jqGrid("getDataIDs");
		    for (i = 0; i < rows.length; i++)
		    {
				$('#alsNonAlsTemplateTable').jqGrid('setCell',rows[i],'selected',0);
		    }
		},
		exitNonAlsMasterDialog: function() {
			var amountSet = true;
			var templateSeleted = false;
			var grid = $("#alsNonAlsTemplateTable");
		    var rows = grid.jqGrid("getDataIDs");
		    var tmp = "";

		    	for (i = 0; i < rows.length; i++)
			    {
			        var selected = grid.jqGrid ('getCell', rows[i], 'selected');
			        var amount = grid.jqGrid ('getCell', rows[i], 'amount');
			        var anatCd = grid.jqGrid ('getCell', rows[i], 'idPk.anatCd');
			        if(selected == 1){
			        	templateSeleted = true;
			        }
			        if(selected == 1 && amount <= 0){
			        	amountSet = false;
			        }else if(selected == 1 && amount > 0){
			        	tmp = tmp + anatCd +"-"+ amount +",";
			        }
			    }
			   	$('#frmNonAlsEntries').val(tmp);
			   	if(templateSeleted){
				    if(amountSet){
				    	$('#frmOper').val('addTemplates');
				    	
						url = "alsAccount/genAlsSabhrsEntriesGridEdit_execute.action";    
		        		$.ajax({
		                  type: "POST",
		                  url: url,
		                  dataType: "json",
		                  data: $('#alsSabhrsEntriesGridForm').serialize(),
		                  success: function(result){
			                  if(result.actionErrors){
			                  	$('#html').html('<p style="color:red;font-size:14px"><b>'+ result.actionErrors +'</b></p>');
			                  }else{
			                  	$('#accMasterDialog').dialog('close');
								$.publish('reloadAlsSabhrsEntriesTable');
			                  }
		                 }
		                });
				    }else{
				    	alert("An amount must be entered for each selected template.");
				    }
		  		}else{
		  			$('#accMasterDialog').dialog('close');
		  		}
		},
	});
})(jQuery);