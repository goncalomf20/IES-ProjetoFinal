package ies.sunday_crypto.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Services.EmailService;


@RestController
@RequestMapping("/mail")
@CrossOrigin(origins = "*")
public class EmailSendController {


    private EmailService emailService;

    public EmailSendController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendEmail(String to , String subject , String body) {
        System.out.println(to);
        System.out.println(subject);
        System.out.println(body);
        return emailService.sendEmail(to, subject, body);
    }
}
