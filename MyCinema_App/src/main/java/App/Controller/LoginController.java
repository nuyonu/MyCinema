package App.Controller;

import App.Controller.Dao.LoginInput;
import App.Database.Entities.User;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    @Autowired
    IRepository service;

    @GetMapping("/Login")
    public String start(Model model) {
        model.addAttribute("LoginInput", new LoginInput());
        return "Login";
    }


    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String auth(@ModelAttribute LoginInput user) {
            User userDatabase=service.userFindByUsername(user.getUsername());
            System.out.println(userDatabase);
            System.out.println(user);
            if( userDatabase.getUsername().equals(user.getUsername()) || userDatabase.getPassword().equals(user.getPassword())) return "Home";
            return "redirect:/Login";
        }

    @RequestMapping(value="/auth",method =RequestMethod.GET)
    public String auth() {
        return "Login";
    }


    @RequestMapping(value="/sign",method =RequestMethod.POST)
    public String register() {
        System.out.println("register");
        return "Home";
    }


}