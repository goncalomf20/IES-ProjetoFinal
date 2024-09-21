package ies.sunday_crypto.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public String sendEmail(String to, String subject, String body) {
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);

            System.out.println(to);
            mimeMessageHelper.setTo(to);

            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(body);

            javaMailSender.send(mimeMessage);

            return "Email Sent Successfully";
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
}
