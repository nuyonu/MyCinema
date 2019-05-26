package app.controller;

import app.controller.services.ICookieService;
import app.database.entities.Movie;
import app.database.entities.Reservation;
import app.database.entities.ScreeningHours;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryMovie;
import app.database.infrastructure.IRepositoryReservation;
import app.database.infrastructure.IRepositoryScreeningHours;
import app.database.infrastructure.IRepositoryUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
class Notification {
    private String movieTitle;
    private String moviePath;
    private String time;
    private String date;
}

@RestController
public class NotificationController {

    @Autowired
    private ICookieService iCookieService;

    @Autowired
    private IRepositoryMovie repositoryMovie;

    @Autowired
    private IRepositoryUser repositoryUser;

    @Autowired
    private IRepositoryScreeningHours repositoryScreeningHours;

    @Autowired
    private IRepositoryReservation repositoryReservation;

    private Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @GetMapping("api/notifications")
    @ResponseBody
    public ResponseEntity<List<Notification>> showNotifications() {

        List<Notification> notifications = new ArrayList<>();

        User user = repositoryUser.findByUsername(iCookieService.getUser());
        if (user == null)
            return ResponseEntity.notFound().build();
        List<Reservation> reservations = repositoryReservation.findAllByUserId(new ObjectId(user.getId()));
        ScreeningHours screeningHours;
        for (Reservation reservation : reservations) {
            Optional<ScreeningHours> optionalScreeningHours = repositoryScreeningHours.findById(reservation.getScreeningId().toString());
            if (optionalScreeningHours.isPresent()) {
                screeningHours = optionalScreeningHours.get();
                Optional<Movie> movieOptional = repositoryMovie.findById(screeningHours.getMovieId().toString());
                if (movieOptional.isPresent()) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(screeningHours.getDate());
                        notifications.add(new Notification(movieOptional.get().getTitle(),
                                movieOptional.get().getPath(),
                                screeningHours.getTime(),
                                new SimpleDateFormat("dd MMMM yyyy").format(date)));
                    } catch (ParseException e) {
                        String error = "Error to parse date" + e;
                        logger.error(error);
                    }
                }
            }
        }

        return ResponseEntity.ok().body(notifications);
    }
}
