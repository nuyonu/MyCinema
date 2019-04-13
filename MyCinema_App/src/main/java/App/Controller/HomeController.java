package App.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController
{
    @GetMapping("/Home")
    public String start(Model model)
    {
        return "Home";
    }

    @GetMapping("/Statistics")
    public String statistics(Model model)
    {
        return "Statistics";
    }

    @GetMapping("/Movies")
    public String movies(Model model)
    {
        return "Movies";
    }

    @GetMapping("/Cinema-rooms")
    public String cinemaRooms(Model model)
    {
        return "Cinema-rooms";
    }

    @GetMapping("/Program")
    public String program(Model model)
    {
        return "Program";
    }

    @GetMapping("/Settings")
    public String settings(Model model)
    {
        return "Settings";
    }
}