package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            for (String id : roomIds)
                repository.deleteById(id);

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/admin-add-room")
    public String addRoom(@RequestParam(name = "room-name", required = true) String roomName,
                          @RequestParam(name = "room-image-path", required = true) String roomImagePath)
    {
        if (!roomName.trim().isEmpty() && !roomImagePath.trim().isEmpty())
            repository.save(new CinemaRoom(roomName, roomImagePath));

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
                           @RequestParam(name = "new-room-image-path") String newRoomImagePath,
                           @RequestParam(name = "room-id") String roomId)
    {
        Optional<CinemaRoom> optionalRoom = repository.findById(roomId);

        if (optionalRoom.isPresent())
        {
            CinemaRoom room = optionalRoom.get();

            if (!newRoomName.trim().isEmpty())
                room.setName(newRoomName);

            if (!newRoomImagePath.trim().isEmpty())
                room.setImagePath(newRoomImagePath);

            repository.save(room);
        }

        return "redirect:/admin-rooms";
    }
}
