$(document).ready(function () {
    changePageAndSize();
});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/laboratorios/list?size=" + this.value + "&page=1" + "&cliente=" + $('#cliente').val() +
            "&nombreComercial=" + $('#nombreComercial').val() + "&razonSocial=" + $('#razonSocial').val() + "&pais=" + $('#pais').val() +
            "&tipoId=" + $('#tipoId').val() + "&id=" + $('#id').val()+"&nameColumn=" + nameColumn +  "&sortBy=" + sort);
    });
}

$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/laboratorios/list" + "?cliente=" + $('#cliente').val() + "&nombreComercial=" + $('#nombreComercial').val() +
            "&razonSocial=" + $('#razonSocial').val() + "&pais=" + $('#pais').val() + "&tipoId=" + $('#tipoId').val() + "&id=" + $('#id').val() +
            "&page=1" +"&nameColumn=" + nameColumn +  "&sortBy=" + sort  + "&size=" + $('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/laboratorios/list" + "?cliente=" + $('#cliente').val() + "&nombreComercial=" + $('#nombreComercial').val() +
            "&razonSocial=" + $('#razonSocial').val() + "&pais=" + $('#pais').val() + "&tipoId=" + $('#tipoId').val() + "&id=" + $('#id').val() +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/laboratorios/list" + "?cliente=" + $('#cliente').val() + "&nombreComercial=" + $('#nombreComercial').val() +
            "&razonSocial=" + $('#razonSocial').val() + "&pais=" + $('#pais').val() + "&tipoId=" + $('#tipoId').val() + "&id=" + $('#id').val() +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }
}

$(document).ready(function(){
    $('#pais').select2();
    $('#tipoId').select2();
   // $('#cliente').select2({placeholder: "Seleccione ..."});

    $('#cliente').select2({placeholder: "Seleccione ..."},{
        ajax: {
            url: "/rest/autoCompClientes/",
            processResults: function (data) {
                return {
                    results: $.map(data, function (obj) {
                        return {
                            id: obj.numeroIdentificacionCliente,
                            text: obj.numeroIdentificacionCliente + " l " + obj.razonSocial.trim()
                        };
                    })
                }
            }
        }
    });
});

function openDeleteModal(metodo) {
    $('#idLabD').val(metodo)
    $('#openDeleteModal').modal('show');
}

function eliminar() {
    window.location.replace("/laboratorios/delete?laboratorioId=" + $('#idLabD').val() + '');
    $('#idLabD').val('');
    $('#openDeleteModal').modal('hide');
}

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