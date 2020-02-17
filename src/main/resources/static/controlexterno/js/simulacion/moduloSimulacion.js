function signaling(el, value, colors = {}) {
    if (colors.yellow && colors.red) {
        if (value > colors.yellow.valor) {
            el.style.background = colors.yellow.color;
        }
        if (value >= colors.red.valor) {
            el.style.background = colors.red.color;
            el.style.background = colors.red.color;
            if (el.getAttribute('data-type') === 'IV-CLIA') {
                let cc = el.parentElement.querySelector('[data-type="Ab"]');
                let checkbox = cc.querySelector('input[type="checkbox"]');
                checkbox.checked = true;
            }
        }
    }
}

function applySignaling() {
    let tds = document.querySelectorAll('[data-type="IV-CLIA"], [data-type="IV-GRB"], [data-type="IV-ALGA"], [data-type="IV-SD"],[data-type="IV-CLIA-EF"]') || [];
    tds.forEach((td = {}) => {
        let value = Math.abs(Number((td.innerText || '').replace(',', '') || 0));
        signaling(td, value, {red, yellow});
    });
}

function abrir(evt, tab) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(tab).style.display = "block";
    evt.currentTarget.className += " active";
}


function seccion(evt, id) {
    if ($('#btnEscenario' + id).text() === "+") {
        $('#btnEscenario' + id + '').text("-");
    } else {
        $('#btnEscenario' + id + '').text("+")
    }
    $('#detalleEscenario' + id + '').slideToggle();
};


function eliminarEscenario(id) {
    if (confirm("¿Desea eliminar los escenarios fijos para este mensurando y método?") == true) {
        $.ajax({
            url: "/controlexterno/eliminarEscenarios/" + id,
            success: function (data) {
                window.location.reload();
            },
            error: function (e) {
                alert("No se pudieron eliminar los escenarios fijos");
            }
        });
    }
};

$("#HistoricoTab").click(function () {
    $("#historicoFrag").load('/controlexterno/historico', 'muestra=' + $("#idMuestra").val() + '&mensurando=' + $("#mensurandosId").val() + '');
});

$("#ConsultaTab").click(function () {
    $("#consultaFrag").load('/controlexterno/consulta', 'muestra=' + $("#idMuestra").val() + '');
});

$("#EscenarioTab").click(function () {
    if ($("#idMetodos").val() !== '') {
        $("#escenarioFijoFrag").load(
            '/controlexterno/escenarioFijo',
            `muestra=${$("#idMuestra").val()}&mensurando=${$("#mensurandosId").val()}&metodo=${$("#idMetodos").val()}`,
            () => applySignaling()
        );
    }
});

function regresarList(idMuestra) {
    window.location.replace("/controlexterno/listResultadosDirector?muestra=" + idMuestra + "&laboratorio=&sede=&usuario=&estado=Todos");
}

function openTabEscenario() {
    document.getElementById("EscenarioTab").click(event, 'Escenario')
};

/*
$('#simuladorForm.calcular').submit(function() {

	//debugger;
    if (confirm("Va a fijar el escenario, ¿desea continuar?") == true) {
	return true;
	}else{
	return false; // return false to cancel form action
	}
});

*/


$("#tipoSubmit").click(function () {
    if (confirm("Va a fijar el escenario, ¿desea continuar?") == true) {
        return true;
    } else {
        return false; // return false to cancel form action
    }
    //$("#submit").trigger("click"); 
});


