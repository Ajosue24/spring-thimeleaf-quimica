
$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/equipos/list?size=" + this.value + "&page=1" + "&programaFront=" + $('#programaFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/equipos/list" + "?programaFront=" + $('#programaFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {

    if (sort == "ASC") {
        window.location.replace("/equipos/list" + "?programaFront=" + $('#programaFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/equipos/list" + "?programaFront=" + $('#programaFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }
}





$(document).ready(function () {

    $('#programaFront').autocomplete({
        source: function (request, response) {
            var programaFront = $('#programaFront').val();
            $.ajax({
                url: '/rest/programas/' + programaFront,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.nombrePrograma.trim(),
                            value: item.nombrePrograma.trim(),
                            id: item.programaId
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


function openDeleteModal(equipo) {
    $('#idEquipoD').val(equipo)
    $('#openDeleteModal').modal('show');
}

function borrarEquipos() {
    window.location.replace("/equipos/delete?equipoId=" + $('#idEquipoD').val() + '');
    $('#idEquipoD').val('');
    $('#openDeleteModal').modal('hide');
}

$(document).ready(function(){
    $('#programaFront').select2();

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