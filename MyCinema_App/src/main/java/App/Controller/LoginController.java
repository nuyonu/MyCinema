package App.Controller;

import App.Controller.Dao.LoginInput;
import App.Controller.ServiceController.CookieHandler;
import App.Database.Entities.User;
import App.Database.Service.IRepository;
import App.DatabasePop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        pop.pop(false);
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