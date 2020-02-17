$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
    	 window.location.replace("/periodosvigencia/list?size=" + this.value + "&page=1" + "&idLaboratoriosSedes=" + $('#idLaboratoriosSedes').val() + "&sede=" + $('#idsedes').val() + "&usuario=" + $('#idusuarios').val() + "&programa=" + $('#inscripProgramaId').val() + "&modalidad=" + $('#modalidad').val() + "&muestra=" + $('#muestra').val() + "&enmora=" + $('#enmora').is(":checked")  +"&muestraPatrocinada=" +  $('#muestraPatrocinada').is(":checked") + "&clientePatrocinador=" + $('#clientePatrocinador').val() + "&fechaInicio=" + $('#fechaInicio').val() +  "&fechaFin=" + $('#fechaFin').val()+"&nameColumn=" + nameColumn +  "&sortBy=" + sort );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/periodosvigencia/list" +  "?idLaboratoriosSedes=" + $('#idLaboratoriosSedes').val() + "&sede=" + $('#idsedes').val() + "&usuario=" + $('#idusuarios').val() + "&programa=" + $('#inscripProgramaId').val() + "&modalidad=" + $('#modalidad').val() + "&muestra=" + $('#muestra').val() + "&enmora=" + $('#enmora').is(":checked")  +"&muestraPatrocinada=" +  $('#muestraPatrocinada').is(":checked") + "&clientePatrocinador=" + $('#clientePatrocinador').val() + "&fechaInicio=" + $('#fechaInicio').val() +  "&fechaFin=" + $('#fechaFin').val()  +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {

    if (sort == "ASC") {
        window.location.replace("/periodosvigencia/list"  + "?idLaboratoriosSedes=" + $('#idLaboratoriosSedes').val() + "&sede=" + $('#idsedes').val() + "&usuario=" + $('#idusuarios').val() + "&programa=" + $('#inscripProgramaId').val() + "&modalidad=" + $('#modalidad').val() + "&muestra=" + $('#muestra').val() +  "&enmora=" + $('#enmora').is(":checked")  +"&muestraPatrocinada=" +  $('#muestraPatrocinada').is(":checked")+ "&clientePatrocinador=" + $('#clientePatrocinador').val() + "&fechaInicio=" + $('#fechaInicio').val() +  "&fechaFin=" + $('#fechaFin').val()  +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/periodosvigencia/list"  + "?idLaboratoriosSedes=" + $('#idLaboratoriosSedes').val() + "&sede=" + $('#idsedes').val() + "&usuario=" + $('#idusuarios').val() + "&programa=" + $('#inscripProgramaId').val() + "&modalidad=" + $('#modalidad').val() + "&muestra=" + $('#muestra ').val() +  "&enmora=" + $('#enmora').is(":checked")  +"&muestraPatrocinada=" +  $('#muestraPatrocinada').is(":checked") + "&clientePatrocinador=" + $('#clientePatrocinador').val() + "&fechaInicio=" + $('#fechaInicio').val() +  "&fechaFin=" + $('#fechaFin').val()  +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }
}


function openDeleteModal(perPrograma) {
    $('#idPerVigenciaD').val(perPrograma)
    $('#openDeleteModal').modal('show');
}

function borrarPerVigencia() {

    window.location.replace("/periodosvigencia/delete?periodosvigenciaId=" + $('#idPerVigenciaD').val() + '');
    $('#idPerVigenciaD').val('');
    $('#openDeleteModal').modal('hide');
}

$(document).ready(function () {
    $("#add").click(function () {
    	debugger;
    	console.log( "usuario" + $('#idusuarios').val());
        window.location.replace("/periodosvigencia/create" +  "?idLaboratoriosSedes=" + $('#idLaboratoriosSedes').val() + "&sede=" + $('#idsedes').val() + "&usuario=" + $('#idusuarios').val() + "&programa=" + $('#inscripProgramaId').val() + "&modalidad=" + $('#modalidad').val() + "&muestra=" + $('#muestra	').val() + "&enmora=" + $('#enmora').is(":checked")  +"&muestraPatrocinada=" +  $('#muestraPatrocinada').is(":checked") + "&clientePatrocinador=" + $('#clientePatrocinador').val() + "&fechaInicio=" + $('#fechaInicio').val() +  "&fechaFin=" + $('#fechaFin').val());
    });
});
/*
$('#idlaboratorio').select2({
	  ajax: {		  	  
		  	url: '/rest/laboratorios',
		  	
		    processResults: function (data) {
		    	  return {
		    	results: $.map(data, function(obj) {
		            return { id: obj.idLaboratoriosSedes, text: obj.razonSocial };
		        })
		    	  }
		    }
	  }
});


$('#idlaboratorio').on('select2:select', function (e) {
    var idLaboratorio = e.params.data.id;
   
    $('#idSede').select2({

  	  ajax: {		  	  
  		  	url: '/rest/programas2'+ idLaboratorio,
  		  	
  		    processResults: function (data) {
  		    	  return {
  		    	results: $.map(data, function(obj) {
  		            return { id: obj.programaId, text: obj.nombrePrograma };
  		        })
  		    	  }
  		    }
  	  }
  	});
});
*//*
$(document).ready(function(){
	$("#idLaboratoriosSedes").change(function () {
	
	    $("#idsedes").load('/periodosvigencia/obtSedes', 'idLaboratorio=' + $("#idLaboratoriosSedes").val() + '');
	    $('#idsedes').select2({placeholder: "Seleccione ..."});
	});
	
	$("#idsedes").change(function () {
	    $("#idusuarios").load('/periodosvigencia/obtUsuarios', 'idSede=' + $("#idsedes").val() + '');
	    $('#idusuarios').select2({placeholder: "Seleccione ..."});
	});
	
	$("#idusuarios").change(function () {
	    $("#inscripProgramaId").load('/periodosvigencia/obtInsProgramas', 'idusuario=' + $("#idusuarios").val() + '');
	    $('#inscripProgramaId').select2({placeholder: "Seleccione ..."});
	});	
});*/
$(document).ready(function(){
    $('#idLaboratoriosSedes').select2();
    $('#inscripProgramaId').select2();
    $('#perVigenciaRegistrados').select2();
    $('#modalidad').select2();
    $('#clientePatrocinador').select2();
    $('#idsedes').select2();
    $('#idusuarios').select2();
    $('#muestra').select2();    
  /*  $('#idLaboratoriosSedes').select2({
        ajax: {
            url: "/rest/autoCompLaboratorio/",
            processResults: function (data) {
                return {
                    results: $.map(data, function (obj) {
                        debugger;
                        return {
                            id: obj.numeroIdentificacion+" l "+obj.razonSocial.trim(),
                            text: obj.numeroIdentificacion + " l " + obj.razonSocial.trim()
                        };
                    })
                }
            }
        }
    }); */   
    
});


/*
$('#idlaboratorio').select2({

	  ajax: {		  	  
		  	url: '/rest/programas2',
		  	
		    processResults: function (data) {
		    	  return {
		    	results: $.map(data, function(obj) {
		            return { id: obj.programaId, text: obj.nombrePrograma };
		        })
		    	  }
		    }
	  }
	});

$(document).ready(function () {
    $( "#idlaboratorio" ).change(function() {
        $("#idsedes").load('/periodosvigencia/obtSedes', 'idLaboratorio=' + $( "#idLaboratoriosSedes" ).val() + '');
    });
});*/

$(document).ready(function(){
	if(nameColumn!='periodosvigenciaId'){
	    var nuevaCadena = nameColumn.replace(".", "");
	    document.getElementById("Ico"+nuevaCadena).style.color="#d43f3a";
	    $("#Ico"+nuevaCadena).removeClass("fa fa-chevron-up");
	    if (sort == "ASC") {
	    	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-up");
	    }
	    else{
	   	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-down");
	    }
	}
});