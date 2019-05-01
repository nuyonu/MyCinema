package app.controller;

import app.controller.services.CookieHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController
{
    @GetMapping("/home")
    public String start(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isConnected()) return "error403";
        model.addAttribute("user",cookieHandler.getUser());
        return "Home";
    }
}