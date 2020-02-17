$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/controlexterno/instructivos" + "?nombre=" + $('#name-f').val());
    });
});