package ies.sunday_crypto.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ies.sunday_crypto.Models.Crypto;
import ies.sunday_crypto.Repository.CryptoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CryptoController {

    @Autowired
    private CryptoRepository cryptoRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "Get all coins", description = "Get a list of all coins")
    @GetMapping("/coins")
    public List<Crypto> getAllCoins() {
        return cryptoRepository.findAll();
    }

    @Operation(summary = "Get coin by ID", description = "Get a specific coin by its ID")
    @GetMapping("/coins/{id}")
    public Optional<Crypto> getCoinByID(@PathVariable String id) {
        return cryptoRepository.findById(id);
    }

    @Operation(summary = "Add coins", description = "Add a list of coins to the database")
    @ApiResponse(responseCode = "201", description = "Coins added successfully",
    content = @Content(schema = @Schema(implementation = Crypto.class)))
    @PostMapping("/coins") // /app/coins
    public Crypto addCoin(@RequestBody List<Crypto> coin) {
        System.out.println("Received data from RabbitMQ");
        System.out.println(coin.toString());
        for (Crypto crypto : coin) {
            cryptoRepository.save(crypto);
        }
        System.out.println("================= Saved to DB ===================");
        return null;
    }

    @Operation(summary = "Add a single coin", description = "Add a single coin to the database")
    @ApiResponse(responseCode = "201", description = "Coin added successfully",
            content = @Content(schema = @Schema(implementation = Crypto.class)))
    @PostMapping("/coin")
    public Crypto addCoins(@RequestBody Crypto coin) {
       
        return cryptoRepository.save(coin);
    }

    @Operation(summary = "Delete coin by ID", description = "Delete a specific coin by its ID")
    @DeleteMapping("coins/{id}")
    public void deleteCoin(@PathVariable String id) {
        cryptoRepository.deleteById(id);
    }

    @Operation(summary = "Delete all coins", description = "Delete all coin records")
    @DeleteMapping("coins")
    public void deleteAllCoins() {
        cryptoRepository.deleteAll();
    }

}
