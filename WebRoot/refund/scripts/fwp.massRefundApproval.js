/**
 *
 * jQuery UI widget to provide behavior for the Mass Refund Approval page.
 *
 * @author cfa027
 */
(function ($) {
	$.widget("fwp.alsMassRefundApproval", $.fwp.alsFormWidget, {
		options: {},
		_create: function () {
			this._setOptions({ "dirtyCheck": "silent", "allowRadiosOff": true });
			this._super();			
			this._wireClickHandlers();
			this._wireEventHandlers();
		},
		_wireClickHandlers: function () {
			var self = this;
			this.element.find("button#searchButton").on("click",function(){
				self._handleSearch();
			});
			this.element.find("button#saveButton").on("click",function(){
				self._handleSave();
			});

		},
		_wireEventHandlers: function () {
			var self = this;
		},
		_handleSearch: function () {
			this.clearPageMessage();
			//To force a reload because the loadonce attribute is set to 'true' in the JSP.
			this.element.find("#applicationItemGrid").jqGrid('setGridParam', {datatype: 'json'});
			$.publish("popGrid");
		},
		_handleSave: function() {
			//TODO build a submittable form of AlsPersonItemTemplLinkDTO and submit it
			alert("Not implemented.");
		},
		_hasEnteredSearchCriteria: function() {
			return this.getChangedFields().length;
		}
	});
})(jQuery);