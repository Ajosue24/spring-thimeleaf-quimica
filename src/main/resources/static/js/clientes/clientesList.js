$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/clientes/list?size=" + this.value + "&page=1" + "&paisFront=" + $('#paisFront').val() + "&idTipoPais=" + $('#idTipoPais').val() + "&razonSocial=" + $('#razonSocial').val() + "&numeroid=" + $('#numeroid').val() + "&nombreComercial=" + $('#nombreComercial').val() + "&estado=" + $('#estado').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort);
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/clientes/list" + "?paisFront=" + $('#paisFront').val() + "&idTipoPais=" + $('#idTipoPais').val() + "&razonSocial=" + $('#razonSocial').val() + "&numeroid=" + $('#numeroid').val() + "&nombreComercial=" + $('#nombreComercial').val() + "&estado=" + $('#estado').is(":checked") +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/clientes/list" + "?paisFront=" + $('#paisFront').val() + "&idTipoPais=" + $('#idTipoPais').val() + "&razonSocial=" + $('#razonSocial').val() + "&numeroid=" + $('#numeroid').val() + "&nombreComercial=" + $('#nombreComercial').val() + "&estado=" + $('#estado').is(":checked") +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/clientes/list" + "?paisFront=" + $('#paisFront').val() + "&idTipoPais=" + $('#idTipoPais').val() + "&razonSocial=" + $('#razonSocial').val() + "&numeroid=" + $('#numeroid').val() + "&nombreComercial=" + $('#nombreComercial').val() + "&estado=" + $('#estado').is(":checked") +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }


}

/*
$(document).ready(function () {
    var idG;
    $('#paisFront').autocomplete({
        source: function (request, response) {
            var desPais = $('#paisFront').val();
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
        }, select: function (event, ui) {
            updateComboIdPais(ui.item.id);
        }
    });
});

function updateComboIdPais(idPais) {
    $("#idTipoPais").load('/clientes/listIdTipoPais', 'idPais.nombrePais=' + idPais + '');
}*/
/*
$(document).ready(function(){
	$("#paisFront").change(function () {
	    $("#idTipoPais").load('/clientes/listIdTipoPais', 'idPais.nombrePais=' + $("#paisFront").val() + '');
	    $('#idTipoPais').select2({placeholder: "Seleccione ..."});
	});	

});
*/
function openDeleteModal(cliente) {
            $('#idClienteM').val(cliente)
            $('#openDeleteModal').modal('show');
}

function borrarClientes() {
    window.location.replace("/clientes/delete?clienteId=" + $('#idClienteM').val() + '');
    $('#idClienteM').val('');
    $('#openDeleteModal').modal('hide');
}
$(document).ready(function(){
    $('#paisFront').select2();
    $('#idTipoPais').select2();
    
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