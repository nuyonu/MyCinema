package app.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MultipartException.class)
    public String handleErrorMaxUploadSizeExceededException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Poți încărca doar imagini mai mici sau egale cu 5MB.");
        return "redirect:/settings";
    }
}
