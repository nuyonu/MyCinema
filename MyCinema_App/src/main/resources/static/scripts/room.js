$(".room-view-table .chair").on('click', function(e){
    e.preventDefault();

    if($(this).hasClass('booked')) {
        $("#booked-already-modal").modal("show");
        return;
    }

    var row = $(this).data('row');
    var col = $(this).data('col');

    $('#book-now-modal input[name=seat_row]').val(row)
    $('#book-now-modal input[name=seat_col]').val(col)

    $('#book-now-modal input[name=row]').val(row)
    $('#book-now-modal input[name=col]').val(col)

    $('#book-now-modal').modal('show')
})