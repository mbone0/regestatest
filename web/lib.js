const READY=4;
const OK=200;

function sleepInterrupt(ms) 
{
  return new Promise(resolve => setTimeout(resolve, ms));
}

// https://www.sitepoint.com/delay-sleep-pause-wait/

function sleepBusy(milliseconds) 
{	
  const date = Date.now();
  let currentDate = null;
  do 
  {
    currentDate = Date.now();
  } 
  while (currentDate - date < milliseconds);
}


function readTextFile3(file,callback)
{
	
	
    var xhr = new XMLHttpRequest();
    xhr.open("GET", file);
    xhr.onload = function ()
    {
        if(xhr.readyState === READY)
        {
            if(xhr.status === OK || xhr.status == 0)
            {
                xhrText= xhr.responseText;
				console.log("response ");
				console.log(xhr.responseText);
				callback(xhrText);
            }
        }
    }
   
    xhr.send();
}

function readTextFile(file)
{
    var rawFile = new XMLHttpRequest();
   
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === READY)
        {
            if(rawFile.status === OK || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                //alert(allText);
            }
        }
    }
    rawFile.send(null);
    
}




function readTextFileP1(file, callback, variable)
{
	
	
    var xhr = new XMLHttpRequest();
    xhr.open("GET", file);
    xhr.onreadystatechange = function ()
    {
        if(xhr.readyState === READY)
        {
            if(xhr.status === OK || xhr.status == 0)
            {
                variable= xhr.responseText;
				//alert(variable);
				callback(variable);
            }
        }
    }
   
    xhr.send();
}
