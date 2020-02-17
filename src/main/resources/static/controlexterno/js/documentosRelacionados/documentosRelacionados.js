$(document).ready(function () {
    $("#buscar").click(function () {
            window.location.replace("/controlexterno/documentosRelacionados" + "?nombre=" + $('#name-f').val());
    });
});