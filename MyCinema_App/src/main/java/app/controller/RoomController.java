package app.controller;

import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RoomController {

    @Autowired
    IRepository repository;

    @GetMapping(value = "reservation")
    public String room(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        return "Room";
    }


}
