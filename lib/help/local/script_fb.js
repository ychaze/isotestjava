/*************************************************************************
 * WSS UX FeedBack Class
 * New format
 *************************************************************************/
/*************************************************************************
 * Constructor ***********************************************************
 *************************************************************************/
function FeedBack
 (
  _Alias,
  _Product,
  _Deliverable,
  _ProductVersion,
  _DocumentationVersion,
  _FeedBackDivID
 )
{
	this.Alias                = _Alias;
	this.Product              = _Product;
	this.Deliverable	  = _Deliverable;
	this.ProductVersion       = _ProductVersion;
	this.DocumentationVersion = _DocumentationVersion;
	this.FeedBackDivID	  = _FeedBackDivID;
	this.DefaultBody	  = this.DefaultBody;
}

/*************************************************************************
 *Member Properties ******************************************************
 *************************************************************************/

//START: Text Regions
FeedBack.prototype.FeedbackIntroduction = L_fbintroduction;
//  END: Text Regions
//START: Button Text
FeedBack.prototype.Submit    = L_fbsend;
FeedBack.prototype.AltSubmit = L_fbaltsend;
//  END: Button Text

//START: Default Mail Body Text
FeedBack.prototype.DefaultBody = L_fddefaultbody;
//  END: Default Mail Body Text

//CSS Class
FeedBack.prototype.table_CSS		= "fbtable";
FeedBack.prototype.tdtitle_CSS		= "fbtitletd";
FeedBack.prototype.input_CSS		= "fbinputtd";
FeedBack.prototype.textarea_CSS		= "fbtextarea";
FeedBack.prototype.verbatimtable_CSS	= "fbverbatimtable";
FeedBack.prototype.button_CSS		= "fbinputbutton";

//BTN IDs
FeedBack.prototype.YesButton_ID    = "YesButton";
FeedBack.prototype.NoButton_ID     = "NoButton";
FeedBack.prototype.BackButton_ID   = "BackButton";
FeedBack.prototype.NextButton_ID   = "NextButton";
FeedBack.prototype.SubmitButton_ID = "SubmitButton";
FeedBack.prototype.Verbatim_ID	   = "VerbatimTextArea";
FeedBack.prototype.Radio_ID	   = "fbRating";

//FeedBack Location ID's
FeedBack.prototype.SpanTag_ID = "fb";
FeedBack.prototype.DivTag_ID  = "feedbackarea";

//BTN Event Methods
FeedBack.prototype.startfeedback_EVENT      = "document.feedback.StartFeedBack([feedback])";
FeedBack.prototype.submitfeedback_EVENT     = "document.feedback.SubmitFeedBack([feedback])";

//Default FeedBack Values
FeedBack.prototype.Rating	   = 3; // default is 3. 3 is satisfied. 0-3 scale
FeedBack.prototype.Verbatim	   = "";
FeedBack.prototype.Title	   = document.title;
FeedBack.prototype.URL	= location.href.replace(location.hash,"");
FeedBack.prototype.SystemLanguage = navigator.systemLanguage;
FeedBack.prototype.Version	   = 2004;

