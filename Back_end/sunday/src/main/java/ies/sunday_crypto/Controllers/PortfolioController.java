package ies.sunday_crypto.Controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.CoinMoney;
import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Models.Investor;
import ies.sunday_crypto.Models.Key;
import ies.sunday_crypto.Models.Name;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Repository.CryptoRepository;
import ies.sunday_crypto.Repository.InvestorRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private InvestorRepository investorRepository;

    private static int counter = 1;

    @Operation(summary = "Get portfolios by investor ID", description = "Get a list of portfolios for a specific investor by ID")
    @GetMapping("/investor/{investorId}")
    public List<Portfolio> getPortfoliosByInvestorId(@PathVariable Long investorId) {
        System.out.println(portfolioRepository.findByInvestorId(investorId));
        return portfolioRepository.findByInvestorId(investorId);
    }

    @Operation(summary = "Get all portfolios", description = "Get a list of all portfolios")
    @GetMapping("/all")
    public List<Portfolio> getAll() {
        return portfolioRepository.findAll();
    }

    @Operation(summary = "Add a new portfolio", description = "Add a new portfolio")
    @ApiResponse(responseCode = "201", description = "Portfolio added successfully", content = @Content(schema = @Schema(implementation = Portfolio.class)))
    @PostMapping("/add")
    public Portfolio addPortfolio(@RequestBody Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    @Operation(summary = "Delete portfolio by ID", description = "Delete a specific portfolio by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePortfolio(@PathVariable Long id) {
        portfolioRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Delete all portfolios", description = "Delete all portfolio records")
    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deletePortfolio() {
        portfolioRepository.deleteAll();
        ;
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Insert coin into portfolio", description = "Insert a coin into a specific portfolio")
    @PostMapping("/insertcoin/{key}")
    public ResponseEntity<HttpStatus> insertCoin(@RequestBody CoinMoney c, @PathVariable String key) {
        try {
            System.out.println("{ " + c.getMoney() + " " + c.getCoin().getId() + " }");
            if (c.getCoin() == null) {
                return ResponseEntity.ok(HttpStatus.NOT_FOUND);
            }
            Optional<Portfolio> portfolioData = portfolioRepository.findByPortfolioKey(key);
            if (portfolioData.isPresent()) {
                Portfolio _portfolio = portfolioData.get();
                System.out.println(_portfolio);
                HashMap<String, Crypto> s = new HashMap<>();
                List<String> sx = new ArrayList<>();
                for (Crypto cry : _portfolio.getAssets().keySet()) {
                    sx.add(cry.getId());
                    s.put(cry.getId(), cry);
                }
                System.out.println(sx);
                if (sx.contains(c.getCoin().getId())) {
                    System.out.println(s.get(c.getCoin().getId()));
                    Map<Crypto, Double> h = _portfolio.getAssets();
                    System.out.println(Double.parseDouble(c.getCoin().getCurrent_price()));
                    Optional<Investor> x = investorRepository.findById(_portfolio.getInvestorId());
                    Investor x2 = x.get();
                    Double m = x2.getBalance();
                    System.out.println("Balance  " + m);
                    if (m >= c.getMoney()) {
                        h.put(s.get(c.getCoin().getId()), h.get(s.get(c.getCoin().getId()))
                                + (c.getMoney() / Double.parseDouble(c.getCoin().getCurrent_price())));
                        x2.setBalance(x2.getBalance() - c.getMoney());
                        investorRepository.save(x2);
                    } else {
                        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
                    }
                    _portfolio.setAssets(h);
                    System.out.println(s.get(c.getCoin().getId()));
                    portfolioRepository.save(_portfolio);
                    return ResponseEntity.ok(HttpStatus.ACCEPTED);

                } else {
                    System.out.println(Double.parseDouble(c.getCoin().getCurrent_price()));
                    Optional<Investor> x = investorRepository.findById(_portfolio.getInvestorId());
                    Investor x2 = x.get();
                    Double m = x2.getBalance();
                    System.out.println("Balance  " + m);
                    if (m >= c.getMoney()) {
                        _portfolio.getAssets().put(c.getCoin(),
                                c.getMoney() / Double.parseDouble(c.getCoin().getCurrent_price()));
                        x2.setBalance(x2.getBalance() - c.getMoney());
                        investorRepository.save(x2);
                    } else {
                        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
                    }
                    _portfolio.getAssets().put(c.getCoin(),
                            c.getMoney() / Double.parseDouble(c.getCoin().getCurrent_price()));
                    portfolioRepository.save(_portfolio);
                    return ResponseEntity.ok(HttpStatus.ACCEPTED);
                }

            }
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/sellcoin/{key}")
    public ResponseEntity<HttpStatus> sellCoin(@RequestBody CoinMoney c, @PathVariable String key) {
        System.out.println("{ " + c.getMoney() + " " + c.getCoin().getId() + " }");
        if (c.getCoin() == null) {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
        Optional<Portfolio> portfolioData = portfolioRepository.findByPortfolioKey(key);
        if (portfolioData.isPresent()) {
            Portfolio _portfolio = portfolioData.get();
            System.out.println(_portfolio);
            HashMap<String, Crypto> s = new HashMap<>();
            List<String> sx = new ArrayList<>();
            for (Crypto cry : _portfolio.getAssets().keySet()) {
                sx.add(cry.getId());
                s.put(cry.getId(), cry);
            }
            System.out.println(sx);
            if (sx.contains(c.getCoin().getId())) {
                System.out.println(s.get(c.getCoin().getId()));
                Map<Crypto, Double> h = _portfolio.getAssets();
                System.out.println(Double.parseDouble(c.getCoin().getCurrent_price()));
                System.out.println(h.get(s.get(c.getCoin().getId())));
                if (h.get(s.get(c.getCoin().getId())) < (c.getMoney()
                        / Double.parseDouble(c.getCoin().getCurrent_price()))) {
                    return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
                }
                Optional<Investor> x = investorRepository.findById(_portfolio.getInvestorId());
                Investor x2 = x.get();
                x2.setBalance(x2.getBalance() + c.getMoney());
                investorRepository.save(x2);
                h.put(s.get(c.getCoin().getId()), h.get(s.get(c.getCoin().getId()))
                        - (c.getMoney() / Double.parseDouble(c.getCoin().getCurrent_price())));
                _portfolio.setAssets(h);
                System.out.println(s.get(c.getCoin().getId()));
                portfolioRepository.save(_portfolio);
                return ResponseEntity.ok(HttpStatus.ACCEPTED);

            } else {
                return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
            }

        }
        return ResponseEntity.ok(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Update portfolio name", description = "Update the name of a specific portfolio")
    @PostMapping("updateName/{key}")
    public ResponseEntity<HttpStatus> insertName(@PathVariable String key, @RequestBody Name name) {
        System.out.println(name.getName());
        Optional<Portfolio> x = portfolioRepository.findByPortfolioKey(key);
        if (x.isPresent()) {
            Portfolio _portfolio = x.get();
            _portfolio.setName(name.getName());
            portfolioRepository.save(_portfolio);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Import portfolio", description = "Import an existing portfolio using a key")
    @PostMapping("importPort/{id}")
    public ResponseEntity<HttpStatus> impoertPort(@PathVariable Long id, @RequestBody Key key) {
        System.out.println(key.getKey());
        Optional<Portfolio> x = portfolioRepository.findByPortfolioKey(key.getKey());
        if (x.isPresent()) {
            Portfolio _portfolio = x.get();
            _portfolio.setIdInvestor(id);
            portfolioRepository.save(_portfolio);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.ok(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a new portfolio for an investor", description = "Add a new portfolio for a specific investor")
    @PostMapping("addPortfolio/{id}")
    public ResponseEntity<HttpStatus> addPort(@PathVariable Long id) throws NoSuchAlgorithmException {
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
        portfolio.setIdInvestor(id);
        portfolio.setName("Default" + counter);
        counter++;
        portfolio.setAssets(new HashMap<Crypto, Double>());
        portfolioRepository.save(portfolio);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
