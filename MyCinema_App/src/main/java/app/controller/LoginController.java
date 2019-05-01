package app.controller;

import app.controller.dao.AjaxResponseBody;
import app.controller.dao.LoginInput;
import app.controller.services.CookieHandler;
import app.database.entities.User;
import app.database.service.IRepository;
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
    IRepository service;

    @GetMapping("disconnect")
    public String disconnect(HttpServletRequest request, HttpServletResponse response, @ModelAttribute(name = "input") LoginInput user) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        cookieHandler.disconnect();
        return "redirect:/Login";
    }

    @GetMapping("/Login")
    public String start(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (cookieHandler.isConnected()) return "redirect:/home";
        cookieHandler.createCookie();
        model.addAttribute("LoginInput", new LoginInput());
        return "Login";
    }


    @PostMapping(value = "/goToHome")
    public String auth(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginInput user) {


        User userDatabase = service.userFindByUsername(user.getUsername());
        if (userDatabase.getUsername().equals(user.getUsername()) && userDatabase.getPassword().equals(user.getPassword())) {
            CookieHandler cookieHandler = new CookieHandler(request, response);
            cookieHandler.setCookie(user.getUsername(), user.isRemainConnected());
            return "redirect:/home";
        }
        return "redirect:/Login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> getMessage(@RequestBody LoginInput user ){
        AjaxResponseBody result=new AjaxResponseBody();
        User userDatabase = service.userFindByUsername(user.getUsername());
        if (userDatabase.getUsername().equals(user.getUsername()) || userDatabase.getPassword().equals(user.getPassword())) {
                result.setMsg("Corect");
            return ResponseEntity.ok(result);
        }
        result.setMsg("Your password or email is wrong!");
        return ResponseEntity.ok(result);
    }
}