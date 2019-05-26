package app.controller;

import app.controller.services.CommonFunctions;
import app.controller.services.ICookieService;
import app.database.entities.CinemaRoom;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.infrastructure.IRepositoryUser;
import app.database.utils.UserType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
public class AdminRoomsController {

    private static final String REDIRECT_TO_ADMIN_ROOMS = "redirect:/admin-rooms";
    private static final String REDIRECT_TO_LOGIN = "redirect:/Login";
    private static final String REDIRECT_TO_ADMIN_EDIT_ROOM = "redirect:/admin-edit-room";
    private static final String REDIRECT_TO_ADMIN_ADD_ROOM = "redirect:/admin-add-room";
    private static final String REDIRECT_TO_HOME = "redirect:/home";
    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String PATH_DELIMITER = "/";

    @Autowired
    private IRepositoryCinemaRoom repository;

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private ICookieService cookieService;

    private final Path cinemaRoomsFolderPath = Paths.get("src/main/resources/static/images/cinema-rooms/");
    private final Logger logger = LoggerFactory.getLogger(AdminRoomsController.class);

    private List<String> errorMessages;
    private List<String> successfulMessages;

    @GetMapping(value = "/admin-rooms")
    public String showRooms(@RequestParam(value = "roomName", required = false, defaultValue = "") String roomName,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return REDIRECT_TO_HOME;

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));
        model.addAttribute("cinemaRoom", new CinemaRoom());
        model.addAttribute("rooms", repository.findAllByNameContainingOrderByNameAsc(roomName));
        model.addAttribute("currentRoomName", roomName);
        return "AdminRooms";
    }

    @PostMapping(value = "/admin-delete-rooms")
    public String deleteRoom(@RequestParam(name = "room-ids", defaultValue = "") List<String> roomIds) {
        for (String id : roomIds) {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                deleteFolder(getRoomFolderById(id));
            }
        }

        return REDIRECT_TO_ADMIN_ROOMS;
    }

    @GetMapping(value = "/admin-add-room")
    public String adminAddRoom(HttpServletRequest request,
                               HttpServletResponse response,
                               Model model)
    {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return REDIRECT_TO_HOME;

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        return "AdminRoomsAdd";
    }

    @PostMapping(value = "/admin-add-room-submit")
    public String addRoom(@RequestParam(name = "room-title", required = true) String title,
                          @RequestParam(name = "room-image-1", required = true) MultipartFile file1,
                          @RequestParam(name = "room-image-2", required = true) MultipartFile file2,
                          @RequestParam(name = "room-image-3", required = true) MultipartFile file3,
                          @RequestParam(name = "room-image-4", required = true) MultipartFile file4,
                          RedirectAttributes redirectAttributes)
    {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (!CommonFunctions.lengthBetween(title, 2, 30))
        {
            errorMessages.add("The name you entered is not valid. It's length must be between 2 and 30 characters long");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_ADD_ROOM;
        }

        if (repository.findByName(title) != null)
        {
            errorMessages.add("There already exists a room which has the name '" + title + "'");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_ADD_ROOM;
        }

        int rows = 14;
        int columns = 13;

        List<List<Integer>> seats = new ArrayList<>(Collections.nCopies(rows, Collections.nCopies(columns, 0)));

        CinemaRoom cinemaRoom = new CinemaRoom(title, seats);
        repository.save(cinemaRoom);

        final Path cinemaRoomFolder = cinemaRoomsFolderPath.resolve(cinemaRoom.getId());

        if (!saveImage(file1, cinemaRoomFolder, "1.jpg") ||
                !saveImage(file2, cinemaRoomFolder, "2.jpg") ||
                !saveImage(file3, cinemaRoomFolder, "3.jpg") ||
                !saveImage(file4, cinemaRoomFolder, "4.jpg"))
        {
            errorMessages.add("The image you entered is not valid. You can store only .jpg/.jpeg, .png, .gif files.");
        }

        if (!errorMessages.isEmpty())
        {
            repository.delete(cinemaRoom);
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            return REDIRECT_TO_ADMIN_ADD_ROOM;
        }

        successfulMessages.add("You have successfully added the room.");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);

        return REDIRECT_TO_ADMIN_ROOMS;
    }

    @GetMapping(value = "/admin-edit-room")
    public String toEditRoomPage(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(name = "id", required = true) String roomId,
                                 Model model)
    {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return REDIRECT_TO_LOGIN;

        if (!repositoryUser.findByUsername(cookieService.getUser()).getUserType().equals(UserType.ADMIN))
            return REDIRECT_TO_HOME;

        model.addAttribute("user", repositoryUser.findByUsername(cookieService.getUser()));

        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent()) {
            model.addAttribute("room", optionalRoom.get());
            model.addAttribute("images", getRoomFolderById(roomId).list());
            return "AdminRoomsEdit";
        }

        return REDIRECT_TO_ADMIN_ROOMS;
    }

    @PostMapping(value = "/admin-rooms-edit-submit")
    public String saveRoom(@RequestParam(name = "room-id") String roomId,
                           @RequestParam(name = "new-room-name") String newRoomName,
                           @RequestParam(name = "image-file", defaultValue = "") List<MultipartFile> newImages,
                           RedirectAttributes redirectAttributes)
    {
        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if (!newRoomName.isEmpty() && !CommonFunctions.lengthBetween(newRoomName, 2, 30))
        {
            errorMessages.add("The name you entered is not valid. It's length must be between 2 and 30 characters long");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            redirectAttributes.addAttribute("id", roomId);
            return REDIRECT_TO_ADMIN_EDIT_ROOM;
        }

        if (repository.findByName(newRoomName) != null)
        {
            errorMessages.add("There already exists a room which has the name '" + newRoomName + "'");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
            redirectAttributes.addAttribute("id", roomId);
            return REDIRECT_TO_ADMIN_EDIT_ROOM;
        }

        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            CinemaRoom room = optionalRoom.get();

            if (!saveImagesInFolder(newImages, cinemaRoomsFolderPath.resolve(roomId)))
            {
                errorMessages.add("The image you entered is not valid. You can store only .jpg/.jpeg, .png, .gif files");
                redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);
                redirectAttributes.addAttribute("id", roomId);
                return REDIRECT_TO_ADMIN_EDIT_ROOM;
            }
            else
            {
                if (!newRoomName.isEmpty())
                    room.setName(newRoomName);

                repository.save(room);
            }
        }

        successfulMessages.add("You have successfully changed the room");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
        return REDIRECT_TO_ADMIN_ROOMS;
    }

    @PostMapping(value = "/admin-rooms-edit-add-image")
    public String addImageToRoom(@RequestParam(name = "room-image") MultipartFile file,
                                 @RequestParam(name = "room-id") String roomId,
                                 RedirectAttributes redirectAttributes) {
        if (repository.existsById(roomId)) {
            final int imageId = getRoomFolderById(roomId).listFiles().length + 1;
            saveImage(file, cinemaRoomsFolderPath.resolve(roomId), imageId + ".jpg");
        }

        redirectAttributes.addAttribute("id", roomId);
        return REDIRECT_TO_ADMIN_EDIT_ROOM;
    }

    @GetMapping(value = "/images/cinema-rooms/{roomId}/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String roomId, @PathVariable String imageId) {
        if (!imageId.matches("[a-zA-Z0-9.]++"))
            return new byte[0];
        else {
            Path path = Paths.get("src/main/resources/static/images/cinema-rooms/" + roomId + PATH_DELIMITER + imageId);

            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                logger.error(e.toString());
            }

            return new byte[0];
        }
    }

    private File getRoomFolderById(String roomId) {
        return cinemaRoomsFolderPath.resolve(roomId).toFile();
    }

    private boolean saveImagesInFolder(List<MultipartFile> images, Path folderPath) {
        for (int index = 0; index < images.size(); ++index) {
            MultipartFile image = images.get(index);

            if (!image.isEmpty() && !saveImage(image, folderPath, (index + 1) + ".jpg"))
                    return false;
        }

        return true;
    }

    private boolean saveImage(MultipartFile inputFile, Path saveLocation, String saveFilename) {
        String inputFilename = StringUtils.cleanPath(inputFile.getOriginalFilename());

        try {
            if (inputFile.isEmpty())
                throw new IOException("Failed to store empty file " + inputFilename);
            else if (inputFilename.contains(".."))
                throw new IOException("Cannot store file with relative path outside current directory " + inputFilename);

            if (!(inputFile.getContentType().equals("image/gif") ||
                    inputFile.getContentType().equals("image/jpeg") ||
                    inputFile.getContentType().equals("image/png")))
            {
                throw new IOException("You can store only .jpg/.jpeg, .png, .gif files.");
            }

            try (InputStream inputStream = inputFile.getInputStream())
            {
                Files.createDirectories(saveLocation);
                Files.copy(inputStream, saveLocation.resolve(saveFilename), REPLACE_EXISTING);
            }
        } catch (IOException e) {
            String error = "Failed to store file " + inputFilename + ". Reason:\n" + e;
            logger.error(error);
            return false;
        }

        return true;
    }

    private void deleteFolder(File folder) {
        try {
            FileUtils.cleanDirectory(folder);
            Files.deleteIfExists(folder.toPath());
        } catch (IOException e) {
            String error = "Couldn't delete file: " + e;
            logger.error(error);
        }
    }
}