$(document).ready(function () {
    var idG;
    $('#desPais').autocomplete({
        source: function (request, response) {
            var desPais = $('#desPais').val();
            $.ajax({
                url: '/rest/autoCompPais/' + desPais,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        return {label: item.nombrePais.trim(), value: item.nombrePais.trim(), id: item.idPais};
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }, select: function (event, ui) {
            updateComboIdPais(ui.item.id);
            $("#idPais").val(ui.item.id);
            debugger;
        }
    });

    $('#desLab').autocomplete({
        source: function (request, response) {
            var desLab = $('#desLab').val();
            $.ajax({
                url: '/rest/autoCompLaboratorio/' + desLab,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        debugger;
                        return {
                            label: item.nombreComercial.trim(),
                            value: item.nombreComercial.trim(),
                            id: item.idLaboratoriosSedes
                        };
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }, select: function (event, ui) {
            updateCliente(ui.item.id);
            $("#laboratoriosSedesId").val(ui.item.id);
            debugger;
        }
    });
});

//Metodo para actualizar la tabla x ajax

function updateComboIdPais(idPais) {
    $("#idPais").load('/laboratorios/idTipoPais', 'idPais.nombrePais=' + idPais + '');
    $("#idDepartamentos").load('/laboratorios/obtDepartamentos', 'idPais.nombrePais=' + idPais + '');
    $("#idTipoPais").load('/laboratorios/idTipoPais', 'idPais.nombrePais=' + idPais + '');
    console.log($("#idTipoPais").load('/laboratorios/idTipoPais', 'idPais.nombrePais=' + idPais + ''));
    console.log($("#idTipoPais").value);
}


$("#idDepartamentos").change(function () {
    $("#idCiudad").load('/laboratorios/obtCiudades', 'idDepartamento=' + $("#idDepartamentos").val() + '');
});

function updateCliente(idLab) {
    $("#clienteId").load('/laboratorios/obtCliente', 'idLaboratorio=' + idLab + '');
    console.log($("#clienteId").value);
    debugger;
}