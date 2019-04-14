package App.Controller;

import App.Controller.ServiceController.CookieHandler;
import App.Database.Entities.Movie;
import App.Database.Service.IRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ProgramController {

    @Autowired
    IRepository serviceDatabase;

    @RequestMapping(value = "/program", method = RequestMethod.GET)
    public String program(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isCoonected()) return "redirect:/error403";
        Integer dayOfWeek = 0;
        List<Movie> list = serviceDatabase.getAllMovie();
        list.stream().forEach(e->e.setDay(dayOfWeek));
        model.addAttribute("listProgram", list);
        model.addAttribute("day", dayOfWeek);
        return "Program";
    }

    @RequestMapping(value = "program", method = RequestMethod.POST)
    public String programQuery(HttpServletRequest request, HttpServletResponse response, Model model) {
        CookieHandler cookieHandler = new CookieHandler(request, response);
        if (!cookieHandler.isCoonected()) return "redirect:/error403";
        System.out.println(request.getQueryString());
        Integer dayOfWeek = Integer.valueOf(request.getParameter("day"));
        System.out.println(dayOfWeek);
        List<Movie> list = serviceDatabase.getAllMovie();
        list.stream().forEach(e->e.setDay(dayOfWeek));
        model.addAttribute("listProgram", list);
        model.addAttribute("day", dayOfWeek);
        return "Program";
    }

}
