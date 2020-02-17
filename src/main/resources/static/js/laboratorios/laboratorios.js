$(document).ready(function () {

    var idG;
    var idCliente;
    emailRegex = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;

    if ($("#idLaboratoriosSedes").val() == '0') {
        $('#clienteId').on('select2:select', function (e) {
            $("#idCliente").val(e.params.data.id);
            $("#laboratorioForm").load('/laboratorios/autoCompFields', 'id=' + e.params.data.id + '');
        });
    }

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

   
    $(document).ready(function () {
        $('#clienteId').select2({placeholder: "Seleccione ..."});
        $('#desPais').select2({placeholder: "Seleccione ..."});
        $('#idTipoPais').select2({placeholder: "Seleccione ..."});
        $('#idDepartamentos').select2({placeholder: "Seleccione ..."});
        $('#idCiudad').select2({placeholder: "Seleccione ..."});
        document.getElementById("divForm").style.display = "block";  
    });

// Validacion de estructura de correos

    document.getElementById('correo').addEventListener('input', function () {
        correoLabel = document.getElementById('correoLabel');

        if (emailRegex.test(correo.value)) {
            correoLabel.innerText = "";
            document.getElementById("save").disabled = false;
        } else {
            correoLabel.innerText = "Correo no válido. Verifique su formato";
            document.getElementById("save").disabled = true;
        }

        if ($("#correo").val().length == 0) {
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

//------------------------------------

//Validacion de contraseñas


    document.getElementById('password2').addEventListener('input', function () {
        passwordLabel = document.getElementById('passwordLabel');

        if ($("#password2").val().length > 0) {
            if ($("#password2").val() === $("#password").val()) {
                document.getElementById("save").disabled = false;
                passwordLabel.innerText = "";
                debugger;
            } else {
                passwordLabel.innerText = "Las contraseñas no coinciden";
                document.getElementById("save").disabled = true;
            }
        } else {
            passwordLabel.innerText = "";
        }
    });
});
