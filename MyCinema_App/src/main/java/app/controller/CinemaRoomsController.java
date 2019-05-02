package app.controller;

import app.controller.services.CookieHandler;
import app.controller.services.ICookieService;
import app.database.infrastructure.IRepositoryCinemaRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CinemaRoomsController {
    @Autowired
    IRepositoryCinemaRoom cinemaRoomRepository;


    @GetMapping("/cinema-rooms")
    public String rooms(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected())
            return "error403";
        model.addAttribute("user",cookieService.getUser());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());

        return "Cinema-rooms";
    }
    @Autowired
    private ICookieService cookieService;
}
