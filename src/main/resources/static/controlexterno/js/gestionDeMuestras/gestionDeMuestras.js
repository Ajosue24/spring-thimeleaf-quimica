$(document).ready(function () {
    $('#btnInformesGenerales').click(function () {
        if ($('#btnInformesGenerales').text() === "+") {
            $('#btnInformesGenerales').text("-");
        } else {
            $('#btnInformesGenerales').text("+")
        }
        $('#seccionInformesGenerales').slideToggle();
    });

    $('#btnParticipantesFuera').click(function () {
        if ($('#btnParticipantesFuera').text() === "+") {
            $('#btnParticipantesFuera').text("-");
        } else {
            $('#btnParticipantesFuera').text("+")
        }
        $('#seccionParticipantesFuera').slideToggle();
    });

    $('#btnMensurandos').click(function () {
        if ($('#btnMensurandos').text() === "+") {
            $('#btnMensurandos').text("-");
        } else {
            $('#btnMensurandos').text("+")
        }
        $('#seccionMensurandos').slideToggle();
    });

    $("#cierreImg").click(function () {
        if (confirm("¿Desea fijar/modificar la fecha de cierre de resultados? \n Esta acción modificará su grupo base") == true) {
            gestionFechas(1, $('#cierre').val());
        }
    });

    $("#liberacionImg").click(function () {
        if (confirm("¿Desea fijar/modificar la fecha de liberación de resultados?") == true) {
            gestionFechas(0, $('#liberacion').val());
        }
    });

    $("#revisionResultados").click(function () {
        var a=$('#idMuestra').val();
        window.location.replace("/controlexterno/revisionResultados/" + $('#idMuestra').val());
    });

    jQuery.datetimepicker.setLocale('es');

    jQuery('input.datetimepicker').attr('readonly', true).datetimepicker({
        format:'Y/m/d H:i',
        /*Adicion de minutos mantis*/
        /*step: 5*/
    }).on('keydown', e => {
        if(e.keyCode === 8 || e.keyCode === 46)
            $(e.target).val('');
    });

    function gestionFechas(tipo, fecha) {
        $.ajax({
            url: "fechas?id=" + $('#idMuestra').val() + "&tipo=" + tipo + "&fecha=" + fecha,
            success: function (datos) {
                window.location.reload();
                alert("Los datos han sido guardados satisfactoriamente");
            },
            error: function (xhr, status, error) {
                alert("Error al fijar/modificar la fecha")
            }
        });
    }
});

function tab() {
    document.getElementById("ConsultaTab").click(event, 'Consulta');
}
$("#linkconsulta").click( function()
    {
        document.getElementById('ConsultaTab').click();
    }
);
function regresarList(idMuestra) {
    window.location.replace("/controlexterno/listResultadosDirector?muestra=" + idMuestra + "&laboratorio=&sede=&usuario=&estado=Todos");
}