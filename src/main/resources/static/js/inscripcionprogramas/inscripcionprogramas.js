/*$("#laboratorioId").change(function () {
    if($("#laboratorioId").val() != null ){
        $("#idSedes").load('/inscripcionprogramas/obtSedes', 'laboratorioId=' + $("#laboratorioId").val() + '');
        $("#idUsuarioLabSedes").val(null).trigger('change');


        delay(function(){
            var mapped = $('#idSedes option').map(function() {
            var obj = {};
            obj[this.value] = this.textContent;
            return obj;
        });

            obj1 =mapped.get();
            if (obj1.length > 0){
                var  jsonvar = obj1[Object.keys(obj1)[0]];
              
                $('#idSedes').val(Object.keys(jsonvar)[0]).trigger('change');
                for (key in obj1) {
                if (obj1.hasOwnProperty(key)){
                    var  jsonvar = obj1[Object.keys(obj1)[key]];
                    console.log(Object.keys(jsonvar)[0]);
                }
            }
                
            }
            else {
             

            }
        }, 300 );
    }
    else{
        $("#idSedes").val(null).trigger('change');
       // $('#idSedes').prop("disabled", true);
        $("#idUsuarioLabSedes").val(null).trigger('change');
        //$("#idUsuarioLabSedes").prop("disabled", true);

    }
});

$("#idSedes").change(function () {
   // $("#idUsuarioLabSedes").prop("disabled", false);
    $("#idUsuarioLabSedes").load('/inscripcionprogramas/obtUsuarios', 'sedeId=' + $("#idSedes").val() + '');
    delay( function () {

        var mapped1 = $('#idUsuarioLabSedes option').map(function () {
            var obj = {};
            obj[this.value] = this.textContent;
            return obj;
        });
        obj1 = mapped1.get();
        if (mapped1.length > 0) {
            var jsonvar = obj1[Object.keys(obj1)[0]];
            $('#idUsuarioLabSedes').val(Object.keys(jsonvar)[0]).trigger('change');
        }
    },300 );
});

$("#idUsuarioLabSedes").change(function () {
    //$("#idUsuarioLabSedes").prop("disabled", false);
});

$(document).ready(function(){
    $('#laboratorioId').select2();
    $('#programaId').select2();
    $('#idSedes').select2({placeholder: "Seleccione..."});
    $('#idUsuarioLabSedes').select2({placeholder: "Seleccione..."});
    document.getElementById("form").style.display = "block";
});

var delay = ( function() {
    var timer = 0;
    return function(callback, ms) {
        clearTimeout (timer);
        timer = setTimeout(callback, ms);
    };
})();
*/

$("#laboratorioId").change(function () {
        $("#idSedes").load('/inscripcionprogramas/obtSedes', 'laboratorioId=' + $("#laboratorioId").val() + '',function () {
            $("#idUsuarioLabSedes").load('/inscripcionprogramas/obtUsuarios', 'sedeId=' + $("#idSedes").val() + '');
            $('#idUsuarioLabSedes').select2({placeholder: "Seleccione ..."});
        });
        $('#idSedes').select2({placeholder: "Seleccione ..."});
});

$("#idSedes").change(function () {
    $("#idUsuarioLabSedes").load('/inscripcionprogramas/obtUsuarios', 'sedeId=' + $("#idSedes").val() + '');
    $('#idUsuarioLabSedes').select2({placeholder: "Seleccione ..."});	
});


$(document).ready(function(){
    $('#laboratorioId').select2();
    $('#idSedes').select2();
    $('#idUsuarioLabSedes').select2();
    $('#programaId').select2();    
    document.getElementById("form").style.display = "block";
});