/*************************************************************************
 * Member Methods ********************************************************
 *************************************************************************/

 FeedBack.prototype.StartFeedBack = _StartFeedBack;
 function _StartFeedBack(FeedBackSpanTag,first)
 {
  //build feedback div

if (first==true)
{
var stream =  '<DIV ID="feedbackarea">'
	+ '<FORM METHOD="post" ENCTYPE="text/plain" NAME="formRating">'
	+ '<H5 STYLE="margin-bottom:0.7em;">' + L_fb1Title_Text + '</H5>'
	+ '<P>' + L_fbintroduction + '</P>'
	+ "<table>"
	+ "<tr>"
	+ "<td>" + L_fb1Poor + "</td>"
	+ this.MakeRadio(0,"1")
	+ this.MakeRadio(0,"2")
	+ this.MakeRadio(0,"3")
	+ this.MakeRadio(0,"4")
	+ this.MakeRadio(0,"5")
	+ "<td>" + L_fb1Excellent + "</td>"
	+ "</tr>"
	+ "</table>"
	+ '<P>' + L_fb1EnterFeedbackHere_Text + '&nbsp;&nbsp;&nbsp;&nbsp;'
	+ this.MakeButton(this.SubmitButton_ID, L_fbsend, this.submitfeedback_EVENT.replace("[feedback]",this.FeedBackDivID)) + '</P>'
	+ '</FORM>'
	+ '</div>';
}
else 
{
var stream =  '<DIV ID="feedbackarea">'
	+ '<FORM METHOD="post" ENCTYPE="text/plain" NAME="formRating">'
	+ '<H5 STYLE="margin-bottom:0.7em;">' + L_fb1Title_Text + '</H5>'
	+ '<P>' + L_fbintroduction + '</P>'
	+ "<table>"
	+ "<tr>"
	+ "<td>" + L_fb1Poor + "</td>"
	+ this.MakeRadio(0,"1")
	+ this.MakeRadio(0,"2")
	+ this.MakeRadio(0,"3")
	+ this.MakeRadio(0,"4")
	+ this.MakeRadio(0,"5")
	+ "<td>" + L_fb1Excellent + "</td>"
	+ "</tr>"
	+ "</table>"
	+ '<P>' + L_fb1EnterFeedbackHere_Text + '&nbsp;&nbsp;&nbsp;&nbsp;'
	+ this.MakeButton(this.SubmitButton_ID, L_fbsend, this.submitfeedback_EVENT.replace("[feedback]",this.FeedBackDivID)) + '&nbsp;&nbsp;&nbsp;<B>' + L_fb1Acknowledge + '</B>' + '</P>'
	+ '</FORM>'
	+ '</div>';
}
  
  //load feedback div
  FeedBackSpanTag.innerHTML = stream;
 }
 
 FeedBack.prototype.MakeRadio = _MakeRadio;
 function _MakeRadio(val,txt)
 {
  var stream = "<td class='" + this.input_CSS + "' align=right>"
		+ txt + "<BR><input name=" + this.Radio_ID + " type=radio value=" + val + " onclick='" + this.setrating_EVENT + "' "
		+ " " + ((this.Rating == val) ? "CHECKED" : "") + ">" + "</input></td>"
  return stream;
 }
 
 FeedBack.prototype.MakeButton = _MakeButton;
 function _MakeButton(id, val, evt)
 {
  var stream = "<input id='submitFeedback' type=button id=" + id +" value=\"" + val + "\" onclick=\"" + evt + "\">"
	
  return stream;
 }
 
 FeedBack.prototype.SubmitFeedBack = _SubmitFeedBack;
 function _SubmitFeedBack()
 {
  if(event.srcElement.id == this.YesButton_ID)
  {
   this.Rating = 3;
   this.Verbatim = this.DefaultBody;
  }
  
  var subject = this.Title
  + " ("
  + "/1:"
  + this.Product
  + "/2:"
  + this.ProductVersion
  + "/3:"
  + this.DocumentationVersion
  + "/4:"
  + this.DeliverableValue()
  + "/5:"
  + this.URLValue()
  + "/6:"
  + GetRating()	//  + this.Rating
  + "/7:"
  + this.DeliveryType()
  + "/8:"
  + this.SystemLanguage
  + "/9:"
  + this.Version
		+ ")"; 

  var sEntireMailMessage = "MAILTO:"
  + this.Alias
  + "?subject=" + subject 
	 + "&body=" + ((this.Verbatim != "") ? this.Verbatim : this.DefaultBody);

  location.href=sEntireMailMessage;
  
  first = false;
  document.feedback.StartFeedBack(fb,first);

  return;
 }
 
 FeedBack.prototype.CheckDeliverable = _CheckDeliverable;
 function _CheckDeliverable()
 {
  var stream = "CheckDeliverable";
  
  alert(stream);
 }

 FeedBack.prototype.SetRating = _SetRating;
 function _SetRating(val)
 {
  this.Rating = val;
 }

 FeedBack.prototype.ReloadFeedBack = _ReloadFeedBack;
 function _ReloadFeedBack()
 {
  location.reload(true);
 }
 
 FeedBack.prototype.ScrollToFeedBack = _ScrollToFeedBack;
 function _ScrollToFeedBack(FeedBackSpanTag)
 {
  window.scrollTo(0,10000);
  FeedBackSpanTag.scrollIntoView(true);
 }
 
 FeedBack.prototype.SetVerbatim = _SetVerbatim;
 function _SetVerbatim(TextAreaValue)
 {
  this.Verbatim = TextAreaValue;
 }
 
