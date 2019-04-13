package App.Controller;

import App.Controller.Dao.RegisterModel;
import App.Database.Entities.User;
import App.Database.Exception.DuplicateData;
import App.Database.Exception.NullParameterPassed;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/SignUp")
public class RegisterController {

    @Autowired
    IRepository service;

    @ModelAttribute("registerModel")
    public RegisterModel registerModel() {
        return new RegisterModel();
    }

    @GetMapping
    public String registerForm() {
        return "/SignUp";
    }

    @PostMapping
    public String PostRegisterForm(@Valid @ModelAttribute("registerModel") RegisterModel registerModel, BindingResult bindingResult) throws NullParameterPassed, DuplicateData {
        if (bindingResult.hasErrors())
            return "/SignUp";

        service.addUser(new User(registerModel.getUsername(), registerModel.getFirstName(), registerModel.getLastName(),
                registerModel.geteMail(), registerModel.getPassword()));

        return "redirect:/Login";
    }
}