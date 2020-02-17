function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    ev.target.appendChild(document.getElementById(data));
}



function seleccionarElementosDrag() {
    var cantidadSeleccionada = $('#listaString > option').length!=0?$('#listaString > option').length!=0:$('#listaString').children('option').length;
}

function validarRolesSeleccionados() {
    if($("#pre-selected-options").val() !=null && $("#pre-selected-options").val().indexOf('' + idParticipante + '') >= 0){
        $("#codProasecal").prop('disabled', false);
        $("#codProasecal").prop('required',true);
    }else{
        $("#codProasecal").prop('disabled', true);
        $("#codProasecal").prop('required',false);
        $("#codProasecal").val("");
    }
}

$( "#codProasecal" ).keyup(function() {
    this.value = this.value.replace(/[^0-9\.]/g,'');
    if($("#pre-selected-options").val() !=null && $("#pre-selected-options").val().indexOf('' + idParticipante + '') >= 0){
        $("#nombreUsuario").val($("#codProasecal").val());
    }
});

$( "#nombreUsuario" ).keyup(function() {
    if($("#pre-selected-options").val() !=null && $("#pre-selected-options").val().indexOf('' + idParticipante + '') >= 0){
        this.value = this.value.replace(/[^0-9\.]/g,'');
        $("#codProasecal").val($("#nombreUsuario").val());
    }
});

function isCreateF(create) {
    if(create!=null&&create!="false"){
        $("#password2").prop('required',true);
        $("#password").prop('required',true);
    }
}






$(document).ready(function () {

    $("#password").keyup(function () {
        if ($("#password").val().length >= 1 && $("#password").val().length <= 4) {
            $("#passwordLbl").show();
        } else {
            $("#passwordLbl").hide();
        }
    });
});

$(document).ready(function () {
    try {
        document.getElementById('password2').addEventListener('input', function () {
            passwordLabel = document.getElementById('passwordLabel');

            if ($("#password2").val().length > 0) {
                if ($("#password2").val() === $("#password").val()) {
                    passwordLabel.innerText = "";
                  
                } else {
                    passwordLabel.innerText = "Las contraseñas no coinciden";
                }
            } else {
                passwordLabel.innerText = "";
            }
        });
    } catch (err) {
    }
});