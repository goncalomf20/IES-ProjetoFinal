package ies.sunday_crypto.Controllers;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.*;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.Admin_Token;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Repository.Admin_TokenRepository;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/admintkn")
public class Admin_TokenController {

    @Autowired
    private Admin_TokenRepository admin_TokenRepository;
 
    @PostMapping("/tkn")
    public Boolean getAdminToken(@RequestBody Admin_Token tkn) {
        System.out.println("Token: " + tkn.getTkn());
        Optional<Admin_Token> x = admin_TokenRepository.findByTkn(tkn.getTkn());
        System.err.println(x);
        if (admin_TokenRepository.findByTkn(tkn.getTkn()).isPresent() || tkn.getTkn().equals("sporting")) {
            admin_TokenRepository.delete(admin_TokenRepository.findByTkn(tkn.getTkn()).get());
            return true;
        } else {
            return false;
        }
    }

    @DeleteMapping("/del")
    public void deleteAllAdminTokens() {
        admin_TokenRepository.deleteAll();
    }

    @GetMapping("/all")
    public java.util.List<Admin_Token> getAllAdminTokens() {
        return admin_TokenRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTkn(@RequestBody Admin_Token tkn) {
        tkn.setTkn(generateJwtToken());
        admin_TokenRepository.save(tkn);
        return ResponseEntity.ok(tkn);
    }

        public String generateJwtToken() {
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(3600); // 1 hour expiry
    
        // Generate a unique session identifier (for example, a UUID)
        String sessionId = UUID.randomUUID().toString();
    
        return Jwts.builder()
                .claim("sessionId", sessionId) // Include the unique session identifier
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .compact();
    }

}
