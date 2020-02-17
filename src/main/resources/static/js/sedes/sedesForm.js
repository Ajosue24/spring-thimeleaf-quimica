
$(document).ready(function () {
    if ($("#idSedes").val() == '0') {
        $("#idLaboratoriosSedes").change(function () {
            $("#sedesForm").load('/sedes/autoCompleteSedes', 'idLaboratorio=' + $("#idLaboratoriosSedes").val() + '');
        });

    }
});

$("#idPais").change(function () {
	$("#idDepartamentos").load('/sedes/obtDepartamentos', 'idPais.nombrePais=' + $("#idPais").val() + '', function() {
			$("#idCiudad").load('/sedes/obtCiudades', 'idDepartamento=' + $("#idDepartamentos").val() + '');
		   	$('#idCiudad').select2({placeholder: "Seleccione ..."});
	  });
   	$('#idDepartamentos').select2({placeholder: "Seleccione ..."});
 });

  $("#idDepartamentos").change(function () {
		$("#idCiudad").load('/sedes/obtCiudades', 'idDepartamento=' + $("#idDepartamentos").val() + '');
	   	$('#idCiudad').select2({placeholder: "Seleccione ..."});
  });

$("#nombreUsuario").keyup(function () {
    this.value = this.value.replace(/[^0-9\.]/g, '');
});

var nombreUsuario = "";

function loadModal(index) {
    var usuario = $("#idUsuarioHddn" + index).val();
    var nombreUsuario = $("#nombreUsuarioHddn" + index).val();

    if (usuario != null || usuario > 1) {
        $("#usuarioIds").val(usuario);
        $("#nombreUsuario").prop("disabled", "disabled");
        $("#nombreUsuario").val(nombreUsuario);
        this.nombreUsuario = nombreUsuario;
    } else {
        usuario = "";
        nombreUsuario = "";
        $("#nombreUsuario").val("");
        $("#usuarioIds").val(0);
        $("#nombreUsuario").prop("disabled", false);
    }

    $('#modalUsuarios').modal('show');
}

function openDeleteModal(index) {
    var usuario = $("#idUsuarioHddn" + index).val();
    var sedesId = $("#idSedesHdd" + index).val();
    $('#idUsuarioD').val(usuario);
    $('#idSedeD').val(sedesId);
    $('#openDeleteModal').modal('show');
}

function borrarUsuario() {
    window.location.replace("/sedes/deleteUsuario" + "?usuarioId=" + $('#idUsuarioD').val() + "&sedesId=" + $('#idSedeD').val());
    $('#idUsuarioD').val('');
    $('#idSedeD').val('');
    $('#openDeleteModal').modal('hide');
}


$(document).ready(function () {

    $("#password").keyup(function () {
        if ($("#password").val().length >= 1 && $("#password").val().length <= 4) {
            $("#saveUser").attr("disabled", "disabled");
            $("#passwordLbl").show();
        } else {
            $("#saveUser").removeAttr("disabled");
            $("#passwordLbl").hide();
        }
    });

    var searchRequest = null;
    $(function () {
        var minlength = 1;

        $("#nombreUsuario").keyup(function () {
            debugger;
            var that = this,
                value = $(this).val();
            if (nombreUsuario != $("#nombreUsuario").val()) {

                if (value.length >= minlength) {
                    if (searchRequest != null)
                        searchRequest.abort();
                    searchRequest = $.ajax({
                        type: 'GET',
                        url: '/rest/usuario/' + $("#nombreUsuario").val(),
                        data: {
                            'nombre': value
                        },
                        dataType: "text",
                        success: function (msg) {
                            if (msg == "true") {
                                $("#saveUser").attr("disabled", "disabled");
                                $("#usuarioLbl").show();
                            } else {
                                $("#saveUser").removeAttr("disabled");
                                $("#usuarioLbl").hide();
                            }
                        }
                    });
                }
            }
        });
    });

    // Validacion de estructura de correos
    emailRegex = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
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

    document.getElementById('correo2').addEventListener('input', function () {
        correoAlternativoLabel = document.getElementById('correoAlternativoLabel');

        if (emailRegex.test(correo2.value)) {
            correoAlternativoLabel.innerText = "";
            document.getElementById("save").disabled = false;
        } else {
            correoAlternativoLabel.innerText = "Correo no válido. Verifique su formato";
            document.getElementById("save").disabled = true;
        }

        if ($("#correo2").val().length == 0) {
            correoAlternativoLabel.innerText = "";
            document.getElementById("save").disabled = false;
        }
    });
});

$(document).ready(function () {
    try {
        document.getElementById('password2').addEventListener('input', function () {
            passwordLabel = document.getElementById('passwordLabel');

            if ($("#password2").val().length > 0) {
                if ($("#password2").val() === $("#password").val()) {
                    document.getElementById("saveUser").disabled = false;
                    passwordLabel.innerText = "";
                    debugger;
                } else {
                    passwordLabel.innerText = "Las contraseñas no coinciden";
                    document.getElementById("saveUser").disabled = true;
                }
            } else {
                passwordLabel.innerText = "";
            }
        });
    } catch (err) {
    }
});

$(document).ready(function () {
    $('#idLaboratoriosSedes').select2();
    $('#idPais').select2();
    $('#idDepartamentos').select2();
    $('#idCiudad').select2();
    document.getElementById("divForm").style.display = "block";  
});
