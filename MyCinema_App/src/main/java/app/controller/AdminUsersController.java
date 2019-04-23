package app.controller;

import app.controller.services.CookieHandler;
import app.database.infrastructure.IRepositoryCinemaRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminUsersController {
    @Autowired
    IRepositoryCinemaRoom repository;

    @GetMapping(value = "/admin-users")
    public String showRooms(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        model.addAttribute("users", repository.findAll());
        return "AdminUsers";
    }
}
