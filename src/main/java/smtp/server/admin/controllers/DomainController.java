package smtp.server.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
public class DomainController {

    @Autowired
    private IDomainService domainService;
    @Autowired
    private IAccountService accountService;
    private final String domainsUrl = "/domains";
    private final String notFound = "Domain not found.";
    private final String exists = "Domain with this name already exists.";

    @GetMapping("/domains")
    public String findDomains(Model model) {
        var domains = (List<Domain>) domainService.findAll();

        model.addAttribute("domains", domains);
        model.addAttribute("newDomain", new Domain(""));
        return "domains";
    }

    @GetMapping("/domain")
    public String viewDomain(@RequestParam Long id, Model model) {
        try {
            Domain domain = domainService.findDomainById(id);
            if (domain == null) {
                model.addAttribute("message", notFound);
                model.addAttribute("url", "/domains");
                return "error";
            }

            model.addAttribute("domain", domain);
            model.addAttribute("accounts", accountService.findAccountsByDomainId(id));
            model.addAttribute("newAccount", new Account("", "", -1, domain));
            return "domain_edit";
        } catch (NullPointerException | IllegalArgumentException e) {
            return "error";
        }
    }

    @PostMapping("/domain")
    public String addDomain(@ModelAttribute("new_domain") Domain newDomain, Model model) {
        newDomain.setId(null);
        List<Domain> domains = domainService.findAll();
        try {
            for (Domain domain : domains)
                if (domain.getName().equals(newDomain.getName())) {
                    model.addAttribute("message", exists);
                    model.addAttribute("url", domainsUrl);
                    return "error";
                }
            domainService.save(newDomain);
            return "redirect:" + domainsUrl;
        } catch (NullPointerException | IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/domain/update")
    public String updateName(@ModelAttribute("domain") Domain oldDomain, Model model) {
        List<Domain> domains = domainService.findAll();
        String url = "/domain?id=" + oldDomain.getId();
        try {
            for (Domain domain : domains)
                if (!domain.getId().equals(oldDomain.getId()) && domain.getName().equals(oldDomain.getName())) {
                    model.addAttribute("message", exists);
                    model.addAttribute("url", url);
                    return "error";
                }
            if (!domainService.existsById(oldDomain.getId())) {
                model.addAttribute("message", "Cannot updated non-existing domain.");
                model.addAttribute("url", url);
                return "error";
            }
            domainService.save(oldDomain);
            return "redirect:" + url;
        } catch (NullPointerException | IllegalArgumentException e) {
            return "error";
        }
    }

    @GetMapping("/domain/delete")
    public String deleteDomain(@RequestParam Long id, Model model) {
        try {
            Domain domain = domainService.findDomainById(id);

            if (domain == null) {
                model.addAttribute("message", notFound);
                model.addAttribute("url", domainsUrl);
                return "error";
            }
            domainService.delete(domain);
            return "redirect:" + domainsUrl;
        } catch (NullPointerException | IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

}