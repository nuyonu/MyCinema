package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminUsersController {
    @Autowired
    IRepositoryUser userRepository;

    @GetMapping("/admin-users")
    public String showUsers(HttpServletRequest request, HttpServletResponse response, Model model) {
        if (!new CookieHandler(request, response).isConnected())
            return "error403";

        model.addAttribute("users", userRepository.findAll());
        return "AdminUsers";
    }

    @PostMapping("/delete-selected-users")
    public String deleteSelectedUsers(@RequestParam(name = "usersForDelete", required = false) List<String> usersForDelete,
                                      RedirectAttributes redirectAttributes) {
        if (usersForDelete == null || usersForDelete.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nu poti sterge daca nu ai selectat nimic");
            return REDIRECT_TO_ADMIN_USERS;
        }

        for (String id : usersForDelete) {
            Optional<User> user = userRepository.findById(id);

            if (user.isPresent()) {
                userRepository.deleteById(id);
                if (!user.get().getAvatarImagePath().equals("/images/userProfileDefaultAvatar/userProfileDefaultAvatar.jpg"))
                    try {
                        Files.deleteIfExists(Paths.get("src/main/resources/static/images/userAvatarImages/" + id + ".jpg"));
                    } catch (IOException e) {
                        System.err.println("Couldn't delete profile file: " + e);
                    }
            }
        }

        if(usersForDelete.size() == 1)
            redirectAttributes.addFlashAttribute(SUCCESFUL_MESSAGE, "Ai eliminat utilizatorul selectat cu succes");
        else
            redirectAttributes.addFlashAttribute(SUCCESFUL_MESSAGE, "Ai eliminat utilizatorii selectati cu succes.");

        return REDIRECT_TO_ADMIN_USERS;
    }

    @PostMapping("/delete-by-username")
    public String deleteByUsername(@RequestParam(name = "usernameForDelete") String username, RedirectAttributes redirectAttributes)
    {
        if(username.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nu ai introdus nici un nume de utilizator.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        User user = userRepository.findByUsername(username);

        if(user == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Utilizatorul pe care ai incercat sa il elimini nu exista.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        userRepository.delete(user);
        if (!user.getAvatarImagePath().equals("/images/userProfileDefaultAvatar/userProfileDefaultAvatar.jpg"))
            try {
                Files.deleteIfExists(Paths.get("src/main/resources/static/images/userAvatarImages/" + user.getId() + ".jpg"));
            } catch (IOException e) {
                System.err.println("Couldn't delete profile file: " + e);
            }

        redirectAttributes.addFlashAttribute(SUCCESFUL_MESSAGE, "Ai eliminat utilizatorul " + user.getUsername() + " cu succes.");
        return REDIRECT_TO_ADMIN_USERS;
    }

    @GetMapping("/admin-edit-user")
    public String editUser(@RequestParam(name ="userId") String userId, Model model, RedirectAttributes redirectAttributes)
    {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent())
        {
            model.addAttribute("user", user.get());
            return "AdminUsersEdit";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Utilizatorul pe care ai incercat sa il modifici nu mai exista in baza de date deoarece a fost sters");

        return REDIRECT_TO_ADMIN_USERS;
    }

    @PostMapping("/admin-change-user-information")
    public String changeUserInformation(@RequestParam(name = "newFirstName", required = false) String newFirstName,
                                        @RequestParam(name = "newLastName", required = false) String newLastName,
                                        @RequestParam(name = "newEmail", required = false) String newEmail,
                                        @RequestParam(name = "newPhoneNumber", required = false) String newPhoneNumber,
                                        @RequestParam(name = "newImageFile", required = false) MultipartFile newImageFile,
                                        @RequestParam(name = "userId") String userId,
                                        RedirectAttributes redirectAttributes)
    {
        boolean hasErrors = false;

        Optional<User> temp = userRepository.findById(userId);

        if(!temp.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilizatorul pe care ai incercat sa il modifici nu mai exista deoarece a fost sters din baza de date.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        User user = temp.get();

        if(!newFirstName.isEmpty())
        {
            if(!lengthBetween(newFirstName, 2, 30)){
                hasErrors = true;
                redirectAttributes.addFlashAttribute("firstNameError", "FirstName trebuie sa contina minim 3 caractere si maxim 30");
            }
            user.setFirstName(newFirstName);
        }

        if(!newLastName.isEmpty())
        {
            if(!lengthBetween(newLastName, 2, 30)){
                hasErrors = true;
                redirectAttributes.addFlashAttribute("lastNameError", "FirstName trebuie sa contina minim 3 caractere si maxim 30");
            }
            user.setLastName(newLastName);
        }

        if(!newEmail.isEmpty())
        {
            if(!EmailValidator.getInstance().isValid(newEmail)){
                hasErrors = true;
                redirectAttributes.addFlashAttribute("emailError", "Email is not valid");
            }
            user.setEmail(newEmail);
        }

        if(!newPhoneNumber.isEmpty())
        {
            if(!isPhoneNumber(newPhoneNumber)) {
                hasErrors = true;
                redirectAttributes.addFlashAttribute("phoneNumberError", "Phone number is not valid");
            }
            user.setPhoneNumber(newPhoneNumber);
        }

        if(!newImageFile.isEmpty())
        {
            final String path = "/images/userAvatarImages/";
            final String jpg = ".jpg";

            if (newImageFile.getContentType().equals("image/gif") ||
                    newImageFile.getContentType().equals("image/jpeg") ||
                    newImageFile.getContentType().equals("image/png")) {

                saveAvatarImage(newImageFile, user);
                user.setAvatarImagePath(path + user.getId() + jpg);
            }
            else {
                hasErrors = true;
                redirectAttributes.addFlashAttribute("errorMessage", "Poți încărca doar imagini cu extensia .jpg/.jpeg, .png, .gif");
            }
        }

        if(hasErrors)
            return "redirect:/admin-edit-user/?userId=" + userId;

        userRepository.save(user);

        return REDIRECT_TO_ADMIN_USERS;
    }

    private static final String REDIRECT_TO_ADMIN_USERS = "redirect:/admin-users";
    private static final String SUCCESFUL_MESSAGE = "succesfulMessage";

    private boolean lengthBetween(String string, Integer left, Integer right)
    {
        if(left > right)
            return string.length() > right && string.length() < left;
        return string.length() > left && string.length() < right;
    }

    private boolean isPhoneNumber(String phoneNumber)
    {
        return phoneNumber.matches("[0-9]+") && (phoneNumber.length() > 8) && (phoneNumber.length() < 14);
    }

    private void saveAvatarImage(MultipartFile imageFile, User user) {
        final String currentDir = System.getProperty("user.dir");
        final String folder = currentDir + "/src/main/resources/static/images/userAvatarImages/";

        try {
            byte[] bytes = imageFile.getBytes();
            Path path = Paths.get(folder + user.getId() + ".jpg");
            Files.write(path, bytes);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
