package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.service.IRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Controller
public class AdminRoomsController
{
    @Autowired
    IRepositoryCinemaRoom repository;

    final private Path cinemaRoomsFolderPath = Paths.get("src/main/resources/static/images/cinema-rooms/");

    @GetMapping(value = "/admin-rooms")
    public String showRooms(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        model.addAttribute("cinemaRoom", new CinemaRoom());
        model.addAttribute("rooms", repository.findAll());
        return "AdminRooms";
    }

    @PostMapping(value = "/admin-delete-rooms")
    public String deleteRoom(@RequestParam(name = "room-ids", defaultValue = "") List<String> roomIds)
    {
        for (String id : roomIds)
        {
            if (repository.existsById(id))
            {
                repository.deleteById(id);
                deleteFolder(getRoomFolderById(id));
            }
        }

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/admin-add-room")
    public String addRoom(@Valid @ModelAttribute(value = "cinemaRoom") CinemaRoom room,
                          BindingResult bindingResult,
                          @RequestParam(name = "room-image", required = true) MultipartFile file)
    {
        if (bindingResult.hasErrors())
            return "redirect:/admin-rooms";

        CinemaRoom cinemaRoom = new CinemaRoom(room.getName());
        repository.save(cinemaRoom);

        if (!saveImage(file, cinemaRoomsFolderPath.resolve(cinemaRoom.getId()), "1.jpg"))
            repository.delete(cinemaRoom);

        return "redirect:/admin-rooms";
    }

    @GetMapping(value = "/admin-edit-room")
    public String toEditRoomPage(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(name = "id", required = true) String roomId,
                                 Model model)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            model.addAttribute("room", optionalRoom.get());
            model.addAttribute("images", getRoomFolderById(roomId).list());
            return "AdminRoomsEdit";
        }

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/admin-rooms-edit-submit")
    public String saveRoom(@RequestParam(name = "room-id") String roomId,
                           @RequestParam(name = "new-room-name") String newRoomName,
                           @RequestParam(name = "image-file", defaultValue = "") List<MultipartFile> newImages,
                           @RequestParam(name = "image-checked", defaultValue = "") List<String> checkedImages,
                           RedirectAttributes redirectAttributes)
    {
        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            CinemaRoom room = optionalRoom.get();

            if (!newRoomName.trim().isEmpty())
                room.setName(newRoomName);

            saveImagesInFolder(newImages, cinemaRoomsFolderPath.resolve(roomId));
            deleteCheckedImages(checkedImages, getRoomFolderById(roomId));

            repository.save(room);
        }

        redirectAttributes.addAttribute("id", roomId);
        return "redirect:/admin-edit-room";
    }

    @PostMapping(value = "/admin-rooms-edit-add-image")
    public String addImageToRoom(@RequestParam(name = "room-image") MultipartFile file,
                                 @RequestParam(name = "room-id") String roomId,
                                 RedirectAttributes redirectAttributes)
    {
        if (repository.existsById(roomId))
        {
            final int imageId = getRoomFolderById(roomId).listFiles().length + 1;
            saveImage(file, cinemaRoomsFolderPath.resolve(roomId),  imageId + ".jpg");
        }

        redirectAttributes.addAttribute("id", roomId);
        return "redirect:/admin-edit-room";
    }

    @RequestMapping(value = "/images/cinema-rooms/{roomId}/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String roomId, @PathVariable String imageId)
    {
        Path path = Paths.get("src/main/resources/static/images/cinema-rooms/" + roomId + "/" + imageId);

        try {
            byte[] data = Files.readAllBytes(path);
            return data;
        }
        catch (IOException e)
        {
            System.err.println(e);
        }

        return null;
    }

    private File getRoomFolderById(String roomId)
    {
        return cinemaRoomsFolderPath.resolve(roomId).toFile();
    }

    private void deleteCheckedImages(List<String> checkedImages, File imagesFolder)
    {
        File[] imagesInFolder = imagesFolder.listFiles();

        Arrays.stream(imagesInFolder)
                .filter(image -> checkedImages.contains(image.getName()))
                .forEach(image -> image.delete());

        imagesInFolder = Arrays.stream(imagesInFolder)
                .filter(image -> !checkedImages.contains(image.getName()))
                .toArray(File[]::new);

        for (int index = 0; index < imagesInFolder.length; ++index)
            renameFile(imagesInFolder[index], (index + 1) + ".jpg");
    }

    private void saveImagesInFolder(List<MultipartFile> images, Path folderPath)
    {
        for (int index = 0; index < images.size(); ++index)
        {
            MultipartFile image = images.get(index);

            if (!image.isEmpty())
                saveImage(image, folderPath, (index + 1) + ".jpg");
        }
    }

    private boolean saveImage(MultipartFile inputFile, Path saveLocation, String saveFilename)
    {
        String inputFilename = StringUtils.cleanPath(inputFile.getOriginalFilename());

        try
        {
            if (inputFile.isEmpty())
                throw new IOException("Failed to store empty file " + inputFilename);
            else if (inputFilename.contains(".."))
                throw new IOException("Cannot store file with relative path outside current directory " + inputFilename);

            try (InputStream inputStream = inputFile.getInputStream())
            {
                Files.createDirectories(saveLocation);
                Files.copy(inputStream, saveLocation.resolve(saveFilename), REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            System.err.println("Failed to store file " + inputFilename + ". Reason:\n" + e);
            return false;
        }

        return true;
    }

    private void renameFile(File file, String newName)
    {
        final Path filePath = file.toPath();

        try
        {
            Files.move(filePath, filePath.resolveSibling(newName), REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't rename a file: " + e);
        }
    }

    private void deleteFolder(File folder)
    {
        try
        {
            FileUtils.cleanDirectory(folder);
            folder.delete();
        }
        catch (IOException e)
        {
            System.err.println("Couldn't delete file: " + e);
        }
    }
}
