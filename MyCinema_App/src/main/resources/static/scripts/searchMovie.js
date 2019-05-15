// $("#search").submit(
function search_func() {


    //stop submit the form event.
    // event.preventDefault();

    var search = {}
    search["input_search"] = $("#input_search").val();


    // $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/search",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log(data);
            document.getElementById('list_movie').innerHTML = "";

            for (var movieIndex = 0; movieIndex < data.listOfResults.length; movieIndex++) {
                var movie = data.listOfResults[movieIndex];
                $("#list_movie").append(
                    "<form action=\"#\" method=\"post\" action=\"/program?day=0&idMovie=id\">" +
                    "<div class=\"movies-data-show\" " + "style=\"background: url( " + movie.path + "); background-repeat: no-repeat; background-size: 100% 100%;\">" +
                    "<div class=\"movie-name\">" +
                    "<p> " + movie.name + "</p>" +
                    "</div>" +
                    "<div class=\"movie-price\">" +
                    "<p >Price:" + movie.price + "</p>" +
                    "</div>" +
                    "<div class=\"movie-book-now\">" +
                    "<button class=\"movie-button-book-now\" type=\"submit\">Book now</button>" +
                    "</div>" +
                    "</div>" +
                    "</form>"
                )
                ;
            }


            $("#btn-search").prop("disabled", false);

        },
        error: function (e) {
            document.getElementById('list_movie').innerHTML = "";
            $("#list_movie").append("<div><h1>Sorry:(</h1><h1>We didn't find anything.</h1></div>")

            console.log("ERROR : ", e);

        },

    });

};
