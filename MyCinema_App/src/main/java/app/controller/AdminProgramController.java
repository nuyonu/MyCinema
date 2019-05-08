package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.ScreeningHours;
import app.database.infrastructure.IRepositoryCinemaRoom;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryScreeningHours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminProgramController {

    @Autowired
    private ICookieService cookieService;

    @Autowired
    private IRepositoryMovie movieRepository;

    @Autowired
    private IRepositoryCinemaRoom cinemaRoomRepository;

    @Autowired
    private IRepositoryScreeningHours screeningHoursRepository;

    private static final String ERROR_MESSAGES = "errorMessages";
    private static final String SUCCESSFUL_MESSAGES = "successfulMessages";
    private static final String REDIRECT_TO_ADMIN_PROGRAM = "redirect:/admin-program";

    @GetMapping(value = "/admin-program")
    public String showProgram(HttpServletRequest request,
                              HttpServletResponse response,
                              Model model) {
        cookieService.setConfig(request, response);

        if (!cookieService.isConnected())
            return "error403";

        model.addAttribute("movies", movieRepository.findAll());
        model.addAttribute("rooms", cinemaRoomRepository.findAll());
        model.addAttribute("screeningHours", screeningHoursRepository.findAll());

        return "AdminProgram";
    }

    @PostMapping(value = "/admin-add-program")
    public String addMovieInProgram(@RequestParam(name = "movie-id") String movieId,
                                    @RequestParam(name = "room-id") String roomId,
                                    @RequestParam(name = "date") String date,
                                    @RequestParam(name = "time") String time,
                                    RedirectAttributes redirectAttributes) throws ParseException {

        ScreeningHours screeningHours = new ScreeningHours();

        List<String> errorMessages;
        List<String> successfulMessages;

        errorMessages = new ArrayList<>();
        successfulMessages = new ArrayList<>();

        if(movieId.equals("empty"))
        {
            errorMessages.add("Trebuie sa selectezi un film.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if(roomId.equals("empty"))
        {
            errorMessages.add("Trebuie sa selectezi o camera.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if(date.isEmpty())
        {
            errorMessages.add("Trebuie sa selectezi o data.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        if(time.isEmpty())
        {
            errorMessages.add("Trebuie sa selectezi o ora.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        screeningHours.setMovieId(movieId);
        screeningHours.setRoomId(roomId);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date startTime = sdf.parse(currentTime);
        Date endTime = sdf.parse(time);
        long timeElapsed = endTime.getTime() - startTime.getTime();
        timeElapsed/=60000; //minute din milisecunde

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate=sdf.parse(currentDate);
        Date endDate=sdf.parse(date);
        long daysElapsed = endDate.getTime() - startDate.getTime();
        daysElapsed/=86400000; //zile din milisecunde

        boolean canSave= true;

        /*if(daysElapsed<0)
            canSave=false;
        else if(daysElapsed==0)
        {
            if(timeElapsed-59>0)
                canSave=true;
            else
                canSave=false;
        }
        else
        {
            if(daysElapsed==1)
                if(timeElapsed+24*60-59<0)
                    canSave=false;
                else
                    canSave=true;
            else
                canSave=true;
        }*/

        if(daysElapsed<0 || daysElapsed==0&&(timeElapsed-59<=0) || daysElapsed==1&&(timeElapsed+24*60-59<0))
            canSave=false;

        screeningHours.setDate(date);
        screeningHours.setTime(time);

        if(!canSave)
        {
            //o-oh... ceva s-a intamplat... ora e de vina ;*

            errorMessages.add("Poti pune filme cu o ora in plus fata de ora curenta.");
            redirectAttributes.addFlashAttribute(ERROR_MESSAGES, errorMessages);

            return REDIRECT_TO_ADMIN_PROGRAM;
        }

        successfulMessages.add("Ai adaugat un film pe data de " + date + " la ora " + time + " cu succes.");
        redirectAttributes.addFlashAttribute(SUCCESSFUL_MESSAGES, successfulMessages);
        screeningHoursRepository.save(screeningHours);

        return REDIRECT_TO_ADMIN_PROGRAM;
    }

}
