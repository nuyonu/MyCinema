package app.controller;

import app.controller.dao.ChangeInformationModel;
import app.controller.services.CookieHandler;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import app.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Controller
public class SettingsController {

    private static final String REDIRECT_TO_SETTINGS = "redirect:/settings";

    @Autowired
    private IRepositoryUser userRepository;

    @Autowired
    private UserService userService;

    @ModelAttribute("changeInformationModel")
    public ChangeInformationModel changeInformationModel() {
        return new ChangeInformationModel();
    }

    @GetMapping("/settings")
    public String getSettings(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isConnected()) return "error403";

        model.addAttribute("userInfo", getUserInformation(request));

        return "settings";
    }

    @PostMapping("/changeInformation")
    public String changeInformation(@Valid @ModelAttribute("changeInformationModel") ChangeInformationModel changeInformationModel, BindingResult bindingResult, Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userInfo", getUserInformation(request));

            return "settings";
        }

        String usernameFromRequest = getUsernameFromCookie(request);

        User user = userRepository.findByUsername(usernameFromRequest);
        user.setFirstName(changeInformationModel.getFirstName());
        user.setLastName(changeInformationModel.getLastName());
        user.setEmail(changeInformationModel.getEmail());
        user.setPhoneNumber(changeInformationModel.getPhoneNumber());
        userRepository.save(user);

        return REDIRECT_TO_SETTINGS;
    }

    @PostMapping("/changeAvatar")
    public String changeAvatar(@RequestParam("imageFile") MultipartFile imageFile,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        if (imageFile.getContentType().equals("image/gif") ||
                imageFile.getContentType().equals("image/jpeg") ||
                imageFile.getContentType().equals("image/png")) {
            String usernameFromRequest = getUsernameFromCookie(request);

            User user = userRepository.findByUsername(usernameFromRequest);


            userService.saveAvatarImage(imageFile, user);

            user.setAvatarImagePath("/images/userAvatarImages/" + user.getId() + ".jpg");
            userRepository.save(user);

            return REDIRECT_TO_SETTINGS;
        } else {
            redirectAttributes.addFlashAttribute("message", "Poți încărca doar imagini cu extensia .jpg/.jpeg, .png, .gif");
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

    private User getUserInformation(HttpServletRequest request) {
        String usernameFromRequest = getUsernameFromCookie(request);

        if (userRepository.findByUsername(usernameFromRequest) != null)
            return userRepository.findByUsername(usernameFromRequest);

        return null;
    }
}
