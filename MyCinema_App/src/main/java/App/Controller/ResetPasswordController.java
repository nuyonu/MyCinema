package App.Controller;

import App.Controller.Dao.Reset;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResetPasswordController {


    @GetMapping("/ForgotPassword")
    public String reset(Model model) {
        model.addAttribute("Reset", new Reset());
        return "ForgotPassword";
    }

    @PostMapping(value = "/reset")
    public  String ResetEmail(@ModelAttribute Reset reset){
        return  "redirect:/Login";
    }
}
