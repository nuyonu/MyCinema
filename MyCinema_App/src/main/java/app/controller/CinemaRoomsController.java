package app.controller;

import app.controller.dao.RoomDao;
import app.controller.dao.Search;
import app.controller.services.ICookieService;
import app.database.entities.CinemaRoom;
import app.database.infrastructure.IRepositoryCinemaRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CinemaRoomsController {
    @Autowired
    IRepositoryCinemaRoom cinemaRoomRepository;


    @GetMapping("/cinema-rooms")
    public String rooms(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return "error403";
        model.addAttribute("user", cookieService.getUser());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());

        return "Cinema-rooms";
    }

    @PostMapping("/api/searchCinema")
    @ResponseBody
    public ResponseEntity<RoomDao> getMessage(@RequestBody Search search) {

        List<CinemaRoom> cinemaRooms = cinemaRoomRepository.findByNameLike(search.getInput_search());
        if (cinemaRooms.isEmpty()) {

            return ResponseEntity.notFound().build();
        } else {

            return ResponseEntity.ok().body(new RoomDao(cinemaRooms));
        }
    }

    @Autowired
    private ICookieService cookieService;
}
