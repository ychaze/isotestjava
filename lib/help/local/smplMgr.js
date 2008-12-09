//	Microsoft Visual Studio.NET Sample Viewer
//	Copyright 2001 Microsoft
//	Author:	Sean Gephardt
//	Revision Date:	08/14/2001
//	Revised 4/2/2003 by Syne Mitchell

// LOCALIZATION CONSTANTS
// Load xml resource
var xmldoc = new ActiveXObject("MSXML.DOMDocument");
xmldoc.async = false;
xmldoc.load(getScriptPath() + "\smplMgr.xml");
var L_LoadSample_HTMLText = getXMLText('L_LoadSample_HTMLText');
var L_CopyAll_HTMLText = getXMLText('L_CopyAll_HTMLText');
var L_Help_HTMLText = getXMLText('L_Help_HTMLText');
var L_GeneralInfoText_HTMLText = getXMLText('L_GeneralInfoText_HTMLText');
var L_ErrFileExists0_HTMLText = getXMLText('L_ErrFileExists0_HTMLText');
var L_ErrFileExists1_HTMLText = "\n" + getXMLText('L_ErrFileExists1_HTMLText');
var L_SMTitle_HTMLText = getXMLText('L_SMTitle_HTMLText');
var L_PopupMenuID1_HTMLText = getXMLText('L_PopupMenuID1_HTMLText');
var L_PopupMenuID2_HTMLText = getXMLText('L_PopupMenuID2_HTMLText');
var L_VisualNote_HTMLText = getXMLText('L_VisualNote_HTMLText');
var L_VisualNote2_HTMLText = getXMLText('L_VisualNote2_HTMLText');
var L_GenInfo_HTMLText = getXMLText('L_GenInfo_HTMLText');
var L_ProjFileLoc_HTMLText = getXMLText('L_ProjFileLoc_HTMLText');
var L_ProjFileLocCopy_HTMLText = "\n" + getXMLText('L_ProjFileLocCopy_HTMLText');

//	ERROR MESSAGE STRINGS
var L_GenericHelpErr_HTMLText = getXMLText('L_GenericHelpErr_HTMLText');
var L_HXDSSessionObjectError_HTMLText = getXMLText('L_HXDSSessionObjectError_HTMLText') + "\n" +  L_GenericHelpErr_HTMLText;
var L_SessObjSampNavError_HTMLText = getXMLText('L_SessObjSampNavError_HTMLText') + "\n" +  L_GenericHelpErr_HTMLText;
var L_ViewSampleError1_HTMLText = getXMLText('L_ViewSampleError1_HTMLText');
var L_ViewSampleError2_HTMLText = getXMLText('L_ViewSampleError2_HTMLText') + "\n" +  L_GenericHelpErr_HTMLText;
var L_CopySampleError1_HTMLText = getXMLText('L_CopySampleError1_HTMLText');
var L_CopySampleLocationError_HTMLText = getXMLText('L_CopySampleLocationError_HTMLText');
var L_CopyOverWriteError_HTMLText = getXMLText('L_CopyOverWriteError_HTMLText');
var L_LoadSolFileError_HTMLText = getXMLText('L_LoadSolFileError_HTMLText');
var L_LoadSolMainError_HTMLText = getXMLText('L_LoadSolMainError_HTMLText') + "\n" +  L_GenericHelpErr_HTMLText;
var L_WSloadSampleError_HTMLText = getXMLText('L_WSloadSampleError_HTMLText');
var L_ParseError_HTMLText = getXMLText('L_ParseError_HTMLText');
var L_LocCssOne_HTMLText = getXMLText('L_LocCssOne_HTMLText');


