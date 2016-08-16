function menuSec (tstId) {
// U = update -- V = view only -- N should be default for main menus
	var role = getUrlParameter('privRole');
	if(role == "V"){
		if (tstId === 'genNonAlsEntries') { // budget information -- appropriations
			$("td[id='add_alsSabhrsEntriesTable']").toggle(false);
			   $("td[id='addTemplate_alsSabhrsEntriesTable']").toggle(false);
		} 
	}
	

	
}

function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function ajaxErrorHandler (x, e, ajaxCallNm, displayMsg, statusObjId) {
	
	if (displayMsg !== null && displayMsg !== "") {
		
		if (statusObjId !== null && statusObjId !== "" ) {
			$('#'+statusObjId).html('<p style="color:red;font-size:14px"><b>'+displayMsg+'</b></p>');
		}
	}
	
	if (x.readyState === 4 && x.status === 200 && e === "parsererror") {
		alert("Your session appears to have expired.  You will be asked to log in again and returned here.");
		window.location.reload();					
	} else if (x.readyState === 0 && x.status === 0 && e === "error") {
		alert("The JBoss application server is down.  Please contact FWP technical support.");
		window.location.reload();					
	} else {
    	alert("DEBUG: " + ajaxCallNm + " Error - " + x.readyState + " "+ x.status +" "+ e); 
	}           
}