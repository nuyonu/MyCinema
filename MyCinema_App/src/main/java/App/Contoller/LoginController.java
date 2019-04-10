package App.Contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/Login")
    public String greeting(@RequestParam(name = "name", required = true, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        if (name.equals("filo"))
            return "Home";
        return "Login";
    }

    @GetMapping("/Login")
    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public String connect(@RequestParam(name = "name", required = true) String name, @RequestParam(name = "password", required = true) String password, Model model) {
//        model.addAttribute("name", name);
        System.out.println(name + " " + password);
        if (name.equals("filo"))
            return "Home";
        return "Login";
    }


}