if (blnShowSmpMgr != false)
{

	//	GLOBALS
	var bAlert = false;
	var sOrigDiv="";
	var bRuntimeSnR = false;
	
	//This global variable tracks whether the placeholder text is currently being displayed, so the
	//script can either replace or append new content, as appropriate.
	var bPlaceholderDiv = true;
	
	//query: do aspx, asmx, and other QuickStart files have custom icons?  If so, need to be added to this list
	//	track file types with custom icons
	var sIcons = "sql asp bas bat bmp cdf c asa cls cs cpp css ctl def dob dsr ico frm gif h htc htm html jpg js odl png txt vbs vb xml xsl sln vcproj";
	
	//	VARS FOR TESTING
	//	var L_defaultLocation_HTMLText = "";
	//	var g_sPageLoc = "ms-help://MS.MSDNVS/vcsample/html/samp.htm";
		var L_defaultLocation_HTMLText = "ms-help:../samp/";
		var g_sPageLoc = unescape(location.href);
	
	//	CREATE HH 2.0 SESSION OBJECT
	try
	{
		var oSessionObj = new ActiveXObject("Hxds.HxSession");
		oSessionObj.Initialize(g_sPageLoc, 0);
	}
	catch(errSessionObject)
	{		alert(errSessionObject.number + "\n" + errSessionObject.description + "\n" + L_HXDSSessionObjectError_HTMLText);	}
	
	//===
	//	Get the Sample Information from the Session Object
	try
	{	var oSmpMgr = oSessionObj.GetNavigationObject("!SampleInfo", "");}
	catch(errSessObjNav)
	{	alert(errSessObjNav.number + "\n" + errSessObjNav.description + "\n" + L_SessObjSampNavError_HTMLText);	}
	}	

function getScriptPath() {
//Determine path to JS so we can retrieve our variables from within the xml file.
	for (i=0; i < document.scripts.length; i++) {
		var spath = document.scripts[i].src;
		spath = spath.toLowerCase();
		if (spath.indexOf("smplmgr.js")!=-1) return spath.replace("smplmgr.js", "");
		}
}

//Note: could improve performance some by using children instead of selectSingleNode
function getXMLText(term) {
	var out = xmldoc.selectSingleNode("/UI/String[@Id='" + term + "']").text;
	return out;
}


//	DHTML for TOC tree menu
function Toc_click()
{
	var eSrc = window.event.srcElement;
	eLI = eSrc.parentElement;
	var eUL = GetNextUL(eLI);
	var eIMG = GetNextImg(eLI);
	if(eUL && "kidShown" == eLI.className){	// hide on-page kids
		eLI.className = "kid";
		eUL.className = "clsHidden";
		eIMG.src = L_defaultLocation_HTMLText + "greenfolder.gif";
	}else if(eUL && eUL.all.length && "kid" == eLI.className){	// show on-page kids
		eLI.className = "kidShown";
		eUL.className = "clsShown";
		eIMG.src = L_defaultLocation_HTMLText + "greenfoldero.gif";
	}
}

function GetNextUL(eSrc)
{
    var eRef = eSrc;
    for(var i = 0; i < eRef.children.length; i++) if("UL" == eRef.children[i].tagName) return eRef.children[i];
    return false;
}

function GetNextImg(eSrc)
{
    var eRef = eSrc;
    for(var i = 0; i < eRef.children.length; i++) if("IMG" == eRef.children[i].tagName) return eRef.children[i];
    return false;
}

//	FILTER KEYBOARD EVENTS
function document.onkeydown()
{
	if (window.event.keyCode == 13 || window.event.keyCode == 32)
	{	/*	document.onmouseup();		*/	}
	if(window.event.keyCode == 145)
	{	bAlert = bAlert?false:true;	}
}

