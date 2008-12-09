/* 
   Display is initially set to none in smplMgr.css & reset by showSmplMgr.js for 
   Sample Manager topics.  This is a necessary work-around for topics that 
   are in .DOC files with Sample Manager topics, but which are not themselves
   Sample Manager Topics.
*/	

blnShowSmpMgr = true;

try{
	var objStyleThingy = null;
	objStyleThingy = document.styleSheets[0];
	objStyleThingy.addRule( "div#smpMgrCell" , "display:inline;" , 0 );
}catch(e){
	alert(e.description);
}	
