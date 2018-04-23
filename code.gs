function doGet(e){
  
  var op = e.parameter.action;
  //sheet url name
  var ss=SpreadsheetApp.openByUrl("sheet url name");
  //sheet name
  var sheet = ss.getSheetByName("Sheet1");

  
  if(op=="insert")
    return insert_value(e,sheet);
  
  //Make sure you are sending proper parameters 
  if(op=="read")
    return read_value(e,ss);
}


function insert_value(request,sheet){ 
   var id = request.parameter.id;
  var link = request.parameter.link;
  var thumb = request.parameter.thumb;
  var rowData = sheet.appendRow([id,link,thumb]);  
 }
  
  



function read_value(request,ss){
  
 
  var output  = ContentService.createTextOutput(),
      data    = {};
  //Note : here sheet is sheet name , don't get confuse with other operation 
      var sheet="sheet1";

  data.records = readData_(ss, sheet);
  
  var callback = request.parameters.callback;
  
  if (callback === undefined) {
    output.setContent(JSON.stringify(data));
  } else {
    output.setContent(callback + "(" + JSON.stringify(data) + ")");
  }
  output.setMimeType(ContentService.MimeType.JAVASCRIPT);
  
  return output;
}


function readData_(ss, sheetname, properties) {

  if (typeof properties == "undefined") {
    properties = getHeaderRow_(ss, sheetname);
    properties = properties.map(function(p) { return p.replace(/\s+/g, '_'); });
  }
  
  var rows = getDataRows_(ss, sheetname),
      data = [];

  for (var r = 0, l = rows.length; r < l; r++) {
    var row     = rows[r],
        record  = {};

    for (var p in properties) {
      record[properties[p]] = row[p];
    }
    
    data.push(record);

  }
  return data;
}



function getDataRows_(ss, sheetname) {
  var sh = ss.getSheetByName(sheetname);

  return sh.getRange(2, 1, sh.getLastRow() - 1, sh.getLastColumn()).getValues();
}


function getHeaderRow_(ss, sheetname) {
  var sh = ss.getSheetByName(sheetname);

  return sh.getRange(1, 1, 1, sh.getLastColumn()).getValues()[0];  
} 
  
