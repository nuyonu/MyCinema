package app.controller;

import app.controller.dao.ResetConn;
import app.database.entities.ResetAccount;
import app.database.entities.User;
import app.database.infrastructure.IRepositoryReset;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ResetPageController {
    private static final String RESET_PAGE = "ResetPage";
    private static final String REDIRECT = "redirect:/accountReset";

    @Autowired
    private IRepositoryReset repository;

    @Autowired
    private IRepositoryUser user;

    @GetMapping("/accountReset")
    public String reset(@RequestParam(name = "code") String code, Model model) {
        ResetAccount account = repository.findByCode(code);
        if (account == null) return "redirect:/ForgotPassword";
        ResetConn conn = new ResetConn();
        conn.setCode(code);
        model.addAttribute("reset", conn);
        return RESET_PAGE;
    }


    @PostMapping("/resetAccount")
    public String confirm(@Valid @ModelAttribute ResetConn conn, BindingResult bindingResult, RedirectAttributes atts) {
        if (bindingResult.hasErrors() || !conn.getPassword().equals(conn.getPasswordConfirm())) {
            return "redirect:/accountReset?code=" + conn.getCode();
        }
        ResetAccount account = repository.findByCode(conn.getCode());
        if (account == null) return REDIRECT;
        User userAccount = user.findByEmail(account.getEmail());
        userAccount.setPassword(conn.getPassword());
        user.save(userAccount);
        repository.deleteByCode(conn.getCode());
        return "redirect:/Login";

    }
}

