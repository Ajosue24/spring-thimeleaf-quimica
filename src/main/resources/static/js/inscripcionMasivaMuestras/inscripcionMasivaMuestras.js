$(document).ready(function() {
    $("#salidaContent").load('/inscripcionMasivaMuestras/respuesta' + '');
    setTimeout(function() {
        $("#salidaContent").trigger('change');
    },5000)
});

$("#salidaContent").change(function () {
    debugger;
    var obj = JSON.parse($("#salidaContent").val());
    var element = document.getElementById("data");
    for (i = 0; i < obj.length; i++ ){
        var line = document.createElement("p");
        var respuesta = document.createTextNode(obj[i].salidaRespuesta);
        var dataw = obj[0];
        console.log(dataw);
        if (obj[i].valError != "0"){
            line.style.cssText = 'font-size: 1em; color: red;';
        } else {
            line.style.cssText = 'font-size: 1em; color: green;';
        }
        line.appendChild(respuesta);
        element.appendChild(line);
    }
//alert($("#salidaContent").val());
    if($("#salidaContent").val()!='[]'&&$("#salidaContent").val()!='[{"salidaRespuesta":"El formato del archivo no es csv","valError":"1"}]'){
        alert('Carga de archivo finalizada.');
    }
    

});

$('#file').change(function() {
        debugger;
        var fullPath = document.getElementById('file').value;
        if (fullPath) {
            var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
            var filename = fullPath.substring(startIndex);
            if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
                filename = filename.substring(1);
            }
        }
        if ( fullPath == null || fullPath == "" ){
            alert("Debe seleccionar un archivo");
        }else {
            var textFile = document.getElementById("texFile");
            textFile.value = filename;
        }
});

$('#save').click( function (e) {
    e.preventDefault();
    if ($("#file").val() =='' ){
        alert("Debe seleccionar un Archivo");
    }else {
        $("#form").submit();
    }
});