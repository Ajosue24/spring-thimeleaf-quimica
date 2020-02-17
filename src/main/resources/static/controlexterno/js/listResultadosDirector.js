$("#ano").change(function () {
    $("#muestra").load('/controlexterno/obtMuestrasxAno', 'ano=' + $("#ano").val() + '');
    setTimeout(function(){
     	if($('#muestra').val()==null){
     		$("#muestra").append("<option value='0' selected='selected'>Seleccione... </option>");
     	}
    },100);
});



$(document).ready(function () {
    $("#buscar2").click(function () {
    	if($('#muestra').val()!='0' && $('#muestra').val()!=null){
    		window.location.replace("/controlexterno/listResultadosDirector" + "?ano=" + $('#ano').val() + "&muestra=" + $('#muestra').val() + "&laboratorio=" + $('#laboratorio').val()+ "&sede=" + $('#sede').val()+ "&usuario=" + $('#usuario').val()+ "&estado=" + $('#estado').val());
    	}
    });
});


$(document).ready(function () {
    $("#auditoria").click(function () {	
        window.location.replace("/controlexterno/auditoria" + "?muestraAudi=" + $('#muestra').val());
    });
});




/*
function openDeleteModal(resultado) {
   var opcion = confirm("¿Está seguro de eliminar el registro?");
    if (opcion == true) {
        window.location.replace("/controlexterno/deleteResultadoDirector?idInscripcionMuestras=" + resultado + '');
	} 
  
}*/
/*
function openDeleteModal(metodo) {
    //$('#idMetodoD').val(metodo)
    $('#openDeleteModal').modal('show');
}

function borrarMetodos() {
    window.location.replace("/metodos/delete?metodoId=" + $('#idMetodoD').val() + '');
    $('#idMetodoD').val('');
    $('#openDeleteModal').modal('hide');
}
*/
function openDeleteModal(resultado,codProasecal) {
    $('#idResultadoD').val(resultado);
    $('#exampleModalLongTitle').text('Eliminar resultado para el usuario '+codProasecal);
}

function borrarResultados() {
    if ($('#justificacion').val() != "" && $('#autorizadoPor').val() != "") {
        window.location.replace("/controlexterno/deleteResultadoDirector?idResultadoT=" + $('#idResultadoD').val() + '' + '&justificacionT=' + $('#justificacion').val() + '' + '&autorizadoPorT=' + $('#autorizadoPor').val() + '');
    }
}

function check(e) {
    tecla = (document.all) ? e.keyCode : e.which;
	var permitidos = [8,32,42,47,43,45,33,44,209,95,191,161,63,59,46,241];
	if (permitidos.indexOf( tecla ) == -1 ) {    
	    // Patron de entrada, en este caso solo acepta numeros y letras
	    patron = /[A-Za-z0-9]/;
	    tecla_final = String.fromCharCode(tecla);
	    return patron.test(tecla_final);
	}
}




$(document).ready(function () {
    $("#simularT").click(function () {
        window.location.replace("/controlexterno/gestionMuestras/" +  $('#muestra').val());
    });
});

$(document).ready(function(){
	muestra=$('#muestra').val();

    if (muestra != "0") {
        if($('#muestra').val()!="0" && $('#muestra').val()!=""&& $('#muestra').val()!=null){
    		$('#mensajeResultado').hide();
    		$('#detalleMuestra').show();
    		$('#detalleMuestraUsuarios').show();    		
	        $.ajax({
	            url: '/rest/detMuestra/' + $('#muestra').val() + '',
	            success: function (data) {

	                $("#numeroM").text(data.numeroMuestra);
	                $("#tipoM").text(data.idTipoMuestra.nombre);
	                $("#fechaIM").text(data.fechaInicial.substr(0,10));
	                $("#fechaFM").text(data.fechaFinal.substr(0,10));
	               if(data.fechaBloqueo!=null){ $("#fechaBM").text(data.fechaBloqueo.substr(0,10));}
	               if(data.fechaCierre!=null){ $("#fechaCM").text(data.fechaCierre.substr(0,10));}
	               if(data.fechaLibResultado!=null){$("#fechaLRM").text(data.fechaLibResultado.substr(0,10));}                
	           
	            }
	        }); 
    	}
        else{
        	$('#detalleMuestra').hide();
       		$('#detalleMuestraUsuarios').hide();         	
        } 
    }


});
/*
window.onload=cargarTabla;

function cargarTabla() {
	
    if ($('#muestra').val()  != "0"&& $('#ano').val()!="") {
    	pagPrevia=document.referrer;
    	if(pagPrevia.indexOf("resultadosParticipante") > -1){
    		$('#buscar2').trigger('click');
    	}
    }
}*/

$(document).ready(function () {
	document.querySelector('.preload').style.setProperty('display', 'none');
});
