
$("#listaUsuarios").change(function () {
	if($("#listaUsuarios").val()!=''){
	    $("#editarPermisosProgramas").load('/pemisosProgramas/obtListPermisosProgramas', 'usuario=' + $("#listaUsuarios").val() + '');
	    $("#datatable-responsive").show();
	}
	else{
		$("#datatable-responsive").hide();
	}
});


function actualizarPermisoRevisor(programa){
	$(programa).load('/pemisosProgramas/actulizarPermisos', 'usuario=' + $("#listaUsuarios").val() + ''+'&'+'programa=' + programa + ''+'&'+ 'estado=' + $("#permisosProgramaRevisor"+programa).is(":checked") + ''+'&'+ 'campo=' + 'Revisor' + '');	
}

function actualizarPermisoDirector(programa){
	$(programa).load('/pemisosProgramas/actulizarPermisos', 'usuario=' + $("#listaUsuarios").val() + ''+'&'+'programa=' + programa + ''+'&'+ 'estado=' + $("#permisosProgramaDirector"+programa).is(":checked") + ''+'&'+ 'campo=' + 'Director' + '');	
}