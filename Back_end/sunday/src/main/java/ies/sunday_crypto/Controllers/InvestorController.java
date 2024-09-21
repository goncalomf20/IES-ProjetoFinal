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

import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Models.LoginDTO;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Models.Token;
import ies.sunday_crypto.Repository.CryptoRepository;
import ies.sunday_crypto.Repository.InvestorRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/investor")
public class InvestorController {


    @Autowired
    private InvestorRepository investorRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private CryptoRepository cryptoRepository;

    @Operation(summary = "Get investor by token", description = "Get investor details by token")
    @PostMapping("/token")
    public ResponseEntity<Investor> token(@RequestBody Token token) {
    System.out.println("Token: " + token.getToken());
    Optional<Investor> investorData = investorRepository.findByToken(token.getToken());
    System.err.println(investorData);
        if (investorData.isPresent()) {
            return new ResponseEntity<>(investorData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(summary = "Delete all investors", description = "Delete all investor records")
    @DeleteMapping("/del")
    public void deleteAllInvestors() {
        investorRepository.deleteAll();
    }
    

    @Operation(summary = "Login", description = "Authenticate and generate JWT token for an investor")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginRequest) throws NoSuchAlgorithmException {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Investor investor = investorRepository.findByUsername(username);
        if (investor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Investor not found");
        }

        if (!password.equals(investor.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }

        String jwtToken = generateJwtToken(investor);
        investor.setToken(jwtToken);
        investorRepository.save(investor);

        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "Register investor", description = "Register a new investor and generate JWT token")
    @PostMapping("/register")
    public ResponseEntity<String> registerInvestor(@RequestBody Investor i) throws NoSuchAlgorithmException {
        Investor existingInvestor = investorRepository.findByUsername(i.getUsername());
        if (existingInvestor != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        String jwtToken = generateJwtToken(i);
        i.setToken(jwtToken);
        investorRepository.save(i);
        Portfolio portfolio = new Portfolio();
        SecureRandom secureRandom = new SecureRandom();
        String randomString = new BigInteger(130, secureRandom).toString(32);

        // Hash the random string
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(randomString.getBytes());

        // Convert the hash to a hexadecimal string
        String hashString = bytesToHex(hash);

        // Take a substring of the hash string to get a key of a specific length
        String key = hashString.substring(0, 64);

        portfolio.setPortfolioKey(key);
        portfolio.setIdInvestor(i.getId());

        portfolio.setAssets(new HashMap<Crypto,Double>());

        portfolioRepository.save(portfolio);

        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "Get all investors", description = "Get a list of all investors")
    @GetMapping("/all")
    public List<Investor> allInvestors(){
     return investorRepository.findAll();   
    }
    
    public String generateJwtToken(Investor investor) {
        Instant now = Instant.now();
        Instant expiryDate = now.plusSeconds(3600); // 1 hour expiry
    
        // Generate a unique session identifier (for example, a UUID)
        String sessionId = UUID.randomUUID().toString();
    
        return Jwts.builder()
                .claim("sub", investor.getUsername())
                .claim("sessionId", sessionId) // Include the unique session identifier
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryDate))
                .compact();
    }

    @Operation(summary = "Get investor by ID", description = "Get investor details by ID")
    @PostMapping("investors/{id}")
    public ResponseEntity<Investor> getInvestorById(@PathVariable("id") Long id ) {
        Optional<Investor> investorData = investorRepository.findById(id);

        if (investorData.isPresent()) {
            return new ResponseEntity<>(investorData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
    }

    @Operation(summary = "Add money to investor's balance", description = "Add money to the balance of a specific investor")
    @PostMapping("addmoney/{id}")
    public ResponseEntity<Boolean> addBalance(@PathVariable("id") Long id , @RequestBody double money){
        Optional<Investor> investorData = investorRepository.findById(id);
        if (investorData.isPresent()) {
            Investor i = investorData.get();
            i.setBalance(i.getBalance() + money);
            investorRepository.save(i);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


}
