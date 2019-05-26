$(document).ready(function () {
    if ($("#booked-movies-list")) {
        populate_booked_movies()
    }
}
)

var populate_booked_movies = function () {
    var loc = $("#booked-movies-list")
    var html = '<div class="list-group-item">Booked movies:</div>';
    $.get("api/notifications")
        .done(data => {
            if (data.length === 0) {
                html += '<div class="list-group-item"><em>No movies booked...</em></div>'
            }

            for (var el of data) {
                html += `<a href="/movies" class="list-group-item list-group-item-action">
                <img src="${el.moviePath}" class="movie_icon_drd">
                <b>${el.movieTitle}</b><br>
                <span>${el.date} ${el.time}</span>
            </a>`
            }
            loc.html(html)
        })
}