// //IV-GRB
// (()=>{
//     var tds = document.querySelectorAll('[data-type="IV-GRB"]');
//     tds.forEach( (td = {}) => {
//         let value = (td.innerText || '').replace(',','');
//         value = Number(value);
//         value = Math.abs(value);
//     });
// })();
//
// //IV-ALGA
// (()=>{
//     var tds = document.querySelectorAll('[data-type="IV-ALGA"]');
//     tds.forEach( (td = {}) => {
//         let value = (td.innerText || '').replace(',','');
//         value = Number(value);
//         value = Math.abs(value);
//
//         if(value>yellow.valor){
//             td.style.background = yellow.color;
//         }
//         if(value>red.valor){
//             td.style.background = red.color;
//             td.style.background = red.color;
//             let asd = td.parentElement.querySelector('[data-type="Ab"]');
//             let cc = asd.parentElement.querySelector('[data-type="Ab"]');
//             let checkbox = cc.querySelector('input[type="checkbox"]');
//             checkbox.checked = true;
//         }
//     });
// })();
// //IV-SD
// (()=>{
//     var tds = document.querySelectorAll('[data-type="IV-SD"]');
//     tds.forEach( (td = {}) => {
//         let value = (td.innerText || '').replace(',','');
//         value = Number(value);
//         value = Math.abs(value);
//
//         if(value>yellow.valor){
//             td.style.background = yellow.color;
//         }
//         if(value>red.valor){
//             td.style.background = red.color;
//             td.style.background = red.color;
//             let asd = td.parentElement.querySelector('[data-type="Ab"]');
//             let cc = asd.parentElement.querySelector('[data-type="Ab"]');
//             let checkbox = cc.querySelector('input[type="checkbox"]');
//             checkbox.checked = true;
//         }
//         debugger;
//     });
// })();


$(document).ready(function () {
    document.querySelector('.preload').style.setProperty('display', 'none');
});


//TEMP

$("#img15469540742").hover(function () {
    $("#15469540742").css("display", "block");
}, function () {
    $("#15469540742").css("display", "none");
});
$("#img15469540741").hover(function () {
    $("#15469540741").css("display", "block");
}, function () {
    $("#15469540741").css("display", "none");
});
$("#img15469540744").hover(function () {
    $("#15469540744").css("display", "block");
}, function () {
    $("#15469540744").css("display", "none");
});
$("#img15469540740").hover(function () {
    $("#15469540740").css("display", "block");
}, function () {
    $("#15469540740").css("display", "none");
});
$("#img1546954074011123").hover(function () {
    $("#1546954074011123").css("display", "block");
}, function () {
    $("#1546954074011123").css("display", "none");
});

$("#img15469540740232").hover(function () {
    $("#15469540740232").css("display", "block");
}, function () {
    $("#15469540740232").css("display", "none");
});
///

$("#img15469540740222").hover(function () {
    $("#15469540740222").css("display", "block");
}, function () {
    $("#15469540740222").css("display", "none");
});
$("#img1546951240740").hover(function () {
    $("#1546951240740").css("display", "block");
}, function () {
    $("#1546951240740").css("display", "none");
});
$("#img15469564340740").hover(function () {
    $("#15469564340740").css("display", "block");
}, function () {
    $("#15469564340740").css("display", "none");
});
$("#img15461119540740").hover(function () {
    $("#15461119540740").css("display", "block");
}, function () {
    $("#15461119540740").css("display", "none");
});
$("#img15469563240740").hover(function () {
    $("#15469563240740").css("display", "block");
}, function () {
    $("#15469563240740").css("display", "none");
});
$("#img15469540216740").hover(function () {
    $("#15469540216740").css("display", "block");
}, function () {
    $("#15469540216740").css("display", "none");
});
$("#img15469540745540").hover(function () {
    $("#15469540745540").css("display", "block");
}, function () {
    $("#15469540745540").css("display", "none");
});
$("#img154695407892340").hover(function () {
    $("#154695407892340").css("display", "block");
}, function () {
    $("#154695407892340").css("display", "none");
});
$("#img15499769540740").hover(function () {
    $("#15499769540740").css("display", "block");
}, function () {
    $("#15499769540740").css("display", "none");
});
$("#img15333740").hover(function () {
    $("#15333740").css("display", "block");
}, function () {
    $("#15333740").css("display", "none");
});
$("#img15555540").hover(function () {
    $("#15555540").css("display", "block");
}, function () {
    $("#15555540").css("display", "none");
});