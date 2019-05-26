package app.controller;

import app.controller.services.CommonFunctions;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SettingsController {

    private static final String REDIRECT_TO_SETTINGS = "redirect:/settings";

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private UserService userService;

    @Autowired
    private ICookieService cookieService;

    private Logger logger = LoggerFactory.getLogger(SettingsController.class);

    private List<String> errorMessages;
    private List<String> successfulMessages;

    @GetMapping("/settings")
    public String getSettings(HttpServletRequest request, HttpServletResponse response, Model model) {

        cookieService.setConfig(request, response);
        if (!cookieService.isConnected()) return "error403";

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "settings";
    }

    @PostMapping("/changeInformation")
    public String changeInformation(@RequestParam(value = "firstName", required = false) String firstName,
                                    @RequestParam(value = "lastName", required = false) String lastName,
                                    @RequestParam(value = "email", required = false) String email,
                                    @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                    @RequestParam(value = "birthDate", required = false) String birthDate,
                                    @RequestParam(value = "new-password", required = false) String newPassword,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        cookieService.setConfig(request, response);
        if (!cookieService.isConnected()) return "error403";

        User user = repositoryUser.findByUsername(cookieService.getUser());

        if (user == null) {
            String error = "Invalid user: " + cookieService.getUser();
            logger.error(error);
            return "redirect:/Login";
        }

        user.setFirstName(checkFirstName(firstName, user.getFirstName()));
        user.setLastName(checkLastName(lastName, user.getLastName()));

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

        if (!birthDate.isEmpty()) {
            try {
                user.setBirthDate(new SimpleDateFormat("dd-mm-yyyy").parse(birthDate));
            } catch (ParseException e) {
                String error = "Error parse date " + e;
                errorMessages.add("Invalid date format. Contact the administrator");
                logger.error(error);
            }
        }

        if (!newPassword.isEmpty() && checkPassword(newPassword))
            user.setPassword(newPassword);

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return REDIRECT_TO_SETTINGS;
        }

        repositoryUser.save(user);
        successfulMessages.add("You have successfully modified your data");
        redirectAttributes.addFlashAttribute("successfulMessages", successfulMessages);

        return REDIRECT_TO_SETTINGS;

    }

    private boolean checkPassword(String newPassword) {
        if (newPassword.length() < 8 || newPassword.length() > 30) {
            errorMessages.add("The password must be between 8 and 30 characters");
            return false;
        }
        if (newPassword.contains(" ")) {
            errorMessages.add("The password can not have white space");
            return false;
        }

        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;
        boolean specialCharacterPresent = false;

        for (int index = 0; index < newPassword.length(); index++) {
            currentCharacter = newPassword.charAt(index);
            if (Character.isDigit(currentCharacter))
                numberPresent = true;
            else if (Character.isUpperCase(currentCharacter))
                upperCasePresent = true;
            else if (Character.isLowerCase(currentCharacter))
                lowerCasePresent = true;
            else if (specialChars.contains(String.valueOf(currentCharacter)))
                specialCharacterPresent = true;
        }
        if(!numberPresent)
            errorMessages.add("The password must contain at least one digit");
        if(!upperCasePresent)
            errorMessages.add("The password must contain at least one big letter");
        if(!lowerCasePresent)
            errorMessages.add("The password must contain at least one small letter");
        if(!specialCharacterPresent)
            errorMessages.add("The password must contain at least one special character");

        return numberPresent && upperCasePresent && lowerCasePresent && specialCharacterPresent;
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

            User user = repositoryUser.findByUsername(usernameFromRequest);

            userService.saveAvatarImage(imageFile, user);

            user.setAvatarImagePath("/images/userAvatarImages/" + user.getId() + ".jpg");
            repositoryUser.save(user);

            successfulMessages.add("You have successfully modified your profile photo");
            redirectAttributes.addFlashAttribute("successfulMessages", successfulMessages);

            return REDIRECT_TO_SETTINGS;
        } else {
            errorMessages.add("You can only upload images with the .jpg / .jpeg, .png, .gif extension");
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return REDIRECT_TO_SETTINGS;
        }
    }

    @GetMapping("/images/userAvatarImages/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageId) {
        if (!imageId.matches("[a-zA-Z0-9.]++")) {
            return new byte[0];
        }

        Path path = Paths.get("src/main/resources/static/images/userAvatarImages/" + imageId);
        if (Files.exists(path))
            return CommonFunctions.imageFromPath(path);
        return new byte[0];

    }

    private String checkFirstName(String newFirstName, String userFirstName) {
        if (!newFirstName.isEmpty()) {
            if (!CommonFunctions.lengthBetween(newFirstName, 2, 30))
                errorMessages.add("First Name must contain a minimum of 3 characters and a maximum of 30 characters");
            return newFirstName;
        }
        return userFirstName;
    }

    private String checkLastName(String newLastName, String userLastName) {
        if (!newLastName.isEmpty()) {
            if (!CommonFunctions.lengthBetween(newLastName, 2, 30))
                errorMessages.add("Last Name must contain a minimum of 3 characters and a maximum of 30 characters");
            return newLastName;
        }

        return userLastName;
    }

    private String getUsernameFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("user"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
