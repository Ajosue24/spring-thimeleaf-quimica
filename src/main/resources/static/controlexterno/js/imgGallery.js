/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(
    function(){
        var galleryPosition = new Array();
        $('.imgGallery').click(function(e){
            e.preventDefault();
            var mod = $(this).closest('span').attr('mod');
            var urlGallery = $(this).closest('a').attr('url');
            $.ajax({url: urlGallery + "/" + mod, success: function(data){
                    galleryImg(data, urlGallery);
            }})
        });
        
        function galleryImg(result, urlGallery){
                var items = [];
                var resource;
                var aux = 0;
                 $.each($.parseJSON(result), function(key,value){
                //do something
                galleryPosition[aux]= value.position;
                urlResource = value.location; 
                if(value.location == ''){
                    urlResource = urlGallery.slice(0, urlGallery.search("carousel-samples/")) + "multimedia-resources/display-blob-img/" + value.img;
                }
                classDiv = 'galleryHidden';
                if(aux==0){
                  classDiv = 'galleryShown';  
                }
                switch(value.type){
                    case 1:
                        resource = "<div id ='resourceGallery" + value.position + "' position='" + value.position + "' class=" + classDiv + ">";
                        resource = resource + '<span class="galleryTitle">' + value.name + "</span><br />";
                        resource = resource + '<video controls>';
                        resource = resource + '<source src="'+ urlResource + '" type="video/mp4">';
                        resource = resource + 'Your browser does not support the video tag.</video>';
                        resource = resource + '<span class="galleryDescription">' + value.description + '</span></div>';
                    break;
                    case 2:
                        resource = "<div id ='resourceGallery" + value.position + "' position='" + value.position + "' class=" + classDiv + ">";
                        resource = resource + '<span class="galleryTitle">' + value.name + "</span><br />";
                        resource = resource + '<audio controls>';
                        resource = resource + '<source src="'+ urlResource + '" type="audio/mpeg">';
                        resource = resource + 'Your browser does not support the video tag.</audio>';
                        resource = resource + '<span class="galleryDescription">' + value.description + '</span></div>';
                    break;
                    case 3:
                        resource = "<div id ='resourceGallery" + value.position + "' position='" + value.position + "' class=" + classDiv + ">";   
                        resource = resource + '<span class="galleryTitle">' + value.name + "</span><br />";
                        resource = resource + "<img src='" + urlResource + "' />";
                        resource = resource + '<span class="galleryDescription">' + value.description + '</span></div>';
                    break;
                    
                }
                items[aux] = resource;
                aux = aux + 1;
                });
                if($('.showImage').length > 0){
                    $('.showImage').remove();
                }
                $('<div class="showImage"><div class="ControlGallery leftControl"><img src="http://localhost/scev3/img/icons/previous.svg" /></div><div class="ControlGallery rightControl"><img src="http://localhost/scev3/img/icons/next.svg" /></div>' + "<div class='galleryContent'><span class=\"closeWindow\">X</span>" + items.join('') + '</div></div>').prependTo('body');
                $('.showImage').css('display', 'block');
                $('.galleryContent').css('max-height', $(window).height());
                $('.galleryContent div video').css('height', ($('.galleryContent').height() - 100));
                $('.galleryContent div img').css('width', ($('.galleryContent').width() - 100));
                $('.galleryContent div img').css('max-height', ($('.galleryContent').height() - 100));
            }
            
            $('.leftControl').live('click', function(){
                resourcePosition = $('.galleryContent').find('.galleryShown').attr('position');
                var currentPosition;
                
                for (var i = 0; i < galleryPosition.length; i++) {
                    if (galleryPosition[i] == resourcePosition) {
                        currentPosition = i;
                    }
                }
                newPosition = galleryPosition[currentPosition - 1];
                if(currentPosition -1 < 0){
                   newPosition = galleryPosition[galleryPosition.length - 1];
                   $("video").paused;
                }
                
                $('.galleryContent').find('#resourceGallery' + resourcePosition).addClass('galleryHidden').removeClass('galleryShown');
                $('.galleryContent').find('#resourceGallery' + newPosition).addClass('galleryShown').removeClass('galleryHidden');
            });
            
            $('.rightControl').live('click', function(){
                resourcePosition = $('.galleryContent').find('.galleryShown').attr('position');
                var currentPosition;
                
                for (var i = 0; i < galleryPosition.length; i++) {
                    if (galleryPosition[i] == resourcePosition) {
                        currentPosition = i;
                    }
                }
                newPosition = galleryPosition[currentPosition + 1];
                if(currentPosition + 1 > galleryPosition.length - 1){
                   newPosition = galleryPosition[0];
                }                
                $('.galleryContent').find('#resourceGallery' + resourcePosition).addClass('galleryHidden').removeClass('galleryShown');
                $('.galleryContent').find('#resourceGallery' + newPosition).addClass('galleryShown').removeClass('galleryHidden');
                $("video").pause();
            })
            $(".closeWindow").live('click', function(){$(".showImage").remove();$('.showImage').css('display', 'none');});
    });