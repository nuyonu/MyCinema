package app.controller;

import app.controller.dao.RegisterModel;
import app.database.entities.User;
import app.database.exception.DuplicateData;
import app.database.exception.NullParameterPassed;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/signUp")
public class RegisterController {

    @Autowired
    IRepository service;

    @ModelAttribute("registerModel")
    public RegisterModel registerModel() {
        return new RegisterModel();
    }

    @GetMapping
    public String registerForm() {
        return "signUp";
    }

    @PostMapping
    public String postRegisterForm(@Valid @ModelAttribute("registerModel") RegisterModel registerModel, BindingResult bindingResult) throws NullParameterPassed, DuplicateData {
        if (bindingResult.hasErrors())
            return "signUp";

        service.addUser(new User(registerModel.getUsername(), registerModel.getFirstName(), registerModel.getLastName(),
                registerModel.geteMail(), registerModel.getPassword()));

        return "redirect:/Login";
    }
}