// CHANGE CSS FOR ELEMENT HIGHLIGHTING
function document.onclick()
{
	var oElement = window.event.srcElement;
	var strEleID = oElement.id;

	// FOR LINKS
	if (strEleID == L_PopupMenuID1_HTMLText)
	{
		for(i=0;i<document.all.item(L_PopupMenuID1_HTMLText).length;i++)
		{
			if(document.all.item(L_PopupMenuID1_HTMLText)[i].className == "clsNoView")
			{ document.all.item(L_PopupMenuID1_HTMLText)[i].className = "clsNoView";	}
			else
			{	document.all.item(L_PopupMenuID1_HTMLText)[i].className = String(L_VisualNote_HTMLText);	}
		}
		if (oElement.className == "clsNoView")
		{	return;		}
		else
		{	oElement.className = String(L_VisualNote2_HTMLText);	}
	}

	// FOR IMAGES
	if (strEleID == L_PopupMenuID2_HTMLText)
	{
		for(i=0;i<document.all.item(L_PopupMenuID2_HTMLText).length;i++)
		{
			if(document.all.item(L_PopupMenuID2_HTMLText)[i].className == "clsNoView")
			{	document.all.item(L_PopupMenuID2_HTMLText)[i].className = "clsNoView";	}
			else
			{	document.all.item(L_PopupMenuID2_HTMLText)[i].className = String(L_VisualNote_HTMLText);	}
		}
		if (window.event.srcElement.parentElement.children(1).className == "clsNoView")
		{	return;	}
		else
		{	window.event.srcElement.parentElement.children(1).className = String(L_VisualNote2_HTMLText);	}
	}
}

