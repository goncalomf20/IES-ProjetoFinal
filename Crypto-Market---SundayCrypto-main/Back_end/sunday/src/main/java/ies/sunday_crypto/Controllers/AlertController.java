package ies.sunday_crypto.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.Alert;
import ies.sunday_crypto.Models.Portfolio;
import ies.sunday_crypto.Repository.AlertRepository;
import ies.sunday_crypto.Repository.PortfolioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "*")
public class AlertController {

    @Autowired
    private AlertRepository alertRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Operation(summary = "Get all alerts", description = "Get a list of all alerts")
    @GetMapping("/getAlert")
    public List<Alert> getAllAlert() {
        return alertRepository.findAll();
    }

    @Operation(summary = "Get alert by ID", description = "Get a specific alert by its ID")
    @GetMapping("/getAlert/{id}")
    public List<Alert>  getAlertByID(@PathVariable int id) {

        List<Portfolio> ports = portfolioRepository.findByInvestorId((long) id);

        List<Alert> alerts = new ArrayList<>();
        for (Portfolio port : ports) {
            alerts.addAll(alertRepository.findByPortfolioid(port.getPortfolioKey()));
        }

        return alerts;
    }

    @Operation(summary = "Add a new alert", description = "Add a new alert")
    @ApiResponse(responseCode = "201", description = "Alert created successfully",
    content = @Content(schema = @Schema(implementation = Alert.class)))
    @PostMapping("/getAlert")
    public Alert addAlert(@RequestBody Alert coin) {
        Alert newAlert = new Alert();   
        newAlert.setPortfolioid(coin.getPortfolioid());
        newAlert.setCoinid(coin.getCoinid());
        newAlert.setAmount_to_exange(coin.getAmount_to_exange());
        newAlert.setLimit_value(coin.getLimit_value());
        newAlert.setSell_buy(coin.getSell_buy());
        newAlert.setCoinValueWhenAlert(coin.getCoinValueWhenAlert());
        newAlert.setIsDone(coin.getIsDone());
        newAlert.setIsPossible(coin.getIsPossible());

        return alertRepository.save(coin);
    }

    @Operation(summary = "Delete alert by ID", description = "Delete a specific alert by its ID")
    @DeleteMapping("getAlert/{id}")
    public void deleteAlert(@PathVariable int id) {
        alertRepository.deleteById(id);
    }

    @Operation(summary = "Delete all alerts", description = "Delete all alert records")
    @DeleteMapping("getAlert")
    public void deleteAllAlert() {
        alertRepository.deleteAll();
    }
    
}