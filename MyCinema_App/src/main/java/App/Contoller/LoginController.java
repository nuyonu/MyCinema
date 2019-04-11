package App.Contoller;

import App.Contoller.UtilClass.LoginClass;
import App.Contoller.UtilClass.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {


    @GetMapping("/Login")
    public String start(Model model) {
        model.addAttribute("student", new Student());
        return "Login";
    }


    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public String connect(@ModelAttribute LoginClass user, BindingResult errors, Model model) {
        System.out.println(user);
        if (user.getUsername() == "filo")
            return "Home";
        return "Login";
    }


}