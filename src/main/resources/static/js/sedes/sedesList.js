
$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/sedes/list?size=" + this.value + "&page=1" + "&pais=" + $('#pais').val() + "&nombreSede=" + $('#nombreSede').val() + "&laboratorioFront=" + $('#laboratorioFront').val() + "&usuario=" + $('#usuario').val() + "&fechaCreacion=" + $('#fechaCreacion').val() + "&impResultados=" + $('#impResultados').is(":checked")+"&nameColumn=" + nameColumn +  "&sortBy=" + sort );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/sedes/list" + "?pais=" + $('#pais').val() + "&nombreSede=" + $('#nombreSede').val() + "&laboratorioFront=" + $('#laboratorioFront').val() + "&usuario=" + $('#usuario').val() + "&fechaCreacion=" + $('#fechaCreacion').val() + "&impResultados=" + $('#impResultados').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/sedes/list" + "?pais=" + $('#pais').val() + "&nombreSede=" + $('#nombreSede').val() + "&laboratorioFront=" + $('#laboratorioFront').val() + "&usuario=" + $('#usuario').val() + "&fechaCreacion=" + $('#fechaCreacion').val() + "&impResultados=" + $('#impResultados').is(":checked") +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/sedes/list" + "?pais=" + $('#pais').val() + "&nombreSede=" + $('#nombreSede').val() + "&laboratorioFront=" + $('#laboratorioFront').val() + "&usuario=" + $('#usuario').val() + "&fechaCreacion=" + $('#fechaCreacion').val() + "&impResultados=" + $('#impResultados').is(":checked") +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }


}
/*

$('#laboratorio').autocomplete({
    source: function (request, response) {
        var desLaboratorio = $('#laboratorio').val();
        $.ajax({
            url: '/rest/autoCompLaboratorio/' + desLaboratorio,
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                response($.map(data, function (item) {
                    return {
                        label: item.numeroIdentificacion + " l " + item.razonSocial.trim(),
                        value: item.numeroIdentificacion + " l " + item.razonSocial.trim(),
                        id: item.idLaboratoriosSedes
                    };
                }));
            },
            error: function (xhr, status, error) {
                alert("Error");
            }
        });
    }
});

$(document).ready(function () {
    var idG;
    $('#pais').autocomplete({
        source: function (request, response) {
            var desPais = $('#pais').val();
            $.ajax({
                url: '/rest/autoCompPais/' + desPais,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.nombrePais.trim(),
                            value: item.nombrePais.trim(),
                            id: item.idPais
                        };
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }
    });
});

*/
function openDeleteModal(cliente) {
    $('#idClienteM').val(cliente)
    $('#openDeleteModal').modal('show');
}

function borrarsedes() {
    window.location.replace("/sedes/delete?clienteId=" + $('#idClienteM').val() + '');
    $('#idClienteM').val('');
    $('#openDeleteModal').modal('hide');
}
$(document).ready(function(){
    $('#pais').select2();
    //$('#laboratorioFront').select2({placeholder: "Seleccione ..."});


    $('#laboratorioFront').select2({
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
    });
});

$(document).ready(function(){
    var nuevaCadena = nameColumn.replace(".", "");
    document.getElementById("Ico"+nuevaCadena).style.color="#d43f3a" ;
    $("#Ico"+nuevaCadena).removeClass("fa fa-chevron-up");
    if (sort == "ASC") {
    	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-up");
    }
    else{
   	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-down");
    }

});
