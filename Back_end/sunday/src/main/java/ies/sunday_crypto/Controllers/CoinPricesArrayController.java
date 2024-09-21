package ies.sunday_crypto.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.CoinPricesArray;
import ies.sunday_crypto.Services.CoinPriceArrayService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CoinPricesArrayController {
    
    private final CoinPriceArrayService coinPriceArrayService;

    @Autowired
    public CoinPricesArrayController(CoinPriceArrayService coinPriceArrayService) {
        this.coinPriceArrayService = coinPriceArrayService;
    }

    @Operation(summary = "Get CoinPricesArrayTime to a specific coin", description = "Get the array [[data, price]] of the last 24h for a specific coin")
    @GetMapping("/coinsArrayTime/{id}")
    public List<Object> getCoinArrayTime(@PathVariable String id) {
        CoinPricesArray coinPricesArray = coinPriceArrayService.getArrayByCoin(id).orElse(null);
        if (coinPricesArray != null) {
            return coinPricesArray.getStatusJson();
        }
        return null;
    }

    @Operation(summary = "Get all CoinPricesArrayTime", description = "Get the array [[data, price]] of the last 24h for every coin")
    @GetMapping("/allCoinsArrayTime")
    public List<CoinPricesArray> getAllCoinsArrayTime() {
        return coinPriceArrayService.getAllCoinsArrayTime();
    }
}