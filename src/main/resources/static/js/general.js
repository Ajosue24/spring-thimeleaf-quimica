    $('#programa').autocomplete({
        source: function (request, response) {
            var programa = $('#programa').val();
            $.ajax({
                url: '/rest/programas/' + programa,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {
                        return {label: item.nombrePrograma.trim(), value: item.nombrePrograma.trim(), id: item.programaId};
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }, select: function (event, ui) {
            $("#idPrograma").val(ui.item.id);
           // debugger;
        }
    });
    
    $('#tipo').autocomplete({
        source: function (request, response) {
            var tipo = $('#tipo').val();          
            $.ajax({
                url: '/rest/tipo/' + tipo,
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    response($.map(data, function (item) {                 
                        return {label: item.nombre.trim(), value: item.nombre.trim(), id: item.tiposMuestrasId};
                    }));
                },
                error: function (xhr, status, error) {
                    alert("Error");
                }
            });
        }, select: function (event, ui) {
            $("#idTipoMuestra").val(ui.item.id);
        }
    });    
	