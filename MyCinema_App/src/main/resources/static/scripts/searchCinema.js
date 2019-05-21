function search_func()
{
    var search = {};
    search["input_search"] = $("#input_search").val();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/searchCinema",
        data: JSON.stringify(search),
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            console.log(data);
            document.getElementById('list_rooms').innerHTML = "";

            for (var roomIndex = 0; roomIndex < data.roomList.length; roomIndex++) {
                var room = data.roomList[roomIndex];
                console.log(room);
                $("#list_rooms").append(
                    "<div class=\"cinema-room\">" +
                        "<h5 >" + room.name + "</h5>" +
                        "<div class=\"slider-container w3-content w3-display-container\">" +
                            "<img class=\"slide\" src=\"/images/cinema-rooms/" + room.id + "/1.jpg\">" +
                            "<img class=\"slide\" src=\"/images/cinema-rooms/" + room.id + "/2.jpg\">" +
                            "<img class=\"slide\" src=\"/images/cinema-rooms/" + room.id + "/3.jpg\">" +
                            "<img class=\"slide\" src=\"/images/cinema-rooms/" + room.id + "/4.jpg\">" +
                            "<div class=\"badges-container w3-center w3-container w3-large w3-text-white w3-display-bottommiddle\">" +
                                "<span class=\"w3-badge badge w3-hover-white badge-active\"></span>" +
                                "<span class=\"w3-badge badge w3-hover-white\"></span>" +
                                "<span class=\"w3-badge badge w3-hover-white\"></span>" +
                                "<span class=\"w3-badge badge w3-hover-white\"></span>" +
                            "</div>" +
                        "</div>" +
                    "</div>"
                )
                ;

                $(".slider-container").slider('refresh');
            }

        },
        error: function (e) {
            document.getElementById('list_rooms').innerHTML = "";
            $("#list_rooms").append("<div><h1>Sorry:(</h1><h1>We didn't find anything.</h1></div>")

            console.log("ERROR : ", e);
        },
    });
};
