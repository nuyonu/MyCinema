package app.controller;

import app.controller.services.ICookieService;
import app.database.infrastructure.IRepositoryRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ReservationController {
    @Autowired
    IRepositoryRoom service;

    @PostMapping(value = "reservationPlace")
    public String reservation(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";

        String idMovie = request.getParameter("idMovie");
        Integer day = Integer.valueOf(request.getParameter("day"));
        String time = request.getParameter("time");
        model.addAttribute("idMovie", idMovie);
        model.addAttribute("day", day);
        model.addAttribute("time", time);
        model.addAttribute("user",cookieService.getUser());
        model.addAttribute("room", service.findByIdMovieAndTimeAndDay(idMovie, time, day));
        return "Room";
    }
    @Autowired
    private ICookieService cookieService;

}
