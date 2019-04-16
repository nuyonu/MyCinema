package app.controller;

import app.DatabasePop;
import app.controller.dao.LoginInput;
import app.controller.services.CookieHandler;
import app.database.entities.User;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    IRepository service;

    @GetMapping("/Login")
    public String start(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        DatabasePop pop = new DatabasePop(service);
        pop.pop(true);
        if (cookieHandler.isConnected()) return "redirect:/home";
        cookieHandler.createCookie();
        model.addAttribute("LoginInput", new LoginInput());
        return "Login";
    }


    @PostMapping(value = "/goToHome")
    public String auth(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInput user) {


        User userDatabase = service.userFindByUsername(user.getUsername());
        if (userDatabase.getUsername().equals(user.getUsername()) || userDatabase.getPassword().equals(user.getPassword())) {
            CookieHandler cookieHandler = new CookieHandler(request, response);
            cookieHandler.setCookie(user.getUsername(), user.isRemainConnected());
            return "redirect:/home";
        }
        return "redirect:/Login";
    }


}