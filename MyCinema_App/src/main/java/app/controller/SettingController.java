package app.controller;

import app.controller.services.CookieHandler;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SettingController {

    @Autowired
    IRepository service;


    @GetMapping("/settings")
    public String getSetting(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isConnected()) return "error403";

        String usernameFromRequest = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("user"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (!service.userFindByUsername(usernameFromRequest).equals(" ")) {
            String username = service.userFindByUsername(usernameFromRequest).getUsername();
            String lastName = service.userFindByUsername(usernameFromRequest).getLastName();
            String firstName = service.userFindByUsername(usernameFromRequest).getFirstName();
            String email = service.userFindByUsername(usernameFromRequest).getEmail();

            List<String> userInformation = new ArrayList<>();
            userInformation.add(username);
            userInformation.add(lastName);
            userInformation.add(firstName);
            userInformation.add(email);

            model.addAttribute("details",userInformation);
        }

        return "Settings";
    }
}
