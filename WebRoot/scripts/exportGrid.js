function exportGrid(gridName,dataLabel,queryForm,selectedRow){
	var _jqGrid=$("#"+gridName);
	_jqGrid.jqGrid("setGridParam", {rowNum: 999999999, autowidth: true, shrinkToFit: false}).trigger("reloadGrid");
	var _rowNum = _jqGrid.jqGrid("getGridParam", "rowNum"),
	_columnModel = _jqGrid.jqGrid("getGridParam", "colModel"),
	_columnNames = _jqGrid.jqGrid("getGridParam", "colNames"),
	_columns = [],
	_data = {};	
	_data[dataLabel] = _jqGrid.jqGrid("getGridParam", "data");
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
	if(queryForm != null && queryForm != ""){
		var filters = $("#"+queryForm).serialize();
		_data["filters"] =  filters;
	}
	if(selectedRow){
		_data["selectedRow"] =  _jqGrid.jqGrid('getGridParam', 'selrow');
	}
	return _data;
}
function exportMultipleGrids(gridList,dataLabelList,queryForm){
	_data = {};	
	_columnsList=[];
	for (i=0;i<gridList.length;i++){
		var _jqGrid=$("#"+gridList[i]);
		var _rowNum = _jqGrid.jqGrid("getGridParam", "rowNum"),
		_columnModel = _jqGrid.jqGrid("getGridParam", "colModel"),
		_columnNames = _jqGrid.jqGrid("getGridParam", "colNames"),
		_columns = [];
		_data[dataLabelList[i]] = _jqGrid.jqGrid("getGridParam", "data");
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
		_columnsList.push(_columns);
		$.each(_data[dataLabelList[i]],function(index, item){
			$.each(_columnModel, function(cidx, citem){
				if(_columnModel[cidx].sorttype !=="text" &&
					item[_columnModel[cidx].name]==="") {
					item[_columnModel[cidx].name] = null;
				}
			});
		});
	}
	_data["columnsList"]=_columnsList;
	
	if(queryForm != null && queryForm != ""){
		var filters = $("#"+queryForm).serialize();
		_data["filters"] =  filters;
	}
	return _data;
	
}