$("#desPais").change(function () {
	 $("#idTipoPais").load('/clientes/idTipoPais', 'idPais.nombrePais=' + $("#desPais").val() + '', function() {
		 $("#idDepartamentos").load('/clientes/obtDepartamentos', 'idPais.nombrePais=' + $("#desPais").val() + '', function() {
			  $("#idCiudad").load('/clientes/obtCiudades', 'idDepartamento=' + $("#idDepartamentos").val() + '');
			  $('#idCiudad').select2({placeholder: "Seleccione ..."});
        });
		 $('#idDepartamentos').select2({placeholder: "Seleccione ..."});
    });
	$('#idTipoPais').select2({placeholder: "Seleccione ..."});
});

$("#idDepartamentos").change(function () {
	$("#idCiudad").load('/clientes/obtCiudades', 'idDepartamento=' + $("#idDepartamentos").val() + '');
	$('#idCiudad').select2({placeholder: "Seleccione ..."});
});

$(document).ready(function(){
    $('#desPais').select2();
    $('#idTipoPais').select2();
    $('#idDepartamentos').select2();
    $('#idCiudad').select2();
    document.getElementById("clienteForm").style.display = "block";
});


// Validacion de estructura de correos
emailRegex = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
document.getElementById('correoCliente').addEventListener('input', function () {
    correoLabel = document.getElementById('correoLabel');

    if (emailRegex.test(correoCliente.value)) {
        correoLabel.innerText = "";
        document.getElementById("save").disabled = false;
    } else {
        correoLabel.innerText = "Correo no válido. Verifique su formato";
        document.getElementById("save").disabled = true;
    }

    if ($("#correoCliente").val().length == 0) {
        correoLabel.innerText = "";
        document.getElementById("save").disabled = false;
    }
});

document.getElementById('correoAlternativo').addEventListener('input', function () {
    correoAlternativoLabel = document.getElementById('correoAlternativoLabel');

    if (emailRegex.test(correoAlternativo.value)) {
        correoAlternativoLabel.innerText = "";
        document.getElementById("save").disabled = false;
    } else {
        correoAlternativoLabel.innerText = "Correo no válido. Verifique su formato";
        document.getElementById("save").disabled = true;
    }

    if ($("#correoAlternativo").val().length == 0) {
        correoAlternativoLabel.innerText = "";
        document.getElementById("save").disabled = false;
    }
});