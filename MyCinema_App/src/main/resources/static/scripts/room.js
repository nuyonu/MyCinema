$(".room-view-table .chair").on('click', function(e){
    e.preventDefault();

    if($(this).hasClass('booked')) {
        $("#booked-already-modal").modal("show");
        return;
    }

    var row = $(this).data('row');
    var col = $(this).data('col');

    $('#book-now-modal input[name=seat-row]').val(row)
    $('#book-now-modal input[name=seat-col]').val(col)

    $('#book-now-modal input[name=row-value]').val(row)
    $('#book-now-modal input[name=column-value]').val(col)

    $('#book-now-modal').modal('show')
})