package smtp.server.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Domain;
import smtp.server.admin.services.IAccountService;
import smtp.server.admin.services.IDomainService;

import java.util.List;

@Controller

public class AccountController {
    @Autowired
    private IDomainService domainService;
    @Autowired
    private IAccountService accountService;
    private final String homePageUrl = "/";
    private final String exists = "Account with a specified username already exists.";
    private final String notFound = "Account not found";


    @GetMapping("/account")
    public String editAccount(@RequestParam Long id, Model model) {
        try {
            Account account = accountService.findAccountById(id);

            if (account == null) {
                model.addAttribute("message", notFound);
                model.addAttribute("url", homePageUrl);
                return "error";
            }
            model.addAttribute("account", account);
            List<Domain> domains = domainService.findAll();
            model.addAttribute("domains", domains);
            return "account_edit";
        } catch (NullPointerException | IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/account")
    public String addAccount(@ModelAttribute("account") Account newAccount, Model model) {
        newAccount.setId(null);
        List<Account> accounts = accountService.findAll();
        try {
            Domain domain = newAccount.getDomain();
            Long domainId = domain.getId();
            String url = "/domain?id=" + domainId;
            if (!domainService.existsById(domainId)) {
                model.addAttribute("message", "Invalid domain");
                url = homePageUrl;
                model.addAttribute("url", url);
                return "error";
            }
            Domain domainFromRepo = domainService.findDomainById(domain.getId());
            if (!domain.getName().equals(domainFromRepo.getName()))
                return "error";
            for (Account account : accounts)
                if (account.getUsername().equals(newAccount.getUsername())) {
                    model.addAttribute("message", exists);
                    model.addAttribute("url", url);
                    return "error";
                }
            accountService.save(newAccount);
            return "redirect:" + url;
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/account/update")
    public String updateAccount(@ModelAttribute("account") Account updatedAccount, @ModelAttribute("domainName") String domainName, Model model) {
        try {
            Domain domain = domainService.findDomainByName(domainName);
            updatedAccount.setDomain(domain);
            List<Account> accounts = accountService.findAccountsByDomainId(domain.getId());
            String url = homePageUrl;
            for (Account account : accounts)
                if (!account.getId().equals(updatedAccount.getId()) && account.getUsername().equals(updatedAccount.getUsername())) {
                    model.addAttribute("message", exists);
                    model.addAttribute("url", url);
                    return "error";
                }
            Account oldAccount = accountService.findAccountById(updatedAccount.getId());
            if (updatedAccount.getPassword().equals(""))
                updatedAccount.setPassword(oldAccount.getPassword());
            else
                updatedAccount.setPassword(new BCryptPasswordEncoder().encode(updatedAccount.getPassword()));
            accountService.save(updatedAccount);
            url = "/domain?id=" + domain.getId();
            return "redirect:" + url;
        } catch (NullPointerException | IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/account/delete")
    public String deleteAccount(@RequestParam Long id, Model model) {
        try {
            Account account = accountService.findAccountById(id);

            if (account == null) {
                model.addAttribute("message", notFound);
                model.addAttribute("url", "/accounts");
                return "error";
            }
            String url = "/domains?id=" + account.getDomain().getId();
            accountService.delete(account);
            return "redirect:" + url;
        } catch (NullPointerException | IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

}