FeedBack.prototype.DeliveryType = _DeliveryType;
function _DeliveryType()
{
 if (this.URL.indexOf("ms-help://")!=-1) {return("h");}
	else if (this.URL.indexOf(".chm::/")!=-1) {return("c");}
	else if (this.URL.indexOf("http://")!=-1) {return("w");}
	else if (this.URL.indexOf("file:")!=-1) {return("f");}
	else return("0");
}
FeedBack.prototype.DeliverableValue = _DeliverableValue;
function _DeliverableValue()
{
 if (this.URL.indexOf("ms-help://")!=-1) 
	{
	delvalue  = location.href.slice(0,location.href.lastIndexOf("/html/"));
	delvalue  = delvalue.slice(delvalue.lastIndexOf("/")+1);
	return delvalue;
	}
	else return(this.Deliverable);
}
FeedBack.prototype.URLValue = _URLValue;
function _URLValue()
{
 if (this.URL.indexOf(".chm::")!=-1) 
	{
	a = this.URL;
	while (a.indexOf("\\") < a.indexOf(".chm::") || a.indexOf("//") > a.indexOf(".chm::")) {
		if (a.indexOf("\\")==-1)
		{
		break;
		}
		a = a.substring(a.indexOf("\\")+1,a.length);
	}
	return("ms-its:"+a)
	}
 else if (this.URL.indexOf("file:///")!=-1) 
	{
	a = this.URL;

	b = a.substring(a.lastIndexOf("html")+5,a.length);
	return("file:///"+b);
	}
	else return(this.URL);
}


//---Gets topic rating.---
function GetRating()
{

	sRating = "0";
	for(var x = 0;x < 5;x++)
  	{
      		if(document.formRating.fbRating(x).checked) {sRating = x + 1;}
  	}
	return sRating;
}

function altFeedback(src) {
	src.title=L_fbaltIcon;
	return;
	}

function HeadFeedBack(HeadFeedBackSpanTag)
{
 
 var sstream =  '<DIV ID="headfeedbackarea">'
	+ '<a href="#Feedback" onmouseover=altFeedback(this) ID="IconFB" Target="_self">'
        + '<img id="feedb" src="../local/pencil.gif">'
        + '</img>'
	+ L_fbsend
        + '</a>'
	+ '</div>';
  
  //load feedback div
  HeadFeedBackSpanTag.innerHTML = sstream;
 }


/*************************************************************************
 * WSS UX FeedBack Class (old style)
 *      Date: 2/8/2004
 *************************************************************************/

/*************************************************************************
 * Constructor ***********************************************************
 *************************************************************************/
function FdBack
 (
  _Alias,
  _Product,
  _Deliverable,
  _ProductVersion,
  _DocumentationVersion,
  _FeedBackDivID
 )
{
	this.Alias                = _Alias;
	this.Product              = _Product;
	this.Deliverable	  = _Deliverable;
	this.ProductVersion       = _ProductVersion;
	this.DocumentationVersion = _DocumentationVersion;
	this.FeedBackDivID	  = _FeedBackDivID;
	this.DefaultBody	  = this.DefaultBody;
}

