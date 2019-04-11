package App.Contoller;

import App.Contoller.Dao.SelectedRoom;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("SameReturnValue")
@Controller
public class AllRoomsController {


    @Autowired
    IRepository repository;

    @RequestMapping(value = "/allRooms",method = RequestMethod.GET)
    public String allRoom(Model model) {
        model.addAttribute("SelectedRoom", new SelectedRoom());
        return "Cinema-rooms";
    }


    @RequestMapping(value = "/getRoom",method = RequestMethod.POST)
    public  String getRoom() {
        return "Room";
    }
}
