package ies.sunday_crypto.Controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.Admin;
import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Models.LoginDTO;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Models.Token;
import ies.sunday_crypto.Repository.AdminRepository;
import ies.sunday_crypto.Repository.CryptoRepository;
import ies.sunday_crypto.Repository.InvestorRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import io.jsonwebtoken.Jwts;
@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private AdminRepository adminRepository;


    @PostMapping("/token")
    public ResponseEntity<Admin> token(@RequestBody Token token) {
    System.out.println("Token: " + token.getToken());
    Optional<Admin> adminData = adminRepository.findByToken(token.getToken());
    System.err.println(adminData);
        if (adminData.isPresent()) {
            return new ResponseEntity<>(adminData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/del")
    public void deleteAllAdmins() {
        adminRepository.deleteAll();
    }
    


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) throws NoSuchAlgorithmException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Admin admin = adminRepository.findByUsername(username);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }

        if (!password.equals(admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }

        String jwtToken = generateJwtToken(admin);
        admin.setToken(jwtToken);
        adminRepository.save(admin);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody Admin a) throws NoSuchAlgorithmException {
        Admin existingInvestor = adminRepository.findByUsername(a.getUsername());
        if (existingInvestor != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        String jwtToken = generateJwtToken(a);
        a.setToken(jwtToken);
        adminRepository.save(a);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/all")
    public List<Admin> allAdmins(){
     return adminRepository.findAll();   
    }
    
    public String generateJwtToken(Admin admin) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(3600); // 1 hour expiry
    
        // Generate a unique session identifier (for example, a UUID)
        String sessionId = UUID.randomUUID().toString();
    
        return Jwts.builder()
                .claim("sub", admin.getUsername())
                .claim("sessionId", sessionId) // Include the unique session identifier
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .compact();
    }

    @PostMapping("admin/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("id") Long id ) {
        Optional<Admin> adminData = adminRepository.findById(id);

        if (adminData.isPresent()) {
            return new ResponseEntity<>(adminData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
    }
}