/*************************************************************************
 *Member Properties ******************************************************
 *************************************************************************/

//START: Text Regions
FdBack.prototype.FdBackIntroduction = L_fdintro;
FdBack.prototype.WhyWrong             = L_fdwhywrong;
FdBack.prototype.WhatWrong		= L_fdwhatwrong;
FdBack.prototype.InformationWrong     = L_fdinfowrong;
FdBack.prototype.NeedsMore            = L_fdneedsmore;
FdBack.prototype.NotExpected          = L_fdnotexpected;
//  END: Text Regions

//START: Button Text
FdBack.prototype.Yes       = L_fdyes;
FdBack.prototype.No        = L_fdno;
FdBack.prototype.Back      = L_fdback;
FdBack.prototype.Next      = L_fdnext;
FdBack.prototype.Submit    = L_fdsubmit;
FdBack.prototype.AltYes    = L_fdaltyes;
FdBack.prototype.AltNo     = L_fdaltno;
FdBack.prototype.AltBack   = L_fdaltback;
FdBack.prototype.AltNext   = L_fdaltnext;
FdBack.prototype.AltSubmit = L_fdaltsubmit;
//  END: Button Text

//START: Default Mail Body Text
FdBack.prototype.DefaultBody = L_fddefaultbody;
//  END: Default Mail Body Text

//CSS Class
FdBack.prototype.table_CSS		= "fdbackarea";
FdBack.prototype.tdtitle_CSS		= "fdbackarea";
FdBack.prototype.input_CSS		= "fdbackinput";
FdBack.prototype.textarea_CSS		= "fbtextarea";
FdBack.prototype.verbatimtable_CSS	= "fbverbatimtable";
FdBack.prototype.button_CSS		= "fdbackinputbutton";

//BTN IDs
FdBack.prototype.YesButton_ID    = "YesButton";
FdBack.prototype.NoButton_ID     = "NoButton";
FdBack.prototype.BackButton_ID   = "BackButton";
FdBack.prototype.NextButton_ID   = "NextButton";
FdBack.prototype.SubmitButton_ID = "SubmitButton";
FdBack.prototype.Verbatim_ID	   = "VerbatimTextArea";
FdBack.prototype.Radio_ID	   = "fbRating";

//FeedBack Location ID's
FdBack.prototype.SpanTag_ID = "fb";
FdBack.prototype.DivTag_ID  = "fdbackarea";

//BTN Event Methods
FdBack.prototype.startFdBack_EVENT      = "document.FdBack.StartFdBack([feedback])";
FdBack.prototype.getwatsoncurvedata_EVENT = "document.FdBack.GetWatsonCurveData([feedback])";
FdBack.prototype.getverbatim_EVENT        = "document.FdBack.GetVerbatim([feedback])";
FdBack.prototype.submitFdBack_EVENT     = "document.FdBack.SubmitFdBack()";
FdBack.prototype.setrating_EVENT	    = "document.FdBack.SetRating(this.value)";
FdBack.prototype.textareablur_EVENT	    = "document.FdBack.SetVerbatim(this.value)";

//Default FeedBack Values
FdBack.prototype.Rating	   = 3; // default is 3. 3 is satisfied. 0-3 scale
FdBack.prototype.Verbatim	   = "";
FdBack.prototype.Title	   = document.title;
FdBack.prototype.URL	= location.href.replace(location.hash,"");
FdBack.prototype.SystemLanguage = navigator.systemLanguage;
FdBack.prototype.Version	   = 2004;

