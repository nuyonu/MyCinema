package app.controller;

import app.controller.services.CookieHandler;
import app.database.entities.CinemaRoom;
import app.database.entities.Movie;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PostMapping(value = "/delete-rooms")
    public String deleteRoom(@RequestParam(name = "room-ids", required = false) List<String> roomIds)
    {
        if (roomIds != null)
            for (String id : roomIds)
                repository.deleteById(id);

        return "redirect:/admin-rooms";
    }

    @PostMapping(value = "/add-room")
    public String deleteRoom(@RequestParam(name = "room-name", required = true) String roomName,
                             @RequestParam(name = "room-image-path", required = true) String roomImagePath)
    {
        if (roomName != null && roomImagePath != null)
            repository.save(new CinemaRoom(roomName, roomImagePath));

        return "redirect:/admin-rooms";
    }
}
