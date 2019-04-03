$(document).ready(function(){
    setInitialTriggers();
    labelClickListener();
});

var setInitialTriggers = function() {
    $(".toggle-menu").on('click', function(e){
        e.preventDefault();
        $(".layout .l-left").toggleClass("on");
    })
}

var labelClickListener = function() {
    $(".field").on('click',function(){
        console.log("works");
        $(this).find(".formField").focus();
    });
}