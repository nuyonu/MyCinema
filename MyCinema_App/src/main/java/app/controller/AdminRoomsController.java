package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminRoomsController
{
    @Autowired
    IRepositoryCinemaRoom repository;

    @GetMapping(value = "/admin-rooms")
    public String showRooms(HttpServletRequest request, HttpServletResponse response, Model model)
    {
        CookieHandler cookieHandler = new CookieHandler(request, response);

        if (!cookieHandler.isConnected())
            return "error403";

        model.addAttribute("rooms", repository.findAll());
        return "AdminRooms";
    }

    @PostMapping(value = "/admin-delete-rooms")
    public String deleteRoom(@RequestParam(name = "room-ids", required = false) List<String> roomIds)
    {
        if (roomIds != null)
        {
            for (String id : roomIds)
            {
                Optional<CinemaRoom> optionalRoom = repository.findById(id);

                if (optionalRoom.isPresent())
                {
                    repository.deleteById(id);

                    try
                    {
                        Files.deleteIfExists(Paths.get("src/main/resources/static/images/cinema-rooms/" + optionalRoom.get().getId() + ".jpg"));
                    }
                    catch (IOException e)
                    {
                        System.err.println("Couldn't delete file: " + e);
                    }
                }
            }
        }

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/admin-add-room")
    public String addRoom(@RequestParam(name = "room-name", required = true) String roomName,
                          @RequestParam(name = "room-image", required = true) MultipartFile file)
    {
        if (roomName.trim().isEmpty())
            return "redirect:/admin-rooms";

        CinemaRoom cinemaRoom = new CinemaRoom(roomName);
        repository.save(cinemaRoom);

        if (!saveImage(file, "src/main/resources/static/images/cinema-rooms", cinemaRoom.getId() + ".jpg"))
            repository.delete(cinemaRoom);

        return "redirect:/admin-rooms";
    }

    @GetMapping(value = "/admin-edit-room")
    public String toEditRoomPage(@RequestParam(name = "id", required = true) String roomId, Model model)
    {
        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            model.addAttribute("room", optionalRoom.get());
            return "AdminRoomsEdit";
        }

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/admin-rooms-submit-edit")
    public String saveRoom(@RequestParam(name = "new-room-name") String newRoomName,
                           @RequestParam(name = "new-room-image") MultipartFile file,
                           @RequestParam(name = "room-id") String roomId)
    {
        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            CinemaRoom room = optionalRoom.get();

            if (!newRoomName.trim().isEmpty())
                room.setName(newRoomName);

            if (!file.isEmpty())
                saveImage(file, "src/main/resources/static/images/cinema-rooms", roomId + ".jpg");

            repository.save(room);
        }

        return "redirect:/admin-rooms";
    }

    @RequestMapping(value = "/images/cinema-rooms/{imageId}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageId, HttpServletRequest request)
    {
        Path path = Paths.get("src/main/resources/static/images/cinema-rooms/" + imageId);

        try {
            byte[] data = Files.readAllBytes(path);
            return data;
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        return null;
    }

    private boolean saveImage(MultipartFile inputFile, String saveLocation, String saveFilename)
    {
        String inputFilename = StringUtils.cleanPath(inputFile.getOriginalFilename());
        Path path = Paths.get(saveLocation);

        try
        {
            if (inputFile.isEmpty())
                throw new IOException("Failed to store empty file " + inputFilename);
            else if (inputFilename.contains(".."))
                throw new IOException("Cannot store file with relative path outside current directory " + inputFilename);

            try (InputStream inputStream = inputFile.getInputStream())
            {
                Files.copy(inputStream, path.resolve(saveFilename), StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            System.err.println("Failed to store file " + inputFilename + ". Reason:\n" + e);
            return false;
        }

        return true;
    }
}
