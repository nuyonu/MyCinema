package app.controller;

import app.controller.dao.Reset;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResetPasswordController {


    @GetMapping("/ForgotPassword")
    public String reset(Model model) {
        model.addAttribute("Reset", new Reset());
        return "ForgotPassword";
    }

    @PostMapping(value = "/reset")
    public String resetEmail(@ModelAttribute Reset reset) {
        return  "redirect:/Login";
    }
}
