package smtp.server.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import smtp.server.admin.security.UserPrincipal;
import smtp.server.admin.services.IAccountService;
import smtp.server.admin.services.IDomainService;

@Controller
public class LoginController {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IDomainService domainService;

    @GetMapping("/login")
    public String showForm(Model model) {
        return "login_form";
    }

    @GetMapping("/login/error")
    public String error(Model model) {
        model.addAttribute("loginError", true);
        return "login_form";
    }

    @GetMapping("/")
    public String success(Model model) {
//    UserPrincipal userPrincipal =(UserPrincipal)principal;
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username", principal.getUsername());
        return "index";
    }

}