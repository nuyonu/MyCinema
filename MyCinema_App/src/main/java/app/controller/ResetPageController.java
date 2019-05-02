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

import javax.validation.Valid;

@Controller
public class ResetPageController {
    @GetMapping("/accountReset")
    public  String reset(@RequestParam(name = "code") String code,Model model)
    {
        ResetAccount account=repository.findByCode(code);
        if(account==null) return "redirect:/ForgotPassword";
        model.addAttribute("reset",new ResetConn());
        return RESET_PAGE;
    }



    @PostMapping("/accountReset")
    public String confirm(@Valid @ModelAttribute("conf") ResetConn conn, @RequestParam(name = "code", defaultValue = "0") String code, BindingResult bindingResult){
        if(bindingResult.hasErrors() ||!conn.getPassword().equals(conn.getPasswordConfirm())) return  RESET_PAGE;
        ResetAccount account=repository.findByCode(code);
        if(account==null) return RESET_PAGE;
        User userAccount=user.findByEmail(account.getEmail());
        userAccount.setPassword(conn.getPassword());
        user.save(userAccount);
        repository.deleteByCode(code);
        return  "Login";

    }

    @Autowired
    private IRepositoryReset repository;

    @Autowired
    private IRepositoryUser user;

    private static final  String RESET_PAGE="ResetPage";
}

