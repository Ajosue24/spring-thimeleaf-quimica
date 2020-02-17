 

$("#idPrograma").change(function () {
    $("#idMensurandos").load('/metodos/obtMensurandos', 'idPrograma=' + $("#idPrograma").val() + '');
    $('#idMensurandos').select2({placeholder: "Seleccione ..."});
});


$(document).ready(function(){
    $('#idPrograma').select2();
    $('#idMensurandos').select2();
    document.getElementById("form").style.display = "block";
});