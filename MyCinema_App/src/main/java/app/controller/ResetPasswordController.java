package app.controller;

import app.controller.dao.AjaxResponseBody;
import app.controller.dao.Reset;
import app.controller.services.CaptchaService;
import app.controller.services.MailServer;
import app.controller.services.Message;
import app.database.entities.ResetAccount;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryReset;
import app.database.infrastructure.IRepositoryUser;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ResetPasswordController {

    @Autowired
    public JavaMailSender emailSender;

    @GetMapping("/ForgotPassword")
    public String reset(Model model) {
        model.addAttribute("Reset", new Reset());
        model.addAttribute("captcha",publicKey );
        return "ForgotPassword";
    }

    @PostMapping("/api/reset")
    @ResponseBody
    public ResponseEntity<AjaxResponseBody> getStatusReset(@RequestBody Reset user) {
        AjaxResponseBody result = new AjaxResponseBody();
        User userDatabase = repositoryUser.findByEmail(user.getEmail());
        if (userDatabase == null) {
            result.setMsg("400");
        } else {
            result.setMsg("200");
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping(value = "/reset")
    public String resetEmailWithCaptcha(@ModelAttribute Reset reset, @RequestParam(name = "g-recaptcha-response") String recaptchaResponse,
                                        HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        if (captchaService.processResponse(recaptchaResponse, ip, secretKey, url)) {
            User user1 = repositoryUser.findByEmail(reset.getEmail());
            if (user1 != null) {
                String code = generator.generate(200);
                ResetAccount resetAccount = new ResetAccount(reset.getEmail(), code);
                if (repositoryReset.findByEmail(resetAccount.getEmail()) == null) repositoryReset.save(resetAccount);
                else {
                    repositoryReset.deleteByEmail(resetAccount.getEmail());
                    repositoryReset.save(resetAccount);
                }
                mailServer.setEmailSender(emailSender);
                mailServer.sendSimpleMessage(reset.getEmail(), "Reset account", new Message(code).toString());
                return "redirect:/Login";
            }
        }
        return "redirect:/ForgotPassword";
    }


    @Autowired
    private IRepositoryUser repositoryUser;

    @Value("${google.recaptcha.key.secret}")
    private String secretKey;
    @Value("${google.recaptcha.key.public}")
    private String publicKey;
    @Value("${google.rechatcha.url}")
    private String url;

    @Autowired
    private IRepositoryReset repositoryReset;
    private MailServer mailServer = new MailServer();
    private CaptchaService captchaService = new CaptchaService();
    private RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.LETTERS)
            .build();

}
