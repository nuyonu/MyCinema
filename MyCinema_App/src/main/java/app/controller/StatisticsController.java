package app.controller;

import app.controller.services.ICookieService;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class StatisticsController {
    @Autowired
    IRepositoryUser repositoryUser;

    @Autowired
    ICookieService cookieService;

    @GetMapping("statistics")
    public String statistics(HttpServletRequest request, HttpServletResponse response, Model model) {

        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";
        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "Statistics";
    }
}
