$(document).ready(function () {
    jQuery.datetimepicker.setLocale('es');
    jQuery('.datetimepicker').datetimepicker();
});

function decargarAuditoria(){
	
	DateA=$('#fechaDesde').val();
	DateB=$('#fechaHasta').val();
	if(DateA==''||DateB==''){
		alert('Las fecha desde y hasta son obligatorias');
	}
	else{
		if (Date.parse(DateB)<Date.parse(DateA)){
			alert('La fecha desde es mayor a la fecha hasta');
		}
		else{
			 window.location.replace("/controlexternoinforme/downloadAuditoria?desde="+DateA+"&hasta="+DateB);
		}
	}
	



}