//	VIEW THE SAMPLE ON THE PAGE
function view(arrFileIndex,sID,sSFL,objID)
{
	try
	{	var oSmp = oSmpMgr.GetSampleFromId(g_sPageLoc,sID,sSFL);		}
	catch(errViewFileTextOne)
	{	alert(errViewFileTextOne.number + "\n" + errViewFileTextOne.description + "\n"+ L_SessObjSampNavError_HTMLText);	}

	try{
		if(sOrigDiv == "")
		{	sOrigDiv = oSampDispDiv.innerHTML;	}
		try{
			var sFileText = oSmp.GetFileTextAtIndex(arrFileIndex);

			sFileText = sFileText.replace(/</gm, "&lt;");
			sFileText = sFileText.replace(/>/gm, "&gt;");

			// SINGLE LINE COMMENTS
			sFileText = sFileText.replace(/(\/\/)(.*)\n/g, "<font color='green'>$1$2</font><br>");

			// C STYLE COMMENTS
			sFileText = sFileText.replace(/(\/)(\*)/gm, "<br><font color='green'>$1$2");
			sFileText = sFileText.replace(/(\*\/)/gm, "$1</font><br>");

			// KEYWORDS
			sFileText = sFileText.replace(/#ifdef/g, "<font color=blue>#ifdef</font>");
			sFileText = sFileText.replace(/#ifndef/g, "<font color=blue>#ifndef</font>");
			sFileText = sFileText.replace(/#else/g, "<font color=blue>#else</font>");
			sFileText = sFileText.replace(/#endif/g, "<font color=blue>#endif</font>");
			sFileText = sFileText.replace(/#include/g, "<font color=blue>#include</font>");
			sFileText = sFileText.replace(/#define/g, "<font color=blue>#define</font>");
			sFileText = sFileText.replace(/#pragma/g, "<font color=blue>#pragma</font>");

			// LINE BREAKS
			sFileText = sFileText.replace(/\n/gmi, "<br>");

			var strInPageLink = "<p align=\"right\"><a href=\"javascript:FnGetBackTop();\">Back to top</a></p>";

			objSampleMgrCode.innerHTML = strInPageLink + sFileText + strInPageLink;
			objSampleMgrCode.scrollIntoView(true);
		}
		catch(errViewFileTextTwo)
		{	alert(errViewFileTextTwo.number + "\n" + errViewFileTextTwo.description + "\n" + L_ViewSampleError1_HTMLText);	}
	}
	catch(errViewFileTextThree)
	{	alert(errViewFileTextThree.number + "\n" + errViewFileTextThree.description + "\n" + L_ViewSampleError2_HTMLText);	}
}
//===
// TEST FUNCTION TO MIMIC ANCHOR BEHAVIOR
function FnGetBackTop()
{
	smpMgrCell.scrollIntoView(true);
	return;
}
//===
//	GET THE DIRECTORY TO COPY PROJECT TO HARD DRIVE
function fnGetDir(oSmp,iInt)
{
	var sDir = "My Documents";	// or "C:\\" or LOCAL PATH
	if (iInt == 0)
	{	sDir=oSmp.ChooseDirectory(sDir, L_ProjFileLoc_HTMLText);	}
	else
	{	sDir=oSmp.ChooseDirectory(sDir, L_ProjFileLoc_HTMLText + L_ProjFileLocCopy_HTMLText);	}
	return sDir;
}

//===
//	 COPY FILES TO LOCAL HARD DRIVE
function copyAll(sID,sSFL)
{
	try
	{	var oSmp = oSmpMgr.GetSampleFromId(g_sPageLoc,sID,sSFL);	}
	catch(errCopyAllOne)
	{	alert(errCopyAllOne.number + "\n" + errCopyAllOne.description + "\n" + L_SessObjSampNavError_HTMLText);	}

	try
	{	var iNumFiles = oSmp.FileCount;	}
	catch(errCopyAllTwo)
	{	alert(errCopyAllTwo.number + "\n" + errCopyAllTwo.description + "n" + L_CopySampleError1_HTMLText);	}

	try
	{
		var sTempDir=oSmp.GetDestinationDir;
		if(!sTempDir)
		{	sTempDir = fnGetDir(oSmp,0);	}
	}
	catch(errCopyAllThree)
	{	alert(errCopyAllThree.number + "\n" + errCopyAllThree.description + "\n" + L_CopySampleLocationError_HTMLText);	}

	if(sTempDir && sTempDir != "")
	{
		var bTmp = null;
		var sWhy = "";
		for(i=1;i<=iNumFiles;i++)
		{
			try
			{	oSmp.CopyFileAtIndex(i,sTempDir,0);	}
			catch(errCopyAllFour)
			{
				if(errCopyAllFour.number==-2147220585)
				{
					bTmp = confirm(L_ErrFileExists0_HTMLText + L_ErrFileExists1_HTMLText);
					if (bTmp == true)
					{
						OverWriteAll(sID,sSFL,sTempDir);
						return;
					}
					else
					{	break;	}
				}
				else
				{
					alert(errCopyAllFour.number + "\n" + errCopyAllFour.description + "\n" + L_CopyOverWriteError_HTMLText);
					break;
				}
			}
		}
	}
}

//===
//	OVERWRITE PREVIOUS SAMPLE LOCATION
function OverWriteAll(sID,sSFL,sTempDir)
{
	var oSmp = oSmpMgr.GetSampleFromId(g_sPageLoc,sID,sSFL);
	var iNumFiles = oSmp.FileCount;
	for(i=1;i<=iNumFiles;i++)
	{	oSmp.CopyFileAtIndex(i,sTempDir,1);	}
	return;
}

//===
//	LOAD SAMPLE PROJECT INTO NEW INSTANCE OF VS.NET
function LoadAll(sID,sSFL)
{
	var strTempProjExt = "";
	var strFinalFullPath = "";
	var strLoadFileFromHere = "";
	var strTmpFileName = "";

	try
	{
		//Get the samples from the navigation object
		var oSmp = oSmpMgr.GetSampleFromId(g_sPageLoc,sID,sSFL);
		var strTempDestDir = oSmp.DestinationDir;	//	PATH FROM SFL file -- alert(strTempDestDir);
		var iNumFiles = oSmp.FileCount;
	}
	catch(errLoadAllone)
	{	alert(errLoadAllone.number + "\n" + errLoadAllone.description + L_SessObjSampNavError_HTMLText);	}

	try
	{
		for(i=1;i<=iNumFiles;i++)
		{
			strTmpFileName = oSmp.GetFileNameAtIndex(i);
			if (strTmpFileName.lastIndexOf(".sln") != -1)
			{	strTempProjExt = strTmpFileName;		}
		}
	}
	catch(errLoadAllTwo)
	{	alert(errLoadAllTwo.number + "\n" + errLoadAllTwo.description + "\n" + L_LoadSolFileError_HTMLText);	}

	// LOAD SAMPLE AFTER COPYING TO TEMP LOCATION
	try
	{
		var sTempDir = oSmp.GetDestinationDir;

		if(!sTempDir)
		{	sTempDir = fnGetDir(oSmp,1);		}

		if(sTempDir && sTempDir != "")
		{
			/*	oSmp.LoadProject(sTempDir);		*/
			for(i=1;i<=iNumFiles;i++)
			{
				try
				{	oSmp.CopyFileAtIndex(i,sTempDir,0);	}
				catch(errCopy)
				{	//	alert(i + "\n" + errCopy.number + "\n" + errCopy.description);
					if (errCopy.number == -2147220585)
					{
						bTmp = confirm(L_ErrFileExists0_HTMLText + L_ErrFileExists1_HTMLText);
						if (bTmp == true)
						{
							OverWriteAll(sID,sSFL,sTempDir);
							break;
						}
						else
						{	return;	}
					}
				}
			}
			strLoadFileFromHere = strTempDestDir + "\\" + strTempProjExt;
			strFinalFullPath = String("\"" + sTempDir + "\\" + strLoadFileFromHere + "\"");

			try
			{
				var WshShell = new ActiveXObject("WScript.Shell");
				WshShell.Run("devenv " + strFinalFullPath, 1, true);
			}
			catch(errWSC)
			{	alert(errWSC.number + "\n" + errWSC.description + "\n" + L_WSloadSampleError_HTMLText);	}
		}
	}
	catch(errLoadAllThree)
	{	alert(errLoadAllThree.number + "\n" + errLoadAllThree.description + "\n" + L_LoadSolMainError_HTMLText );	}
	return;
}

//===
// INSERT TEXT STREAM INTO CURRENT PAGE
//===
function RuntimeSnR()
{
	try{
		var oChild = null;
		oChild = "<hr id='nDivider'><p>&nbsp;</p><div id='objSampleMgrCode' ></div><p>&nbsp;</p>";
		nstext.insertAdjacentHTML("BeforeEnd",oChild);
		sNST = "<DIV id=\"oSampDispDiv\">" + nstext.innerHTML + "</div>";
		sNST.replace("<!--Footer Start-->", "</DIV><!--Footer Start-->");
		nstext.innerHTML = sNST;								
	}
	catch(errSNR){}
}

//===
//	ADD SAMPLE Viewer TO CURRENT PAGE
//===
function addSample(sID,sSFL)
{
	if(bRuntimeSnR ==  false)
	{
		//Append a call to RuntimeSnr() to the end of the
		//window.onload event handler.
		var sFnDef = new String(window.onload);
		sFnDef = sFnDef.substring(sFnDef.indexOf("{")+1,sFnDef.length - 1) + "\nRuntimeSnR();\n";
		fnOnLoad = new Function(sFnDef);
		window.onload = fnOnLoad;
		bRuntimeSnR = true;
	}

	try
	{
		//Get the sample from the navigation object
		var oSmp = oSmpMgr.GetSampleFromId(g_sPageLoc,sID,sSFL);
		if (oSmp == null)
		{	return;	}
	}
	catch(errAddSamp)
	{
		alert(L_SessObjSampNavError_HTMLText);
		return;
	}


	try{
		
		//If placeholder text is displaying, overwrite it with innerHTML call
		//otherwise, append new files to the end of the div, this enables Sample Manager to 
		//display multiple instances.
		if (bPlaceholderDiv)
		{
			smpMgrCell.innerHTML = "<div title=\""+ L_SMTitle_HTMLText +"\" class='oSampMgrTitle' oncontextmenu=\"return false;\">"+ L_SMTitle_HTMLText +":</div>";
			bPlaceholderDiv = false;
		}else{
			smpMgrCell.insertAdjacentHTML("beforeEnd","<div title=\""+ L_SMTitle_HTMLText +"\" class='oSampMgrTitle' oncontextmenu=\"return false;\">"+ L_SMTitle_HTMLText +":</div>");
		}
		
		
		
		var oSampleMgr = window.document.createElement("DIV");
		var sSampleMgrID = document.uniqueID;
		oSampleMgr.className = "sampleMgr";
		oSampleMgr.id = sSampleMgrID;
		oSampleMgr.onContextMenu = "return false;"
		oSampleMgr.tabIndex=0;

	//	walk the sample
		try
		{	var iNumFiles = oSmp.FileCount;	}
		catch(errAddSampCount)
		{	alert(errAddSampCount.number + "\n" + errAddSampCount.description + "\n" + L_CopySampleError1_HTMLText);	}

		var oList = document.createElement("UL");
		oList.ID = "ulRoot";
		oList.style.display="block";
		oList.onclick=Toc_click;

		for(i=1;i<=iNumFiles;i++)
		{
			try
			{
				var sFileName = oSmp.GetFileNameAtIndex(i);
				find(sFileName, i, sID, sSFL, oList);
			}
			catch(errFileName)
			{	alert(errFileName.number + "\n" + errFileName.description + "\n" + L_SessObjSampNavError_HTMLText);	}
		}

		oSampleMgr.appendChild(oList);
		oSampleMgr.bgColor="menu";
		oSampleMgr.frColor="menutext";
		smpMgrCell.insertAdjacentElement("beforeEnd",oSampleMgr);

		var oLinks = window.document.createElement("DIV");
		var sFieldID =  "inp" + sSampleMgrID;
		oLinks.className = "sampleLinks";

		var sLoadText ="";

		if(!sLoadText || sLoadText == "")
		{	sLoadText = L_LoadSample_HTMLText;	}

		var sProjExt ="";

		try
		{	sProjExt = oSmp.ProjectFileExt;		}
		catch(err1)
		{	alert(err1.number + "\n" + err1.description + "\n" + L_SessObjSampNavError_HTMLText);	}

		if(!sProjExt || sProjExt == "" || sProjExt == "exe")
		{	sProjExt = "disabled=true";	}
		else
		{
			try
			{
				var WshShell = new ActiveXObject("WScript.Shell");
				var strTemp = WshShell.RegRead("HKEY_CLASSES_ROOT\\.sln\\");
				sProjExt = 'onclick="LoadAll(\''+sID+'\',\''+sSFL+'\');"';
			}
			catch(errNOVS)
			{	sProjExt = "disabled=true";	}
		}

		oLinks.innerHTML = "<DIV oncontextmenu=\"return false\" style='display: none;'><a href=\"javascript:\" title=\"" + sLoadText + "\" " + sProjExt + " >" + sLoadText + "</a></DIV>" +
				"<DIV oncontextmenu=\"return false\"><a href=\"javascript:\" title=\"" + L_CopyAll_HTMLText + "\" onclick=\"copyAll('" +sID+ "\','" + sSFL + "\');\" >" + L_CopyAll_HTMLText + "</a></DIV>" +
				"<DIV><MSHelp:link keywords=\"MSDN_Help_On_Sample_Manager\" TABINDEX=\"0\">" + L_Help_HTMLText + "</MSHelp:link></div><p>";

		oLinks.Sample=oSmp;
		smpMgrCell.insertAdjacentElement("beforeEnd",oLinks);
		smpMgrCell.style.display = "inline";
	}
	catch(err2)
	{	alert(err2.number + "\n" + err2.description + "\n" + L_SessObjSampNavError_HTMLText);	}
}

//Note: this recursive function searches through all the children of
//the UL to find the folder to attach a file to.  It's inefficient code
//since the more files, the longer each search, and recursion only makes it 
//worse. Consider rewriting to improve performance.

function find(sPath, iFileIdx, sID, sSFL, oList)
{	/* assumes that oList is an UL	*/
	var aFilePath = sPath.split("\\");
	var bDo = true;

	for(var j = 0; j<oList.children.length;j++)
	{
		if(oList.children[j].innerText.indexOf(aFilePath[0])==0)
		{
			iPos = sPath.indexOf("\\");

			//Note: Added check on .children[j].children[2]
			//to handle case where file occuring earlier in the 
			//the file list has the same name as a directory.  

			if ((iPos!=-1) && (oList.children[j].children[2] != null))
			{
				bDo = false;
				find(sPath.substring(iPos+1), iFileIdx, sID, sSFL, oList.children[j].children[2]);
			}
		}
	}
	if(bDo){
		iPos = sPath.indexOf("\\");
		if(iPos!=-1){
			var oLI = document.createElement("LI");
			var oLS = document.createElement("UL");
			var oA = document.createElement("A");
			var oIMG = document.createElement("IMG");

			oLS.className = "clsHidden";
			oA.innerText = aFilePath[0];
			oA.title = aFilePath[0];
			oA.onClick = "javascript:void(0);Toc_click()";
			oA.style.cursor = "hand";

			oIMG.onClick = "javascript:void(0);Toc_click()";
			oIMG.alt = aFilePath[0];
			oIMG.className = "clsHand";
			oIMG.width = 16;
			oIMG.height = 16;
			oIMG.hspace = 4;
			oIMG.src = L_defaultLocation_HTMLText + "greenfolder.gif";

			oLI.className = "kid";
			find(sPath.substring(iPos+1), iFileIdx, sID, sSFL, oLS);
			oLI.appendChild(oIMG);
			oLI.appendChild(oA);
			oLI.appendChild(oLS);
			oList.appendChild(oLI);
		}
		else
		{
			var oLI = document.createElement("LI");
			var oA = document.createElement("A");
			var oIMG = document.createElement("IMG");
			var iconType = aFilePath[0].substring(aFilePath[0].lastIndexOf(".")+1).toLowerCase();

			oA.innerText = aFilePath[0];
			oA.id = L_PopupMenuID1_HTMLText;
			oA.title = aFilePath[0];

			oIMG.alt = aFilePath[0];
			oIMG.id = L_PopupMenuID2_HTMLText;
			oIMG.width = 16;
			oIMG.height = 16;
			oIMG.hspace = 4;

			//	FOR ELEMENTS NOT CLICKABLE
			if (iconType == "fts" || iconType == "hlp" || iconType == "hpj" || iconType == "dib" || iconType == "sdl" || iconType == "dll" || iconType == "licenses" || iconType == "projdata" || iconType == "doc" || iconType == "chm" || iconType == "mdb" || iconType == "exe" || iconType == "msi" || iconType == "cur" || iconType == "gif" || iconType == "png" || iconType == "bmp" || iconType == "ico" || iconType == "jpg" || iconType == "rle" || iconType == "avi" || iconType == "wav" || iconType == "wma" || iconType == "wmv")
			{
				oA.className = "clsNoView";
				oIMG.className = "clsNoView";
			}
			else
			{
				oA.className = L_VisualNote_HTMLText;	//	oA.href = "javascript:view(" + iFileIdx + ",'"+sID+"','"+sSFL+"','"+ oA.id+"');";
				oA.href = "javascript:";
				oA.onClick = "view(" + iFileIdx + ",'"+sID+"','"+sSFL+"','"+ oA.id+"');";
				oIMG.className = "clsNoHand";		//	oIMG.onClick = "javascript:view(" + iFileIdx + ",'"+sID+"','"+sSFL+"','"+ oA.id+"');";
				oIMG.onClick = "view(" + iFileIdx + ",'"+sID+"','"+sSFL+"','"+ oA.id+"');";
			}

			if(sIcons.indexOf(iconType.toLowerCase())!=-1)
			{	oIMG.src =  L_defaultLocation_HTMLText + iconType + "-icon.gif";	}
			else
			{	oIMG.src =  L_defaultLocation_HTMLText + "bluepage.gif";	}

			oLI.appendChild(oIMG);
			oLI.appendChild(oA);

			var bDone = false;
			for(var k = 0; k<oList.children.length;k++)
			{
				if(oList.children[k].className=="kid")
				{
					oList.children[k].insertAdjacentElement("BeforeBegin",oLI);
					bDone = true;
					break;
				}
			}
			if(!bDone)
			{	oList.appendChild(oLI);	}
		}
	}
}

