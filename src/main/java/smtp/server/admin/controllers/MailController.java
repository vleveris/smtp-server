package smtp.server.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import smtp.server.admin.models.Account;
import smtp.server.admin.models.Mail;
import smtp.server.admin.services.IAccountService;
import smtp.server.admin.services.IMailService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class MailController {
    @Autowired
    private IMailService mailService;
    @Autowired
    private IAccountService accountService;

    @GetMapping("/mail/{accountId}")
    public String getAccountMail(@PathVariable Long accountId, Model model) {
        try {
            Account account = accountService.findAccountById(accountId);
            String username = account.getUsername();
            Set<Mail> mails = account.getMails();
            Long id = account.getId();
            model.addAttribute("mails", mails);
            model.addAttribute("username", username);
            model.addAttribute("accountId", id);
            return "account_mail";
        } catch (IllegalArgumentException | NullPointerException e) {
            return "error";
        }
    }

    //@GetMapping(value = "/mail/{accountId}/{mailId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @GetMapping("/mail/{accountId}/{mailId}")
    public ResponseEntity<String> downloadMail(@PathVariable Long accountId, @PathVariable Long mailId) {
        Account account = accountService.findAccountById(accountId);
        Set<Account> accounts = new HashSet<>();
        accounts.add(account);
        Mail mail = mailService.findMailByAccountsInAndId(accounts, mailId);
        String message = mail.getMessage();
        MediaType media = MediaType.APPLICATION_OCTET_STREAM;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=mail.eml")
                .contentType(media)
                .contentLength(message.length())
                .body(message);
    }
}
