$(document).ready(function () {

    $("#revisionFiltro").click(function () {
        window.location.replace("/controlexterno/revisionResultados/" + $('#idMuestra').val() + "?laboratorio=" + $('#laboratorio').val() + "&sede=" + $('#sede').val()
            + "&usuario=" + $('#usuario').val() + "&resultados=" + $('#resultado').val() + "&proceso=" + $('#proceso').val())
    });

    $("textarea[ckeditor]").each((index, textarea) => {
        let instance = CKEDITOR.replace(textarea);
        let isReadOnly = $("#observacionReadOnly").val();
        if (typeof isReadOnly === 'string')
            isReadOnly = isReadOnly === 'true'
        CKEDITOR.config.readOnly = isReadOnly;
    });
});

var Inf = true;

function seccionObservacion(evt, id) {
    if ($('#btn' + id).text() === "+") {
        $('#btn' + id + '').text("-");
        $('#img' + id).show();
    } else {
        $('#btn' + id + '').text("+")
        $('#img' + id).hide();
    }
    $('#secobservacion' + id + '').slideToggle();
};


function guardarObservacion(idTipo) {
    debugger;
    let instance = null;
    let observation = null;
    LoadInfo()
    if (typeof idTipo === 'number')
        instance = CKEDITOR.instances[`TA${idTipo}`];
    else
        console.error('idTipo is not a number');

    if (!!instance) {
        observation = instance.getData();
        debugger;
        $.ajax({
            type: "POST",
            data: {"idTipo": idTipo, "idMuestra": $('#idMuestra').val(), "observacion": observation},
            dataType: "json",

            url: 'observacion',
            success: function (data) {
                debugger;
                window.location.reload();
            },
            error: function (e) {
                /*debugger;
                alert("Ocurrio un error al guardar la observacion");*/
                window.location.reload();
            }
        });
    }
};

function regresarSimulacion(muestra) {
    window.location.replace("/controlexterno/gestionMuestras/" + muestra);
}


function openModal(insMuestra, codProasecal) {
    $('#idInscripcionMuestra').val(insMuestra);
    $('#exampleModalLongTitle').text('Observacion individual para el usuario ' + codProasecal);

    $.ajax({
        url: 'obtenerObservacionIndividual/' + insMuestra + '',
        success: function (data) {
            $('#obsIndividual').val(data)

            try {
                CKEDITOR.replace(obsIndividual);
            } catch (e) {
                CKEDITOR.instances.obsIndividual.setData(data);
            }

            $('#openDeleteModal').modal('show');
        },
        error: function (e) {
            alert("Ocurrio un error al cargar la observacion del usuario " + codProasecal);
        }
    });
}


function observacionIndividual() {
    let a = CKEDITOR.instances.obsIndividual.getData();
    let b = $('#idInscripcionMuestra').val();

    $.ajax({
        type: "POST",
        data: {"observacion": CKEDITOR.instances.obsIndividual.getData()},
        dataType: "json",
        url: 'guardarObservacionIndividual/' + $('#idInscripcionMuestra').val(),
        success: function (data) {
            window.location.reload();
        }
        ,
        error: function (e) {
           // alert("Ocurrio un error al guardar el informe");
            window.location.reload();
        }
    });
}


function generarInformes(idMuestra) {
    if (Inf) {
        Inf = false;
        LoadInfo()
        $.ajax({
            url: 'generarInformes/' + idMuestra + '',
            success: function (data) {
                window.location.reload();
                alert('El proceso a terminado');
            },
            error: function (e) {
                alert("Ocurrio un error al generar los informes");
                Inf = true;
            }
        });
    }
};


function generarInformeFueraFecha(idInscripcionMuestra) {
    $.ajax({
        url: 'generarInformeFueraFecha/' + idInscripcionMuestra + '',
        success: function (data) {
            window.location.reload();
        },
        error: function (e) {
            alert("Ocurrio un error al generar el informe");
        }
    });
};


function generarVersionDefinitiva(idMuestra) {
    LoadInfo()
    $.ajax({
        url: 'versionFecha/' + idMuestra + '',
        success: function (data) {
            window.location.reload();
        },
        error: function (e) {
            alert("Ocurrio un error al generar las versiones definitivas");
        }
    });
};


/*function revision(idInscripcionMuestra, usr) {
    $.ajax({
        url: 'revision/' + idInscripcionMuestra + '?usr='+usr+'',
        success: function (data) {
            window.location.reload();
        },
        error: function (e) {
            alert("Ocurrio un error al revisar el resultado");
        }
    });
};*/


function actualizarRevisionDirector(informe) {
    $(informe).load('/controlexterno/revisionResultados/actualizarEstadoRevision', 'informe=' + informe + '' + '&' + 'estado=' + $("#RevisionDirector" + informe).is(":checked") + '' + '&' + 'campo=' + 'director' + '');
}

function actualizarRevisionRevisor(informe, enfecha) {
    $(informe).load('/controlexterno/revisionResultados/actualizarEstadoRevision', 'informe=' + informe + '' + '&' + 'estado=' + $("#RevisionRevisor" + informe).is(":checked") + '' + '&' + 'campo=' + 'revisor' + '');
    if (enfecha) {
        window.location.reload();
    }

}

function LoadInfo() {
    try {
        document.querySelector('.preload').style.setProperty('display', 'flex');
    } catch (e) {
    }
}
