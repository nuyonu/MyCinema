package app.controller;

import app.controller.services.CommonFunctions;
import app.controller.services.CookieHandler;
import app.controller.services.ICookieService;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import app.database.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SettingsController {

    private static final String REDIRECT_TO_SETTINGS = "redirect:/settings";

    @Autowired
    private IRepositoryUser userRepository;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private List<String> errorMessages;
    private List<String> successfulMessages;

    @GetMapping("/settings")
    public String getSettings(HttpServletRequest request, HttpServletResponse response, Model model) {

        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";

        model.addAttribute("userInfo", userRepository.findByUsername(getUsernameFromCookie(request)));

        return "settings";
    }

    @PostMapping("/changeInformation")
    public String changeInformation(@RequestParam(value = "firstName", required = false) String firstName,
                                    @RequestParam(value = "lastName", required = false) String lastName,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        cookieService.setConfig(request,response);
        if (!cookieService.isConnected()) return "error403";

        User user = userRepository.findByUsername(getUsernameFromCookie(request));

        if (user == null) {
            String error = "Invalid user: " + getUsernameFromCookie(request);
            logger.error(error);
            return "redirect:/Login";
        }

        if (!firstName.isEmpty()) {
            if (!CommonFunctions.lengthBetween(firstName, 2, 30))
                errorMessages.add("firstName trebuie sa contina minim 3 caractere si maxim 30");
            user.setFirstName(firstName);
        }

        if (!lastName.isEmpty()) {
            if (!CommonFunctions.lengthBetween(lastName, 2, 30))
                errorMessages.add("FirstName trebuie sa contina minim 3 caractere si maxim 30");
            user.setLastName(lastName);
        }

        if (!email.isEmpty()) {
            if (!EmailValidator.getInstance().isValid(email))
                errorMessages.add("Email is not valid");
            user.setEmail(email);
        }

        if (!phoneNumber.isEmpty()) {
            if (!CommonFunctions.isPhoneNumber(phoneNumber))
                errorMessages.add("Phone number is not valid");
            user.setPhoneNumber(phoneNumber);
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return REDIRECT_TO_SETTINGS;
        }

        userRepository.save(user);
        successfulMessages.add("Ai modificat cu succes datele tale");
        redirectAttributes.addFlashAttribute("successfulMessages", successfulMessages);

        return REDIRECT_TO_SETTINGS;

    }

    @PostMapping("/changeAvatar")
    public String changeAvatar(@RequestParam("imageFile") MultipartFile imageFile,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (imageFile.getContentType().equals("image/gif") ||
                imageFile.getContentType().equals("image/jpeg") ||
                imageFile.getContentType().equals("image/png")) {
            String usernameFromRequest = getUsernameFromCookie(request);

            User user = userRepository.findByUsername(usernameFromRequest);

            userService.saveAvatarImage(imageFile, user);

            user.setAvatarImagePath("/images/userAvatarImages/" + user.getId() + ".jpg");
            userRepository.save(user);

            successfulMessages.add("Ai modificat cu succes poza de profil.");
            redirectAttributes.addFlashAttribute("successfulMessages", successfulMessages);

            return REDIRECT_TO_SETTINGS;
        } else {
            errorMessages.add("Poți încărca doar imagini cu extensia .jpg/.jpeg, .png, .gif");
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return REDIRECT_TO_SETTINGS;
        }
    }

    @GetMapping("/images/userAvatarImages/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageId) {
        Path path = Paths.get("src/main/resources/static/images/userAvatarImages/" + imageId);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.getStackTrace();
        }

        return new byte[0];
    }

    private String getUsernameFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("user"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
    @Autowired
    private ICookieService cookieService;
}
