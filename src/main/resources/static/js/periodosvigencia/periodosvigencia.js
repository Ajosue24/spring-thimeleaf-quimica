$("#idLaboratoriosSedes").change(function () {
    $("#idsedes").load('/periodosvigencia/obtSedes', 'idLaboratorio=' + $("#idLaboratoriosSedes").val() + '', function() {
        $("#idusuarios").load('/periodosvigencia/obtUsuarios', 'idSede=' + $("#idsedes").val() + '', function() {
            $("#inscripProgramaId").load('/periodosvigencia/obtInsProgramas', 'idusuario=' + $("#idusuarios").val() + '',function () {
                $("#perVigenciaRegistrados").load('/periodosvigencia/perVigUserXPrograma', 'idusuario=' + $('#idusuarios').val() +'&inscripProgramaId=' + $('#inscripProgramaId').val() + '');
                $('#perVigenciaRegistrados').select2({placeholder: "Seleccione ..."});
            });
            $('#inscripProgramaId').select2({placeholder: "Seleccione ..."});
        });
        $('#idusuarios').select2({placeholder: "Seleccione ..."});
    });
    $('#idsedes').select2({placeholder: "Seleccione ..."});
});

$("#idsedes").change(function () {
    $("#idusuarios").load('/periodosvigencia/obtUsuarios', 'idSede=' + $("#idsedes").val() + '',function () {
        $("#inscripProgramaId").load('/periodosvigencia/obtInsProgramas', 'idusuario=' + $("#idusuarios").val() + '',function () {
            $("#perVigenciaRegistrados").load('/periodosvigencia/perVigUserXPrograma', 'idusuario=' + $('#idusuarios :selected').val() +'&inscripProgramaId=' + $('#inscripProgramaId :selected').val() + '');
            $('#perVigenciaRegistrados').select2({placeholder: "Seleccione ..."});
        });
        $('#inscripProgramaId').select2({placeholder: "Seleccione ..."});
    });
    $('#idusuarios').select2({placeholder: "Seleccione ..."});
});

$("#idusuarios").change(function () {
    $("#inscripProgramaId").load('/periodosvigencia/obtInsProgramas', 'idusuario=' + $("#idusuarios").val() + '',function () {
        $("#perVigenciaRegistrados").load('/periodosvigencia/perVigUserXPrograma', 'idusuario=' + $('#idusuarios :selected').val() +'&inscripProgramaId=' + $('#inscripProgramaId :selected').val() + '');
        $('#perVigenciaRegistrados').select2({placeholder: "Seleccione ..."});
    });
    $('#inscripProgramaId').select2({placeholder: "Seleccione ..."});
});	

$(document).ready(function(){
    $('#idLaboratoriosSedes').select2();
    $('#idsedes').select2();
    $('#idusuarios').select2();    
    $('#inscripProgramaId').select2();
    $('#perVigenciaRegistrados').select2();
    $('#modalidad').select2();
    $('#clientePatrocinador').select2({placeholder: "Seleccione ..."});
    document.getElementById("form").style.display = "block";
});

$(function() {
    $('#muestraPatrocinada').click(function(e) {
        console.log($("#mostrarMuestra:checked").val());
        //var x=$(".js-switch").is(":checked"); muestraPatrocinada
        var x=$("#muestraPatrocinada.js-switch").is(":checked")
        //console.log(x);
    	if (x == false ){
    		 document.getElementById("clientePatrocinador").disabled = true;
            var sel = document.getElementById("clientePatrocinador");
            var texto=$('#clientePatrocinador :selected').text();
            var valor=$('#clientePatrocinador').val();
            sel.remove(sel.selectedIndex);
            $('#clientePatrocinador').prepend("<option value="+valor+" > "+texto+"</option>");
        }else {
        	 document.getElementById("clientePatrocinador").disabled = false;
        }
    });
});

$("#inscripProgramaId").change(function () {
    $("#perVigenciaRegistrados").load('/periodosvigencia/perVigUserXPrograma', 'idusuario=' + $('#idusuarios :selected').val() +'&inscripProgramaId=' + $('#inscripProgramaId :selected').val() + '');
    $('#perVigenciaRegistrados').select2({placeholder: "Seleccione ..."});      
});



/*
function HabCampos(){
	        var x=$("#muestraPatrocinada.js-switch").is(":checked")
    debugger;
	        //console.log(x);
	    	if (x == false ){
	    		// document.getElementById("clientePatrocinador").disabled = true;
                //$("#clientePatrocinador option[value="Seleccione..."]").attr("selected",true);
                 document.getElementById("clientePatrocinador").val();
                //$("#clientePatrocinador").val();
	        }else {
	        	 document.getElementById("clientePatrocinador").disabled = false;
	        }

}*/



$(document).ready(function () {
    var x=$("#muestraPatrocinada.js-switch").is(":checked")
    //console.log(x);
    if (x == false ){
        document.getElementById("clientePatrocinador").disabled = true;
    }else {
        document.getElementById("clientePatrocinador").disabled = false;
    }
});


