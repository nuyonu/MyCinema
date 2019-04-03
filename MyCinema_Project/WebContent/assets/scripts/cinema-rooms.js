$(document).ready(function () {
    $(".badge:nth-child(1)").addClass("badge-active");
    
    $(".badge").on('click', function () {
        var index = ($(this).index());

        var sliderContainer = $(this).parent().parent();
        sliderContainer.find(".slide").hide();
        sliderContainer.find(".slide:nth-child(" + (index + 1) + ")").show();

        var badgesContainer = $(this).parent();
        badgesContainer.find(".badge").removeClass("badge-active");
        badgesContainer.find(".badge:nth-child(" + (index + 1) + ")").addClass("badge-active");
    });
});