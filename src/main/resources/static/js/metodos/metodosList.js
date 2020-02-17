$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/metodos/list?size=" + this.value + "&page=1" + "&programaFront=" + $('#programaFront').val() + "&mensurandoFront=" + $('#mensurandoFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked")+"&nameColumn=" + nameColumn +  "&sortBy=" + sort   );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/metodos/list" + "?programaFront=" + $('#programaFront').val() + "&mensurandoFront=" + $('#mensurandoFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort  + "&page=1" + "&size" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/metodos/list" + "?programaFront=" + $('#programaFront').val() + "&mensurandoFront=" + $('#mensurandoFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/metodos/list" + "?programaFront=" + $('#programaFront').val() + "&mensurandoFront=" + $('#mensurandoFront').val() + "&nombreFront=" + $('#nombreFront').val() + "&grupoFront=" + $('#grupoFront').val() + "&codProsecalFront=" + $('#codProsecalFront').val() + "&estadoFront=" + $('#estadoFront').is(":checked") +
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
            updateComboMensurando(ui.item.id);
        }
    });
});


function updateComboMensurando(idPrograma) {
    $("#mensurandoFront").load('/metodos/obtMensurandos', 'idPrograma=' + idPrograma + '');
}
*//*
$(document).ready(function(){
	$("#programaFront").change(function () {
	    $("#mensurandoFront").load('/metodos/obtMensurandos', 'idPrograma=' + $("#programaFront").val() + '');
	    $('#mensurandoFront').select2({placeholder: "Seleccione ..."});
	});	

});
*/
function openDeleteModal(metodo) {
    $('#idMetodoD').val(metodo)
    $('#openDeleteModal').modal('show');
}

function borrarMetodos() {
    window.location.replace("/metodos/delete?metodoId=" + $('#idMetodoD').val() + '');
    $('#idMetodoD').val('');
    $('#openDeleteModal').modal('hide');
}

$(document).ready(function(){
    $('#programaFront').select2();
    $('#mensurandoFront').select2();   

    var nuevaCadena = nameColumn.replace(".", "");
    nuevaCadena = nuevaCadena.replace(".", "");
    document.getElementById("Ico"+nuevaCadena).style.color="#d43f3a" ;
    $("#Ico"+nuevaCadena).removeClass("fa fa-chevron-up");
    if (sort == "ASC") {
    	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-up");
    }
    else{
   	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-down");
    }

});