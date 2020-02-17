/*<![CDATA[*/
var sort = /*[[${sort}]]*/ 'DESC';
$(document).ready(function() {
    changePageAndSize();

});

function changePageAndSize() {
    $('#pageSizeSelect').change(function(evt) {
        window.location.replace("/programas/list?size=" + this.value + "&page=1"+"&find="+$('#criterio').val());
    });
}


$(document).ready(function() {
    $("#buscar").click(function() {
        window.location.replace("/programas/list"+"?find="+$('#criterio').val()+"&findColumn="+$('#filtrosList :selected').attr('id')+
            "&nameColumn="+$('#filtrosList :selected').attr('id')+"&sortBy=DESC"+"&page=1"+"&size"+$('#pageSizeSelect').val());
    });
});

function redirect(url) {
    if(sort == "ASC"){
        window.location.replace("/programas/list"+"?find="+$('#criterio').val()+"&findColumn="+$('#filtrosList :selected').attr('id')+
            "&nameColumn="+url+"&sortBy=DESC"+"&page=1"+"&size"+$('#pageSizeSelect').val());
    }else if(sort == "DESC"){
        window.location.replace("/programas/list"+"?find="+$('#criterio').val()+"&findColumn="+$('#filtrosList :selected').attr('id')+
            "&nameColumn="+url+"&sortBy=ASC"+"&page=1"+"&size"+$('#pageSizeSelect').val());
    }


}

$(document).ready(function() {
    $('#filtrosList').change(function(evt) {
if($('#filtrosList :selected').attr('id')=="estado"){
    $( function() {
        var availableTags = [
            "activo",
            "inactivo"
        ];
        $( ":input" ).autocomplete({
            source: availableTags
        });
    } );
}else{
    $( function() {
        var availableTags = [
            ""
        ];
        $( ":input" ).autocomplete({
            source: availableTags
        });
    } );
}
    });
});

/*]]>*/
