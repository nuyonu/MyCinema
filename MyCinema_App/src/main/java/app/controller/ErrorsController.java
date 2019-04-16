package app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Profile("prod")
public class ErrorsController implements ErrorController {


    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error403";
            }

        }
        return "error";
    }

    @GetMapping(value = "/error500")
    public String handleError500() {
        return "error500";

    }

    @GetMapping(value = "/error404")
    public String handleError404() {
        return "error404";

    }

    @GetMapping(value = "/error403")
    public String handleError() {
        return "error403";

    }
}
