/**
 * A base form widget for handling common ALS Person form functions
 *
 * @author c8a650
 */
(function ($) {
	$.widget("fwp.alsFormWidget", {
		options: {
			dirtyCheck: "true", //"true", "silent", or unset/otherwise
			allowRadiosOff: false,
			crudStatusField: null
		},
		_create: function () {
			this._super();
			this._wireForm();
			this._wireUpperOnBlur();
			this._wireMasks();
			this._wireRadioBehavior();
			this._wireShowHideFieldSets();
		},
		_wireForm: function () {
			var self = this;
			this._form = this.element.find("form").first();
			/* Add dirty plugin to the form */
			if (this.options.dirtyCheck === "true") {
				this._form.areYouSure();
				if(this.options.crudStatusField) {
					this._form.on("dirty.areYouSure",function() {
						var _crudStatus = self._form.find("[name='"+self.options.crudStatusField+"']");
						if(_crudStatus.val() !== 'C' && _crudStatus.val() !== 'D') {
							_crudStatus.val("U");
						}
					});
				}
			} else if (this.options.dirtyCheck === "silent") {
				this._form.areYouSure({"silent": true});
			}
		},
		_wireUpperOnBlur: function () {
			this._form.find("input:not(:file,:password)").addClass("uppercase");
			this._form.find("input:not(:file,:password)").on('blur', function () {
				if ($(this).val()) {
					$(this).val($(this).val().toUpperCase());
				}
				if($(this).hasClass("ui-autocomplete-input")) {
					/* Find sibling hidden */
					$(this).siblings("input[type='hidden']").val($(this).val().toUpperCase());
				}
			});
		},
		_wireMasks: function () {
			this.element.find(".fwp-phone-number-input")
				.attr("placeholder","999-999-9999")
				.inputmask({
					"mask": "999-999-9999",
					greedy: false,
					clearIncomplete: true});
			this.element.find(".fwp-tax-ein-input")
				.attr("placeholder","999999999")
				.inputmask({"mask": "999999999",
					greedy: false,
					clearIncomplete: true});
			this.element.find(".fwp-ssn-input")
				.attr("placeholder","999999999")
				.inputmask({"mask": "999999999",
					greedy: false,
					clearIncomplete: true});
			this.element.find(".fwp-short-ssn-input")
			.attr("placeholder","9999")
			.inputmask({"mask": "9999",
				greedy: false,
				clearIncomplete: true});
			this.element.find(".fwp-als-no-input")
				.attr("placeholder","99999999")
				.inputmask({"mask": "99999999",
					greedy: false,
					clearIncomplete: false});
			this.element.find(".fwp-date-input")
				.attr("placeholder","mm/dd/yyyy")
				.inputmask("mm/dd/yyyy",{
					yearrange: { minyear: 1111, maxyear: 2999}
			});
			this.element.find(".fwp-currency-input").inputmask("currency", {
				radixPoint: ".",
				groupSeparator: ",",
				digits: 2,
				rightAlign: false,
				prefix: "$",
				autoUnmask: true
			});
		},
		_wireRadioBehavior: function() {
			var self = this;
			if(this.options.allowRadiosOff) {
				this.element.find("input[type='radio']").each(function(index,item){
					$(this).on("click",function(){
						self.element.find("[name='" + $(this).attr("name") + "']")
							.not($(this)).data("fwp-radio-checked", false);
						var _oldChecked = $(this).data("fwp-radio-checked");
						if($(this).prop("checked")) {
							$(this).data("fwp-radio-checked", true);
							if(_oldChecked) {
								$(this).data("fwp-radio-checked", false);
								$(this).removeProp("checked");
							}
						} else {
							$(this).data("fwp-radio-checked", false);
						}
					});
				});
			}
		},
		_wireShowHideFieldSets: function () {
			var _container, _a;
			this.element.find("fieldset legend:not(.fwp-exclude-from-show-hide)").each(function (i1, item) {
				_a = $("<a>").text("[Hide] ").addClass("fwp-show-hide-section");
				if ($(item).data("fwp-hidden-container") === "true") {
					$(item).siblings().each(function (i2, sibling) {
						_container = $(sibling);
						_container.hide();
						_container.data("fwp-hidden-container", "true");
						$(_a).text("[Show] ");
					});
				}
				_a.on("click", function () {
					var _aSelf = this;
					$(item).siblings().each(function (i2, sibling) {
						_container = $(sibling);
						if (_container.data("fwp-hidden-container") === "true") {
							_container.show();
							_container.data("fwp-hidden-container", "false");
							$(_aSelf).text("[Hide] ");
						} else {
							_container.hide();
							_container.data("fwp-hidden-container", "true");
							$(_aSelf).text("[Show] ");
						}
					});
				});
				$(item).prepend(_a);
			});
		},
		_handleSave: function (url,includeFWPDisabledElements) {
			var self = this, dfd = $.Deferred();
			this._clearFieldErrors();
			$.ajax({
				type: "POST",
				data: this._serializeForm(includeFWPDisabledElements),
				dataType: "json",
				cache: false,
				url: url,
				success: function (data) {
					if (data.rtrn && (data.rtrn.procStatus === "ERROR" || data.rtrn.procStatus === "WARNING")) {
						self._markFieldErrorWarnings(data.rtrn.procRtrnList, data.rtrn.procMsg);
					} else {
						self.resetForm(data);
					}
					dfd.resolve(data, (data.rtrn && (data.rtrn.procStatus === "ERROR" || data.rtrn.procStatus === "WARNING")));
				},
				error: function (x, e) {
					dfd.reject(x, e);
				}
			});
			return dfd;
		},
		_serializeForm: function(includeFWPDisabledElements) {
			var _serializedForm;
			if(includeFWPDisabledElements) {
				this._form.find(".fwp-disabled").removeAttr("disabled");
			}
			_serializedForm = this._form.serialize();
			if(includeFWPDisabledElements) {
				this._form.find(".fwp-disabled").attr("disabled","disabled");
			}
			return _serializedForm;
		},
		_clearFieldErrors: function (fieldName) {
			var _selectorError = ".fwp-field-error",
				_selectorStyle = ".fwp-field-has-error";

			if (fieldName) {
				_selectorError = "[data-fwp-error-field-name='" + fieldName + "']";
				_selectorStyle = "[name='" + fieldName + "']";
			}

			this.element.find(_selectorError).each(function () {
				$(this).remove();
			});
			this.element.find(_selectorStyle)
				.each(function () {
					$(this).removeClass("fieldErrorStyle").removeClass("fieldWarningStyle").removeClass("fwp-field-has-error");
				});
			this._setPageStatusMessage();
		},
		_setPageStatusMessage: function (div, message, info) {
			if (!this.element.find(".fwpPageStatusContainer").length) {
				this._form.append($("<div>").addClass("fwpPageStatusContainer"));
			}
			if (div) {
				this.element.find(".fwpPageStatusContainer").append(div);
			} else if (message) {
				this.element.find(".fwpPageStatusContainer").append(
					$("<div>").addClass(info ? "pageInfoStyle" : "pageErrorStyle").text(message));
			} else {
				this.element.find(".fwpPageStatusContainer").empty();
			}
		},
		_markFieldErrorWarnings: function (errors, pageError) {
			var self = this;
			this._clearFieldErrors();
			if (errors && errors.length) {
				this._setPageStatusMessage(null, pageError);
			}
			$.each(errors, function (index, item) {
				self._markFieldError(item.fieldName, item.procStatus, item.procMsg, item.confirmedFieldName);
			});
		},
		_markFieldError: function (fieldName, procStatus, procMsg, confirmedFieldName) {
			var self = this,
				_field, _highlightStyle,
				_errorWarningDiv,
				_errorsContainer = this.element.find(".fwpPageStatusContainer"),
				_confirmedFieldName,
				_confirmationKey;
			_field = self._findErrorField(fieldName);
			_highlightStyle = self._createHighlightStyle(procStatus);
			_errorWarningDiv = self._createErrorWarningDiv(procStatus, procMsg, fieldName);
			if (_field && _field.length) {
				/*autocomplete widget */
				if (_field.hasClass("s2j-combobox")) {
					_field.find("input").addClass(_highlightStyle).addClass("fwp-field-has-error");
				}
				else if (_field.siblings(".hasDatepicker").length) {
					/* date picker */
					_field.siblings(".hasDatepicker").addClass(_highlightStyle).addClass("fwp-field-has-error");
				}
				else {
					_field.addClass(_highlightStyle).addClass("fwp-field-has-error");
				}
				_field.after(_errorWarningDiv);
			} else {
				_errorsContainer.append(_errorWarningDiv);
			}
			self._highLightTabContainer(_field, _highlightStyle);

			/* Warnings require the user to confirm the warning.  Then the warning is marked as confirmed and
			 will allow save. */
			if (procStatus === 'WARNING' && confirmedFieldName) {
				_confirmedFieldName = self.element.find("[name='" + confirmedFieldName + "']");
				if (!_confirmedFieldName || !_confirmedFieldName.length) {
					_confirmedFieldName = $("<input>").attr("type", "hidden").attr("name", confirmedFieldName);
					_field.siblings().last().after(_confirmedFieldName);
				}
				if (_confirmedFieldName.val() !== 'true') {
					_confirmationKey = confirm(procMsg + "\nPress OK to allow.");
					_confirmedFieldName.val(_confirmationKey);
				}
			}
		},
		_highLightTabContainer: function (field, highlightStyle) {
			var _tabPanel = field.closest("[role='tabpanel']");

			if (_tabPanel && _tabPanel.size()) {
				_tabPanel.siblings("[role='tablist']").find("[href='#" +
					_tabPanel.attr("id") + "']")
					.addClass(highlightStyle)
					.addClass("fwp-field-has-error");
			}
		},
		_findErrorField: function (fieldName) {
			var _field = this.element.find("[name='" + fieldName + "']"), _id;
			if(_field.length > 1) {
				_field = _field.last();
			}
			if (_field.attr("type") === "hidden") {
				/* Likely a widget of some sort:  s2j-combobox */
				return _field.siblings(":not(script)").last();
			} else if (_field.attr("type") === "radio") {
				_id = _field.attr("id");
				return _field.siblings("label[for='"+_id+"']");
			}
			else if (_field.hasClass("hasDatepicker")) {
				/* Date picker - find the button */
				return _field.siblings("button").last();
			}
			return _field;
		},
		_createErrorWarningDiv: function (procStatus, procMsg, fieldName) {
			var _return;
			if (procStatus === 'ERROR') {
				_return = $("<div>").addClass("fwp-field-error fieldMessageErrorStyle").text(procMsg);
			} else if (procStatus === 'WARNING') {
				_return = $("<div>").addClass("fwp-field-error fieldMessageWarningStyle").text(procMsg);
			} else {
				_return = $("<div>").addClass("fwp-field-error fieldMessageInfoStyle").text(procMsg);
			}
			if (fieldName) {
				_return.attr("data-fwp-error-field-name", fieldName);
			}
			return _return;
		},
		_createHighlightStyle: function (procStatus) {
			if (procStatus === 'ERROR') {
				return "fieldErrorStyle";
			} else {
				return "fieldWarningStyle";
			}
		},
		_showDeletedProviderMessages: function () {
			jQuery.growlUI("Record Deleted", "Record has been deleted.  Navigating to Search page.");
			jQuery.blockUI();
		},
		_objectToDotNotation: function(object) {
			if(dotize) {
				return dotize.convert(object);
			} else {
				throw "dotize.js not loaded";
			}
		},
		/**
		 * @desc Given an object, a string path, and a value, initialize down the object path
		 * and set the value.
		 * @param {object} obj - to initialize
		 * @param {string} objPath - string path ala "item.some.path"
		 * @param {object} value - to set at string path
		 * @returns {object}
		 */
		_initObjectValue: function (obj, objPath, value) {
			if (!objPath) return value;
			var pathParts = objPath.split('.');
			obj[pathParts[0]] = this._initObjectValue(obj[pathParts[0]] || {}, pathParts.slice(1).join('.'), value);
			return obj;
		},
		/**
		 * @desc Given an object and a string path determine if the object has a full graph
		 * of the path.
		 * @param {object} obj
		 * @param {string} objPath
		 * @returns {boolean}
		 */
		_objectHasPath: function (obj, objPath) {
			if (!objPath) return false;
			if (obj === undefined || obj === null) return false;
			var pathParts = objPath.split('.');
			if (obj.hasOwnProperty(pathParts.slice(-1))) return true;
			return this._objectHasPath(obj[pathParts[0]], pathParts.slice(1).join('.'));
		},
		/**
		 * @desc Given an object and a string path return the value
		 * of the path.
		 * @param {object} obj
		 * @param {string} objPath
		 * @returns {object}
		 */
		_objectPathValue: function (obj, objPath) {
			if (!objPath) return undefined;
			if (obj === undefined || obj === null) return undefined;
			var pathParts = objPath.split('.');
			if (obj.hasOwnProperty(pathParts.slice(-1))) return obj[pathParts.slice(-1)];
			return this._objectPathValue(obj[pathParts[0]], pathParts.slice(1).join('.'));
		},

		_setFormValues: function(object) {
			var _resultList = this._objectToDotNotation(object), _name, self = this;
			$.each(_resultList, function(name, value){
				_name = "[name='"+name+"']";
				if(self._form.has(_name).length) {
					self._form.find(_name).each(function(index, item){
						if($(item).hasClass("fwp-date-input")) {
							if($(item).hasClass("hasDatepicker")) {
								if(value) {
									$(item).datepicker("setDate",
										moment(value).toDate());
								} else {
									$(item).datepicker("setDate", null);
								}
							} else {
								if(value) {
									$(item).val(moment(value).format("MM/DD/YYYY"));
								} else {
									$(item).val(null);
								}
							}
						} else if($(item).attr("type")!=="radio") {
							$(item).val(value);
						} else {
							if($(item).val()===value) {
								$(item).prop("checked",true);
							} else {
								$(item).prop("checked", false);
							}
						}
					});
				}
			});
		},
		_getFieldValue: function ($field) {
			/* Relies on the areyousure.js plugin */
			if ($field.is(':disabled')) {
				return 'ays-disabled';
			}

			if ($field.attr('name') === undefined) {
				return null;
			}

			var val;
			var type = $field.attr('type');
			if ($field.is('select')) {
				type = 'select';
			}

			switch (type) {
				case 'checkbox':
				case 'radio':
					val = $field.is(':checked');
					break;
				case 'select':
					val = '';
					$field.find('option').each(function (o) {
						var $option = $(this);
						if ($option.is(':selected')) {
							val += $option.val();
						}
					});
					break;
				default:
					val = $field.val();
			}
			return val;
		},
		/*
		 PUBLIC Functions
		 */
		disableForm: function (context,excludeFieldSelectors) {
			var _each, _securableItem, _selector = ':input:not(.fwp-always-enabled),.s2j-combobox',
				_isExcluded = false;
			if (context) {
				_each = context.find(_selector);
			} else {
				_each = this.element.find(_selector);
			}
			if(excludeFieldSelectors != null){
				_each.each(function () {
					_isExcluded = false;
					_securableItem = $(this);
					if(excludeFieldSelectors) {
						for(var i = 0; i < excludeFieldSelectors.length; i++) {
							if($(this).is(excludeFieldSelectors[i])) {
								_isExcluded = true;
							}
						}
					}
					if(!_isExcluded) {
						if (_securableItem.hasClass("s2j-combobox")) {
							_securableItem.find("a").hide();
						} else {
							_securableItem.prop('disabled', 'disabled')
								.addClass("fwp-disabled");
							if (_securableItem.is("select") && (_securableItem.val() == null ||
								_securableItem.val() === "")) {
								_securableItem.text("");
							}
						}
					}
				});
			}else{
				_each.each(function () {
					_securableItem = $(this);
					if (_securableItem.hasClass("s2j-combobox")) {
						_securableItem.find("a").hide();
					} else {
						_securableItem.prop('readonly', true).addClass('disabled').attr('disabled', 'disabled');
					}
				});
			}
		},
		resetForm: function () {
			this._form.trigger('reinitialize.areYouSure');
			this._clearFieldErrors();
			if(this.options.crudStatusField) {
				this._form.find("[name='"+this.options.crudStatusField+"']").val("R");
			}
		},
		clearFormDirty: function() {
			this._form.removeClass("dirty");
		},
		addPageMessage: function (status, message, style) {
			var _pageStatusContainer = this.element.find(".fwpPageStatusContainer");
			if (!_pageStatusContainer.length) {
				this._form.append($("<div>").addClass("fwpPageStatusContainer"));
				_pageStatusContainer = this.element.find(".fwpPageStatusContainer");
			}
			_pageStatusContainer.append(this._createErrorWarningDiv(status, message));
			this._highLightTabContainer(_pageStatusContainer, style);
			if(style) {
				_pageStatusContainer.css(style);
			}
		},
		clearPageMessage: function() {
			this.element.find(".fwpPageStatusContainer").empty();
		},
		hideFieldSetLegend: function(id) {
			this.element.find("#"+id).data("fwp-hidden-container","true");
		},
		getChangedFields: function () {
			var _changedFields = [], self = this, _val, _orig;
			this._form.find(":input").each(function () {
				/* use the areyousure data element */
				_val = self._getFieldValue($(this));
				_orig = $(this).data("ays-orig");
				console.log($(this).attr("name") + " orig:" + _orig + " curr:" + _val);
				if ($(this).attr('name') !== undefined &&
					$(this).data("ays-ignore") === undefined &&
					_orig !== undefined &&
					_orig !== _val &&
					$(this).attr('name') !== 'srchAll') {
					_changedFields.push({
						"field": $(this).attr("name"),
						"originalValue": _orig,
						"currentValue": _val
					});
		}
			});
			return _changedFields;
		}

	});
})(jQuery);