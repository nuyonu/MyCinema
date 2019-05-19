package app.controller;

import app.controller.services.CommonFunctions;
import app.controller.services.ICookieService;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import app.database.service.UserService;
import app.database.utils.UserType;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminUsersController {
    private static final String REDIRECT_TO_ADMIN_USERS = "redirect:/admin-users";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String ERROR_MESSAGES = "errorMessages";

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private UserService userService;

    @Autowired
    private ICookieService cookieService;

    private List<String> errorMessages;
    private List<String> successfulMessages;

    private Logger logger = LoggerFactory.getLogger(AdminUsersController.class);

    @GetMapping("/admin-users")
    public String showUsers(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "username", required = false, defaultValue = "") String username,
                            Model model) {

        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return "error403";

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return "noAccess";

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("currentUsername", username);
        model.addAttribute("users", repositoryUser.findAllByUsernameContainingOrderByUsernameAsc(username));

        return "AdminUsers";
    }

    @PostMapping("/delete-selected-users")
    public String deleteSelectedUsers(@RequestParam(name = "usersForDelete", required = false) List<String> usersForDelete,
                                      RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (usersForDelete == null || usersForDelete.isEmpty()) {
            errorMessages.add("You can not delete if you have not selected anything");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_USERS;
        }

        deleteUsers(usersForDelete);

        if (usersForDelete.size() == 1) {
            if (!errorMessages.isEmpty())
                redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            else {
                successfulMessages.add("You have successfully removed the selected user");
                redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
            }
        } else {
            if (!errorMessages.isEmpty()) {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
                successfulMessages.add("Ai eliminat utilizatorii fara grad ADMIN cu succes.");
                redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
            } else {
                successfulMessages.add("You have successfully removed selected users");
                redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
            }
        }

        return REDIRECT_TO_ADMIN_USERS;
    }

    @GetMapping("/admin-edit-user")
    public String editUser(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam(name = "userId") String userId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        cookieService.setConfig(request, response);
        if (!cookieService.isConnected())
            return "error403";

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return "noAccess";

        errorMessages = new ArrayList<>();
        Optional<User> user = repositoryUser.findById(userId);

        if (user.isPresent()) {
            model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
            model.addAttribute("userForEdit", user.get());
            return "AdminUsersEdit";
        }

        redirectAttributes.addFlashAttribute(ERROR_MESSAGES, "The user you attempted to modify has disappeared from the database because it was deleted");

        return REDIRECT_TO_ADMIN_USERS;
    }

    @PostMapping("/admin-change-user-information")
    public String changeUserInformation(@RequestParam(name = "newFirstName", required = false) String newFirstName,
                                        @RequestParam(name = "newLastName", required = false) String newLastName,
                                        @RequestParam(name = "newEmail", required = false) String newEmail,
                                        @RequestParam(name = "newPhoneNumber", required = false) String newPhoneNumber,
                                        @RequestParam(name = "newImageFile", required = false) MultipartFile newImageFile,
                                        @RequestParam(name = "userId") String userId,
                                        RedirectAttributes redirectAttributes) {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        Optional<User> temp = repositoryUser.findById(userId);

        if (!temp.isPresent()) {
            errorMessages.add("The user you attempted to modify has disappeared since it was deleted from the database");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_USERS;
        }

        User user = temp.get();

        for (int index = 0; index < 20; index++) {
            repositoryUser.save(new User(
                    RandomStringUtils.random(10, true, true),
                    RandomStringUtils.random(10, true, true),
                    RandomStringUtils.random(10, true, true),
                    RandomStringUtils.random(10, true, true) + "@gmail.com",
                    RandomStringUtils.random(10, true, true)
            ));
        }

        user.setFirstName(verifyFirstName(newFirstName, user));
        user.setLastName(verifyLastName(newLastName, user));
        user.setEmail(verifyEmail(newEmail, user));
        user.setPhoneNumber(verifyPhoneNumber(newPhoneNumber, user));

        if (!newImageFile.isEmpty()) {
            if (newImageFile.getContentType().equals("image/gif") ||
                    newImageFile.getContentType().equals("image/jpeg") ||
                    newImageFile.getContentType().equals("image/png")) {

                userService.saveAvatarImage(newImageFile, user);
                user.setAvatarImagePath("/images/userAvatarImages/" + user.getId() + ".jpg");
            } else
                errorMessages.add("You can only upload images with the .jpg / .jpeg, .png, .gif extension");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return "redirect:/admin-edit-user/?userId=" + userId;
        }

        repositoryUser.save(user);

        successfulMessages.add("You have successfully changed user data: " + user.getUsername());
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_USERS;
    }

    private String verifyFirstName(String firstName, User user) {
        if (firstName.isEmpty())
            return user.getFirstName();
        else if (!CommonFunctions.lengthBetween(firstName, 2, 30)) {
            errorMessages.add("First Name must contain a minimum of 3 characters and a maximum of 30 characters");
            return user.getFirstName();
        }

        return firstName;
    }

    private String verifyLastName(String lastName, User user) {
        if (lastName.isEmpty())
            return user.getLastName();
        else if (!CommonFunctions.lengthBetween(lastName, 2, 30)) {
            errorMessages.add("Last Name must contain a minimum of 3 characters and a maximum of 30 characters");
            return user.getLastName();
        }
        return lastName;
    }

    private String verifyEmail(String email, User user) {
        if (email.isEmpty())
            return user.getEmail();
        else if (!EmailValidator.getInstance().isValid(email)) {
            errorMessages.add("Email is not valid");
            return user.getEmail();
        }
        return email;
    }

    private String verifyPhoneNumber(String phoneNumber, User user) {
        if (phoneNumber.isEmpty())
            return user.getPhoneNumber();
        else if (!CommonFunctions.isPhoneNumber(phoneNumber)) {
            errorMessages.add("Phone number is not valid");
            return user.getPhoneNumber();
        }
        return phoneNumber;
    }

    private void deleteUsers(List<String> usersForDelete) {
        for (String id : usersForDelete) {
            Optional<User> user = repositoryUser.findById(id);

            if (user.isPresent()) {
                if (user.get().getUserType() == UserType.ADMIN) {
                    errorMessages.add("You can not remove the user: " + user.get().getUsername() + " because it has the rank of ADMIN");
                } else {
                    repositoryUser.deleteById(id);
                    deleteUserAvatarProfile(user.get());
                }
            }
        }
    }

    private void deleteUserAvatarProfile(User user) {
        if (!user.getAvatarImagePath().equals("/images/userProfileDefaultAvatar/userProfileDefaultAvatar.jpg"))
            try {
                Files.deleteIfExists(Paths.get("src/main/resources/static/images/userAvatarImages/" + user.getId() + ".jpg"));
            } catch (IOException e) {
                String error = "Couldn't delete profile file: " + e;
                logger.error(error);
            }
    }
}