/*************************************************************************
 * Member Methods ********************************************************
 *************************************************************************/

 FdBack.prototype.StartFdBack = _StartFdBack;
 function _StartFdBack(FdBackSpanTag)
 {
  //build feedback div
  var stream = "<div id='" + this.DivTag_ID + "'>"
		+ "<table class='"+this.table_CSS+"'>"
		+ 	"<tr><td colspan='2' class='" + this.tdtitle_CSS + "'>" + L_fdintro + "</td></tr>"
		+	"<tr><td colspan='2' class='" + this.input_CSS + "' align=right>"
		+	this.MakeButton(this.YesButton_ID, this.Yes, this.submitFdBack_EVENT.replace("[feedback]",this.FeedBackDivID))
		+ 	this.MakeButton(this.NoButton_ID,  this.No,  this.getwatsoncurvedata_EVENT.replace("[feedback]",this.FeedBackDivID))
		+	"</td></tr>"
		+ "</table>"
  + "</div>";
  
  //load feedback div
  FdBackSpanTag.innerHTML = stream;
 }
 
 FdBack.prototype.GetWatsonCurveData = _GetWatsonCurveData;
 function _GetWatsonCurveData(FdBackSpanTag)
 {
  if(this.Rating > 2) this.Rating = 0;
  
  var stream = "<div id='" + this.DivTag_ID + "'>"
		+ "<table class='"+this.verbatimtable_CSS+"'>"
		+ "<tr><td colspan='2' class='" + this.tdtitle_CSS + "'>" + this.WhyWrong + "</td></tr>"
		+ this.MakeRadio(0, this.InformationWrong)
		+ this.MakeRadio(1, this.NeedsMore)
		+ this.MakeRadio(2, this.NotExpected)
		+ "<tr><td colspan='2' class='" + this.input_CSS + "' align=right>"
		+ this.MakeButton(this.BackButton_ID, this.Back, this.startFdBack_EVENT.replace("[feedback]",this.FeedBackDivID))
//Following commented to supress verbatim and the line after enables mail
//		+ this.MakeButton(this.NextButton_ID, this.Next, this.getverbatim_EVENT.replace("[feedback]",this.FeedBackDivID))
		+ this.MakeButton(this.SubmitButton_ID, this.Submit,this.submitFdBack_EVENT.replace("[feedback]",this.FeedBackDivID))
		+ "</td></tr>"
		+ "</table>"
  + "</div>";
  
  //load feedback div
  FdBackSpanTag.innerHTML = stream;
  
   //scroll down to feedback  
  this.ScrollToFdBack(FdBackSpanTag);
 }

