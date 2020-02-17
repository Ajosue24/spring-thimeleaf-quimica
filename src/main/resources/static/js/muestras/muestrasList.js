
$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/muestras/list?size=" + this.value + "&page=1" + "&programaFront=" + $('#programaFront').val() + "&tipoMuestraFront=" + $('#tipoMuestraFront').val() + "&numeroFront=" + $('#numeroFront').val() + "&fechaFront=" + $('#fechaFront').val()+"&nameColumn=" + nameColumn +  "&sortBy=" + sort );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/muestras/list" + "?programaFront=" + $('#programaFront').val() + "&tipoMuestraFront=" + $('#tipoMuestraFront').val() + "&numeroFront=" + $('#numeroFront').val() + "&fechaFront=" + $('#fechaFront').val() +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/muestras/list" + "?programaFront=" + $('#programaFront').val() + "&tipoMuestraFront=" + $('#tipoMuestraFront').val() + "&numeroFront=" + $('#numeroFront').val() + "&fechaFront=" + $('#fechaFront').val() +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/muestras/list" + "?programaFront=" + $('#programaFront').val() + "&tipoMuestraFront=" + $('#tipoMuestraFront').val() + "&numeroFront=" + $('#numeroFront').val() + "&fechaFront=" + $('#fechaFront').val() +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }
}


/*]]>*/

/*
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
        }, select: function (event, ui) {
            updateComboTipoMuestra(ui.item.id);
        }
    });
});


function updateComboTipoMuestra(idPrograma) {
    $("#tipoMuestraFront").load('/muestras/obtTipoMuestra', 'idPrograma=' + idPrograma + '');
}*/

function openDeleteModal(muestra) {
    $('#idMuestraD').val(muestra)
    $('#openDeleteModal').modal('show');
}

function borrarMuestras() {
    window.location.replace("/muestras/delete?muestraId=" + $('#idMuestraD').val() + '');
    $('#idMuestraD').val('');
    $('#openDeleteModal').modal('hide');
}
/*
$(document).ready(function(){
	$("#programaFront").change(function () {
	    $("#tipoMuestraFront").load('/muestras/obtTipoMuestra', 'idPrograma=' + $("#programaFront").val() + '');
	    $('#tipoMuestraFront').select2({placeholder: "Seleccione ..."});
	});	

});
*/
$(document).ready(function(){
    $('#programaFront').select2();
    $('#tipoMuestraFront').select2();
    
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