$(".discardBtn").click(function () {
    $('input[type="text"]').val('');
    $('input[type="email"]').val('');
    $('input[type="date"]').val('');
    $('input[type="password"]').val('');
});

$(".changeAvatar").click(function() {
    $("#image-avatar").click();
});