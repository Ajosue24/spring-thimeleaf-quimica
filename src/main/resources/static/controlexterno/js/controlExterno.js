$(document).ready(function () {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace("/controlexterno/index?pagSizeIndex=" + this.value + "&page=1" + "&desde=" + $('#desde').val() + "&hasta=" + $('#hasta').val() + "&numero=" + $('#numero').val() );
    });
}


$(document).ready(function () {
    $("#buscar").click(function () {
        window.location.replace("/controlexterno/index" + "?desde=" + $('#desde').val() + "&hasta=" + $('#hasta').val() + "&numero=" + $('#numero').val());
    });
});




$(document).ready(function () {
    $("#regresar").click(function () {
        window.location.replace("/controlexterno/listResultadosDirector");
    });
});


function regresarList(muestra){
	 window.location.replace("/controlexterno/listResultadosDirector?ano=&muestra="+muestra+"&laboratorio=&sede=&usuario=&estado=Todos");
}

$(document).ready(function () {
    try{
        document.querySelector('.preload').style.setProperty('display', 'none');
    }catch(e){

    }
});
