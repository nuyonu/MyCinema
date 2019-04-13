package App.Controller;

import App.Controller.Dao.Reset;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ResetPasswordController {


    @GetMapping("/ForgotPassword")
    public String reset(Model model) {
        model.addAttribute("Reset", new Reset());
        return "ForgotPassword";
    }

    @RequestMapping(value = "/reset",method = RequestMethod.POST)
    public  String ResetEmail(@ModelAttribute Reset reset){
//         todo emAIL
        return  "redirect:/Login";
    }
}
