package App.Controller;

import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RoomController {

    @Autowired
    IRepository repository;

    @RequestMapping(value = "reservation")
    public String room(HttpServletRequest request, HttpServletResponse response, Model model)
    {
//        model.addAttribute("Room" ,repository.findRoomByName(i);
        return "Room";
    }


}
