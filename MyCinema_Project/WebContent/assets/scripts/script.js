$(document).ready(function(){
    setInitialTriggers();
})

var setInitialTriggers = function() {
    $(".toggle-menu").on('click', function(e){
        e.preventDefault();
        $(".layout .l-left").toggleClass("on")
    })
}
