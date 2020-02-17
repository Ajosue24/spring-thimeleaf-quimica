$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/controlexterno/videosRelacionados" + "?nombre=" + $('#name-f').val());
    });
});