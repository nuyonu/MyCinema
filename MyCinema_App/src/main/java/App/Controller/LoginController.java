package App.Controller;

import App.Controller.Dao.LoginInput;
import App.Controller.ServiceController.CookieHandler;
import App.Database.Entities.User;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    IRepository service;

    @GetMapping("/Login")
    public String start(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);

//        DatabasePop pop = new DatabasePop(service);
//        pop.pop();
        if (cookieHandler.isConnected()) return "redirect:/home";
        cookieHandler.createCookie();
        model.addAttribute("LoginInput", new LoginInput());
        return "Login";
    }


    @RequestMapping(value = "/goToHome", method = RequestMethod.POST)
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