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
			this._setGridSelectLists();
			widthFunction();
		},
		_wireClickHandlers: function () {
			var self = this;
			this.element.find("button#searchButton").on("click",function(){
				self._handleSearch();
			});
		},
		_wireEventHandlers: function () {
			var self = this;
		},
		_handleSearch: function () {
			this.clearPageMessage();
			$.publish("popGrid");
		},
		_hasEnteredSearchCriteria: function() {
			return this.getChangedFields().length;
		}
	});
})(jQuery);