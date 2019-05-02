package app.controller;

import app.controller.services.ICookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RoomController {


    @GetMapping(value = "reservation")
    public String room(HttpServletRequest request, HttpServletResponse response, Model model)

    {
       cookieService.setConfig(request,response);
        model.addAttribute("user",cookieService.getUser());
        return "Room";
    }
    @Autowired
    private ICookieService cookieService;


}
