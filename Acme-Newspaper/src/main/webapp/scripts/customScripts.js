$(document).ready(function() {
	$('#dateInput').tooltip();
	$('#vatInput').tooltip();
	$('#myInput').trigger('focus');
	
	$('#myTable').DataTable({
		"lengthMenu": [ 5,10,15 ],
		 "paging": false
	});
	
	$('#myTablePaginate').DataTable({
		"lengthMenu": [ 5,10,15 ]
	});
	
	$('#myTablePaginate2').DataTable({
		"lengthMenu": [ 4,10,15 ]
	});
});

// PHONE QUESTION
// Para usar la función poner onclick="patternPhone(spring_mensaje_pregunta, spring_mensaje_exitoso)" en el botón de save.
// Tiene que exisitir un <form ...> y un <input .. name="phone"/>
function checkPattern(str) {
	var patt = new RegExp(
			"^((([+]([1-9]|[1-9][0-9]|[1-9][0-9][0-9]) )([(]([1-9]|[1-9][0-9]|[1-9][0-9][0-9])[)] )([0-9]{4,}))|([+]([1-9]|[1-9][0-9]|[1-9][0-9][0-9]) [0-9]{4,})|[0-9]{4,})");
	var res = patt.test(str);
	return res;
}

function patternPhone(question) {
	var phone = document.getElementsByName('phone')[0].value;
	if (!checkPattern(phone))
		ask(question);
	else
		submitSave();
}

function ask(q) {
	quest = confirm(q);
	if (quest)
		submitSave();
}

function submitSave() {
	document.getElementById("save").name = "save";
	document.getElementsByTagName("form")[0].submit();
}

// CATEGORIES - Collapsable category tree
function abrir(id) {
	document.getElementById(id).getElementsByTagName("UL")[0].style.display = "";
	var enlace = document.getElementById(id).getElementsByTagName("button")[0];
	enlace.innerHTML = "[-]";
	enlace.setAttribute('onclick', "cerrar(" + id + ")");
}

function cerrar(id) {
	document.getElementById(id).getElementsByTagName("UL")[0].style.display = "none";
	var enlace = document.getElementById(id).getElementsByTagName("button")[0];
	enlace.innerHTML = "[+]";
	enlace.setAttribute('onclick', "abrir(" + id + ")");
}

// CREDIT CARD - Generate Brand from Number. If it has a unknown format brand will be: Other
function GetCreditCard(number) {
	document.getElementById("brand").value = this.calculate(number);
	document.getElementById("showBrand").innerHTML = this.calculate(number);
}

function calculate(number) {
	// visa
	var re = new RegExp("^4");
	if (number.match(re) != null)
		return "Visa";

	// Mastercard
	// Updated for Mastercard 2017 BINs expansion
	if (/^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$/.test(number))
		return "Mastercard";

	// AMEX
	re = new RegExp("^3[47]");
	if (number.match(re) != null)
		return "AMEX";

	// Discover
	re = new RegExp("^(6011|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[0-1][0-9]|92[0-5]|64[4-9])|65)");
	if (number.match(re) != null)
		return "Discover";

	// Diners
	re = new RegExp("^36");
	if (number.match(re) != null)
		return "Diners";

	// Diners - Carte Blanche
	re = new RegExp("^30[0-5]");
	if (number.match(re) != null)
		return "Diners - Carte Blanche";

	// JCB
	re = new RegExp("^35(2[89]|[3-8][0-9])");
	if (number.match(re) != null)
		return "JCB";

	// Visa Electron
	re = new RegExp("^(4026|417500|4508|4844|491(3|7))");
	if (number.match(re) != null)
		return "Visa Electron";

	return "Other";
}
