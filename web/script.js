
var xhrTextTimes="";
var xhrTextGoods="";
var xhrTextSuppl="";

var basepath="";

const protocol="jerp://"
const path= "path="
const andcommand="&command=";
const andgood ="&good=";
const andquantity ="&quantity=";
const andspeed ="&speed=";
const andtime ="&time=";

const configuration="configuration/";
const commandgetgui= "get_gui_data";
const commandgetsup= "request_supply";
const goodsfile=configuration+"out_goods.txt";
const timesfile=configuration+"out_times.txt";
const supplfile=configuration+"out_supply.txt";
	
	
function changeHTMLCallback(elementID, newValue)
{
	document.getElementById(elementID).innerHTML  = newValue;
}

function changeResult(result)
{
	var elem="resultText";
	changeHTMLCallback(elem, result);
}


function initTimes(string)
{	
	var elementid="timeCombobox";	
	populateCombobox(elementid, string);
}

function initGoods(string)
{	
	var elementid="goodsCombobox";	
	populateComboboxWithValue(elementid, string);
}
function populateComboboxWithValue(elementID, string)
{
	
	var lines = string.split(/\r?\n/);
	
	var doc = document.getElementById(elementID);
	
	var selectTag="";
	
	for (let i = 0; i < lines.length; i+=2) 
	{
		selectTag +=  '<option value="'+lines[i]+'">'+lines[i+1]+'</option>'
	}
	
	doc.innerHTML=selectTag;
}

function populateCombobox(elementID, string)
{
	var lines = string.split(/\r?\n/);
	
	var doc = document.getElementById(elementID);
	
	var selectTag="";
	
	for (let i = 0; i < lines.length; i++) 
	{
		selectTag +=  '<option value="'+lines[i]+'">'+lines[i]+'</option>'
	}

	doc.innerHTML=selectTag;
}
		
function init1()
{
	basepath =document.getElementById('homepath').value;		
	var url=protocol+path+basepath+andcommand+commandgetgui;
	window.open(url, "_self");
	sleepBusy(1000);
	init2();
}

function init2()
{
	readTextFileP1(goodsfile, initGoods,xhrTextGoods);
	readTextFileP1(timesfile, initTimes,xhrTextTimes);
}

function search1()
{	
	var good= document.getElementById("goodsCombobox").value;
	
	var quantity =document.getElementById('quantity').value
	
	var speed=document.getElementById("checkboxSpeed").checked;
	
	var time= document.getElementById("timeCombobox").value;
	
	var url = protocol+path+basepath+andcommand+commandgetsup+andgood+good+andquantity+quantity+andspeed+speed+andtime+time;
	
	window.open(url, "_self");
	sleepBusy(1000);
	search2();
}

function search2()
{
	readTextFileP1(supplfile, changeResult, xhrTextSuppl);
}
