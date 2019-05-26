package app.controller;

import app.controller.dao.AjaxResponseBody;
import app.controller.dao.LoginInput;
import app.controller.services.CryptoService;
import app.controller.services.ICookieService;
import app.controller.services.ICryptoService;
import app.database.entities.Activation;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryActivation;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    IRepositoryUser service;

    @GetMapping("disconnect")
    public String disconnect(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(name = "input") LoginInput user) {
        cookieService.setConfig(request, response);
        cookieService.disconnect();
        return REDIRECT_LOGIN;
    }

    @GetMapping("/Login")
    public String start(HttpServletRequest request, HttpServletResponse response, Model model) {
        cookieService.setConfig(request, response);
        if (cookieService.isConnected()) return "redirect:/home";
        cookieService.createCookie();
        model.addAttribute("LoginInput", new LoginInput());
        return "Login";
    }

    @GetMapping("/activation")
    public String activation(@RequestParam(name = "code") String code) {
        repositoryActivation.deleteByCode(code);
        return REDIRECT_LOGIN;
    }

    @PostMapping(value = "/goToHome")
    public String auth(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInput user) {


        User userDatabase = service.findByUsername(user.getUsername());

        if (userDatabase == null) return REDIRECT_LOGIN;
        if (userDatabase.getUsername().equals(user.getUsername()) && cryptoService.decrypt(userDatabase.getPassword()).equals(user.getPassword())) {
            cookieService.setConfig(request, response);
            cookieService.setCookie(user.getUsername(), user.isRemainConnected());
            return "redirect:/home";
        }
        return REDIRECT_LOGIN;
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<AjaxResponseBody> getMessage(@RequestBody LoginInput user) {
        AjaxResponseBody result = new AjaxResponseBody();
        User userDatabase = service.findByUsername(user.getUsername());
        if (userDatabase != null && matchUser(userDatabase, user, cryptoService)) {
            Activation activation = repositoryActivation.findByEmail(userDatabase.getEmail());
            if (activation == null) {
                result.setMsg("Corect");
                return ResponseEntity.ok(result);
            } else {
                result.setMsg("email");
                return ResponseEntity.ok(result);
            }

        }
        result.setMsg("Your password or username is wrong!");
        return ResponseEntity.ok(result);
    }


    private ICryptoService cryptoService = new CryptoService();

    @Autowired
    IRepositoryActivation repositoryActivation;

    @Autowired
    private ICookieService cookieService;
    private static final String REDIRECT_LOGIN = "redirect:/Login";


    private static boolean matchUser(User user, LoginInput input, ICryptoService service) {
        return user.getUsername().equals(input.getUsername()) && service.decrypt(user.getPassword()).equals(input.getPassword());
    }


}