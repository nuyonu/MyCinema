package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class StatisticsController {
    @GetMapping("statistics")
    public String statistics(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "Statistics";
    }
}
