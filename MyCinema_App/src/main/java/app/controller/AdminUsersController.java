package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import app.database.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdminUsersController {
    @Autowired
    private IRepositoryUser userRepository;

    @Autowired
    private UserService userService;

    private static final String REDIRECT_TO_ADMIN_USERS = "redirect:/admin-users";
    private static final String SUCCESSFUL_MESSAGE = "successfulMessage";
    private static final String ERROR_MESSAGE = "errorMessage";
    private List<String> errorMessages = new ArrayList<>();

    private Logger logger = LoggerFactory.getLogger(AdminUsersController.class);

    @GetMapping("/admin-users")
    public String showUsers(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(value = "page") Optional<Integer> page,
                            @RequestParam(value = "size") Optional<Integer> size,
                            @RequestParam(value = "username", required = false, defaultValue = "") String username,
                            Model model) {

        if (!new CookieHandler(request, response).isConnected())
            return "error403";

        int currentPage = page.orElse(1);
        int pageSize = clamp(size.orElse(25), 25, 100);

        currentPage = (currentPage < 1) ? 1 : currentPage;

        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), username);
        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("currentUsername", username);

        return "AdminUsers";
    }

    @PostMapping("/delete-selected-users")
    public String deleteSelectedUsers(@RequestParam(name = "usersForDelete", required = false) List<String> usersForDelete,
                                      RedirectAttributes redirectAttributes) {
        if (usersForDelete == null || usersForDelete.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Nu poti sterge daca nu ai selectat nimic");
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
                        String error = "Couldn't delete profile file: " + e;
                        logger.error(error);
                    }
            }
        }

        if (usersForDelete.size() == 1)
            redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGE, "Ai eliminat utilizatorul selectat cu succes");
        else
            redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGE, "Ai eliminat utilizatorii selectati cu succes.");

        return REDIRECT_TO_ADMIN_USERS;
    }

    @PostMapping("/delete-by-username")
    public String deleteByUsername(@RequestParam(name = "usernameForDelete") String username, RedirectAttributes redirectAttributes) {
        if (username.isEmpty()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Nu ai introdus nici un nume de utilizator.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Utilizatorul pe care ai incercat sa il elimini nu exista.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        userRepository.delete(user);
        if (!user.getAvatarImagePath().equals("/images/userProfileDefaultAvatar/userProfileDefaultAvatar.jpg"))
            try {
                Files.deleteIfExists(Paths.get("src/main/resources/static/images/userAvatarImages/" + user.getId() + ".jpg"));
            } catch (IOException e) {
                String error = "Couldn't delete profile file: " + e;
                logger.error(error);
            }

        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGE, "Ai eliminat utilizatorul " + user.getUsername() + " cu succes.");
        return REDIRECT_TO_ADMIN_USERS;
    }

    @GetMapping("/admin-edit-user")
    public String editUser(@RequestParam(name = "userId") String userId, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "AdminUsersEdit";
        }

        redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Utilizatorul pe care ai incercat sa il modifici nu mai exista in baza de date deoarece a fost sters");

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

        Optional<User> temp = userRepository.findById(userId);

        if (!temp.isPresent()) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE, "Utilizatorul pe care ai incercat sa il modifici nu mai exista deoarece a fost sters din baza de date.");
            return REDIRECT_TO_ADMIN_USERS;
        }

        User user = temp.get();

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
                errorMessages.add("Poți încărca doar imagini cu extensia .jpg/.jpeg, .png, .gif");
        }

        if (!errorMessages.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return "redirect:/admin-edit-user/?userId=" + userId;
        }

        userRepository.save(user);

        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGE, "Ai modificat cu succes datele utilizatorului: " + user.getUsername());

        return REDIRECT_TO_ADMIN_USERS;
    }

    private boolean lengthBetween(String string, Integer left, Integer right) {
        if (left > right)
            return string.length() > right && string.length() < left;
        return string.length() > left && string.length() < right;
    }

    private boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+") && (phoneNumber.length() > 8) && (phoneNumber.length() < 14);
    }

    private <T extends Comparable<T>> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    private String verifyFirstName(String firstName, User user) {
        if (!firstName.isEmpty() && !lengthBetween(firstName, 2, 30)) {
            errorMessages.add("FirstName trebuie sa contina minim 3 caractere si maxim 30");
            return user.getFirstName();
        }
        return firstName;
    }

    private String verifyLastName(String lastName, User user) {
        if (!lastName.isEmpty() && !lengthBetween(lastName, 2, 30)) {
            errorMessages.add("LastName trebuie sa contina minim 3 caractere si maxim 30");
            return user.getLastName();
        }
        return lastName;
    }

    private String verifyEmail(String email, User user) {
        if (!email.isEmpty() && !EmailValidator.getInstance().isValid(email)) {
            errorMessages.add("Email is not valid");
            return user.getEmail();
        }
        return email;
    }

    private String verifyPhoneNumber(String phoneNumber, User user) {
        if (!phoneNumber.isEmpty() && !isPhoneNumber(phoneNumber)) {
            errorMessages.add("Phone number is not valid");
            return user.getPhoneNumber();
        }
        return phoneNumber;
    }
}
