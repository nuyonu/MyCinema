package app.controller;

import app.controller.services.ICookieService;
import org.springframework.beans.factory.annotation.Autowired;
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
        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user",cookieService.getUser());
        return "Home";
    }
    @Autowired
    ICookieService cookieService;
}