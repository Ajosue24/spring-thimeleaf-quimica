
$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/usuarios/list?size=" + this.value + "&page=1" + "&nombreUsuario=" + $('#nombreUsuario').val() + "&nombreApellido=" + $('#nombreApellido').val() + "&codProasecal=" + $('#codProasecal').val() + "&estado=" + $('#estado').is(":checked"));
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/usuarios/list" + "?nombreUsuario=" + $('#nombreUsuario').val() + "&nombreApellido=" + $('#nombreApellido').val()  + "&codProasecal=" + $('#codProasecal').val()+"&estado=" + $('#estado').is(":checked") + "&sortBy=DESC" + "&page=1" + "&size" + $('#pageSizeSelect').val());
    });
});
function redirect(url) {
    if (sort == "ASC") {
        window.location.replace("/usuarios/list" + "?nombreUsuario=" + $('#nombreUsuario').val() + "&nombreApellido=" + $('#nombreApellido').val() + "&codProasecal=" + $('#codProasecal').val() + "&estado=" + $('#estado').is(":checked") +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace("/usuarios/list" + "?nombreUsuario=" + $('#nombreUsuario').val() + "&nombreApellido=" + $('#nombreApellido').val() + "&codProasecal=" + $('#codProasecal').val() + "&estado=" + $('#estado').is(":checked") +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size" + $('#pageSizeSelect').val());
    }


}
