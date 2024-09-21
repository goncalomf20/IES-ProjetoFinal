package ies.sunday_crypto.Controllers;

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

import ies.sunday_crypto.Models.CoinPricesArray7d;
import ies.sunday_crypto.Repository.CoinPriceArray7dRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CoinPriceArray7dController {

    @Autowired
    private CoinPriceArray7dRepository coinPriceArray7dRepository;

    @Operation(summary = "Get all CoinPricesArray7d", description = "Get the array [[data, price]] associated with the relative coin for the last 7 days for every coin")
    @GetMapping("/array7d")
    public List<CoinPricesArray7d> getAllArrays() {
        return coinPriceArray7dRepository.findAll();
    }

    @Operation(summary = "Get CoinPricesArray7d by ID", description = "Get the array [[data, price]] for the last 7 days for a specific coin")
    @GetMapping("/array7d/{id}")
    public Optional<CoinPricesArray7d> getArrayByCoin(@PathVariable String id) {
        return coinPriceArray7dRepository.findById(id);
    }

    @Operation(summary = "Add a new CoinPricesArray7d", description = "Add a new array of the last 7 days for a specific coin")
    @ApiResponse(responseCode = "201", description = "CoinPricesArray7d created successfully",
    content = @Content(schema = @Schema(implementation = CoinPricesArray7d.class)))
    @PostMapping("/array7d")
    public CoinPricesArray7d addCoinArray(@RequestBody CoinPricesArray7d coin) {
       
        return coinPriceArray7dRepository.save(coin);
    }

    @Operation(summary = "Delete CoinPricesArray7d by ID", description = "Delete a specific CoinPricesArray7d by its ID")
    @DeleteMapping("array7d/{id}")
    public void deleteArray(@PathVariable String id) {
        coinPriceArray7dRepository.deleteById(id);
    }

    @Operation(summary = "Delete all CoinPricesArray7d", description = "Delete all CoinPricesArray7d records")
    @DeleteMapping("array7d")
    public void deleteAllCoins() {
        coinPriceArray7dRepository.deleteAll();
    }
    
}


    

