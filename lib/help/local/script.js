// updated from DevDiv
var jsPath = scriptPath();

writeCSS(jsPath);

function scriptPath() { 
//Determine path to JS-the CSS is in the same directory as the script
	var spath = document.scripts[document.scripts.length - 1].src;
	spath = spath.toLowerCase();
	return spath.replace("script.js", "");
}

function writeCSS(spath) {
	document.writeln('<SCRIPT SRC="' + spath + '\script_loc.js"></SCRIPT>');
	document.writeln('<SCRIPT SRC="' + spath + '\script_main.js"></SCRIPT>');
	document.writeln('<SCRIPT SRC="' + spath + '\script_fb.js"></SCRIPT>');
}
