/*$('#programa').autocomplete({
	source : function(request, response) {
		var programa = $('#programa').val();
		$.ajax({
			url : '/rest/programas/' + programa,
			dataType : 'json',
			contentType : 'application/json',
			success : function(data) {
				response($.map(data, function(item) {
					return {
						label : item.nombrePrograma.trim(),
						value : item.nombrePrograma.trim(),
						id : item.programaId
					};
				}));
			},
			error : function(xhr, status, error) {
				alert("Error");
			}
		});
	},
	select : function(event, ui) {
		$("#idPrograma").val(ui.item.id);
		// debugger;
	}
});*/

/*

$(document).ready(function () {

    $('#programas2').autocomplete({
        source: function (request, response) {
            var programaFront = $('#programas2').val();
            $.ajax({
                url: '/rest/programas/' + programaFront,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        return {
                            label: item.nombrePrograma.trim(),
                            value: item.nombrePrograma.trim(),
                            id: item.programaId
                        };
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }
    });
});


$(document).ready(function() {
	$('#filtrosList').change(function(evt) {
		if ($('#filtrosList :selected').attr('id') == "estado") {
			$(function() {
				var availableTags = [ "activo", "inactivo" ];
				$(":input").autocomplete({
					source : availableTags
				});
			});
		} else {
			$(function() {
				var availableTags = [ "" ];
				$(":input").autocomplete({
					source : availableTags
				});
			});
		}
	});
});

*/

/*<![CDATA[*/
$(document).ready(function() {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function(evt) {
        window.location.replace("/reactivos/list?size=" + this.value + "&page=1"+"&codProasecal="+$('#codProasecal').val()+"&grupo="+$('#grupo').val()+"&programas="+$('#programas').val()+"&nombreReactivo="+$('#nombreReactivo').val()+"&estado="+$('#estado').is(":checked")+"&nameColumn=" + nameColumn +  "&sortBy=" + sort );
    });
}


$(document).ready(function() {
    $("#buscar").click(function() {
        window.location.replace("/reactivos/list"+"?codProasecal="+$('#codProasecal').val()+"&grupo="+$('#grupo').val()+"&programas="+$('#programas').val()+"&nombreReactivo="+$('#nombreReactivo').val()+"&nameColumn=" + nameColumn +  "&sortBy=" + sort +"&page=1"+"&size"+$('#pageSizeSelect').val()+"&estado="+$('#estado').is(":checked"));
    });
});

function redirect(url) {
    if(sort == "ASC"){
        window.location.replace("/reactivos/list"+"?codProasecal="+$('#codProasecal').val()+"&grupo="+$('#grupo').val()+"&programas="+$('#programas').val()+"&nombreReactivo="+$('#nombreReactivo').val()+ "&nameColumn="+url+"&sortBy=DESC"+"&page=1"+"&size="+$('#pageSizeSelect').val()+"&estado="+$('#estado').is(":checked"));
    }else if(sort == "DESC"){
        window.location.replace("/reactivos/list"+"?codProasecal="+$('#codProasecal').val()+"&grupo="+$('#grupo').val()+"&programas="+$('#programas').val()+"&nombreReactivo="+$('#nombreReactivo').val()+"&estado="+$('#estado').is(":checked")+
            "&nameColumn="+url+"&sortBy=ASC"+"&page=1"+"&size="+$('#pageSizeSelect').val());
    }


}

function openDeleteModal(reactivo) {
    $('#reactivoID').val(reactivo)
    $('#openDeleteModal').modal('show');
}

function borrarReactivo() {
    window.location.replace("/reactivos/delete?reactivoID=" + $('#reactivoID').val() + '');
    $('#reactivoID').val('');
    $('#openDeleteModal').modal('hide');
}

/*]]>*/


$(document).ready(function(){
    $('#programas').select2();
    document.getElementById("reactivoForm").style.display = "block";
    var nuevaCadena = nameColumn.replace(".", "");
    document.getElementById("Ico"+nuevaCadena).style.color="#d43f3a" ;
    $("#Ico"+nuevaCadena).removeClass("fa fa-chevron-up");
    if (sort == "ASC") {
    	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-up");
    }
    else{
   	 $("#Ico"+nuevaCadena).addClass("fa fa-chevron-down");
    }

});



