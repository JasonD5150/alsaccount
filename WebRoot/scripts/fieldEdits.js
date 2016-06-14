/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */


// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
};

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
};

function daysInFebruary(year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
};
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31;
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30;}
		if (i==2) {this[i] = 29;}
   } 
   return this;
};

function testDate(dtObj){
	var dtStr = dtObj.value;
	if (dtStr != "") {
		if (dtStr.indexOf("/") == -1) {
			tMonth = dtStr.substring(0,2);
			tDay = dtStr.substring(2,4);
			tYear= dtStr.substring(4);
			
			dtStr = tMonth + "/" + tDay + "/"+ tYear;
//			alert(dtStr);
		}
	};
	

	if (dtStr != "") { 
		var daysInMonth = DaysArray(12);
		var pos1=dtStr.indexOf(dtCh);
		var pos2=dtStr.indexOf(dtCh,pos1+1);
		var strMonth=dtStr.substring(0,pos1);
		var strDay=dtStr.substring(pos1+1,pos2);
		var strYear=dtStr.substring(pos2+1);
		strYr=strYear;
		if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1);
		if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1);
		for (var i = 1; i <= 3; i++) {
			if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1);
		}
		month=parseInt(strMonth);
		day=parseInt(strDay);
		year=parseInt(strYr);
		if (pos1==-1 || pos2==-1){
			alert("The date format should be : mm/dd/yyyy or mmddyyyy");
			setTimeout(function () {dtObj.focus();}, 0);
			return false;
		}
		if (strMonth.length<1 || month<1 || month>12){
			alert("Please enter a valid month");
			setTimeout(function () {dtObj.focus();}, 0);
			return false;
		}
		if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
			alert("Please enter a valid day");
			setTimeout(function () {dtObj.focus();}, 0);
			return false;
		}
		if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
			alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear);
			setTimeout(function () {dtObj.focus();}, 0);
			return false;
		}
		if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
			alert("Please enter a valid date");
			setTimeout(function () {dtObj.focus();}, 0);
			return false;
		}
	}
	
	dtObj.value = dtStr;
	if($('#updateAccCdFrom').val() != "" && $('#updateAccCdTo').val() != ""){
		$('#upf').val($('#updateAccCdFrom').val())
		$('#upt').val($('#updateAccCdTo').val())
		$.publish('reloadItemTypes');
	}
	return true;
};

function testNumber(numObj){
	var numStr;
	var numFloat;
	numStr = numObj.value;
	numFloat = parseFloat(numStr);

	if (numStr != "") {
	    if (numStr != numFloat) {
	        alert("Please enter a valid number");
	        setTimeout(function () {numObj.focus();}, 0);
	        return false;
	    }
	}
    return true;
};

//removes all non-number characters(including spaces) except for . and - 
//returns a decimal value or NaN value if no numbers were included in the string
function cleanNumberString(strNumber){
	var re = /[^0-9\.\-]/g;
	strNumber = strNumber.replace(re,'');
	return parseFloat(strNumber);
};

function testLatitude(lat){
	if (lat < 44.34778 || lat > 49.00368) {
		return false;
	}	
	return true;
};

//longitutde should be a negative number
function fixLongitude(lon){
	if (lon > 0) { 
		lon = -1 * lon;		
	}
	return lon;
};

function testLongitude(lon){
	lon = fixLongitude(lon);
	if (lon < -116.06377 || lon > -104.00428) {
		return false;
	}					
	return true;
};

function testTime(field,colname) { 
	var errorMsg = ""; 
	// regular expression to match required time format 
	re = /^(\d{1,2}):(\d{2})(:00)?([ap]m)?$/; 

	
	if(field != '') { 
		if(regs = field.match(re)) { 
			if(regs[4]) { 
				errorMsg = "24hr clock: hh:mm " + regs[1]; 
			} else { 
				// 24-hour time format 
				if(regs[1] > 23) { 
					errorMsg = "Invalid value for hours: hh: " + regs[1]; 
				} 
			} 

			if(!errorMsg && regs[2] > 59) { 
				errorMsg = "Invalid value for minutes: :mm " + regs[2]; 
			} 
		} else { 
				errorMsg = "Invalid time format: hh:mm " + field; 
		} 
	} 
	
	if(errorMsg != "") { 
		return [false,errorMsg]; 
	} 
	
	return [true,""]; 
};



function test24Time(fieldObj) { 

	
	var fieldVal = fieldObj.value;
	// regular expression to match 24 hour required time format 
	var re = /([0-1]\d|2[0-3]):([0-5]\d)/; 

	if(!re.test(fieldVal)) { 
		alert("Invalid time. Use 24 hour time (HH:MM)");
		return false; 
	} 
	
	return true; 
};

String.prototype.trim = function() {
	return this.replace(/^\s*/, "").replace(/\s*$/, "");
};

