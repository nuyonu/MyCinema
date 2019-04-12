package App.Controller;

import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RoomController {

    @Autowired
    IRepository repository;

    @RequestMapping( value = "{id}/Room",method = RequestMethod.GET)
    public String room(@PathVariable("id") String id, Model model)
    {
        model.addAttribute("Room" ,repository.findRoomByName(id));
        return "Room";
    }


}
