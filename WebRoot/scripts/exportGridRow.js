function exportGridRow(gridName,dataLabel){
	var _jqGrid=$("#"+gridName);
	_jqGrid.jqGrid("setGridParam", {rowNum: 999999999, autowidth: true, shrinkToFit: false}).trigger("reloadGrid");
	var _rowNum = _jqGrid.jqGrid("getGridParam", "rowNum"),
	_columnModel = _jqGrid.jqGrid("getGridParam", "colModel"),
	_columnNames = _jqGrid.jqGrid("getGridParam", "colNames"),
	_columns = [],
	_data = {};	
	var sel_id = _jqGrid.jqGrid('getGridParam','selrow');
	_data[dataLabel] = _jqGrid.jqGrid("getRowData", sel_id);
	$.each(_columnModel, function (index) {
		if (!_columnModel[index].hidden) {
			_columns.push({
				"itemVal": _columnModel[index].name,
				"itemLabel": _columnNames[index]
			});
		}
	});

	/* Null empty strings in non-text fields */
	_data["columnNameValues"] = _columns;
	$.each(_data[dataLabel],function(index, item){
		$.each(_columnModel, function(cidx, citem){
			if(_columnModel[cidx].sorttype !=="text" &&
				item[_columnModel[cidx].name]==="") {
				item[_columnModel[cidx].name] = null;
			}
		});
	});
	
	return _data;
}