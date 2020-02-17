$('input.inputNumeric').on('keypress keyup paste', e => {
    let target = $(e.target);
    let last = target.attr('data-last') || '';
    let value = target.val() || '';
    let invalid = !value.match(/^([0-9]+(\.([0-9]+)?)?)?$/) || (!value.includes('.') && value.length > 15);
    switch (e.type) {
        case 'paste': e.preventDefault(); break;
        case 'keyup':
            if (invalid) target.val(last);
            else target.attr('data-last', value);
            break;
        case 'keypress':
            if (invalid) target.val(last);
            break;
    }
});

function filterFloat(evt, input) {
    // Backspace = 8, Enter = 13, ‘0′ = 48, ‘9′ = 57, ‘.’ = 46, ‘-’ = 43
    var key = window.Event ? evt.which : evt.keyCode;
    var chark = String.fromCharCode(key);
    var tempValue = input.value + chark;
    if (key >= 48 && key <= 57) {
        if (filter(tempValue) === false) {
            return false;
        } else {
            return true;
        }
    } else {
        if (key == 8 || key == 13 || key == 0) {
            return true;
        } else if (key == 46 || key == 44) {
            if (filter(tempValue) === false) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}

function filter(__val__) {
    var preg = /\d{1,2}([\.,][\d{1,2}])?/;
    if (preg.test(__val__) === true) {
        return true;
    } else {
        return false;
    }

}

function check(e) {
    tecla = (document.all) ? e.keyCode : e.which;
//alert (tecla);
    var permitidos = [8, 32, 42, 47, 43, 45, 33, 44, 209, 95, 191, 161, 63, 59, 46,241];

    if (permitidos.indexOf(tecla) == -1) {
        // Patron de entrada, en este caso solo acepta numeros y letras
        patron = /[A-Za-z0-9]/;
        tecla_final = String.fromCharCode(tecla);
        return patron.test(tecla_final);
    }

    //Tecla de retroceso para borrar, siempre la permite
    /* if (tecla == 8 && tecla == 32) {
         return true;
     }
 */

}

var rounding = {};

function roundValue(input, wait = 0) {
    let id = input.attr('id');
    let value = input.val() || "";
    let round = (n, dec = 0) => (+(Math.round(+n + `e+${dec}`) + `e-${dec}`)).toFixed(dec);

    if (!!rounding[id]) clearTimeout(rounding[id]);
    if (value !== "")
        rounding[id] = setTimeout(() => input.val(round(value, input.data().round)), wait);
}

$(document).ready(function () {
    var preload = document.querySelector('.preload') || {};
    if (!!preload.style)
        preload.style.setProperty('display', 'none');
    $('input[data-round]').on('blur keyup', (e) => roundValue($(e.target), e.type === 'keyup' ? 1000 : 0));
    $('#simuladorForm').on('submit', (e) => roundValue($('input[data-round]')));
});


function mensajeDecimales(cant) {
    for (var i = 0; i < cant; i++) {
        valorInf = $("#valorInformado" + i).val();
        if (valorInf % 1 == 0) {
            //alert ("Es un numero entero");
        } else {
            alert('Algunos valores han sido ajustados según la cantidad de cifras decimales establecidas en el instructivo del programa.');
            break;
        }
    }
}