// FdBack.prototype.GetVerbatim = _GetVerbatim;
 function _GetVerbatim(FdBackSpanTag)
 {
  var stream = "<div id='" + this.DivTag_ID + "'>"
		+ "<table class='"+this.verbatimtable_CSS+"'>"
		+ "<tr><td colspan='2' class='" + this.tdtitle_CSS + "'>" + this.WhatWrong + "</td></tr>"
		+ "<tr><td colspan='2' class='" + this.input_CSS + "'><textarea class="+ this.textarea_CSS +" name=" + this.Verbatim_ID + " maxlength=750 onblur=\"" + this.textareablur_EVENT.replace("[textarea]",this.Verbatim_ID) + "\">" + this.Verbatim + "</textarea></td></tr>"
		+ "<tr><td colspan='2' class='" + this.input_CSS + "' align=right>"
		+ this.MakeButton(this.BackButton_ID, this.Back, this.getwatsoncurvedata_EVENT.replace("[feedback]",this.FeedBackDivID))
		+ this.MakeButton(this.SubmitButton_ID, this.Submit, this.submitFdBack_EVENT.replace("[feedback]",this.FeedBackDivID))
		+ "</td></tr>"
		+ "</table>"
  + "</div>";
  
  //load feedback div
  FdBackSpanTag.innerHTML = stream;
  
  //scroll down to feedback  
  this.ScrollToFdBack(FdBackSpanTag);  
 }
 
 FdBack.prototype.MakeRadio = _MakeRadio1;
 function _MakeRadio1(val, txt)
 {
  var stream = "<tr><td colspan='2' class='" + this.input_CSS + "'>"
		+ "<input name=" + this.Radio_ID + " type=radio value=" + val + " onclick='" + this.setrating_EVENT + "' "
		+ " " + ((this.Rating == val) ? "CHECKED" : "") + ">" + txt + "</input>"
		+ "</tr>"
  
  return stream;
 }
 
 FdBack.prototype.MakeButton = _MakeButton1;
 function _MakeButton1(id, val, evt)
 {
  var stream = "<input class=" + this.button_CSS + " type=button id=" + id +" value=" + val + " onclick=\"" + evt + "\">"
	
  return stream;
 }
 
 FdBack.prototype.SubmitFdBack = _SubmitFdBack;
 function _SubmitFdBack()
 {
  if(event.srcElement.id == this.YesButton_ID)
  {
   this.Rating = 3;
   this.Verbatim = this.DefaultBody;
  }
  
  var subject = this.Title
  + " ("
  + "/1:"
  + this.Product
  + "/2:"
  + this.ProductVersion
  + "/3:"
  + this.DocumentationVersion
  + "/4:"
  + this.DeliverableValue()
  + "/5:"
  + this.URLValue()
  + "/6:"
  + this.Rating
  + "/7:"
  + this.DeliveryType()
  + "/8:"
  + this.SystemLanguage
  + "/9:"
  + this.Version
		+ ")"; 

  var sEntireMailMessage = "MAILTO:"
  + this.Alias
  + "?subject=" + subject 
	 + "&body=" + ((this.Verbatim != "") ? this.Verbatim : this.DefaultBody);

  location.href=sEntireMailMessage;
  
  fdbackarea.style.display="none";
 }
 
 FdBack.prototype.CheckDeliverable = _CheckDeliverable;
 function _CheckDeliverable()
 {
  var stream = "CheckDeliverable";
  
  alert(stream);
 }

 FdBack.prototype.SetRating = _SetRating;
 function _SetRating(val)
 {
  this.Rating = val;
 }

 FdBack.prototype.ReloadFdBack = _ReloadFdBack;
 function _ReloadFdBack()
 {
  location.reload(true);
 }
 
 FdBack.prototype.ScrollToFdBack = _ScrollToFdBack;
 function _ScrollToFdBack(FdBackSpanTag)
 {
  window.scrollTo(0,10000);
  FdBackSpanTag.scrollIntoView(true);
 }
 
 FdBack.prototype.SetVerbatim = _SetVerbatim;
 function _SetVerbatim(TextAreaValue)
 {
  this.Verbatim = TextAreaValue;
 }
 
FdBack.prototype.DeliveryType = _DeliveryType;
function _DeliveryType()
{
 if (this.URL.indexOf("ms-help://")!=-1) {return("h");}
	else if (this.URL.indexOf(".chm::/")!=-1) {return("c");}
	else if (this.URL.indexOf("http://")!=-1) {return("w");}
	else if (this.URL.indexOf("file:")!=-1) {return("f");}
	else return("0");
}
FdBack.prototype.DeliverableValue = _DeliverableValue;
function _DeliverableValue()
{
 if (this.URL.indexOf("ms-help://")!=-1) 
	{
	delvalue  = location.href.slice(0,location.href.lastIndexOf("/html/"));
	delvalue  = delvalue.slice(delvalue.lastIndexOf("/")+1);
	return delvalue;
	}
	else return(this.Deliverable);
}
FdBack.prototype.URLValue = _URLValue;
function _URLValue()
{
 if (this.URL.indexOf(".chm::")!=-1) 
	{
	a = this.URL;
	while (a.indexOf("\\") < a.indexOf(".chm::") || a.indexOf("//") > a.indexOf(".chm::")) {
		if (a.indexOf("\\")==-1)
		{
		break;
		}
		a = a.substring(a.indexOf("\\")+1,a.length);
	}
	return("ms-its:"+a)
	}
 else if (this.URL.indexOf("file:///")!=-1) 
	{
	a = this.URL;

	b = a.substring(a.lastIndexOf("html")+5,a.length);
	return("file:///"+b);
	}
	else return(this.URL);
}