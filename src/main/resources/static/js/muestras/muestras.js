$(document).ready(function(){
	$("#idPrograma").change(function () {
	    $("#idTipoMuestra").load('/muestras/obtTipoMuestra', 'idPrograma=' + $("#idPrograma").val() + '');
	    $('#idTipoMuestra').select2({placeholder: "Seleccione ..."});
	});	

});

function HabCampos(){
    if($("#idPrograma :selected").text()=='PRUEBA DE EFICIENCIA EN INMUNOHEMATOLOGIA LABORATORIOS'){
    		document.getElementById("numero1").disabled = false;
    		document.getElementById("numero2").disabled = false;
    		document.getElementById("numero3").disabled = false;    		
    	}
    else
    	{
    		document.getElementById("numero1").disabled = true;
    		document.getElementById("numero2").disabled = true;
    		document.getElementById("numero3").disabled = true;
    	}
    
}


 document.getElementById("editor-one").addEventListener("DOMNodeInserted", ActualizarDatos, true);
 function ActualizarDatos(){
	 setTimeout(function(){
		 $("#observacionMuestra").val($("#editor-one").html()) 
	 },500);	 
 }

 
 function ActObservacion(){
	 $("#editor-one").html($("#observacionMuestra").val())
 }
 
 $(document).ready(function(){
	    $('#idPrograma').select2();
	    $('#idTipoMuestra').select2();
	    document.getElementById("form").style.display = "block";
	});