package App.Controller;

import App.Controller.ServiceController.CookieHandler;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;

@Controller
public class ReservationController {
    @Autowired
    IRepository service;

    @RequestMapping(value = "reservationPlace")
    public String reservation(HttpServletRequest request, HttpServletResponse response, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isConnected()) return "redirect:/error403";
        String idMovie = request.getParameter("idMovie");
        Integer day = Integer.valueOf(request.getParameter("day"));
        String time = request.getParameter("time");
        model.addAttribute("idMovie", idMovie);
        model.addAttribute("day", day);
        model.addAttribute("time", time);
        model.addAttribute("room", service.findRoomReservation(idMovie, time, day));
        return "Room";
    }

}
