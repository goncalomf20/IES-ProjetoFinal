package ies.sunday_crypto.Services;


public interface EmailService {

    String sendEmail(String to, String subject, String body);
    
}
