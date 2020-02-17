$(document).ready(function () {
    var pathname = window.location.href;
    var error = "?inscripcion=error";
    var success = "?inscripcion=success";
    var andError = "&inscripcion=error";
    var andSuccess = "&inscripcion=success";

    $('#openInscripcionModal').on('hide.bs.modal', function () {
        /*Error*/

        if (ee > 0) {
            if (pathname.includes(success)) {
                window.location = pathname.replace(success, error);
            }
            if (pathname.includes(andSuccess)) {
                window.location = pathname.replace(andSuccess, andError);
            }

            if (pathname.includes(error) || pathname.includes(andError)) {
                window.location = pathname;
            } else {
                if (pathname.endsWith("list")) {
                    window.location = pathname + error;
                } else {
                    window.location = pathname + andError;
                }
            }
        }

        /*Success*/

        else if (ss > 0) {
            if (pathname.includes(error)) {
                window.location = pathname.replace(error, success);
            }
            if (pathname.includes(andError)) {
                window.location = pathname.replace(andError, andSuccess);
            }

            if (pathname.includes(success) || pathname.includes(andSuccess)) {
                window.location = pathname;
            } else {
                if (pathname.endsWith("list")) {
                    window.location = pathname + success;
                } else {
                    window.location = pathname + andSuccess;
                }
            }
        }
    })
});

var ss = 0;
var ee = 0;

function loadModalPeriodoVigencia(id) {
    debugger;
    $.ajax({
        url: '/rest/inscripcionPrograma/' + id + '',
        success: function (data) {
            debugger;
            $("#idPeriodoModal").val(data.periodosvigenciaId);
            $("#programaModal").text(data.inscripProgramaId.programaId.nombrePrograma);
            $("#idProgramaModal").val(data.inscripProgramaId.programaId.programaId);
            $("#usuarioModal").text(data.inscripProgramaId.idUsuarioLabSedes.usuarioId.codProasecal);
            $("#idUsuarioModal").val(data.inscripProgramaId.idUsuarioLabSedes.usuarioId.idUsuario)
            $("#fechaInicioModal").text(data.fechaInicio);
            $("#fechaFinalModal").text(data.fechaFin);
            $("#modalidadModal").text(data.modalidad);

            $("#tablaMuestras").load('/muestras/fechas', 'fechaInicial=' + data.fechaInicio + '&'
                + 'fechaFinal=' + data.fechaFin + '&'
                + 'idPrograma=' + $("#idPeriodoModal").val() + '');

            $('#openInscripcionModal').modal('show');

        },
        error: function (xhr, status, error) {
            alert(status + " " + error);
        }
    });
}

function save(check) {
    var idMuestras = check.id;
    var idPrograma = $("#idPeriodoModal").val();
    var muestra = idMuestras.substr(0, idMuestras.length - 3);
    var estado = $("#" + check.id + "").is(":checked");
    debugger;

    if (estado) {
        $.ajax({
            url: '/inscripcionMuestras/save?periodo=' + idPrograma + '&muestra=' + muestra + '',
            success: function (data) {
                ss += 1;
            },
            error: function (xhr, status, error) {
                ee += 1;
            }
        });
    } else {
        $.ajax({
            url: '/inscripcionMuestras/delete?periodo=' + idPrograma + '&muestra=' + muestra + '',
            success: function (data) {
                ss += 1;
            },
            error: function (xhr, status, error) {
                ee += +1;
            }
        });
    }
}