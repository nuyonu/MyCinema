package app.controller;

import app.controller.dao.AjaxResponseBody;
import app.controller.dao.LoginInput;
import app.controller.services.ICookieService;
import app.database.entities.User;
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


    @PostMapping(value = "/goToHome")
    public String auth(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInput user) {


        User userDatabase = service.findByUsername(user.getUsername());
        if (userDatabase == null) return REDIRECT_LOGIN;
        if (userDatabase.getUsername().equals(user.getUsername()) && userDatabase.getPassword().equals(user.getPassword())) {
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
        if (userDatabase != null && matchUser(userDatabase, user)) {
            result.setMsg("Corect");
            return ResponseEntity.ok(result);
        }
        result.setMsg("Your password or email is wrong!");
        return ResponseEntity.ok(result);
    }

    @Autowired
    private ICookieService cookieService;
    private static final String REDIRECT_LOGIN = "redirect:/Login";

    private static boolean matchUser(User user, LoginInput input) {
        return user.getUsername().equals(input.getUsername()) || user.getUsername().equals(input.getPassword());
    }
}