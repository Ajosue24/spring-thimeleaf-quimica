$(document).ready(function(){
    $('#laboratorio').select2();
    $('#programaFront').select2();
    $('#sede').select2();
    $('#muestra').select2();
    $('#usuario').select2();
    $("#buscar").click(function () {
        window.location.replace(
            "/inscripcionprogramas/list" +
            "?laboratorio=" + $('#laboratorio').val() +
            "&programaFront=" + $('#programaFront').val() +
            "&sede=" + $('#sede').val() +
            "&muestra=" + $('#muestra').val() +
            "&usuario=" + $('#usuario').val()  +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
    changePageAndSize();
});

function changePageAndSize() {
    $('#pageSizeSelect').change(function (evt) {
        window.location.replace(
            "/inscripcionprogramas/list?size=" + this.value + "&page=1" +
            "&laboratorio=" + $('#laboratorio').val() +
            "&programaFront=" + $('#programaFront').val() +
            "&sede=" + $('#sede').val() +
            "&muestra=" + $('#muestra').val() +
            "&usuario=" + $('#usuario').val()  +"&nameColumn=" + nameColumn +  "&sortBy=" + sort + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    });
}

function redirect(url) {
    if (sort == "ASC") {
        window.location.replace(
            "/inscripcionprogramas/list" +
            "?laboratorio=" + $('#laboratorio').val() +
            "&programaFront=" + $('#programaFront').val() +
            "&sede=" + $('#sede').val() +
            "&muestra=" + $('#muestra').val() +
            "&usuario=" + $('#usuario').val() +
            "&nameColumn=" + url + "&sortBy=DESC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    } else if (sort == "DESC") {
        window.location.replace(
            "/inscripcionprogramas/list" +
            "?laboratorio=" + $('#laboratorio').val() +
            "&programaFront=" + $('#programaFront').val() +
            "&sede=" + $('#sede').val() +
            "&muestra=" + $('#muestra').val() +
            "&usuario=" + $('#usuario').val() +
            "&nameColumn=" + url + "&sortBy=ASC" + "&page=1" + "&size=" + $('#pageSizeSelect').val());
    }
}


function openDeleteModal(InsProgramas) {
    $('#idInsProgramaD').val(InsProgramas)
    $('#openDeleteModal').modal('show');
}

function borrarInsProgramas() {
    window.location.replace("/inscripcionprogramas/delete?inscripProgramaId=" + $('#idInsProgramaD').val() + '');
    $('#idInsProgramaD').val('');
    $('#openDeleteModal').modal('hide');
}

$(document).ready(function(){
    var nuevaCadena = nameColumn.replace(".", "");
    var nuevaCadena = nuevaCadena.replace(".", "");
	if(nuevaCadena!='inscripProgramaId'){
	    document.getElementById("Ico"+nuevaCadena).style.color="#d43f3a" ;
	    $("#Ico"+nuevaCadena).removeClass("fa fa-chevron-up");
	    if (sort == "ASC") {
	    	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-up");
	    }
	    else{
	   	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-down");
	    }
	}
});


/*
setTimeout(function(){
	
	
	mues=$('#muestrasTabla').val();
	validMuestra=mues.substr(-150); 
	alert(validMuestra);
	//   $('#MensajeError').css("display", "none"); 
	 
},2000); // 20000ms = 20segundos Ejecuta la funcion una unica vez
	  //setInterval("alert('Steven')",10000);//Ejecuta la funcion indefinidamente

*/
	  