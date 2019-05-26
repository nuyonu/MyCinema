package app.controller;

import app.controller.dao.RegisterModel;
import app.controller.services.CryptoService;
import app.controller.services.ICryptoService;
import app.controller.services.MailServer;
import app.controller.services.Message;
import app.database.entities.Activation;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryActivation;
import app.database.infrastructure.IRepositoryUser;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
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
    private IRepositoryUser userRepository;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private IRepositoryActivation repositoryActivation;
    private ICryptoService service = new CryptoService();
    private MailServer mailServer = new MailServer();
    private RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();

    @ModelAttribute("registerModel")
    public RegisterModel registerModel() {
        return new RegisterModel();
    }

    @GetMapping
    public String registerForm() {
        return "signUp";
    }

    @PostMapping
    public String postRegisterForm(@Valid @ModelAttribute("registerModel") RegisterModel registerModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "signUp";
        userRepository.save(new User(registerModel.getUsername(), registerModel.getFirstName(), registerModel.getLastName(),
                registerModel.geteMail(), service.encrypt(registerModel.getPassword())));

        String code = generator.generate(200);
        Activation activation  =new Activation(registerModel.geteMail(),code);
        repositoryActivation.deleteByEmail(registerModel.geteMail());
        repositoryActivation.save(activation);
        mailServer.setEmailSender(emailSender);
        mailServer.sendSimpleMessage(registerModel.geteMail(),"Activation account", new Message(code,false).toString());
        return "redirect:/Login";
    }
}